package com.example.IssueReport_Service.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.IssueReport_Service.domain.Location;

public record ReportSubmittedEvent(
    UUID reportId,
    UUID reporterId,
    Location location,
    LocalDateTime occurredAt
) {}