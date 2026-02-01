package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "tags_in_project")
@Data
public class TagInProject {

    @EmbeddedId
    private TagInProjectId id;

    @MapsId ("projectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @MapsId ("tagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

}
