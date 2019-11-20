package com.MyQuizAppAdminService.rest;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.MyQuizAppAdminService.beans.Question;
import com.MyQuizAppAdminService.beans.SuggestedQuestion;
import com.MyQuizAppAdminService.services.QuestionService;
import com.MyQuizAppAdminService.services.SuggestedQuestionService;
import com.MyQuizAppAdminService.utils.ValidationUtil;

@RestController
@Lazy
public class AdminController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private SuggestedQuestionService suggestedQuestionService;

	@PostMapping("/addQuestion")
	public ResponseEntity<?> addQuestion(@RequestBody Question question) {
		if (ValidationUtil.validationCheck(question)) {
			questionService.addQuestion(question);
			return ResponseEntity.status(HttpStatus.OK).body("Question added");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@PutMapping("/updateQuestion")
	public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
		if (ValidationUtil.validationCheck(question)) {
			try {
				questionService.updateQuestion(question);
			} catch (EntityNotFoundException e) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question does not exists");
			}
			return ResponseEntity.status(HttpStatus.OK).body("Question updated");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@DeleteMapping("/removeQuestion/{questionId}")
	public ResponseEntity<?> removeQuestion(@PathVariable long questionId) {
		try {
			questionService.removeQuestion(questionId);
			return ResponseEntity.status(HttpStatus.OK).body("Question removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question does not exists");
		}
	}

	@GetMapping("/getQuestion/{questionId}")
	public ResponseEntity<?> getQuestion(@PathVariable long questionId) {
		Question question = questionService.getQuestionById(questionId);
		if (question != null) {
			return ResponseEntity.status(HttpStatus.OK).body(question);
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("getAllQuestions")
	public ResponseEntity<?> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		if (questions != null) {
			return ResponseEntity.status(HttpStatus.OK).body((List<Question>) Hibernate.unproxy(questions));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("/getSuggestedQuestions")
	public ResponseEntity<?> getAllSuggestedQuestions() {
		List<SuggestedQuestion> suggestedQuestions = suggestedQuestionService.getAllSuggestedQuestions();
		if (suggestedQuestions != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body((List<SuggestedQuestion>) Hibernate.unproxy(suggestedQuestions));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Thre are no suggested questions");
		}
	}

	@PostMapping("/addSuggestedQuestion")
	public ResponseEntity<?> addSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
		if (ValidationUtil.validationCheck(suggestedQuestion)) {
			suggestedQuestion.getQuestion().setApproved(true);
			suggestedQuestionService.removeSuggestedQuestion(suggestedQuestion.getId());
			questionService.addQuestion(suggestedQuestion.getQuestion());
			return ResponseEntity.status(HttpStatus.OK).body("Question added");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@PostMapping("/addAllSuggestedQuestions")
	public ResponseEntity<?> addAllSuggestedQuestions() {
		List<SuggestedQuestion> suggestedQuestions = suggestedQuestionService.getAllSuggestedQuestions();
		if (suggestedQuestions != null) {
			suggestedQuestions.forEach(sq -> {
				sq.getQuestion().setApproved(true);
				suggestedQuestionService.removeSuggestedQuestion(sq.getId());
				questionService.addQuestion(sq.getQuestion());
			});
			return ResponseEntity.status(HttpStatus.OK).body("All Suggested questions has been added");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("There are no suggested questions");
		}
	}

	@DeleteMapping("/deleteSuggestedQuestion/{sqId}")
	public ResponseEntity<?> deleteSuggestedQuestion(@PathVariable("sqID") long suggestedQuestionId) {
		try {
			suggestedQuestionService.removeSuggestedQuestion(suggestedQuestionId);
			return ResponseEntity.status(HttpStatus.OK).body("Suggested question removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Suggested question does not exists");
		}
	}

	@DeleteMapping("/deleteAllSuggestedQuestions")
	public ResponseEntity<?> deleteAllSuggestedQuestions() {
		try {
			suggestedQuestionService.removeAllSuggestedQuestions();
			return ResponseEntity.status(HttpStatus.OK).body("All suggested questions has been removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("There are no suggested questions");
		}
	}

}
