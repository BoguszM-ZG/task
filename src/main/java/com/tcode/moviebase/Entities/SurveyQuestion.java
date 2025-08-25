package com.tcode.moviebase.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "survey_question", schema = "movies")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany
    @JoinColumn(name = "question_id")
    private Set<SurveyOption> surveyOptions = new LinkedHashSet<>();

    @OneToMany
    @JoinColumn(name = "question_id")
    private Set<SurveyAnswer> surveyAnswers = new LinkedHashSet<>();



}