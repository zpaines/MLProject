package utilities;

import java.util.ArrayList;

public class ListSimilarity {

	public static double basicDifference(ArrayList<? extends Object> a, ArrayList<? extends Object> b) {
		double dif = 0.0;
		
		for (int i=0 ;i<a.size(); i++) {
			int j = b.indexOf(a.get(i));
			
			dif += (Math.abs(j-i))*(1.0/(2.0+i)+1.0/(2.0+j));
		}
		
		return dif;
	}
}
