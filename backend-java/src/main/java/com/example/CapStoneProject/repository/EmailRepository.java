package com.example.CapStoneProject.repository;

import com.example.CapStoneProject.models.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<EmailMessage, UUID> {

}