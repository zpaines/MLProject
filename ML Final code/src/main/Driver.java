package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import data_gen.EvaluateHidden;
import utilities.ListSimilarity;
import utilities.Pair;
import Jama.Matrix;

public final class Driver {
	public static final String DATA_FILE = //"C://Users/Derek/My Documents/Million Song Data/train_triplets.txt";
			"data/90x60x7.txt";
	
	/** Learning Params */
	public static final int _f = 7;
	public static final double _alpha = 1.0/20.0;
	public static final int _iterations = 100;
	public static final double _lambda = .01;
	
	/** Main Variables */
	protected static int _M; //#users
	protected static int _N; //#item
	protected static ArrayList<String> _UserIDs;
	protected static ArrayList<String> _ItemIDs;
	protected static ArrayList<HashMap<Integer, Double>> _r;
	protected static ArrayList<HashMap<Integer, Double>> _rt;
	protected static Matrix _X;
	protected static Matrix _Y;
	
	public static void main(String[] args) throws IOException {
		
		DataImporter.importData(DATA_FILE);
		
		//printData();
		
		//TODO: @Zach fill this out to match the signature you want
		//Trainer.trainModel()
		
		CostMinimizer.minCost();
		
		//TODO: recommend
		ArrayList<Pair<String, Double>> t = Recommender.recommend("0",60);
		ArrayList<String> s = new ArrayList<String>();
		for (int i=0; i<t.size();i++) {
			System.out.println("item ["+t.get(i).first + "] with value [" + t.get(i).second + "]");
			s.add(t.get(i).first);
		}
		System.out.println("*****************************");
		ArrayList<Pair<String,Double>> t2 = EvaluateHidden.recommend("pre-data/90x60x7.txt",0,60);
		ArrayList<String> s2 = new ArrayList<String>();
		for (int i=0; i<t2.size();i++) {
          System.out.println("item ["+t2.get(i).first + "] with value [" + t2.get(i).second + "]");
          s2.add(t2.get(i).first);
      }
		System.out.println(ListSimilarity.basicDifference(s, s2));
		
		for (int i=0; i<s.size(); i++) {
			System.out.println(s.get(i) + "\t" + s2.get(i));
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
