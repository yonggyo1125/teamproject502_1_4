window.addEventListener("DOMContentLoaded", function() {
    const header = document.querySelector("header");
    window.addEventListener("scroll", function() {
        header.classList.remove("on");
        if (pageYOffset >= 100) {
            header.classList.add("on");
        }
    });

});