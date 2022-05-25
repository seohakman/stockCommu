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
		// 주소의 전체경로를 추출
		String uri = request.getRequestURI();
		// 프로젝트 이름을 추출
		String pj = request.getContextPath();
		// 프로젝트 이름을 뺀 나머지 가상경로를 추출
		String command = uri.substring(pj.length());
		System.out.println("command : " + command );
		
		if(command.equals("/notify/notifyBoard.do")) {
			//공지사항페이지로 이동
			//검색 폼에서 파라미터를 받아온다.
			String page = request.getParameter("page");
			if(page==null) {page = "1";}
			String searchType = request.getParameter("searchType");
			if(searchType==null) {searchType = "subject";}
			String keyword = request.getParameter("keyword");
			if(keyword==null) {keyword = "";}

			// 검색을 정의하는 클래스에 파라미터를 담는다.
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//pageMaker로 page 정보 객체 생성
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
			//글 작성 페이지로 이동
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyWriteAction.do")) {
			//작성한 글을 DB에 넣는다.
			HttpSession session = request.getSession();
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			String id = (String) session.getAttribute("id");
			int midx = (int)session.getAttribute("midx");

			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.insertNotify(subject, content, id, midx);
			PrintWriter out = response.getWriter();
			if(value == 1) {
				response.sendRedirect(pj+"/notify/notifyBoard.do");
			}else{
				out.println("<script>alert('글쓰기 실패');</script>");
			}
		}else if(command.equals("/notify/notifyContent.do")) {
			// 글 상세 내용 페이지로 이동
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyDAO ndo = new NotifyDAO();
			// 해당 글 조회수 올려주는 작업
			int value = ndo.viewCountUp(bidx);
			// 글 상세 내용 가져오는 작업
			NotifyVO nv = null;
			nv = ndo.notifySelectOne(bidx);
			request.setAttribute("nv", nv);
			
			// 해당 글의 댓글 가져오는 작업
			ArrayList<NotifyReplyVO> alist = ndo.replyNotify(bidx);
			request.setAttribute("alist", alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyContent.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyContentLike.do")) {
			//들어온 name 값에 따라 추천수를 더하거나 뺀다.
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
			//글에서 수정하기 버튼을 눌럿을때 해당하는 bidx의 정보를 가져와 수정하기 페이지를 출력한다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyVO nv = null;
			NotifyDAO ndo = new NotifyDAO();
			
			nv = ndo.notifySelectOne(bidx);
			request.setAttribute("nv", nv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/notify/notifyModify.jsp");
			rd.forward(request, response);
		}else if(command.equals("/notify/notifyContentModifyAction.do")) {
			//글 수정페이지에서 등록버튼을 눌럿을경우 DB에서 해당 글을 업데이트 한다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.notifyModify(bidx, subject, content);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('실패하였습니다.')</script>");
			}
		}else if(command.equals("/notify/notifyReplyAction.do")) {
			//댓글을 DB에 저장하기위해 데이터를 받아오자
			String content = request.getParameter("notifyReply");
			String writer = request.getParameter("writer");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.insertReply(content, writer, bidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/notify/notifyContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('실패하였습니다.')</script>");
			}
		}else if(command.equals("/notify/notifyContentDelete.do")) {
			//게시글 삭제하기 DB는 남겨두고 delyn만 바꾼다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			NotifyDAO ndo = new NotifyDAO();
			int value = ndo.notifyDelete(bidx);
			
			if(value==1) {
				response.sendRedirect(pj+"/notify/index.do");
			}
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
