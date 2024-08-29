window.addEventListener("DOMContentLoaded", function() {
    const html = "<h1>윈포윈도우 테스트</h1>";
    const options = {
        center: {
            lat: 37.557756188912954,
            lng: 126.94062742683245,
        },
        marker: [

        ],
        // markerImage: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png",
    };
    mapLib.loadByCategory("AT4", 0, "map1", 1000, 600, options);
    // mapLib.loadByAddress("서울특별시 마포구 신촌로 176", 1, "map1", 1000, 600, options)
    // mapLib.loadByKeyword("제주도 맛집", 3, "map1", 1000, 600, options);
    //mapLib.loadCurrentLocation("map1", 1000, 600, options);
    //mapLib.load("map1", 1000, 600, options);
    /*
    mapLib.load("map1", 300, 300, options);
    mapLib.load("map2", 400, 400, options);
    mapLib.load("map3", 500, 500, options);
    */
});