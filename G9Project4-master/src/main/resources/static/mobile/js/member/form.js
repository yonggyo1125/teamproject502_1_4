window.addEventListener("DOMContentLoaded", function() {
    const removeEl = document.querySelector(".profile-image .remove");
    if (removeEl) {
        removeEl.addEventListener("click", function() {
            deleteProfileImage(this);
        });
    }
});

/**
 * 프로필 이미지 후속 처리
 * 
 * @param files
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
    targetEl.innerHTML="";
    targetEl.append(box);

    const removeEl = box.querySelector(".remove");
    removeEl.addEventListener("click", function () {
        deleteProfileImage(this);
    });
}




/**
 * 파일 삭제 후속 처리
 *
 * @param file
 */
function fileDeleteCallback(file) {
    const targetEl = document.querySelector(".profile-image");
    if (targetEl) targetEl.innerHTML = "";
}

function deleteProfileImage(el) {
    if (!confirm('정말 삭제하겠습니까?')) {
        return;
    }

    const seq = el.dataset.seq;
    fileManager.delete(seq);
}