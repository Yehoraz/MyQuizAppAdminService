package com.MyQuizAppAdminService.rest;

import java.util.List;

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
import com.MyQuizAppAdminService.exceptions.ExistsException;
import com.MyQuizAppAdminService.exceptions.InvalidInputException;
import com.MyQuizAppAdminService.exceptions.NotExistsException;
import com.MyQuizAppAdminService.services.AdminService;

@RestController
@Lazy
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping("/addQuestion")
	public ResponseEntity<?> addQuestion(@RequestBody Question question) {
		try {
			adminService.addQuestion(question);
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question already exists");
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Question added");
	}

	@PutMapping("/updateQuestion")
	public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
		try {
			adminService.updateQuestion(question);
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@DeleteMapping("/removeQuestion/{questionId}")
	public ResponseEntity<?> removeQuestion(@PathVariable long questionId) {
		try {
			adminService.deleteQuestion(questionId);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/getQuestion/{questionId}")
	public ResponseEntity<?> getQuestion(@PathVariable long questionId) {
		Question question = null;
		try {
			question = adminService.getQuestion(questionId);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(question);
	}

	@GetMapping("getAllQuestions")
	public ResponseEntity<?> getAllQuestions() {
		List<Question> questions = null;
		try {
			questions = adminService.getAllQuestions();
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(questions);
	}

	// Suggested
	// section***************************************************************

	@PostMapping("/addSuggestedQuestion")
	public ResponseEntity<?> addSuggestedQuestion(@RequestBody SuggestedQuestion suggestedQuestion) {
		try {
			adminService.approveSuggestedQuestion(suggestedQuestion);
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question already exists");
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid suggested question input");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Question approved");
	}

	@PostMapping("/addAllSuggestedQuestions")
	public ResponseEntity<?> addAllSuggestedQuestions() {
		try {
			adminService.approveAllSuggestedQuestions();
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("There are no suggested questions to approve");
		}
		return ResponseEntity.status(HttpStatus.OK).body("All the suggested questions approved");
	}

	@DeleteMapping("/deleteSuggestedQuestion/{sqId}")
	public ResponseEntity<?> deleteSuggestedQuestion(@PathVariable("sqID") long suggestedQuestionId) {
		try {
			adminService.deleteSuggestedQuestion(suggestedQuestionId);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@DeleteMapping("/deleteAllSuggestedQuestions")
	public ResponseEntity<?> deleteAllSuggestedQuestions() {
		try {
			adminService.deleteAllSuggestedQuestions();
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("/getSuggestedQuestion/{sQuestionId}")
	public ResponseEntity<?> getSuggestedQuestion(@PathVariable long sQuestionId) {
		SuggestedQuestion sQuestion = null;
		try {
			sQuestion = adminService.getSuggestedQuestion(sQuestionId);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(sQuestion);
	}

	@GetMapping("/getAllSuggestedQuestions")
	public ResponseEntity<?> getAllSuggestedQuestions() {
		List<SuggestedQuestion> sQuestions = null;
		try {
			sQuestions = adminService.getAllSuggestedQuestions();
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(sQuestions);
	}
}
