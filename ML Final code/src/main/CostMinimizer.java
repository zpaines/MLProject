package main;

import java.util.stream.IntStream;

import Jama.Matrix;


public class CostMinimizer {
  
 
  public static void minCost(int[][] r) {
    Matrix y = new Matrix(5,5,5);
    Matrix x = new Matrix(5,5,5);
    
    boolean converged = false;
    
    double lambda = 0.5;
    
    Matrix lambdaI1 = new Matrix(y.getColumnDimension(), y.getColumnDimension(), lambda);
    Matrix lambdaT2 = new Matrix(x.getColumnDimension(), x.getColumnDimension(), lambda);    
    Matrix Ii = new Matrix(r.length, r.length, 1);
    Matrix Iu = new Matrix(r[0].length, r[0].length, 1);
    
    while (!converged) {
        
      /*
       * Compute the Yus
       */
      Matrix xT = x.transpose();
      Matrix XX = xT.times(x);
      
      for (int i=0; i<y.getRowDimension(); i++) {
        Matrix C = new Matrix(createcSquarei(r, i)).minus(Ii);
        Matrix XtCI = xT.times(C).plus(lambdaT2);
        Matrix inverted = XX.plus(XtCI).inverse();
        Matrix Yu = inverted.times(xT).times(C).times(new Matrix(computePi(r, i)));
        y.setMatrix(i,i, 0, y.getColumnDimension(), Yu);
      }
      
      /*
       * Compute the Xis
       */
      
      Matrix yT = y.transpose();
      Matrix YY = yT.times(y);
            
      for (int u=0; u<x.getRowDimension(); u++) {
        Matrix C = new Matrix(createcSquareu(r, u)).minus(Iu);
        Matrix YtCI = yT.times(C).plus(lambdaI1);
        Matrix inverted = YY.plus(YtCI).inverse();
        Matrix Xu = inverted.times(yT).times(C).times(new Matrix(computePu(r, u)));
        x.setMatrix(u,u, 0, y.getColumnDimension(), Xu);
      }
    
    }
    
  }
  
  private static double[][] computePi(int[][] r, int i) {
    double[][] P = new double[1][r.length];
    for (int u=0; u<r.length; u++) {
      P[0][u] = (r[u][i] > 0) ? 0 : 1;
    }
    return P;
  }
  
  private static double[][] computePu(int[][] r, int u) {
    double[][] P = new double[1][r[0].length];
    for (int i=0; i<r.length; i++) {
      P[0][i] = (r[u][i] > 0) ? 0 : 1;
    }
    return P;
  }
  
  private static double[][] createcSquarei(int[][] r, int i) {
    double[][] C = new double[r.length][r.length];
    for (int u=0; u<r.length; u++) {
      C[u][u] = computeConfidence(r[u][i]); //-1?
    }
    return C;
  }
  
  private static double[][] createcSquareu(int[][] r, int u) {
    double[][] C = new double[r[0].length][r[0].length];
    for (int i=0; i<r[0].length; i++) {
      C[i][i] = computeConfidence(r[u][i]); //-1?
    }
    return C;
  }

  private static double[][] computeSquare(double[][] array) {
    double[][] square = new double[array.length][array.length];
    for (int u=0; u<array.length; u++) {        //For each row
      for (int i=0; i<array.length; u++) {      //Multiply by the other rows
        for (int n=0; n<array[0].length; n++) { //Iterate through each element of the rows
          square[u][i] += array[u][n] * array[i][n];
        }
      }
    }
    return square;
  }
  
  private static double[][] computeSubtract(double[][] a1, double[][] a2) {
    double[][] res = new double[a1.length][a1[0].length];
    for (int i=0; i<a1.length; i++) {
      for (int u=0; u<a1[0].length; u++) {
        res[i][u] = a1[u][i] - a2[u][i];
      }
    }
  return res;
  }
  
  private static double computeConfidence(int rValue) {
    return 1 + 10*rValue;
  }
  
}
