var joinLib = joinLib || {};

/**
* ajax 처리
*
* @param method : 요청 메서드 - GET, POST, PUT ...
* @param url : 요청 URL
* @param responseType : json - 응답 결과를 json 변환, 아닌 경우는 문자열로 반환
*/
joinLib.ajaxLoad = function(method, url, params, responseType) {
    method = !method || !method.trim()? "GET" : method.toUpperCase();
    const token = document.querySelector("meta[name='csrf_token']").content;
    const header = document.querySelector("meta[name='csrf_header']").content;
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.setRequestHeader(header, token);

        xhr.send(params);
        responseType = responseType?responseType.toLowerCase():undefined;
        if (responseType == 'json') {
            xhr.responseType=responseType;
        }

        xhr.onreadystatechange = function() {
            if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
                const resultData = responseType == 'json' ? xhr.response : xhr.responseText;

                resolve(resultData);
            }
        };

        xhr.onabort = function(err) {
            reject(err);
        };

        xhr.onerror = function(err) {
            reject(err);
        };

        xhr.ontimeout = function(err) {
            reject(err);
        };
    });
};

/**
* 이메일 인증 메일 보내기
*
* @param email : 인증할 이메일
*/
joinLib.sendEmailVerify = function(email) {
    const { ajaxLoad } = joinLib;

    const url = `/app/email/verify?email=${email}`;

    ajaxLoad("GET", url, null, "json")
        .then(data => {
            if (typeof callbackEmailVerify == 'function') { // 이메일 승인 코드 메일 전송 완료 후 처리 콜백
                callbackEmailVerify(data);
            }
        })
        .catch(err => console.error(err));
};

/**
* 인증 메일 코드 검증 처리
*
*/
joinLib.sendEmailVerifyCheck = function(authNum) {
    const { ajaxLoad } = joinLib;
    const url = `/app/email/auth_check?authNum=${authNum}`;

    ajaxLoad("GET", url, null, "json")
        .then(data => {
            if (typeof callbackEmailVerifyCheck == 'function') { // 인증 메일 코드 검증 요청 완료 후 처리 콜백
                callbackEmailVerifyCheck(data);
            }
        })
        .catch(err => console.error(err));
};

window.addEventListener("DOMContentLoaded", function() {
    /* 인증 코드 전송 S */
    const emailVerifyEl = document.getElementById("email_verify"); // 인증코드 전송
    const emailConfirmEl = document.getElementById("email_confirm"); // 확인 버튼
    const emailReVerifyEl = document.getElementById("email_re_verify"); // 재전송 버튼
    const authNumEl = document.getElementById("auth_num"); // 인증코드
    if (emailVerifyEl) {
        emailVerifyEl.addEventListener("click", function() {
            const { ajaxLoad, sendEmailVerify } = joinLib;
            const email = frmRegist.email.value.trim();
            if (!email) {
                alert('이메일을 입력하세요.');
                frmRegist.email.focus();
                return;
            }

            /* 이메일 확인 전 이미 가입된 이메일인지 여부 체크 S */
            ajaxLoad("GET", `/app/email/join/email_dup_check?email=${email}`, null, "json")
                .then(data => {
                    if (data.success) { // 중복이메일인 경우
                        alert("이미 가입된 이메일입니다.");
                        frmRegist.email.focus();
                    } else { // 중복이메일이 아닌 경우
                        sendEmailVerify(email); // 이메일 인증 코드 전송
                        this.disabled = frmRegist.email.readonly = true;

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
                                const { sendEmailVerifyCheck } = joinLib;
                                sendEmailVerifyCheck(authNum);
                            });
                          }
                          /* 인증번호 확인 처리 E */
                    }
                });

            /* 이메일 확인 전 이미 가입된 이메일인지 여부 체크 E */
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
        alert("인증코드가 이메일로 전송되었습니다. 확인 후 인증코드를 입력하세요.");

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
        frmRegist.email.readonly = true;

        // 4. 인증 성공시 인증코드 입력 영역 제거, 5. 인증 코드 입력 영역에 "확인된 이메일 입니다."라고 출력 처리
        const authBoxEl = document.querySelector(".auth_box");
        authBoxEl.innerHTML = "<span class='confirmed'>확인된 이메일입니다.</span>";

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
                emailVerifyEl.disabled = frmRegist.email.readonly = false;
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
        emailVerifyEl.disabled = frmRegist.email.readonly = true;

        this.count = 60 * 3;
        if (this.intervalId) clearInterval(this.intervalId);
        countEl.innerHTML = "03:00";
    }
};