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
	public static final String readFileName  = "pre-data/trivial1.txt";
	public static final String writeFileName =     "data/trivial1.txt";

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(readFileName));
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFileName));
		writer.close();
		reader.close();
		System.exit(0);
		
		String curLine;
		String tokens[];
		int u = 0;
		while ((curLine = reader.readLine()) != null) {
			tokens = curLine.split(" ");
			
			for (int i=0; i<tokens.length; i++) {
				System.out.println("" + u + " " + i + " " + tokens[i]);
				writer.write("" + u + " " + i + " " + tokens[i]);
				writer.newLine();
			}
			
			u ++;
		}
		
		writer.close();
		reader.close();
	}
}
