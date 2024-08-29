const search = {
    reset() {
        const {skey, areaCode, contentType, category1, category2, category3} = frmTotalSearch;
        console.log(skey?.value, areaCode?.value, contentType?.value, category1?.value, category2?.value, category3?.value);
        if (skey) skey.field = '';
        if (areaCode) areaCode.field = '';
        if (contentType) contentType.value = '';
        if (category1) category1.value = '';
        if (category2) category2.value = '';
        if (category3) category3.value = '';
    }
};
window.addEventListener("DOMContentLoaded", function () {

    const resetEl = document.getElementById("search-reset");
    resetEl.addEventListener("click", function (e) {
        search.reset();
        const {skey, areaCode, contentType, category1, category2, category3} = frmTotalSearch;
        console.log(skey?.value, areaCode?.value, contentType?.value, category1?.value, category2?.value, category3?.value);
    });
});