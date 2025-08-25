package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.ReportMessageDto;
import com.tcode.moviebase.Services.ReportMessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class ReportMessageController {

    private final ReportMessageService reportMessageService;


    @Operation(summary = "Report a message", description = "Allows users to report a message with a reason.")
    @PostMapping("/report/{messageId}")
    public ResponseEntity<?> reportMessage(@PathVariable Long messageId,@RequestBody(required = false) String reason) {
        if (reason == null || reason.isBlank()) {
            reason = "No reason provided";
        }

        reportMessageService.report(messageId, reason);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all reported messages", description = "Retrieves all messages that have been reported.")
    @PreAuthorize("hasRole('client_admin')")
    @GetMapping("/reported")
    public ResponseEntity<Page<ReportMessageDto>> getAllReportedMessages(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size ){
        var pageable = PageRequest.of(page, size);
        var reports = reportMessageService.getAllReports(pageable);
        if (reports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reports);
    }

    @Operation(summary = "approve a reported message", description = "Approves a reported message and deletes it from the system.")
    @PreAuthorize("hasRole('client_admin')")
    @DeleteMapping("/report/approve/{reportId}")
    public ResponseEntity<?> approveReport(@PathVariable Long reportId) {

        reportMessageService.approveReport(reportId);
        return ResponseEntity.ok("Report approved and message deleted successfully");
    }

    @Operation(summary = "Reject a reported message", description = "Rejects a reported message and removes it from the report list.")
    @PreAuthorize("hasRole('client_admin')")
    @DeleteMapping("/report/reject/{reportId}")
    public ResponseEntity<?> rejectReport(@PathVariable Long reportId) {
        reportMessageService.rejectReport(reportId);
        return ResponseEntity.ok("Report rejected successfully");
    }


}
