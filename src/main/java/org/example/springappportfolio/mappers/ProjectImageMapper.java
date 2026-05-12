package org.example.springappportfolio.mappers;

import org.example.springappportfolio.dto.ProjectImageDto;
import org.example.springappportfolio.models.ProjectImage;
import org.springframework.stereotype.Component;

@Component
public class ProjectImageMapper {

    public ProjectImageDto toDto(ProjectImage image){
        if (image == null) {
           return null;
        }

        return new ProjectImageDto(
                image.getId(),
                image.getImageData(),
                image.getImageFormat(),
                image.getImageCaption(),
                image.getCreatedAt()
        );
    }

}
