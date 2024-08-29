window.addEventListener("DOMContentLoaded", function () {
        const location = document.getElementById("orderBydistance");
        location.addEventListener("click", function (e) {
            // e.preventDefault();
            if (navigator.geolocation) {
                // GeoLocation을 이용해서 접속 위치를 얻어옵니다
                navigator.geolocation.getCurrentPosition(function (position) {

                    const currentLocation = {
                        latitude: position.coords.latitude, // 위도
                        longitude: position.coords.longitude,// 경도
                        radius: 1000
                    }
                    frmTotalSearch.latitude.value = currentLocation.latitude;
                    frmTotalSearch.longitude.value = currentLocation.longitude;
                });
            }
        })
    }
)