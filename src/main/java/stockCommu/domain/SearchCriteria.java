package stockCommu.domain;

import stockCommu.domain.Criteria;

public class SearchCriteria extends Criteria {
	
	private String searchType;
	private String keyword;
	
	public SearchCriteria() {
		this.searchType = "";
		this.keyword = "";
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
}
