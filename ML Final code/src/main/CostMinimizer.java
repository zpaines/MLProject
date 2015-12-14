package main;

import java.util.stream.IntStream;

import Jama.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class CostMinimizer {
  
 
  public static void minCost() {
    Driver._Y = new Matrix(Driver._N,Driver._f,1);
    Driver._X = new Matrix(Driver._M, Driver._f,1);
    Random r = new Random();
    for (int y = 0; y<Driver._Y.getRowDimension(); y++) {
      for (int x=0; x<Driver._Y.getColumnDimension(); x++) {
        Driver._Y.set(y, x, r.nextDouble()*2);
      }
    }
    for (int y = 0; y<Driver._X.getRowDimension(); y++) {
      for (int x=0; x<Driver._X.getColumnDimension(); x++) {
        Driver._X.set(y, x, r.nextDouble()*2);
      }
    }
    //N = # items
    boolean converged = false;
    int count = 0;
    double lambda = Driver._lambda;
    
    Matrix lambdaI1 = Matrix.identity(Driver._Y.getColumnDimension(), Driver._Y.getColumnDimension()).times(Driver._lambda);  //Driver_Y.getColumnDimension
    Matrix lambdaI2 = Matrix.identity(Driver._X.getColumnDimension(), Driver._X.getColumnDimension()).times(Driver._lambda);  //Driver_ 
    Matrix Ii = new Matrix(Driver._M, Driver._M, 1);
    Matrix Iu = new Matrix(Driver._N, Driver._N, 1);
    while (!converged) {
        if (++count > Driver._iterations) {
          converged = true;
        }
      /*
       * Compute the Yus
       */
      
      Matrix xT = Driver._X.transpose();
      Matrix XX = xT.times(Driver._X);
      
      for (int i=0; i< Driver._Y.getRowDimension(); i++) {
        Matrix XtCI = computeXtC(xT, i).times(Driver._X).plus(lambdaI2);
        Matrix inverted = XX.plus(XtCI).inverse();
        Matrix Yu = multiplyCi(inverted.times(xT), i).times(new Matrix(computePi(i)));
        Driver._Y.setMatrix(i,i, 0, Driver._Y.getColumnDimension()-1, Yu.transpose());
      }
      
      
      /*
       * Compute the Xis
       */
      
      Matrix yT = Driver._Y.transpose();
      Matrix YY = yT.times(Driver._Y);
            
      for (int u=0; u<Driver._X.getRowDimension(); u++) {
        Matrix YtCI = computeYtC(yT, u).times(Driver._Y);
        Matrix a = YY.plus(YtCI).plus(lambdaI1);
        Matrix inverted = a.inverse();
        Matrix p = new Matrix(computePu(u));
        Matrix Xu = multiplyCu(inverted.times(yT), u).times(p);
        Driver._X.setMatrix(u,u, 0, Driver._X.getColumnDimension()-1, Xu.transpose());
      }
    
    }
    
  }
  
  private static double[][] computePi(int i) {
    double[][] P = new double[Driver._M][1];
    HashMap<Integer, Double> item = Driver._rt.get(i);
    for (int u=0; u<Driver._M; u++) {
      if (item.containsKey(u)) {
        P[u][0] = 1;
      } else {
        P[u][0] = 0; //-1?
      }
    }
    return P;
  }
  
  private static double[][] computePu(int u) {
    double[][] P = new double[Driver._N][1];
    HashMap<Integer, Double> user = Driver._r.get(u);
    for (int i=0; i<Driver._N; i++) {
      if (user.containsKey(i)) {
        P[i][0] = 1;
      } else {
        P[i][0] = 0; //-1?
      }
    }
    return P;
  }
  
  private static double[][] createcSquarei(int i) {
    double[][] C = new double[Driver._M][Driver._M];
    HashMap<Integer, Double> item = Driver._rt.get(i);
    for (int u=0; u<Driver._M; u++) {
      if (item.containsKey(u)) {
        C[u][u] = computeConfidence(item.get(u))-1;
      } else {
        C[u][u] = 0; //-1?
      }
    }
    return C;
  }
  
  private static double[][] createcSquareu(int u) {
    double[][] C = new double[Driver._N][Driver._N];
    HashMap<Integer, Double> user = Driver._r.get(u);
    for (int i=0; i<Driver._N; i++) {
      if (user.containsKey(i)) {
        C[i][i] = computeConfidence(user.get(i))-1;
      } else {
        C[i][i] = 0; //-1?
      }
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
  
  private static Matrix computeYtC(Matrix yT, int u) {
    double[][] YtCu = new double[Driver._f][Driver._N];
    HashMap<Integer, Double> user = Driver._r.get(u);
    for (int row = 0; row < Driver._f; row++) {
      for (int col=0; col < Driver._N; col++) {
        YtCu[row][col] = yT.get(row, col) * (computeConfidence(user.get(col)) - 1);
      }
    }
    return new Matrix(YtCu);
  }
  
  private static Matrix computeXtC(Matrix xT, int i) {
    double[][] XtCu = new double[Driver._f][Driver._M];
    HashMap<Integer, Double> item = Driver._rt.get(i);
    for (int row = 0; row < Driver._f; row++) {
      for (int col=0; col < Driver._N; col++) {
        XtCu[row][col] = xT.get(row, col) * (computeConfidence(item.get(col)) - 1);
      }
    }
    return new Matrix(XtCu);
  }
  
  private static Matrix multiplyCu(Matrix multiplied, int u) {
    double[][] result = new double[multiplied.getRowDimension()][multiplied.getColumnDimension()];
    HashMap<Integer, Double> user = Driver._r.get(u);
    for (int row = 0; row < multiplied.getRowDimension(); row++) {
      for (int col=0; col < multiplied.getColumnDimension(); col++) {
        result[row][col] = multiplied.get(row, col) * (computeConfidence(user.get(col)) - 1);
      }
    }
    return new Matrix(result);
   }
  
  private static Matrix multiplyCi(Matrix multiplied, int i) {
    double[][] result = new double[multiplied.getRowDimension()][multiplied.getColumnDimension()];
    HashMap<Integer, Double> item = Driver._rt.get(i);
    for (int row = 0; row < multiplied.getRowDimension(); row++) {
      for (int col=0; col < multiplied.getColumnDimension(); col++) {
        result[row][col] = multiplied.get(row, col) * (computeConfidence(item.get(col)) - 1);
      }
    }
    return new Matrix(result);
  }
  
  private static double computeConfidence(Double rValue) {
    if (rValue == null) {
      return 1;
    }
    return 1 + Driver._alpha*rValue;
  }
  
}
