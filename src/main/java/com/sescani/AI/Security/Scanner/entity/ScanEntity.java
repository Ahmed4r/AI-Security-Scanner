package com.sescani.AI.Security.Scanner.entity;

import java.time.LocalDateTime;

import com.sescani.AI.Security.Scanner.dto.ScanType;

import jakarta.persistence.*;

@Entity
@Table(name = "scans")
public class ScanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanType testType;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean violationDetected;

    @Column(columnDefinition = "TEXT")
    private String aiResponse;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ScanEntity() {
        // Required by JPA
    }

    public ScanEntity(
            ScanType testType,
            String status,
            boolean violationDetected,
            String aiResponse) {
        this.testType = testType;
        this.status = status;
        this.violationDetected = violationDetected;
        this.aiResponse = aiResponse;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public ScanType getTestType() {
        return testType;
    }

    public String getStatus() {
        return status;
    }

    public boolean isViolationDetected() {
        return violationDetected;
    }

    public String getAiResponse() {
        return aiResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}