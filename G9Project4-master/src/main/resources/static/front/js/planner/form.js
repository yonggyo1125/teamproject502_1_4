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

        // 여행 완료 체크 처리
        const tourDoneEl = tr.querySelector(".tour-done");
        this.addTourDoneEvent(tourDoneEl);

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
        const tr = document.getElementById(`item-${seq}`);
        if (tr && tr.classList.contains("done")) { // 여행완료 체크된 경우 여행지 선택 팝업 X
            return;
        }

        let url = '/planner/select/tourplace?data=' + seq;

        layerPopup.open(url, 800, 600);
    },
    /**
     * 여행 완료 토클 이벤트 추가
     *
     */
    addTourDoneEvent(el) {
        if (el) {
            el.addEventListener("click", function() {
                const tr = this.parentElement.parentElement;
                const classList = tr.classList;
                classList.remove("done");
                const dateEl = tr.querySelector("select");
                if (dateEl) dateEl.removeAttribute("readonly");

                const chkEl = tr.querySelector("input[name='chk']");
                if (chkEl) chkEl.disabled = false;

                if (this.checked) {
                    classList.add("done");
                    if (dateEl) dateEl.setAttribute("readonly", true);
                    if (chkEl) chkEl.disabled = true;
                }
            });
        }
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

    /* 양식 제출 처리 S */
    frmSave.addEventListener("submit", function(e) {
        const trs = document.querySelectorAll(".itinerary tbody tr");
        console.log(trs);
        if (trs.length > 0) {
            const items = [];
            for (const tr of trs) {
                const seq = tr.dataset.seq;
                const date = tr.querySelector(`#tour-date-${seq}`).value;
                const contentId = tr.querySelector(".content-id").value;
                const done = tr.querySelector(".tour-done").checked;

                items.push({seq, date, contentId, done});
            }

            frmSave.itinerary.value = JSON.stringify(items);
        }
    });
    /* 양식 제출 처리 E */

    // 여행 완료처리 토클 이벤트 처리
    const tourDoneEls = document.getElementsByClassName("tour-done");
    for (const el of tourDoneEls) {
        planner.addTourDoneEvent(el);
    }
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
    // 팝업 닫기
    layerPopup.close();

    const { contentId, title, address, firstImage, firstImage2 } = item;

    const imageUrl = firstImage2 ? firstImage2 : firstImage;

    const contentIdEl = document.querySelector(`[name='contentId_${seq}']`);
    const placeEl = document.getElementById("tourplace-" + seq);
    const imageEl = document.getElementById("tourplace-image-" + seq);
    const addressEl = document.getElementById("tourplace-address-" + seq);

    contentIdEl.value = contentId;
    placeEl.innerHTML = title;
    addressEl.innerHTML = address;

    if (imageUrl?.trim()) {
        const rootUrl = document.querySelector("meta[name='rootUrl']").content;
        const url = `${rootUrl}tour/detail/${contentId}`;

        imageEl.innerHTML = `<a href='${url}' target='_blank'><img src='${imageUrl}' width='50'></a>`;
    }
}