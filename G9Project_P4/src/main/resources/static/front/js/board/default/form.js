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
    const attachTpl = document.getElementById("attach-file-tpl");
    const editorTpl = document.getElementById("editor-file-tpl");

    const attachTarget = document.getElementById("uploaded-files-attach");
    const editorTarget = document.getElementById("uploaded-files-editor");

    const domParser = new DOMParser();

    for (const file of files) {
        const { seq, location, fileUrl, fileName } = file;

        const target = location === 'editor' ? editorTarget : attachTarget;
        let html = location === 'editor' ? editorTpl : attachTpl;

        if (location === 'editor') { // 에디터 첨부
            imageUrls.push(fileUrl);
        }

        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(".file-item");
    }

    // 에디터 본문에 이미지 추가
    if (imageUrls.length > 0) {
        editor.execute("insertImage", { source: imageUrls });
    }
}