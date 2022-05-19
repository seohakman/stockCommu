'use strict';

/* Navbar  */
// 토글버튼 사용
const navbarToggleBtn = document.querySelector('.navbar__toggle-btn');
navbarToggleBtn.addEventListener('click', () => {
	document.getElementById("myDropdown").classList.toggle('show');
});

/* Join */
//비밀번호 확인
/*const PWDCheck = document.querySelector("#join-btn");
PWDCheck.addEventListener("click", () => {
	if(document.fm.ID.value == ""){
		alert("아이디를 입력하세요.");
		document.fm.ID.focus();
		return;
	}else if(document.fm.name.value == ""){
		alert("이름을 입력하세요.");
		document.fm.name.focus();
		return;
	}else if(document.fm.PWD.value == ""){
		alert("비밀번호를 입력하세요.");
		document.fm.PWD.focus();
		return;
	}else if(document.fm.PWDCheck.value == ""){
		alert("비밀번호 확인 입력하세요.");
		document.fm.PWDCheck.focus();
		return;
	}else if(document.fm.PWD.value != document.fm.PWDCheck.value){
		alert("비밀번호가 일치하지 않습니다.");
		document.fm.PWDCheck.focus();
		return;
	}else if(document.fm.email.value == ""){
		alert("이메일을 입력하세요.");
		document.fm.email.focus();
		return;
	}
		//가상경로를 사용해서 페이지를 이동시킨다.
	document.fm.action = "<%=request.getContextPath()%>/member/memberJoinAction.do";
	document.fm.method = "post";
	document.fm.submit();
});
*/
/* login */
