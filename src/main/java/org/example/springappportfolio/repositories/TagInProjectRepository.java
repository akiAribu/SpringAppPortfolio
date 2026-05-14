package org.example.springappportfolio.repositories;

import org.example.springappportfolio.models.TagInProject;
import org.example.springappportfolio.models.TagInProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagInProjectRepository extends JpaRepository<TagInProject, TagInProjectId> {
    void deleteByProjectId(Integer projectId);
}
