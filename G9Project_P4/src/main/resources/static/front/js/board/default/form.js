window.addEventListener("DOMContentLoaded", function() {
   (async() => {
        try {
            const { editorLoad } = commonLib;
            const editor = await editorLoad("content");
            if (editor) window.editor = editor;
        } catch (err) {}
   })();
});