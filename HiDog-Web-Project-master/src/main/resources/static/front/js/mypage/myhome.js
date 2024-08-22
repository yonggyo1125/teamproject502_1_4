function openProfilePopup() { // 팝업 열기
    document.getElementById('profilePopup').style.display = 'block';
}

function closeProfilePopup() { // 팝업 닫기
    document.getElementById('profilePopup').style.display = 'none';
}

document.getElementById('uploadForm').addEventListener('submit', function(event) { // 폼 제출 처리
    event.preventDefault(); // 기본 폼 제출 방지

    var formData = new FormData(this);

    fetch('/mypage/myhome', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                console.error('Error:', response);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});