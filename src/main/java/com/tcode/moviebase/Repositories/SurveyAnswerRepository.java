package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.SurveyAnswer;
import com.tcode.moviebase.Entities.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {


    boolean existsByQuestion_IdAndUserId(Long questionId, String userId);


    @Query("""
           select sa.answer as answer, count(sa) as votes
           from SurveyAnswer sa
           where sa.question.id = :questionId
           group by sa.answer
           order by votes desc
           limit 1
           """)
    SurveyOption findMostChosenAnswerByQuestionId(@Param("questionId") Long questionId);
}
