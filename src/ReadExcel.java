import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.read.biff.BiffException;


public class ReadExcel {

	public static void main(String[] args) throws BiffException, IOException {
		List<Integer> l=new ArrayList<Integer>();
		
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(6);
		l.add(7);
		l.add(8);
		l.add(9);
		l.add(0);
		
		
		for(int i=0;i<5;i++){
			List<Integer> sub=l.subList(i*2, (i+1)*2);
			
			System.out.println(sub);
		}
	}
}
