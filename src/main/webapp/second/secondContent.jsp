<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="stockCommu.domain.*"%>
<% SecondVO sv = (SecondVO)request.getAttribute("sv"); 
   ArrayList<SecondReplyVO> slist = (ArrayList<SecondReplyVO>)request.getAttribute("slist");
%>
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
<script>
	function replyFn(){
		document.fm.action="<%=request.getContextPath()%>/second/secondReplyAction.do?bidx=<%=sv.getBidx()%>&writer=<%=sv.getWriter()%>"
		document.fm.method="post";
	}
</script>
<title>자유게시판 게시글</title>
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
      <h1 id="homeTitle"> 자유 게시판 </h1>
		<table >
			<thead>
				<tr>
					<th id="subject">제목</th>
					<th><%= sv.getSubject() %></th>
					<th id="writer">작성자</th>
					<th><%= sv.getWriter() %></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="4"><%= sv.getContent() %></td>
				</tr>
				<tr>
					<td colspan="4" id="count">
						<div>추천수 : <%= sv.getLikeCount() %></div>
						<div>조회수 : <%= sv.getViewCount() %></div>
					</td>
				</tr>
			</tbody>
    	</table>
		<br>
		<div class="div-btn">
			<button id="good" onclick="location.href='<%=request.getContextPath()%>/second/secondContentLike.do?name=good&bidx=<%=sv.getBidx()%>'">추천</button>
			<button id="bad" onclick="location.href='<%=request.getContextPath()%>/second/secondContentLike.do?name=bad&bidx=<%=sv.getBidx()%>'">비추천</button>
			<button id="report">신고</button>
<%if(session.getAttribute("midx").equals(sv.getMidx())){ %>
			<button id="modify" onclick="location.href='<%=request.getContextPath()%>/second/secondContentModify.do?bidx=<%=sv.getBidx()%>'">수정</button>
			<button onclick="if(!confirm('삭제하시겠습니까?')){return false};
			location.href='<%=request.getContextPath()%>/second/secondContentDelete.do?bidx=<%=sv.getBidx()%>'">삭제</button>
<%} %>
		</div>
		<br>
		<!-- 댓글창 -->
		<table id="replyTable">
			<thead>
				<th colspan="3">댓글</th>
			</thead>
			<tbody>
<%for(SecondReplyVO srv: slist){ %>
				<tr>
					<td><%=srv.getContent() %></td>
					<td><%=srv.getWriter() %></td>
					<td><%=srv.getWriteday() %></td>
				</tr>
<%} %>
				<tr>
					<td><br><br></td>
				</tr>
			</tbody>
			<tfoot>
			<form name=fm>
				<tr>
					<td colspan="2">
						<textarea name="secondReply" id="txtarea"></textarea>
					</td>
					<td id="submitTd">
						<button type="submit" id="replySubmit" onclick="replyFn()">등록</button>
					</td>
				</tr>
			</form>
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