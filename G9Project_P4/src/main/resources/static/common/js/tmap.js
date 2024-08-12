const tmapLib = {
    load(mapId, width, height, zoom) {
        navigator.geolocation.getCurrentPosition((pos) => {
            const { latitude, longitude } = pos.coords;

            // 현재 위치 기반으로 지도 띄우기
            const map = new Tmapv2.Map(mapId, {
                center : new Tmapv2.LatLng(latitude, longitude),
                width : width ?? "100%",
                height : height ?? "400px",
                zoom : zoom || 17,
                zoomControl : true,
                scrollwheel : true
            });

        });
    }
};