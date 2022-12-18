package challenges.day15;

public class Range implements Comparable<Range> {
	final int min;
	final int max;

	public Range( final int min, final int max ) {
		this.min = min;
		this.max = max;
	}
	
	public Range combine( final Range r ) {
		return new Range( Math.min( min, r.min ), Math.max( max, r.max ) );
	}
	
	public int size( ) {
		return Math.abs( max - min );
	}
	
	@Override
	public int compareTo( final Range o ) {
		final int x = min - o.min;
		if( x == 0 ) return max - o.max;
		else return x;
	}
	
	@Override
	public String toString( ) {
		return "<" + min + "," + max + ">";
	}
}
