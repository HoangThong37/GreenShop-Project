package com.shopme.admin.category;

public class CategoryPageInfo {
	private int totalPages; //  toàn bộ page
	private long totalElements; // toàn bộ phần tử
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	
	
}
