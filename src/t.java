import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class t {

	public static void main(String[] args) throws IOException, InterruptedException {
		Process p1 = java.lang.Runtime.getRuntime().exec("ping  www.baidu.com");
		int returnVal = p1.waitFor();
		boolean reachable = (returnVal==0);
		System.out.println(reachable);
	}
	public static void maina(String[] args) throws IOException {
		String path="d:\\abc.csv";
		 File file = new File(path);
         InputStream in= new java.io.FileInputStream(file);
         byte[] b = new byte[3];
         in.read(b);
         in.close();
         String code="gbk";
         if (b[0] == -17 && b[1] == -69 && b[2] == -65){
             System.out.println(file.getName() + "：编码为UTF-8");
         	code="utf-8";
         }
         else{
             System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");
             code="gbk";
         }
         
         
         BufferedReader ir =new BufferedReader((new InputStreamReader(new FileInputStream(path),code)));
		
         String data="";
         int i=0;
         try {
			while((data=ir.readLine())!=null){
				System.out.println(data);
				i++;
				if(!data.contains(",")){
					continue;
				}
				if(i==8520){
					System.out.println(data);
				}
				 data=data.replaceAll(",", " , ");
				 String[] r=data.split(",");
					//String mobile =r[2];
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(data);
			System.out.println(i);
			//continue;
		}
			
	}
	/*static boolean contains(String s){
		char[] ch=s.toCharArray();
		
		int i=0;
		for(char c:ch){
			if(c==','){
				i++;
			}
		}
		
		return ss.length==14;
	}*/
}
