package com.poc.web.models;

import java.util.HashSet;

public class PageOfBankAccounts {
	
	private HashSet<BriefUserInfoReadModel> data;
	
	private boolean firstPage;
	
	private boolean lastPage;

	public HashSet<BriefUserInfoReadModel> getData() {
		return data;
	}

	public void setData(HashSet<BriefUserInfoReadModel> data) {
		this.data = data;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

}
