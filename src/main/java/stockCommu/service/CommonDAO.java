package stockCommu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import stockCommu.dbconn.Dbconn;
import stockCommu.domain.ReportVO;
import stockCommu.domain.SearchCriteria;

public class CommonDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	
	public CommonDAO() {
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public int reportBoard(int bidx, int midx, String board, String reason, String content ) {
		// DB에 신고 내용을 넣는다.
		int value=0;
		String sql = "insert into reporttable(ridx,bidx,midx,board,reason,content) values(ridx_report.nextval,?,?,?,?,?) ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setInt(2, midx);
			pstmt.setString(3, board);
			pstmt.setString(4,reason);
			pstmt.setString(5, content);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	public ArrayList<ReportVO> selectAllReport(SearchCriteria scri){
		// DB에서 페이지에 맞는 신고 내용을 불러온다.
		ArrayList<ReportVO> alist = new ArrayList<ReportVO>();
		ReportVO rv = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM("
				+"SELECT tb.*, ROWNUM rNum FROM("
				+	"SELECT * FROM reporttable ORDER BY ridx DESC"
				+	") tb"
				+")WHERE rNum BETWEEN ? AND ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*10+1);
			pstmt.setInt(2, (scri.getPage()*10));
			rs= pstmt.executeQuery();
			
			while(rs.next()) {
				rv = new ReportVO();
				rv.setBidx(rs.getInt("bidx"));
				rv.setRidx(rs.getInt("ridx"));
				rv.setMidx(rs.getInt("midx"));
				rv.setBoard(rs.getString("board"));
				rv.setReason(rs.getString("reason"));
				rv.setContent(rs.getString("content"));
				
				alist.add(rv);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return alist;
	}
	
	public int selectCount() {
		// 신고 전체 건수
		String sql= "select count(*) cnt from reporttable";
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
	
	public int deleteReport(int ridx) {
		// 신고내용 삭제
		int value = 0;
		String sql = "delete from reporttable where ridx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ridx);
			value = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return value;
	}
}
