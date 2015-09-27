package com.song.common.util;

public class ExcelHeader {
	private int width;
	private String title;
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ExcelHeader(){
		
	}
	
	public ExcelHeader(int width, String title){
		this.width = width;
		this.title = title;
	}
}
