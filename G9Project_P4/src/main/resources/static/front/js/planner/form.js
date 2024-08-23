window.addEventListener("DOMContentLoaded", function() {
    const reSelectCalendar = document.getElementById("reselect-calendar");
    reSelectCalendar.addEventListener("click", function() {
        // 선택된 날짜 hidden 값 - sDate, eDate 값을 초기화
        frmSave.sDate.value = frmSave.eDate.value = "";

        // 달력 갱신 /calendar
        const rootUrl = document.querySelector("meta[name='rootUrl']").content;
        const url = `${rootUrl}calendar`;
        ifrmCalendar.location.href = url;
    });
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
    if (frmSave.sDate.value?.trim()) {
        url += `sDate=${frmSave.sDate.value.trim()}`;
    }

    if (frmSave.eDate.value?.trim()) {
        url += `&eDate=${frmSave.eDate.value.trim()}`;
    }

    ifrmCalendar.location.href=url;
}