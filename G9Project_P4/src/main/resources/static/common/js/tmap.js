const tmapLib = {
    departure: null, // 출발지 LatLng 객체
    arrival: null, // 도착지 latLng 객체
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

            /**
            * 지도 좌표 클릭 처리
            *
            * 좌표는 총 2개 - 출발지 좌표, 도착 좌표
            *       출발지 좌표, 도착 좌표가 모두 체크된 경우 -> 경로 표기
            */
            map.addListener("click", function(e) {
                if (!tmapLib.departure) { // 출발지 좌표 객체
                    tmapLib.departure = e.latLng;

                } else if (tmapLib.departure && !tmapLib.arrival) { // 도착지 좌표
                    tmapLib.arrival = e.latLng;
                }

                console.log(tmapLib);
            });
        });
    }
};