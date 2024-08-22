window.addEventListener("DOMContentLoaded", function() {
    (async() => {
        try {
            const { editorLoad } = commonLib;
            const editor = await editorLoad("content");
            if (editor) window.editor = editor;
        } catch (err) {}
    })();
});


/**
 * 파일 업로드 후속 처리
 *
 * files : 업로드한 파일 목록
 */
function fileUploadCallback(files) {
    if (!files || files.length === 0) {
        return;
    }

    // 에디터에 첨부할 이미지 URL
    const imageUrls = [];

    // 파일 업로드 location 별 파일 목록 템플릿
    const attachTpl = document.getElementById("attach-file-tpl").innerHTML;
    const editorTpl = document.getElementById("editor-file-tpl").innerHTML;

    const attachTarget = document.getElementById("uploaded-files-attach");
    const editorTarget = document.getElementById("uploaded-files-editor");

    const domParser = new DOMParser();

    for (const file of files) {
        const { seq, location, fileUrl, fileName } = file;

        const target = location === 'editor' ? editorTarget : attachTarget;
        let html = location === 'editor' ? editorTpl : attachTpl;

        html = html.replace(/\[seq\]/g, seq)
            .replace(/\[fileName\]/g, fileName)
            .replace(/\[fileUrl\]/g, fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(".file-item");

        target.append(el);

        if (location === 'editor') { // 에디터 첨부
            imageUrls.push(fileUrl);

            const insertEditorEl = el.querySelector(".insert-editor");
            if (insertEditorEl) {
                insertEditorEl.addEventListener("click", (e) => insertEditor(e.currentTarget.dataset.url));
            }
        }

        // 파일 삭제 이벤트 처리
        const removeEl = el.querySelector(".remove");
        removeEl.addEventListener("click", () => {
            if (confirm('정말 삭제하겠습니까?')) {
                fileManager.delete(seq);
            }
        });

    }

    // 에디터 본문에 이미지 추가
    if (imageUrls.length > 0) {
        insertEditor(imageUrls);
    }
}

function insertEditor(source) {
    editor.execute("insertImage", { source });
}


/**
 * 파일 삭제 후속 처리
 *
 */
function fileDeleteCallback(file) {
    if (!file) return;

    const { seq } = file;

    const el = document.getElementById(`file-${seq}`);
    el.parentElement.removeChild(el);
}


/**
 * 맵 실행 및 버튼 기능 추가
 */
window.addEventListener("DOMContentLoaded", function() {
    tmapLib.load("mapId");

    // 버튼 이벤트 리스너
    const startEl = document.getElementById("start");
    startEl.addEventListener("click", () => tmapLib.currentAction = 'start');

    const viasEl = document.getElementById("vias");
    viasEl.addEventListener("click", () => tmapLib.currentAction = 'via');

    const completeEl = document.getElementById("complete");
    completeEl.addEventListener("click", () => {
        if (tmapLib.resultDrawArr.length == 0) {
            tmapLib.route(tmapLib.mapId)

        } else {
            console.log(tmapLib.resultDrawArr)
            return;
        }
        viasEl.style.display = 'none';
        startEl.style.display = 'none';
    });


    const resetEl = document.getElementById("reset");
    resetEl.addEventListener("click", function() {
        if (confirm("정말 다시 선택 하시겠습니까??")) {
            viasEl.style.display = '';
            startEl.style.display = '';
            tmapLib.reset();
        }
    });

});

/**
 * form 으로 json 데이터 전송
 * @param departurePoints   : 시작 마커 좌표
 * @param viaPoints   : 경유 마커 좌표
 */
function mapDrawingCallback(departurePoints, viaPoints) {
    const json = JSON.stringify(departurePoints);
    const json2 = JSON.stringify(viaPoints);

    frmSave.longText1.value = json;
    frmSave.longText2.value = json2;
}





// function loadMapCallback(data) {
//     let locations= frmSave.longText1.value;
//     if (!locations?.trim()) return;
//
//     locations = JSON.parse(locations);
//     const start = locations.pop();
//     data.arrival = data.departure = new Tmapv2.LatLng(start.lat, start.lng);
//     data.via = locations.map(({lat, lng}) => new Tmapv2.LatLng(lat, lng));
//
//     data.route();
// }

/*

// 제출 버튼 ajaxLoad 처리
const submitEl = document.getElementById("submitBtn");
submitEl.addEventListener("click", () => {
      // DB 저장 경로 생성

        via.forEach(point => this.push({lng: point.lng(), lat: point.lat()}), locations);

        locations.push({lat: arrival.lat(), lng: arrival.lng()});

        const {ajaxLoad} = commonLib;

       // ajaxLoad("board/save", "POST", {locations}, {contentType: "application/json"})
    });
*/