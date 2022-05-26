<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="stockCommu.domain.*" %>
<% 
	ArrayList<NotifyVO> alist = (ArrayList<NotifyVO>)request.getAttribute("alist"); 
	PageMaker pm = (PageMaker)request.getAttribute("pm");
%>
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

<title>공지사항</title>
</head>
<body>
	<!-- Navbar -->
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
    <!-- main content -->
    <section id="home">
      <h1 id="homeTitle"> 공지사항 </h1>
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
<% for(NotifyVO nv: alist){ 
	if(session.getAttribute("midx") != null){ %>
	<!-- 로그인 했을 경우에만 게시물 클릭 이동이 가능하게 한다. -->
			<tbody>
             	<tr class="b">
	                <td><%=nv.getBidx() %></td>
	                <td>
	                	<a href="<%=request.getContextPath()%>/notify/notifyContent.do?bidx=<%=nv.getBidx()%>"><%=nv.getSubject()%></a>
	                </td>
	                <td><%=nv.getWriter() %></td>
	                <td><%=nv.getViewCount() %></td>
	                <td><%=nv.getLikeCount() %></td>
	                <td><%=nv.getWriteday() %></td>
             	</tr>
			</tbody>
<%}else{%>
			<tbody>
             	<tr class="b">
	                <td><%=nv.getBidx() %></td>
	                <td><%=nv.getSubject()%></td>
	                <td><%=nv.getWriter() %></td>
	                <td><%=nv.getViewCount() %></td>
	                <td><%=nv.getLikeCount() %></td>
	                <td><%=nv.getWriteday() %></td>
             	</tr>
			</tbody>
<%}} %>
		</table>
		<form id="search" action="<%=request.getContextPath()%>/notify/notifyBoard.do" method="post">
			<select name="searchType">
				<option value="subject">제목</option>
				<option value="writer">작성자</option>
			</select>
			<input type="text" name="keyword">
			<input type="submit" value="검색">
		</form>
<%if(session.getAttribute("midx") != null && session.getAttribute("superMember").equals("Y")){ %>
		<!-- 관리자의 경우에만 공지사항 글쓰기 가능 -->
		<div id="writeDiv">
			<button id="writeBtn" onclick="writePage()">글쓰기</button>
		</div> <br> <br>
<%} %>
        <div class="page">
<% 
	if(pm.isPrev() == true){
		out.println("<a href='"+request.getContextPath()+"/notify/notifyBoard.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");		
	}
	for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++){
		out.println("<a href='"+request.getContextPath()+"/notify/notifyBoard.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
	}
	if(pm.isNext() && pm.getEndPage() > 0){
		out.println("<a href='"+request.getContextPath()+"/notify/notifyBoard.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");		
	}
%>
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
		location.href="<%= request.getContextPath()%>/notify/notifyWrite.do";
	}
</script>
<script src="<%=request.getContextPath() %>/script/script.js"></script>
</html>