package main;

import java.util.ArrayList;

import utilities.ListSimilarity;
import Jama.Matrix;

public class Test {
	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<String> b = new ArrayList<String>();
		
		a.add("1");
		a.add("2");
		a.add("3");
		
		b.add("1");
		b.add("3");
		b.add("2");
		
		System.out.println(ListSimilarity.basicDifference(a, b));
	}

}
