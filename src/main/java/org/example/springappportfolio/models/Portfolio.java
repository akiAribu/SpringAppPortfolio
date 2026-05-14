package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id", nullable = false)
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "portfolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @Column(name = "views_count")
    private Integer viewsCount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "portfolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Contact> contacts = new ArrayList<>();

    @Column(name = "is_public")
    @ColumnDefault("true")
    private Boolean isPublic = true;

    @Column(name = "bio", length = 2000)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    @Column(name = "experience_years")
    private Integer experienceYears;

}
