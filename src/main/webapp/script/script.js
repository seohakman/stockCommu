'use strict';

/* Navbar  */
// 토글버튼 사용
const navbarToggleBtn = document.querySelector('.navbar__toggle-btn');
navbarToggleBtn.addEventListener('click', () => {
	document.getElementById("myDropdown").classList.toggle('show');
});

/* chatToggle */


$(document).ready(function(){
	$('.chat_toggle_btn').click(function(){
		if($('#chatDiv').css('display')=='none'){
                                
			$('#chatDiv').css('display','block');
		  }else{
	      
			$('#chatDiv').css('display','none');
		  }
	
		});
});