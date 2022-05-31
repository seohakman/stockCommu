<%@page import="java.util.ArrayList"%>
<%@page import="stockCommu.domain.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	ArrayList<MyPageVO> alist = (ArrayList<MyPageVO>) request.getAttribute("alist");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/global.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/myPage.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<script src="https://kit.fontawesome.com/9eb162ac0d.js"
	crossorigin="anonymous"></script>
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<title>마이 페이지 - 비밀번호 변경</title>
</head>
<body>
	<!-- 가로 Navbar -->
    <nav id="navbar">
      <div class="navbar__logo">
        <i class="far fa-arrow-alt-circle-up"></i>
        <a href="<%= request.getContextPath()%>/main/index.do">StockSophia</a>
      </div>
      		<!-- 세션에 midx값이 없을 경우 로그인버튼을 보여주고 아니면 회원 이름을 표시한다. -->
<% if(session.getAttribute("midx")==null){ 
	session.setAttribute("saveUrl", request.getRequestURI().substring(0,request.getRequestURI().length()-3)+"do");
%>
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
        	<!-- 로그인 했을 경우 로그아웃 버튼을 보여주고 아닌경우 회원가입 버튼을 보여줌 -->
<%if(session.getAttribute("midx") != null){ %>
		  <a href='<%= request.getContextPath()%>/member/memberLogoutAction.do'>로그아웃</a>
		  <!-- 관리자일 경우 관리페이지 보여줌 -->
<%	if(session.getAttribute("superMember").equals("Y")){ %>
		  <a href='<%= request.getContextPath()%>/member/superMember.do'>관리페이지</a>
<%	
}}else{
%>
		  <a href='<%= request.getContextPath()%>/member/memberJoin.do'>회원가입</a>
<%} %>
          <a href="<%= request.getContextPath()%>/second/secondBoard.do">자유게시판</a>
          <a href="<%= request.getContextPath()%>/main/index.do">추천게시판</a>
          <a href="<%= request.getContextPath()%>/notify/notifyBoard.do">공지사항</a>
        </div>
      </div>
    </nav>
    <!-- mypage nav -->
    <nav id="mpDiv">
    	<div>
    		<a href='<%= request.getContextPath()%>/member/mypage.do'>포트폴리오</a>
    		<a href='<%= request.getContextPath()%>/member/mygraph.do'>자산추이</a>
    		<a href='<%= request.getContextPath()%>/member/myscrap.do'>스크랩</a>
    		<a href='<%= request.getContextPath()%>/member/mypagePassword.do'>비밀번호 변경</a>
    	</div>
    </nav>
	<!-- main content -->
    <section id="home">
    	<form action="<%=request.getContextPath()%>/member/mypagePasswordAction.do" method="post">
    		<table class="myPassword">
    			<thead>
	    			<tr>
	    				<th colspan="2">비밀번호 확인</th>
	    			</tr>
    			</thead>
    			<tbody>
	    			<tr>
	    				<td>password: <input type="password" name="password" required></td>
	    				<td><button class="pSubBtn" type="submit">확인<button></td>
	    			</tr>
    			</tbody>
    		</table>
    	</form>
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