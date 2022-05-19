package stockCommu.dbconn;

import java.sql.*;

public class Dbconn {
	//접속정보
	// 127.0.0.1 - 본인 컴퓨터 , 1521 포트번호
	String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String user = "system";
	String password = "1234";
	
	public Connection getConnection() {
		Connection conn = null;
		try {
		//해당 패키지에 있는 클래스를 가져온다.
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// 접속정보를 활용해서 연결객체를 만든다
		conn = DriverManager.getConnection(url, user, password);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}

