$(document).ready(function () {

    loadAvailableTags();
    renderCurrentTags();

    $(document).click(function (e) {

        if (!$(e.target).closest(".tag-dropdown").length) {
            $("#tagsDropdownMenu").removeClass("open");
        }

        if ($(e.target).is("#imageModalOverlay")) {
            closeImageModal();
        }

        if ($(e.target).is("#editModalOverlay")) {
            hideEditForm();
        }
    });
});

function toggleTagsDropdown() {
    $("#tagsDropdownMenu").toggleClass("open");
}

function showError(elementId, err) {

    let message = err.responseText;

    try {

        const json = JSON.parse(err.responseText);

        if (json.errors && json.errors.length > 0) {
            message = json.errors.map(e => e.field + ": " + e.message).join("\n");
        } else if (json.message) {
            message = json.message;
        }

    } catch (e) {}

    $("#" + elementId).text(message).css("color", "red");
}

function showErrorAlert(err) {

    let message = err.responseText;

    try {

        const json = JSON.parse(err.responseText);

        message = json.message || json.errorCode || err.responseText;

    } catch (e) {}

    alert(message);
}

function showEditForm() {
    $("#editModalOverlay").addClass("active");
    $("body").css("overflow", "hidden");
}

function hideEditForm() {
    $("#editModalOverlay").removeClass("active");
    $("body").css("overflow", "auto");
    $("#editResult").text("");
}

function updateProject() {

    const data = {
        title: $("#editTitle").val(),
        shortDescription: $("#editShortDescription").val(),
        fullDescription: $("#editFullDescription").val(),
        projectLink: $("#editLink").val()
    };

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "?portfolioId=" + window.portfolioId,
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function () {
            location.reload();
        },

        error: function (err) {
            showError("editResult", err);
        }
    });
}

function deleteProject() {

    if (!confirm("Delete this project?")) {
        return;
    }

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "?portfolioId=" + window.portfolioId,
        method: "DELETE",

        success: function () {
            window.location.href = "/profile";
        },

        error: function (err) {
            showErrorAlert(err);
        }
    });
}

function addImage() {

    const file = $("#imageFile")[0].files[0];

    if (!file) {
        $("#imageResult").text("Please select a file");
        return;
    }

    const formData = new FormData();

    formData.append("file", file);
    formData.append("caption", $("#imageCaption").val());

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "/images?portfolioId=" + window.portfolioId,
        method: "POST",
        data: formData,
        processData: false,
        contentType: false,

        success: function () {

            $("#imageFile").val("");
            $("#imageCaption").val("");
            $("#imageResult").text("Image added");

            location.reload();
        },

        error: function (err) {
            showError("imageResult", err);
        }
    });
}

function uploadPreview() {

    const file = $("#previewFile")[0].files[0];

    if (!file) {
        $("#previewResult").text("Please select a file");
        return;
    }

    const formData = new FormData();

    formData.append("file", file);

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "/preview?portfolioId=" + window.portfolioId,
        method: "POST",
        data: formData,
        processData: false,
        contentType: false,

        success: function () {

            $("#previewFile").val("");
            $("#previewResult").text("Preview updated");

            location.reload();
        },

        error: function (err) {
            showError("previewResult", err);
        }
    });
}

function renderCurrentTags() {

    const tags = window.projectTagNames;
    const container = $("#projectTags");

    container.empty();

    if (!tags || tags.length === 0) {

        container.append(`
            <span class="muted">
                No technologies specified
            </span>
        `);

        return;
    }

    tags.forEach(tag => {

        let html = `
            <div class="tag-pill">
                <span>${tag}</span>
        `;

        if (window.isOwner) {

            html += `
                <button class="tag-remove"
                        onclick="removeTag('${tag}')">
                    ×
                </button>
            `;
        }

        html += `</div>`;

        container.append(html);
    });
}

function loadAvailableTags() {

    const currentTagNames = window.projectTagNames || [];

    $.ajax({
        url: "/api/v1/tags",
        method: "GET",

        success: function (tags) {

            const container = $("#tagsCheckboxes");

            container.empty();

            tags.forEach(tag => {

                const checked = currentTagNames.includes(tag.name)
                    ? "checked"
                    : "";

                container.append(`
                    <label class="tag-checkbox">
                        <input type="checkbox"
                               value="${tag.name}"
                               ${checked}>
                        <span>${tag.name}</span>
                    </label>
                `);
            });
        }
    });
}

function saveTags() {

    const selected = [];

    $("#tagsCheckboxes input:checked").each(function () {
        selected.push($(this).val());
    });

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "/tags?portfolioId=" + window.portfolioId,
        method: "PUT",
        contentType: "application/json",

        data: JSON.stringify({
            tags: selected
        }),

        success: function () {
            location.reload();
        },

        error: function (err) {
            showError("tagsResult", err);
        }
    });
}

function removeTag(tagName) {

    const currentTags = (window.projectTagNames || [])
        .filter(t => t !== tagName);

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "/tags?portfolioId=" + window.portfolioId,
        method: "PUT",
        contentType: "application/json",

        data: JSON.stringify({
            tags: currentTags
        }),

        success: function () {
            location.reload();
        },

        error: function (err) {
            showErrorAlert(err);
        }
    });
}

function openImageModal(element) {

    const imageUrl = $(element).data("image-url");
    const imageCaption = $(element).data("image-caption");
    const imageId = $(element).data("image-id");

    $("#modalImage").attr("src", imageUrl);

    $("#modalImageCaption").text(
        imageCaption ? imageCaption : ""
    );

    if (window.isOwner) {

        $("#modalDeleteButton")
            .show()
            .off("click")
            .on("click", function () {
                deleteImage(imageId);
            });

    } else {

        $("#modalDeleteButton").hide();
    }

    $("#imageModalOverlay").addClass("active");

    $("body").css("overflow", "hidden");
}

function closeImageModal() {

    $("#imageModalOverlay").removeClass("active");

    $("body").css("overflow", "auto");
}

function deleteImage(imageId) {

    if (!confirm("Delete this image?")) {
        return;
    }

    $.ajax({
        url: "/api/v1/projects/" + window.projectId + "/images/" + imageId + "?portfolioId=" + window.portfolioId,
        method: "DELETE",

        success: function () {
            location.reload();
        },

        error: function (err) {
            showErrorAlert(err);
        }
    });
}