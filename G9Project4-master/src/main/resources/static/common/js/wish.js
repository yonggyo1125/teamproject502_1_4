const wishList = {
    /**
     * 추가
     *  type - BOARD : 게시판
     *      - TOUR : 여행지
     *  callback : 추가 이후 후속 처리
     */
    add(seq, type, callback) {
        this.process(seq, type, 'GET', callback)
    },
    remove(seq, type, callback) {
        this.process(seq, type, 'DELETE', callback)
    },
    process(seq, type, method, callback) {
        const { ajaxLoad } = commonLib;
        (async() => {
            try {
                await ajaxLoad(`/wish/${type}/${seq}`, method, null, null, 'text');
                // 후속 처리
                if (typeof callback === 'function') {
                    callback(seq, type);
                }
            } catch (err) {
                console.err(err);
            }
        })();
    }
};

window.addEventListener("DOMContentLoaded", function () {
   // wishlist-toggle
    // on -> 추가 상태, on 없는 상태 제거 상태
   const els = document.getElementsByClassName("wishlist-toggle");
   for (const el of els) {
       el.addEventListener("click", function () {
          const classList = this.classList;
            const { seq, type } = this.dataset;
            if (!seq || !type) {
                return;
            }

           if (classList.contains("on")) { // 이미 추가 상태이므로 제거 처리
               wishList.remove(seq, type, () => classList.remove("on"));
           } else { // 추가 처리
               wishList.add(seq, type, () => classList.add("on"));
           }
       });
   }
});