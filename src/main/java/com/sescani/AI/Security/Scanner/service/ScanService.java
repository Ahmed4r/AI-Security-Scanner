package com.sescani.AI.Security.Scanner.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sescani.AI.Security.Scanner.GroqClient;
import com.sescani.AI.Security.Scanner.OllamaClient;
import com.sescani.AI.Security.Scanner.analyzer.ResponseAnalyzer;
import com.sescani.AI.Security.Scanner.dto.ScanRequest;
import com.sescani.AI.Security.Scanner.dto.ScanResult;
import com.sescani.AI.Security.Scanner.entity.ScanEntity;
import com.sescani.AI.Security.Scanner.exception.ScanNotFoundException;
import com.sescani.AI.Security.Scanner.repository.ScanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class ScanService {
    private final ResponseAnalyzer responseAnalyzer;
    private final OllamaClient ollamaClient;
    private final GroqClient groqClient;
    private final ScanRepository scanRepository;

    public ScanService(ResponseAnalyzer responseAnalyzer, OllamaClient ollamaClient, ScanRepository scanRepository,
            GroqClient groqClient) {
        this.responseAnalyzer = responseAnalyzer;
        this.ollamaClient = ollamaClient;
        this.scanRepository = scanRepository;
        this.groqClient = groqClient;
    }

    public ScanResult scanAi(ScanRequest request, String apiKey) {

        String response = switch (request.provider()) {

            case GROQ -> groqClient.chat(
                    request.systemPrompt(),
                    request.attackPrompt(),
                    apiKey);

            case OLLAMA -> ollamaClient.chat(
                    request.systemPrompt(),
                    request.attackPrompt());
        };

        boolean detected = switch (request.testType()) {

            case SECRET_LEAKAGE ->
                responseAnalyzer.containsSecret(
                        response,
                        request.secret());

            case PROMPT_INJECTION ->
                responseAnalyzer.containsForbiddenOutput(
                        response,
                        request.forbiddenOutput());
        };

        String status = detected ? "FAILED" : "PASSED";

        ScanEntity entity = new ScanEntity(
                request.testType(),
                status,
                detected,
                response);

        scanRepository.save(entity);
        return new ScanResult(
                request.testType().name(),
                status,
                detected,
                response);
    }

    public ScanResult getScanById(Long id) {

        ScanEntity entity = scanRepository.findById(id)
                .orElseThrow(() -> new ScanNotFoundException(id));

        return new ScanResult(
                entity.getTestType().name(),
                entity.getStatus(),
                entity.isViolationDetected(),
                entity.getAiResponse());
    }

    public Page<ScanResult> getScanHistory(int page, int size) {

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return scanRepository.findAll(pageable)
                .map(entity -> new ScanResult(
                        entity.getTestType().name(),
                        entity.getStatus(),
                        entity.isViolationDetected(),
                        entity.getAiResponse()));
    }

}
