package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import stockCommu.dbconn.Dbconn;

public class MainDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public MainDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int insertMain(String subject, String content, String ID) {
		int value = 0;
		String sql = "";
		
		
		
		
		
		
		
		return value;
	}
}
