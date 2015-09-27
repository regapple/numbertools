package com.song.order.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import com.song.common.dao.Bdao;
import com.song.common.util.DateUtils;
import com.song.order.bean.TraderOrder;

public class ImportHistoryDataExcel {

	static Bdao bdao=null;
	static java.util.concurrent.atomic.AtomicInteger count=new AtomicInteger(0);
	public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
		String path = "c:\\data\\二组离职人员导出线索名单.xls";
		
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"ApplicationContext.xml");
		
		bdao = ac.getBean(Bdao.class);
		File f=new File(path);
		List<File> list=new ArrayList<>();
		list.add(f);
		//List<File> list=listFile(f);
		//readFile(list);
		
		for(File ff:list){
			/*
			if(count.get()==5){
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				final File file=ff;
				Thread tt = new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							readFile(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();e
							System.out.println(file.getName()+"---error---"+e.getMessage());
						}
					}
				}, "add_keyword_queue_");
				tt.start();
				
				up();
			}*/
			readFile(ff);
		}
	}
	static  void up(){
		count.incrementAndGet();
	}
	static  void down(){
		count.decrementAndGet();
	}
	public static void readFile(File f) throws IOException {
		String retMsg = "";
		List<TraderOrder> result = new ArrayList<TraderOrder>();
		int duplicate = 0;
		String sql = "select * from trader_order where mobile=?";
		
		try {
			
			//for(File f:files){
				
				System.out.println(f.getName());
				
				
				Workbook workbook=WorkbookFactory.create(f);
				Sheet sheet=workbook.getSheetAt(0);
				int startRow=sheet.getFirstRowNum();
				int endRow=sheet.getLastRowNum();
				Row row=sheet.getRow(startRow);
				
				int[] index=new int[6];
				Cell cell=row.getCell(row.getFirstCellNum()+1);
				String value=cell.getStringCellValue();
				if(value.contains("淘宝")){
					index=new int[]{0,14,1,6,7,4,9};
				}else{
					index=new int[]{0,14,1,6,7,4,9};
				}
				
				for(int rowNum=startRow+1;rowNum<endRow;rowNum++){
					Row r=sheet.getRow(rowNum);
					
					if(r==null){
						continue;
					}
					
					String name=r.getCell(index[0]).getStringCellValue().replace("'", "\\'");
					String tid=r.getCell(index[1]).getStringCellValue();
					String mobile="";
					Cell c=r.getCell(index[2]);
					if(c==null){
						continue;
					}
					int type=c.getCellType();
					if(type==Cell.CELL_TYPE_NUMERIC){
						double d=c.getNumericCellValue();
						DecimalFormat df=new DecimalFormat();
						
						mobile=df.format(d).replace(",", "");
					}else{
						mobile=r.getCell(index[2]).getStringCellValue();
					}
					String phone="";
					int type1=r.getCell(index[3]).getCellType();
					if(type1==Cell.CELL_TYPE_NUMERIC){
						double d=r.getCell(index[3]).getNumericCellValue();
						DecimalFormat df=new DecimalFormat();
						phone=df.format(d).replace(",", "");
					}else{
						phone=r.getCell(index[3]).getStringCellValue();
					}
					String shopName=r.getCell(index[4]).getStringCellValue().replace("'", "\'");
					String shopLink=r.getCell(index[5]).getStringCellValue();
					//String owner=r.getCell(index[6]).getStringCellValue();
					if ( !StringUtils.isEmpty(mobile)) {
					//	List<TraderOrder> l = bdao.query(sql,new Object[] { mobile }, TraderOrder.class);
						if(true){
							TraderOrder t = new TraderOrder();
							t.setName(name);
							t.setTid(tid);
							t.setMobile(mobile);
							t.setPhone(phone);
							t.setShopName(shopName);
							t.setShopLink(shopLink);
							t.setOwner("");
							t.setShopType("");
							
							result.add(t);
						}else{
							duplicate++;
						}
					}else{
						//System.out.println(rowNum);
					}
				}
				retMsg = "上传数据成功，共上传【" + (result.size() + duplicate) + "】条记录，其中重复记录【"
						+ duplicate + "】条，已过滤！";
				save(result);
				
				System.out.println(retMsg);
			//}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retMsg = "上传数据失败";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		down();
	}
	static void save(List<TraderOrder> result){
		String baseSql = "insert into trader_order(name, tid, mobile, phone, shop_name, shop_link,import_date,owner)values(";
		
		List<String> sqls= new ArrayList<>();
		
		for (TraderOrder t : result) {
			StringBuffer s=new StringBuffer(baseSql);
			
			s.append("'").append(t.getName()).append("','").append(t.getTid()).append("','").append(t.getMobile()).append("','");
			s.append(t.getPhone()).append("','").append(t.getShopName()).append("','").append(t.getShopLink()).append("','");
			s.append(DateUtils.getCurrentDate()).append("','").append(t.getOwner()).append("')");
		
			sqls.add(s.toString());
			if(sqls.size()==100){
				String[] ss=new String[sqls.size()];
				sqls.toArray(ss);
				bdao.getMysqlJdbcTemplate().batchUpdate(ss);
				sqls.clear();
			}
		}
		String[] s=new String[sqls.size()];
		sqls.toArray(s);
		if(sqls.size()>0){
			bdao.getMysqlJdbcTemplate().batchUpdate(s);
			
		}
		
	}
	static List<File> listFile(File f){
		List<File> listFile=new ArrayList<>();
		
		File[] fs=f.listFiles();
		for(File ff:fs){
			if(ff.isFile()){
				listFile.add(ff);
			}else if(ff.isDirectory()){
				listFile.addAll(listFile(ff));
			}
		}
		
		return listFile;
	}
}
