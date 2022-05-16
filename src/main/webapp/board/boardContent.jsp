<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/boardContent.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/global.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<script src="https://kit.fontawesome.com/9eb162ac0d.js"
	crossorigin="anonymous"></script>
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<title>게시글</title>
</head>
<body>
	<!-- Navbar -->
    <nav id="navbar">
      <div class="navbar__logo">
        <i class="far fa-arrow-alt-circle-up"></i>
        <a href="#">StockSophia</a>
      </div>
			<a id="login" href="#">로그인</a>
      <div class="dropdown">
        <button class="navbar__toggle-btn">
          <i class="fas fa-bars fa-2x"></i>
        </button>
        <div class="navbar__toggle_content" id="myDropdown">
          <a href="#">회원가입</a>
          <a href="#">자유게시판</a>
          <a href="#">추천게시판</a>
          <a href="#">마이페이지</a>
        </div>
      </div>
    </nav>
    <!-- main content -->
    <section id="home">
      <h1 id="homeTitle"> 추천 게시판 </h1>
		<table >
			<thead>
				<tr>
					<th>제목</th>
					<th></th>
					<th>작성자</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="4">
						달이 익어가니 서둘러 젊은 피야
						민들레 한 송이 들고
						사랑이 어지러이 떠다니는 밤이야
						날아가 사뿐히 이루렴
						팽팽한 어둠 사이로
						떠오르는 기분
						이 거대한 무중력에 혹 휘청해도
						두렵진 않을 거야
						푸르른 우리 위로
						커다란 strawberry moon 한 스쿱
						나에게 너를 맡겨볼래 eh-oh
						바람을 세로질러
						날아오르는 기분 so cool
						삶이 어떻게 더 완벽해 ooh
						다시 마주하기 어려운 행운이야
						온몸에 심장이 뛰어
						Oh 오히려 기꺼이 헤매고픈 밤이야
						너와 길 잃을 수 있다면
						맞잡은 서로의 손으로
						출입구를 허문
						이 무한함의 끝과 끝 또 위아래로
						비행을 떠날 거야
						푸르른 우리 위로
						커다란 strawberry moon 한 스쿱
						나에게 너를 맡겨볼래 eh-oh
						바람을 세로질러
						날아오르는 기분 so cool
						삶이 어떻게 더 완벽해 ooh
						놀라워 이보다
						꿈같은 순간이 또 있을까 (더 있을까)
						아마도 우리가 처음 발견한
						오늘 이 밤의 모든 것, 그 위로 날아
						푸르른 우리 위로
						커다란 strawberry moon 한 스쿱
						세상을 가져보니 어때 eh-oh
						바람을 세로질러
						날아오르는 기분 so cool
						삶이 어떻게 더 완벽해 ooh
					</td>
				</tr>
			</tbody>
    	</table>
		<br>
		<div class="div-btn">
			<button id="good">추천</button>
			<button id="bad">비추천</button>
			<button id="report">신고</button>
			<button id="modify">수정</button>
		</div>
		<br>
		<!-- 댓글창 -->
		<table id="replyTable">
			<thead>
				<th colspan="2">댓글</th>
			</thead>
			<tbody>
				<tr>
					<td>하나둘셋넷</td>
					<td>작성자</td>
				</tr>
				<tr>
					<td>하나둘셋넷</td>
					<td>작성자</td>
				</tr>	
				<tr>
					<td><br><br></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td>
						<textarea name="" id="txtarea"></textarea>
					</td>
					<td id="submitTd">
						<button type="submit" id="replySubmit">등록</button>
					</td>
				</tr>
			</tfoot>
		</table>
    </section>
    <!-- Contact -->
    <footer>
      <section id="contact" class="section">
        <h1 class="contact__title"></h1>
        <p class="contact__rights">
          2022 StockSophia - All rights reserved
        </p>
      </section>
    </footer>
</body>
<script src="<%=request.getContextPath() %>/script/script.js"></script>
</html>