package stockCommu.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;


@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		// 주소의 전체경로를 추출
		String uri = request.getRequestURI();
		// 프로젝트 이름을 추출
		String pj = request.getContextPath();
		// 프로젝트 이름을 뺀 나머지 가상경로를 추출
		String command = uri.substring(pj.length());
		System.out.println("command : " + command );
		
		if(command.equals("/main/index.do")) {
			//메인페이지로 이동
			RequestDispatcher rd = request.getRequestDispatcher("/main/index.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainWrite.do")) {
			//글 작성 페이지로 이동
			RequestDispatcher rd = request.getRequestDispatcher("/main/mainWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainWriteAction.do")) {
			HttpSession session = request.getSession();
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String id = (String) session.getAttribute("id");
			int midx = (int)session.getAttribute("midx");

			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
