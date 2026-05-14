package org.example.springappportfolio.models;

public enum TechnologyTag {

    JAVA("Java"),
    SPRING("Spring"),
    SPRING_BOOT("Spring Boot"),
    HIBERNATE("Hibernate"),
    JPA("JPA"),

    PYTHON("Python"),
    DJANGO("Django"),
    FLASK("Flask"),
    FASTAPI("FastAPI"),

    JAVASCRIPT("JavaScript"),
    TYPESCRIPT("TypeScript"),
    REACT("React"),
    ANGULAR("Angular"),
    VUE("Vue"),

    HTML("HTML"),
    CSS("CSS"),
    BOOTSTRAP("Bootstrap"),
    TAILWIND("Tailwind"),

    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    MONGODB("MongoDB"),
    REDIS("Redis"),

    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    NGINX("Nginx"),

    ANDROID("Android"),
    KOTLIN("Kotlin"),

    CSHARP("C#"),
    DOTNET(".NET"),

    CPP("C++"),

    GIT("Git"),
    GITHUB("GitHub"),

    REST_API("REST API"),
    GRAPHQL("GraphQL");

    private final String displayName;

    TechnologyTag(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
