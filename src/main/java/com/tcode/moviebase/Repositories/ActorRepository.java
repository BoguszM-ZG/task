package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Dtos.ActorDto;
import com.tcode.moviebase.Entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    void deleteActorById(Long id);

    List<Actor> id(Long id);

    @Query("SELECT new com.tcode.moviebase.Dtos.ActorDto("+
            "a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth, "+
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes, "+
            "AVG(g.grade)) "+
            "FROM Actor a LEFT JOIN a.actorGrades g "+
            "GROUP BY a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth," +
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes "+
            "ORDER BY AVG(g.grade) DESC")
    List<ActorDto> findAllActorsWithAvgGradeDesc();


    @Query("SELECT new com.tcode.moviebase.Dtos.ActorDto("+
            "a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth, "+
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes, "+
            "AVG(g.grade)) "+
            "FROM Actor a LEFT JOIN a.actorGrades g "+
            "GROUP BY a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth," +
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes "+
            "ORDER BY AVG(g.grade) ASC")
    List<ActorDto> findAllActorsWithAvgGradeAsc();

    @Query("SELECT new com.tcode.moviebase.Dtos.ActorDto("+
            "a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth, "+
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes, "+
            "AVG(g.grade)) "+
            "FROM Actor a LEFT JOIN a.actorGrades g "+
            "GROUP BY a.gender, a.firstName, a.lastName, a.age, a.dateOfBirth," +
            "a.placeOfBirth, a.height, a.biography, a.countOfPrizes "+
            "ORDER BY AVG(g.grade) DESC LIMIT 10")
    List<ActorDto> findTopTenActorsByAvgGrade();
}
