package org.example.springappportfolio.models;

public enum Specialization {

    JAVA_DEVELOPER("Java Developer"),
    PYTHON_DEVELOPER("Python Developer"),
    JAVASCRIPT_DEVELOPER("JavaScript Developer"),
    TYPESCRIPT_DEVELOPER("TypeScript Developer"),
    GO_DEVELOPER("Go Developer"),
    RUST_DEVELOPER("Rust Developer"),
    CSharp_DEVELOPER("C# Developer"),
    CPP_DEVELOPER("C++ Developer"),
    FRONTEND_DEVELOPER("Frontend Developer"),
    BACKEND_DEVELOPER("Backend Developer"),
    FULLSTACK_DEVELOPER("Fullstack Developer"),
    DEVOPS_DEVELOPER("DevOps Developer"),
    DATA_ENGINEER("Data Engineer"),
    DATA_SCIENTIST("Data Scientist"),
    ML_ENGINEER("ML Engineer"),
    MOBILE_DEVELOPER("Mobile Developer"),
    QA_ENGINEER("QA Engineer"),
    SECURITY_ENGINEER("Security Engineer"),
    UI_UX_DESIGNER("UI/UX Designer"),
    PRODUCT_MANAGER("Product Manager"),
    OTHER("Other");

    private final String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
