package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Survey;
import com.tcode.moviebase.Entities.SurveyAnswer;
import com.tcode.moviebase.Entities.SurveyOption;
import com.tcode.moviebase.Entities.SurveyQuestion;
import com.tcode.moviebase.Repositories.SurveyAnswerRepository;
import com.tcode.moviebase.Repositories.SurveyOptionRepository;
import com.tcode.moviebase.Repositories.SurveyQuestionRepository;
import com.tcode.moviebase.Repositories.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private SurveyQuestionRepository surveyQuestionRepository;

    @Mock
    private SurveyOptionRepository surveyOptionRepository;

    @Mock
    private SurveyAnswerRepository surveyAnswerRepository;

    @InjectMocks
    private SurveyService surveyService;


    @Test
    void testGetSurveyById() {
        Survey survey = new Survey();
        survey.setId(1L);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        Survey result = surveyService.getSurveyById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateSurvey() {
        Survey survey = new Survey();
        survey.setTitle("Test");
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey result = surveyService.createSurvey("Test");

        assertEquals("Test", result.getTitle());
        verify(surveyRepository).save(any(Survey.class));
    }

    @Test
    void testAddQuestionToSurvey() {
        Survey survey = new Survey();
        survey.setId(1L);
        survey.setSurveyQuestions(new HashSet<>());
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(surveyQuestionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(surveyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SurveyQuestion question = surveyService.addQuestionToSurvey(1L, "Question");

        assertEquals("Question", question.getContent());
        assertEquals(survey, question.getSurvey());
        assertTrue(survey.getSurveyQuestions().contains(question));
    }

    @Test
    void testAddOptionToQuestion() {
        SurveyQuestion question = new SurveyQuestion();
        question.setId(2L);
        question.setSurveyOptions(new HashSet<>());
        Survey survey = new Survey();
        question.setSurvey(survey);
        when(surveyQuestionRepository.findById(2L)).thenReturn(Optional.of(question));
        when(surveyOptionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(surveyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SurveyOption option = surveyService.addOptionToQuestion(2L, "option");

        assertEquals("option", option.getContent());
        assertEquals(question, option.getQuestion());
        assertTrue(question.getSurveyOptions().contains(option));
    }

    @Test
    void testSurveyExists() {
        when(surveyRepository.existsById(1L)).thenReturn(true);
        assertTrue(surveyService.surveyExists(1L));
    }

    @Test
    void testQuestionExists() {
        when(surveyQuestionRepository.existsById(2L)).thenReturn(true);
        assertTrue(surveyService.questionExists(2L));
    }

    @Test
    void testOptionExists() {
        when(surveyOptionRepository.existsById(3L)).thenReturn(true);
        assertTrue(surveyService.optionExists(3L));
    }

    @Test
    void testHasUserAnsweredQuestion() {
        when(surveyAnswerRepository.existsByQuestion_IdAndUserId(2L, "user")).thenReturn(true);
        assertTrue(surveyService.hasUserAnsweredQuestion(2L, "user"));
    }

    @Test
    void testSubmitAnswer() {
        SurveyQuestion question = new SurveyQuestion();
        question.setId(2L);
        SurveyOption option = new SurveyOption();
        option.setId(3L);
        question.setSurveyOptions(new HashSet<>(Collections.singletonList(option)));

        when(surveyQuestionRepository.findById(2L)).thenReturn(Optional.of(question));
        when(surveyOptionRepository.findById(3L)).thenReturn(Optional.of(option));
        when(surveyAnswerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SurveyAnswer answer = surveyService.submitAnswer("user", 2L, 3L);

        assertNotNull(answer);
        assertEquals(question, answer.getQuestion());
        assertEquals(option, answer.getAnswer());
        assertEquals("user", answer.getUserId());
    }

    @Test
    void testOptionBelongsToQuestion() {
        when(surveyOptionRepository.existsByIdAndQuestion_Id(3L, 2L)).thenReturn(true);
        assertTrue(surveyService.optionBelongsToQuestion(3L, 2L));
    }

    @Test
    void testOptionExistsByContent() {
        when(surveyOptionRepository.existsByQuestion_IdAndContent(2L, "option")).thenReturn(true);
        assertTrue(surveyService.optionExistsByContent(2L, "option"));
    }
}
