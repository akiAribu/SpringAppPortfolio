package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table (name = "tags_in_project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagInProject {

    @EmbeddedId
    private TagInProjectId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false, insertable = false, updatable = false)
    private Tag tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagInProject that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
