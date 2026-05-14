package org.example.springappportfolio.controllers.api.user;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.SpecializationDto;
import org.example.springappportfolio.models.Specialization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SpecializationApiController {

    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        List<SpecializationDto> specializations = Arrays.stream(Specialization.values())
                .map(SpecializationDto::from)
                .toList();

        return ResponseEntity.ok(specializations);
    }

}
