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
				session.setAttribute("superMember", mv.getSupermember());
				
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
		}else if(command.equals("/member/superMember.do")) {
			//������ ������ ����
			//�˻� ������ �Ķ���͸� �޾ƿ´�.
			String page = request.getParameter("page");
			if(page==null) {page = "1";}
			String searchType = request.getParameter("searchType");
			if(searchType==null) {searchType = "id";}
			String keyword = request.getParameter("keyword");
			if(keyword==null) {keyword = "";}
			
			// �˻��� �����ϴ� Ŭ������ �Ķ���͸� ��´�.
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setKeyword(keyword);
			scri.setSearchType(searchType);
			
			//pageMaker�� page ���� ��ü ����
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
			//������ ���� �ο��ϱ�
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.addSuper(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('������ ���� �ο� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('������ ���� �ο� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/superMemberDelete.do")) {
			// ������ ���� ����
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.deleteSuper(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('������ ���� ���� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('������ ���� ���� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/memberDelete.do")) {
			//��� ���� ���
			int midx = Integer.parseInt(request.getParameter("midx"));
			
			int value = 0;
			MemberDAO md = new MemberDAO();
			value = md.deleteMember(midx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('ȸ�� ���� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}else {
				out.println("<script>alert('ȸ�� ���� ����');"
						+"location.href='"+request.getContextPath()+"/member/superMember.do'</script>");
			}
		}else if(command.equals("/member/myPofolAction.do")) {
			//������ �Է��ϴ� ���� �޾Ƽ� ����
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
				out.println("<script>alert('�����Ͽ����ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}else if(command.equals("/member/mypage.do")) {
			//���� �������� �̵�
			HttpSession session = request.getSession();
			int midx = (int) session.getAttribute("midx");
			
			MemberDAO md = new MemberDAO();
			ArrayList<MyPageVO> alist = md.selectAllPofol(midx);
			request.setAttribute("alist",alist);
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/myPage.jsp");
			rd.forward(request,response);
			
		}else if(command.equals("/member/myPofolModifyAction.do")) {
			//���������� �����մϴ�.
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
				out.println("<script>alert('�����Ͽ����ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}else if(command.equals("/member/myPofolDelete.do")) {
			//�������� �����͸� ����~
			int myidx = Integer.parseInt(request.getParameter("myidx"));
			MemberDAO md = new MemberDAO();
			int value = md.deletePofol(myidx);
			PrintWriter out = response.getWriter();
			if(value==1) {
				out.println("<script>alert('�����Ǿ����ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}else {
				out.println("<script>alert('�����Ͽ����ϴ�.');"
						+ "location.href='"+request.getContextPath()+"/member/mypage.do'</script>");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
