package com.MyQuizAppAdminService.utils;

import com.MyQuizAppAdminService.beans.Answer;
import com.MyQuizAppAdminService.beans.Question;
import com.MyQuizAppAdminService.beans.SuggestedQuestion;

public class ValidationUtil {

	public static boolean validationCheck(Object obj) {
		if (obj != null) {
			if (obj instanceof SuggestedQuestion) {
				SuggestedQuestion sQuestion = (SuggestedQuestion) obj;
				if (sQuestion.getId() < 0 || sQuestion.getPlayerId() < 1 || sQuestion.getQuestion() == null) {
					return false;
				} else {
					return validationCheck(sQuestion.getQuestion());
				}
			} else if (obj instanceof Question) {
				Question question = (Question) obj;
				if (question.getCorrectAnswerId() < 0 || question.getId() < 0 || question.getQuestionText().length() < 1
						|| question.getAnswers() == null) {
					return false;
				} else {
					if (question.getAnswers().stream().filter(a -> (!validationCheck(a))).count() > 0) {
						return false;
					} else {
						return true;
					}
				}
			} else if (obj instanceof Answer) {
				Answer answer = (Answer) obj;
				if (answer.getId() < 0 || answer.getAnswerText().length() < 1) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
