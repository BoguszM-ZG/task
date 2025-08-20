package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.SurveyDto;
import com.tcode.moviebase.Dtos.SurveyOptionDto;
import com.tcode.moviebase.Dtos.SurveyQuestionDto;
import com.tcode.moviebase.Dtos.SurveyResultDto;
import com.tcode.moviebase.Entities.Survey;
import com.tcode.moviebase.Entities.SurveyAnswer;
import com.tcode.moviebase.Entities.SurveyOption;
import com.tcode.moviebase.Entities.SurveyQuestion;
import com.tcode.moviebase.Services.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    public final SurveyService surveyService;

    @Operation(summary = "get survey by id", description = "Returns a survey by its ID. If the survey does not exist, returns a 404 Not Found response.")
    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(survey);
    }

    @Operation(summary = "create survey", description = "Creates a new survey with the given title. Returns the created survey.")
    @PostMapping("/create")
    public ResponseEntity<Survey> createSurvey(@RequestBody String surveyTitle) {
        Survey survey = surveyService.createSurvey(surveyTitle);
        return ResponseEntity.ok(survey);
    }

    @Operation(summary = "add question to survey", description = "Adds a question to the specified survey. If the survey does not exist, returns a 404 Not Found response.")
    @PostMapping("/{surveyId}/questions")
    public ResponseEntity<SurveyQuestion> addQuestionToSurvey(@PathVariable Long surveyId, @RequestBody String questionContent) {
        if (!surveyService.surveyExists(surveyId)) {
            return ResponseEntity.notFound().build();
        }
        SurveyQuestion survey = surveyService.addQuestionToSurvey(surveyId, questionContent);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(survey);
    }

    @Operation(summary = "add option of answer to question", description = "Adds an option of answer to the specified question in a survey. If the survey or question does not exist, returns a 404 Not Found response.")
    @PostMapping("/{surveyId}/questions/{questionId}/options")
    public ResponseEntity<?> addOptionToQuestion(@PathVariable Long surveyId, @PathVariable Long questionId, @RequestBody String optionContent) {
        if (!surveyService.surveyExists(surveyId)) {
            return ResponseEntity.notFound().build();
        }
        if (!surveyService.questionExists(questionId)) {
            return ResponseEntity.notFound().build();
        }
        if (surveyService.optionExistsByContent(questionId, optionContent)) {
            return ResponseEntity.badRequest().body("Option with this content already exists for the question.");
        }
        SurveyOption option = surveyService.addOptionToQuestion(questionId, optionContent);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    @Operation(summary = "submit question answer", description = "Submits an answer to a question in a survey. If the survey, question, or option does not exist, returns a 400 Bad Request response. If the user has already answered the question, returns a 400 Bad Request response.")
    @PostMapping("/{surveyId}/questions/{questionId}/options/{optionId}/submit")
    public ResponseEntity<?> submitSurveyAnswer(@AuthenticationPrincipal Jwt jwt, @PathVariable Long questionId, @PathVariable Long surveyId, @PathVariable Long optionId) {
        if (!surveyService.surveyExists(surveyId)) {
            return ResponseEntity.badRequest().body("Survey does not exist.");
        }
        if (!surveyService.questionExists(questionId)) {
            return ResponseEntity.badRequest().body("Question does not exist.");
        }

        if (!surveyService.optionBelongsToQuestion(optionId, questionId)) {
            return ResponseEntity.badRequest().body("Option does not belong to the specified question.");
        }
        var userId = jwt.getClaimAsString("sub");
        if (surveyService.hasUserAnsweredQuestion(questionId, userId)) {
            return ResponseEntity.badRequest().body("You have already answered this question.");
        }


        SurveyAnswer answer = surveyService.submitAnswer(userId, questionId, optionId);
        if (answer == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(answer);
    }


    @Operation(summary = "get survey for user", description = "Returns a survey for a user by its ID. If the survey does not exist, returns a 404 Not Found response.")
    @GetMapping("/{surveyId}/for-user")
    public ResponseEntity<?> getSurveyForUser(@PathVariable Long surveyId) {
        var survey = surveyService.getSurveyById(surveyId);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        var surveyQuestions = survey.getSurveyQuestions();


        var surveyDto = new SurveyDto(survey.getTitle(), surveyQuestions.stream().map(q -> new SurveyQuestionDto(q.getContent(), q.getSurveyOptions().stream().map(o -> new SurveyOptionDto(o.getContent())).toList())).toList());

        return ResponseEntity.ok(surveyDto);
    }

    @Operation(summary = "get all surveys", description = "Returns a list of all surveys.")
    @GetMapping("/all")
    public ResponseEntity<?> getAllSurveys() {
        var surveys = surveyService.getAllSurveys();
        if (surveys.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var surveyDtos = surveys.stream().map(survey -> new SurveyDto(survey.getTitle(), survey.getSurveyQuestions().stream().map(q -> new SurveyQuestionDto(q.getContent(), q.getSurveyOptions().stream().map(o -> new SurveyOptionDto(o.getContent())).toList())).toList())).toList();
        return ResponseEntity.ok(surveyDtos);
    }

    @Operation(summary = "get survey titles", description = "Returns a list of all survey titles.")
    @GetMapping("/all/titles")
    public ResponseEntity<?> getSurveyTitles() {
        var titles = surveyService.getSurveyTitles();
        if (titles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(titles);
    }


    @Operation(summary = "get most chosen option for survey", description = "Returns the most chosen options for each question in a survey. If the survey does not exist, returns a 404 Not Found response.")
    @GetMapping("/{surveyId}/results")
    public ResponseEntity<?> getMostChosenOptionForSurvey(@PathVariable Long surveyId) {
        var survey = surveyService.getSurveyById(surveyId);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        var surveyResultDto = survey.getSurveyQuestions().stream().map(question -> {
            var mostChosenOption = surveyService.getMostChosenOption(question.getId());
            if (mostChosenOption == null) {
                return null;
            }
            return new SurveyResultDto(question.getContent(), mostChosenOption.getContent());
        }).filter(Objects::nonNull).toList();


        return ResponseEntity.ok(surveyResultDto);
    }


}
