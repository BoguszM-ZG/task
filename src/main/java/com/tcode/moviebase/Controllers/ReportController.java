package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Services.CommentService;
import com.tcode.moviebase.Services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    private final CommentService commentService;

    @PostMapping("/{commentId}")
    public ResponseEntity<?> reportComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId, @RequestBody String reason) {
        if (!commentService.checkIfCommentExists(commentId)) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }
        var userId = jwt.getClaimAsString("sub");
        if (userId == null) {
            return ResponseEntity.badRequest().body("You must be logged in to report a comment.");
        }
        var report = reportService.reportComment(commentId, userId, reason);
        if (report != null) {
            return ResponseEntity.ok(report.getReason());
        } else {
            return ResponseEntity.badRequest().body("Failed to report comment. Please try again.");
        }
    }


    @Operation(summary = "Approve a report", description = "Approve a report by its ID. Requires admin privileges.")
    @PostMapping("/approve/{reportId}")
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> approveReport(@PathVariable Long reportId) {
        if (!reportService.checkIfReportExists(reportId)) {
            return ResponseEntity.badRequest().body("Report does not exist.");
        }
        reportService.approveReport(reportId);
        return ResponseEntity.ok("Report approved and comment deleted.");
    }

    @Operation(summary = "Reject a report", description = "Reject a report by its ID. Requires admin privileges.")
    @PostMapping("/reject/{reportId}")
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> rejectReport(@PathVariable Long reportId) {
        if (!reportService.checkIfReportExists(reportId)) {
            return ResponseEntity.badRequest().body("Report does not exist.");
        }
        reportService.rejectReport(reportId);
        return ResponseEntity.ok("Report rejected.");
    }

    @Operation(summary = "Get all reports", description = "Retrieve all reports. Requires admin privileges.")
    @GetMapping
    @PreAuthorize("hasRole('client_admin') ")
    public ResponseEntity<?> getAllReports() {
        var reports = reportService.getAllReports();
        if (reports.isEmpty()) {
            return ResponseEntity.ok("No reports found.");
        } else {
            return ResponseEntity.ok(reports);
        }
    }
}
