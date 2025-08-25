package com.tcode.moviebase.Controllers;



import com.tcode.moviebase.Services.CommentService;
import com.tcode.moviebase.Services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    public final ReportService reportService;

    @PostMapping("/{commentId}")
    public ResponseEntity<?> reportComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId, @RequestBody String reason) {

        var userId = jwt.getClaimAsString("sub");

        var report = reportService.reportComment(commentId, userId, reason);

        return ResponseEntity.ok(report.getReason());
    }


    @Operation(summary = "Approve a report", description = "Approve a report by its ID. Requires admin privileges.")
    @PostMapping("/approve/{reportId}")
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> approveReport(@PathVariable Long reportId) {
        reportService.approveReport(reportId);
        return ResponseEntity.ok("Report approved and comment deleted.");
    }

    @Operation(summary = "Reject a report", description = "Reject a report by its ID. Requires admin privileges.")
    @PostMapping("/reject/{reportId}")
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> rejectReport(@PathVariable Long reportId) {
        reportService.rejectReport(reportId);
        return ResponseEntity.ok("Report rejected.");
    }

    @Operation(summary = "Get all reports", description = "Retrieve all reports. Requires admin privileges.")
    @GetMapping
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> getAllReports(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var reports = reportService.getAllReports(pageable);
        if (reports.isEmpty()) {
            return ResponseEntity.ok("No reports found.");
        } else {
            return ResponseEntity.ok(reports);
        }
    }
}
