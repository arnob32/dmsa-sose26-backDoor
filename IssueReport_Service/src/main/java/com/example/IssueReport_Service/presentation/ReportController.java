package com.example.IssueReport_Service.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.IssueReport_Service.application.ReportService;
import com.example.IssueReport_Service.domain.Location; 
import com.example.IssueReport_Service.domain.Report;
import com.example.IssueReport_Service.domain.ReportStatus;
import com.example.IssueReport_Service.infrastructure.ReportRepository;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @PostMapping
    public ResponseEntity<Report> submitReport(@RequestBody SubmitReportRequest request) {
        
        // Construct the Location Value Object from the incoming DTO fields
        Location reportLocation = new Location(
                request.latitude(), 
                request.longitude(), 
                request.address()
        );

        Report report = reportService.submitReport(
                request.reporterId(),
                request.title(),
                request.description(),
                reportLocation, 
                request.categoryId(),
                request.photoUrls()      
        );
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @PutMapping("/{reportId}/status")
    public ResponseEntity<Report> updateStatus(
            @PathVariable UUID reportId,
            @RequestParam ReportStatus newStatus) {
        Report updatedReport = reportService.updateReportStatus(reportId, newStatus);
        return ResponseEntity.ok(updatedReport);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }

    public record SubmitReportRequest(
                UUID reporterId, 
                String title, 
                String description, 
                Double latitude,      
                Double longitude,     
                String address,       
                UUID categoryId, 
                List<String> photoUrls
        ) {}
}