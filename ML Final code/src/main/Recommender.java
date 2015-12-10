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
			
			tVal = Driver._X.getMatrix(u, u, 0, Driver._f-1).transpose().times(
					Driver._Y.getMatrix(i, i, 0, Driver._f-1)).get(0, 0);
			
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
		
		int[] ans = new int[num_suggestions];
		int item_idx = 0;
		for (; item_idx < items.size(); item_idx++) {
			ans[item_idx] = items.get(item_idx);
		}
		for (; item_idx < num_suggestions; item_idx++) {
			ans[item_idx] = -1;
		}
		
		return ans;
	}
}
