<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/join.css" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<script src="https://kit.fontawesome.com/9eb162ac0d.js"
	crossorigin="anonymous"></script>
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<title>회원가입</title>
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
      <h1 id="homeTitle"> 회 원 가 입 </h1>
			<table >
        <tr>
          <td>아이디 :</td>
          <td>
			<input type="text" required> 
			<button type="button" class="check"> 중복확인 </button>
		  </td>
        </tr>
		<tr>
          <td>이름 :</td>
          <td><input type="text" required></td>
        </tr>
        <tr>
          <td>비밀번호 :</td>
          <td><input type="password" required></td>
        </tr>
		<tr>
          <td>비밀번호 확인 :</td>
          <td><input type="password" required></td>
        </tr>
        <tr>
          <td>이메일 :</td>
          <td><input type="email" required></td>
        </tr>
        <tr>
          <td colspan="2" class="tdJoin">
            <button type="submit" id="join-btn"><span>생성</span></button>
          </td>
        </tr>
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
<script src="<%= request.getContextPath() %>/script/script.js"></script>
</html>