package main;

import java.util.ArrayList;


public class Recommender {

	public int[] recommend(int u, int num_suggestions) {
		ArrayList<Integer> items = new ArrayList<Integer>();
		ArrayList<Double> vals = new ArrayList<Double>();
		
		double tVal;
		for (int i=0; i<Driver._N; i++) {
			if (Driver._r.get(u).containsKey(i))
				continue;
			
			tVal = Driver._X.row(u).dot(Driver._Y.row(i));
			
			int idx = 0;
			for (; idx<items.size(); idx++) {
				if (tVal > vals.get(idx)) {
					break;
				}
			}
			
			items.add(idx, i);
			vals.add(idx, tVal);
			
			if (items.size() > num_suggestions) {
				items.remove(num_suggestions);
				vals.remove(num_suggestions);
			}
		}
		
		
	}
}
