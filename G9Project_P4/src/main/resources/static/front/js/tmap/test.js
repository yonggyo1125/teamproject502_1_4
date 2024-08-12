window.addEventListener("DOMContentLoaded", function() {
    // 1. 지도 띄우기
    map = new Tmapv2.Map("map", {
        center : new Tmapv2.LatLng(37.56520450, 126.98702028),
    		width : "100%",
    		height : "400px",
    		zoom : 17,
    		zoomControl : true,
    		scrollwheel : true
    	});


    // 2. 시작, 도착 심볼찍기
    // 시작
    marker_s = new Tmapv2.Marker(
    				{
    					position : new Tmapv2.LatLng(37.564991,126.983937),
    					/*icon : "/upload/tmap/marker/pin_r_m_s.png",
    					iconSize : new Tmapv2.Size(24, 38), */
    					map : map
    				});

    // 도착
    	marker_e = new Tmapv2.Marker(
    				{
    					position : new Tmapv2.LatLng(37.566158,126.988940),
    					/* icon : "/upload/tmap/marker/pin_r_m_e.png",
    					iconSize : new Tmapv2.Size(24, 38), */
    					map : map
    				});


    const data = {
                 					"startX" : "126.983937",
                 					"startY" : "37.564991",
                 					"endX" : "126.988940",
                 					"endY" : "37.566158",
                 					"reqCoordType" : "WGS84GEO",
                 					"resCoordType" : "EPSG3857",
                 					"startName" : "출발지",
                 					"endName" : "도착지"
                 				};


    const { ajaxLoad } = commonLib;

    (async() => {
        const headers = { appKey: "AMJpAtQzfg6RGV9RB1oHM3ZaCPp0T0Qx4nYhDbCZ"};
        const url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";
        try {
            const response = await ajaxLoad(url, "POST", data, headers, "JSON");
            console.log(response);
        } catch (err) {
            console.log(err);
        }
    })();


});


function addComma(num) {
		var regexp = /\B(?=(\d{3})+(?!\d))/g;
		return num.toString().replace(regexp, ',');
	}

	function drawLine(arrPoint) {
		var polyline_;

		polyline_ = new Tmapv2.Polyline({
			path : arrPoint,
			strokeColor : "#DD0000",
			strokeWeight : 6,
			map : map
		});
		resultdrawArr.push(polyline_);
	}