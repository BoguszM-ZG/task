package com.tcode.moviebase.Services;


import com.tcode.moviebase.Dtos.ReportCommentDto;
import com.tcode.moviebase.Entities.ReportComment;
import com.tcode.moviebase.Exceptions.CommentNotFoundException;
import com.tcode.moviebase.Exceptions.ReportCommentException;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.ReportCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class ReportService {
    private final CommentRepository commentRepository;
    private final ReportCommentRepository reportCommentRepository;

    public ReportComment reportComment(Long commentId, String userId, String reason) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment does not exist."));

            var reportComment = new ReportComment();
            reportComment.setUserId(userId);
            reportComment.setComment(comment);
            reportComment.setReason(reason);
            return reportCommentRepository.save(reportComment);
    }


    @Transactional
    public void approveReport(Long reportId) {
        var report = reportCommentRepository.findById(reportId).orElseThrow( () -> new ReportCommentException("Report does not exist."));
            reportCommentRepository.delete(report);
            commentRepository.delete(report.getComment());

    }

    @Transactional
    public void rejectReport(Long reportId) {
        reportCommentRepository.findById(reportId).orElseThrow( () -> new ReportCommentException("Report does not exist."));
        reportCommentRepository.deleteById(reportId);
    }

    public Page<ReportCommentDto> getAllReports(Pageable pageable) {
        return reportCommentRepository.findAll(pageable).map(this::reportCommentToReportCommentDto);
    }


    private ReportCommentDto reportCommentToReportCommentDto(ReportComment reportComment) {
        return new ReportCommentDto(
                reportComment.getId(),
                reportComment.getUserId(),
                reportComment.getComment().getId(),
                reportComment.getComment().getCommentText(),
                reportComment.getReason(),
                reportComment.getCreatedAt().toString()
        );
    }
}
