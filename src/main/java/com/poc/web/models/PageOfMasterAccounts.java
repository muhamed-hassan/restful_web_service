package com.poc.web.models;

import java.util.ArrayList;

public class PageOfMasterAccounts {
	
	private ArrayList<BriefUserInfoReadModel> data;
	
	private int totalElements;
	
	private int totalPages;
	
	private boolean firstPage;
	
	private boolean lastPage;

	public ArrayList<BriefUserInfoReadModel> getData() {
		return data;
	}

	public void setData(ArrayList<BriefUserInfoReadModel> data) {
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
