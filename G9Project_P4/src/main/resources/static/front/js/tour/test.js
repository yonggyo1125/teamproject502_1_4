window.addEventListener("DOMContentLoaded", function() {
    const options = {
        center: {
            lat: 37.557756188912954,
            lng: 126.94062742683245,
        },
        marker: [
            {lat: 37.557756188912954, lng: 126.94062742683245},
            {lat: 37.557287959390024, lng: 126.94120499658828},
            {lat: 37.561184514897825, lng: 126.94069261563956},
        ],
        markerImage: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png",
    };
    mapLib.load("map1", 1000, 600, options);

});