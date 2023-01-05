package challenges.day04;

/**
 * Captures is single, integer range
 * 
 * @author Joris
 */
public class Range {
	/** The start of the interval */
	private final int min;
	
	/** The end of the interval */
	private final int max;
	
	/**
	 * Creates a new range that spans the values, arbitrarily ordered
	 * 
	 * @param x
	 * @param y
	 */
	public Range( final int x, final int y ) {
		this.min = Math.min( x, y );
		this.max = Math.max( x, y );
	}

	/**
	 * Creates a range from a string description
	 * 
	 * @param range The range as a string: x-y
	 * @return The range [x,y] described by the string
	 */
	public static Range fromString( final String range ) {
		final String[] s = range.split( "-" );
		return new Range( Integer.parseInt( s[0] ), Integer.parseInt( s[1] ) );
	}
	
	/**
	 * Compares this range with another to check whether it completely overlaps
	 * the other range
	 * 
	 * @param r2 The other range
	 * @return True if r2 is contained within this range
	 */
	public boolean contains( final Range r2 ) {
		return r2.min >= min && r2.max <= max;
	}
	
	/**
	 * Compares this range against another to check if they share at least a
	 * single common value
	 * 
	 * @param r2 The other range
	 * @return True iff r2 has at least one value spanned by this range
	 */
	public boolean overlaps( final Range r2 ) {
		return r2.min <= max && r2.max >= min;
	}
}
