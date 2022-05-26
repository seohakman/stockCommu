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
		// 주소의 전체경로를 추출
		String uri = request.getRequestURI();
		// 프로젝트 이름을 추출
		String pj = request.getContextPath();
		// 프로젝트 이름을 뺀 나머지 가상경로를 추출
		String command = uri.substring(pj.length());
		System.out.println("command : " + command );
		
		if(command.equals("/second/secondBoard.do")) {
			//자유게시판으로 이동
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
			SecondDAO sdo = new SecondDAO();
			int cnt = sdo.selectCount(scri);
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			//공지사항 글을 가져오는 코드
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
			//글 작성 페이지로 이동
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondWrite.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondWriteAction.do")) {
			//작성한 글을 DB에 넣는다.
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
				out.println("<script>alert('글쓰기 실패');</script>");
			}
		}else if(command.equals("/second/secondContent.do")) {
			// 글 상세 내용 페이지로 이동
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			SecondDAO sdo = new SecondDAO();
			// 해당 글 조회수 올려주는 작업
			int value = sdo.viewCountUp(bidx);
			// 글 상세 내용 가져오는 작업
			SecondVO sv = null;
			sv = sdo.secondSelectOne(bidx);
			request.setAttribute("sv", sv);
			
			// 해당 글의 댓글 가져오는 작업
			ArrayList<SecondReplyVO> slist = sdo.replySecond(bidx);
			request.setAttribute("slist", slist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondContent.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondContentLike.do")) {
			//들어온 name 값에 따라 추천수를 더하거나 뺀다.
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
			//글에서 수정하기 버튼을 눌럿을때 해당하는 bidx의 정보를 가져와 수정하기 페이지를 출력한다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			SecondVO sv = null;
			SecondDAO sdo = new SecondDAO();
			
			sv = sdo.secondSelectOne(bidx);
			request.setAttribute("sv", sv);
			
			RequestDispatcher rd = request.getRequestDispatcher("/second/secondModify.jsp");
			rd.forward(request, response);
		}else if(command.equals("/second/secondContentModifyAction.do")) {
			//글 수정페이지에서 등록버튼을 눌럿을경우 DB에서 해당 글을 업데이트 한다.
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			
			SecondDAO sdo = new SecondDAO();
			int value = sdo.secondModify(bidx, subject, content);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('실패하였습니다.')</script>");
			}
		}else if(command.equals("/second/secondReplyAction.do")) {
			//댓글을 DB에 저장하기위해 데이터를 받아오자
			String content = request.getParameter("secondReply");
			String writer = request.getParameter("writer");
			int bidx = Integer.parseInt(request.getParameter("bidx"));
			
			SecondDAO sdo = new SecondDAO();
			int value = sdo.insertReply(content, writer, bidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				response.sendRedirect(pj+"/second/secondContent.do?bidx="+bidx);
			}else {
				out.println("<script>alert('실패하였습니다.')</script>");
			}
		}else if(command.equals("/second/secondContentDelete.do")) {
			//게시글 삭제하기 DB는 남겨두고 delyn만 바꾼다.
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
