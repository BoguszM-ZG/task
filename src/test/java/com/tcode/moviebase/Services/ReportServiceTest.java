package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Entities.ReportComment;
import com.tcode.moviebase.Exceptions.CommentNotFoundException;
import com.tcode.moviebase.Exceptions.ReportCommentException;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.ReportCommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;



import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportCommentRepository reportCommentRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ReportService reportService;

    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    void reportComment_success() {
        Long commentId = 1L;
        String userId = "user1";
        String reason = "Spam";
        var comment = new Comment();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(reportCommentRepository.save(any(ReportComment.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = reportService.reportComment(commentId, userId, reason);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(reason, result.getReason());
        assertEquals(comment, result.getComment());
        verify(reportCommentRepository).save(any(ReportComment.class));
    }

    @Test
    void reportComment_commentNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class,
                () -> reportService.reportComment(1L, "u", "r"));
        verify(reportCommentRepository, never()).save(any());
    }

    @Test
    void approveReport_success() {
        Long reportId = 5L;
        var comment = new Comment();
        var report = new ReportComment();
        report.setComment(comment);

        when(reportCommentRepository.findById(reportId)).thenReturn(Optional.of(report));

        reportService.approveReport(reportId);

        verify(reportCommentRepository).delete(report);
        verify(commentRepository).delete(comment);
    }

    @Test
    void approveReport_notFound() {
        when(reportCommentRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ReportCommentException.class, () -> reportService.approveReport(9L));
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void rejectReport_success() {
        Long reportId = 3L;
        var report = new ReportComment();
        when(reportCommentRepository.findById(reportId)).thenReturn(Optional.of(report));

        reportService.rejectReport(reportId);

        verify(reportCommentRepository).deleteById(reportId);
    }

    @Test
    void rejectReport_notFound() {
        Long reportId = 11L;
        when(reportCommentRepository.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(ReportCommentException.class, () -> reportService.rejectReport(reportId));

        verify(reportCommentRepository, times(1)).findById(reportId);
        verify(reportCommentRepository, never()).deleteById(anyLong());
        verifyNoInteractions(commentRepository);
    }

}