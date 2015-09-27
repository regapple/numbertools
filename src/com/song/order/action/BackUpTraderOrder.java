package com.song.order.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.song.common.dao.Bdao;
import com.song.common.util.DateUtils;
import com.song.common.util.ExcelUtil;
import com.song.common.util.PropertiesConfigFileUtil;
import com.song.order.bean.TraderOrder;

public class BackUpTraderOrder {

	/**
	 * 备份数据
	 * @param args
	 */
	public static void backUp(String[] args){
		ApplicationContext o = new ClassPathXmlApplicationContext(
				"ApplicationContext.xml");
		Bdao bdao =o.getBean(Bdao.class);
		
		String sql="select *  from trader_order";
		List<TraderOrder> ret=bdao.query(sql, null, TraderOrder.class);
		String[] titleStrs = { "掌柜姓名", "淘宝id", "手机", "电话", "店铺名称", "店铺链接","类目","店铺等级" ,"地址","Email","支付宝","QQ","B店C店"};
		
		List<String[]> contentList = new ArrayList<String[]>();
		
		for(TraderOrder t:ret){
			String[] s=new String[]{t.getName(),t.getTid(),t.getMobile(),t.getPhone(),t.getShopName(),t.getShopLink(),t.getCategory(),t.getShopRank(),t.getAddress(),t.getEmail(),t.getEpayId(),t.getQq(),t.getShopType()};
			
			contentList.add(s);
		}
		ret=null;
		String filePath= PropertiesConfigFileUtil.readValue("savePath");
		
		String fileName=DateUtils.getCurrentDate()+".xlsx";
		
		
		try {
			File f=new File(filePath+"backup/"+DateUtils.getCurrentDate().replace("-", "")+"/"+fileName);
			
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			File file=ExcelUtil.writerFile(titleStrs, contentList, f.getAbsolutePath());
			
			a(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	private static  void a(File f){
		if(b()&&(DateUtils.getCurrentDate().equals(DateUtils.getLastDayOfMonth()))){
			try {
				File zipFile=new File(f.getParentFile().getAbsolutePath()+File.separatorChar+DateUtils.getCurrentDate().replace("-", "")+".zip");
				
				ZipOutputStream zipout=new ZipOutputStream(new FileOutputStream(zipFile));
				
				FileInputStream in=new FileInputStream(f);
				
				zipout.putNextEntry(new ZipEntry(f.getName()));
				
				byte[] b=new byte[2048];
				
				int size=0;
				
				while((size=in.read(b))>0){
					zipout.write(b, 0, size);
				}
				
				zipout.closeEntry();
				in.close();
				zipout.close();
				
				d(zipFile);
				
				
				zipFile.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	private static  boolean b(){
		boolean ret=false;
		Process p1;
		try {
			p1 = java.lang.Runtime.getRuntime().exec("ping  www.baidu.com");
			int returnVal = p1.waitFor();
			ret = (returnVal==0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return ret;
	}
	
	private static void d(File f){
		if(DateUtils.dayOfWeek(new Date())==2){
			try {
				String send="mailabctest@qq.com";
				String pw="abc123456";
				String host="smtp.qq.com";
				String r="sina3258@126.com";
				MimeMessage message;
				Session session;
				Transport transport;
				
				Properties p=new Properties();
				
				p.put("mail.smtp.host", host);
				p.put("mail.smtp.auth", true);
				p.put("mail.sender.username", send);
				p.put("mail.sender.password", pw);
				
				session = Session.getInstance(p);
				session.setDebug(false);//开启后有调试信息
		        message = new MimeMessage(session);
		        
		        
				InternetAddress from = new InternetAddress(send);
				message.setFrom(from);
				
				InternetAddress to = new InternetAddress(r);
	            message.setRecipient(Message.RecipientType.TO, to);
	            
	            // 主题
	            message.setSubject(DateUtils.getCurrentDate());
	            
	            Multipart multipart = new MimeMultipart();
	            BodyPart contentPart = new MimeBodyPart();
	            contentPart.setContent(DateUtils.getCurrentDate(), "text/html;charset=UTF-8");
	            multipart.addBodyPart(contentPart);
				
	            BodyPart attachmentBodyPart = new MimeBodyPart();
	            DataSource source = new FileDataSource(f);
	            attachmentBodyPart.setDataHandler(new DataHandler(source));
	            
	            attachmentBodyPart.setFileName(MimeUtility.encodeWord(f.getName()));
	            multipart.addBodyPart(attachmentBodyPart);
	            
	            message.setContent(multipart);
	            // 保存邮件
	            message.saveChanges();

	            transport = session.getTransport("smtp");
	            // smtp验证，就是你用来发邮件的邮箱用户名密码
	            transport.connect(host, send, pw);
	            // 发送
	            transport.sendMessage(message, message.getAllRecipients());

			} catch (AddressException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
	}
}
