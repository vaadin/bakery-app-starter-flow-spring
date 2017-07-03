package com.vaadin.flow.demo.patientportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.demo.patientportal.entity.Patient;
import com.vaadin.flow.demo.patientportal.repositories.PatientRepository;

/**
 * @author mstahv
 */
@Service
@Transactional
public class PatientService {

    @Autowired
    PatientRepository repo;

    public Patient findAttached(Patient p) {
        if (p.isPersistent()) {
            p = repo.findOne(p.getId());
            p.getJournalEntries().size();
        }
        return p;
    }

}
