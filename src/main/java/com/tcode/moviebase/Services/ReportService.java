package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.ReportComment;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.ReportCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReportService {
    private final CommentRepository commentRepository;
    private final ReportCommentRepository reportCommentRepository;

    public ReportComment reportComment(Long commentId, String userId, String reason) {
        var comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null)
        {
            var reportComment = new ReportComment();
            reportComment.setUserId(userId);
            reportComment.setComment(comment);
            reportComment.setReason(reason);
            return reportCommentRepository.save(reportComment);
        }
        else
        {
            return null;
        }
    }

    public boolean checkIfReportExists(Long reportId) {
        return reportCommentRepository.existsById(reportId);
    }

    @Transactional
    public void approveReport(Long reportId) {
        var report = reportCommentRepository.findById(reportId).orElse(null);
        if (report != null && report.getComment() != null) {
            reportCommentRepository.delete(report);
            commentRepository.delete(report.getComment());
        }
    }

    @Transactional
    public void rejectReport(Long reportId) {
        reportCommentRepository.findById(reportId).ifPresent(reportCommentRepository::delete);
    }

    public List<ReportComment> getAllReports() {
        return reportCommentRepository.findAll();
    }
}
