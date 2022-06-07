'use strict';

/* Navbar  */
// 토글버튼 사용
const navbarToggleBtn = document.querySelector('.navbar__toggle-btn');
navbarToggleBtn.addEventListener('click', () => {
	document.getElementById("myDropdown").classList.toggle('show');
});


