package com.tcode.moviebase.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "category", length = 100)
    private String category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "prizes")
    private String prizes;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGrade> movieGrades = new ArrayList<>();

}