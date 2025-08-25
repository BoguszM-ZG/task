package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;


    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";


    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/messages";
    }

    @Test
    public void testAddMessageToNonExistingThread() {
        String content = "This is a test message";
        try {
            restTemplate.postForEntity(baseUrl + "/send/999", content, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testAddMessageToExistingThread() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var addedForum = restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum/member/join/" + addedForum.getBody().getId(), request, String.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(addedForum.getBody());
        HttpEntity<ForumThread> threadRequest = new HttpEntity<>(thread, headers);
        var addedThread = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/threads/create/" + addedForum.getBody().getId(),
                threadRequest, ForumThread.class);

        String content = "This is a test message";
        HttpEntity<String> messageRequest = new HttpEntity<>(content, headers);
        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/messages/send/" + addedThread.getBody().getId(),
                messageRequest, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMessagesByThreadId() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var addedForum = restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum/member/join/" + addedForum.getBody().getId(), request, String.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(addedForum.getBody());
        HttpEntity<ForumThread> threadRequest = new HttpEntity<>(thread, headers);
        var addedThread = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/threads/create/" + addedForum.getBody().getId(),
                threadRequest, ForumThread.class);

        String content = "This is a test message";
        HttpEntity<String> messageRequest = new HttpEntity<>(content, headers);
        restTemplate.postForEntity(
                "http://localhost:" + port + "/api/messages/send/" + addedThread.getBody().getId(),
                messageRequest, Object.class);

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/messages/" + addedThread.getBody().getId(),
                Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMessagesByNonExistingThreadId() {
        try {
            restTemplate.getForEntity(baseUrl + "/999", Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testGetMessagesByThreadIdWithNoMessages() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var addedForum = restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum/member/join/" + addedForum.getBody().getId(), request, String.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(addedForum.getBody());
        HttpEntity<ForumThread> threadRequest = new HttpEntity<>(thread, headers);
        var addedThread = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/threads/create/" + addedForum.getBody().getId(),
                threadRequest, ForumThread.class);

        var response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/messages/" + addedThread.getBody().getId(),
                Object.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testSendMessageUserNotMemberOfForum() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var addedForum = restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(addedForum.getBody());
        HttpEntity<ForumThread> threadRequest = new HttpEntity<>(thread);
        var addedThread = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/threads/create/" + addedForum.getBody().getId(),
                threadRequest, ForumThread.class);

        String content = "This is a test message";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<String> messageRequest = new HttpEntity<>(content, headers);

        try {
            restTemplate.postForEntity(
                    "http://localhost:" + port + "/api/messages/send/" + addedThread.getBody().getId(),
                    messageRequest, Object.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("User is not a member of this forum", e.getResponseBodyAsString());
        }
    }

    @Test
    public void testDeleteMessage() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var addedForum = restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum/member/join/" + addedForum.getBody().getId(), request, String.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(addedForum.getBody());
        HttpEntity<ForumThread> threadRequest = new HttpEntity<>(thread, headers);
        var addedThread = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/threads/create/" + addedForum.getBody().getId(),
                threadRequest, ForumThread.class);

        String content = "This is a test message";
        HttpEntity<String> messageRequest = new HttpEntity<>(content, headers);
        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/messages/send/" + addedThread.getBody().getId(),
                messageRequest, Message.class);

        Long messageId = response.getBody().getId();

        var deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/messages/" + messageId,
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
    
    
    
    
}
