package com.vaadin.flow.demo.patientportal.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Patient extends Person {

    private Long medicalRecord;

    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    @OneToMany(cascade = CascadeType.ALL)
    private List<JournalEntry> journalEntries = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastVisit;

    public Patient() {
    }

    public Long getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(Long medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    @PrePersist
    void prePersist() {
        if (getJournalEntries() != null && !getJournalEntries().isEmpty()) {
            lastVisit = getJournalEntries().get(getJournalEntries().size() - 1).getDate();
        }
    }

}
