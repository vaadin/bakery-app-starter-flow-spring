package com.vaadin.flow.demo.patientportal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.demo.patientportal.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
