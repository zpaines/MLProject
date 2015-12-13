package data_gen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Jama.Matrix;
import utilities.Pair;

public class EvaluateHidden {
	
	
	public static ArrayList<Pair<String, Double>> recommend(String filename, int user, int num_suggest) throws IOException {
		int M=0, N=0, f=0;
		ArrayList<Integer> Cu = new ArrayList<Integer>();
	
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String curLine = reader.readLine();
		String tokens[];
		
		while (curLine.split(" ").length == 1) {
			tokens = curLine.split("\t");
			
			if (Integer.parseInt(tokens[0]) == user) {
				Cu.add(Integer.parseInt(tokens[1]));
			}
			
			curLine = reader.readLine();
		}
		
		
		tokens = curLine.split(" ");
		M = Integer.parseInt(tokens[0]);
		N = Integer.parseInt(tokens[1]);
		f = Integer.parseInt(tokens[2]);

		Matrix Xu = new Matrix(f, 1, 0);
		for (int u=0; u<M; u++) {
			curLine = reader.readLine();
			if (u == user) {
				tokens = curLine.split(" ");
				for (int k=0; k<f; k++) {
					Xu.set(k, 0, Double.parseDouble(tokens[k]));
				}
			}
		}
		
		Matrix Y = new Matrix(N, f, 0);
		for (int i=0; i<N; i++) {
			curLine = reader.readLine();
			tokens = curLine.split(" ");
			
			for (int k=0; k<f; k++) {
				Y.set(i, k, Double.parseDouble(tokens[k]));
			}
		}
		
		reader.close();
		
		ArrayList<Pair<String, Double>> ans = new ArrayList<Pair<String, Double>>();
		for (int i=0 ;i<N; i++) {
			if (Cu.contains(i)) {
				continue;
			}
			double val = Xu.times(Y.getMatrix(i, i, 0, f-1)).get(0, 0);
			int idx=0;
			for (; idx<ans.size(); idx++) {
				if (val > ans.get(idx).second) {
					break;
				}
			}
			
			ans.add(idx, new Pair<String, Double>(""+i, val));
		}
	
		for (int item : Cu) {
			System.out.println("Cu contains " + item);
		}
		
		return ans;
		
	}

}
