window.addEventListener("DOMContentLoaded", function() {
    if (ClassicEditor) { // 에디터 사용의 경우
        ClassicEditor.create(document.getElementById("content"), {
            height: 450,
        })
        .then((editor) => console.log(editor));
    }
});