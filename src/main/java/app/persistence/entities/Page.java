package app.persistence.entities;

import java.util.List;

public class Page {
	
	private List<Object[]> data;
	
	private boolean firstPage;
	
	private boolean lastPage;

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
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
