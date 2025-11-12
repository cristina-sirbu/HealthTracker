package com.healthtracker.healthtracker.medication.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "medications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","name"})
)
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 - N: each medication belongs to one user -> FK is in Medications table.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank
    private String name;

    private String form; // e.g. tablet, capsule
    private String strength; // e.g. 500mg

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}

