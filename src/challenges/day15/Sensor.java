package challenges.day15;

import aocutil.geometry.Coord2D;
import aocutil.string.RegexMatcher;

public class Sensor {
	/** The coordinate of the sensor */
	protected final Coord2D pos;
	
	/** The minimal scanning range */
	protected final int minrange;
	
	/**
	 * Creates a new Sensor
	 * 
	 * @param pos The position of the beacon
	 * @param minrange The minimal scanning range
	 */
	public Sensor( final Coord2D pos, final int minrange ) {
		this.pos = pos;
		this.minrange = minrange;
	}

	public static Sensor fromString( final String input ) {
		final RegexMatcher rm = RegexMatcher.match( "Sensor at x=#D, y=#D: closest beacon is at x=#D, y=#D", input );
		final Coord2D s = new Coord2D( rm.getInt( 1 ), rm.getInt( 2 ) );
		final Coord2D b = new Coord2D( rm.getInt( 3 ), rm.getInt( 4 ) );
		
		return new Sensor( s, s.getManhattanDistance( b ) );
	}
	
	
	public Range getCoverage( final int index, final boolean rowbased ) {
		// is the sensor within range of the row at all?
		if( rowbased ) {
			if( pos.y + minrange < index || pos.y - minrange > index ) return null;
			final int d = minrange - Math.abs( pos.y - index );
			return new Range( pos.x - d, pos.x + d ); 
		} else {
			if( pos.x + minrange < index || pos.x - minrange > index ) return null;
			final int d = minrange - Math.abs( pos.x - index );
			return new Range( pos.y - d, pos.y + d ); 
			
		}
	}
	
	
	@Override
	public String toString( ) {
		return pos.toString( ) + "[" + minrange + "]";
	}
}
