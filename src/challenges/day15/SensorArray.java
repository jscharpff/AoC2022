package challenges.day15;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;

public class SensorArray {
	/** The sensors in this array */
	protected List<Sensor> sensors;
	
	/**
	 * Creates a new sensor array
	 * 
	 * @param sensors The sensors in the array
	 */
	private SensorArray( final List<Sensor> sensors ) { 
		this.sensors = new ArrayList<>( sensors );
	}
	
	public static SensorArray fromStringList( final List<String> input ) {
		final List<Sensor> S = new ArrayList<>( );
		for( final String s : input ) S.add( Sensor.fromString( s ) );
		return new SensorArray( S );
	}
	
	public List<Range> getCoverage( final int index, final boolean rowbased ) {
		final List<Range> ranges = new ArrayList<>( sensors.size( ) );
		for( final Sensor s : sensors ) {
			final Range r = s.getCoverage( index, rowbased );
			if( r == null ) continue;
			ranges.add( r );
		}
		ranges.sort( Range::compareTo );
		
		// merge overlapping range in pairwise fashion
		int idx = 1;
		while( idx < ranges.size( ) ) {
			final Range r1 = ranges.get( idx - 1 );
			final Range r2 = ranges.get( idx );
			
			if( r2.min - 1 > r1.max ) {
				idx++;
			} else {
				ranges.remove( idx );
				ranges.set( idx-1, r1.combine( r2 ) );
			}
		}
		return ranges;
	}
	
	public Coord2D getNotCovered( final int maxindex ) {
		// go over rows until we find an uncovered range
		int x = -1;
		for( int y = 0; y < maxindex; y++ ) {
			final List<Range> R = getCoverage( y, true );
			if( R.size( ) == 1 ) continue;
			x = R.get( 1 ).min - 1;
		}
		
		// then find the y coordinate not spanned in that row
		final List<Range> r = getCoverage( x, false );
		return new Coord2D( x, r.get( 1 ).min - 1 );
	}
	
	@Override
	public String toString( ) {
		return sensors.toString( );
	}

}
