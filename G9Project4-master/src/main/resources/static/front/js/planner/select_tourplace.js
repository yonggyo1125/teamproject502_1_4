window.addEventListener("DOMContentLoaded", function () {
    const items = document.getElementsByClassName("select");
    for (const el of items) {
        el.addEventListener("click", function () {
            const item = JSON.parse(this.dataset.json);
            if (typeof parent.selectTourPlaceCallback === 'function') {
                const searchParams = new URLSearchParams(location.search);
                const data = searchParams.get("data");

                parent.selectTourPlaceCallback(item, data);
            }
        });
    }
});