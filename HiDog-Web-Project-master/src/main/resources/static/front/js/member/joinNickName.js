document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("checkUserName").addEventListener("click", function() {
        var userName = document.getElementById("userName").value;
        var resultSpan = document.getElementById("userNameError");

        if (!userName.trim()) {
            resultSpan.textContent = "닉네임을 입력해주세요.";
            resultSpan.style.color = "red";
            return;
        }

        fetch(`/app/member/join/check-username?userName=${encodeURIComponent(userName)}`)
            .then(response => response.json()) // JSON 응답 처리
            .then(isTaken => {
                if (isTaken) {
                    resultSpan.textContent = "이미 사용 중인 닉네임입니다.";
                    resultSpan.style.color = "red";
                } else {
                    resultSpan.textContent = "사용 가능한 닉네임입니다.";
                    resultSpan.style.color = "green";
                }
            })
            .catch(error => {
                console.error("Error:", error);
                resultSpan.textContent = "검사 중 오류";
                resultSpan.style.color = "red";
            });
    });
});
