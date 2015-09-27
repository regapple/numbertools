package com.song.common.dao;

import java.util.List;

public class Page {
	// 记录总条数
	private int totalCount;
	// 记录总页数
	private int totalPage;
	// 当前页数
	private int pageNo;
	// 每页记录条数
	private int pageSize;
	// 记录集合
	List<?> result;
	
	public String toString() {
		return "totalCount=" + totalCount + "  totalPage=" + totalPage + "  pageNo=" + pageNo + "  pageSize=" + pageSize;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	
}
