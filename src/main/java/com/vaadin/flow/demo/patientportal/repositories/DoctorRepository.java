package com.vaadin.flow.demo.patientportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.demo.patientportal.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
