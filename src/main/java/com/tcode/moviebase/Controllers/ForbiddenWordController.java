package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Services.ForbiddenWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forbidden-words")
@RequiredArgsConstructor
public class ForbiddenWordController {

    private final ForbiddenWordService forbiddenWordService;


    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addForbiddenWord(@RequestBody String word) {
        forbiddenWordService.addForbiddenWord(word);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> getForbiddenWords(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(forbiddenWordService.getForbiddenWords(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> deleteForbiddenWord(@PathVariable Long id) {
        forbiddenWordService.deleteForbiddenWord(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> getForbiddenWord(@PathVariable Long id) {
        var forbiddenWord = forbiddenWordService.getForbiddenWord(id);
        return ResponseEntity.ok(forbiddenWord);
    }






}
