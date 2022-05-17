package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;

import stockCommu.dbconn.Dbconn;

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
		// �ý��� �ð�
		LocalDate Now = LocalDate.now(ZoneId.of("Asia/Seoul"));
		int value=0;		
		//sql ������ ����ϰڴ�
	    String sql="insert into a_member(ID,PWD,name,email,memberday,point)"
	   	  		+ "values(?,?,?,?,?,?)";
		// Statement - ������ �����ϱ� ���� Ŭ����, ������ �����ϱ� ���� Ŭ������ �����Ѵ�.
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
