package main;

import java.util.ArrayList;
import java.util.HashMap;

public final class Driver {
	public static final String DATA_FILE = "";
	
	/** Learning Params */
	public static final int _f = 80;
	public static final double _alpha = 1/20;
	public static final int _iterations = 100;
	
	/** Main Variables */
	protected static ArrayList<HashMap<Integer, Double>> _r;
	protected static ArrayList<Double[]> _X;
	protected static ArrayList<Double[]> _Y;
	
	public static void main(String[] args) {
		
		DataImporter.importData(_r, DATA_FILE);
		
		//TODO: @Zach fill this out to match the signature you want
		//Trainer.trainModel()
		
		//TODO: recommend
	}
}
