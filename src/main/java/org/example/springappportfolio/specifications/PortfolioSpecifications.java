package org.example.springappportfolio.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.springappportfolio.models.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PortfolioSpecifications {

    public static Specification<Portfolio> isPublic() {
        return (root,
                query,
                cb) -> cb.isTrue(root.get("isPublic"));
    }

    public static Specification<Portfolio> hasSpecialization(Specialization specialization) {
        return (root, query, cb) ->
                cb.equal(root.get("specialization"), specialization);
    }

    public static Specification<Portfolio> excludeAdmins() {
        return (root, query, cb) -> {
            Join<Portfolio, ?> userJoin = root.join("user");
            return cb.notEqual(userJoin.get("userRole"), "ROLE_ADMIN");
        };
    }

    public static Specification<Portfolio> hasAnySpecialization(List<Specialization> specializations) {
        if (specializations == null || specializations.isEmpty()) {
            return null;
        }

        return (root, query, cb) ->
            root.get("specialization").in(specializations);

    }

    public static Specification<Portfolio> hasMinExperience(Integer minExperience) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("experienceYears"), minExperience);
    }

    public static Specification<Portfolio> hasMaxExperience(Integer maxExperience) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("experienceYears"), maxExperience);
    }

    public static Specification<Portfolio> hasAnyTag(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> {
            query.distinct(true);
            Join<Portfolio, Project> projects = root.join("projects");
            Join<Project, TagInProject> tagInProjects = projects.join("tags");
            Join<TagInProject, Tag> tags = tagInProjects.join("tag");

            return tags.get("tagName").in(tagNames);
        };

    }

    public static Specification<Portfolio> hasAllTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> {
            for (String tagName : tagNames) {
                query.distinct(true);
                Join<Portfolio, Project> projects = root.join("projects");
                Join<Project, TagInProject> tagInProjects = projects.join("tags");
                Join<TagInProject, Tag> tags = tagInProjects.join("tags");

                cb.equal(tags.get("tagName"), tagNames);
            }

            return query.getRestriction();
        };

    }

    public static Specification<Portfolio> hasNameContaining(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        return (root, cbQuery, cb) -> {
            cbQuery.distinct(true);
            Join<Portfolio, User> user = root.join("user");
            String pattern = "%" +  query.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(user.get("username")), pattern),
                    cb.like(cb.lower(user.get("firstName")), pattern),
                    cb.like(cb.lower(user.get("lastName")), pattern)
            );
        };
    }

    public static Specification<Portfolio> hasExperienceInRanges(List<String> ranges) {
        if (ranges == null || ranges.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String range : ranges) {
                switch (range) {
                    case "0":
                        predicates.add(cb.or(
                           cb.isNull(root.get("experienceYears")),
                           cb.equal(root.get("experienceYears"), 0)
                        ));
                        break;
                    case "1-3":
                        predicates.add(cb.and(
                           cb.ge(root.get("experienceYears"), 1),
                           cb.le(root.get("experienceYears"), 3)
                        ));
                        break;
                    case "3-5":
                        predicates.add(cb.and(
                                cb.gt(root.get("experienceYears"), 3),
                                cb.le(root.get("experienceYears"), 5)
                        ));
                        break;
                    case "5+":
                        predicates.add(cb.gt(root.get("experienceYears"), 5));
                        break;
                }
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

}
