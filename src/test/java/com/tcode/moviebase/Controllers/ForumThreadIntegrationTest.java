package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForumThreadIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;



    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/threads";
    }

    @Test
    public void testCreateThreadNonExistingForum() {
        ForumThread thread = new ForumThread();
        thread.setThreadName("test");
        thread.setForum(null);

        try{
            restTemplate.postForEntity(baseUrl + "/create/1", thread, ForumThread.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testCreateThread(){
        var forum = new Forum();
        forum.setForumName("Test Forum");
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(forum);
        var response = restTemplate.postForEntity(baseUrl + "/create/1", thread, ForumThread.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Thread", response.getBody().getThreadName());
    }

    @Test
    public void testAddAndGetAllThreads() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(forum);
        restTemplate.postForEntity(baseUrl + "/create/1", thread, ForumThread.class);

        var response = restTemplate.getForEntity(baseUrl + "/all", ForumThread[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testGetThreadsByForumId() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(forum);
        restTemplate.postForEntity(baseUrl + "/create/1", thread, ForumThread.class);

        var response = restTemplate.getForEntity(baseUrl + "/forum/1", ForumThread[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testGetThreadsByNonExistingForum() {

        try {
            restTemplate.getForEntity(baseUrl + "/forum/999", ForumThread[].class);
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("No threads found for this forum"));
        }
    }

    @Test
    public void testDeleteThread() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        restTemplate.postForEntity("http://localhost:" + port + "/api/forum", forum, Forum.class);

        ForumThread thread = new ForumThread();
        thread.setThreadName("Test Thread");
        thread.setForum(forum);
        var createdResponse = restTemplate.postForEntity(baseUrl + "/create/1", thread, ForumThread.class);
        assertEquals(HttpStatus.OK, createdResponse.getStatusCode());

        restTemplate.delete(baseUrl + "/delete/" + createdResponse.getBody().getId());

        try {
            restTemplate.getForEntity(baseUrl + "/forum/1", ForumThread[].class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("No threads found for this forum"));
        }
    }

    @Test
    public void testDeleteNonExistingThread() {
        try {
            restTemplate.delete(baseUrl + "/delete/999");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Forum thread with that id does not exist"));
        }
    }
}
