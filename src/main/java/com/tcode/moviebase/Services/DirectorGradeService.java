package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.DirectorGrade;
import com.tcode.moviebase.Exceptions.DirectorNotFoundException;
import com.tcode.moviebase.Exceptions.GradeOutOfRangeException;
import com.tcode.moviebase.Repositories.DirectorGradeRepository;
import com.tcode.moviebase.Repositories.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorGradeService {

    private final DirectorGradeRepository directorGradeRepository;
    private final DirectorRepository directorRepository;

    public void addDirectorGrade(String userId, Long directorId, int grade) {
        var director = directorRepository.findById(directorId).orElseThrow( () -> new DirectorNotFoundException("Director not found"));

        if (grade < 0 || grade > 10) {
            throw new GradeOutOfRangeException(grade);
        }
        DirectorGrade directorGrade = new DirectorGrade();
        directorGrade.setUserId(userId);
        directorGrade.setDirector(director);
        directorGrade.setGrade(grade);

        directorGradeRepository.save(directorGrade);
    }

    public void updateDirectorGrade(String userId, Long directorId, int grade) {
        DirectorGrade existingGrade = directorGradeRepository.findByUserIdAndDirectorId(userId, directorId);
        if (existingGrade != null) {
            directorGradeRepository.delete(existingGrade);
        }
        addDirectorGrade(userId, directorId, grade);
    }


    public boolean existsDirectorGrade(String userId, Long directorId) {
        return directorGradeRepository.existsByUserIdAndDirectorId(userId, directorId);
    }

    public Double getAvgDirectorGrade(Long directorId) {
        var grades = directorGradeRepository.findByDirectorId(directorId);
        if (grades.isEmpty()) {
            return 0.0;
        }

        var result = grades.stream().mapToDouble(DirectorGrade::getGrade).sum();
        return result / grades.size();
    }
}
