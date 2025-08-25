package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ForbiddenWordDto;
import com.tcode.moviebase.Entities.ForbiddenWord;
import com.tcode.moviebase.Exceptions.ForbiddenWordsException;
import com.tcode.moviebase.Repositories.ForbiddenWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForbiddenWordService {

    private final ForbiddenWordRepository forbiddenWordRepository;

    public void addForbiddenWord(String word) {
        ForbiddenWord forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord(word);

        forbiddenWordRepository.save(forbiddenWord);
    }

    public Page<ForbiddenWordDto> getForbiddenWords(Pageable pageable) {
        return forbiddenWordRepository.findAll(pageable).map(this::mapToDto);
    }

    public void deleteForbiddenWord(Long id) {
        if (!forbiddenWordRepository.existsById(id)) {
            throw new ForbiddenWordsException("Forbidden word not found");
        }
        forbiddenWordRepository.deleteById(id);
    }

    public ForbiddenWordDto getForbiddenWord(Long id) {
        var forbiddenWord = forbiddenWordRepository.findById(id).orElseThrow( () -> new ForbiddenWordsException("Forbidden word not found"));
        return mapToDto(forbiddenWord);
    }


    public boolean containsForbiddenWords(String text) {
        return forbiddenWordRepository.findAll().stream()
                .anyMatch(forbiddenWord -> text.toLowerCase().contains(forbiddenWord.getWord().toLowerCase()));
    }

    private ForbiddenWordDto mapToDto(ForbiddenWord forbiddenWord) {
        return new ForbiddenWordDto(forbiddenWord.getId(), forbiddenWord.getWord());
    }
}
