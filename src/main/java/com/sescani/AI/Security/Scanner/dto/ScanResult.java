package com.sescani.AI.Security.Scanner.dto;

public record ScanResult(
        String testType,
        String status,
        boolean violationDetected,
        String aiResponse) {
}