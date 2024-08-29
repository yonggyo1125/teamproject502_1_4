window.addEventListener('DOMContentLoaded', () => {
    const mapEl = document.getElementById("map");
    const {lat, lng, address, mlevel} = mapEl.dataset;

    const options = {
        center: {lat, lng},
        marker: {lat, lng},
        zoom: mlevel
    };
    if(options.center.lat==0.0){
        return;
    }else if (options.center.lat !== undefined) {
        mapLib.load("map", 900, 600, options);
    } else if (address !== undefined) {
        try {
            mapLib.loadByAddress(address, 0, "map", 900, 600, options);
        } catch (e) {
            mapLib.loadByAddress(address.split(" ", -1), "map", 900, 600, options);
        }
    }

});