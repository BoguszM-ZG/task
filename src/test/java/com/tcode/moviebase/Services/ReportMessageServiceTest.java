package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Entities.ReportMessage;
import com.tcode.moviebase.Repositories.MessageRepository;
import com.tcode.moviebase.Repositories.ReportMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportMessageServiceTest {
    
    @Mock
    private ReportMessageRepository reportMessageRepository;
    
    @Mock
    private MessageRepository messageRepository;
    
    @InjectMocks
    private ReportMessageService reportMessageService;

    @Test
    void testReportMessage() {
        Message msg = new Message();
        when(messageRepository.findById(1L)).thenReturn(Optional.of(msg));
        ReportMessage report = new ReportMessage();
        when(reportMessageRepository.save(any())).thenReturn(report);

        ReportMessage result = reportMessageService.report(1L, "reason");

        assertNotNull(result);
        verify(reportMessageRepository).save(any());
    }

    @Test
    void testGetAllReports() {
        when(reportMessageRepository.findAll()).thenReturn(List.of(new ReportMessage()));
        List<ReportMessage> reports = reportMessageService.getAllReports();
        assertEquals(1, reports.size());
    }

    @Test
    void testApproveReport() {
        Message msg = new Message();
        ReportMessage report = new ReportMessage();
        report.setMessage(msg);
        when(reportMessageRepository.findById(2L)).thenReturn(Optional.of(report));

        reportMessageService.approveReport(2L);

        verify(reportMessageRepository).delete(report);
        verify(messageRepository).delete(msg);
    }

    @Test
    void testApproveReportNotFound() {
        when(reportMessageRepository.findById(3L)).thenReturn(Optional.empty());
        reportMessageService.approveReport(3L);
        verify(reportMessageRepository, never()).delete(any());
        verify(messageRepository, never()).delete(any());
    }

    @Test
    void testRejectReport() {
        reportMessageService.rejectReport(4L);
        verify(reportMessageRepository).deleteById(4L);
    }

    @Test
    void testExistById() {
        when(reportMessageRepository.existsById(5L)).thenReturn(true);
        assertTrue(reportMessageService.existsById(5L));
        when(reportMessageRepository.existsById(6L)).thenReturn(false);
        assertFalse(reportMessageService.existsById(6L));
    }
    
    
}
