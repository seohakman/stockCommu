package dbconn;

import java.sql.*;

public class Dbconn {
		//��������		
		// 127.0.0.1 - ���� ��ǻ�� , 1521 ��Ʈ��ȣ
		String url = "jdbc:mysql://localhost:3306/mydb";
		String user = "root";
		String password = "1234";
	
	public Connection getConnection() {
		Connection conn = null;
		try {
		//�ش� ��Ű���� �ִ� Ŭ������ �����´�.
		Class.forName("com.mysql.cj.jdbc.Driver");
		// ���������� Ȱ���ؼ� ���ᰴü�� �����
		conn = DriverManager.getConnection(url, user, password);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
