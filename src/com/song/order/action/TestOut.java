package com.song.order.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestOut {
	private static Connection conn = null;
    private static Statement sm = null;
    private static String insert = "INSERT INTO";//插入sql
    private static String values = "VALUES";//values关键字
    private static List<String> tableList = new ArrayList<String>();//全局存放表名列表
    private static List<String> insertList = new ArrayList<String>();//全局存放insertsql文件的数据
    private static String filePath = "E://insertSQL.txt";//绝对路径 导出数据的文件
	
    public static void main(String[] args) throws Exception {
		
		 connectSQL("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/torder?useUnicode=true&characterEncoding=UTF-8", "root", "root");
		 
		 
		 sm=conn.createStatement();
		 
		 String sql="SELECT * FROM trader_order INTO OUTFILE 'C:\\tutorials.txt' ";
	
		 sm.execute(sql);
	}
    
    public static void connectSQL(String driver, String url, String UserName, String Password) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, UserName, Password);
            sm = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
