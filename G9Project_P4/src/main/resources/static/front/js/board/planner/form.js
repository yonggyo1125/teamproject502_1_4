window.addEventListener("DOMContentLoaded", function() {
    const { editorLoadByDOM } = commonLib;
    const contentEls = document.getElementsByClassName("content");
    for (const el of contentEls) {
        const editorName = `editor_${el.dataset.seq}`;
        (async() {
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