package com.MyQuizAppAdminService.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MyQuizAppAdminService.beans.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

}
