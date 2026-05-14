package org.example.springappportfolio.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class TagInProjectId implements Serializable {

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "tag_id")
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagInProjectId that)) return false;
        return Objects.equals(projectId, that.projectId)
                && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, tagId);
    }

}
