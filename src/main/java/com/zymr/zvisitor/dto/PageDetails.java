package com.zymr.zvisitor.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDetails implements Serializable {
	private static final long serialVersionUID = 3127544743696180985L;

	@JsonProperty("current_page")
	private long currentPage;
	
	@JsonProperty("total_page")
	private long totalPage;
	
	@JsonProperty("total_count")
	private long totalCount;
	
	@JsonProperty("current_count")
	private long currentCount;
	
	@JsonIgnore
	private Object pageData;
	
	public long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}
	
	public long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public long getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(long currentCount) {
		this.currentCount = currentCount;
	}
	
	public Object getPageData() {
		return pageData;
	}
	public void setPageData(Object pageData) {
		this.pageData = pageData;
	}
	
	@Override
	public String toString() {
		return "PageDetails [currentPage=" + currentPage + ", totalPage=" + totalPage + ", totalCount=" + totalCount
				+ ", currentCount=" + currentCount + ", pageData=" + pageData + "]";
	}
}
