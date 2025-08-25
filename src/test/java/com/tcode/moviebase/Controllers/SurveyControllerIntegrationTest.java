package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Survey;
import com.tcode.moviebase.Repositories.SurveyOptionRepository;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";


    private static RestTemplate restTemplate;
    @Autowired
    private SurveyOptionRepository surveyOptionRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/surveys";
    }

    @Test
    public void testAddSurvey() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(title, headers);

        ResponseEntity<Survey> response = restTemplate.postForEntity(baseUrl + "/create", request, Survey.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(title, response.getBody().getTitle());
    }

    @Test
    public void testGetSurveyById() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(title, headers);

        ResponseEntity<Survey> createResponse = restTemplate.postForEntity(baseUrl + "/create", request, Survey.class);
        Long surveyId = createResponse.getBody().getId();


        ResponseEntity<Survey> getResponse = restTemplate.getForEntity(baseUrl + "/" + surveyId, Survey.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(title, getResponse.getBody().getTitle());
    }

    @Test
    public void testGetNonExistentSurvey() {
        Long nonExistentId = 999L;

        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentId, Survey.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testAddQuestionToSurvey() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(title, headers);

        ResponseEntity<Survey> createResponse = restTemplate.postForEntity(baseUrl + "/create", request, Survey.class);
        Long surveyId = createResponse.getBody().getId();

        String questionContent = "What is your favorite movie?";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);

        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);

        assertEquals(HttpStatus.OK, questionResponse.getStatusCode());
        assertNotNull(questionResponse.getBody());
    }

    @Test
    public void testAddQuestionToNonExistentSurvey() {
        Long nonExistentSurveyId = 999L;
        String questionContent = "What is your favorite movie?";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);

        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentSurveyId + "/questions", questionRequest, Survey.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testAddOptionToQuestion() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(title, headers);

        ResponseEntity<Survey> createResponse = restTemplate.postForEntity(baseUrl + "/create", request, Survey.class);
        Long surveyId = createResponse.getBody().getId();

        String questionContent = "What is your favorite movie?";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();

        String optionContent = "Inception";
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);

        ResponseEntity<?> optionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest, Object.class);

        assertEquals(HttpStatus.OK, optionResponse.getStatusCode());
    }

    @Test
    public void testAddOptionToNonExistentQuestion() {
        Long nonExistentSurveyId = 999L;
        Long nonExistentQuestionId = 999L;
        String optionContent = "Inception";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);

        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentSurveyId + "/questions/" + nonExistentQuestionId + "/options", optionRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testAddOptionWithDuplicateContent() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(title, headers);

        ResponseEntity<Survey> createResponse = restTemplate.postForEntity(baseUrl + "/create", request, Survey.class);
        Long surveyId = createResponse.getBody().getId();

        String questionContent = "What is your favorite movie?";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();

        String optionContent = "Inception";
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);

        ResponseEntity<?> firstResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest, Object.class);
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());


        try {
            restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Option with this content already exists for the question.", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testAddOptionToQuestionWithNonExistentSurvey() {
        Long nonExistentSurveyId = 999L;
        Long nonExistentQuestionId = 999L;
        String optionContent = "Inception";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);

        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentSurveyId + "/questions/" + nonExistentQuestionId + "/options", optionRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testSubmitSurveyAnswer() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        String questionContent = "test";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();

        String optionContent = "test";
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);
        restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest, Object.class);

        Long optionId = surveyOptionRepository.findAll().get(2).getId();


        headers.set("Authorization", jwtToken);
        HttpEntity<Void> answerRequest = new HttpEntity<>(headers);
        ResponseEntity<?> answerResponse = restTemplate.postForEntity(
                baseUrl + "/" + surveyId + "/questions/" + questionId + "/options/" + optionId + "/submit",
                answerRequest, Object.class);

        assertEquals(HttpStatus.OK, answerResponse.getStatusCode());
    }

    @Test
    public void testSubmitSurveyAnswerAlreadyAnswered() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        String questionContent = "test";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();

        String optionContent = "test";
        HttpEntity<String> optionRequest = new HttpEntity<>(optionContent, headers);
        restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest, Object.class);
        Long optionId = surveyOptionRepository.findAll().get(3).getId();

        headers.set("Authorization", jwtToken);
        HttpEntity<Void> answerRequest = new HttpEntity<>(headers);
        restTemplate.postForEntity(
                baseUrl + "/" + surveyId + "/questions/" + questionId + "/options/" + optionId + "/submit",
                answerRequest, Object.class);


        try {
            restTemplate.postForEntity(
                    baseUrl + "/" + surveyId + "/questions/" + questionId + "/options/" + optionId + "/submit",
                    answerRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("You have already answered this question.", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testGetSurveyForUser() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        ResponseEntity<Object> response = restTemplate.getForEntity(baseUrl + "/" + surveyId + "/for-user", Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetSurveyForUserNotFound() {
        Long nonExistentSurveyId = 999L;
        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentSurveyId + "/for-user", Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testSubmitSurveyAnswerForNonExistentSurvey() {
        Long nonExistentSurveyId = 999L;
        Long questionId = 999L;
        Long optionId = 999L;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> answerRequest = new HttpEntity<>(null, headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/" + nonExistentSurveyId + "/questions/" + questionId + "/options/" + optionId + "/submit",
                    answerRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Survey does not exist.", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testSubmitSurveyAnswerForNonExistentQuestion() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        Long nonExistentQuestionId = 999L;
        Long optionId = 999L;

        headers.set("Authorization", jwtToken);
        HttpEntity<Void> answerRequest = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/" + surveyId + "/questions/" + nonExistentQuestionId + "/options/" + optionId + "/submit",
                    answerRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Question does not exist.", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testSubmitSurveyAnswerForNonExistentOption() {
        String title = "test";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        String questionContent = "test?";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();

        Long nonExistentOptionId = 999L;

        headers.set("Authorization", jwtToken);
        HttpEntity<Void> answerRequest = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/" + surveyId + "/questions/" + questionId + "/options/" + nonExistentOptionId + "/submit",
                    answerRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Option does not belong to the specified question.", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testGetAllSurveys() {
        var survey1 = new Survey();
        survey1.setTitle("Survey 1");
        restTemplate.postForEntity(baseUrl + "/create", survey1, Survey.class);
        ResponseEntity<Survey[]> response = restTemplate.getForEntity(baseUrl + "/all", Survey[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

    }

    @Test
    public void testGetSurveyTitle(){
        var survey1 = new Survey();
        survey1.setTitle("Survey 1");
        restTemplate.postForEntity(baseUrl + "/create", survey1, Survey.class);
        ResponseEntity<String[]> response = restTemplate.getForEntity(baseUrl + "/all/titles", String[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testGetMostChosenOptionForSurvey() {
        String title = "test survey";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> surveyRequest = new HttpEntity<>(title, headers);
        ResponseEntity<Survey> surveyResponse = restTemplate.postForEntity(baseUrl + "/create", surveyRequest, Survey.class);
        Long surveyId = surveyResponse.getBody().getId();

        String questionContent = "What is your favorite movie?";
        HttpEntity<String> questionRequest = new HttpEntity<>(questionContent, headers);
        ResponseEntity<Survey> questionResponse = restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions", questionRequest, Survey.class);
        Long questionId = questionResponse.getBody().getId();


        String optionContent2 = "test";
        HttpEntity<String> optionRequest2 = new HttpEntity<>(optionContent2, headers);
        restTemplate.postForEntity(baseUrl + "/" + surveyId + "/questions/" + questionId + "/options", optionRequest2, Object.class);

        Long optionId1 = surveyOptionRepository.findAll().get(1).getId();

        headers.set("Authorization", jwtToken);
        restTemplate.postForEntity(
                baseUrl + "/" + surveyId + "/questions/" + questionId + "/options/" + optionId1 + "/submit",
                new HttpEntity<>(headers), Object.class);


        ResponseEntity<?> response = restTemplate.getForEntity(baseUrl + "/" + surveyId + "/results", Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


}
