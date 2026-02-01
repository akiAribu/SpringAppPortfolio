package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table (name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "project_preview")
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

    @OneToMany(mappedBy = "project")
    private Set<TagInProject> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<ProjectImage> projectImages = new LinkedHashSet<>();

}
