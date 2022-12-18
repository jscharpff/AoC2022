package challenges.day16.volcano;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * State of the Volcano valve system
 * 
 * @author Joris
 */
public class VState {
	/** The volcano to which this state applies */
	private final Volcano volcano;
	
	/** The players in the state */
	protected final int N;
	
	/** The player states */
	protected final List<PState> pstates;
	
	/** The unopened valves */
	protected final Set<Valve> unopened;

	/** The accumulated pressure so far */
	protected final int pressure;
	
	private int pressureUB = -1;
	private String statestr;

	/**
	 * Creates a new initial state
	 * 
	 * @param volcano The volcano to which this state applies
	 * @param valves The set of valves in the volcano
	 * @param startpos The starting position(s) of each of the players
	 * @param time The starting time of the initial time (remaining minutes)
	 */
	public VState( final Volcano volcano, final Collection<Valve> valves, final Valve[] startpos, final int time ) {
		this.volcano = volcano;
		
		N = startpos.length;
		pstates = new ArrayList<>( N );
		for( int i = 0; i < N; i++ ) pstates.add( new PState( time, startpos[i] ) );
		
		unopened = new HashSet<>( );
		valves.stream( ).filter( v -> v.pressureInc > 0 ).forEach( unopened::add );
		
		pressure = 0;
	}
	
	/**
	 * Creates a new state
	 * 
	 * @param volcano The volcano to which this state applies
	 * @param pstates The new player states
	 * @param unopened The set of unopened valves
	 * @param pressure The pressure achieved in this state so far (if valves
	 *   left open until time expires)
	 */
	private VState( final Volcano volcano, final List<PState> pstates, final Set<Valve> unopened, final int pressure ) {
		this.volcano = volcano;
		N = pstates.size( );
		this.pstates = new ArrayList<>( pstates );
		this.unopened = new HashSet<>( unopened );
		this.pressure = pressure;
	}
	
	/**
	 * UB heuristic computation of the maximal pressure that can be achieved from
	 * this state
	 * 
	 * @return The UB on the maximal pressure achievable from this state
	 */
	public int getPressureUB( ) {
		if( pressureUB != -1 ) return pressureUB;
		
		pressureUB = pressure;
		for( final Valve v : unopened ) {
			// find shortest distance to valve from any player that is within reach
			final int mintime = pstates.stream( ).mapToInt( p -> p.time - volcano.getDistance( p.pos, v ) + 1 ).max( ).getAsInt( );
			if( mintime > 0 ) pressureUB += v.pressureInc * mintime;
		}
		return pressureUB;
	}
	
	
	/**
	 * Determines the set of all new states that can be reached from this one by
	 * considering all possible moves to unopened valves
	 * 
	 * @return The set of new states, empty if this is a terminal state
	 */
	public Set<VState> getNewStates( ) {
		final Set<VState> newstates = new HashSet<>( );

		// no more targets?
		if( unopened.isEmpty( ) ) return newstates;
		
		// build map of potential next valve targets
		// check if at least one target is still reachable
		boolean found = false;
		final List<Map<Valve, Integer>> targets = new ArrayList<>( );
		for( final PState ps : pstates ) {
			final Map<Valve, Integer> reachable = new HashMap<>( );
			for( final Valve v : unopened ) {
				final int dt = volcano.getDistance( ps.pos, v ) + 1;
				if( dt <= ps.time ) {
					reachable.put( v, dt );
					found = true;
				}
			}
			targets.add( reachable );
		}
		
		// no more reachable targets?
		if( !found ) return newstates;
		
		// create new states for all reachable targets
		newstates.addAll( generateStates( targets ) );
		return newstates;
	}

	/**
	 * Starts a recursive generation of all possible new states by considering
	 * all new states reachable by each of the players
	 * 
	 * @param targets The map of remaining valve targets wth their distances
	 * @return The set of reachable new states
	 */
	private Set<VState> generateStates( final List<Map<Valve, Integer>> targets ) {
		final Set<VState> states = new HashSet<>( );
		generateStates( states, targets, new HashSet<>( unopened ), pressure, new ArrayList<>( pstates ), 0 );
		return states;
	}
	
