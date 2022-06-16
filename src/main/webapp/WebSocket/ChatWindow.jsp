<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	var webSocket = new WebSocket("<%=application.getInitParameter("CHAT_ADDR")%>/ChatingServer");
	var chatWindow, chatMessage, chatId;
	
	//채팅창이 열리면 대화창, 메시지 입력창, 대화명 표시란으로 사용할 dom 객체 저장
	window.onload = function(){
		chatWindow = document.getElementById("chatWindow");
		chatMessage = document.getElementById("chatMessage");
		chatId = document.getElementById("chatId").value;
	}
	
	//메세지 전송
	function sendMessage(){
		//대화창에 표시
		chatWindow.innerHTML += "<div class='myMsg'>" + chatMessage.value + "</div>"
			webSocket.send(chatId + ':' + chatMessage.value); //서버로 전송
			chatMessage.value = ""; //메세지 입력창 내용 지우기
			chatWindow.scrollTop = chatWindow.scrollHeight;
	}
	
	//서버와 연결 종료
	function disconnect(){
		webSocket.close();
	}
	
	//엔터키 입력 처리
	function enterKey(){
		if(window.event.keyCode == 13){
			sendMessage();
		}
	}
	
	//웹소켓 서버에 연결됐을 때 실행
	webSocket.onopen = function(event){
		chatWindow.innerHTML += "채팅에 접속되었습니다.<br/>";
	};
	
	//웹소켓이 닫혔을 때 실행
	webSocket.onclose = function(event){
		chatWindow.innerHTML += "채팅이 종료되었습니다.<br/>";
	};
	
	//에러 발생시
	webSocket.onerror = function(event){
		alert(event.data);
		chatWindow.innerHTML += "채팅 중 에러가 발생했습니다.<br/>";
	};
	
	//메세지를 받았을 때 실행
	webSocket.onmessage = function(event){
		var message = event.data.split(":");
		var sender = message[0];
		var content = message[1];
		if(content != ""){
			if(content.match("/")){//귓속말
				if(content.match(("/" + chatId))){ //나에게 보낸 메세지만 출력
					var temp = content.replace(("/" + chatId), "[귓속말] : ");
					chatWindow.innerHTML += "<div>" + sender + "" + temp + "</div>";
				}
			}else{ //일반대화
				chatWindow.innerHTML += "<div>" + sender + " : " + content + "</div>";				
			}
		}
		chatWindow.scrollTop = chatWindow.scrollHeight;
	}
</script>
<style type="text/css">
	#chatWindow{
		border: 1px solid black;
		width: 270px;
		height: 350px;
		overflow: scroll;
		padding: 5px;
	}
	#chatMessage{
		margin-top: 5px;
		width: 206px;
		height: 30px;
		border-radius: 5px;
	}
	#sendBtn{
		cursor: pointer;
		background-color: var(--color-white);
		height: 30px;
		position: relative;
		top: 2px;
		left: -2px;
		border-radius: 5px;
	}
	#closeBtn{
		cursor: pointer;
		background-color: var(--color-white);
		margin-bottom: 3px;
		position: relative;
		top: 2px;
		left: -2px;
		border-radius: 5px;
	}
	#chatId{
		width: 90px;
		height: 24px;
		border: 1px solid #AAAAAA;
		border-radius: 5px;
		background-color: var(--color-white); 
	}
	.myMsg{
		text-align: right;
	}
</style>
</head>
<body>
	대화명 : <input type="text" id="chatId" value="<%= session.getAttribute("name") %>" readonly />
	<button id="closeBtn" onclick="disconnect()">채팅종료</button>
	<div id="chatWindow"></div>
	<div>
		<input type="text" id="chatMessage" onkeyup="enterKey()">
		<button id="sendBtn" onclick="sendMessage()">전송</button>
	</div>
</body>
</html>