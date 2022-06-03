<%@page import="java.util.ArrayList"%>
<%@page import="stockCommu.domain.GraphVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	
	
	ArrayList<GraphVO> alist = (ArrayList<GraphVO>) request.getAttribute("alist");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/global.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
<script src="https://kit.fontawesome.com/9eb162ac0d.js"
	crossorigin="anonymous"></script>
<link
rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"
/>
<!-- 구글 그래프 스크립트 -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<title>자산 추이</title>
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
    	<div id="chart_div"></div>
    	<!-- 구글 그래프 차트 -->
    	<script type="text/javascript">
    	
    	google.charts.load('current', {packages: ['corechart', 'line']});
    	google.charts.setOnLoadCallback(drawBasic);
		
    	var data;
    	var chart;
    	var options;
	 	function drawBasic() { 

			  data = new google.visualization.DataTable();
			  data.addColumn('datetime', 'year');
			  data.addColumn('number', 'Dogs');

			  data.addRows([
			    [new Date(2000, 8, 5), 0],
			    [new Date(2000, 8, 6), 10],
			    [new Date(2000, 8, 7), 23],
			    [new Date(2000, 8, 8), 17],
			    [new Date(2000, 8, 9), 18],
			    [new Date(2000, 8, 20), 9],
			  ]);

			  options = {
			    hAxis: {
			      title: 'Time',
			      format:'yy-MM',
			      ticks: [new Date(2000,8,1), new Date(2002,08,20)]
			    },
			    vAxis: {
			      title: 'Popularity'
			    },
			    explorer: { axis: 'horizontal' }
			  };

			  chart = new google.visualization.LineChart(document.getElementById('chart_div'));

			  chart.draw(data, options);
			} 
	 	
	 	function addData(arr){
	 		
	 		const year = ['2020','2020','2021','2021'];
	    	var month = ['08','09','03','05'];
	    	var day = ['01','03','04','05'];
	    	var value = [10,23,15,30];
	    	var len = year.length;
	    	var arr = new Array();
			for(let i = 0; i<len; i++){
			     /* arr[i] = ["new"+" Date("+year[i]+","+month[i]+","+day[i]+"),"+value[i]]; */
			     arr = [new Date(year[i],month[i],day[i]),value[i]];
			     data.addRow(arr);
			 }
			   /* arr = arr.toString(); */
			   console.log(arr);
			   console.log(year);
	 		chart.draw(data, options);
	 		alert('데이터추가 완료');
	 	}
	 	console.log(new Date(2022,06,07));
    	</script>
    	<form action="<%=request.getContextPath()%>/member/myGraphAdd.do" method="post">
	    	<div> 
	  			<input name="inputDate" type="date" value="날짜">
	  			자산 : <input name="money" type="text">
	    		<button type="button" onclick="addData()">데이터 추가</button>
	    		<button type="submit" >확인</button>
	    	</div>    	
    	</form>
    	<table>
    		<thead></thead>
    		<tbody>
<%for(GraphVO gv : alist){ %>    		
    			<tr>
    				<td><%=gv.getYear() %></td>
    				<td><%=gv.getMoney() %></td>
    			</tr>
<%} %>
    		</tbody>
    	</table>
        <div class="fixed_content">fixed</div>
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