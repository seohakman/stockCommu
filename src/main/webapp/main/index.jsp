<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"  %>
<%@ page import="stockCommu.domain.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<% 
	ArrayList<MainVO> alist = (ArrayList<MainVO>)request.getAttribute("alist");
	ArrayList<NotifyVO> nlist = (ArrayList<NotifyVO>)request.getAttribute("nlist");
	PageMaker pm = (PageMaker)request.getAttribute("pm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://kit.fontawesome.com/f5807db9d4.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/global.css" />
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/myStyle.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
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
		<c:if test="${not empty mv.midx }">
		  <a href='<%= request.getContextPath()%>/member/memberLogoutAction.do'>로그아웃</a>
		  <a href='<%= request.getContextPath()%>/member/mypage.do'>마이페이지</a>
		</c:if>
		<!-- 관리자일 경우 관리페이지 보여줌 -->
		<c:if test="${mv.supermember eq 'Y' }">
		  <a href='<%= request.getContextPath()%>/member/superMember.do'>관리페이지</a>
		</c:if>
		<c:if test="${empty mv }">
		  <a href='<%= request.getContextPath()%>/member/memberJoin.do'>회원가입</a>
		</c:if>
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
				<!-- 공지사항 글 -->
				<!-- 로그인 했을 경우에만 게시물 클릭 이동이 가능하게 한다. -->
				<c:if test="${not empty nlist }">
					<c:if test="${not empty mv }">
						<c:forEach var="vo" items="${nlist}">
							<tr class="b">
								<th>공지</th>
								<th>
									<a href="<%=request.getContextPath()%>/notify/notifyContent.do?bidx=${vo.bidx}">${vo.subject}</a>
								</th>
								<th>${vo.writer }</th>
								<th>${vo.viewCount }</th>
								<th>${vo.likeCount }</th>
								<c:set var="writeday" value="${vo.writeday }"/>
								<th>${fn:substring(writeday,0,16)}</th>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty mv }">
						<c:forEach var="vo" items="${nlist}">
							<tr class="b">
								<th>공지</th>
								<th>${vo.subject}</th>
								<th>${vo.writer }</th>
								<th>${vo.viewCount }</th>
								<th>${vo.likeCount }</th>
								<c:set var="writeday" value="${vo.writeday }"/>
								<th>${fn:substring(writeday,0,16)}</th>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>
				<!-- 추천게시판 글 -->
				<c:if test="${empty alist }">
					<tr>
						<td colspan="6"> 등록 된 게시글이 없습니다. </td>
					</tr>
				</c:if>
				<c:if test="${not empty alist }">
					<c:if test="${not empty mv }">
						<c:forEach var="vo" items="${alist}">
							<tr class="b">
								<td>${vo.bidx }</td>
								<td>
									<a href="<%=request.getContextPath()%>/main/mainContent.do?bidx=${vo.bidx}">${vo.subject}</a>
								</td>
								<td>${vo.writer }</td>
								<td>${vo.viewCount }</td>
								<td>${vo.likeCount }</td>
								<c:set var="writeday" value="${vo.writeday }"/>
								<td>${fn:substring(writeday,0,16)}</td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${empty mv }">
						<c:forEach var="vo" items="${alist}">
							<tr class="b">
								<td>${vo.bidx }</td>
								<td>${vo.subject}</td>
								<td>${vo.writer }</td>
								<td>${vo.viewCount }</td>
								<td>${vo.likeCount }</td>
								<c:set var="writeday" value="${vo.writeday }"/>
								<td>${fn:substring(writeday,0,16)}</td>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>
			</tbody>
		</table>
			<!-- 검색창 -->
		<form id="search" action="<%=request.getContextPath()%>/main/index.do" method="post">
			<select name="searchType">
				<option value="subject">제목</option>
				<option value="writer">작성자</option>
			</select>
			<input type="text" name="keyword">
			<input type="submit" value="검색">
		</form>
		<c:if test="${not empty mv }">
			<div id="writeDiv">
				<button id="writeBtn" onclick="writePage()">글쓰기</button>
			</div> <br> <br>
		</c:if>
        <div class="page">
        
<% 
	if(pm.isPrev() == true){
		out.println("<a href='"+request.getContextPath()+"/main/index.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");		
	}
	for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++){
		out.println("<a href='"+request.getContextPath()+"/main/index.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
	}
	if(pm.isNext() && pm.getEndPage() > 0){
		out.println("<a href='"+request.getContextPath()+"/main/index.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");		
	}
%>
        </div>
        <div class="fixed_content">fixed</div>
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
<script>
	function writePage(){
		location.href="<%= request.getContextPath()%>/main/mainWrite.do";
	}
</script>
<script src="<%=request.getContextPath() %>/script/script.js"></script>
</html>