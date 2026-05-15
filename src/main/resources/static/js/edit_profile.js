function parseError(err) {
    let result = { message: "", fieldErrors: [] };

    try {
        const json = JSON.parse(err.responseText);
        result.message = json.message || "";

        if (json.errors && json.errors.length > 0) {
            result.fieldErrors = json.errors;
        }
    } catch (e) {
        result.message = err.responseText;
    }

    return result;
}

function showFieldError(fieldId, error) {
    const errorDiv = $("#" + fieldId + "Error");

    if (error) {
        errorDiv
            .text(error)
            .css("color", "red")
            .css("font-size", "12px")
            .show();
    } else {
        errorDiv.hide().text("");
    }
}

function clearAllErrors() {
    $(".field-error").hide().text("");
}

function updateProfile() {
    clearAllErrors();

    const data = {
        username: $("#username").val(),
        email: $("#email").val(),
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val()
    };

    $.ajax({
        url: "/api/v1/users/me",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function () {
            $("#profileResult")
                .text("Profile updated")
                .css("color", "green");
        },

        error: function (err) {
            const errorData = parseError(err);

            if (errorData.fieldErrors.length > 0) {
                errorData.fieldErrors.forEach(e => {
                    showFieldError(e.field, e.message);
                });
            }

            $("#profileResult")
                .text(errorData.message)
                .css("color", "red");
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
                const html = `
                    <div style="margin-bottom:10px;">
                        <b>${contact.contactType}</b>: 
                        <span>${contact.contactValue}</span>

                        <button onclick="deleteContact(${contact.contactId})">Delete</button>

                        ${
                    contact.isVisible
                        ? `<button onclick="toggleVisibility(${contact.contactId}, false)">Hide</button>`
                        : `<button onclick="toggleVisibility(${contact.contactId}, true)">Show</button>`
                }
                    </div>
                `;

                container.append(html);
            });
        }
    });
}

function createContact() {
    clearAllErrors();

    const data = {
        contactType: $("#contactType").val(),
        contactValue: $("#contactValue").val()
    };

    $.ajax({
        url: "/api/v1/contacts",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function () {
            $("#contactType").val("");
            $("#contactValue").val("");
            $("#contactResult").text("");
            loadContacts();
        },

        error: function (err) {
            const errorData = parseError(err);

            if (errorData.fieldErrors.length > 0) {
                errorData.fieldErrors.forEach(e => {
                    showFieldError(e.field, e.message);
                });
            }

            $("#contactResult")
                .text(errorData.message)
                .css("color", "red");
        }
    });
}

function deleteContact(contactId) {
    $.ajax({
        url: `/api/v1/contacts/${contactId}`,
        method: "DELETE",

        success: function () {
            loadContacts();
        },

        error: function (err) {
            alert(err.responseText);
        }
    });
}

function toggleVisibility(contactId, isVisible) {
    $.ajax({
        url: `/api/v1/contacts/${contactId}/visibility?isVisible=${isVisible}`,
        method: "PATCH",

        success: function () {
            loadContacts();
        },

        error: function (err) {
            alert(err.responseText);
        }
    });
}

function loadSpecializations() {
    $.ajax({
        url: "/api/v1/specializations",
        method: "GET",

        success: function (specializations) {
            const datalist = $("#specializationList");
            datalist.empty();

            specializations.forEach(spec => {
                datalist.append(
                    `<option value="${spec.displayName}" data-code="${spec.code}"></option>`
                );
            });

            // берём значение из Thymeleaf inline script
            if (typeof currentSpec !== "undefined" && currentSpec) {
                $("#specialization").val(currentSpec);
            }
        }
    });
}

function updatePortfolio() {
    clearAllErrors();

    const selectedDisplayName = $("#specialization").val();
    const selectedOption = $(`#specializationList option[value="${selectedDisplayName}"]`);

    const specCode = selectedOption.data("code") || selectedDisplayName;

    const data = {
        specialization: specCode,
        experienceYears: $("#experienceYears").val()
            ? parseInt($("#experienceYears").val())
            : null,
        bio: $("#bio").val()
    };

    $.ajax({
        url: "/api/v1/portfolios/me",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function () {
            $("#portfolioResult")
                .text("Portfolio updated")
                .css("color", "green");
        },

        error: function (err) {
            const errorData = parseError(err);

            if (errorData.fieldErrors.length > 0) {
                errorData.fieldErrors.forEach(e => {
                    showFieldError(e.field, e.message);
                });
            }

            $("#portfolioResult")
                .text(errorData.message)
                .css("color", "red");
        }
    });
}

function changePassword() {
    clearAllErrors();

    const data = {
        oldPassword: $("#oldPassword").val(),
        newPassword: $("#newPassword").val(),
        confirmPassword: $("#confirmPassword").val()
    };

    $.ajax({
        url: "/api/v1/users/me/password",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function () {
            $("#passwordResult")
                .text("Password changed successfully")
                .css("color", "green");

            $("#oldPassword").val("");
            $("#newPassword").val("");
            $("#confirmPassword").val("");
        },

        error: function (err) {
            const errorData = parseError(err);

            if (errorData.fieldErrors.length > 0) {
                errorData.fieldErrors.forEach(e => {
                    showFieldError(e.field, e.message);
                });
            }

            $("#passwordResult")
                .text(errorData.message)
                .css("color", "red");
        }
    });
}

$(document).ready(function () {
    loadContacts();
    loadSpecializations();
});