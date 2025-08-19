package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.ReportMessage;
import com.tcode.moviebase.Repositories.MessageRepository;
import com.tcode.moviebase.Repositories.ReportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportMessageService {
    private final ReportMessageRepository reportMessageRepository;
    private final MessageRepository messageRepository;


    public ReportMessage report(Long messageId, String reason) {
        var message = messageRepository.findById(messageId).orElse(null);
        var reportMessage = new ReportMessage();
        reportMessage.setMessage(message);
        reportMessage.setReason(reason);

        return reportMessageRepository.save(reportMessage);
    }

    public List<ReportMessage> getAllReports() {
        return reportMessageRepository.findAll();
    }

    @Transactional
    public void approveReport(Long reportId) {
        var report = reportMessageRepository.findById(reportId).orElse(null);
        if (report != null && report.getMessage() != null) {
            reportMessageRepository.delete(report);
            messageRepository.delete(report.getMessage());
        }
    }

    public void rejectReport(Long reportId) {
        reportMessageRepository.deleteById(reportId);
    }


    public boolean existsById(Long reportId) {
        return reportMessageRepository.existsById(reportId);
    }
}
