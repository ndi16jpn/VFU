function autoToggleNavMenu() {
    var navbarBurger = document.getElementById("navbarBurger");
    if (navbarBurger) {
        var target = document.getElementById(navbarBurger.dataset.target);
        navbarBurger.addEventListener("click", function() {
            navbarBurger.classList.toggle("is-active");
            target.classList.toggle("is-active");
        });
    }
}

function init() {
    autoToggleNavMenu();
}

window.addEventListener("DOMContentLoaded", init);
