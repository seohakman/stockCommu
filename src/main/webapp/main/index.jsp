<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/global.css" />
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/myStyle.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<script src="https://kit.fontawesome.com/9eb162ac0d.js"
	crossorigin="anonymous"></script>
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>

<title>추천 게시판</title>
</head>
<body>
	<!-- Navbar -->
    <nav id="navbar">
      <div class="navbar__logo">
        <i class="far fa-arrow-alt-circle-up"></i>
        <a href="<%= request.getContextPath()%>/main/index.do">StockSophia</a>
      </div>
<% if(session.getAttribute("midx")==null){ %>
	  <a id="login" href="<%= request.getContextPath()%>/member/memberLogin.do">로그인</a>
<% }else{
		out.println("<a id='login'>"+session.getAttribute("name") +"</a>");
}
%>
      <div class="dropdown">
        <button class="navbar__toggle-btn">
          <i class="fas fa-bars fa-2x"></i>
        </button>
        <div class="navbar__toggle_content" id="myDropdown">
<%if(session.getAttribute("midx") != null){ %>
		  <a href='<%= request.getContextPath()%>/member/memberLogoutAction.do'>로그아웃</a>
<%	
}else{
%>
		  <a href='<%= request.getContextPath()%>/member/memberJoin.do'>회원가입</a>
<%} %>
          <a href="<%= request.getContextPath()%>/second/secondBoard.do">자유게시판</a>
          <a href="<%= request.getContextPath()%>/main/index.do">추천게시판</a>
          <a href="<%= request.getContextPath()%>/notify/notifyBoard.do">공지사항</a>
        </div>
      </div>
    </nav>
    <!-- main content -->
    <section id="home">
      <h1 id="homeTitle"> 추천 게시판 </h1>
		<table>
			<thead>
				<tr>
					<th class="b1">No.</th>
					<th class="b2">제목</th>
					<th class="b3">작성자</th>
					<th class="b4">조회</th>
					<th class="b5">추천</th>
					<th class="b6">작성일</th>
				</tr>
			</thead>
			<tbody>
             	<tr class="b">
	                <td >1</td>
	                <td >공지사항</td>
	                <td >운영자</td>
	                <td >100</td>
	                <td>100</td>
	                <td>2022.05.10</td>
             	</tr>
				<tr class="b">
					<td >2</td>
					<td >글 제목</td>
					<td >홍길동</td>
					<td >3</td>
					<td>2</td>
					<td>2022.05.10</td>
				</tr>
			</tbody>
		</table>
<%if(session.getAttribute("midx") != null){ %>
		<div id="writeDiv">
			<button id="writeBtn" onclick="writePage()">글쓰기</button>
		</div> <br> <br> <br>
<%} %>
        <div class="page">
        	&ltrif; 1 2 3 4 5 6 &rtrif;
        </div>
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
<script>
	function writePage(){
		location.href="<%= request.getContextPath()%>/main/mainWrite.do";
	}
</script>
<script src="<%=request.getContextPath() %>/script/script.js?ver=1"></script>
</html>