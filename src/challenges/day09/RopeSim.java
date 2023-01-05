package challenges.day09;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;

/**
 * Simulation of a rope bridge that consists of knots in which the head knot is
 * moved and all trailing knots are moved accordingly.
 * 
 * @author Joris
 */
public class RopeSim {
	/** The list of knots in the rope, head being the first tail the last */
	private Coord2D[] knots;
	
	/**
	 * Creates a new rope bridge simulation
	 * 
	 * @param length The number of knots in this rope
	 */
	public RopeSim( final int length ) {
		knots = new Coord2D[ length ];
		for( int i = 0; i < length; i++ )
			knots[i] = new Coord2D( 0, 0 );
	}
	
	/**
	 * Processes a list of head moves and computes the new position of the 
	 * trailing knots as an effect of the move
	 * 
	 * @param moves The list of strings that describes the moves of the head
	 * @return The number of unique positions visited by the tail knot
	 */
	public int processMoves( final List<String> moves ) {
		// create set to keep track of unique visited tail positions
		final Set<Coord2D> tailpos = new HashSet<>( );
		tailpos.add( knots[ knots.length - 1 ] );
		
		// process moves one by one
		for( final String move : moves ) {
			final String[] m = move.split( " " );
			for( int i = 0; i < Integer.parseInt( m[1] ); i++ ) {
				move( Direction.fromLetter( m[0].charAt( 0 ) ) );
				
				// add to history, will only be added to the set if not visited before 
				tailpos.add( knots[ knots.length - 1 ] );
			}
		}
		
		return tailpos.size( );
	}
	
	/**
	 * Process a single move of the head knot in the given direction. Updates the
	 * position of all trailing knots according to a simple distance rule. 
	 * 
	 * @param dir The direction to move the head knot into
	 */
	protected void move( Direction dir ) {
		// move the head in the specified direction
		knots[0] = knots[0].move( dir, 1 );
		
		// then for all following knots, update position according to the knot
		// previous to it
		for( int i = 1; i < knots.length; i++ ) {
			// get positional difference and absolute distance
			final Coord2D d = knots[i].diff( knots[i-1] );
			final int dx = Math.abs( d.x );
			final int dy = Math.abs( d.y );
			
			// is my predecessor at least 2 steps away in any direction?
			if( dx >= 2 || dy >= 2 ) {
				// yes, move at most 1 step in the direction of the previous knot
				// per axis, depending on the difference in row and/or column
				knots[i] = knots[i].move( dx > 0 ? d.x / dx : 0, dy > 0 ? d.y / dy : 0 );
			}
		}
	}
}
