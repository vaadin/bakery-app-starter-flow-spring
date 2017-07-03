package com.vaadin.flow.demo.patientportal.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vaadin.flow.demo.patientportal.entity.Doctor;
import com.vaadin.flow.demo.patientportal.entity.Gender;
import com.vaadin.flow.demo.patientportal.entity.Patient;
import com.vaadin.flow.demo.patientportal.service.StringLongPair;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    final static int AGE_GROUP_YEARS = 10;

    @Query("SELECT p.birthDate, COUNT(*) FROM Patient p GROUP BY p.birthDate")
    List<Object[]> queryGetStatsByBirthDate();

    default List<StringLongPair> getStatsByAgeGroup() {
        LocalDate today = LocalDate.now();

        Map<Integer,Long> groupOf10s = new HashMap<>();
        queryGetStatsByBirthDate().forEach(a -> {
            int group10 = (int) ChronoUnit.YEARS.between(((Date) a[0]).toLocalDate(), today) / AGE_GROUP_YEARS;
            groupOf10s.put(group10, groupOf10s.getOrDefault(group10, 0L) + (Long)a[1]);
        });

        return groupOf10s.entrySet().stream()
                .sorted((a,b) -> a.getKey().compareTo(b.getKey()))
                .map(e -> {
                    int fromYo = e.getKey() * AGE_GROUP_YEARS;
                    int toYo = fromYo + (AGE_GROUP_YEARS - 1);
                    return new StringLongPair("" + fromYo + "-" + toYo + " years",
                            e.getValue());
                })
                .collect(Collectors.toList());
    }

    @Query("SELECT pd, COUNT(*) FROM Patient p INNER JOIN p.doctor pd GROUP BY pd")
    List<Object[]> queryGetStatsByDoctor();

    default Map<Doctor,Long> getStatsByDoctor() {
        Map<Doctor, Long> map = new HashMap<>();
        queryGetStatsByDoctor().stream().forEach(a -> map.put((Doctor)a[0], (Long)a[1]));
        return map;
    }

    @Query("SELECT p.gender, COUNT(*) FROM Patient p GROUP BY p.gender")
    List<Object[]> queryGetStatsByGender();

    default List<StringLongPair> getStatsByGender() {
        return queryGetStatsByGender().stream().map(a ->
            new StringLongPair(Optional.ofNullable((Gender)a[0]).map(g -> g.name()).orElse("undefined"),
                    (Long)a[1])).collect(Collectors.toList());
    }

}
