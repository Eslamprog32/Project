package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class UserEntity {

    @Id
    @Column(name = "user_name", unique = true)
    private String username;
    @Column(name = "ssn", unique = true)
    private String SSN;
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "martal_status")
    private String martalStatus;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "role")
    private String role;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;


    @JsonManagedReference
    @OneToOne(mappedBy = "userEntity")
    private PatientEntity patientEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "userEntity")
    private DoctorEntity doctor;

    @JsonManagedReference
    @OneToMany(mappedBy = "userEntity")
    private List<WorksInEntity> organization;

}
