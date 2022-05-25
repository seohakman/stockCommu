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
		//자유게시판에 DB에 글 삽입
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
		// second 테이블의 모든 내용을 가져온다.
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
		// second 테이블에서 bidx에 해당하는 게시글 데이터를 가져온다.
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
		//좋아요 수 증가감소 메서드 
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
		//작성글 수정 메서드
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
		//댓글 작성 메서드
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
		//bidx에 해당하는 댓글을 모두 가져오자
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
		//bidx에 해당하는 글 삭제
		//실제로는 열 값만 바꿔서 페이지에 표시 안되게하는 것
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
		// 조회수 증가 메서드
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
		//특정 조건에 맞는 게시글의 총개수를 구한다.
		String str = ""; // scri에 들어있는 searchType과 keyword의 값에 따라 다른 값을 준다.
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
