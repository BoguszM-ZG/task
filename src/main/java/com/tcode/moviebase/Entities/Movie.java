package com.tcode.moviebase.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "movie", schema = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "movie_year", nullable = false)
    private Integer movie_year;

    @Column(name = "category", length = 100)
    private String category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "prizes")
    private String prizes;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGrade> movieGrades = new ArrayList<>();

    @Column(name = "tag")
    private String tag;

    @Column(name = "world_premiere")
    private LocalDate world_premiere;

    @Column(name = "polish_premiere")
    private LocalDate polish_premiere;

}