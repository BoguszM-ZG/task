package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ForbiddenWordDto;
import com.tcode.moviebase.Entities.ForbiddenWord;
import com.tcode.moviebase.Exceptions.ForbiddenWordsException;
import com.tcode.moviebase.Repositories.ForbiddenWordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForbiddenWordServiceTest {

    @Mock
    private ForbiddenWordRepository forbiddenWordRepository;

    @InjectMocks
    private ForbiddenWordService forbiddenWordService;

    private ForbiddenWord createEntity(Long id, String word) {
        ForbiddenWord fw = new ForbiddenWord();
        fw.setId(id);
        fw.setWord(word);
        return fw;
    }

    @Test
    void addForbiddenWord_savesEntity() {
        ArgumentCaptor<ForbiddenWord> captor = ArgumentCaptor.forClass(ForbiddenWord.class);
        forbiddenWordService.addForbiddenWord("spam");
        verify(forbiddenWordRepository).save(captor.capture());
        assertEquals("spam", captor.getValue().getWord());
    }

    @Test
    void getForbiddenWords_returnsMappedDtos() {
        ForbiddenWord fw1 = createEntity(1L, "alpha");
        Page<ForbiddenWord> page = new PageImpl<>(List.of(fw1), PageRequest.of(0, 10), 1);
        when(forbiddenWordRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ForbiddenWordDto> result = forbiddenWordService.getForbiddenWords(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        ForbiddenWordDto dto = result.getContent().getFirst();
        verify(forbiddenWordRepository).findAll(any(Pageable.class));
    }

    @Test
    void deleteForbiddenWord_whenExists_deletes() {
        when(forbiddenWordRepository.existsById(5L)).thenReturn(true);
        forbiddenWordService.deleteForbiddenWord(5L);
        verify(forbiddenWordRepository).deleteById(5L);
    }

    @Test
    void deleteForbiddenWord_whenNotExists_throws() {
        when(forbiddenWordRepository.existsById(9L)).thenReturn(false);
        assertThrows(ForbiddenWordsException.class,
                () -> forbiddenWordService.deleteForbiddenWord(9L));
        verify(forbiddenWordRepository, never()).deleteById(anyLong());
    }

    @Test
    void getForbiddenWord_whenExists_returnsDto() {
        ForbiddenWord fw = createEntity(3L, "test");
        when(forbiddenWordRepository.findById(3L)).thenReturn(Optional.of(fw));

        ForbiddenWordDto dto = forbiddenWordService.getForbiddenWord(3L);

        assertEquals("test", dto.getWord());
    }

    @Test
    void getForbiddenWord_whenNotExists_throws() {
        when(forbiddenWordRepository.findById(44L)).thenReturn(Optional.empty());
        assertThrows(ForbiddenWordsException.class,
                () -> forbiddenWordService.getForbiddenWord(44L));
    }

    @Test
    void containsForbiddenWords_true_caseInsensitive() {
        ForbiddenWord fw1 = createEntity(1L, "Bad");
        ForbiddenWord fw2 = createEntity(2L, "Evil");
        when(forbiddenWordRepository.findAll()).thenReturn(List.of(fw1, fw2));

        boolean result = forbiddenWordService.containsForbiddenWords("This is a BAD example");

        assertTrue(result);
        verify(forbiddenWordRepository).findAll();
    }

    @Test
    void containsForbiddenWords_false_whenNoMatch() {
        when(forbiddenWordRepository.findAll()).thenReturn(List.of(createEntity(1L, "xxx")));
        boolean result = forbiddenWordService.containsForbiddenWords("safe content");
        assertFalse(result);
    }

    @Test
    void containsForbiddenWords_emptyList_false() {
        when(forbiddenWordRepository.findAll()).thenReturn(List.of());
        assertFalse(forbiddenWordService.containsForbiddenWords("anything"));
    }
}