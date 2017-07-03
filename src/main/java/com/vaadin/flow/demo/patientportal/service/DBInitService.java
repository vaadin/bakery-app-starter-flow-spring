package com.vaadin.flow.demo.patientportal.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import com.vaadin.flow.demo.patientportal.entity.AppointmentType;
import com.vaadin.flow.demo.patientportal.entity.Doctor;
import com.vaadin.flow.demo.patientportal.entity.Gender;
import com.vaadin.flow.demo.patientportal.entity.JournalEntry;
import com.vaadin.flow.demo.patientportal.entity.Patient;
import com.vaadin.flow.demo.patientportal.repositories.DoctorRepository;
import com.vaadin.flow.demo.patientportal.repositories.PatientRepository;

@Component
@Transactional
public class DBInitService {

    public static final int NUM_DOCTORS = 20;
    public static final int NUM_PATIENTS = 100;
    public static final int MAX_JOURNAL_ENTRIES = 20;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Lorem lorem = new LoremIpsum();
    private List<Doctor> doctors;
    private Random random = new Random();
    private long medicalRecordId = random.nextInt(1000);

    public DBInitService() {
    }

    public void initDatabase() {
        createDoctors();
        createPatients();
    }

    private void createDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>(NUM_DOCTORS);

        getRandomUsers(NUM_DOCTORS, "doctors").ifPresent(result -> result.forEach(doctor -> {
            doctors.add(new Doctor(capitalize(doctor.get("name").get("first").asText()),
                    capitalize(doctor.get("name").get("last").asText())));
        }));

        doctorRepository.save(doctors);
    }


    private void createPatients() {
        doctors = doctorRepository.findAll();
        ArrayList<Patient> patients = new ArrayList<>(NUM_PATIENTS);

        getRandomUsers(NUM_PATIENTS, "patients").ifPresent(result -> result.forEach(r -> {
            Patient patient = new Patient();
            patient.setGender(Gender.valueOf(r.get("gender").asText().toUpperCase()));

            patient.setTitle(capitalize(r.get("name").get("title").asText()));
            patient.setFirstName(capitalize(r.get("name").get("first").asText()));
            patient.setMiddleName(patient.getGender() == Gender.FEMALE ? lorem.getFirstNameFemale() : lorem.getFirstNameMale());
            patient.setLastName(capitalize(r.get("name").get("last").asText()));
            patient.setSsn(r.get("id").get("value").asText());
            patient.setMedicalRecord(medicalRecordId++);

            try {
                patient.setBirthDate(df.parse(r.get("dob").asText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            patient.setDoctor(doctors.get(random.nextInt(doctors.size())));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -random.nextInt(2));
            cal.add(Calendar.MONTH, -random.nextInt(12));
            patient.setJournalEntries(Stream.generate(() -> {
                cal.add(Calendar.DAY_OF_YEAR, -random.nextInt(365));
                JournalEntry journalEntry = new JournalEntry();
                journalEntry.setDate(cal.getTime());
                journalEntry.setAppointmentType(AppointmentType.values()[random.nextInt(AppointmentType.values().length)]);
                journalEntry.setEntry(lorem.getParagraphs(1, 4));
                journalEntry.setDoctor(patient.getDoctor());

                return journalEntry;
            }).limit(random.nextInt(MAX_JOURNAL_ENTRIES)).collect(Collectors.toList()));

            patient.setPictureUrl(r.get("picture").get("large").asText());

            patients.add(patient);
        }));

        
        patientRepository.save(patients);

    }

    private Optional<JsonNode> getRandomUsers(int num, String seed) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://randomuser.me/api/?results=" + num + "&exc=login,location&nat=us&noinfo&seed=" + seed, String.class);
        ObjectMapper om = new ObjectMapper();
        try {
            return Optional.of(om.readTree(response.getBody()).get("results"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return Optional.empty();
    }

    private String capitalize(String s) {
        return String.join(" ", Arrays.stream(s.split(" "))
                .map(name -> name.substring(0, 1)
                        .toUpperCase() + name.substring(1))
                .collect(Collectors.toList())
        );
    }
}
