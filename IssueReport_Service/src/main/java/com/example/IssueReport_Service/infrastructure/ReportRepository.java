package com.example.IssueReport_Service.infrastructure;

import com.example.IssueReport_Service.domain.Report;
import com.example.IssueReport_Service.domain.ReportStatus;
import com.example.IssueReport_Service.domain.ReporterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    
    // Sesuai UML: findByReporterId(id: ReporterId) : List<Report>
    List<Report> findByReporterId(ReporterId reporterId);
    
    // Sesuai UML: findByStatus(status: ReportStatus) : List<Report>
    List<Report> findByStatus(ReportStatus status);
}