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
			return ResponseEntity.status(HttpStatus.OK).body("Question added");
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question already exists");
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid input");
		}
	}

	@PutMapping("/updateQuestion")
	public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
		try {
			adminService.updateQuestion(question);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
	}

	@DeleteMapping("/removeQuestion/{questionId}")
	public ResponseEntity<?> removeQuestion(@PathVariable long questionId) {
		try {
			adminService.deleteQuestion(questionId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
	}

	@GetMapping("/getQuestion/{questionId}")
	public ResponseEntity<?> getQuestion(@PathVariable long questionId) {
		Question question = null;
		try {
			question = adminService.getQuestion(questionId);
			return ResponseEntity.status(HttpStatus.OK).body(question);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("getAllQuestions")
	public ResponseEntity<?> getAllQuestions() {
		List<Question> questions = null;
		try {
			questions = adminService.getAllQuestions();
			return ResponseEntity.status(HttpStatus.OK).body(questions);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	// Suggested
	// section***************************************************************

	@PostMapping("/addSuggestedQuestion")
	public ResponseEntity<?> addSuggestedQuestion(@RequestBody SuggestedQuestion suggestedQuestion) {
		try {
			adminService.approveSuggestedQuestion(suggestedQuestion);
			return ResponseEntity.status(HttpStatus.OK).body("Question approved");
		} catch (ExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question already exists");
		} catch (InvalidInputException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invalid suggested question input");
		}
	}

	@PostMapping("/addAllSuggestedQuestions")
	public ResponseEntity<?> addAllSuggestedQuestions() {
		try {
			adminService.approveAllSuggestedQuestions();
			return ResponseEntity.status(HttpStatus.OK).body("All the suggested questions approved");
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("There are no suggested questions to approve");
		}
	}

	@DeleteMapping("/deleteSuggestedQuestion/{sqId}")
	public ResponseEntity<?> deleteSuggestedQuestion(@PathVariable("sqID") long suggestedQuestionId) {
		try {
			adminService.deleteSuggestedQuestion(suggestedQuestionId);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
	}

	@DeleteMapping("/deleteAllSuggestedQuestions")
	public ResponseEntity<?> deleteAllSuggestedQuestions() {
		try {
			adminService.deleteAllSuggestedQuestions();
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
		}
	}

	@GetMapping("/getSuggestedQuestion/{sQuestionId}")
	public ResponseEntity<?> getSuggestedQuestion(@PathVariable long sQuestionId) {
		SuggestedQuestion sQuestion = null;
		try {
			sQuestion = adminService.getSuggestedQuestion(sQuestionId);
			return ResponseEntity.status(HttpStatus.OK).body(sQuestion);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

	@GetMapping("/getAllSuggestedQuestions")
	public ResponseEntity<?> getAllSuggestedQuestions() {
		List<SuggestedQuestion> sQuestions = null;
		try {
			sQuestions = adminService.getAllSuggestedQuestions();
			return ResponseEntity.status(HttpStatus.OK).body(sQuestions);
		} catch (NotExistsException e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
		}
	}

}
