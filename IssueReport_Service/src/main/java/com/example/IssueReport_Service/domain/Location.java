package com.example.IssueReport_Service.domain;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private Double latitude;
    private Double longitude;
    private String address;

    public Location() {}
    public Location(Double latitude, Double longitude, String address) {
        this.latitude = latitude; this.longitude = longitude; this.address = address;
    }
    
    public Boolean isValid() { return latitude != null && longitude != null; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}