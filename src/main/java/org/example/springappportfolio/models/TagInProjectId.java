package org.example.springappportfolio.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class TagInProjectId implements Serializable {

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "tag_id")
    private String tagId;

}
