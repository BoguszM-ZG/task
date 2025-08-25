package com.tcode.moviebase.Services;


import com.tcode.moviebase.Dtos.ReportMessageDto;
import com.tcode.moviebase.Entities.ReportMessage;
import com.tcode.moviebase.Exceptions.MessageDoesntExistsException;
import com.tcode.moviebase.Repositories.MessageRepository;
import com.tcode.moviebase.Repositories.ReportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportMessageService {
    private final ReportMessageRepository reportMessageRepository;
    private final MessageRepository messageRepository;


    public ReportMessage report(Long messageId, String reason) {
        var message = messageRepository.findById(messageId).orElseThrow( () -> new MessageDoesntExistsException("Message doesn't exist"));
        var reportMessage = new ReportMessage();
        reportMessage.setMessage(message);
        reportMessage.setReason(reason);

        return reportMessageRepository.save(reportMessage);
    }

    public Page<ReportMessageDto> getAllReports(Pageable pageable) {
        return reportMessageRepository.findAll(pageable).map(this::reportMessageToReportMessageDto);
    }

    @Transactional
    public void approveReport(Long reportId) {
        var report = reportMessageRepository.findById(reportId).orElseThrow(() -> new MessageDoesntExistsException("Report doesn't exist"));

        reportMessageRepository.delete(report);
        messageRepository.delete(report.getMessage());

    }

    public void rejectReport(Long reportId) {
        if (reportMessageRepository.findById(reportId).isPresent()) {
            reportMessageRepository.deleteById(reportId);
        } else {
            throw new MessageDoesntExistsException("Report doesn't exist");
        }
    }


    public boolean existsById(Long reportId) {
        return reportMessageRepository.existsById(reportId);
    }

    private ReportMessageDto reportMessageToReportMessageDto(ReportMessage reportMessage) {
        var dto = new ReportMessageDto();
        dto.setReportId(reportMessage.getId());
        dto.setMessage(reportMessage.getMessage().getContent());
        dto.setReason(reportMessage.getReason());
        return dto;
    }
}
