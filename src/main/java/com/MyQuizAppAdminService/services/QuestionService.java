package com.MyQuizAppAdminService.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MyQuizAppAdminService.beans.Question;
import com.MyQuizAppAdminService.repos.QuestionRepository;

@Service
public class QuestionService {

	
	@Autowired
	private QuestionRepository repository;
	
	public void addQuestion(Question question) {
		repository.save(question);
	}
	
	public void removeQuestion(long questionId) throws EntityNotFoundException {
		if(repository.existsById(questionId)) {
			repository.deleteById(questionId);
		}else {
			throw new EntityNotFoundException("Question with id: " + questionId + " does not exists");
		}
	}
	
	public void updateQuestion(Question question) throws EntityNotFoundException {
		if(repository.existsById(question.getId())) {
			repository.save(question);
		}else {
			throw new EntityNotFoundException("Question with id: " + question.getId() + " does not exists");
		}
	}
	
	public Question getQuestionById(long questionId) {
			return repository.findById(questionId).orElse(null);
	}
	
	public List<Question> getAllQuestions() throws EntityNotFoundException {
		if(repository.count() > 0) {
			return repository.findAll();
		}else {
			throw new EntityNotFoundException("Question database is empty");
		}
	}
	
}
