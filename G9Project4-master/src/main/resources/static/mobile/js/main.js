/*메인페이지 상단 바탕이미지 시작 */
document.addEventListener("DOMContentLoaded", function() {
    const images = document.querySelectorAll(".banner .banner-image");
    let currentIndex = 0;
    const totalImages = images.length;

    function changeImage() {
        // 현재 이미지 비활성화
        images[currentIndex].classList.remove("active");

        // 다음 이미지 인덱스 계산
        currentIndex = (currentIndex + 1) % totalImages;

        // 다음 이미지 활성화
        images[currentIndex].classList.add("active");
    }

    // 이미지 전환 주기 설정 (10초)
    setInterval(changeImage, 5000); // 초마다 이미지 전환
});
/*메인페이지 상단 바탕이미지 끝 */