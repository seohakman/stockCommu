<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="stockCommu.domain.*"%>
<% NotifyVO nv = (NotifyVO)request.getAttribute("nv"); 
   ArrayList<NotifyReplyVO> alist = (ArrayList<NotifyReplyVO>)request.getAttribute("alist");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://kit.fontawesome.com/f5807db9d4.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/boardContent.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/global.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<script>
	function replyFn(){
		document.fm.action="<%=request.getContextPath()%>/notify/notifyReplyAction.do?bidx=<%=nv.getBidx()%>&writer=<%=session.getAttribute("id")%>"
		document.fm.method="post";
	}
</script>
<title>게시글</title>
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
		  <a href='<%= request.getContextPath()%>/member/mypage.do'>마이페이지</a>
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
		<table >
			<thead>
				<tr>
					<th id="subject">제목</th>
					<th><%= nv.getSubject() %></th>
					<th id="writer">작성자</th>
					<th><%= nv.getWriter() %></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="4"><%= nv.getContent() %></td>
				</tr>
				<tr>
					<td colspan="4"><img alt="" src="<%=request.getContextPath()%>/imgs/<%=nv.getFilename()%>"></td>
				</tr>
				<tr>
					<td colspan="4" id="count">
						<div>추천수 : <%= nv.getLikeCount() %></div>
						<div>조회수 : <%= nv.getViewCount() %></div>
					</td>
				</tr>
			</tbody>
    	</table>
		<br>
		<div class="div-btn">
			<button id="good" onclick="location.href='<%=request.getContextPath()%>/notify/notifyContentLike.do?name=good&bidx=<%=nv.getBidx()%>'">추천</button>
			<button id="bad" onclick="location.href='<%=request.getContextPath()%>/notify/notifyContentLike.do?name=bad&bidx=<%=nv.getBidx()%>'">비추천</button>
			<button id="report" onclick="location.href=
			'<%=request.getContextPath()%>/common/reportWrite.do?bidx=<%=nv.getBidx()%>&board=<%=request.getServletPath().substring(1,request.getServletPath().indexOf("/",1))%>'">
			신고</button>
<%if(session.getAttribute("midx").equals(nv.getMidx())){ %>
			<button id="modify" onclick="location.href='<%=request.getContextPath()%>/notify/notifyContentModify.do?bidx=<%=nv.getBidx()%>'">수정</button>
			<button onclick="if(!confirm('삭제하시겠습니까?')){return false};
			location.href='<%=request.getContextPath()%>/notify/notifyContentDelete.do?bidx=<%=nv.getBidx()%>'">삭제</button>
<%} %>
		</div>
<%if(session.getAttribute("superMember").equals("Y")){ %>
		<div class="div-btn" style="margin-top:15px;">
			<button onclick="location.href='<%=request.getContextPath() %>/notify/notifyAction.do?type=up&bidx=<%=nv.getBidx()%>'">공지하기</button>
			<button onclick="location.href='<%=request.getContextPath() %>/notify/notifyAction.do?type=down&bidx=<%=nv.getBidx()%>'">공지내리기</button>
		</div>
<%} %>		
		<br>
		<!-- 댓글창 -->
		<table id="replyTable">
			<thead>
				<th colspan="4">댓글</th>
			</thead>
			<tbody>
<%for(NotifyReplyVO nrv: alist){ %>
				<tr>
					<td><%=nrv.getContent() %></td>
					<td><%=nrv.getWriter() %></td>
					<td id="replyDelete">
<%	if(session.getAttribute("id").equals(nrv.getWriter())){ %>
						<button onclick="if(!confirm('삭제하시겠습니까?')){return false}; 
						location.href='<%=request.getContextPath()%>/notify/replyDelete.do?bidx=<%=nrv.getBidx()%>&ridx=<%=nrv.getRidx()%>'">삭제</button>
<%} %>
					</td>
					<td><%=nrv.getWriteday().substring(0,16) %></td>
				</tr>
<%} %>
				<tr>
					<td><br><br></td>
				</tr>
			</tbody>
			<tfoot>
			<form name=fm>
				<tr>
					<td colspan="3">
						<textarea name="notifyReply" id="txtarea"></textarea>
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