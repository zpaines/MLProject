package data_gen;

/**
 * Artificially set X and Y, generate data with this.
 * this method has the most assumptions about the form of the data,
 * and so may not generalize well.
 * (Expected higher than normal accuracy.)
 * @author Derek
 *
 */
public class GenHidden {
	public static final int _M = 80;
	public static final int _N = 55;
	public static final int _f = 3;
	
	public static void main(String[] args) {
		String filename = _M+"x"+_N+"x"+_f+".txt";
		IdealCreator creator = new IdealCreator(_M, _N, _f, filename);
		creator.create();
		
		System.out.println("Done!");
	}

}
