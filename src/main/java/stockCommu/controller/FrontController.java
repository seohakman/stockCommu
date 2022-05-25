package stockCommu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
			// 주소의 전체경로를 추출
			String uri = request.getRequestURI();
			// 프로젝트 이름을 추출
			String pj = request.getContextPath();
			// 전체 경로에서 프로젝트 이름을 뺀 나머지 가상경로를 추출
			// ex) /member/memberList.do
			String command = uri.substring(pj.length());
			
			String[] subpath = command.split("/");
			String location = subpath[1];    // 2번째 문자열 추출 ex) member
			
			
			 if(location.equals("member")) {
				 MemberController mc = new MemberController();
				 mc.doPost(request, response); 
			 }else if(location.equals("main")) {
				 MainController mac = new MainController(); 
				 mac.doPost(request, response);
			 }else if(location.equals("second")) {
				 SecondController sc = new SecondController();
				 sc.doPost(request, response);
			 }else if(location.equals("notify")) {
				 NotifyController nc = new NotifyController();
				 nc.doPost(request, response);
			 }
			 
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doGet(request, response);
	}

}