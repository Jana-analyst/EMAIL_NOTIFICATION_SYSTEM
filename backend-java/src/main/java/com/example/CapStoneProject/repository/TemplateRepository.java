package com.example.CapStoneProject.repository;

import com.example.CapStoneProject.models.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemplateRepository extends JpaRepository<EmailTemplate, UUID> {

}