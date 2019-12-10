package com.MyQuizAppAdminService.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MyQuizAppAdminService.beans.Question;
import com.MyQuizAppAdminService.beans.SuggestedQuestion;
import com.MyQuizAppAdminService.exceptions.ExistsException;
import com.MyQuizAppAdminService.exceptions.InvalidInputException;
import com.MyQuizAppAdminService.exceptions.NotExistsException;
import com.MyQuizAppAdminService.repos.QuestionRepository;
import com.MyQuizAppAdminService.repos.SuggestedQuestionRepository;
import com.MyQuizAppAdminService.utils.ValidationUtil;

@Service
public class AdminService {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private SuggestedQuestionRepository sQuestionRepository;

	// Question
	// section************************************************************************************

	public void addQuestion(Question question) throws ExistsException, InvalidInputException {
		question.setApproved(true);
		if (ValidationUtil.validationCheck(question)) {
			if (!questionRepository.existsByQuestionTextAndIsApproved(question.getQuestionText(), true)) {
				questionRepository.save(question);
			} else {
				throw new ExistsException(question, 0, "Question already exists");
			}
		} else {
			throw new InvalidInputException(question, 0, "Invalid question input");
		}
	}

	public void updateQuestion(Question question) throws InvalidInputException, ExistsException, NotExistsException {
		question.setApproved(true);
		if (ValidationUtil.validationCheck(question)) {
			if (!questionRepository.existsByQuestionTextAndIsApproved(question.getQuestionText(), true)) {
				Question question2 = questionRepository.findById(question.getId()).orElse(null);
				if (question2 != null) {
					question.setId(question2.getId());
					questionRepository.save(question);
				} else {
					throw new NotExistsException(null, question.getId(), "Question with this id don't exists");
				}
			} else {
				throw new ExistsException(question, 0, "Question already exists");
			}
		} else {
			throw new InvalidInputException(question, 0, "Invalid question input");
		}
	}

	public void deleteQuestion(long questionId) throws NotExistsException {
		if (questionRepository.existsById(questionId)) {
			Question question = questionRepository.findById(questionId).orElse(null);
			question.setApproved(false);
			question.setQuestionText("remove");
			questionRepository.save(question);
		} else {
			throw new NotExistsException(null, questionId, "Question with this id don't exists");
		}
	}

	public Question getQuestion(long questionId) throws NotExistsException {
		Question question = questionRepository.findById(questionId).orElse(null);
		if (question != null) {
			return question;
		} else {
			throw new NotExistsException(null, questionId, "Question with this id don't exists");
		}
	}

	public List<Question> getAllQuestions() throws NotExistsException {
		List<Question> questions = questionRepository.findByIsApproved(true);
		if (questions != null) {
			return questions;
		} else {
			throw new NotExistsException(null, 0, "There are no questions");
		}
	}

	// Suggested
	// section***************************************************************

	public void approveSuggestedQuestion(SuggestedQuestion suggestedQuestion)
			throws ExistsException, InvalidInputException {
		if (ValidationUtil.validationCheck(suggestedQuestion)) {
			suggestedQuestion.getQuestion().setApproved(true);
			if (sQuestionRepository.existsById(suggestedQuestion.getId())) {
				sQuestionRepository.deleteById(suggestedQuestion.getId());
			} else {
				// logger!
			}
			if (!questionRepository.existsByQuestionTextAndIsApproved(suggestedQuestion.getQuestion().getQuestionText(),
					true)) {
				questionRepository.save(suggestedQuestion.getQuestion());
			} else {
				throw new ExistsException(suggestedQuestion.getQuestion(), 0, "Question already exists");
			}
		} else {
			throw new InvalidInputException(suggestedQuestion, 0, "Invalid suggested question input");
		}
	}

	public void approveAllSuggestedQuestions() throws NotExistsException {
		List<SuggestedQuestion> suggestedQuestions = sQuestionRepository.findAll();
		if (suggestedQuestions != null) {
			suggestedQuestions.forEach(sq -> {
				sq.getQuestion().setApproved(true);
				if (sQuestionRepository.existsById(sq.getId())) {
					sQuestionRepository.deleteById(sq.getId());
				} else {
					// logger!
				}
				if (!questionRepository.existsByQuestionTextAndIsApproved(sq.getQuestion().getQuestionText(), true)) {
					questionRepository.save(sq.getQuestion());
				} else {
					// logger!
				}
			});
		} else {
			throw new NotExistsException(null, 0, "There are no suggested questions");
		}
	}

	public void deleteSuggestedQuestion(long sqID) throws NotExistsException {
		if (sQuestionRepository.existsById(sqID)) {
			sQuestionRepository.deleteById(sqID);
		} else {
			throw new NotExistsException(null, sqID, "Suggested question with this id don't exists");
		}
	}

	public void deleteAllSuggestedQuestions() throws NotExistsException {
		if (sQuestionRepository.count() > 0) {
			sQuestionRepository.deleteAll();
		} else {
			throw new NotExistsException(null, 0, "There are no suggested questions");
		}
	}

	public SuggestedQuestion getSuggestedQuestion(long sqID) throws NotExistsException {
		SuggestedQuestion sQuestion = sQuestionRepository.findById(sqID).orElse(null);
		if (sQuestion != null) {
			return sQuestion;
		} else {
			throw new NotExistsException(null, sqID, "Suggested question with this id don't exists");
		}
	}

	public List<SuggestedQuestion> getAllSuggestedQuestions() throws NotExistsException {
		List<SuggestedQuestion> sQuestions = sQuestionRepository.findAll();
		if (sQuestions != null) {
			return sQuestions;
		} else {
			throw new NotExistsException(null, 0, "There are no suggested questions");
		}
	}

}
