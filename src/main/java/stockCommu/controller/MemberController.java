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
		// �ּ��� ��ü��θ� ����
		String uri = request.getRequestURI();
		// ������Ʈ �̸��� ����
		String pj = request.getContextPath();
		// ������Ʈ �̸��� �� ������ �����θ� ����
		String command = uri.substring(pj.length());
		System.out.println("command : " + command );
		
		if(command.equals("/member/memberJoin.do")) {
			//ȸ������ ������ �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/member/join.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/memberLogin.do")) {
			//�α��� ������ �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/member/login.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/findID.do")) {
			//���̵�ã�� ������ �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/member/findID.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/findPWD.do")) {
			//���̵�ã�� ������ �̵�
			RequestDispatcher rd = request.getRequestDispatcher("/member/findPWD.jsp");
			rd.forward(request, response);
		}else if(command.equals("/member/memberJoinAction.do")) {
			//DB�� ȸ�������� �߰��Ѵ�.
			String ID = request.getParameter("ID");
			String PWD = request.getParameter("PWD");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			boolean bl = md.checkedID(ID);
			PrintWriter out = response.getWriter();
			if(bl){
				out.println("<script>alert('�ߺ��� ���̵��Դϴ�.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}else {
			    int value = md.insertMember(ID, PWD, name, email);
			   
			    //PrintWriter out = response.getWriter();
			    if(value==1){
			    	response.sendRedirect(pj+"/main/index.do");
				//   out.println("<script>alert('ȸ������ ����');location.href='"+request.getContextPath()+"/index.jsp'</script>");
			    }else{
				   response.sendRedirect(pj+"/member/memberJoin.do");
				//  out.println("<script>alert('ȸ������ ����');location.href='./inputOk.jsp'</script>");
			    } 
			}
		}else if(command.equals("/member/idCheckAction.do")) {
			//DB�� ȸ�������Ϸ��� ID�� ���� ID�� �ִ��� Ȯ���Ѵ�.
			String ID = request.getParameter("ID");
			
			MemberDAO md = new MemberDAO();
			boolean bl = md.checkedID(ID);
			PrintWriter out = response.getWriter();
			if(bl) {
				out.println("<script>alert('�ߺ��� ���̵� �ֽ��ϴ�.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}else{
				out.println("<script>alert('����ص� �Ǵ� ���̵��Դϴ�.');location.href='"+request.getContextPath()+"/member/memberJoin.do'</script>");
			}
		}else if(command.equals("/member/memberLoginAction.do")) {
			// �α��� ��ư�� �������� �α��� ����� ���۽�Ų��.
			// 1. �Ѿ�� ���� �޴´�.
			String id = request.getParameter("ID");
			String pwd = request.getParameter("PWD");
			// 2.ó�� (��������)
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
				// 3.�̵�
				if(session.getAttribute("saveUrl") != null) {
					response.sendRedirect((String)session.getAttribute("saveUrl"));
				}else {
					response.sendRedirect(request.getContextPath()+"/main/index.do");
				}
			}else {
				out.println("<script>alert('���̵�, ��й�ȣ�� Ʋ�Ȱų� �������� �ʴ� ȸ���Դϴ�.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
					
			}
		}else if(command.equals("/member/memberLogoutAction.do")) {
			//�α׾ƿ�
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect(request.getContextPath()+"/main/index.do");
		}else if(command.equals("/member/findIDAction.do")) {
			// ���̵�ã�� ��ư�� Ŭ������ ��
			PrintWriter out = response.getWriter();
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			MemberVO mv = null;
			mv = md.findID(name, email);
			if(mv != null) {
				out.println("<script>alert('"+name+"���� ���̵�� "+mv.getId()+"�Դϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
			}else {
				out.println("<script>alert('���̵� �������� �ʽ��ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/findID.do'</script>");
			}
			
		}else if(command.equals("/member/findPWDAction.do")) {
			// ��й�ȣã�� ��ư�� Ŭ������ ��
			PrintWriter out = response.getWriter();
			String name = request.getParameter("name");
			String id = request.getParameter("ID");
			String email = request.getParameter("email");
			
			MemberDAO md = new MemberDAO();
			MemberVO mv = null;
			mv = md.findPWD(name, id, email);
			if(mv != null) {
				out.println("<script>alert('"+name+"���� ��й�ȣ�� "+mv.getPwd()+"�Դϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
			}else {
				out.println("<script>alert('���̵� �������� �ʽ��ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/findPWD.do'</script>");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
