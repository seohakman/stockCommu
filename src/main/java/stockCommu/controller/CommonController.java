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
		// �ּ��� ��ü��θ� ����
		String uri = request.getRequestURI();
		// ������Ʈ �̸��� ����
		String pj = request.getContextPath();
		// ������Ʈ �̸��� �� ������ �����θ� ����
		String command = uri.substring(pj.length());
		System.out.println("command : " + command );
		
		if(command.equals("/common/reportWrite.do")) {
			// �� �Խñۿ��� �Ű��������� �̵��Ѵ�.
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
				out.println("<script> alert('�����ƽ��ϴ�.'); location.href='"+request.getContextPath()+"/"+board+"/"+board+"Content.do?bidx="+bidx+"' </script>");
			}
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
