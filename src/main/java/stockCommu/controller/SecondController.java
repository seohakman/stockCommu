package stockCommu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockCommu.domain.SecondReplyVO;
import stockCommu.domain.SecondVO;
import stockCommu.domain.NotifyVO;
import stockCommu.domain.PageMaker;
import stockCommu.domain.SearchCriteria;
import stockCommu.service.NotifyDAO;
import stockCommu.service.SecondDAO;


@WebServlet("/SecondController")
public class SecondController extends HttpServlet {
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
		
		if(command.equals("/second/secondBoard.do")) {
			//�����Խ������� �̵�
			//�˻� ������ �Ķ���͸� �޾ƿ´�.
			String page = request.getParameter("page");
			if(page==null) {page = "1";}
			String searchType = request.getParameter("searchType");
			if(searchType==null) {searchType = "subject";}
			String keyword = request.getParameter("keyword");
			if(keyword==null) {keyword = "";}

			// �˻��� �����ϴ� Ŭ������ �Ķ���͸� ��´�.
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//pageMaker�� page ���� ��ü ����
			PageMaker pm = new PageMaker();
			SecondDAO sdo = new SecondDAO();
			int cnt = sdo.selectCount(scri);
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			//�������� ���� �������� �ڵ�
			NotifyDAO ndo = new NotifyDAO();
			ArrayList<NotifyVO> nlist;
			nlist = ndo.notifyingAll();
			request.setAttribute("nlist", nlist);
			
			ArrayList<SecondVO> alist;
			alist = sdo.secondSelectAll(scri);
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondBoard.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondWrite.do")) {
			//�� �ۼ� �������� �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondWriteAction.do")) {
			//�ۼ��� ���� DB�� �ִ´�.
			HttpSession session = request.getSession();
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String id = (String) session.getAttribute("id");
			int midx = (int)session.getAttribute("midx");

			SecondDAO sdo = new SecondDAO();
			int value = sdo.insertSecond(subject, content, id, midx);
			PrintWriter out = response.getWriter();
			if(value == 1) {
				response.sendRedirect(pj+"/second/secondBoard.do");
			}else{
				out.println("<script>alert('�۾��� ����');</script>");
			}
		}else if(command.equals("/second/secondContent.do")) {
			// �� �� ���� �������� �̵�
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			SecondDAO sdo = new SecondDAO();
			// �ش� �� ��ȸ�� �÷��ִ� �۾�
			int value = sdo.viewCountUp(bidx);
			// �� �� ���� �������� �۾�
			SecondVO sv = null;
			sv = sdo.secondSelectOne(bidx);
			request.setAttribute("sv", sv);
			
			// �ش� ���� ��� �������� �۾�
			ArrayList<SecondReplyVO> slist = sdo.replySecond(bidx);
			request.setAttribute("slist", slist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondContent.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondContentLike.do")) {
			//���� name ���� ���� ��õ���� ���ϰų� ����.
			String name = request.getParameter("name");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			int value = 0;
			SecondDAO sdo = new SecondDAO();
			if(name.equals("good")) {
				value = sdo.secondLikeCount(1, bidx);
				 if(value==1) { 
					 response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx);
					 }
			}else if(name.equals("bad")) {
				value = sdo.secondLikeCount(-1, bidx);
				 if(value==1) {
					 response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx); 
					 }
			}
			
		}else if(command.equals("/second/secondContentModify.do")) {
			//�ۿ��� �����ϱ� ��ư�� �������� �ش��ϴ� bidx�� ������ ������ �����ϱ� �������� ����Ѵ�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			SecondVO sv = null;
			SecondDAO sdo = new SecondDAO();
			
			sv = sdo.secondSelectOne(bidx);
			request.setAttribute("sv", sv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondModify.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondContentModifyAction.do")) {
			//�� �������������� ��Ϲ�ư�� ��������� DB���� �ش� ���� ������Ʈ �Ѵ�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			
			SecondDAO sdo = new SecondDAO();
			int value = sdo.secondModify(bidx, subject, content);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/second/secondReplyAction.do")) {
			//����� DB�� �����ϱ����� �����͸� �޾ƿ���
			String content = request.getParameter("secondReply");
			String writer = request.getParameter("writer");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			SecondDAO sdo = new SecondDAO();
			int value = sdo.insertReply(content, writer, bidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/second/secondContentDelete.do")) {
			//�Խñ� �����ϱ� DB�� ���ܵΰ� delyn�� �ٲ۴�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			SecondDAO sdo = new SecondDAO();
			int value = sdo.secondDelete(bidx);
			
			if(value==1) {
				response.sendRedirect(pj+"/second/secondBoard.do");
			}
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
