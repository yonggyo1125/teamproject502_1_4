document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.querySelector(".sidebar");
    const buttonEl = document.getElementById("toggleBtn-mypage");
    if (sidebar && buttonEl) {
        buttonEl.addEventListener("click", function() {
            sidebar.classList.toggle("on");
        });
    }
});