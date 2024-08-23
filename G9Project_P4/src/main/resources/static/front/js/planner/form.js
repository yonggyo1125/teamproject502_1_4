

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