package com.MyQuizAppAdminService.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MyQuizAppAdminService.beans.SuggestedQuestion;

@Repository
public interface SuggestedQuestionRepository extends JpaRepository<SuggestedQuestion, Long> {

}
