package com.example.lastone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posters_likes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostersLikesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String LikeID;

    @NotNull
    @Column(name = "user_name")
    private String username;

    @NotNull
    private Long ID;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_name", insertable = false, updatable = false)
    private UserEntity userEntity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID", insertable = false, updatable = false)
    private PostersEntity posterEntity;
}
