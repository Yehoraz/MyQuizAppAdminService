package com.MyQuizAppAdminService.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MyQuizAppAdminService.beans.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>{

}
