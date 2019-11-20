package com.MyQuizAppAdminService.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MyQuizAppAdminService.beans.SuggestedQuestion;
import com.MyQuizAppAdminService.repos.SuggestedQuestionRepository;

@Service
public class SuggestedQuestionService {

	@Autowired
	private SuggestedQuestionRepository repository;

	public void removeSuggestedQuestion(long id) throws EntityNotFoundException{
		if (repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new EntityNotFoundException();
		}
	}

	public SuggestedQuestion getSuggestedQuestion(long sQuestionId) {
			return repository.findById(sQuestionId).orElse(null);
	}

	public List<SuggestedQuestion> getAllSuggestedQuestions() {
		if (repository.count() > 0) {
			return repository.findAll();
		} else {
			return null;
		}
	}

	public void removeAllSuggestedQuestions() throws EntityNotFoundException {
		if (repository.count() > 0) {
			repository.deleteAll();
		} else {
			throw new EntityNotFoundException();
		}
	}

	private boolean validationCheck(Object obj) {
		return true;
	}
	
}
