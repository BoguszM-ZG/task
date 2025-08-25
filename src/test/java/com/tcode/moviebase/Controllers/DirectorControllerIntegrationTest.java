package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Director;
import com.tcode.moviebase.Repositories.DirectorRepository;
import com.tcode.moviebase.Dtos.DirectorDto;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DirectorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    @Autowired
    private DirectorRepository directorRepository;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";


    static class PageResponse<T> {
        private List<T> content;
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
    }

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        directorRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/api/directors";
    }

    private Director newDirectorEntity(String fn, String ln) {
        var d = new Director();
        d.setFirstName(fn);
        d.setLastName(ln);
        d.setGender("Male");
        d.setDateOfBirth(LocalDate.of(1970,1,1));
        d.setBiography("Bio");
        return d;
    }

    private HttpHeaders authHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.set("Authorization", jwtToken);
        return h;
    }

    @Test
    void testAddDirectorAndGetById() {
        Director payload = newDirectorEntity("John","Smith");
        Director created = restTemplate.postForObject(baseUrl, payload, Director.class);
        assertNotNull(created);

        Long id = directorRepository.findAll().stream()
                .filter(d -> d.getFirstName().equals("John") && d.getLastName().equals("Smith"))
                .map(Director::getId)
                .findFirst()
                .orElseThrow();

        DirectorDto dto = restTemplate.getForObject(baseUrl + "/" + id, DirectorDto.class);
        assertNotNull(dto);
        assertEquals("John", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
    }

    @Test
    void testDeleteDirector() {
        Long id = directorRepository.save(newDirectorEntity("Del","Me")).getId();
        restTemplate.delete(baseUrl + "/" + id);
        try {
            restTemplate.getForEntity(baseUrl + "/" + id, DirectorDto.class);
            fail("Should 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetAllDirectorsNoContent() {
        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    void testGetAllDirectorsWithData() {
        directorRepository.save(newDirectorEntity("A","One"));
        directorRepository.save(newDirectorEntity("B","Two"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertFalse(resp.getBody().getContent().isEmpty());
    }

    @Test
    void testGetDirectorsByGender() {
        directorRepository.save(newDirectorEntity("G1","X"));
        directorRepository.save(newDirectorEntity("G2","Y"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "/gender?gender=Male",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertFalse(resp.getBody().getContent().isEmpty());
    }

    @Test
    void testGetDirectorsSortedAsc() {
        directorRepository.save(newDirectorEntity("X","Zulu"));
        directorRepository.save(newDirectorEntity("Y","Alpha"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "/sorted/A-Z",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        var list = resp.getBody().getContent();
        if (list.size() >= 2) {
            assertTrue(list.get(0).getLastName()
                    .compareToIgnoreCase(list.get(1).getLastName()) <= 0);
        }
    }

    @Test
    void testGetDirectorsSortedDesc() {
        directorRepository.save(newDirectorEntity("X","Alpha"));
        directorRepository.save(newDirectorEntity("Y","Zulu"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "/sorted/Z-A",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test
    void testGetDirectorsByFirstName() {
        directorRepository.save(newDirectorEntity("Match","A"));
        directorRepository.save(newDirectorEntity("Other","B"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "/firstName?firstName=Match",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Match", resp.getBody().getContent().get(0).getFirstName());
    }

    @Test
    void testGetDirectorsByLastName() {
        directorRepository.save(newDirectorEntity("John","Target"));
        directorRepository.save(newDirectorEntity("Jane","Else"));

        ResponseEntity<PageResponse<DirectorDto>> resp = restTemplate.exchange(
                baseUrl + "/lastName?lastName=Target",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Target", resp.getBody().getContent().getFirst().getLastName());
    }

    @Test
    void testAddAndUpdateGrade() {
        Long id = directorRepository.save(newDirectorEntity("Grade","Test")).getId();
        HttpEntity<Void> ent = new HttpEntity<>(authHeaders());

        ResponseEntity<Double> r1 = restTemplate.exchange(
                baseUrl + "/" + id + "/grade?grade=5",
                HttpMethod.POST,
                ent,
                Double.class
        );
        assertEquals(5.0, r1.getBody());

        ResponseEntity<Double> r2 = restTemplate.exchange(
                baseUrl + "/" + id + "/grade?grade=3",
                HttpMethod.POST,
                ent,
                Double.class
        );
        assertEquals(3.0, r2.getBody());
    }


    @Test
    void testDeleteNonExisting() {
        Long id = 99999L;
        try {
            restTemplate.delete(baseUrl + "/" + id);
            fail("Should 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddDirectorToMovie() {
        Director director = directorRepository.save(newDirectorEntity("Dir","Mov"));
        Long directorId = director.getId();
        Long movieId = 1L;

        HttpEntity<Void> ent = new HttpEntity<>(authHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + directorId + "/movies/" + movieId,
                HttpMethod.POST,
                ent,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movie added to director successfully.", response.getBody());
    }
}