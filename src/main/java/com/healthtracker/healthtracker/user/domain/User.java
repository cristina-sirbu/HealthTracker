package com.healthtracker.healthtracker.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "users", 
    // In the users table, the username column must be unique — no two users can have the same username.
    uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
@Getter
@Setter
public class User {
    
    // Let the database automatically generate the ID, usually by auto-increment.
    // How it works?
    //   JPA sends an INSERT statement without the ID, and the database assigns the next available ID.
    //   After insertion, JPA retrieves the generated ID and sets it in the entity.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
