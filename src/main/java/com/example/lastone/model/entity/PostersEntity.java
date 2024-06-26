package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "posters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "picture", length = 64 * 1024)
    private byte[] picture;

    private int likes;
    private int days;

    @Column(name = "user_name")
    private String username;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "posterEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostersLikesEntity> postersLikesEntities;
}
