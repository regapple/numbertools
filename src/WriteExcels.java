import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.song.common.dao.Bdao;
import com.song.common.util.ExcelUtil;
import com.song.order.bean.TraderOrder;

/**
 * 生成excel文件
 * @author javaloveiphone
 *
 */
/**
 * 记录一个异常 java.io.FileNotFoundException:
 * C:\Users\javaloveiphone\Desktop\example.xls (另一个程序正在使用此文件，进程无法访问。) at
 * java.io.FileOutputStream.open(Native Method) at
 * java.io.FileOutputStream.<init>(FileOutputStream.java:194) at
 * java.io.FileOutputStream.<init>(FileOutputStream.java:84) at
 * com.write.excel.WriteExcel.writeExcel(WriteExcel.java:45) at
 * com.write.excel.WriteExcel.main(WriteExcel.java:148)
 */
public class WriteExcels {
	// 标题字体
	//private HSSFFont titleFont = null;
	 private XSSFFont titleFont = null; //2007格式

	// 标题样式
	//private HSSFCellStyle titleStyle = null;
	 private XSSFCellStyle titleStyle = null;//2007格式

	// 行信息内容样式
	//private HSSFCellStyle contentStyle = null;

	 private XSSFCellStyle contentStyle = null;//2007格式

	 public void appendWriterExcel(List<String[]> contentList,
				String filename) throws InvalidFormatException, IOException{
		 /*File f=new File(filename);
		 
		 FileInputStream in=new FileInputStream(f);
		 //POIFSFileSystem fs=new POIFSFileSystem(in);
		 
		 FileOutputStream out=new FileOutputStream(f);
		 OPCPackage opcp=OPCPackage.open(f);
		 XSSFWorkbook workbook=new XSSFWorkbook(opcp);
		 XSSFSheet sheet=workbook.getSheetAt(0);
		 
		 int endRow=sheet.getLastRowNum();
		 
		 for(int i=0;i<contentList.size();i++){
			 XSSFRow row=sheet.createRow(endRow+i+1);
			 
			 String[] content=contentList.get(i);
			 for(int j=0;j<content.length;j++){
				 XSSFCell cell=row.createCell(j);
				 
				 cell.setCellStyle(contentStyle);
				 
				 String s=content[j];
				 
				 if(StringUtils.isEmpty(s)){
					 s="";
				 }
				 cell.setCellValue(new XSSFRichTextString(s));
			 }
		 }
		 
		 out.flush();
		 workbook.write(out);
		 workbook.close();
		 out.close();*/
	 }
	/**
	 * 写excel文件
	 * 
	 * @throws IOException
	 */
	public void writeExcel(String[] titleStrs, List<String[]> contentList,
			String filename) throws IOException {
		File f=new File(filename);
		/*if(!f.exists()){
			f.createNewFile();
		}*/
		FileOutputStream fileOut = new FileOutputStream(f);
		/*
		 * severlet响应生成excel文件 HttpServletResponse response
		 * 
		 * // 文件标题 String head = new String(filename.getBytes("GB2312"),
		 * "ISO-8859-1"); response.reset();
		 * response.setContentType("application/vnd.ms-excel");
		 * response.addHeader("Content-Disposition", "attachment; filename="+
		 * head + ".xls");
		 * 
		 * HSSFWorkbook wb = new HSSFWorkbook(); 。。。。。
		 * 
		 * java.io.OutputStream os = response.getOutputStream(); wb.write(os);
		 * os.close();
		 */

		//HSSFWorkbook wb = new HSSFWorkbook();// 创建新HSSFWorkbook对象
		// XSSFWorkbook wb = new XSSFWorkbook();//2007格式
		 SXSSFWorkbook wb=new SXSSFWorkbook();
		//setExcelStyle(wb);// 执行样式初始化
		
		//HSSFSheet sheet = wb.createSheet(filename);// 创建新的sheet对象
		// XSSFSheet sheet = wb.createSheet("sheet1");//2007格式
		 Sheet sheet=wb.createSheet("sheet1");
		//HSSFRow titleRow = sheet.createRow((short) 0);// 创建第一行
		// XSSFRow titleRow = sheet.createRow((short) 0);//2007格式
		Row titleRow= sheet.createRow(0);
		// titleRow.setHeight((short)300);//设置行高,设置太小可能被隐藏看不到
		titleRow.setHeightInPoints(20);// 20像素
		int titleCount = titleStrs.length;// 列数
		// 写标题
		for (int k = 0; k < titleCount; k++) {
			//HSSFCell cell = titleRow.createCell((short) k); // 新建一个单元格
			// XSSFCell cell = titleRow.createCell((short) k); //2007格式
			 Cell cell=titleRow.createCell(k);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符集转换
			//cell.setCellStyle(titleStyle);// 设置标题样式
			// cell.setCellValue(new HSSFRichTextString(titleStrs[k])); //
			// 为单元格赋值
			// cell.setCellValue(wb.getCreationHelper().createRichTextString(""));
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(titleStrs[k]);
			sheet.setColumnWidth((short) k, (short) 5000);// 设置列宽
		}

		int contentCount = contentList.size();// 总的记录数
		// 写内容
		for (int i = 0; i < contentCount; i++) {
			String[] contents = contentList.get(i);
			//HSSFRow row = sheet.createRow((short) (i + 1)); // 新建一行
			// XSSFRow row = sheet.createRow((i + 1)); // //2007格式
			Row row=sheet.createRow(i+1);
			for (int j = 0; j < titleCount; j++) {
				//HSSFCell cell = row.createCell((short) j); // 新建一个单元格
				// XSSFCell cell = row.createCell(j); // //2007格式
				Cell cell=row.createCell(j);
				//cell.setCellStyle(contentStyle);// 设置内容样式
				if (contents[j] == null || contents[j].equals("null")) {
					contents[j] = "";
				}
				/*// 格式化日期
				if (j == 2) {
					//HSSFCellStyle style = wb.createCellStyle();
					 XSSFCellStyle style = wb.createCellStyle();//2007格式
					style.setDataFormat(wb.getCreationHelper()
							.createDataFormat()
							.getFormat("yyyy-mm-dd hh:mm:ss"));
					// cell.setCellValue(new Date());
					// cell.setCellValue(Calendar.getInstance());
					cell.setCellValue(contents[j]);
					cell.setCellStyle(style);
				} else {*/
				//		cell.setCellValue(new HSSFRichTextString(contents[j]));
					//cell.setCellValue(new XSSFRichTextString(contents[j]));
					cell.setCellValue(contents[j]);
			//	}
			}
		}
		wb.write(fileOut);
		fileOut.flush();
		wb.close();
		fileOut.close();
	}

