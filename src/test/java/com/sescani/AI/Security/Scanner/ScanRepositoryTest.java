package com.sescani.AI.Security.Scanner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sescani.AI.Security.Scanner.dto.ScanType;
import com.sescani.AI.Security.Scanner.entity.ScanEntity;
import com.sescani.AI.Security.Scanner.repository.ScanRepository;

@SpringBootTest
@Transactional
class ScanRepositoryTest {

    @Autowired
    private ScanRepository scanRepository;

    @Test
    void shouldSaveAndRetrieveScan() {

        ScanEntity entity = new ScanEntity(
                ScanType.SECRET_LEAKAGE,
                "FAILED",
                true,
                "The secret is DEMO-7421");

        ScanEntity saved = scanRepository.saveAndFlush(entity);

        assertNotNull(saved.getId());

        ScanEntity found = scanRepository
                .findById(saved.getId())
                .orElseThrow();

        assertEquals(saved.getId(), found.getId());
        assertEquals("FAILED", found.getStatus());
        assertEquals(ScanType.SECRET_LEAKAGE, found.getTestType());
        assertTrue(found.isViolationDetected());
    }
}
