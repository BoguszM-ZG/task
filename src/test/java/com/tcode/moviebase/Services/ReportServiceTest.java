package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Entities.ReportComment;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.ReportCommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportCommentRepository reportCommentRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testReportComment() {
        Long commentId = 1L;
        String userId = "user1";
        String reason = "Inappropriate content";
        var comment = new Comment();
        var report = new ReportComment();
        report.setUserId(userId);
        report.setReason(reason);
        report.setComment(comment);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(reportCommentRepository.save(org.mockito.ArgumentMatchers.any(ReportComment.class))).thenReturn(report);

        var result = reportService.reportComment(commentId, userId, reason);
        assertNotNull(result);
        assertNotNull(result.getComment());
        assertNotNull(result.getUserId());
        assertNotNull(result.getReason());
    }

    @Test
    void testCheckIfReportExists() {
        Long reportId = 1L;
        when(reportCommentRepository.existsById(reportId)).thenReturn(true);
        var exists = reportService.checkIfReportExists(reportId);
        assertTrue(exists);
    }


    @Test
    void testApproveReport() {
        Long reportId = 1L;
        var report = new ReportComment();
        var comment = new Comment();
        report.setComment(comment);

        when(reportCommentRepository.findById(reportId)).thenReturn(Optional.of(report));

        reportService.approveReport(reportId);

        verify(reportCommentRepository).delete(report);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testRejectReport() {
        Long reportId = 1L;
        var report = new ReportComment();

        when(reportCommentRepository.findById(reportId)).thenReturn(Optional.of(report));
        reportService.rejectReport(reportId);
        verify(reportCommentRepository).delete(report);
    }

    @Test
    void testGetAllReports() {
        reportService.getAllReports();
        verify(reportCommentRepository).findAll();
    }

    @Test
    void testReportCommentNotFound() {
        Long commentId = 1L;
        String userId = "user1";
        String reason = "Inappropriate content";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());
        var result = reportService.reportComment(commentId, userId, reason);
        assertNull(result);
    }
}
