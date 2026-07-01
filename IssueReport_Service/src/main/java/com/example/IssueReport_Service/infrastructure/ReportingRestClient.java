package com.example.IssueReport_Service.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class ReportingRestClient {

    private final RestClient restClient;

    public ReportingRestClient(RestClient.Builder restClientBuilder) {
        // We will update this base URL to use the Eureka Service Name later!
        this.restClient = restClientBuilder.baseUrl("http://localhost:8080").build();
    }


    @CircuitBreaker(name = "notificationService", fallbackMethod = "fallbackPostReportSubmitted")
    public void postReportSubmitted(ReportSubmittedEvent event) {
        restClient.post()
                .uri("/api/internal/events/report-submitted")
                .body(event)
                .retrieve()
                .toBodilessEntity();
        
        System.out.println("SUCCESS: Published ReportSubmittedEvent for ID: " + event.reportId());
    }

    public void fallbackPostReportSubmitted(ReportSubmittedEvent event, Throwable t) {
        System.err.println("CIRCUIT BREAKER ACTIVATED: Failed to publish event for ID: " + event.reportId());
        System.err.println("Reason: " + t.getMessage());
        

    }
}