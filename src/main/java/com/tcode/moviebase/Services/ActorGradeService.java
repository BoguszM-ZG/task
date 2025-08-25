package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.ActorGrade;
import com.tcode.moviebase.Exceptions.ActorNotFoundException;
import com.tcode.moviebase.Exceptions.GradeOutOfRangeException;
import com.tcode.moviebase.Repositories.ActorGradeRepository;
import com.tcode.moviebase.Repositories.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorGradeService {
    private final ActorService actorService;
    private final ActorGradeRepository actorGradeRepository;
    private final ActorRepository actorRepository;

    public ActorGrade addActorGrade(Long actorId, int grade) {
        var actor = actorRepository.findById(actorId).orElseThrow(() -> new ActorNotFoundException("Actor not found"));

        if (grade < 1 || grade > 10) {
            throw new GradeOutOfRangeException(grade);
        }
        var actorGrade = new ActorGrade();
        actorGrade.setActor(actor);
        actorGrade.setGrade(grade);
        return actorGradeRepository.save(actorGrade);
    }

    public Double getAvgGrade(Long actorId) {
        List<ActorGrade> grades = actorGradeRepository.findByActorId(actorId);
        if (grades.isEmpty()) {
            return null;
        }

        var result = grades.stream().mapToDouble(ActorGrade::getGrade).sum();

        return result / grades.size();
    }

}
