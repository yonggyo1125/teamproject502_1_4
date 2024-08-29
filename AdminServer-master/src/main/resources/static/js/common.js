const commonLib = {
    /**
    * ajax 요청 공통 기능
    *
    * @param responseType : 응답 데이터 타입(text - text로, 그외는 json)
    */
    ajaxLoad(url, method = "GET", data, headers, responseType) {
        if (!url) {
            return;
        }

        const csrfToken = document.querySelector("meta[name='csrf_token']")?.content?.trim();
        const csrfHeader = document.querySelector("meta[name='csrf_header']")?.content?.trim();
        let rootUrl = document.querySelector("meta[name='rootUrl']")?.content?.trim() ?? '';
        rootUrl = rootUrl === '/' ? '' : rootUrl;

        url = location.protocol + "//" + location.host + rootUrl + url;

        method = method.toUpperCase();
        if (method === 'GET') {
            data = null;
        }

        if (data && !(data instanceof FormData) && typeof data !== 'string' && data instanceof Object) {
            data = JSON.stringify(data);
        }

        if (csrfHeader && csrfToken) {
            headers = headers ?? {};
            headers[csrfHeader] = csrfToken;
        }

        const options = {
            method
        };

        if (data) options.body = data;
        if (headers) options.headers = headers;

        return new Promise((resolve, reject) => {
            fetch(url, options)
                .then(res => responseType === 'text' ? res.text() : res.json()) // res.json() - JSON / res.text() - 텍스트
                .then(data => resolve(data))
                .catch(err => reject(err));
        });
    }
};

window.addEventListener("DOMContentLoaded", function() {
    /* 양식 공통 처리 S */
    const formActions = document.getElementsByClassName("form_action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const dataset = this.dataset;
            const mode = dataset.mode || "edit";
            const formName = dataset.formName;
            if (!formName || !document[formName]) {
                return;
            }

            const formEl = document[formName];
           formEl._method.value = mode == 'delete' ? 'DELETE' : 'PATCH';

           const modeTitle = mode == 'delete' ? '삭제' : '수정';

           const chks = formEl.querySelectorAll("input[name='chk']:checked");

           if (chks.length == 0) { // 체크가 안된 경우
                alert(`${modeTitle}할 항목을 선택하세요.`);
                return;
           }

            if (confirm(`정말 ${modeTitle} 하겠습니까?`)) {
                formEl.submit();
            }

        });
    }
    /* 양식 공통 처리 E */

    /* 전체 선택 토글 기능 S */
    const checkAlls = document.getElementsByClassName("checkall");
    for (const el of checkAlls) {
        el.addEventListener("click", function() {
            const targetName = this.dataset.targetName;
            const chks = document.getElementsByName(targetName);
            for (const el of chks) {
                el.checked = this.checked;
            }
        });
    }
    /* 전체 선택 토글 기능 E */
});