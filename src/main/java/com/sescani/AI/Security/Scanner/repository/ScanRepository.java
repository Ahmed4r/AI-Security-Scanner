package com.sescani.AI.Security.Scanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sescani.AI.Security.Scanner.entity.ScanEntity;

public interface ScanRepository extends JpaRepository<ScanEntity, Long> {

}
