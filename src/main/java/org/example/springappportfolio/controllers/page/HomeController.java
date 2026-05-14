package org.example.springappportfolio.controllers.page;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.dto.PortfolioPageResponse;
import org.example.springappportfolio.dto.PortfolioSearchRequest;
import org.example.springappportfolio.dto.SpecializationDto;
import org.example.springappportfolio.models.Specialization;
import org.example.springappportfolio.models.Tag;
import org.example.springappportfolio.repositories.TagRepository;
import org.example.springappportfolio.services.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PortfolioService portfolioService;
    private final TagRepository tagRepository;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/api/v1/portfolios/public")
    public ResponseEntity<PortfolioPageResponse> getPublicPortfolios(
            @RequestParam(required = false) List<String> specializations,
            @RequestParam(required = false) List<String> experience,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        List<Specialization> specEnums = null;
        if (specializations != null && !specializations.isEmpty()) {
            specEnums = specializations.stream()
                    .map(code -> {
                        try {
                            return Specialization.valueOf(code);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(s -> s != null)
                    .collect(Collectors.toList());
        }

        PortfolioSearchRequest request = new PortfolioSearchRequest(
                null,
                null,
                null,
                tags,
                specEnums,
                experience,
                page,
                8
        );

        PortfolioPageResponse response = portfolioService.searchPortfoliosPaginated(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/specializations/all")
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        return ResponseEntity.ok(
                Arrays.stream(Specialization.values())
                        .map(SpecializationDto::from)
                        .toList()
        );
    }

    @GetMapping("/api/v1/tags/all")
    public ResponseEntity<List<String>> getAllTags() {
        List<String> tags = tagRepository.findAll().stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }
}