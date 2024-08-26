window.addEventListener("DOMContentLoaded", function() {
    const remove = document.querySelector(".profile-image .remove");
    if (remove) {
        remove.addEventListener("click", (e) => {
            if (confirm('정말 삭제하겠습니까?')) {
                fileManager.delete(e.currentTarget.dataset.seq);
            }
        });
    }
});

function fileUploadCallback(files) {
   if (files.length === 0) return;

   const file = files[0];
   const el = document.querySelector(".profile-image > .inner");
   const html = `<img src='${file.fileUrl}' alt="${file.fileName}">`;
   el.innerHTML = html;

   const wrap = document.querySelector(".profile-image");
   let remove = wrap.querySelector(".remove");
   if (remove) {
        remove.parentElement.removeChild(remove);
   }
   remove = document.createElement("i");
   remove.className="remove xi-close";
   wrap.prepend(remove);

   remove.addEventListener("click", () => {
        if (confirm('정말 삭제하겠습니까?')) {
            fileManager.delete(file.seq);
        }
   });
}


function fileDeleteCallback(seq) {
    const el = document.querySelector(".profile-image > .inner");
    el.innerHTML = "";

    const remove = document.querySelector(".profile-image .remove");
    if (remove) {
        remove.parentElement.removeChild(remove);
    }
}