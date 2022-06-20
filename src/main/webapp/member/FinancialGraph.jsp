<%@page import="stockCommu.domain.PageMaker"%>
<%@page import="java.util.ArrayList"%>
<%@page import="stockCommu.domain.GraphVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	
	PageMaker pm = (PageMaker) request.getAttribute("pm");
	ArrayList<GraphVO> alist = (ArrayList<GraphVO>) request.getAttribute("alist"); // 표
	ArrayList<GraphVO> glist = (ArrayList<GraphVO>) request.getAttribute("glist"); // 그래프
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://kit.fontawesome.com/f5807db9d4.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/global.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/myPage.css" />
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700&display=swap"
			rel="stylesheet">
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
    	<h1 id="homeTitle"> 자 산 추 이 </h1>
    	<div id="chart_div" style="width :100%;"></div>
    	<form action="<%=request.getContextPath()%>/member/myGraphAdd.do" method="post">
	    	<div id = "inputProperty"> 
	  			<input name="inputDate" type="date" value="날짜" required>
	  			자산 : <input name="money" type="text" required>
	    		<button type="submit" >추가</button>
	    	</div>    	
    	</form>
    	<table>
    		<thead>
    			<tr>
    				<th>날짜</th>
    				<th>자산</th>
    				<th></th>
    			</tr>
    		</thead>
    		<tbody>
<%for(GraphVO gv: alist){ %>
				<tr>
					<td><%=gv.getYear() %>-<%=gv.getMonth() %>-<%=gv.getDay() %></td>
					<td><%=gv.getMoney() %>원</td>
					<td>
						<button id="deleteProperty" onclick="if(!confirm('삭제하시겠습니까?')){return false};
						location.href='<%=request.getContextPath()%>/member/mygraphDeleteAction.do?inputdate=<%=gv.getInputDate()%>'">삭제</button>
					</td>
				</tr>
<%} %>    		
    		</tbody>
    	</table>
		<!-- page -->
        <div class="page">
<% 
	if(pm.isPrev() == true){
		out.println("<a href='"+request.getContextPath()+"/member/mygraph.do?page="+(pm.getStartPage()-1)+"'>◀</a>");		
	}
	for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++){
		out.println("<a href='"+request.getContextPath()+"/member/mygraph.do?page="+i+"'>"+i+"</a>");
	}
	if(pm.isNext() && pm.getEndPage() > 0){
		out.println("<a href='"+request.getContextPath()+"/member/mygraph.do?page="+(pm.getEndPage()+1)+"'>▶</a>");		
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
<!-- 구글 그래프 차트 script -->
<script type="text/javascript">	
    	google.charts.load('current', {packages: ['corechart', 'line'], callback: drawBasic});	
    	google.charts.setOnLoadCallback(addData);
    	var data;
    	var chart;
    	var options;

    	function drawBasic() { 

			  data = new google.visualization.DataTable();
			  data.addColumn('datetime', 'year');
			  data.addColumn('number', 'Property');
			  
			  data.addRows([
			  ]);
			  options = {
					  width:'100%'
			  }
			  
			  chart = new google.visualization.LineChart(document.getElementById('chart_div'));
			  chart.draw(data, options);
			}
    	
	 	
	 	var year = [];
    	var month = [];
    	var day = [];
    	var value = [];	 	
<%for(GraphVO gv : glist){%>
	    year.push(<%=gv.getYear()%>);
	    month.push(<%=gv.getMonth()%>);
	    day.push(<%=gv.getDay()%>);
	    value.push(<%=gv.getMoney()%>);
<% }%>
        var len = year.length;
	 	function addData(){
	    	var arr = new Array();
	    	for(let i = 0; i<len; i++){
			     arr = [new Date(year[i],month[i]-1,day[i]),value[i]];
			     data.addRow(arr);
			 }
			options = {
					pointSize: 5,
					width:'100%',
				    hAxis: {
				      title: 'Time',
				      format:'yy-MM',
				      ticks: [new Date(year[0],month[0]-1,day[0]),new Date(year[len-1],month[len-1]-1,day[len-1])]
				    },
				    explorer: { axis: 'horizontal' }
				  };
	 		chart.draw(data, options);
	 	}
	 	
</script>
<script src="<%=request.getContextPath() %>/script/script.js"></script>
</html>