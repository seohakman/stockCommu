package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.MemberVO;

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
				mv.setId(rs.getString("id"));
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
	
	public MemberVO findID(String name, String email) {
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
	
}
