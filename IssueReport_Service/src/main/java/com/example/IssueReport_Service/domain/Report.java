package com.example.IssueReport_Service.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;

    @Embedded
    @AttributeOverride(name="value", column=@Column(name="reporter_id"))
    private ReporterId reporterId;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Embedded
    private Location location;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ElementCollection
    @CollectionTable(name = "report_photo_urls", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "photo_url")
    private final List<String> photoUrls = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void submit() { this.status = ReportStatus.SUBMITTED; }
    public void review() { this.status = ReportStatus.UNDER_REVIEW; }
    public void assignPriority(Priority level) { this.priority = level; }
    public void updateStatus(ReportStatus newStatus) { this.status = newStatus; }
    public void resolve() { this.status = ReportStatus.RESOLVED; }

    public UUID getReportId() { return reportId; }
    
    public ReporterId getReporterId() { return reporterId; }
    public void setReporterId(ReporterId reporterId) { this.reporterId = reporterId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void addPhotoUrl(String photoUrl) { this.photoUrls.add(photoUrl); }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}