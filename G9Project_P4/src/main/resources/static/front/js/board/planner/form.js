window.addEventListener("DOMContentLoaded", function() {
    const { editorLoadByDOM } = commonLib;
    const contentEls = document.getElementsByClassName("content");
    for (const el of contentEls) {
        const editorName = `editor_${el.dataset.seq}`;
        (async() => {
            try {
                const editor = await editorLoadByDOM(el);
                window.editors = window.editors ?? {}
                window.editors[editorName] = editor;
            } catch (err) {
                console.error(err);
            }
        })();
    }


    frmSave.addEventListener("submit", function(e) {
        e.preventDefault();

        const items = document.getElementsByClassName("content");
        const contentData = {};
        for (const el of items) {
            const seq = el.dataset.seq;
            contentData[`content_${seq}`] = el.value;
        }

        frmSave.content.value = JSON.stringify(contentData);
    });

});

function getEditor(editorName) {
    return editors[editorName];
}


/** 이미지 업로드 후속 처리 */
function fileUploadCallback(files) {
    if (!files || files.length === 0) return;

    const editorTpl = document.getElementById("editor-file-tpl").innerHTML;
    const domParser = new DOMParser();

    const imageUrls = {};
    for (const file of files) {
        const { seq, location, fileName, fileUrl } = file;
        imageUrls[location] = imageUrls[location] ?? [];
        imageUrls[location].push(fileUrl);

        const target = document.getElementById(`uploaded-files-${location}`);
        let html = editorTpl;

        html = html.replace(/\[seq\]/g, seq)
                    .replace(/\[fileName\]/g, fileName)
                    .replace(/\[fileUrl\]/g, fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(".file-item");

        target.append(el);
        const insertEditorEl = el.querySelector(".insert-editor");
        if (insertEditorEl) {
            insertEditorEl.addEventListener("click", (e) => insertImages({[location]: [e.currentTarget.dataset.url]}));
        }

        // 파일 삭제 이벤트 처리
        const removeEl = el.querySelector(".remove");
        removeEl.addEventListener("click", () => {
            if (confirm('정말 삭제하겠습니까?')) {
                fileManager.delete(seq);
            }
        });
    }

    insertImages(imageUrls);
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

function insertImages(imageUrls) {
    for (const [editorName, urls] of Object.entries(imageUrls)) {
        const editor = getEditor(editorName);
        editor.execute('insertImage', { source: urls });
    }
}