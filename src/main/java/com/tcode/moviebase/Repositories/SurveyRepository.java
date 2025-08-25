package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
