window.addEventListener("DOMContentLoaded", function() {
    tmapLib.load("map");

    const resetEl = document.getElementById("reset");
    resetEl.addEventListener("click", function() {
        if (confirm("정말 다시 선택?")) {
            tmapLib.reset();
        }
    });

});