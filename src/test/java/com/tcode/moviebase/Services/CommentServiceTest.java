package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.CommentDto;
import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.CommentNotFoundException;
import com.tcode.moviebase.Exceptions.DeleteYourOwnCommentException;
import com.tcode.moviebase.Exceptions.ForbiddenWordsException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ForbiddenWordService forbiddenWordService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addComment_success() {
        Long movieId = 10L;
        String userId = "u1";
        String text = "test";
        Movie movie = new Movie();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(forbiddenWordService.containsForbiddenWords(text)).thenReturn(false);
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArgument(0));

        CommentDto dto = commentService.addComment(userId, movieId, text);

        assertEquals(userId, dto.getUserId());
        assertEquals(text, dto.getCommentText());
        verify(forbiddenWordService).containsForbiddenWords(text);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getUserId());
        assertEquals(text, captor.getValue().getCommentText());
        assertEquals(movie, captor.getValue().getMovie());
    }

    @Test
    void addComment_movieNotFound_throws() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class,
                () -> commentService.addComment("u", 1L, "test"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void addComment_forbiddenWords_throws() {
        Long movieId = 2L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));
        when(forbiddenWordService.containsForbiddenWords("test")).thenReturn(true);

        assertThrows(ForbiddenWordsException.class,
                () -> commentService.addComment("u", movieId, "test"));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentsByMovieId_mapsToDto() {
        Long movieId = 3L;
        Pageable pageable = PageRequest.of(0, 5);
        Comment c1 = new Comment(); c1.setUserId("u1"); c1.setCommentText("A");
        Comment c2 = new Comment(); c2.setUserId("u2"); c2.setCommentText("B");
        Page<Comment> page = new PageImpl<>(List.of(c1, c2), pageable, 2);

        when(commentRepository.findAllByMovieId(movieId, pageable)).thenReturn(page);

        Page<CommentDto> result = commentService.getCommentsByMovieId(movieId, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("A", result.getContent().getFirst().getCommentText());
    }

    @Test
    void deleteComment_success() {
        Long commentId = 5L;
        String userId = "u1";
        when(commentRepository.existsByIdAndUserId(commentId, userId)).thenReturn(true);

        commentService.deleteComment(userId, commentId);

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void deleteComment_notOwner_throws() {
        when(commentRepository.existsByIdAndUserId(6L, "uX")).thenReturn(false);
        assertThrows(DeleteYourOwnCommentException.class,
                () -> commentService.deleteComment("uX", 6L));
        verify(commentRepository, never()).deleteById(anyLong());
    }

    @Test
    void checkIfCommentExists_true() {
        when(commentRepository.findById(7L)).thenReturn(Optional.of(new Comment()));
        assertTrue(commentService.checkIfCommentExists(7L));
    }

    @Test
    void checkIfCommentExists_false() {
        when(commentRepository.findById(8L)).thenReturn(Optional.empty());
        assertFalse(commentService.checkIfCommentExists(8L));
    }

    @Test
    void getCommentById_found() {
        Long id = 9L;
        Comment c = new Comment(); c.setUserId("u1"); c.setCommentText("test");
        when(commentRepository.findById(id)).thenReturn(Optional.of(c));

        CommentDto dto = commentService.getCommentById(id);

        assertEquals("u1", dto.getUserId());
        assertEquals("test", dto.getCommentText());
    }

    @Test
    void getCommentById_notFound_throws() {
        when(commentRepository.findById(11L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class,
                () -> commentService.getCommentById(11L));
    }

    @Test
    void updateComment_success() {
        Long id = 12L;
        String userId = "owner";
        String newText = "new";
        Comment c = new Comment();
        c.setUserId(userId);
        c.setCommentText("test");

        when(commentRepository.findById(id)).thenReturn(Optional.of(c));
        when(commentRepository.existsByIdAndUserId(id, userId)).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArgument(0));

        CommentDto dto = commentService.updateComment(id, userId, newText);

        assertEquals(newText, dto.getCommentText());
        verify(commentRepository).save(c);
    }

    @Test
    void updateComment_notFound_throws() {
        when(commentRepository.findById(13L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class,
                () -> commentService.updateComment(13L, "u", "x"));
    }

    @Test
    void updateComment_notOwner_throws() {
        Long id = 14L;
        Comment c = new Comment();
        c.setUserId("realOwner");
        when(commentRepository.findById(id)).thenReturn(Optional.of(c));
        when(commentRepository.existsByIdAndUserId(id, "intruder")).thenReturn(false);

        assertThrows(DeleteYourOwnCommentException.class,
                () -> commentService.updateComment(id, "intruder", "test"));
        verify(commentRepository, never()).save(any());
    }
}