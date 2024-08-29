window.addEventListener("DOMContentLoaded", function() {
    /* 인증 코드 전송 S */
    const emailVerifyEl = document.getElementById("email_verify"); // 인증코드 전송
    const emailConfirmEl = document.getElementById("email_confirm"); // 확인 버튼
    const emailReVerifyEl = document.getElementById("email_re_verify"); // 재전송 버튼
    const authNumEl = document.getElementById("auth_num"); // 인증코드
    if (emailVerifyEl) {
        emailVerifyEl.addEventListener("click", function() {
            const { ajaxLoad, sendEmailVerify } = commonLib;
            const email = frmRegist.email.value.trim();
            if (!email) {
                alert('이메일을 입력하세요.');
                frmRegist.email.focus();
                return;
            }

            sendEmailVerify(email); // 이메일 인증 코드 전송
            this.disabled = true;
            frmRegist.email.setAttribute("readonly", true);

            /* 인증코드 재전송 처리 S */
            if (emailReVerifyEl) {
                emailReVerifyEl.addEventListener("click", function() {
                    sendEmailVerify(email);
                });
            }

            /* 인증코드 재전송 처리 E */

            /* 인증번호 확인 처리 S */
            if (emailConfirmEl && authNumEl) {
                emailConfirmEl.addEventListener("click", function() {
                    const authNum = authNumEl.value.trim();
                    if (!authNum) {
                        alert("인증코드를 입력하세요.");
                        authNumEl.focus();
                        return;
                    }

                    // 인증코드 확인 요청
                    const { sendEmailVerifyCheck } = commonLib;
                    sendEmailVerifyCheck(authNum);
                });
            }
            /* 인증번호 확인 처리 E */

        });
    }
    /* 인증 코드 전송 E */
});

/**
 * 이메일 인증 메일 전송 후 콜백 처리
 *
 * @param data : 전송 상태 값
 */
function callbackEmailVerify(data) {
    if (data && data.success) { // 전송 성공
        alert("인증코드가 이메일로 전송되었습니다. 확인후 인증코드를 입력하세요.");

        /** 3분 유효시간 카운트 */
        authCount.start();

    } else { // 전송 실패
        alert("인증코드 전송에 실패하였습니다.");
    }
}

/**
 * 인증메일 코드 검증 요청 후 콜백 처리
 *
 * @param data : 인증 상태 값
 */
function callbackEmailVerifyCheck(data) {
    if (data && data.success) { // 인증 성공
        /**
         * 인증 성공시
         * 1. 인증 카운트 멈추기
         * 2. 인증코드 전송 버튼 제거
         * 3. 이메일 입력 항목 readonly 속성으로 변경
         * 4. 인증 성공시 인증코드 입력 영역 제거
         * 5. 인증 코드 입력 영역에 "확인된 이메일 입니다."라고 출력 처리
         */

        // 1. 인증 카운트 멈추기
        if (authCount.intervalId) clearInterval(authCount.intervalId);

        // 2. 인증코드 전송 버튼 제거
        const emailVerifyEl = document.getElementById("email_verify");
        emailVerifyEl.parentElement.removeChild(emailVerifyEl);

        // 3. 이메일 입력 항목 readonly 속성으로 변경
        frmRegist.email.setAttribute("readonly", true);

        // 4. 인증 성공시 인증코드 입력 영역 제거, 5. 인증 코드 입력 영역에 "확인된 이메일 입니다."라고 출력 처리
        const authBoxEl = document.querySelector(".auth_box");
        authBoxEl.innerHTML = "<span class='confirmed'>확인된 이메일 입니다.</span>";

    } else { // 인증 실패
        alert("이메일 인증에 실패하였습니다.");
    }
}

/**
 * 유효시간 카운트
 *
 */
const authCount = {
    intervalId : null,
    count : 60 * 3, // 유효시간 3분
    /**
     * 인증 코드 유효시간 시작
     *
     */
    start() {
        const countEl = document.getElementById("auth_count");
        if (!countEl) return;

        this.initialize(); // 초기화 후 시작

        this.intervalId = setInterval(function() {

            authCount.count--;
            if (authCount.count < 0) {
                authCount.count = 0;
                clearInterval(authCount.intervalId);

                const emailConfirmEl = document.getElementById("email_confirm"); // 확인 버튼
                const emailReVerifyEl = document.getElementById("email_re_verify"); // 재전송 버튼
                const emailVerifyEl = document.getElementById("email_verify"); // 인증코드 전송
                emailConfirmEl.disabled = emailReVerifyEl.disabled = true;
                emailVerifyEl.disabled = false;
                frmRegist.email.removeAttribute("readonly");
                return;
            }

            const min = Math.floor(authCount.count / 60);
            const sec = authCount.count - min * 60;

            countEl.innerHTML=`${("" + min).padStart(2, '0')}:${("" + sec).padStart(2, '0')}`;
        }, 1000);
    },

    /**
     * 인증 코드 유효시간 초기화
     *
     */
    initialize() {
        const countEl = document.getElementById("auth_count");
        const emailVerifyEl = document.getElementById("email_verify"); // 인증코드 전송
        const emailConfirmEl = document.getElementById("email_confirm"); // 확인 버튼
        const emailReVerifyEl = document.getElementById("email_re_verify"); // 재전송 버튼
        emailConfirmEl.disabled = emailReVerifyEl.disabled = false;
        emailVerifyEl.disabled = true;
        frmRegist.email.setAttribute("readonly", true);

        this.count = 60 * 3;
        if (this.intervalId) clearInterval(this.intervalId);
        countEl.innerHTML = "03:00";
    }
};

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


/**
 * 파일 삭제 후 후속 처리
 *
 */
function fileDeleteCallback(file) {
    const targetEl = document.querySelector(".profile-image");
    if (targetEl) targetEl.innerHTML = "";
}