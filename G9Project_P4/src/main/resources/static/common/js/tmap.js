const tmapLib = {
    departure: null, // 출발지 LatLng 객체
    arrival: null, // 도착지 latLng 객체
    markers: [], // 마커
    resultDrawArr: [],
    mapId: null,
    width: '100%',
    height: '400px',
    zoom: 17,
    load(mapId, width, height, zoom) {
        this.mapId = mapId;
        this.width = width;
        this.height = height;
        this.zoom = zoom;
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
                const opt = { position : e.latLng, map }

                if (!tmapLib.departure) { // 출발지 좌표 객체
                    tmapLib.departure = e.latLng;
                    const marker = new Tmapv2.Marker(opt);
                    tmapLib.markers.push(marker);

                } else if (tmapLib.departure && !tmapLib.arrival) { // 도착지 좌표
                    tmapLib.arrival = e.latLng;

                    const marker = new Tmapv2.Marker(opt);
                    tmapLib.markers.push(marker);

                    // 경로 표기
                    tmapLib.route(map);
                }


            });
        });
    },
    /**
    * 출발지 -> 도착지 최단 경로 표기
    *
    */
    route(map) {
        const appKey = document.querySelector("meta[name='tmap_apikey']")?.content;

        if (!this.departure || !this.arrival || !appKey) {
            return;
        }

        const { ajaxLoad } = commonLib;

        const { departure, arrival } = this;

        const data = {
           startX : departure._lng,
           startY : departure._lat,
           endX : arrival._lng,
           endY : arrival._lat,
           reqCoordType : "WGS84GEO",
           resCoordType : "EPSG3857",
           startName : "출발지",
           endName : "도착지"
        };

        const headers = { appKey };
        const url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";

        (async() => {
            try {
                const response = await ajaxLoad(url, 'POST', data, headers, 'JSON');
                const resultData = response.features;
                const drawInfoArr = [];
                for (let i in resultData) {
                    const geometry = resultData[i].geometry;
                    const properties = resultData[i].properties;
                    let polyline_;

                    if (geometry.type === "LineString") { // 경로 선 표기
                        for (let j in geometry.coordinates) {
                            const latLng = new Tmapv2.Point(
                                geometry.coordinates[j][0],
                                geometry.coordinates[j][1]
                            );

                            const convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(latLng);

                            const convertChange = new Tmapv2.LatLng(
                                convertPoint._lat,
                                convertPoint._lng
                            );

                            drawInfoArr.push(convertChange);
                        }
                    } else { // geometry.type : Point - 경유지 표기
                        let markerImg = "";
                        let pType = "";
                        let size;

                       if (properties.pointType == "S") { //출발지 마커
                            /*
                           markerImg = "/upload/tmap/marker/pin_r_m_s.png";
                            */
                           pType = "S";
                            //size = new Tmapv2.Size(24, 38);
                       } else if (properties.pointType == "E") { //도착지 마커
                            //markerImg = "/upload/tmap/marker/pin_r_m_e.png";
                            pType = "E";
                            //size = new Tmapv2.Size(24, 38);
                       } else { //각 포인트 마커 - 경유지
                            //markerImg = "http://topopen.tmap.co.kr/imgs/point.png";
                            pType = "P";
                            //size = new Tmapv2.Size(8, 8);
                                    							}

                       // 경로들의 결과값들을 포인트 객체로 변환
                       const latlon = new Tmapv2.Point(
                            geometry.coordinates[0],
                            geometry.coordinates[1]);

                      // 포인트 객체를 받아 좌표값으로 다시 변환
                      const convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(latlon);

                      const routeInfoObj = {
                          //markerImage : markerImg,
                          lng : convertPoint._lng,
                          lat : convertPoint._lat,
                            pointType : pType
                          };

                     // Marker 추가
                     const marker = new Tmapv2.Marker({ position : new Tmapv2.LatLng(routeInfoObj.lat, routeInfoObj.lng),
                        //icon : routeInfoObj.markerImage,
                        //iconSize : size,
                            map});

                        tmapLib.markers.push(marker);
                    }
                } // endfor

                tmapLib.drawLine(drawInfoArr, map);

            } catch (err) {
                console.error(err);
            }
        })();
    },
    drawLine(arrPoint, map) {
        const polyline_ = new Tmapv2.Polyline({
            path : arrPoint,
        	strokeColor : "#DD0000", // 경로 선 색상
            strokeWeight : 6, // 두께
        	map : map
        });
        this.resultDrawArr.push(polyline_);
    },
    /**
    * 초기화
    *
    */
    reset() {
        this.departure = this.arrival = null;
        this.markers.forEach(m => {
            try {
                m?.setMap(null);
            } catch (e) {}
        });
        this.resultDrawArr.forEach(d => {
            try {
                d?.setMap(null);
            } catch (e) {}
        });
        this.markers = this.resultDrawArr = [];
    }
};