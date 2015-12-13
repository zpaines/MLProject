package main;

import java.util.ArrayList;

import utilities.Pair;


public class Recommender {

	public static ArrayList<Pair<String, Double>> recommend(String userID, int num_suggestions) {
		int u = Driver._UserIDs.indexOf(userID);
		
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
		
		ArrayList<Pair<String, Double>> ans = new ArrayList<Pair<String, Double>>();
		int item_idx = 0;
		for (; item_idx < items.size(); item_idx++) {
			ans.add(new Pair<String, Double>(Driver._ItemIDs.get(items.get(item_idx)), vals.get(item_idx)));
		}
		for (; item_idx < num_suggestions; item_idx++) {
			ans.add(new Pair<String, Double>("n/a", -1.0/0.0));
		}
		
		return ans;
	}
}
