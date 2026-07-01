package com.example.IssueReport_Service.domain;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class ReporterId {
    private UUID value;

    public ReporterId() {}
    public ReporterId(UUID value) { this.value = value; }
    
    public Boolean equals(ReporterId other) { return this.value.equals(other.value); }
    public UUID getValue() { return value; }
    public void setValue(UUID value) { this.value = value; }
}