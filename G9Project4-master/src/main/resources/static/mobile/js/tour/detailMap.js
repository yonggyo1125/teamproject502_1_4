window.addEventListener('DOMContentLoaded', () => {
    const mapEl = document.getElementById("map");
    const { lat, lng } = mapEl.dataset;

    const options = {
        center: { lat, lng },
        marker: {lat, lng},
        zoom: 4
    };
    mapLib.load("map", 900, 600, options);
});