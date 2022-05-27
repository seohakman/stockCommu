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


import stockCommu.domain.MemberVO;
import stockCommu.domain.MyPageVO;
import stockCommu.domain.PageMaker;
import stockCommu.domain.SearchCriteria;
import stockCommu.service.MemberDAO;


@WebServlet("/MemberController")
public class MemberController extends HttpServlet {
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
		
		if(command.equals("/member/memberJoin.do")) {
			//회원가입 페이지 이동
			RequestDispatcher rd = request.getRequestDispatcher("/member/join.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/memberLogin.do")) {
			//로그인 페이지 이동
			RequestDispatcher rd = request.getRequestDispatcher("/member/login.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/findID.do")) {
			//아이디찾기 페이지 이동
			RequestDispatcher rd = request.getRequestDispatcher("/member/findID.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/findPWD.do")) {
			//아이디찾기 페이지 이동
			RequestDispatcher rd = request.getRequestDispatcher("/member/findPWD.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/memberJoinAction.do")) {
			//DB에 회원정보를 추가한다.
			String ID = request.getParameter("ID");
			String PWD = request.getParameter("PWD");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			boolean bl = md.checkedID(ID);
			PrintWriter out = response.getWriter();
			if(bl){
				out.println("<script>alert('중복된 아이디입니다.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}else {
			    int value = md.insertMember(ID, PWD, name, email);
			   
			    //PrintWriter out = response.getWriter();
			    if(value==1){
			    	response.sendRedirect(pj+"/main/index.do");
				//   out.println("<script>alert('회원가입 성공');location.href='"+request.getContextPath()+"/index.jsp'</script>");
			    }else{
				   response.sendRedirect(pj+"/member/memberJoin.do");
				//  out.println("<script>alert('회원가입 실패');location.href='./inputOk.jsp'</script>");
			    } 
			}
		}else if(command.equals("/member/idCheckAction.do")) {
			//DB에 회원가입하려는 ID와 같은 ID가 있는지 확인한다.
			String ID = request.getParameter("ID");
			
			MemberDAO md = new MemberDAO();
			boolean bl = md.checkedID(ID);
			PrintWriter out = response.getWriter();
			if(bl) {
				out.println("<script>alert('중복된 아이디가 있습니다.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}else{
				out.println("<script>alert('사용해도 되는 아이디입니다.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}
		}else if(command.equals("/member/memberLoginAction.do")) {
			// 로그인 버튼이 눌렷을때 로그인 기능을 동작시킨다.
			// 1. 넘어온 값을 받는다.
			String id = request.getParameter("ID");
			String pwd = request.getParameter("PWD");
			// 2.처리 (쿼리실행)
			MemberDAO md = new MemberDAO();
			MemberVO mv = null;
			mv = md.memberLogin(id, pwd);
			PrintWriter out = response.getWriter();
			
			HttpSession session = request.getSession();
			if(mv != null) {
				session.setAttribute("midx", mv.getMidx());
				session.setAttribute("id", mv.getId());
				session.setAttribute("name", mv.getName());
				session.setAttribute("point", mv.getPoint());
				session.setAttribute("superMember", mv.getSupermember());
				
				// 3.이동
				if(session.getAttribute("saveUrl") != null) {
					response.sendRedirect((String)session.getAttribute("saveUrl"));
				}else {
					response.sendRedirect(request.getContextPath()+"/main/index.do");
				}
			}else {
				out.println("<script>alert('아이디, 비밀번호가 틀렸거나 존재하지 않는 회원입니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
					
			}
		}else if(command.equals("/member/memberLogoutAction.do")) {
			//로그아웃
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect(request.getContextPath()+"/main/index.do");
		}else if(command.equals("/member/findIDAction.do")) {
			// 아이디찾기 버튼을 클릭했을 때
			PrintWriter out = response.getWriter();
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			MemberVO mv = null;
			mv = md.findID(name, email);
			if(mv != null) {
				out.println("<script>alert('"+name+"님의 아이디는 "+mv.getId()+"입니다.');"
						+ "location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
			}else {
				out.println("<script>alert('아이디가 존재하지 않습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/findID.do'</script>");
			}
			
		}else if(command.equals("/member/findPWDAction.do")) {
			// 비밀번호찾기 버튼을 클릭했을 때
			PrintWriter out = response.getWriter();
			String name = request.getParameter("name");
			String id = request.getParameter("ID");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			MemberVO mv = null;
			mv = md.findPWD(name, id, email);
			if(mv != null) {
				out.println("<script>alert('"+name+"님의 비밀번호는 "+mv.getPwd()+"입니다.');"
						+ "location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
			}else {
				out.println("<script>alert('아이디가 존재하지 않습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/findPWD.do'</script>");
			}
		}else if(command.equals("/member/superMember.do")) {
			//관리자 페이지 입장
			//검색 폼에서 파라미터를 받아온다.
			String page = request.getParameter("page");
			if(page==null) {page = "1";}
			String searchType = request.getParameter("searchType");
			if(searchType==null) {searchType = "id";}
			String keyword = request.getParameter("keyword");
			if(keyword==null) {keyword = "";}
			
			// 검색을 정의하는 클래스에 파라미터를 담는다.
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//pageMaker로 page 정보 객체 생성
			PageMaker pm = new PageMaker();
			MemberDAO md = new MemberDAO();
			int cnt = md.selectCount(scri);
			pm.setScri(scri);
			pm.setTotalCount(cnt);
			
			ArrayList<MemberVO> alist = new ArrayList();
			alist = md.memberSelectAll(scri);
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/superMember.jsp");
			rd.forward(request, response);
			
		}else if(command.equals("/member/superMemberAdd.do")) {
			//관리자 권한 부여하기
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.addSuper(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('관지자 권한 부여 성공');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('관지자 권한 부여 실패');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/superMemberDelete.do")) {
			// 관리자 권한 제거
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.deleteSuper(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('관지자 권한 제거 성공');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('관지자 권한 제거 실패');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/memberDelete.do")) {
			//멤버 정지 기능
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.deleteMember(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('회원 정지 성공');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('회원 정지 실패');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/myPofolAction.do")) {
			//포폴에 입력하는 값을 받아서 수행
			HttpSession session = request.getSession();
			int midx = (int) session.getAttribute("midx");
			String name = request.getParameter("name");
			String price = request.getParameter("price");
			String estimate = request.getParameter("estimate");
			String eps = request.getParameter("eps");
			String per = request.getParameter("per");
			String pbr = request.getParameter("pbr");
			String etc = request.getParameter("etc");
			
			MemberDAO md = new MemberDAO();
			int value = md.insertPofol(midx, name, price, estimate, eps, per, pbr, etc);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}else {
				out.println("<script>alert('실패하였습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}else if(command.equals("/member/mypage.do")) {
			//마이 페이지로 이동
			HttpSession session = request.getSession();
			int midx = (int) session.getAttribute("midx");
			
			MemberDAO md = new MemberDAO();
			ArrayList<MyPageVO> alist = md.selectAllPofol(midx);
			request.setAttribute("alist",alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/myPage.jsp");
			rd.forward(request,response);
			
		}else if(command.equals("/member/myPofolModifyAction.do")) {
			//마이포폴을 수정합니다.
			int myidx = Integer.parseInt(request.getParameter("myidx"));
			int midx = Integer.parseInt(request.getParameter("midx"));
			String name = request.getParameter("name");
			String price = request.getParameter("price");
			String estimate = request.getParameter("estimate");
			String eps = request.getParameter("eps");
			String per = request.getParameter("per");
			String pbr = request.getParameter("pbr");
			String etc = request.getParameter("etc");
			
			MemberDAO md = new MemberDAO();
			int value = md.modifyPofol(myidx, midx, name, price, estimate, eps, per, pbr, etc);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}else {
				out.println("<script>alert('실패하였습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}else if(command.equals("/member/myPofolDelete.do")) {
			//마이포폴 데이터를 삭제~
			int myidx = Integer.parseInt(request.getParameter("myidx"));
			MemberDAO md = new MemberDAO();
			int value = md.deletePofol(myidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('삭제되었습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}else {
				out.println("<script>alert('실패하였습니다.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
