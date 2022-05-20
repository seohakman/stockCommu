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


import stockCommu.domain.MemberVO;
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
				System.out.println(mv.getName());
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
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
