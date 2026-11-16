package com.sescani.AI.Security.Scanner.exception;

public class ScanNotFoundException extends RuntimeException {

    public ScanNotFoundException(Long id) {
        super("Scan not found with id: " + id);
    }
}