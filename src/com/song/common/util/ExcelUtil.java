package com.song.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtil {

	private static Workbook wb;
	
	private static Font titleFont;
	
	private static CellStyle titleStyle;
	
	private static CellStyle contentStyle;
	
	private static void checkDir(String filePath){
		int index=filePath.lastIndexOf("/");
		if(index==-1){
			return;
		}
		String dirPath=filePath.substring(0, index);
		File f=new File(dirPath);
		
		if(!f.exists()){
			f.mkdirs();
		}
		
	}
	public static File writerFile(String[] title,List<String[]> content,String filePath) throws IOException{
		checkDir(filePath);
		
		File f=new File(filePath);
		
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream out=new FileOutputStream(f);
		
	    wb=new SXSSFWorkbook();
		setStyle(wb);
		Sheet sheet=wb.createSheet("sheet1");
		
		Row titleRow=sheet.createRow(0);
		
		titleRow.setHeightInPoints(20);
		
		int tCount=title.length;
		for(int i=0;i<tCount;i++){
			Cell cell=titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(title[i]);
			
			sheet.setColumnWidth(i, 5000);
		}
		int rnum=1;
		for(String[] c:content){
			Row r=sheet.createRow(rnum);
			for(int i=0;i<c.length;i++){
				Cell cell=r.createCell(i);
				cell.setCellStyle(contentStyle);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				sheet.setColumnWidth(i, 5000);
				
				String v=c[i];
				if(v==null){
					v="";
				}
				
				cell.setCellValue(v);
			}
			
			rnum++;
		}
		
		wb.write(out);
		out.flush();
		wb.close();
		out.close();
		
		return f;
	}
	private static void setStyle(Workbook wb){
		titleFont=wb.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		titleStyle=wb.createCellStyle();
		titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
		titleStyle.setBorderRight(CellStyle.BORDER_THIN);
		titleStyle.setBorderTop(CellStyle.BORDER_THIN);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		titleStyle.setFont(titleFont);
		
		contentStyle=wb.createCellStyle();
		contentStyle.setBorderBottom(CellStyle.BORDER_THIN);
		contentStyle.setBorderLeft(CellStyle.BORDER_THIN);
		contentStyle.setBorderRight(CellStyle.BORDER_THIN);
		contentStyle.setBorderTop(CellStyle.BORDER_THIN);
		contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
		contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
	}
}
