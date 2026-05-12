package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;

@Entity
@Table (name = "project_images")
@Getter
@Setter
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Lob
    @JdbcTypeCode(java.sql.Types.BINARY)
    @Column(name = "image_data", nullable = false, columnDefinition = "BYTEA")
    private byte[] imageData;

    @Column(name = "image_format", nullable = false, length = 63)
    private String imageFormat;

    @Column(name = "image_caption", length = 500)
    private String imageCaption;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}
