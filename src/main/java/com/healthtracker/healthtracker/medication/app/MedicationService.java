package com.healthtracker.healthtracker.medication.app;

import com.healthtracker.healthtracker.medication.domain.Medication;
import com.healthtracker.healthtracker.medication.infra.MedicationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public Medication create(Long userId, String name, String form, String strength) {
        if (userId == null || name == null || name.isBlank()) {
            throw new IllegalArgumentException("UserId and name are required");
        }
        try {
            Medication m = new Medication();
            m.setUserId(userId);
            m.setName(name);
            m.setForm(form);
            m.setStrength(strength);
            return medicationRepository.save(m);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Medication with this name already exists for this user");
        }
    }

    public List<Medication> getMedicationsByUserId(Long userId) {
        return medicationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<Medication> getMedicationByUserId(Long userId, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        return medicationRepository.findByUserId(userId, pageable);
    }
}