	/**
	 * Recursively generates the new states, per player, by considering the
	 * available actions to the current player and generating all outcome states
	 * thereof
	 * 
	 * @param states The resulting set of new states
	 * @param targets The map with targets and distances
	 * @param remaining The set of remaining unopened valves
	 * @param newpressure The total pressure built up so far (by taking the actions)
	 * @param ps The new player states
	 * @param idx The index of the current player to consider
	 */
	private void generateStates( final Set<VState> states, final List<Map<Valve, Integer>> targets, final Set<Valve> remaining, final int newpressure, final List<PState> ps, final int idx ) {
		// done, add the state
		if( idx == N ) {
			states.add( new VState( volcano, ps, remaining, newpressure ) );
			return;
		}
		
		// get all possible actions for the current player
		final PState s = ps.get( idx );
		final Map<Valve, Integer> T = targets.get( idx );
		if( T.size( ) == 0 ) {
			// no actions for this player, continue with the others
			generateStates( states, targets, remaining, newpressure, ps, idx + 1 );
			return;
		}
		
		// create new state for every possible outcome for this player
		for( final Valve target : T.keySet( ) ) {
			// can we still pick the target?
			if( !remaining.contains( target ) ) continue;
			
			// yes, generate the outcome and check the next player
			final int timerem = s.time - T.get( target );
			final int pressinc = timerem * target.pressureInc;
			remaining.remove( target );
			ps.set( idx, new PState( timerem, target ) );
			generateStates( states, targets, remaining, newpressure + pressinc, ps, idx + 1 );
			ps.set( idx, s );
			remaining.add( target );
		}
	}
	
	
	/** @return The state description string */
	@Override
	public String toString( ) {
		return toStateString( );
	}
	
	/**
	 * Generates and caches the state description string
	 * 
	 * @return The state description string
	 */
	private String toStateString( ) {
		if( statestr == null ) {
			// player states
			final StringBuilder ps = new StringBuilder( );
			for( final PState p : pstates ) {
				ps.append( p );
				ps.append( "," );
			}
			if( ps.length( ) > 0 ) ps.delete( ps.length( ) - 1, ps.length( ) );
			
			// valve states
			final StringBuilder vs = new StringBuilder( );
			for( final Valve v : unopened ) {
				vs.append( v );
				vs.append( '|' );
			}
			if( vs.length( ) > 0 ) vs.delete( vs.length( ) - 1, vs.length( ) );
			
			statestr = "[" + ps.toString( ) + " " + vs.toString( ) + " => " + pressure + "]";
		}
		return statestr;
	}
	
	/** @return The state hash code, using that of its descriptive string */
	@Override
	public int hashCode( ) {
		return toStateString( ).hashCode( );
	}
	
	/**
	 * Compares this state to another object
	 * 
	 * @param obj The other object
	 * @return True iff all players are in the same state, the set of unopend
	 *   valves is equal and the pressure is equal
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof VState) ) return false;
		final VState v = (VState) obj;
		
		if( pressure != v.pressure ) return false;
		if( !pstates.equals( v.pstates ) ) return false;
		if( !unopened.equals( v.unopened ) ) return false;
		
		return true;
	}
	
	/**
	 * The state of a single player in the Volcano
	 */
	private class PState {
		/** The time remaining for this player */
		private final int time;
		
		/** The position of this player */
		private final Valve pos;
		
		/**
		 * Creates a new player state
		 * 
		 * @param time The time in the player state
		 * @param pos The valve at which the player is positioned
		 */
		protected PState( final int time, final Valve pos ) {
			this.time = time;
			this.pos = pos;
		}
		
		/**
		 * Compares this player state to another object
		 * 
		 * @param obj The object to compare to
		 * @return True iff the time and position equal
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof PState) ) return false;
			final PState p = (PState)obj;
			
			return time == p.time && pos.equals( p.pos );
		}
		
		/** @return Description of the player state with position and time */
		@Override
		public String toString( ) {
			return pos.getLabel( ) + "@" + time;
		}
	}
}
