# 회원가입 이메일 인증

## Email 서버

### 이메일 서버 소스를 이전 먼저 할 것
- 패키지명은 조별로 적절하게 변경
- 서버 실행 시 환경 변수 설정

```
configServerUrl=http://localhost:3100;db.host=localhost;db.password=db 비밀번호;db.port=1521;db.username=db 계정;ddl.auto=update;eurekaHost=http://localhost:3101;hostname=localhost;mail.password=이메일 app 비밀번호;mail.username=이메일 계정;redis.host=localhost;redis.port=6379
```

## Front 서버

### member/controllers/MemberController.java

```java

...

@SessionAttributes({"requestLogin", "EmailAuthVerified"})
public class MemberController implements ExceptionProcessor {

    ...

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        // 이메일 인증 여부 false로 초기화
        model.addAttribute("EmailAuthVerified", false);

        return utils.tpl("member/join");
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model, SessionStatus status, HttpSession session) {
    
        ...

        status.setComplete();
        session.removeAttribute("EmailAuthVerified");

        return "redirect:" + utils.redirectUrl("/member/login");
    }

    ...
   
}

```

### member/validators/JoinValidator.java


```java

...

public class JoinValidator implements Validator, PasswordValidator, MobileValidator {
    private final MemberRepository memberRepository;
    private final HttpSession session;

   

    @Override
    public void validate(Object target, Errors errors) {
        ...
        
    // 1. 이미 가입된 회원인지 체크
        if(memberRepository.exists(email)){
            errors.rejectValue("email","Duplicated");
        }

        // 이메일 인증 여부 체크
        Boolean emailVerified = (Boolean)session.getAttribute("EmailAuthVerified");
        if (emailVerified == null || !emailVerified) {
            errors.rejectValue("email","NotVerified");
        }

        ....
        
    }
}
```

### static/front/js/member/join.js

```javascript
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
```


### static/js/common.js

```javascript
...

/**
* 이메일 인증 메일 보내기
*
* @param email : 인증할 이메일
*/
commonLib.sendEmailVerify = function(email) {
    const { ajaxLoad } = commonLib;

    const url = `/email/verify?email=${email}`;

    ajaxLoad(url, "GET", null, null, "json")
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
commonLib.sendEmailVerifyCheck = function(authNum) {
    const { ajaxLoad } = commonLib;
    const url = `/email/auth_check?authNum=${authNum}`;

    ajaxLoad(url, "GET", null, null, "json")
        .then(data => {
            if (typeof callbackEmailVerifyCheck == 'function') { // 인증 메일 코드 검증 요청 완료 후 처리 콜백
                callbackEmailVerifyCheck(data);
            }
        })
        .catch(err => console.error(err));
};
```

### templates/front/member/join.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{front/layouts/main}">
<main layout:fragment="content">
    <h1 th:text="#{회원가입}"></h1>
    <form name="frmRegist" method="post" th:action="${@utils.redirectUrl('/member/join')}" autocomplete="off" th:object="${requestJoin}">
        <div class="error global" th:each="err : ${#fields.globalErrors()}" th:text="${err}"></div>
        <input type="hidden" name="gid" th:field="*{gid}">
        <dl>
            <dt th:text="#{이메일}"></dt>
            <dd>
                <div>
                    <input type="text" name="email" th:field="*{email}" th:readonly="${session.EmailAuthVerified != null && session.EmailAuthVerified}">
                    <button th:if="${session.EmailAuthVerified == null || !session.EmailAuthVerified}" type="button" id="email_verify" th:text="#{인증코드전송}"></button>
                </div>
                <div class="auth_box">
                    <th:block th:if="${session.EmailAuthVerified == null || !session.EmailAuthVerified}">
                        <input type="text" id="auth_num" th:placeholder="#{인증코드_입력}">
                        <span id="auth_count">03:00</span>
                        <button type="button" id="email_confirm" th:text="#{확인}" disabled></button>
                        <button type="button" id="email_re_verify" th:text="#{재전송}" disabled></button>
                    </th:block>
                    <th:block th:unless="${session.EmailAuthVerified == null || !session.EmailAuthVerified}">
                        <span class='confirmed' th:text="#{확인된_이메일_입니다.}"></span>
                    </th:block>
                </div>
                <div class="error" th:each="err : ${#fields.errors('email')}" th:text="${err}"></div>
            </dd>
        </dl>
        ....
        
    </form>
    <script th:replace="~{common/_file_tpl::image}"></script>
</main>
</html>
```