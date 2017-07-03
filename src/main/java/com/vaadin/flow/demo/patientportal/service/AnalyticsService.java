package com.vaadin.flow.demo.patientportal.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.demo.patientportal.entity.Doctor;
import com.vaadin.flow.demo.patientportal.repositories.PatientRepository;

@Service
public class AnalyticsService {

    @Autowired
    PatientRepository patientsRepository;

    public List<StringLongPair> getStatsByAgeGroup() {
        return patientsRepository.getStatsByAgeGroup();
    }

    public List<StringLongPair> getStatsByGender() {
        return patientsRepository.getStatsByGender();
    }

    public Map<Doctor,Long> getStatsByDoctor() {
        return patientsRepository.getStatsByDoctor();
    }

}
