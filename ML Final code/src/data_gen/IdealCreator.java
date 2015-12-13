package data_gen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import Jama.Matrix;

public class IdealCreator {
	
	private int _M;
	private int _N;
	private int _f;
	private String _filename;

	/**
	 * 
	 * @param m #users
	 * @param n #items
	 * @param f factor dimensionality
	 * @param filename automatically puts in data folder
	 */
	public IdealCreator(int m, int n, int f, String filename) {
		_M = m;
		_N = n;
		_f = f;
		_filename = filename;
	}
	
	public void create() {
		Random rand = new Random();
		
		Matrix X = new Matrix(_M, _f, 0);
		for (int row=0; row<_M; row++) {
			for (int col=0; col<_f; col++) {
				X.set(row, col, rand.nextGaussian());
			}
		}
		
		Matrix Y = new Matrix(_N, _f, 0);
		for (int row=0; row<_N; row++) {
			for (int col=0; col<_f; col++) {
				Y.set(row, col, rand.nextGaussian());
			}
		}
		
		
		
		try {
			BufferedWriter data_writer = new BufferedWriter(new FileWriter("data/"+this._filename));
			BufferedWriter predata_writer = new BufferedWriter(new FileWriter("pre-data/"+this._filename));
			
			for (int u=0; u<_M; u++) {
				for (int i=0; i<_N; i++) {
					
					double prod = X.getMatrix(u, u, 0, _f-1).transpose().times(
							Y.getMatrix(i, i, 0, _f-1)).get(0,0);
					if (connect(prod, rand)) {
						data_writer.write("" + u + "\t" + i + "\t" + prod);
						data_writer.newLine();
						predata_writer.write("" + u + "\t" + i + "\t" + prod);
						predata_writer.newLine();
					}
				}
			}
			
			data_writer.close();
			
			predata_writer.write(_M+" "+_N+" "+_f);
			predata_writer.newLine();
			
			for (int u=0; u<_M; u++) {
				for (int k=0; k<_f; k++) {
					predata_writer.write(""+X.get(u, k)+" ");
				}
				predata_writer.newLine();
			}
			
			for (int i=0; i<_N; i++) {
				for (int k=0; k<_f; k++) {
					predata_writer.write(Y.get(i, k) + " ");
				}
				predata_writer.newLine();
			}
			
			predata_writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private boolean connect(double prod, Random rand) {
		double r;
		
		// base chance of being selected
		/*r = rand.nextDouble();
		if (r <= 0.2) return true;*/
		
		//
		r = rand.nextDouble();
		if (r*Math.sqrt(_f)/4 <= prod) return true;
		
		return false;
	}
}
