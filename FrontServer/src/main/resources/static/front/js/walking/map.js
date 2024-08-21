window.addEventListener("DOMContentLoaded", function() {
   tmapLib.load("mapId");

   // 버튼 이벤트 리스너
   const startEl = document.getElementById("start");
   startEl.addEventListener("click", () => tmapLib.currentAction = 'start');

   const viasEl = document.getElementById("vias");
   viasEl.addEventListener("click", () => tmapLib.currentAction = 'via');

   const completeEl = document.getElementById("complete");
   completeEl.addEventListener("click", () => {
      if (tmapLib.resultDrawArr.length == 0) {
         tmapLib.route(tmapLib.mapId)

      } else {
         console.log(tmapLib.resultDrawArr)
         return;
      }
      viasEl.style.display = 'none';
      startEl.style.display = 'none';
   });


   const resetEl = document.getElementById("reset");
   resetEl.addEventListener("click", function() {
      if (confirm("정말 다시 선택 하시겠습니까??")) {
         viasEl.style.display = '';
         startEl.style.display = '';
         tmapLib.reset();
      }
   });

});