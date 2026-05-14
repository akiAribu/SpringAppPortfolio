package org.example.springappportfolio.dto;

import java.util.List;

public record UpdateProjectTagsRequest(
        List<String> tags
) {}
