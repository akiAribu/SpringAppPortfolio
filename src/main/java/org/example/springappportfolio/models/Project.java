package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table (name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Lob
    @JdbcTypeCode(java.sql.Types.BINARY)
    @Column(name = "project_preview", columnDefinition = "BYTEA")
    private byte[] projectPreview;

    @Column(name = "project_title")
    private String projectTitle;

    @Column(name = "full_description", length = Integer.MAX_VALUE)
    private String fullDescription;

    @Column(name = "short_description", length = Integer.MAX_VALUE)
    private String shortDescription;

    @Column(name = "project_link")
    private String projectLink;

    @ColumnDefault("true")
    @Column(name = "is_visible")
    private Boolean isVisible;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "project")
    private Set<TagInProject> tags = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProjectImage> projectImages = new LinkedHashSet<>();

}