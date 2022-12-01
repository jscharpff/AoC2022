package challenges.day01;

/**
 * One of Santa's little helpers that is carrying snacks
 * 
 * @author Joris
 */
public class Elf {
	/** The weight this elf is carrying */
	private final long weight;
	
	/**
	 * Creates a new elf that carries the specified comma-separated list of
	 * snack weights
	 * 
	 * @param snacks The comma-separated list of snack weights
	 */
	public Elf( final String snacks ) {
		long sum = 0;
		for( String s : snacks.split( "," ) ) sum += Integer.parseInt( s );
		weight = sum;
	}
	
	/** @return The total weight this elf is carrying */
	public long getWeight( ) {
		return weight;
	}
}
