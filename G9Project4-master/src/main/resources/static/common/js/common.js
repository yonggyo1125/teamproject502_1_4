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

        if (!/^http[s]?/i.test(url)) {
            let rootUrl = document.querySelector("meta[name='rootUrl']")?.content?.trim() ?? '';
            rootUrl = rootUrl === '/' ? '' : rootUrl;

            url = rootUrl.includes("http") ? rootUrl + url.replace("/", "") : location.protocol + "//" + location.host + url;
        }
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
    },
    /**
     * 에디터 로드
     *
     */
    editorLoad(id) {
        if(!ClassicEditor || !id?.trim()) return;

        return ClassicEditor.create(document.getElementById(id.trim()), {});
    },
    popup : {
        /**
         * 레이어 팝업 열기
         *
         * @param url : 팝업으로 열 주소
         * @param width : 팝업 너비, 기본값 350
         * @param height : 팝업 높이, 기본값 350
         */
        open(url, width, height) {
            if (!url) return;

            let rootUrl = document.querySelector("meta[name='rootUrl']")?.content?.trim() ?? '';
            rootUrl = rootUrl === '/' ? '' : rootUrl;

            url = location.protocol + "//" + location.host + rootUrl + url;

            width = width || 350;
            height = height || 350;

            /* 이미 열려 있는 레이어팝업이 있다면 제거 */
            this.close();

            /* 레이어 팝업 요소 생성 S */
            const popupEl = document.createElement("div"); // 팝업
            popupEl.id = "layer_popup";
            popupEl.style.width = width + "px";
            popupEl.style.height = height + "px";

            const iframeEl = document.createElement("iframe");
            iframeEl.width = width;
            iframeEl.height = height;
            iframeEl.src = url;
            popupEl.appendChild(iframeEl);

            /* 레이어 팝업 가운데 배치 좌표 구하기 S */
            const centerX = Math.round((window.innerWidth - width) / 2);
            const centerY = Math.round((window.innerHeight - height) / 2);
            popupEl.style.top = centerY + "px";
            popupEl.style.left = centerX + "px";

            /* 레이어 팝업 가운데 배치 좌표 구하기 E */

            const layerDimEl = document.createElement("div"); // 레어어 배경
            layerDimEl.id = "layer_dim";

            /* 레이어 팝업 요소 생성 E */

            /* 레이어 팝업 노출 S */
            document.body.appendChild(layerDimEl);
            document.body.appendChild(popupEl);
            /* 레이어 팝업 노출 E */

            /* 레이어 배경 클릭시 close 처리 */
            layerDimEl.addEventListener("click", this.close);
        },
        /**
         * 레이어 팝업 닫기
         *
         */
        close() {
            const popupEl = document.getElementById("layer_popup");
            if (popupEl) popupEl.parentElement.removeChild(popupEl);

            const layerDimEl = document.getElementById("layer_dim");
            if (layerDimEl) layerDimEl.parentElement.removeChild(layerDimEl);
        }
    }
};

/**
 * 이메일 인증 메일 보내기
 *
 * @param email : 인증할 이메일
 */
commonLib.sendEmailVerify = function(email) {
    const { ajaxLoad } = commonLib;

    const url = `/api/email/verify?email=${email}`;

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
    const url = `/api/email/auth_check?authNum=${authNum}`;

    ajaxLoad(url, "GET", null, null, "json")
        .then(data => {
            if (typeof callbackEmailVerifyCheck == 'function') { // 인증 메일 코드 검증 요청 완료 후 처리 콜백
                callbackEmailVerifyCheck(data);
            }
        })
        .catch(err => console.error(err));
};