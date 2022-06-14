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
import javax.websocket.Session;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import stockCommu.domain.MainReplyVO;
import stockCommu.domain.MainVO;
import stockCommu.domain.NotifyVO;
import stockCommu.domain.PageMaker;
import stockCommu.domain.SearchCriteria;
import stockCommu.service.MainDAO;
import stockCommu.service.NotifyDAO;


@WebServlet("/MainController")
public class MainController extends HttpServlet {
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
		
		// ���� ���ε� ���
		String uploadPath = "D:\\open API (A)\\Dev\\stockCommu\\src\\main\\webapp\\";
		String saveFolder = "imgs";
		String saveFullPath = uploadPath + saveFolder;
		
		if(command.equals("/main/index.do")) {
			//������������ �̵�
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
			MainDAO mdo = new MainDAO();
			int cnt = mdo.selectCount(scri);
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			//�������� ���� �������� �ڵ�
			NotifyDAO ndo = new NotifyDAO();
			ArrayList<NotifyVO> nlist;
			nlist = ndo.notifyingAll();
			request.setAttribute("nlist", nlist);
			
			ArrayList<MainVO> alist;
			alist = mdo.mainSelectAll(scri);
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			RequestDispatcher rd = request.getRequestDispatcher("/main/index.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainWrite.do")) {
			//�� �ۼ� �������� �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/main/mainWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainWriteAction.do")) {
			//�ۼ��� ���� DB�� �ִ´�.
			int sizeLimit = 1024*1024*15;
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
			
			HttpSession session = request.getSession();
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content").replace("\r\n", "<br>");
			String id = (String) session.getAttribute("id");
			int midx = (int)session.getAttribute("midx");
			
			// �����ڿ� ����� ������ ��� ��ü�� �����Ѵ�.
			Enumeration files = multi.getFileNames();
			// ��� ���ϰ�ü�� ���� �̸��� ��´�.
			String file = (String)files.nextElement();
			//����Ǵ� ���� �̸�
			String fileName = multi.getFilesystemName(file);
			//���� ���� �̸�
			String originFileName = multi.getOriginalFileName(file);
			
			MainDAO mdo = new MainDAO();
			int value = mdo.insertMain(subject, content, id, midx, fileName);
			PrintWriter out = response.getWriter();
			if(value == 1) {
				response.sendRedirect(pj+"/main/index.do");
			}else{
				out.println("<script>alert('�۾��� ����');</script>");
			}
		}else if(command.equals("/main/mainContent.do")) {
			// �� �� ���� �������� �̵�
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			MainDAO mdo = new MainDAO();
			// �ش� �� ��ȸ�� �÷��ִ� �۾�
			int value = mdo.viewCountUp(bidx);
			// �� �� ���� �������� �۾�
			MainVO mv = null;
			mv = mdo.mainSelectOne(bidx);
			request.setAttribute("mv", mv);
			// �ش� ���� ��� �������� �۾�
			ArrayList<MainReplyVO> mlist = mdo.replyMain(bidx);
			request.setAttribute("mlist", mlist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/main/mainContent.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainContentLike.do")) {
			//���� name ���� ���� ��õ���� ���ϰų� ����.
			String name = request.getParameter("name");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			int value = 0;
			MainDAO mdo = new MainDAO();
			if(name.equals("good")) {
				value = mdo.mainLikeCount(1, bidx);
				 if(value==1) { 
					 response.sendRedirect(pj+"/main/mainContent.do?bidx="+bidx);
					 }
			}else if(name.equals("bad")) {
				value = mdo.mainLikeCount(-1, bidx);
				 if(value==1) {
					 response.sendRedirect(pj+"/main/mainContent.do?bidx="+bidx); 
					 }
			}
			
		}else if(command.equals("/main/mainContentModify.do")) {
			//�ۿ��� �����ϱ� ��ư�� �������� �ش��ϴ� bidx�� ������ ������ �����ϱ� �������� ����Ѵ�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			MainVO mv = null;
			MainDAO mdo = new MainDAO();
			
			mv = mdo.mainSelectOne(bidx);
			request.setAttribute("mv", mv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/main/mainModify.jsp");
			rd.forward(request, response);
		}else if(command.equals("/main/mainContentModifyAction.do")) {
			//�� �������������� ��Ϲ�ư�� ��������� DB���� �ش� ���� ������Ʈ �Ѵ�.
			int sizeLimit = 1204*1024*15;
			MultipartRequest multi = new MultipartRequest(request, saveFullPath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());

			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String subject = multi.getParameter("subject");
			String content = multi.getParameter("content").replace("\r\n", "<br>");
			
			Enumeration files = multi.getFileNames();
			String file = (String)files.nextElement();
			String fileName = multi.getFilesystemName(file);
			String originFileName = multi.getOriginalFileName(file);
			
			MainDAO mdo = new MainDAO();
			int value = mdo.mainModify(bidx, subject, content, fileName);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/main/mainContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/main/mainReplyAction.do")) {
			//����� DB�� �����ϱ����� �����͸� �޾ƿ���
			String content = request.getParameter("mainReply").replace("\r\n", "<br>");
			String writer = request.getParameter("writer");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			MainDAO mdo = new MainDAO();
			int value = mdo.insertReply(content, writer, bidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/main/mainContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.')</script>");
			}
		}else if(command.equals("/main/mainContentDelete.do")) {
			//�Խñ� �����ϱ� DB�� ���ܵΰ� delyn�� �ٲ۴�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			MainDAO mdo = new MainDAO();
			int value = mdo.mainDelete(bidx);
			
			if(value==1) {
				response.sendRedirect(pj+"/main/index.do");
			}
		}else if(command.equals("/main/replyDelete.do")) {
			//�Խñ��� ����� �����Ѵ�.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			int ridx = Integer.parseInt(request.getParameter("ridx"));
			MainDAO md = new MainDAO();
			int value = md.replyDelete(ridx);
			if(value == 1) {
				response.sendRedirect(pj+"/main/mainContent.do?bidx="+bidx);
			}else {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('�����߽��ϴ�.'); history.back()</script>");
			}
			
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
