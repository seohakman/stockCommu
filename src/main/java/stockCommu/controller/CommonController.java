package stockCommu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockCommu.domain.ReportVO;
import stockCommu.service.CommonDAO;

@WebServlet("/CommonController")
public class CommonController extends HttpServlet {
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
		
		if(command.equals("/common/reportWrite.do")) {
			// 각 게시글에서 신고페이지로 이동한다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String board = request.getParameter("board");
			HttpSession session = request.getSession();
			int midx = (int) session.getAttribute("midx");
			
			ReportVO rv = new ReportVO();
			rv.setBidx(bidx);
			rv.setBoard(board);
			rv.setMidx(midx);
			
			request.setAttribute("rv", rv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/common/reportWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/common/reportWriteAction.do")) {
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			int midx = Integer.parseInt(request.getParameter("midx"));
			String board = request.getParameter("board");
			String reason = request.getParameter("reason");
			String content = request.getParameter("content");
			
			CommonDAO cd = new CommonDAO();
			int value = cd.reportBoard(bidx, midx, board, reason, content);
			if(value==1) {
				PrintWriter out = response.getWriter();
				out.println("<script> alert('접수됐습니다.'); location.href='"+request.getContextPath()+"/"+board+"/"+board+"Content.do?bidx="+bidx+"' </script>");
			}
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
