package stockCommu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import stockCommu.domain.NotifyReplyVO;
import stockCommu.domain.NotifyVO;
import stockCommu.domain.PageMaker;
import stockCommu.domain.SearchCriteria;
import stockCommu.service.NotifyDAO;


@WebServlet("/NotifyController")
public class NotifyController extends HttpServlet {
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
		
		String uploadPath = "D:\\open API (A)\\Dev\\stockCommu\\src\\main\\webapp\\";
		String saveFolder = "imgs";
		String saveFullPath = uploadPath + saveFolder;
		
		if(command.equals("/notify/notifyBoard.do")) {
			//���������������� �̵�
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
			NotifyDAO ndo = new NotifyDAO();
			int cnt = ndo.selectCount(scri);
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			ArrayList<NotifyVO> alist;
			alist = ndo.notifySelectAll(scri);
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyBoard.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyWrite.do")) {
			//�� �ۼ� �������� �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyWriteAction.do")) {
			//�ۼ��� ���� DB�� �ִ´�.
			int sizeLimit = 1204*1024*15;
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
			Enumeration files = multi.getFileNames();
			String file = (String)files.nextElement();
			String fileName = multi.getFilesystemName(file);
			String originFileName = multi.getOriginalFileName(file);
			
			HttpSession session = request.getSession();
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			String id = (String) session.getAttribute("id");
			int midx = (int)session.getAttribute("midx");

			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.insertNotify(subject, content, id, midx, fileName);
			PrintWriter out = response.getWriter();
			if(value == 1) {
				response.sendRedirect(pj+"/notify/notifyBoard.do");
			}else{
				out.println("<script>alert('�۾��� ����');</script>");
			}
		}else if(command.equals("/notify/notifyContent.do")) {
			// �� �� ���� �������� �̵�
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyDAO ndo = new NotifyDAO();
			// �ش� �� ��ȸ�� �÷��ִ� �۾�
			int value = ndo.viewCountUp(bidx);
			// �� �� ���� �������� �۾�
			NotifyVO nv = null;
			nv = ndo.notifySelectOne(bidx);
			request.setAttribute("nv", nv);
			
			// �ش� ���� ��� �������� �۾�
			ArrayList<NotifyReplyVO> alist = ndo.replyNotify(bidx);
			request.setAttribute("alist", alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyContent.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyContentLike.do")) {
			//���� name ���� ���� ��õ���� ���ϰų� ����.
			String name = request.getParameter("name");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			int value = 0;
			NotifyDAO ndo = new NotifyDAO();
			if(name.equals("good")) {
				value = ndo.notifyLikeCount(1, bidx);
				 if(value==1) { 
					 response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx);
					 }
			}else if(name.equals("bad")) {
				value = ndo.notifyLikeCount(-1, bidx);
				 if(value==1) {
					 response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx); 
					 }
			}
			
		}else if(command.equals("/notify/notifyContentModify.do")) {
			//�ۿ��� �����ϱ� ��ư�� �������� �ش��ϴ� bidx�� ������ ������ �����ϱ� �������� ����Ѵ�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyVO nv = null;
			NotifyDAO ndo = new NotifyDAO();
			
			nv = ndo.notifySelectOne(bidx);
			request.setAttribute("nv", nv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyModify.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyContentModifyAction.do")) {
			//�� �������������� ��Ϲ�ư�� ��������� DB���� �ش� ���� ������Ʈ �Ѵ�.
			int sizeLimit = 1204*1024*15;
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
			Enumeration files = multi.getFileNames();
			String file = (String)files.nextElement();
			String fileName = multi.getFilesystemName(file);
			String originFileName = multi.getOriginalFileName(file);
			
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content");
			
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.notifyModify(bidx, subject, content, fileName);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/notify/notifyReplyAction.do")) {
			//����� DB�� �����ϱ����� �����͸� �޾ƿ���
			String content = request.getParameter("notifyReply");
			String writer = request.getParameter("writer");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			System.out.println(bidx);
			
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.insertReply(content, writer, bidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/notify/notifyContentDelete.do")) {
			//�Խñ� �����ϱ� DB�� ���ܵΰ� delyn�� �ٲ۴�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.notifyDelete(bidx);
			
			if(value==1) {
				response.sendRedirect(pj+"/notify/notifyBoard.do");
			}
			
		}else if(command.equals("/notify/notifyAction.do")) {
			//�������� �ٸ� �Խ��ǿ� �ø��� ������
			String type = request.getParameter("type");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyDAO ndo = new NotifyDAO();
			
			int value = ndo.notifyingType(type,bidx);
			PrintWriter out = response.getWriter();
			if(value==1 && type.equals("up")) {
				out.println("<script>alert('�������׿� ����ϼ̽��ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/notify/notifyBoard.do';</script>");
			}else if(value==1 && type.equals("down")){
				out.println("<script>alert('�����ϼ̽��ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/notify/notifyBoard.do';</script>");
			}else {
				out.println("<script>alert('�����߽��ϴ�.');"
						+ "history.back();</script>");	
			}
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
