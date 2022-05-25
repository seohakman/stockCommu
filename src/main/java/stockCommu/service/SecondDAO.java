package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.SecondReplyVO;
import stockCommu.domain.SecondVO;
import stockCommu.domain.SearchCriteria;

public class SecondDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public SecondDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int insertSecond(String subject, String content, String ID, int midx) {
		//�����Խ��ǿ� DB�� �� ����
		int value = 0;
		String sql = "insert into second(bidx,subject,content,midx,writer,viewcount,likecount) values(bidx_second.nextval,?,?,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, midx);
			pstmt.setString(4, ID);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public ArrayList<SecondVO> secondSelectAll(SearchCriteria scri){
		// second ���̺��� ��� ������ �����´�.
		ArrayList<SecondVO> alist = new ArrayList();
		SecondVO sv = null;
		ResultSet rs = null;
		
		String str = "";
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("writer")){
			str = "and writer like '%"+scri.getKeyword()+"%'";
		}
		
		String sql = "SELECT * FROM("
					+"SELECT tb.*, ROWNUM rNum FROM("
					+	"SELECT * FROM second ORDER BY bidx DESC"
					+	") tb"
					+")WHERE rNum BETWEEN ? AND ? and delyn = 'N'" + str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*10+1);
			pstmt.setInt(2, (scri.getPage()*10));
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				sv = new SecondVO();
				sv.setBidx(rs.getInt("bidx"));
				sv.setSubject(rs.getString("subject"));
				sv.setContent(rs.getString("content"));
				sv.setWriteday(rs.getString("writeday"));
				sv.setFilename(rs.getString("filename"));
				sv.setViewCount(rs.getInt("viewcount"));
				sv.setLikeCount(rs.getInt("likecount"));
				sv.setMidx(rs.getInt("midx"));
				sv.setWriter(rs.getString("writer"));
				
				alist.add(sv);
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
	
	public SecondVO secondSelectOne(int bidx) {
		// second ���̺��� bidx�� �ش��ϴ� �Խñ� �����͸� �����´�.
		SecondVO sv = null;
		ResultSet rs = null;
		
		String sql = "select * from second where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				sv = new SecondVO();
				sv.setBidx(rs.getInt("bidx"));
				sv.setSubject(rs.getString("subject"));
				sv.setContent(rs.getString("content"));
				sv.setWriteday(rs.getString("writeday"));
				sv.setFilename(rs.getString("filename"));
				sv.setViewCount(rs.getInt("viewcount"));
				sv.setLikeCount(rs.getInt("likecount"));
				sv.setMidx(rs.getInt("midx"));
				sv.setWriter(rs.getString("writer"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sv;
	}
	
	public int secondLikeCount(int count, int bidx) {
		//���ƿ� �� �������� �޼��� 
		int value = 0;
		String sql = "update second set likecount= likecount + ? where bidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, count);
			pstmt.setInt(2, bidx);
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
	
	public int secondModify(int bidx, String subject, String content) {
		//�ۼ��� ���� �޼���
		int value = 0;
		String sql = "update second set subject= ?, content= ? where bidx= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, bidx);
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
	
	public int insertReply(String content, String writer, int bidx) {
		//��� �ۼ� �޼���
		int value = 0;
		String sql = "insert into secondreply(ridx,content,bidx,writer) values(ridx_second.nextval,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, bidx);
			pstmt.setString(3, writer);
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
	
	public ArrayList<SecondReplyVO> replySecond(int bidx){
		//bidx�� �ش��ϴ� ����� ��� ��������
		ArrayList<SecondReplyVO> slist = new ArrayList();
		ResultSet rs = null;
		SecondReplyVO srv = null;
		
		String sql = "select * from secondreply where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				srv = new SecondReplyVO();
				srv.setBidx(rs.getInt("bidx"));
				srv.setContent(rs.getString("content"));
				srv.setWriter(rs.getString("writer"));
				srv.setWriteday(rs.getString("writeday"));
				
				slist.add(srv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return slist;
	}
	
	public int secondDelete(int bidx) {
		//bidx�� �ش��ϴ� �� ����
		//�����δ� �� ���� �ٲ㼭 �������� ǥ�� �ȵǰ��ϴ� ��
		int value = 0;
		String sql = "update second set delyn = 'Y' where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int viewCountUp(int bidx) {
		// ��ȸ�� ���� �޼���
		int value = 0;
		String sql = "update second set viewcount = viewcount + 1 where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public int selectCount(SearchCriteria scri) {
		//Ư�� ���ǿ� �´� �Խñ��� �Ѱ����� ���Ѵ�.
		String str = ""; // scri�� ����ִ� searchType�� keyword�� ���� ���� �ٸ� ���� �ش�.
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("writer")){
			str = "and writer like '%"+scri.getKeyword()+"%'";
		}
		String sql= "select count(*) cnt from second where delyn = 'N'" + str;
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
}
