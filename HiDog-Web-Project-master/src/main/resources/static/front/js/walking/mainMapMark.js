const mainMapLib = {
    departure: null, // 출발지 LatLng 객체
    arrival: null, // 도착지 LatLng 객체
    departureList: [], // DB 출발지 배열
    via: [], // 경유지 LatLng 객체 배열
    markers: [], // 마커
    viaMarkers: [], // 경유 마커
    resultDrawArr: [],
    map: null, // 지도 객체
    width: '100%',
    height: '400px',
    zoom: 17,
    currentAction: null, // start, end, via
    init() {
        const startMarkerElement = document.querySelector('[data-startMarker]');
        const startMarkerData = startMarkerElement.getAttribute('data-startMarker');
        const startMarkerArray = JSON.parse(startMarkerData);

        for (let i = 0; i < startMarkerArray.length; i += 2) {
            const lat = parseFloat(startMarkerArray[i]).toFixed(12);
            const lng = parseFloat(startMarkerArray[i + 1]).toFixed(12);

            // 마커 옵션 설정
            const opt = {
                position: new Tmapv2.LatLng(lat, lng),
                map: this.map,
                icon: 'https://notion-emojis.s3-us-west-2.amazonaws.com/prod/svg-twitter/1f415.svg',
                iconSize: new Tmapv2.Size(50, 50)
            };

            // 마커 생성
            const startMarker = new Tmapv2.Marker(opt);
            this.markers.push(startMarker);
            let clickable = true;

            let clickDeparturePoint = [];

            startMarker.addListener('click', () => {
                if (clickable) {
                    // 다른 마커 숨기기
                    this.markers.forEach(marker => {
                        if (marker !== startMarker) {
                            marker.setVisible(false);
                        }
                    });
                    this.showRoute();
                    clickable = false;
                    console.log("클릭");

                    const position = startMarker.getPosition(); // 마커의 좌표를 가져옴
                    const latFixed = parseFloat(position.lat()).toFixed(12);
                    const lngFixed = parseFloat(position.lng()).toFixed(12);
                    clickDeparturePoint.push({ "lat": latFixed, "lng": lngFixed });

                    // 출발점을 클릭한 마커 좌표로 고정함.
                    this.departure = this.arrival = position;

                    commonLib.ajaxLoad('walking/map', 'POST', {clickDeparturePoint}, {
                        "Content-Type": "application/json"
                    })
                        .then(response => {
                            console.log('Server Response:', response);
                                callback(response);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                        });

                    clickDeparturePoint = [];

                    function callback(response) {
                        const viaPoints = response;
                        mainMapLib.via = viaPoints;
                        response.forEach(point => {
                            const lat = parseFloat(point.lat);
                            const lng = parseFloat(point.lng);

                            // 마커 옵션 설정
                            const opt = {
                                position: new Tmapv2.LatLng(lat, lng),
                                map: mainMapLib.map,
                                icon: 'https://notion-emojis.s3-us-west-2.amazonaws.com/prod/svg-twitter/1f6a9.svg',
                                iconSize: new Tmapv2.Size(50, 50)
                            };

                            const viaMarker = new Tmapv2.Marker(opt);

                            mainMapLib.viaMarkers.push(viaMarker);
                        });

                        mainMapLib.route();
                    }
                } else {
                    // 모든 마커 보이기
                    this.markers.forEach(marker => {
                        marker.setVisible(true);
                    });

                    mainMapLib.viaMarkers.forEach(marker => {
                        marker.setMap(null);
                    });
                    mainMapLib.viaMarkers = [];
                    this.hideRoute();
                    clickable = true;
                    console.log("재클릭");
                    this.resultDrawArr = [];
                }
            });
        }
    },
    load(mapId, width, height, zoom) {
        this.width = width ?? '80%';
        this.height = height ?? '600px';
        this.zoom = zoom || 17;



        navigator.geolocation.getCurrentPosition((pos) => {
            const {latitude, longitude} = pos.coords;

            // 현재 위치 기반으로 지도 띄우기
            this.map = new Tmapv2.Map(mapId, {
                center: new Tmapv2.LatLng(latitude, longitude),
                width: this.width,
                height: this.height,
                zoom: this.zoom,
                zoomControl: true,
                scrollwheel: true
            });

            if (typeof this.init === 'function') {
                this.init();
            }

        })
    }
    ,
    // 경로 그리기
    async route() {
        const appKey = document.querySelector("meta[name='tmap_apikey']")?.content;

        if (!this.departure || !this.arrival || !appKey) {
            return;
        }

        const { ajaxLoad } = commonLib;

        console.log(this.via)
        // 경유지 좌표를 passList 형식으로 변환
        // 경유지 좌표를 passList 형식으로 변환
        const passList = this.via.map(point => `${point.lng},${point.lat}`).join('_');



        console.log("passList:", passList)


        const data = {
            startX: this.departure.lng(),
            startY: this.departure.lat(),
            endX: this.arrival.lng(),
            endY: this.arrival.lat(),
            passList: passList,
            reqCoordType: 'WGS84GEO',
            resCoordType: 'EPSG3857',
            startName: '출발지',
            endName: '도착지',
        };

        console.log(data.startX);
        const headers = { appKey };
        const url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";

        try {
            const response = await ajaxLoad(url, 'POST', data, headers, "JSON");
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
                            convertPoint._lat.toFixed(12),
                            convertPoint._lng.toFixed(12)
                        );
                        drawInfoArr.push(convertChange);
                    }
                }
            }

            this.drawLine(drawInfoArr);

            const departurePoints = [];
            const viaPoints = [];

            const {departure, arrival, via} = mainMapLib;
            departurePoints.push({lat: departure.lat().toFixed(12), lng: departure.lng().toFixed(12)});
            via.forEach(point => {
                viaPoints.push({lat: point.lat().toFixed(12), lng: point.lng().toFixed(12)});
            });
            if (typeof mapDrawingCallback === 'function') {
                mapDrawingCallback(departurePoints, viaPoints);
            }

        }
        catch (err) {
            console.log()
        }
    },

    // 경로 선 그리기
    drawLine(arrPoint) {
        const polyline_ = new Tmapv2.Polyline({
            path: arrPoint,
            strokeColor: '#ff0090', // 경로 선 색상
            strokeWeight: 6, // 두께
            map: this.map
        });
        this.resultDrawArr.push(polyline_);
    },

    // 경로 숨기기
    hideRoute() {
        this.resultDrawArr.forEach(d => d.setMap(null));
    },

    // 경로 표시 하기
    showRoute() {
        this.resultDrawArr.forEach(d => d.setMap(this.map));
    },

    // 초기화
    reset() {
        this.departure = this.arrival = null;
        this.via = []; // 경유점 초기화
        this.markers.forEach(m => m.setMap(null));
        this.resultDrawArr.forEach(d => d.setMap(null));
        this.markers = [];
        this.resultDrawArr = [];
    }
}