	public void setExcelStyle(XSSFWorkbook workBook){
		// 设置列标题字体，样式
			titleFont = workBook.createFont();
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 标题列样式
			titleStyle = workBook.createCellStyle();
			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置边框
			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleStyle.setFont(titleFont);
			// 内容列样式
			contentStyle = workBook.createCellStyle();
			contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	}
	/** 样式初始化 */
//	public void setExcelStyle(HSSFWorkbook workBook) {
//		// 设置列标题字体，样式
//		titleFont = workBook.createFont();
//		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		// 标题列样式
//		titleStyle = workBook.createCellStyle();
//		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置边框
//		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		titleStyle.setFont(titleFont);
//		// 内容列样式
//		contentStyle = workBook.createCellStyle();
//		contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//	}

	/** 测试 */
	public static void main(String[] args) {
		WriteExcels we = new WriteExcels();
		long l=System.currentTimeMillis();
		String[] titleStrs = { "掌柜姓名", "淘宝id", "手机", "电话", "店铺名称", "店铺链接" };
		ApplicationContext c=new ClassPathXmlApplicationContext("ApplicationContext.xml");
		
		Bdao dao=c.getBean(Bdao.class);
		
		List<TraderOrder> ret=dao.get("select *  from trader_order ", TraderOrder.class);
		List<String[]> contentList = new ArrayList<String[]>();
		
		for(TraderOrder t:ret){
			String[] s=new String[]{t.getName(),t.getTid(),t.getMobile(),t.getPhone(),t.getShopName(),t.getShopLink()};
			
			contentList.add(s);
		}

		int size=contentList.size();
		int singleSize=10000;
		int loopSize=Double.valueOf(Math.ceil(size*1.0/singleSize)).intValue();
		//System.out.println(size+"---"+loopSize);
		String filename = "d:\\example.xlsx";
		
		
		try {
			ExcelUtil.writerFile(titleStrs, contentList, filename);
			//we.writeExcel(titleStrs, contentList, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long l2=System.currentTimeMillis();
		
		System.out.println((l2-l)/1000.0);
	/*	for(int i=0;i<loopSize;i++){
			System.out.println(i);
			int start=i*singleSize;
			int end=(i+1)*singleSize;
			if(end>size){
				end=size;
			}
			List<String[]> subList=contentList.subList(start,end);
			try {
				//if(i==0){
					we.writeExcel(titleStrs, subList, filename);
			///	}else{
					//we.appendWriterExcel(subList, filename);
			//	}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}*/
		
		System.out.println(123);
	}
}