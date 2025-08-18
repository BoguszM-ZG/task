package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Forum;
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

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForumIntegrationTest {

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
        baseUrl = "http://localhost:" + port + "/api/forum";
    }

    @Test
    public void testAddForum() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var response = restTemplate.postForEntity(baseUrl, forum, Forum.class);

        assertNotNull(response.getBody());
    }

    @Test
    public void testAddForumWithInvalidData() {
        var forum = new Forum();
        forum.setForumName(""); // Invalid name
        try {
            restTemplate.postForEntity(baseUrl, forum, Forum.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }


    }

    @Test
    public void testGetAllForums() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        restTemplate.postForEntity(baseUrl, forum, Forum.class);

        var response = restTemplate.getForEntity(baseUrl, Forum[].class);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetForumById() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var createdResponse = restTemplate.postForEntity(baseUrl, forum, Forum.class);
        assertNotNull(createdResponse.getBody());
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());

        Long forumId = createdResponse.getBody().getId();
        var response = restTemplate.getForEntity(baseUrl + "/" + forumId, Forum.class);
        assertNotNull(response.getBody());
        assertEquals(forumId, response.getBody().getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetNonExistentForumById() {
        Long nonExistentId = 999L;
        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentId, Forum.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testDeleteForum() {
        var forum = new Forum();
        forum.setForumName("Test Forum");
        var createdResponse = restTemplate.postForEntity(baseUrl, forum, Forum.class);
        assertNotNull(createdResponse.getBody());
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());

        Long forumId = createdResponse.getBody().getId();
        restTemplate.delete(baseUrl + "/" + forumId);

        try {
            restTemplate.getForEntity(baseUrl + "/" + forumId, Forum.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testDeleteNonExistentForum() {
        Long nonExistentId = 999L;
        try {
            restTemplate.delete(baseUrl + "/" + nonExistentId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }


}
