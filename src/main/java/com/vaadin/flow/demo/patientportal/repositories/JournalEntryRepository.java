package com.vaadin.flow.demo.patientportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.demo.patientportal.entity.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}
