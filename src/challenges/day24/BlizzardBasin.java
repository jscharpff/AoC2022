package challenges.day24;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import aocutil.cache.Cache;
import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Models a basin full of moving blizzards and offers a convenient function to
 * traverse it (twice...)
 *  
 * @author Joris
 */
public class BlizzardBasin {
	/** The map of blizzards in their initial position */
	private final CoordGrid<Direction> blizzards;
	
	/** The coordinate of the basin entrance */
	private final Coord2D entrance;
	
	/** The coordinate of the basin exit */
	private final Coord2D exit;

	/**
	 * Creates a new basin
	 * 
	 * @param map The map of initial blizzard positions
	 * @param start The coordinate of the basin entrance
	 * @param end The coordinate of the basin exit
	 */
	private BlizzardBasin( final CoordGrid<Direction> map, final Coord2D start, final Coord2D end ) {
		this.blizzards = map;
		this.entrance = start;
		this.exit = end;
	}
	
	/**
	 * Reconstructs a blizzard basin from a list of strings
	 * 
	 * @param input The list of strings
	 * @return The basin
	 */
	public static BlizzardBasin fromStringList( final List<String> input ) { 
		// read map itself, then store blizzards in a dedicated map
		final CoordGrid<Character> map = CoordGrid.fromCharGrid( input, '.' );
		
		// get start and end coordinates
		Coord2D start = null; Coord2D end = null;
		for( int x = map.window( ).getMinX( ); x <= map.window( ).getMaxX( ); x++ ) {
			if( map.get( x, 0 ) == '.' ) start = new Coord2D( x - 1, -1 );
			if( map.get( x, map.window( ).getMaxY( ) ) == '.' ) end = new Coord2D( x - 1, map.window( ).getMaxY( ) - 1 );
		}
		
		// create map for blizzards and fill it
		final CoordGrid<Direction> bmap = new CoordGrid<Direction>( map.window( ).getWidth( ) - 2, map.window( ).getHeight( ) - 2, null );
		for( final Coord2D c : map.getKeys( ) ) {
			final char ch = map.get( c );
			if( ch == '#' || ch == '.' ) continue;
			bmap.set( c.move( -1, -1 ), Direction.fromSymbol( ch ) );
		}
		
		// create the basin
		return new BlizzardBasin( bmap, start, end );
	}
	
	/**
	 * Finds the fastest way from the start to the end of the basin without such
	 * that the path avoids all (moving) blizzards
	 * 
	 * @return The minimal time required to get from start to end
	 */
	public int navigate( ) {
		final State endstate = navigate( new State( entrance, 0, exit ) ); 
		return endstate.time;
	}
	
	/**
	 * Finds the fastest way from the start to the end of the basin, then back to
	 * the start and once more to the entrance, while avoiding all moving
	 * blizzards
	 * 
	 * @return The minimal time required to get from start to end, end to start
	 *   and from start to end again
	 */
	public int navigateTwice( ) {
		// first navigate to the exit
		State s = new State( entrance, 0, exit );		
		s = navigate( s );
		
		// continue from the exit state onward, now back towards the entrance
		s = navigate( new State( exit, s.time, entrance ) );
		
		// and again, now back to the exit
		s = navigate( new State( entrance, s.time, exit ) );
		
		// return the total time needed, which is the time we find the exit in the
		// last run
		return s.time;
	}
	
	/**
	 * Performs an A*-like search to find the target position (encoded in the
	 * state). The search uses a heuristic function that uses time and distance
	 * to compute a state value and prioritises based upon the lowest score, as
	 * that is closest to the target and/or uses the least amount of time.
	 * 
	 * @param initial The starting state of the navigation process, which also
	 *   specifies the target position
	 * @return The final state that results when the target position is reached
	 */
	private State navigate( final State initial ) {
		// best so far and cache
		State beststate = new State( new Coord2D( 0, 0 ), Integer.MAX_VALUE, initial.target );
		final Cache<State, Integer> M = new Cache<>( 100000, 0.5 );

		// perform an A*-like search over all states that we can end up in
		// prioritise state to explore next based upon lowest heuristic score
		final PriorityQueue<State> Q = new PriorityQueue<>( );
		Q.add( initial );
		while( !Q.isEmpty( ) ) {
			final State state = Q.remove( );
			
			// already tried this state before?
			if( M.contains( state ) ) continue;
			M.set( state, 0 );
			
			// passed best time known?
			if( state.time >= beststate.time ) continue;
			
			// have we reached the goal?
			if( state.pos.equals( state.target ) ) {
				beststate = state;
				continue;
			}
			
			// check what other states we can reach from this one and only consider
			// them if they are not already in queue or recently considered
			final Set<State> newstates = getNewStates( state );
			for( final State s : newstates ) 
				if( !Q.contains( s ) && !M.contains( s ) )
					Q.add( s );
		}
		
		// return the best state
		return beststate;
	}
	
