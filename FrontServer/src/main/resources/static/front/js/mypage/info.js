function fileUploadCallback(files) {
    if (files.length === 0) return;
    const el = document.querySelector(".profile-image");

    el.style.backgroundImage = "url('" + files[0].fileUrl + "');";
    console.log(el);
    //el.style.backgroundSize = "cover";

}