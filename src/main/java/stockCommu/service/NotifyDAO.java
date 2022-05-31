package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.NotifyReplyVO;
import stockCommu.domain.NotifyVO;
import stockCommu.domain.SearchCriteria;

public class NotifyDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public NotifyDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int insertNotify(String subject, String content, String ID, int midx,String fileName) {
		int value = 0;
		String sql = "insert into notify(bidx,subject,content,midx,writer,viewcount,likecount, filename) values(bidx_notify.nextval,?,?,?,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setInt(3, midx);
			pstmt.setString(4, ID);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			pstmt.setString(7, fileName);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public ArrayList<NotifyVO> notifySelectAll(SearchCriteria scri){
		// notify 테이블의 모든 내용을 가져온다.
		ArrayList<NotifyVO> alist = new ArrayList();
		NotifyVO nv = null;
		ResultSet rs = null;

		String str = "";
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("writer")){
			str = "and writer like '%"+scri.getKeyword()+"%'";
		}
		
		String sql = "SELECT * FROM("
					+"SELECT tb.*, ROWNUM rNum FROM("
					+	"SELECT * FROM notify ORDER BY bidx DESC"
					+	") tb"
					+")WHERE rNum BETWEEN ? AND ? and delyn = 'N'" + str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*10+1);
			pstmt.setInt(2, (scri.getPage()*10));
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				nv = new NotifyVO();
				nv.setBidx(rs.getInt("bidx"));
				nv.setSubject(rs.getString("subject"));
				nv.setContent(rs.getString("content"));
				nv.setWriteday(rs.getString("writeday"));
				nv.setFilename(rs.getString("filename"));
				nv.setViewCount(rs.getInt("viewcount"));
				nv.setLikeCount(rs.getInt("likecount"));
				nv.setMidx(rs.getInt("midx"));
				nv.setWriter(rs.getString("writer"));
				
				alist.add(nv);
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
	
	public NotifyVO notifySelectOne(int bidx) {
		// notify 테이블에서 bidx에 해당하는 게시글 데이터를 가져온다.
		NotifyVO nv = null;
		ResultSet rs = null;
		
		String sql = "select * from notify where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				nv = new NotifyVO();
				nv.setBidx(rs.getInt("bidx"));
				nv.setSubject(rs.getString("subject"));
				nv.setContent(rs.getString("content"));
				nv.setWriteday(rs.getString("writeday"));
				nv.setFilename(rs.getString("filename"));
				nv.setViewCount(rs.getInt("viewcount"));
				nv.setLikeCount(rs.getInt("likecount"));
				nv.setMidx(rs.getInt("midx"));
				nv.setWriter(rs.getString("writer"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nv;
	}
	
	public int notifyLikeCount(int count, int bidx) {
		int value = 0;
		String sql = "update notify set likecount= likecount + ? where bidx = ?";
		
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
	
	public int notifyModify(int bidx, String subject, String content, String fileName) {
		int value = 0;
		String sql = "update notify set subject= ?, content= ?, filename = ? where bidx= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, subject);
			pstmt.setString(2, content);
			pstmt.setString(3, fileName);
			pstmt.setInt(4, bidx);
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
		int value = 0;
		String sql = "insert into notifyreply(ridx,content,bidx,writer) values(ridx_notify.nextval,?,?,?)";
		
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
	
	public ArrayList<NotifyReplyVO> replyNotify(int bidx){
		//bidx에 해당하는 댓글을 모두 가져오자
		ArrayList<NotifyReplyVO> alist = new ArrayList();
		ResultSet rs = null;
		NotifyReplyVO nrv = null;
		
		String sql = "select * from notifyreply where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				nrv = new NotifyReplyVO();
				nrv.setBidx(rs.getInt("bidx"));
				nrv.setContent(rs.getString("content"));
				nrv.setWriter(rs.getString("writer"));
				nrv.setWriteday(rs.getString("writeday"));
				
				alist.add(nrv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return alist;
	}
	
	public int notifyDelete(int bidx) {
		int value = 0;
		String sql = "update notify set delyn = 'Y' where bidx = ?";
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
		String sql = "update notify set viewcount = viewcount + 1 where bidx = ?";
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
		String sql= "select count(*) cnt from notify where delyn = 'N'" + str;
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
	
	public int notifyingType(String type,int bidx) {
		//공지사항의 글을 다른 게시판에 올리고 내리는 글인지 선택하는 메서드
		int value = 0;
		String str = "";
		if(type.equals("up")) {
			str = "Y";
		}else if(type.equals("down")) {
			str = "N";
		}
		
		String sql = "update notify set notifying = '"+ str +"' where bidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return value;
	}
	
	
	public ArrayList<NotifyVO> notifyingAll(){
		//공지 등록할 글만 가져온다.
		ArrayList<NotifyVO> nlist = new ArrayList();
		String sql= "select * from notify where notifying = 'Y'";
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				NotifyVO nv = new NotifyVO();
				nv.setBidx(rs.getInt("bidx"));
				nv.setSubject(rs.getString("subject"));
				nv.setContent(rs.getString("content"));
				nv.setWriteday(rs.getString("writeday"));
				nv.setFilename(rs.getString("filename"));
				nv.setViewCount(rs.getInt("viewcount"));
				nv.setLikeCount(rs.getInt("likecount"));
				nv.setMidx(rs.getInt("midx"));
				nv.setWriter(rs.getString("writer"));
				
				nlist.add(nv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return nlist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
