'use strict';

/* Navbar  */
// 토글버튼 사용
const navbarToggleBtn = document.querySelector('.navbar__toggle-btn');
navbarToggleBtn.addEventListener('click', () => {
	document.getElementById("myDropdown").classList.toggle('show');
});

/* Join */
//비밀번호 확인
const PWDCheck = document.querySelector("#join-btn");
PWDCheck.addEventListener("click", () => {
	if(document.fm.PWD.value != document.fm.PWDCheck.value){
		alert("비밀번호가 일치하지 않습니다.");
		document.fm.PWDCheck.focus();
		return;
	}else{
		//가상경로를 사용해서 페이지를 이동시킨다.
		document.fm.action = "<%= request.getContextPath() %>/member/memberJoinAction.do";
		document.fm.method = "post";
		document.fm.submit();
	}
});

/* login */
