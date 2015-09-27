package com.song.order.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import com.song.common.dao.Bdao;
import com.song.common.util.DateUtils;
import com.song.order.bean.TraderOrder;

public class ImportHistoryData {

	public static void main(String[] args) {
		
		String path="C:\\Users\\yinhua\\Documents\\Navicat\\";
		
		File f=new File(path);
		
		//File[] files=f.listFiles();
		
		List<File> listFile=listFile(f);
		
		System.out.println(listFile.size());
	}
	
	static List<File> listFile(File f){
		List<File> list=new ArrayList<>();
		
		File[] fs=f.listFiles();
		
		for(File ff:fs){
			if(ff.isFile()){
				list.add(ff);
			}else{
				list.addAll(listFile(ff));
			}
		}
		
		return list;
	}
	
	static void convertFile(List<File> fileList){
		ApplicationContext ac=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		Bdao bdao = ac.getBean(Bdao.class);
		
		String retMsg="";
		List<TraderOrder> result=new ArrayList<TraderOrder>();
		int duplicate=0;
		try {
			for(File f:fileList){
				InputStream input=new FileInputStream(f);
				byte[] b=new byte[3];
				input.read(b);
				input.close();
				String code="gbk";
				 if (b[0] == -17 && b[1] == -69 && b[2] == -65){
			            // System.out.println(file.getName() + "：编码为UTF-8");
			         	code="utf-8";
			         }
			         else{
			            // System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
			             code="gbk";
			         }
				
				 
				 BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				 
				 
				 
			}
			
			Reader in=new FileReader("");
			Iterable<CSVRecord> it=CSVFormat.EXCEL.parse(in);
			String sql="select * from trader_order where mobile=?";
			//14
			for(CSVRecord r:it){
				//CSVRecord r=it.iterator().next();
				if(r.getRecordNumber()==1){
					continue;
				}
				String mobile =r.get(2);
				String tid=r.get(13);
				if(!StringUtils.isEmpty(tid)&&!StringUtils.isEmpty(mobile)){
					List<TraderOrder> l=bdao.query(sql, new Object[]{mobile}, TraderOrder.class);
					if(CollectionUtils.isEmpty(l)){
						TraderOrder t=new TraderOrder();
						t.setName(r.get(0));
						t.setTid(r.get(13));
						t.setMobile(r.get(2));
						t.setPhone(r.get(12));
						t.setShopName(r.get(13));
						t.setShopLink(r.get(3));
						t.setCategory("");
						t.setShopRank("");
						t.setAddress("");
						t.setEmail(r.get(9));
						t.setEpayId(r.get(10));
						t.setQq("");
						t.setSource("");
						t.setShopType(r.get(3).contains("taobao")?"C店":"B店");
						t.setSex("");
						t.setImportDate(DateUtils.getCurrentDate());
						t.setOwner(r.get(8));
						
						result.add(t);
					}else{
						duplicate++;
					}
				}
				
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retMsg="上传数据失败";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql="insert into trader_order(owner, name, tid, mobile, phone, shop_name, shop_link, category, shop_rank, address, email, epay_id, qq, source, shop_type, sex, import_date)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> toSave=new ArrayList<>();
		
		for(TraderOrder t:result){
			Object[] o=new Object[17];
			o[0]=t.getOwner();
			o[1]=t.getName();
			o[2]=t.getTid();
			o[3]=t.getMobile();
			o[4]=t.getPhone();
			o[5]=t.getShopName();
			o[6]=t.getShopLink();
			o[7]=t.getCategory();
			o[8]=t.getShopRank();
			o[9]=t.getAddress();
			o[10]=t.getEmail();
			o[11]=t.getEpayId();
			o[12]=t.getQq();
			o[13]=t.getSource();
			o[14]=t.getShopType();
			o[15]=t.getSex();
			o[16]=t.getImportDate();
			
			toSave.add(o);
			if(toSave.size()==100){
				bdao.batchUpdate(sql, toSave);
				toSave.clear();
			}
		}
		if(toSave.size()>0){
			bdao.batchUpdate(sql, toSave);
		}
		
		retMsg="上传数据成功，共上传【"+(result.size()+duplicate)+"】条记录，其中重复记录【"+duplicate+"】条，已过滤！";
		
		System.out.println(retMsg);
	}
}
