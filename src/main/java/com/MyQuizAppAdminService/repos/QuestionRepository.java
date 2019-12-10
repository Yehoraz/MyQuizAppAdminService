package com.MyQuizAppAdminService.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MyQuizAppAdminService.beans.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

	public boolean existsByQuestionTextAndIsApproved(String questionText, boolean flag);
	
	public Optional<Question> findByQuestionTextAndIsApproved(String questionText, boolean flag);
	
	public List<Question> findByIsApproved(boolean flag);
	
}
