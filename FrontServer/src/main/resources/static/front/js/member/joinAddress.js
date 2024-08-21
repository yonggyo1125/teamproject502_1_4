function user_execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수

                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }

                    if (data.userSelectedType === 'R') {

                        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                            extraAddr += data.bname;
                        }

                        if (data.buildingName !== '' && data.apartment === 'Y') {
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }

                        if (extraAddr !== '') {
                            extraAddr = ' (' + extraAddr + ')';
                        }

                        document.getElementById("user_extraAddress").value = extraAddr;

                    } else {
                        document.getElementById("user_extraAddress").value = '';
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('user_postcode').value = data.zonecode;
                    document.getElementById("user_address").value = addr;
                    // 커서를 상세주소 필드로 이동한다.
                    document.getElementById("user_detailAddress").focus();
                }
            }).open();
        }