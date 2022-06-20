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
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://kit.fontawesome.com/f5807db9d4.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/global.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/myPage.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<title>마이 페이지 - 포트폴리오</title>
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
    		<a href='<%= request.getContextPath()%>/member/mypagePassword.do'>비밀번호 변경</a>
    	</div>
    </nav>
	<!-- main content -->
    <section id="home">
    	<table>
    		<thead>
    			<tr>
    				<th colspan="7" class="tableCap">My Portfolio</th>
    			</tr>
    			<tr>
    				<th>종목</th>
    				<th>주당 가격</th>
    				<th>추정치</th>
    				<th>EPS</th>
    				<th>PER</th>
    				<th>PBR</th>
    				<th>비고</th>
    				<th colspan="2"></th>
    			</tr>
    		</thead>
    		<tbody>
<%for(MyPageVO mpv : alist){ %>
    			<tr>
	    		<!-- 회원이 저장한 포폴을 불러온다. -->
	    		<form action="<%=request.getContextPath()%>/member/myPofolModifyAction.do?myidx=<%=mpv.getMyidx()%>&midx=<%=mpv.getMidx()%>" method="post">
    				<td><input name="name" type="text" size="10" value="<%=mpv.getName() %>"></td>
    				<td><input name="price" type="text" size="5" value="<%=mpv.getPrice()%>"></td>
    				<td><input name="estimate" type="text" size="5" value="<%=mpv.getEstimate()%>"></td>
    				<td><input name="eps" type="text" size="1" value="<%=mpv.getEps()%>"></td>
    				<td><input name="per" type="text" size="1" value="<%=mpv.getPer()%>"></td>
    				<td><input name="pbr" type="text" size="1" value="<%=mpv.getPbr()%>"></td>
    				<td class="etc"><input name="etc" type="text" value="<%=mpv.getEtc()%>"></td>
    				<td><button class="subBtn" type="submit">수정</button></td>
    				<td><button class="subBtn" type="button" onclick="if(!confirm('삭제하시겠습니까?')){return false};
			location.href='<%=request.getContextPath()%>/member/myPofolDelete.do?myidx=<%=mpv.getMyidx()%>'">삭제</button></td>
	    		</form>
    			</tr>
<%} %>
    			<tr>
    			<!-- 새로운 포폴을 등록한다. -->
    			<form action="<%=request.getContextPath()%>/member/myPofolAction.do" method="post" >
    				<td><input name="name" type="text" size="10"></td>
    				<td><input name="price" type="text" size="5"></td>
    				<td><input name="estimate" type="text" size="5"></td>
    				<td><input name="eps" type="text" size="1"></td>
    				<td><input name="per" type="text" size="1"></td>
    				<td><input name="pbr" type="text" size="1"></td>
    				<td class="etc"><input name="etc" type="text"></td>
    				<td><button class="subBtn" type="submit">등록</button></td>
	    		</form>
    			</tr>
    		</tbody>
    	</table>
    </section>
    <!-- Contact -->
<%if(session.getAttribute("midx") != null){ %>
      <div id="footer_chat_div">
		<div id="chatDiv" style="display: none;">   			
    		<iframe id="chatWindow"
  				src="<%= request.getContextPath() %>/WebSocket/ChatWindow.jsp">
			</iframe>
		</div>
		<button onclick="location.href='#chatDiv'" class="chat_toggle_btn" >
   			<i id="chatIcon" class="fa-solid fa-comments fa-2x"></i>
   		</button>
      </div>
<%} %>
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