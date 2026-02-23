package com.example.CapStoneProject.repository;

import com.example.CapStoneProject.models.EmailStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailStatusRepository extends JpaRepository<EmailStatus, UUID> {

    Optional<EmailStatus> findByEmail_Id(UUID emailId);

}