	/**
	 * Determines the set of new states reachable from the current state
	 * 
	 * @param state The current state
	 * @return The set of new states that can be reached
	 */
	protected Set<State> getNewStates( final State state ) {
		final Set<State> N = new HashSet<>( 4 );
		final int t = state.time + 1;
		
		// consider a move in all directions
		for( final Direction d : Direction.values( ) ) {
			// test only valid positions
			final Coord2D pos = state.pos.moveDir( d, 1 );
			if( !isValid( pos ) || isOccupied( pos, t ) ) continue;
			
			N.add( new State( pos, t, state.target ) );
		}
		
		// and consider not moving
		if( !isOccupied( state.pos, t ) ) N.add( new State( state.pos, t, state.target ) );
		
		// return the set
		return N;
	}
	
	/**
	 * Checks if the given coordinate is a valid position in the basin
	 * 
	 * @param pos The position to test
	 * @return True iff the position is within the basin or the exit
	 */
	protected boolean isValid( final Coord2D pos ) {
		return entrance.equals( pos ) || exit.equals( pos ) || blizzards.contains( pos );
	}
		
	/**
	 * Checks if a given position would be occupied by any blizzard at the given
	 * time step
	 * 
	 * @param pos The position to check
	 * @param time The time step
	 * @return True iff the position is occupied by at least one blizzard
	 */
	protected boolean isOccupied( final Coord2D pos, final int time ) {
		final int W = blizzards.window( ).getWidth( );
		final int H = blizzards.window( ).getHeight( );
		
		// check if it is occupied by any blizzard that has moved time steps since
		// its initial position
		if( blizzards.get( ((pos.x - time) % W + W) % W, pos.y ) == Direction.East ) return true;
		if( blizzards.get( (pos.x + time) % W, pos.y ) == Direction.West ) return true;
		if( blizzards.get( pos.x, ((pos.y - time) % H + H) % H ) == Direction.South ) return true;
		if( blizzards.get( pos.x, (pos.y + time ) % H ) == Direction.North ) return true;
			
		// nope, the position is free!
		return false;
	}
	
	/** @return The map of initial blizzard positions */
	@Override
	public String toString( ) {
		return "Basin from " + entrance + " to " + exit + "\n" + blizzards.toString( x -> x == null ? "." : "" + x.toSymbol( ) );
	}
	
	/**
	 * The current state of our navigation process
	 */
	private class State implements Comparable<State> {
		/** Our current position */
		protected final Coord2D pos;
		
		/** The elapsed time */
		protected final int time;
		
		/** The goal position we are navigating to */
		protected final Coord2D target;
		
		/** Cached state score for priority queue */
		private final int score;
		
		protected State( final Coord2D pos, final int time, final Coord2D target ) {
			this.pos = pos;
			this.time = time;
			this.target = target;
			this.score = getStateScore( );
		}
		
		/**
		 * Heuristic function to estimate the value of the state. Used to 
		 * prioritise the A* search
		 * 
		 * @return The heuristic state value
		 */
		private int getStateScore( ) {
			final double wdist = 0.3;
			return (int)(wdist * (double)pos.getManhattanDistance( target ) + (1 - wdist) * (double)time);
		}
		
		/**
		 * Compares two states based upon their heuristic value
		 * 
		 * @param s The other state
		 * @return A negative number if this state has a lower score, positive if
		 *   the other state has a lower score, 0 if equal
		 */
		@Override
		public int compareTo( final State s ) {
			return getStateScore( ) - s.getStateScore( );
		}
		
		/** @return The string that (uniquely) describes this state */
		@Override
		public String toString( ) {
			return "[" + pos + ", " + time + ", " + target + ": " + score + "]";
		}
		
		/**
		 * Checks if this state is equal to another object
		 * 
		 * @param obj The other object to test against
		 * @return True iff the pos, time and target are equal, false otherwise
		 */
		@Override
		public boolean equals( final Object obj ) {
			if( obj == null || !(obj instanceof State) ) return false;
			final State s = (State)obj;
			
			return s.time == time && s.pos.equals( pos ) && s.target.equals( target );
		}
		
		/** @return The hash code of the state string */
		@Override
		public int hashCode( ) {
			return toString( ).hashCode( );
		}
	}
}
