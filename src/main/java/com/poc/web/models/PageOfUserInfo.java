package com.poc.web.models;

import java.util.HashSet;

public class PageOfUserInfo {
	
	private HashSet<BriefUserInfoReadModel> data;
	
	private int totalElements;
	
	private int totalPages;
	
	private boolean firstPage;
	
	private boolean lastPage;

	public HashSet<BriefUserInfoReadModel> getData() {
		return data;
	}

	public void setData(HashSet<BriefUserInfoReadModel> data) {
		this.data = data;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
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
