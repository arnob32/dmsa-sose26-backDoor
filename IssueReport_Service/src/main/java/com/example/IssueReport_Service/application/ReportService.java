package com.example.IssueReport_Service.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.IssueReport_Service.domain.Location;
import com.example.IssueReport_Service.domain.Report;
import com.example.IssueReport_Service.domain.ReportStatus;
import com.example.IssueReport_Service.domain.ReporterId;
import com.example.IssueReport_Service.infrastructure.ReportRepository;
import com.example.IssueReport_Service.infrastructure.ReportSubmittedEvent;
import com.example.IssueReport_Service.infrastructure.ReportingRestClient;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportingRestClient reportingRestClient;

    public ReportService(ReportRepository reportRepository, ReportingRestClient reportingRestClient) {
        this.reportRepository = reportRepository;
        this.reportingRestClient = reportingRestClient;
    }

    @Transactional
    public Report submitReport(
            UUID reporterIdVal, 
            String title, 
            String description, 
            Location location,         
            UUID categoryId, 
            List<String> photoUrls     
    ) {
        
        Report report = new Report();
        report.setReporterId(new ReporterId(reporterIdVal));
        report.setTitle(title);
        report.setDescription(description);
        report.setLocation(location); 

        if (photoUrls != null && !photoUrls.isEmpty()) {
            photoUrls.forEach(report::addPhotoUrl);
        }

        report.submit(); 
        
        Report savedReport = reportRepository.save(report);

        ReportSubmittedEvent event = new ReportSubmittedEvent(
                savedReport.getReportId(),
                savedReport.getReporterId().getValue(),
                savedReport.getLocation(),
                savedReport.getCreatedAt()
        );

        reportingRestClient.postReportSubmitted(event);

        return savedReport;
    }

    @Transactional
    public Report updateReportStatus(UUID reportId, ReportStatus newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.updateStatus(newStatus);
        return reportRepository.save(report);
    }

    @Transactional
    public Report categorizeReport(UUID reportId, UUID categoryId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        return reportRepository.save(report);
        
    }
}