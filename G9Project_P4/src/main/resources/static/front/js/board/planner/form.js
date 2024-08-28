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
});

function getEditor(editorName) {
    return editors[editorName];
}


/** 이미지 업로드 후속 처리 */
function fileUploadCallback(files) {
    if (!files || files.length === 0) return;

    const imageUrls = {};
    for (const file of files) {
        const { location, fileUrl } = file;
        imageUrls[location] = imageUrls[location] ?? [];
        imageUrls[location].push(fileUrl);
    }

    insertImages(imageUrls);
}

function insertImages(imageUrls) {
    for (const [editorName, urls] of Object.entries(imageUrls)) {
        const editor = getEditor(editorName);
        editor.execute('insertImage', { source: urls });
    }
}