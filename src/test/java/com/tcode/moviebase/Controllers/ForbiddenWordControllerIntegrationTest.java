package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.ForbiddenWord;
import com.tcode.moviebase.Repositories.ForbiddenWordRepository;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForbiddenWordControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ForbiddenWordRepository forbiddenWordRepository;

    static class PageResponse<T> {
        private List<T> content;
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/forbidden-words";
    }

    @Test
    void testAddForbiddenWord(){
        forbiddenWordRepository.deleteAll();
        String word = "spam";
        restTemplate.postForEntity(baseUrl, word, Void.class);
        var words = forbiddenWordRepository.findAll();
        assert(words.size() == 1);
        assert(words.getFirst().getWord().equals(word));
    }

    @Test
    void testDeleteForbiddenWord(){
        forbiddenWordRepository.deleteAll();
        var forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord("spam");
        forbiddenWord = forbiddenWordRepository.save(forbiddenWord);
        restTemplate.delete(baseUrl + "/" + forbiddenWord.getId());
        var words = forbiddenWordRepository.findAll();
        assert(words.isEmpty());
    }

    @Test
    void testGetForbiddenWords(){
        forbiddenWordRepository.deleteAll();
        var forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord("spam");
        forbiddenWordRepository.save(forbiddenWord);
        var response = restTemplate.getForEntity(baseUrl + "?page=0&size=10", PageResponse.class);
        var body = response.getBody();
        assert(body != null);
        assert(body.getContent().size() == 1);
    }

    @Test
    void testGetForbiddenWordById(){
        forbiddenWordRepository.deleteAll();
        var forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord("spam");
        forbiddenWord = forbiddenWordRepository.save(forbiddenWord);
        var response = restTemplate.getForEntity(baseUrl + "/" + forbiddenWord.getId(), ForbiddenWord.class);
        var body = response.getBody();
        assert(body != null);
        assert(body.getWord().equals("spam"));
    }


}
