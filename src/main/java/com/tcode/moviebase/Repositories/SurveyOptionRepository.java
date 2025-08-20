package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Long> {

    boolean existsByIdAndQuestion_Id(long id, long questionId);

    boolean existsByQuestion_IdAndContent(Long id, String content);

    SurveyOption findByContent(String content);
}
