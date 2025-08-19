package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.SurveyDto;
import com.tcode.moviebase.Dtos.SurveyOptionDto;
import com.tcode.moviebase.Dtos.SurveyQuestionDto;
import com.tcode.moviebase.Entities.Survey;
import com.tcode.moviebase.Entities.SurveyAnswer;
import com.tcode.moviebase.Entities.SurveyOption;
import com.tcode.moviebase.Entities.SurveyQuestion;
import com.tcode.moviebase.Services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    public final SurveyService surveyService;

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(survey);
    }

    @PostMapping("/create")
    public ResponseEntity<Survey> createSurvey(@RequestBody String surveyTitle) {
        Survey survey = surveyService.createSurvey(surveyTitle);
        return ResponseEntity.ok(survey);
    }

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

    @PostMapping("/{surveyId}/questions/{questionId}/options")
    public ResponseEntity<?> addOptionToQuestion(@PathVariable Long surveyId, @PathVariable Long questionId, @RequestBody String optionContent) {
        if (!surveyService.surveyExists(surveyId)) {
            return ResponseEntity.notFound().build();
        }
        if (!surveyService.questionExists(questionId)) {
            return ResponseEntity.notFound().build();
        }
        if(surveyService.optionExistsByContent(questionId, optionContent)) {
            return ResponseEntity.badRequest().body("Option with this content already exists for the question.");
        }
        SurveyOption option = surveyService.addOptionToQuestion(questionId, optionContent);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    @PostMapping("/{surveyId}/questions/{questionId}/options/{optionId}/submit")
    public ResponseEntity<?> submitSurveyAnswer(@AuthenticationPrincipal Jwt jwt, @PathVariable Long questionId, @PathVariable Long surveyId,
                                                           @PathVariable Long optionId) {
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
        if (surveyService.hasUserAnsweredQuestion(questionId, userId))
        {
            return ResponseEntity.badRequest().body("You have already answered this question.");
        }


        SurveyAnswer answer = surveyService.submitAnswer(userId, questionId, optionId);
        if (answer == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(answer);
    }


    @GetMapping("/{surveyId}/for-user")
    public ResponseEntity<?> getSurveyForUser(@PathVariable Long surveyId) {
        var survey = surveyService.getSurveyById(surveyId);
        if (survey == null) {
            return ResponseEntity.notFound().build();
        }
        var surveyQuestions = survey.getSurveyQuestions();



        var surveyDto = new SurveyDto(
            survey.getTitle(),
            surveyQuestions.stream()
                .map(q -> new SurveyQuestionDto(
                    q.getContent(),
                    q.getSurveyOptions().stream()
                        .map(o -> new SurveyOptionDto(o.getContent()))
                        .toList()
                ))
                .toList()
        );

        return ResponseEntity.ok(surveyDto);
    }




}
