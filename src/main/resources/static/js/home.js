let selectedSpecializations = [];
let selectedExperience = [];
let selectedTags = [];
let allSpecializations = [];
let currentPage = 0;

$(document).ready(function () {
    loadSpecializations();
    loadTags();
    loadPortfolios();
});

function loadSpecializations() {
    $.ajax({
        url: "/api/v1/specializations/all",
        method: "GET",
        success: function (specs) {
            allSpecializations = specs;
            const container = $("#specializationsList");
            container.empty();
            specs.forEach(spec => {
                container.append(`
                        <div class="checkbox-item">
                            <input type="checkbox" id="spec-${spec.code}" value="${spec.code}" onchange="updateFilterButton()">
                            <label for="spec-${spec.code}">${spec.displayName}</label>
                        </div>
                    `);
            });
        }
    });
}

function loadTags() {
    $.ajax({
        url: "/api/v1/tags/all",
        method: "GET",
        success: function (tags) {
            const container = $("#tagsList");
            container.empty();
            tags.forEach(tag => {
                container.append(`
                        <div class="checkbox-item">
                            <input type="checkbox" id="tag-${tag}" value="${tag}" onchange="updateFilterButton()">
                            <label for="tag-${tag}">${tag}</label>
                        </div>
                    `);
            });
        }
    });
}

function loadPortfolios(page = 0) {
    currentPage = page;
    let params = "page=" + page + "&size=8";
    if (selectedSpecializations.length > 0) {
        params += "&" + selectedSpecializations.map(s => "specializations=" + s).join("&");
    }
    if (selectedExperience.length > 0) {
        params += "&" + selectedExperience.map(e => "experience=" + e).join("&");
    }
    if (selectedTags.length > 0) {
        params += "&" + selectedTags.map(t => "tags=" + t).join("&");
    }

    $.ajax({
        url: "/api/v1/portfolios/public?" + params,
        method: "GET",
        success: function (response) {
            if (!response || !response.content) {
                console.error("Invalid response:", response);
                return;
            }
            renderPortfolios(response.content);
            renderPagination(response);
            if (response.totalElements > 0) {
                $("#resultsCount").text(`${response.totalElements} portfolio${response.totalElements !== 1 ? 's' : ''} found`);
            } else {
                $("#resultsCount").text("");
            }
        },
        error: function(xhr, status, error) {
            console.error("Error loading portfolios:", error);
            $("#portfolioGrid").html('<div class="no-results"><h3>Error loading portfolios</h3><p>Please try again later</p></div>');
        }
    });
}

function renderPortfolios(portfolios) {
    const grid = $("#portfolioGrid");
    grid.empty();

    if (!portfolios || portfolios.length === 0) {
        $("#noResults").show();
        $("#resultsCount").text("No results");
        return;
    }

    $("#noResults").hide();
    $("#resultsCount").text("");

    portfolios.forEach(p => {
        const initials = getInitials(p.firstName, p.lastName);
        const avatarHtml = p.userImage
            ? `<img src="/api/v1/users/${p.userId}/avatar" alt="avatar">`
            : `<div class="card-avatar-placeholder">${initials}</div>`;

        const experienceText = p.experienceYears
            ? `${p.experienceYears} year${p.experienceYears !== 1 ? 's' : ''} experience`
            : '';

        grid.append(`
                <div class="portfolio-card" onclick="window.location.href='/u/${p.userId}'">
                    <div class="card-avatar">
                        ${avatarHtml}
                    </div>
                    <div class="card-body">
                        <div class="card-name">${p.fullName || p.username}</div>
                        <div class="card-specialization">${p.specializationDisplayName || 'Not specified'}</div>
                        <div class="card-experience">${experienceText}</div>
                    </div>
                </div>
            `);
    });
}

function renderPagination(response) {
    const container = $("#pagination");
    container.empty();

    if (response.totalPages <= 1) return;

    container.append(`<button onclick="loadPortfolios(${response.page - 1})" ${!response.hasPrevious ? 'disabled' : ''}>Previous</button>`);

    for (let i = 0; i < response.totalPages; i++) {
        container.append(`<button class="${i === response.page ? 'active' : ''}" onclick="loadPortfolios(${i})">${i + 1}</button>`);
    }

    container.append(`<button onclick="loadPortfolios(${response.page + 1})" ${!response.hasNext ? 'disabled' : ''}>Next</button>`);

    container.append(`<span class="page-info">Page ${response.page + 1} of ${response.totalPages} (${response.totalElements} total)</span>`);
}

function getInitials(firstName, lastName) {
    let initials = "";
    if (firstName) initials += firstName.charAt(0).toUpperCase();
    if (lastName) initials += lastName.charAt(0).toUpperCase();
    return initials || "?";
}

function toggleFilter() {
    $("#filterContent").toggleClass("show");
}

function updateFilterButton() {
    selectedSpecializations = [];
    selectedExperience = [];
    selectedTags = [];

    $("#specializationsList input:checked").each(function () {
        selectedSpecializations.push($(this).val());
    });
    $("#experienceList input:checked").each(function () {
        selectedExperience.push($(this).val());
    });
    $("#tagsList input:checked").each(function () {
        selectedTags.push($(this).val());
    });

    const totalSelected = selectedSpecializations.length + selectedExperience.length + selectedTags.length;
    const btn = $("#filterBtn");
    if (totalSelected > 0) {
        btn.addClass("active");
        btn.find("span").first().text(`Filters (${totalSelected})`);
    } else {
        btn.removeClass("active");
        btn.find("span").first().text("Filter");
    }
}

function applyFilters() {
    updateFilterButton();
    loadPortfolios(0);
    $("#filterContent").removeClass("show");
}

function clearFilters() {
    $(".checkbox-group input").prop("checked", false);
    selectedSpecializations = [];
    selectedExperience = [];
    selectedTags = [];
    loadPortfolios(0);
    $("#filterContent").removeClass("show");
    $("#filterBtn").removeClass("active");
    $("#filterBtn span").first().text("Filter");
}

$(document).click(function (e) {
    if (!$(e.target).closest('.filter-dropdown').length) {
        $("#filterContent").removeClass("show");
    }
});