const planner = {
    /* 초기화 */
    init() {
        const dates = this.getDates();
        if (dates.length === 0 || !this.getTarget()) return;

        let seq = Date.now();

        for (const date of dates) {
           this.add(seq, date);
           seq++;
        }
    },
    // 여행일정 추가
    add(seq, date) {
        const domParser = new DOMParser();
        const options = this.getDates().map(s => `<option value='${s}'>${s}</option>\n`);
        let html = this.getTpl();
        html = html.replace(/\[seq\]/g, seq)
                   .replace(/\[dates\]/g, options);

        const dom = domParser.parseFromString(html, 'text/html');
        const tr = dom.querySelector("tr");
        if (date) {
            const dateEl = tr.querySelector(`select[name='date_${seq}']`);
            dateEl.value = date;
        }
        // 여행지 선택 팝업 처리
        const selectTourPlaces = tr.getElementsByClassName("select-tour-place");
        for (const el of selectTourPlaces) {
            el.addEventListener("click", function(e) {
                planner.selectTourPlace(seq);
            });
        }

        this.getTarget().append(tr);

    },
    getTarget() {
        return document.querySelector(".itinerary tbody");
    },
    /**
    * 선택 날짜 - sDate, eDate로
    * 날짜 선택 범위
    *
    */
    getDates() {
        let sDate = frmSave.sDate.value;
        let eDate = frmSave.eDate.value;
        if (!sDate || !eDate) {
            return [];
        }

        let sTime = new Date(sDate).getTime();
        let eTime = new Date(eDate).getTime();

        const dates = [];
        for (let i = sTime; i <= eTime; i += 60 * 60 * 24 * 1000) {
            const d = new Date(i);
            const strDate = `${d.getFullYear()}-${('' + (d.getMonth() + 1)).padStart(2, '0')}-${('' + d.getDate()).padStart(2, '0')}`;

            dates.push(strDate);
        }

       return dates;
    },
    getTpl() {
       return document.getElementById("tpl-planner").innerHTML;
    },
    // 전체 일정 삭제
    removeAll() {
        // 선택된 날짜 hidden 값 - sDate, eDate 값을 초기화
        frmSave.sDate.value = frmSave.eDate.value = "";

        // 달력 갱신 /calendar
        const rootUrl = document.querySelector("meta[name='rootUrl']").content;
        const url = `${rootUrl}calendar`;
        ifrmCalendar.location.href = url;

        // 여행 일정 제거
        document.querySelector(".itinerary tbody").innerHTML = "";
    },
    // 여행지 선택
    selectTourPlace(seq) {
        let url = '/planner/select/tourplace?data=' + seq;

        layerPopup.open(url, 800, 600);
    }
};

window.addEventListener("DOMContentLoaded", function() {
    const reSelectCalendar = document.getElementById("reselect-calendar");
    reSelectCalendar.addEventListener("click", function() {
        planner.removeAll();
    });

    // 전체 삭제
    const removeAll = document.querySelector(".controls .remove-all");
    removeAll.addEventListener("click", function() {
        planner.removeAll();
    });

    // 일정 추가
    const addEl = document.querySelector(".controls .add");
    addEl.addEventListener("click", function() {
        planner.add(Date.now());
    });

    // 일정 제거
    const removeEl = document.querySelector(".controls .remove");
    removeEl.addEventListener("click", function() {
        if (!confirm('정말 삭제하겠습니까?')) {
            return;
        }

        const chkChecked = document.querySelectorAll(".itinerary input[name='chk']:checked")
        if (chkChecked.length === 0) {
            alert('삭제할 여행일정을 선택하세요.');
            return;
        }

        const chks = document.querySelectorAll(".itinerary input[name='chk']");
        for (const chk of chks) {
            if (chk.checked) {
                const seq = chk.value;
                const tr = document.getElementById(`item-${seq}`);
                if (tr) tr.parentElement.removeChild(tr);
            }
        }
    });

    // 여행지 선택 하기 S
    const selectTourPlaces = document.getElementsByClassName("select-tour-place");
    for (const el of selectTourPlaces) {
        el.addEventListener("click", function() {
            const tr = this.parentElement;
            const seq = tr.dataset.seq;
            planner.selectTourPlace(seq);
        });
    }
    // 여행지 선택 하기 E
});


/**
* 날짜 선택 후속 처리
*
*/
function callbackCalendar(date) {
    if (!frmSave.sDate.value?.trim()) {
        frmSave.sDate.value = date;
    } else if (!frmSave.eDate.value?.trim()) {
        frmSave.eDate.value = date;
    }

    const rootUrl = document.querySelector("meta[name='rootUrl']").content;
    let url = `${rootUrl}calendar?`;

    let selected = 0;
    if (frmSave.sDate.value?.trim()) {
        url += `sDate=${frmSave.sDate.value.trim()}`;
        selected++;
    }

    if (frmSave.eDate.value?.trim()) {
        url += `&eDate=${frmSave.eDate.value.trim()}`;
        selected++;
    }

    ifrmCalendar.location.href=url;
    const trs = document.querySelectorAll(".itinerary tbody tr");
    if (selected === 2 && trs.length === 0) {
        planner.init(); // 선택 일정만큼 입력 항목 생성
    }
}

/**
* 여행지 팝업 선택 콜백 처리
*
*/
function selectTourPlaceCallback(item, seq) {
    console.log("item", item, seq);
    if (!item || !planner.callbackTarget) return;
    console.log(item);
    const { contentId, title, address, firstImage, firstImage2 } = item;
    const targetEl = planner.callbackTarget;

    const imageUrl = firstImage2 ? firstImage2 : firstImage;

    const contentIdEl = targetEl.querySelector(`[name='content_id_${seq}']`);
    const placeEl = targetEl.querySelector("#tourplace-" + seq);
    const imageEl = targetEl.querySelector("#tourplace-image-" + seq);
    const addressEl = targetEl.querySelector("#tourplace-address-" + seq);

    contentIdEl.value = contentId;
    placeEl.innerHTML = title;
    addressEl.innerHTML = address;

    if (imageUrl?.trim()) {
        const img = new Image();
        img.src = imageUrl.trim();
        img.width=100
        imageEl.innerHTML = img;
    }
}