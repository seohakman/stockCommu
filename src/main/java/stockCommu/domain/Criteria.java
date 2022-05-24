package stockCommu.domain;

public class Criteria {
	
	private int page; //���� ������
	private int perPageNum; //ȭ�鿡 �Խñ� ��� ����
	
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
