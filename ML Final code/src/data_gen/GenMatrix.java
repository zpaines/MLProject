package data_gen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generate data based on connection matrix in text file.
 * @author Derek
 *
 */
public class GenMatrix {
	public static final String fileName = "trivial0.txt";
	
	public static String readFileName  = "pre-data/trivial1.txt";
	public static String writeFileName =     "data/trivial1.txt";

	public static void main(String[] args) throws IOException {
		readFileName = "pre-data/"+fileName;
		writeFileName= "data/"+fileName;
		
		BufferedReader reader = new BufferedReader(new FileReader(readFileName));
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFileName));
		
		String curLine;
		String tokens[];
		int u = 0;
		while ((curLine = reader.readLine()) != null) {
			tokens = curLine.split(" ");
			
			for (int i=0; i<tokens.length; i++) {
				if (tokens[i].equals("0")) {
					continue;
				}
				//System.out.println("" + u + " " + i + " " + tokens[i]);
				writer.write("" + u + "\t" + i + "\t" + tokens[i]);
				writer.newLine();
			}
			
			u ++;
		}
		
		writer.close();
		reader.close();
	}
}
