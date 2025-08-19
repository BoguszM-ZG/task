package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {


    boolean existsByQuestion_IdAndUserId(Long questionId, String userId);
}
