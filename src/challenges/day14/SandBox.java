package challenges.day14;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that simulates particles of sand falling into a sandbox with rocks
 * that may obstruct the particles and cause them to come to rest 
 * 
 * @author Joris
 */
public class SandBox {	
	/** The grid of rocks and sand */
	protected final CoordGrid<Part> grid;
	
	/** The sand entry point */
	protected final Coord2D entry;
	
	/** The floor Y coordinate */
	protected final int floorY;
	
	private SandBox( final CoordGrid<Part> grid ) {
		this.grid = grid;
		this.entry = new Coord2D( 500, 0 );
		this.floorY = grid.window( ).getMaxY( ) + 2;
	}
	
	/**
	 * Simulates the falling of sand particles until they encounter obstacles in
	 * their path. 
	 * 
	 * @param infiniteFloor True iff there is no observable floor and sand
	 *   particles may fall forever, false if there is a floor at the bottom of
	 *   the sand box simulation   
	 * @return If inifiteFloor is true, the simulation will continue until the
	 *   first particle falls to the infinite floor and will return the number of
	 *   particles that are in rest. If false, the simulation will continue until
	 *   the entrance is blocked an return the total number of particles in rest. 
	 */
	public long sim( final boolean infiniteFloor ) {
		long parts = 0;
		
		while( true ) {
			parts++;
			Coord2D c = entry;
			
			// simulate falling behaviour until we hit rocks or sand
			boolean falling = true;
			while( falling ) {
				// fall down!
				c = c.move( 0, 1 );
			
				if( !infiniteFloor && c.y >= floorY ) {
					falling = false;
				} else if( grid.hasValue( c ) ) {
					// check if we can fall sideways to the left
					if( !grid.hasValue( c.move( -1, 0 ) ) ) {
						c = c.move( -1, 0 );
						// or to the right
					} else if( !grid.hasValue( c.move( 1, 0 ) ) ) {
						c = c.move( 1, 0 );						
					} else {
						// nope, we are done falling and go into rest
						falling = false;
					}
				}
				
				// the particle has fallen off the grid?
				if( infiniteFloor && c.y > floorY ) return parts - 1;
			}
			
			// done falling, add it to the grid at the last position, unless this is
			// the entry coordinate, that also means we are done
			final Coord2D prev = c.move( 0, -1 );
			if( prev.equals( entry ) ) return parts;
			grid.set( prev, Part.Sand );
		}
	}

	/**
	 * Reconstructs a sandbox simulation from a list of strings that describe the
	 * rocks in the sandbox as paths of horizontal and vertical lines
	 *  
	 * @param input The list of rock paths: x1,y1 -> x2,y2 -> x3, y3
	 * @return The reconstructed sandbox simulation object
	 */
	public static SandBox fromStringList( final List<String> input ) {
		final CoordGrid<Part> grid = new CoordGrid<>( null );
		
		// go over all paths in the input
		for( final String s : input ) {
			// split the coordinates
			final String[] p = s.split( " -> " );
			for( int i = 1; i < p.length; i++ ) {
				// for every pair of coordinates, trace the path and add the rocks
				// to the grid
				Coord2D curr = Coord2D.fromString( p[i-1] );
				Coord2D end = Coord2D.fromString( p[i] );
				
				// traverse path and mark the grid positions
				while( !curr.equals( end ) ) {
					grid.set( curr, Part.Rock );
					final Coord2D diff = curr.diff( end );
					curr = curr.move( Integer.signum( diff.x ), Integer.signum( diff.y ) );
				}
				// and don't forget the last position
				grid.set( end, Part.Rock );
			}
		}
		
		return new SandBox( grid );
	}
	
	/**
	 * @return The string description that visualises the grid
	 */
	@Override
	public String toString( ) {
		return grid.toString( x -> x == null ? "." : x.toString( ) );
	}

	/** Simple enum that holds the particle types of the sandbox simulation */
	protected enum Part {
		Sand, Rock; 
		
		/** @return The string description of the particle */
		@Override
		public String toString( ) {
			switch( this ) {
				case Sand: return "o";
				case Rock: return "#";
				default: return ".";
			}
		}
	}
}
