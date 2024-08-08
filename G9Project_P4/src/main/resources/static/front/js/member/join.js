/**
* 프로필 이미지 업로드 후속 처리
*
*/
function fileUploadCallback(files) {
    if (files.length === 0) {
        return;
    }

    const file = files[0];
    let html = document.getElementById("image-file-tpl").innerHTML;
    html = html.replace(/\[seq\]/g, file.seq)
                .replace(/\[fileUrl\]/g, file.fileUrl);

    const domParser = new DOMParser();
    const dom = domParser.parseFromString(html, 'text/html');
    const box = dom.querySelector(".image-file-box");

    const targetEl = document.querySelector(".profile-image");
    targetEl.innerHTML = "";
    targetEl.append(box);

    const removeEl = box.querySelector(".remove");
    removeEl.addEventListener("click", function() {
        if (!confirm('정말 삭제 하겠습니까?')) {
            return;
        }

        const seq = this.dataset.seq;
        fileManager.delete(seq);
    });


}