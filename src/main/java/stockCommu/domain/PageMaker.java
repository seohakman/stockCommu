package stockCommu.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import stockCommu.domain.SearchCriteria;

public class PageMaker {
	
	private int totalCount; // 조건에 맞는 게시글 총 숫자
	private int startPage;	// 페이지에 보여지는 첫 번째 페이지 숫자
	private int endPage;   	// 페이지에 보여지는 마지막 페이지 숫자
	private boolean prev;
	private boolean next;
	private int displayPageNum = 5; // 화면에 보여질 페이지 숫자 갯수
	private SearchCriteria scri;
	
	public SearchCriteria getScri() {
		return scri;
	}
	public void setScri(SearchCriteria scri) {
		this.scri = scri;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public int getDisplayPageNum() {
		return displayPageNum;
	}
	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}
	
	public void calcData() {
		endPage = (int)(Math.ceil(scri.getPage()/(double)displayPageNum)*displayPageNum);
		
		startPage = (endPage - displayPageNum)+1;
		//tempEndPage 총 페이지 숫자
		int tempEndPage = (int)(Math.ceil(totalCount/(double)scri.getPerPageNum()));
		
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		prev = startPage == 1? false:true;
		next = endPage* scri.getPerPageNum() >= totalCount? false:true;
	}
	
	public String encoding(String keyword) {
		String str = "";
	
		try {
			if(keyword != null) {
			str = URLEncoder.encode(keyword,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
