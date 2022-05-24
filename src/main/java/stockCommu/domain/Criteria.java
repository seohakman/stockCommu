package stockCommu.domain;

public class Criteria {
	
	private int page; //현재 페이지
	private int perPageNum; //화면에 게시글 출력 개수
	
	public Criteria() {
		this.page = 1;
		this.perPageNum = 10;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if(page<=1) {
			this.page = 1;
		}else {
			this.page = page;
			
		}
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public void setPerPageNum(int perPageNum) {
		if(perPageNum <=0 || perPageNum >100) {
			this.perPageNum = 10;
		}else {
			this.perPageNum = perPageNum;
			
		}
	}
	
	
}
