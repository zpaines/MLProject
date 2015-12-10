package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import utilities.Pair;
import Jama.Matrix;

public final class Driver {
	public static final String DATA_FILE = //"C://Users/Derek/My Documents/Million Song Data/train_triplets.txt";
			"data/trivial1.txt";
	
	/** Learning Params */
	public static final int _f = 3;
	public static final double _alpha = 1/20;
	public static final int _iterations = 100;
	
	/** Main Variables */
	protected static int _M; //#users
	protected static int _N; //#item
	protected static ArrayList<String> _UserIDs;
	protected static ArrayList<String> _ItemIDs;
	protected static ArrayList<HashMap<Integer, Double>> _r;
	protected static ArrayList<HashMap<Integer, Double>> _rt;
	protected static Matrix _X;
	protected static Matrix _Y;
	
	public static void main(String[] args) {
		
		DataImporter.importData(DATA_FILE);
		
		printData();
		
		//TODO: @Zach fill this out to match the signature you want
		//Trainer.trainModel()
		
		CostMinimizer.minCost();
		
		//TODO: recommend
		ArrayList<Pair<Integer, Double>> t = Recommender.recommend(0,2);
		for (int i=0; i<t.size();i++) {
			System.out.println("item ["+t.get(i).first + "] with value [" + t.get(i).second + "]");
		}
		
	}
	
	private static void printData() {
		for (int u=0; u<_M; u++) {
			System.out.print("User ["+_UserIDs.get(u)+"]: ");
			for (Entry<Integer, Double> entry : _r.get(u).entrySet()) {
				System.out.print(""+entry.getKey() + ":"+entry.getValue() + " ");
			}
			System.out.println();
		}
	}
}
