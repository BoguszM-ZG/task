package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Survey;
import com.tcode.moviebase.Entities.SurveyAnswer;
import com.tcode.moviebase.Entities.SurveyOption;
import com.tcode.moviebase.Entities.SurveyQuestion;
import com.tcode.moviebase.Repositories.SurveyAnswerRepository;
import com.tcode.moviebase.Repositories.SurveyOptionRepository;
import com.tcode.moviebase.Repositories.SurveyQuestionRepository;
import com.tcode.moviebase.Repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyOptionRepository surveyOptionRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElse(null);
    }

    public Survey createSurvey(String surveyTitle) {
        Survey survey = new Survey();
        survey.setTitle(surveyTitle);
        surveyRepository.save(survey);
        return survey;
    }


    public SurveyQuestion addQuestionToSurvey(Long surveyId, String questionContent) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);

        SurveyQuestion question = new SurveyQuestion();
        question.setContent(questionContent);
        question.setSurvey(survey);
        surveyQuestionRepository.save(question);

        survey.getSurveyQuestions().add(question);
        surveyRepository.save(survey);

        return question;
    }

    public SurveyOption addOptionToQuestion(Long questionId, String optionContent) {
        SurveyQuestion question = surveyQuestionRepository.findById(questionId).orElse(null);

        SurveyOption option = new SurveyOption();
        option.setContent(optionContent);
        option.setQuestion(question);
        surveyOptionRepository.save(option);

        question.getSurveyOptions().add(option);
        surveyRepository.save(question.getSurvey());

        return option;
    }

    public boolean surveyExists(Long surveyId) {
        return surveyRepository.existsById(surveyId);
    }

    public boolean questionExists(Long questionId) {
        return surveyQuestionRepository.existsById(questionId);
    }

    public boolean optionExists(Long optionId) {
        return surveyOptionRepository.existsById(optionId);
    }

    public boolean hasUserAnsweredQuestion(Long questionId, String userId) {
        return surveyAnswerRepository.existsByQuestion_IdAndUserId(questionId, userId);
    }

    public SurveyAnswer submitAnswer(String userId, Long questionId, Long optionId) {
        SurveyQuestion question = surveyQuestionRepository.findById(questionId).orElse(null);
        SurveyOption option = surveyOptionRepository.findById(optionId).orElse(null);

        if (question == null || option == null || !question.getSurveyOptions().contains(option)) {
            return null;
        }

        SurveyAnswer answer = new SurveyAnswer();
        answer.setQuestion(question);
        answer.setAnswer(option);
        answer.setUserId(userId);
        surveyAnswerRepository.save(answer);

        return answer;
    }

    public boolean optionBelongsToQuestion(Long optionId, Long questionId) {
        return surveyOptionRepository.existsByIdAndQuestion_Id(optionId, questionId);
    }
    public boolean optionExistsByContent(Long questionId, String content) {
        return surveyOptionRepository.existsByQuestion_IdAndContent(questionId, content);
    }


    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public List<String> getSurveyTitles() {
        return surveyRepository.findAll().stream()
                .map(Survey::getTitle)
                .toList();
    }

    public SurveyOption getMostChosenOption(Long questionId) {
        return surveyAnswerRepository.findMostChosenAnswerByQuestionId(questionId);
    }
}
