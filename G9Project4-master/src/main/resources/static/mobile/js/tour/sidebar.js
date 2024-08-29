// scripts.js
document.addEventListener("DOMContentLoaded", function() {
    var sidebar = document.getElementById("sidebar");
    var toggleBtn = document.getElementById("toggleBtn");

    toggleBtn.addEventListener("click", function() {
        if (sidebar.style.right === "0px") {
            sidebar.style.right = "-280px"; // 사이드 바 닫기
            toggleBtn.style.right = "-60px"; // 버튼 원래 위치로 이동
        } else {
            sidebar.style.right = "0px"; // 사이드 바 열기
            toggleBtn.style.right = "210px"; // 버튼도 사이드 바에 맞춰 이동
        }
    });
});
