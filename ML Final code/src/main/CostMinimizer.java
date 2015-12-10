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
    
    Matrix lambdaI1 = new Matrix(Driver._Y.getColumnDimension(), Driver._Y.getColumnDimension(), lambda);
    Matrix lambdaT2 = new Matrix(Driver._X.getColumnDimension(), Driver._X.getColumnDimension(), lambda);    
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
      
      for (int i=0; i<Driver._Y.getRowDimension(); i++) {
        Matrix C = new Matrix(createcSquarei(i));//.minus(Ii);
        Matrix XtCI = xT.times(C).times(Driver._X).plus(lambdaT2);
        Matrix inverted = XX.plus(XtCI).inverse();
        Matrix Yu = inverted.times(xT).times(C).times(new Matrix(computePi(i)));
        Driver._Y.setMatrix(i,i, 0, Driver._Y.getColumnDimension()-1, Yu.transpose());
      }
      
      
      /*
       * Compute the Xis
       */
      
      Matrix yT = Driver._Y.transpose();
      Matrix YY = yT.times(Driver._Y);
            
      for (int u=0; u<Driver._X.getRowDimension(); u++) {
        Matrix C = new Matrix(createcSquareu(u));//.minus(Iu);
        Matrix t2 = yT.times(C);
        Matrix t = t2.times(Driver._Y);
        Matrix YtCI = t.plus(lambdaI1);
        Matrix a = YY.plus(YtCI);
       /* for (int y = 0; y<a.getRowDimension(); y++) {
          for (int x=0; x<a.getColumnDimension(); x++) {
            System.out.print(a.get(y, x) + ",");
          }
          System.out.println();
        }*/
        Matrix inverted = a.inverse();
        Matrix p = new Matrix(computePu(u));
        Matrix Xu = inverted.times(yT).times(C).times(p);
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
  
  private static double computeConfidence(double rValue) {
    return 1 + Driver._alpha*rValue;
  }
  
}
