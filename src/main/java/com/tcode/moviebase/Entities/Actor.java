package com.tcode.moviebase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "actors", schema = "movies")
public class    Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 10)
    @NotNull
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Size(max = 30)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Size(max = 30)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 40)
    @Column(name = "place_of_birth", length = 40)
    private String placeOfBirth;

    @Column(name = "height")
    private Integer height;

    @Size(max = 255)
    @Column(name = "biography")
    private String biography;

    @Column(name = "count_of_prizes")
    private Integer countOfPrizes;

    @OneToMany(mappedBy = "actor")
    private List<ActorGrade> actorGrades = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "actor_movies",
        schema = "movies",
        joinColumns = @JoinColumn(name = "actor_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonIgnore
    private List<Movie> movies = new ArrayList<>();

}