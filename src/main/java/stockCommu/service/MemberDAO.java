package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.MemberVO;
import stockCommu.domain.SearchCriteria;

public class MemberDAO {
	// 연결객체
	private Connection conn;
	// 구문객체
	private PreparedStatement pstmt;
	
	
	public MemberDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	public int insertMember(String ID,String PWD,String name,String email){
		int value=0;		
		//sql 구문을 사용하겠다
	    String sql="insert into member(midx,ID,PWD,name,email,point)"
	   	  		+ "values(midx.nextval,?,?,?,?,?)";
		// Statement - 쿼리를 실행하기 위한 클래스, 쿼리를 실행하기 위한 클래스를 생성한다.
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
		//입력받은 ID와 같은 ID가 DB에 있는지 확인
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
		//로그인 메서드
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
		//아이디 찾기 메서드
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
		//비밀번호 찾기 메서드
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
		//멤버 한명만 가져오는 메서드
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
		//DB의 모든 멤버 가져오기
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
		//특정 조건에 맞는 게시글의 총개수를 구한다.
		String str = ""; // scri에 들어있는 searchType과 keyword의 값에 따라 다른 값을 준다.
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
		//멤버에게 관리자 권한을 부여한다.
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
		// 멤버의 관리자 권한을 제거한다
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
