package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.MainReplyVO;
import stockCommu.domain.MainVO;
import stockCommu.domain.SearchCriteria;

public class MainDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public MainDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int insertMain(String subject, String content, String ID, int midx, String fileName) {
		int value = 0;
		String sql = "insert into main(bidx,subject,content,midx,writer,viewcount,likecount,filename) values(bidx_main.nextval,?,?,?,?,?,?,?)";
		
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
	
	public ArrayList<MainVO> mainSelectAll(SearchCriteria scri){
		// main 테이블의 모든 내용을 가져온다.
		ArrayList<MainVO> alist = new ArrayList();
		MainVO mv = null;
		ResultSet rs = null;
		
//		String sql = "select * from main where delyn = 'N'";
		
		String str = "";
		if(scri.getSearchType().equals("subject")) {
			str = "and subject like '%"+scri.getKeyword()+"%'";
		}else if(scri.getSearchType().equals("writer")){
			str = "and writer like '%"+scri.getKeyword()+"%'";
		}
		
		String sql = "SELECT * FROM("
					+"SELECT tb.*, ROWNUM rNum FROM("
					+	"SELECT * FROM main ORDER BY bidx DESC"
					+	") tb"
					+")WHERE rNum BETWEEN ? AND ? and delyn = 'N'" + str;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*10+1);
			pstmt.setInt(2, (scri.getPage()*10));
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				mv = new MainVO();
				mv.setBidx(rs.getInt("bidx"));
				mv.setSubject(rs.getString("subject"));
				mv.setContent(rs.getString("content"));
				mv.setWriteday(rs.getString("writeday"));
				mv.setFilename(rs.getString("filename"));
				mv.setViewCount(rs.getInt("viewcount"));
				mv.setLikeCount(rs.getInt("likecount"));
				mv.setMidx(rs.getInt("midx"));
				mv.setWriter(rs.getString("writer"));
				
				alist.add(mv);
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
	
	public MainVO mainSelectOne(int bidx) {
		// main 테이블에서 bidx에 해당하는 게시글 데이터를 가져온다.
		MainVO mv = null;
		ResultSet rs = null;
		
		String sql = "select * from main where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				mv = new MainVO();
				mv.setBidx(rs.getInt("bidx"));
				mv.setSubject(rs.getString("subject"));
				mv.setContent(rs.getString("content"));
				mv.setWriteday(rs.getString("writeday"));
				mv.setFilename(rs.getString("filename"));
				mv.setViewCount(rs.getInt("viewcount"));
				mv.setLikeCount(rs.getInt("likecount"));
				mv.setMidx(rs.getInt("midx"));
				mv.setWriter(rs.getString("writer"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	public int mainLikeCount(int count, int bidx) {
		int value = 0;
		String sql = "update main set likecount= likecount + ? where bidx = ?";
		
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
	
	public int mainModify(int bidx, String subject, String content, String fileName) {
		int value = 0;
		String sql = "update main set subject= ?, content= ?, filename=? where bidx= ?";
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
		String sql = "insert into mainreply(ridx,content,bidx,writer) values(ridx_main.nextval,?,?,?)";
		
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
	
	public ArrayList<MainReplyVO> replyMain(int bidx){
		//bidx에 해당하는 댓글을 모두 가져오자
		ArrayList<MainReplyVO> mlist = new ArrayList();
		ResultSet rs = null;
		MainReplyVO mrv = null;
		
		String sql = "select * from mainreply where bidx = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				mrv = new MainReplyVO();
				mrv.setBidx(rs.getInt("bidx"));
				mrv.setContent(rs.getString("content"));
				mrv.setWriter(rs.getString("writer"));
				mrv.setWriteday(rs.getString("writeday"));
				
				mlist.add(mrv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mlist;
	}
	
	public int mainDelete(int bidx) {
		int value = 0;
		String sql = "update main set delyn = 'Y' where bidx = ?";
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
		String sql = "update main set viewcount = viewcount + 1 where bidx = ?";
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
		String sql= "select count(*) cnt from main where delyn = 'N'" + str;
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
