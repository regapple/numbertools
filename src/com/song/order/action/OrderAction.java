package com.song.order.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.song.common.dao.Bdao;
import com.song.common.util.DateUtils;
import com.song.common.util.ExcelUtil;
import com.song.common.util.PropertiesConfigFileUtil;
import com.song.order.bean.TraderOrder;

@Controller
public class OrderAction {

	@Autowired
	private Bdao bdao;
	
	@RequestMapping("list")
	public ModelAndView toUrl(){
		
		//类目category
		String sqlCategory="select distinct category from  trader_order where category is not null and category<>''";
		//店铺级别shopRank
		String sqlShopRank="select distinct shop_Rank from  trader_order where shop_Rank is not null and shop_rank<>''";
		
		List<Map<String,Object>> categoryData=bdao.getMysqlJdbcTemplate().queryForList(sqlCategory);
		List<Map<String,Object>> shopRankData=bdao.getMysqlJdbcTemplate().queryForList(sqlShopRank);
		
		List<String> categoryValue=new ArrayList<String>();
		List<String> shopRankValue=new ArrayList<String>();
		
		for(Map<String,Object> m :categoryData){
			String value=(String) m.get("CATEGORY");
			if(!StringUtils.isBlank(value)){
				categoryValue.add(value);
			}
		}
		
		for(Map<String,Object> m :shopRankData){
			String value=(String) m.get("SHOP_RANK");
			if(!StringUtils.isBlank(value)){
				shopRankValue.add(value);
			}
		}
		
		ModelAndView mv = new ModelAndView("listData");
		mv.addObject("category", categoryValue);
		mv.addObject("shopRank", shopRankValue);

		return mv;
	}
	@RequestMapping("upload")
	public ModelAndView toUpload(){
		ModelAndView mv = new ModelAndView("upload");
		return mv;
	}
	@RequestMapping("queryList")
	@ResponseBody
	public Map<String,Object> queryList(@RequestParam("rowPerPage")int rowPerPage,@RequestParam("pageIndex")int pageIndex,@RequestParam("tid")String tid,@RequestParam("owner")String owner){
		try {
			tid=URLDecoder.decode(tid, "utf-8");
			owner=URLDecoder.decode(owner,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="select count(*)size from trader_order where 1=1 ";
		String sql2="select * from trader_order where 1=1 ";
		
		String where="";
		if(rowPerPage==0){
			rowPerPage=30;
		}
		if(pageIndex==0){
			pageIndex=1;
		}
		List<Object> o=new ArrayList<Object>();
		if(tid!=null&&!"".equals(tid)){
			where=where+" and tid like ?";
			o.add("%"+tid+"%");
		}
		if(owner!=null&&!"".equals(owner)){
			where = where +" and owner like ?";
			o.add("%"+owner+"%");
		}
		List<Map<String,Object>> ret=bdao.getMysqlJdbcTemplate().queryForList(sql+where,o.toArray());
		int total =0;
		if(!CollectionUtils.isEmpty(ret)){
			Map<String,Object> m=ret.get(0);
			Long size=(Long) m.get("size");
			total=size.intValue();
		}
		int start=(pageIndex-1)*rowPerPage;
		String limit =" order by id desc limit "+start+","+rowPerPage;
		
		List<TraderOrder> result=bdao.query(sql2+where+limit, o.toArray(), TraderOrder.class);
		
		Map<String,Object> retMap=new HashMap<>();
		retMap.put("total", total);
		retMap.put("items", result);
		
		
		return retMap;
	}
	@RequestMapping(value="download")
	public void download(HttpServletResponse res){
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
		try {
			String fileName=DateUtils.getCurrentDate()+".xlsx";

			res.reset();
			res.setHeader("Content-Disposition", "attachment;filename="+fileName);
			res.setContentType("application/octet-stream;charset=utf-8");
			File f=ExcelUtil.writerFile(titleStrs, contentList, filePath+fileName);
			
			OutputStream out=res.getOutputStream();
			InputStream in=new FileInputStream(f);
			byte[] b=new byte[2024];
			int len=0;
			while((len=in.read(b))!=-1){
				out.write(b, 0, len);
			}
			
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*@RequestMapping(value="saveData")
	@ResponseBody
	public String uploadFile(@RequestParam("path")String path,@RequestParam("owner")String owner){
		String msg=convertFile(path, owner);
		String retMsg="{\"msg\":\""+msg+"\"}";
		
		return retMsg;
	}*/
	@RequestMapping(value="uploadFile",method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView uploadFile(@RequestParam("inputFile")MultipartFile inputFile,String owner){
		String s=saveFile(inputFile);
		ModelAndView mv=new ModelAndView();
		String retMsg=converFile(s, owner);
		mv.setViewName("result");
		mv.addObject("result", retMsg);
		
		return mv;
	}
	@RequestMapping("delete")
	@ResponseBody
	public String delete(@RequestParam("id")String id){
		
		StringBuffer ret=new StringBuffer("{\"msg\":\"");
		if(id!=null&&!"".equals(id)){
			
			String[] ids=id.split("л");
			if(ids.length>0){
				List<Object> o =new ArrayList<Object>();
				StringBuffer in=new StringBuffer("in(");
				
				for(String i:ids){
					in.append("?,");
					o.add(i);
				}
				
				if(in.length()>3){
					in.deleteCharAt(in.length()-1);
				}
				in.append(")");
				String sql="delete from trader_order where 1=1 and id "+in.toString();
				
				int i=bdao.getMysqlJdbcTemplate().update(sql,o.toArray());
				
				if(i>0){
					ret.append("删除成功");
				}else{
					ret.append("删除失败，请稍后再试");
				}
			}else{
				ret.append("没有需要删除的数据");
			}
			
		}else{
			ret.append("没有需要删除的数据");
		}
		return ret.append("\"}").toString();
	}
	//将上传的文件保存到本地，并返回文件目录地址
	private String saveFile(MultipartFile file){
		
		String savePath = PropertiesConfigFileUtil.readValue("savePath");
		String fileName=file.getOriginalFilename();
		String suffix=fileName.substring(fileName.lastIndexOf("."));
		
		String newFileName=DateUtils.getCurrentDateMillis()+(new Random().nextInt(1000))+suffix;
		
		File dir=new File(savePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String toPath=savePath+newFileName;
		try {
			file.transferTo(new File(toPath));
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toPath;
	}
	//解析上传的文件内容
	private String converFile(String toPath,String owner){
		String ret="";
		try {
			 File file = new File(toPath);
	         InputStream fileIn= new java.io.FileInputStream(file);
	         byte[] b = new byte[3];
	         fileIn.read(b);
	         fileIn.close();
	         String code="gbk";
	         if (b[0] == -17 && b[1] == -69 && b[2] == -65){
	         	code="utf-8";
	         }
	         else{
	             code="gbk";
	         }
			BufferedReader in =new BufferedReader((new InputStreamReader(new FileInputStream(toPath),code)));
			String one=in.readLine();
			//第一行题头的长度
			int oneLength=commaSize(one);
			
			String data="";
			String sql="select * from trader_order where mobile=?";
		
			int duplicate=0;
			int noMobile=0;
			List<TraderOrder> result=new ArrayList<TraderOrder>();
			while((data=in.readLine())!=null){
				
				if(!dataIntegrated(oneLength,data)){
					continue;
				}
				
				data=data.replaceAll(",", " , ");
				String[] r=data.split(",");
				String mobile =r[2];
				if(!StringUtils.isBlank(mobile)){
					
					List<TraderOrder> l=bdao.query(sql, new Object[]{mobile}, TraderOrder.class);
					if(CollectionUtils.isEmpty(l)){
						TraderOrder t=new TraderOrder();
						t.setName(r[0]);
						t.setTid(r[1]);
						t.setMobile(r[2]);
						t.setPhone(r[3]);
						t.setShopName(r[4]);
						t.setShopLink(r[5]);
						t.setCategory(r[6]);
						t.setShopRank(r[7]);
						/*t.setAddress(r[8]);
						t.setEmail(r[9]);
						t.setEpayId(r[10]);
						t.setQq(r[11]);
						t.setSource(r[12]);
						t.setShopType(r[13]);
						t.setSex("");*/
						t.setImportDate(DateUtils.getCurrentDate());
						t.setOwner(owner);
						
						result.add(t);
					}else{
						duplicate++;
					}
				}else{
					noMobile++;
				}
			}
			save(result);
			
			ret="上传数据成功，共上传【"+(result.size()+duplicate)+"】条记录，其中已过虑重复记录【"+duplicate+"】条，无效记录【"+noMobile+"】条";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret="上传数据失败";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret="上传数据失败";
		}
		return ret;
	}
	/*
	 * 逗号的个数
	 * @param d
	 * @return
	 */
	private int commaSize(String d){
		int ret=0;
		if(d.contains(",")){
			char[] ch=d.toCharArray();
			for(char c:ch){
				if(c==','){
					ret++;
				}
			}
		}
		
		return ret;
	}
	/*
	 * 判断是否数据完整
	 */
	private boolean dataIntegrated(int length,String data){
		
		return commaSize(data)==length;
	}
	//保存数据到数据库
	private void save(List<TraderOrder> result){
		String sql="insert into trader_order(owner, name, tid, mobile, phone, shop_name, shop_link, category, shop_rank, address, email, epay_id, qq, source, shop_type, sex, import_date)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> toSave=new ArrayList<>();
		
		for(TraderOrder t:result){
			Object[] o=new Object[17];
			o[0]=t.getOwner();
			o[1]=t.getName();
			o[2]=t.getTid();
			o[3]=t.getMobile();
			o[4]=t.getPhone();
			if(t.getShopName()==null||"".equals(t.getShopName())){
				o[5]=t.getTid();
			}else{
				o[5]=t.getShopName();
			}
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
		}
		bdao.batchUpdate(sql, toSave);
	}
}
