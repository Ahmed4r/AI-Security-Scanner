package com.sescani.AI.Security.Scanner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sescani.AI.Security.Scanner.dto.ScanRequest;
import com.sescani.AI.Security.Scanner.dto.ScanResult;
import com.sescani.AI.Security.Scanner.service.ScanService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1")
public class ScanController {
    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/scans")
    public ScanResult scanAi(
            @Valid @RequestBody ScanRequest request,
            @RequestHeader(value = "X-Groq-Api-Key", required = false) String apiKey) {

        return scanService.scanAi(request, apiKey);
    }

    @GetMapping("/scans/{id}")
    public ScanResult getScanById(@PathVariable Long id) {
        return scanService.getScanById(id);
    }

    @GetMapping("/scans")
    public Page<ScanResult> getScanHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return scanService.getScanHistory(page, size);
    }

}
