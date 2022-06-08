<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/findID.css" />
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
	function findIDbtn(){
		document.fm.action="<%=request.getContextPath()%>/member/findIDAction.do";
		document.fm.method="post";
	}
</script>
<title>아이디 찾기</title>
</head>
<body>
	<!-- Navbar -->
    <nav id="navbar">
      <div class="navbar__logo">
        <i class="far fa-arrow-alt-circle-up"></i>
        <a href="<%= request.getContextPath()%>/main/index.do">StockSophia</a>
      </div>
			<a id="login" href="<%= request.getContextPath()%>/member/memberLogin.do">로그인</a>
      <div class="dropdown">
        <button class="navbar__toggle-btn">
          <i class="fas fa-bars fa-2x"></i>
        </button>
        <div class="navbar__toggle_content" id="myDropdown">
          <a href="<%= request.getContextPath()%>/member/memberJoin.do">회원가입</a>
          <a href="<%= request.getContextPath()%>/second/secondBoard.do">자유게시판</a>
          <a href="<%= request.getContextPath()%>/main/index.do">추천게시판</a>
          <a href="<%= request.getContextPath()%>/notify/notifyBoard.do">공지사항</a>
        </div>
      </div>
    </nav>
    <!-- main content -->
    <section id="home">
      <h1 id="homeTitle"> 아이디 찾기 </h1>
	  <form name=fm>
	  <table >
        <tr>
          <td>이름 :</td>
          <td><input type="text" name="name" required></td>
        </tr>
        <tr>
          <td>이메일 :</td>
          <td><input type="text" name="email" required></td>
        </tr>
        <tr>
          <td colspan="2">
            <button type="submit" id="findID-btn" onclick="findIDbtn()"><span>아이디 찾기</span></button>
          </td>
        </tr>
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