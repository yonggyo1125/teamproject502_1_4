window.addEventListener("DOMContentLoaded", function() {
    (async() => {
        try {
            const { editorLoad } = commonLib;
            const editor = await editorLoad("content");
            if (editor) window.editor = editor;
        } catch (err) {}
    })();

    /* 이미지 본문 추가 이벤트 처리 S */
    const insertEditors = document.getElementsByClassName("insert-editor");
    for (const el of insertEditors) {
        el.addEventListener("click", (e) => insertEditor(e.currentTarget.dataset.url));
    }

    const removeEls = document.querySelectorAll(".file-item .remove");
    for (const el of removeEls) {
        el.addEventListener("click", function () {
            if (confirm('정말 삭제하겠습니까?')) {
                const seq = this.dataset.seq;
                fileManager.delete(seq);
            }
        });
    }
    /* 이미지 본문 추가 이벤트 처리 E */
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


    //에디터에 첨부할 이미지 URL
    const imageUrls = [];

    //파일 업로드 location 별 파일 목록 템플릿
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

        if (location === 'editor') { //에디터 첨부
            imageUrls.push(fileUrl);

            const insertEditorEl = el.querySelector(".insert-editor");
            if (insertEditorEl) {
                insertEditorEl.addEventListener("click", (e) => insertEditor(e.currentTarget.dataset.url));
            }
        }

        //파일 삭제 이벤트 처리
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