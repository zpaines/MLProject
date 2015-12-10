package main;

import Jama.Matrix;

public class Test {
	public static void main(String[] args) {
		Matrix M = new Matrix(3,3,1);
		M.print(5, 1);
		
		Matrix r = new Matrix(1,3,2);
		
		M.setMatrix(1, 1, 0, 2, r);
		M.print(5, 1);
		
		M.getMatrix(1,1,0,2).print(5, 1);
	}

}
