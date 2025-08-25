package com.tcode.moviebase.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "directors", schema = "movies")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Size(max = 30)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;

    @Size(max = 255)
    @Column(name = "biography")
    private String biography;

    @Size(max = 10)
    @NotNull
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @OneToMany(mappedBy = "director",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DirectorGrade> directorGrades = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "director_movies",
            schema = "movies",
            joinColumns = @JoinColumn(name = "director_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> movies = new LinkedHashSet<>();

}