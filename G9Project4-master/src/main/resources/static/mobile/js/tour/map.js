window.addEventListener("DOMContentLoaded", function() {
    var placeOverlay2 = new kakao.maps.CustomOverlay({zIndex:1}),
        contentNode2 = document.createElement('div'), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
        markers2 = [], // 마커를 담을 배열입니다
        currCategory2 = ''; // 현재 선택된 카테고리를 가지고 있을 변수입니다

    var markers = [];
    var mapContainer = document.getElementById('map');
    var mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);
    var ps = new kakao.maps.services.Places();
    var ps2 = new kakao.maps.services.Places(map);
    kakao.maps.event.addListener(map, 'idle', searchPlaces2);
    contentNode2.className = 'placeinfo_wrap';
    addEventHandle2(contentNode2, 'mousedown', kakao.maps.event.preventMap);
    addEventHandle2(contentNode2, 'touchstart', kakao.maps.event.preventMap);
    addCategoryClickEvent2();
    placeOverlay2.setContent(contentNode2);
    var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
    function addEventHandle2(target, type, callback) {
        if (target.addEventListener) {
            target.addEventListener(type, callback);
        } else {
            target.attachEvent('on' + type, callback);
        }
    }
    function searchPlaces2() {
        if (!currCategory2) {
            return;
        }

        // 커스텀 오버레이를 숨깁니다
        placeOverlay2.setMap(null);

        // 지도에 표시되고 있는 마커를 제거합니다
        removeMarker2();

        ps2.categorySearch(currCategory2, placesSearchCB2, {useMapBounds:true});
    }
    function placesSearchCB2(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {

            // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
            displayPlaces2(data);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요

        } else if (status === kakao.maps.services.Status.ERROR) {
            // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요

        }
    }
    function displayPlaces2(places) {

        // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
        // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
        var order2 = document.getElementById(currCategory2).getAttribute('data-order');

        for ( var i=0; i<places.length; i++ ) {

            // 마커를 생성하고 지도에 표시합니다
            var marker2 = addMarker2(new kakao.maps.LatLng(places[i].y, places[i].x), order2);

            // 마커와 검색결과 항목을 클릭 했을 때
            // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
            (function(marker2, place) {
                kakao.maps.event.addListener(marker2, 'click', function() {
                    displayPlaceInfo2(place);
                });
            })(marker2, places[i]);
        }
    }
    function addMarker2(position, order) {
        var imageSrc2 = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
            imageSize2 = new kakao.maps.Size(27, 28),  // 마커 이미지의 크기
            imgOptions2 =  {
                spriteSize : new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
                spriteOrigin : new kakao.maps.Point(46, (order*36)), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                offset: new kakao.maps.Point(11, 28) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
            },
            markerImage2 = new kakao.maps.MarkerImage(imageSrc2, imageSize2, imgOptions2),
            marker2 = new kakao.maps.Marker({
                position: position, // 마커의 위치
                image: markerImage2
            });

        marker2.setMap(map); // 지도 위에 마커를 표출합니다
        markers2.push(marker2);  // 배열에 생성된 마커를 추가합니다

        return marker2;
    }
    function removeMarker2() {
        for ( var i = 0; i < markers2.length; i++ ) {
            markers2[i].setMap(null);
        }
        markers2 = [];
    }
    function displayPlaceInfo2 (place) {
        var content2 = '<div class="placeinfo">' +
            '   <a class="title" href="' + place.place_url + '" target="_blank" title="' + place.place_name + '">' + place.place_name + '</a>';

        if (place.road_address_name) {
            content2 += '    <span title="' + place.road_address_name + '">' + place.road_address_name + '</span>' +
                '  <span class="jibun" title="' + place.address_name + '">(지번 : ' + place.address_name + ')</span>';
        }  else {
            content2 += '    <span title="' + place.address_name + '">' + place.address_name + '</span>';
        }

        content2 += '    <span class="tel">' + place.phone + '</span>' +
            '</div>' +
            '<div class="after"></div>';

        contentNode2.innerHTML = content2;
        placeOverlay2.setPosition(new kakao.maps.LatLng(place.y, place.x));
        placeOverlay2.setMap(map);
    }

// 각 카테고리에 클릭 이벤트를 등록합니다
    function addCategoryClickEvent2() {
        var category2 = document.getElementById('category'),
            children2 = category2.children;

        for (var i=0; i<children2.length; i++) {
            children2[i].onclick = onClickCategory2;
        }
    }

// 카테고리를 클릭했을 때 호출되는 함수입니다
    function onClickCategory2() {
        var id2 = this.id,
            className2 = this.className;

        placeOverlay2.setMap(null);

        if (className2 === 'on') {
            currCategory2 = '';
            changeCategoryClass2();
            removeMarker2();
        } else {
            currCategory2 = id2;
            changeCategoryClass2(this);
            searchPlaces2();
        }
    }

// 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
    function changeCategoryClass2(el) {
        var category2 = document.getElementById('category'),
            children2 = category2.children,
            i;

        for ( i=0; i<children2.length; i++ ) {
            children2[i].className = '';
        }

        if (el) {
            el.className = 'on';
        }
    }

    function searchPlaces() {
        var keyword = document.getElementById('keyword').value;

        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            alert('키워드를 입력해주세요!');
            return false;
        }

        ps.keywordSearch(keyword, placesSearchCB);
    }

    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            displayPlaces(data);
            displayPagination(pagination);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
            return;
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
            return;
        }
    }

    function displayPlaces(places) {
        var listEl = document.getElementById('placesList');
        var menuEl = document.getElementById('menu_wrap');
        var fragment = document.createDocumentFragment();
        var bounds = new kakao.maps.LatLngBounds();

        removeAllChildNods(listEl);
        removeMarker();

        for (var i = 0; i < places.length; i++) {
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
            var marker = addMarker(placePosition, i);
            var itemEl = getListItem(i, places[i]);

            bounds.extend(placePosition);

            (function(marker, title) {
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    displayInfowindow(marker, title);
                });

                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    infowindow.close();
                });

                itemEl.onmouseover = function() {
                    displayInfowindow(marker, title);
                };

                itemEl.onmouseout = function() {
                    infowindow.close();
                };
            })(marker, places[i].place_name);

            fragment.appendChild(itemEl);
        }

        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;
        map.setBounds(bounds);
    }

    function getListItem(index, places) {
        var el = document.createElement('li');
        var itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
            '<div class="info">' +
            '   <h5>' + places.place_name + '</h5>';

        if (places.road_address_name) {
            itemStr += '    <span>' + places.road_address_name + '</span>' +
                '   <span class="jibun gray">' + places.address_name + '</span>';
        } else {
            itemStr += '    <span>' + places.address_name + '</span>';
        }

        itemStr += '  <span class="tel">' + places.phone + '</span>' +
            '</div>';

        el.innerHTML = itemStr;
        el.className = 'item';

        return el;
    }

    function addMarker(position, idx, title) {
        var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
        var imageSize = new kakao.maps.Size(36, 37);
        var imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691),
            spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10),
            offset: new kakao.maps.Point(13, 37)
        };
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
        var marker = new kakao.maps.Marker({
            position: position,
            image: markerImage
        });

        marker.setMap(map);
        markers.push(marker);

        return marker;
    }

    function removeMarker() {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
        markers = [];
    }

    function displayPagination(pagination) {
        var paginationEl = document.getElementById('pagination');
        var fragment = document.createDocumentFragment();

        while (paginationEl.hasChildNodes()) {
            paginationEl.removeChild(paginationEl.lastChild);
        }

        for (var i = 1; i <= pagination.last; i++) {
            var el = document.createElement('a');
            el.href = "#";
            el.innerHTML = i;

            if (i === pagination.current) {
                el.className = 'on';
            } else {
                el.onclick = (function(index) {
                    return function() {
                        pagination.gotoPage(index);
                    };
                })(i);
            }

            fragment.appendChild(el);
        }

        paginationEl.appendChild(fragment);
    }

    function displayInfowindow(marker, title) {
        var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

        infowindow.setContent(content);
        infowindow.open(map, marker);
    }

    function removeAllChildNods(el) {
        while (el.hasChildNodes()) {
            el.removeChild(el.lastChild);
        }
    }

    window.searchPlaces = searchPlaces; // Make searchPlaces function accessible from the form
});
