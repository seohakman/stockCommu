package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;

import stockCommu.dbconn.Dbconn;

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
		// 시스템 시간
		LocalDate Now = LocalDate.now(ZoneId.of("Asia/Seoul"));
		int value=0;		
		//sql 구문을 사용하겠다
	    String sql="insert into a_member(ID,PWD,name,email,memberday,point)"
	   	  		+ "values(?,?,?,?,?,?)";
		// Statement - 쿼리를 실행하기 위한 클래스, 쿼리를 실행하기 위한 클래스를 생성한다.
		try{
		//	Statement stat = conn.createStatement();
		//value = stat.executeUpdate(sql);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, ID);
		pstmt.setString(2, PWD);
		pstmt.setString(3, name);
		pstmt.setString(4, email);
		pstmt.setString(5, Now.toString());
		pstmt.setInt(6, 0);
		value = pstmt.executeUpdate();
			   
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return value;
	}
}
