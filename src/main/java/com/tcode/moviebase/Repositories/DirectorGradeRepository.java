package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.DirectorGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectorGradeRepository extends JpaRepository<DirectorGrade, Long> {
    boolean existsByUserIdAndDirectorId(String userId, Long directorId);

    DirectorGrade findByUserIdAndDirectorId(String userId, Long directorId);

    List<DirectorGrade> findByDirectorId(Long directorId);
}
