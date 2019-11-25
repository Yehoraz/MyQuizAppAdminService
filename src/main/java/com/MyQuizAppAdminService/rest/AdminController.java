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

	private Question question = null;
	private SuggestedQuestion suggestedQuestion = null;

	// Question
	// section************************************************************************************

	@PostMapping("/addQuestion")
	public ResponseEntity<?> addQuestion(@RequestBody Question question) {
		restartVariables();
		if (ValidationUtil.validationCheck(question)) {
			questionService.addQuestion(question);
			return ResponseEntity.status(HttpStatus.OK).body("Question added");
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@PutMapping("/updateQuestion")
	public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
		restartVariables();
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
		restartVariables();
		try {
			questionService.removeQuestion(questionId);
			return ResponseEntity.status(HttpStatus.OK).body("Question removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question does not exists");
		}
	}

	@GetMapping("/getQuestion/{questionId}")
	public ResponseEntity<?> getQuestion(@PathVariable long questionId) {
		restartVariables();
		question = questionService.getQuestionById(questionId);
		if (question != null) {
			return ResponseEntity.status(HttpStatus.OK).body(question);
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("getAllQuestions")
	public ResponseEntity<?> getAllQuestions() {
		restartVariables();
		List<Question> questions = questionService.getAllQuestions();
		if (questions != null) {
			return ResponseEntity.status(HttpStatus.OK).body((List<Question>) Hibernate.unproxy(questions));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	// Suggested
	// section***************************************************************

	@PostMapping("/addSuggestedQuestion")
	public ResponseEntity<?> addSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
		restartVariables();
		if (ValidationUtil.validationCheck(suggestedQuestion)) {
			suggestedQuestion.getQuestion().setApproved(true);
			try {
				suggestedQuestionService.removeSuggestedQuestion(suggestedQuestion.getId());
				questionService.addQuestion(suggestedQuestion.getQuestion());
				return ResponseEntity.status(HttpStatus.OK).body("Question added");
			} catch (EntityNotFoundException e) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("This Suggested question does not exists");
			}
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@PostMapping("/addAllSuggestedQuestions")
	public ResponseEntity<?> addAllSuggestedQuestions() {
		restartVariables();
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
		restartVariables();
		try {
			suggestedQuestionService.removeSuggestedQuestion(suggestedQuestionId);
			return ResponseEntity.status(HttpStatus.OK).body("Suggested question removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Suggested question does not exists");
		}
	}

	@DeleteMapping("/deleteAllSuggestedQuestions")
	public ResponseEntity<?> deleteAllSuggestedQuestions() {
		restartVariables();
		try {
			suggestedQuestionService.removeAllSuggestedQuestions();
			return ResponseEntity.status(HttpStatus.OK).body("All suggested questions has been removed");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("There are no suggested questions");
		}
	}

	@GetMapping("/getSuggestedQuestion/{sQuestionId}")
	public ResponseEntity<?> getSuggestedQuestion(@PathVariable long sQuestionId) {
		restartVariables();
		suggestedQuestion = suggestedQuestionService.getSuggestedQuestion(sQuestionId);
		if (suggestedQuestion != null) {
			return ResponseEntity.status(HttpStatus.OK).body(suggestedQuestion);
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("/getSuggestedQuestions")
	public ResponseEntity<?> getAllSuggestedQuestions() {
		restartVariables();
		List<SuggestedQuestion> suggestedQuestions = suggestedQuestionService.getAllSuggestedQuestions();
		if (suggestedQuestions != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body((List<SuggestedQuestion>) Hibernate.unproxy(suggestedQuestions));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	private void restartVariables() {
		question = null;
		suggestedQuestion = null;
	}

}
