package stockCommu.dbconn;

import java.sql.*;

public class Dbconn {
	//��������
	// 127.0.0.1 - ���� ��ǻ�� , 1521 ��Ʈ��ȣ
	
	/*
	 * String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe"; String user = "system";
	 * String password = "1234";
	 */
	 
	 String url = "jdbc:mysql://127.0.0.1:3306/mysql?serverTimezone=UTC&characterEncoding=UTF-8";
	 String user = "root"; 
	 String password = "1234";
	 
	
	public Connection getConnection() {
		Connection conn = null;
		try {
		//�ش� ��Ű���� �ִ� Ŭ������ �����´�.
		/* Class.forName("oracle.jdbc.driver.OracleDriver"); */
		Class.forName("com.mysql.cj.jdbc.Driver");
		// ���������� Ȱ���ؼ� ���ᰴü�� �����
		conn = DriverManager.getConnection(url, user, password);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}

