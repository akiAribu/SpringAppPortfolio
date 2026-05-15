function showError(elementId, err) {

    console.log("Error status:", err.status);
    console.log("Response text:", err.responseText);

    let message = "An error occurred";

    try {

        if (err.responseText) {

            const json = JSON.parse(err.responseText);

            if (json.errors && json.errors.length > 0) {

                message = json.errors
                    .map(e => e.field + ": " + e.message)
                    .join(", ");

            } else if (json.message) {

                message = json.message;

            } else if (json.errorCode) {

                message = json.errorCode;
            }

        }

    } catch (e) {

        message = err.responseText || "An error occurred";

    }

    $("#" + elementId)
        .text(message)
        .css("color", "red")
        .css("white-space", "normal");
}

function togglePortfolioVisibility() {

    const currentPublic = window.portfolioData.isPublic;
    const newPublic = !currentPublic;

    $.ajax({

        url: "/api/v1/portfolios/me",
        method: "PUT",
        contentType: "application/json",

        data: JSON.stringify({
            isPublic: newPublic
        }),

        success: function () {

            location.reload();

        },

        error: function (err) {

            showError("visibilityResult", err);

        }

    });

}

function uploadAvatar() {

    const file = $("#avatarInput")[0].files[0];

    if (!file) {

        alert("Choose file");
        return;

    }

    const formData = new FormData();
    formData.append("file", file);

    $.ajax({

        url: "/api/v1/users/me/avatar",
        method: "POST",

        data: formData,

        processData: false,
        contentType: false,

        success: function () {

            $("#avatar").attr(
                "src",
                "/api/v1/users/me/avatar?t=" + new Date().getTime()
            );

        },

        error: function (err) {

            showErrorMessage(err);

        }

    });

}

function loadContacts() {

    $.ajax({
        url: "/api/v1/portfolios/me/contacts",
        method: "GET",
        success: function (contacts) {
            const container = $("#contactsContainer");
            container.empty();
            if (!contacts || contacts.length === 0) {
                $("#contactsEmpty").show();
                return;
            }
            $("#contactsEmpty").hide();
            contacts.forEach(contact => {
                let html = `
                    <div class="contact-item">

                        <div class="contact-type">
                            ${contact.contactType}
                        </div>

                        <div class="contact-value">
                            ${contact.contactValue}
                        </div>

                    </div>
                `;

                container.append(html);
            });
        }
    });
}

function showCreateProjectForm() {

    $("#createProjectForm").fadeIn(200);

}

function hideCreateProjectForm() {

    $("#createProjectForm").fadeOut(200);
    $("#createProjectResult").text("");

}

function createProject() {

    const formData = new FormData();

    formData.append(
        "title",
        $("#projectTitle").val()
    );

    formData.append(
        "shortDescription",
        $("#projectShortDesc").val()
    );

    formData.append(
        "fullDescription",
        $("#projectFullDesc").val()
    );

    formData.append(
        "projectLink",
        $("#projectLink").val()
    );

    formData.append(
        "isVisible",
        $("#projectIsVisible").is(":checked")
    );

    const previewFile = $("#projectPreview")[0].files[0];

    if (previewFile) {

        formData.append(
            "preview",
            previewFile
        );

    }

    $.ajax({

        url: "/api/v1/portfolios/me/projects/with-preview",
        method: "POST",

        data: formData,

        processData: false,
        contentType: false,
        dataType: "json",

        success: function () {

            $("#projectTitle").val("");
            $("#projectShortDesc").val("");
            $("#projectFullDesc").val("");
            $("#projectLink").val("");
            $("#projectPreview").val("");

            hideCreateProjectForm();

            loadProjects();

        },

        error: function (err) {

            showError("createProjectResult", err);

        }

    });

}

function loadProjects() {

    $.ajax({

        url: "/api/v1/portfolios/me",
        method: "GET",

        success: function (portfolio) {

            const container = $("#projectsContainer");

            container.empty();

            if (!portfolio.projects || portfolio.projects.length === 0) {

                $("#noProjects").show();
                return;

            }

            $("#noProjects").hide();

            const sortedProjects = [...portfolio.projects].sort((a, b) => {

                if (!a.createdAt && !b.createdAt) return 0;
                if (!a.createdAt) return 1;
                if (!b.createdAt) return -1;

                return new Date(b.createdAt) - new Date(a.createdAt);

            });

            sortedProjects.forEach(project => {

                let card = `
                <div class="project-card">

                    <a href="/project/${project.projectId}?portfolioId=${portfolio.portfolioId}"
                       class="project-link">

                        ${
                    project.preview
                        ? `
                            <img class="project-image"
                                 src="/api/v1/projects/${project.projectId}/preview?portfolioId=${portfolio.portfolioId}"
                                 alt="${project.title}">
                            `
                        : `
                            <div class="project-image project-placeholder">
                                No Preview
                            </div>
                            `
                }

                        <div class="project-body">

                            <h3 class="project-title">
                                ${project.title}
                            </h3>

                            <p class="project-description">
                                ${project.shortDescription || ''}
                            </p>

                        </div>

                    </a>

                    <div class="project-footer">

                        <div class="project-actions">

                            <button class="secondary-btn"
                                    onclick="editProject(${project.projectId}, '${portfolio.portfolioId}')">
                                Edit
                            </button>

                            <button class="danger-btn"
                                    onclick="deleteProject(${project.projectId}, ${portfolio.portfolioId})">
                                Delete
                            </button>

                            ${
                    project.isVisible
                        ? `
                                <button class="secondary-btn"
                                        onclick="toggleProjectVisibility(${project.projectId}, ${portfolio.portfolioId}, false)">
                                    Hide
                                </button>
                                `
                        : `
                                <button class="primary-btn"
                                        onclick="toggleProjectVisibility(${project.projectId}, ${portfolio.portfolioId}, true)">
                                    Show
                                </button>
                                `
                }

                        </div>

                    </div>

                </div>
                `;

                container.append(card);

            });

        },

        error: function (err) {

            showErrorMessage(err);

        }

    });

}

function editProject(projectId, portfolioId) {

    window.location.href =
        "/project/" + projectId + "?portfolioId=" + portfolioId;

}

function deleteProject(projectId, portfolioId) {

    if (!confirm("Delete this project?")) return;

    $.ajax({

        url:
            "/api/v1/projects/" +
            projectId +
            "?portfolioId=" +
            portfolioId,

        method: "DELETE",

        success: function () {

            loadProjects();

        },

        error: function (err) {

            showErrorMessage(err);

        }

    });

}

function toggleProjectVisibility(projectId, portfolioId, isVisible) {

    $.ajax({

        url:
            "/api/v1/projects/" +
            projectId +
            "/visibility?portfolioId=" +
            portfolioId +
            "&isVisible=" +
            isVisible,

        method: "PATCH",

        success: function () {

            loadProjects();

        },

        error: function (err) {

            showErrorMessage(err);

        }

    });

}

function showErrorMessage(err) {

    let message = err.responseText;

    try {

        const json = JSON.parse(err.responseText);

        message =
            json.message ||
            json.errorCode ||
            err.responseText;

    } catch (e) {}

    alert(message);

}

$(document).ready(function () {

    loadContacts();
    loadProjects();

});