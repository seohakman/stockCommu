package stockCommu.domain;

public class MemberVO {
	
	private int midx;			// 회원 인덱스
	private String id;
	private String pwd;			// 비밀번호
	private String name;
	private String email;
	private String memberday;	// 가입일
	private int point;			// 회원 점수
	private String delyn;		// 삭제여부
	private String supermember; // 관리자 회원
	
	
	public String getSupermember() {
		return supermember;
	}
	public void setSupermember(String supermember) {
		this.supermember = supermember;
	}
	public int getMidx() {
		return midx;
	}
	public void setMidx(int midx) {
		this.midx = midx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMemberday() {
		return memberday;
	}
	public void setMemberday(String memberday) {
		this.memberday = memberday;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	
	
}
