package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.GraphVO;
import stockCommu.domain.MemberVO;
import stockCommu.domain.MyPageVO;
import stockCommu.domain.SearchCriteria;

public class MemberDAO {
	// ���ᰴü
	private Connection conn;
	// ������ü
	private PreparedStatement pstmt;
	
	
	public MemberDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	public int insertMember(String ID,String PWD,String name,String email){
		int value=0;		
		//sql ������ ����ϰڴ�
	    String sql="insert into member(midx,ID,PWD,name,email,point)"
	   	  		+ "values(midx.nextval,?,?,?,?,?)";
		// Statement - ������ �����ϱ� ���� Ŭ����, ������ �����ϱ� ���� Ŭ������ �����Ѵ�.
		try{
		//	Statement stat = conn.createStatement();
		//value = stat.executeUpdate(sql);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, ID);
		pstmt.setString(2, PWD);
		pstmt.setString(3, name);
		pstmt.setString(4, email);
		pstmt.setInt(5, 0);
		value = pstmt.executeUpdate();
			   
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return value;
	}
	
	public boolean checkedID(String ID) {
		ResultSet rs = null;
		//�Է¹��� ID�� ���� ID�� DB�� �ִ��� Ȯ��
		String sql = "select id from member where id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			rs = pstmt.executeQuery();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(rs.next()) {
				return true;
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public MemberVO memberLogin(String ID, String PWD) {
		//�α��� �޼���
		String sql = "select * from member where id = ? and pwd =?";
		ResultSet rs = null;
		MemberVO mv = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ID);
			pstmt.setString(2, PWD);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mv = new MemberVO();
				mv.setMidx(rs.getInt("midx"));
				mv.setName(rs.getString("name"));
				mv.setId(rs.getString("id"));
				mv.setPwd(rs.getString("pwd"));
				mv.setSupermember(rs.getString("supermember"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return mv;
	}
	
	public MemberVO findID(String name, String email) {
		//���̵� ã�� �޼���
		String sql = "select * from member where name = ? and email =?";
		ResultSet rs = null;
		MemberVO mv = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mv = new MemberVO();
				mv.setMidx(rs.getInt("midx"));
				mv.setId(rs.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return mv;
	}
	
	public MemberVO findPWD(String name,String ID, String email) {
		//��й�ȣ ã�� �޼���
		String sql = "select * from member where name = ? and id=? and email =?";
		ResultSet rs = null;
		MemberVO mv = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, ID);
			pstmt.setString(3, email);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mv = new MemberVO();
				mv.setMidx(rs.getInt("midx"));
				mv.setPwd(rs.getString("pwd"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		return mv;
	}
	
	public MemberVO findMemberOne(String ID, String PWD) {
		//��� �Ѹ� �������� �޼���
		String sql = "select * from member where id = ? and pwd = ?";
		MemberVO mv = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,ID);
			pstmt.setString(2, PWD);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mv = new MemberVO();
				mv.setMidx(rs.getInt("midx"));
				mv.setId(rs.getString("id"));
				mv.setPwd(rs.getString("pwd"));
				mv.setName(rs.getString("name"));
				mv.setEmail(rs.getString("email"));
				mv.setMemberday(rs.getString("memberday"));
				mv.setPoint(rs.getInt("point"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mv;
	}
	
	public ArrayList<MemberVO> memberSelectAll(SearchCriteria scri){
		//DB�� ��� ��� ��������
		ArrayList<MemberVO> alist = new ArrayList();
		ResultSet rs = null;

		String str = "";
		if(scri.getSearchType().equals("id")) {
			str = "and id like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("name")){
			str = "and name like '%"+scri.getKeyword()+"%'";
		}

		String sql = "SELECT * FROM("
					+"SELECT tb.*, ROWNUM rNum FROM("
					+	"SELECT * FROM member ORDER BY midx deSC"
					+	") tb"
					+")WHERE rNum BETWEEN ? AND ? " + str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*10+1);
			pstmt.setInt(2, (scri.getPage()*10));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberVO mv = new MemberVO();
				mv.setMidx(rs.getInt("midx"));
				mv.setName(rs.getString("name"));
				mv.setId(rs.getString("id"));
				mv.setEmail(rs.getString("email"));
				mv.setDelyn(rs.getString("delyn"));
				
				alist.add(mv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alist;
	}
	
	public int selectCount(SearchCriteria scri) {
		//Ư�� ���ǿ� �´� �Խñ��� �Ѱ����� ���Ѵ�.
		String str = ""; // scri�� ����ִ� searchType�� keyword�� ���� ���� �ٸ� ���� �ش�.
		if(scri.getSearchType().equals("id")) {
			str = "id like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("name")){
			str = "name like '%"+scri.getKeyword()+"%'";
		}
		String sql= "select count(*) cnt from member where " + str;
		int value = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				value = rs.getInt("cnt");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	public int addSuper(int midx) {
		//������� ������ ������ �ο��Ѵ�.
		int value =0;
		String sql = "update member set supermember = 'Y' where midx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,midx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int deleteSuper(int midx) {
		// ����� ������ ������ �����Ѵ�
		int value = 0;
		String sql = "update member set supermember = 'N' where midx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int deleteMember(int midx) {
		//ȸ�� ���� �޼���
		int value = 0;
		String sql = "update member set delyn = 'Y' where midx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int insertPofol(int midx, String name, String price, String estimate, String eps, String per, String pbr, String etc) {
		//���������� ������ ���ο� ��ü�� ����Ѵ�.
		int value = 0;
		String sql = "insert into mypage(myidx, midx, name, price,estimate, eps, per, pbr, etc) values(myidx.nextval, ?,?,?,?,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			pstmt.setString(2, name);
			pstmt.setString(3,price);
			pstmt.setString(4, estimate);
			pstmt.setString(5, eps);
			pstmt.setString(6,per);
			pstmt.setString(7, pbr);
			pstmt.setString(8,etc);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	public ArrayList<MyPageVO> selectAllPofol(int midx){
		//DB�� ������ �������� �޼���
		ArrayList<MyPageVO> alist = new ArrayList();
		String sql = "select * from mypage where midx = ? order by myidx asc";
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MyPageVO mpv = new MyPageVO();
				mpv.setName(rs.getString("name"));
				mpv.setPrice(rs.getString("price"));
				mpv.setEstimate(rs.getString("estimate"));
				mpv.setEps(rs.getString("eps"));
				mpv.setPer(rs.getString("per"));
				mpv.setPbr(rs.getString("pbr"));
				mpv.setEtc(rs.getString("etc"));
				mpv.setMidx(rs.getInt("midx"));
				mpv.setMyidx(rs.getInt("myidx"));
				
				
				alist.add(mpv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alist;
	}
	
	public int modifyPofol(int myidx,int midx, String name, String price, String estimate, String eps, String per, String pbr, String etc) {
		int value = 0;
		
		String sql = "update mypage set name = ?, price = ?, estimate = ?, eps = ?, per = ?, pbr = ?, etc = ? where myidx = ? and midx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2,price);
			pstmt.setString(3, estimate);
			pstmt.setString(4, eps);
			pstmt.setString(5,per);
			pstmt.setString(6, pbr);
			pstmt.setString(7,etc);
			pstmt.setInt(8, myidx);
			pstmt.setInt(9, midx);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int deletePofol(int myidx) {
		int value = 0;
		String sql = "delete from mypage where myidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, myidx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int modifyPassword(int midx, String password) {
		int value = 0 ;
		String sql = "update member set pwd = ? where midx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setInt(2, midx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public int insertGraph(int midx, String inputDate, String money) {
		//�׷����� ����� �ڻ����̸� �Է��Ѵ�.
		int value = 0;
		String sql = "insert into mygraph(midx, inputdate, money) values(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			pstmt.setString(2,inputDate);
			pstmt.setString(3, money);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public ArrayList<GraphVO> selectGraph(int midx, SearchCriteria scri) {
		// �׷��� �׸��� ���� �ҷ����� �޼���
		// ArrayList�� �޾ƿ;��ϰ� GraphVO ���� ����ȭ�ؼ� �޾ƿ;���
		ArrayList<GraphVO> alist = new ArrayList<GraphVO>();
		GraphVO gv = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM("  
				+	"SELECT tb.*, ROWNUM rNum FROM("
				+	"SELECT * FROM mygraph WHERE midx = ? ORDER BY INPUTDATE DESC"
				+	") tb) WHERE rNum BETWEEN ? AND ?";

		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			pstmt.setInt(2, (scri.getPage()-1)*10+1);
			pstmt.setInt(3, (scri.getPage()*10));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				gv = new GraphVO();
				String date = rs.getString("inputdate");
				gv.setInputDate(rs.getString("inputdate"));
				gv.setMidx(rs.getInt("midx"));
				gv.setYear(date.substring(0,4));
				gv.setMonth(date.substring(5,7));
				gv.setDay(date.substring(8,10));
				gv.setMoney(rs.getString("money"));
				alist.add(gv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return alist;
	}
	
	public int totalProperty(int midx) {
		int value = 0;
		ResultSet rs = null;
		String sql = "select count(*) cnt from mygraph where midx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				value = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	public int deleteProperty(int midx, String inputdate) {
		int value = 0;
		String sql = "delete from mygraph where midx = ? and inputdate = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, midx);
			pstmt.setString(2, inputdate);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
