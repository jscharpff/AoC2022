package challenges.day16.volcano;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import aocutil.cache.Cache;
import aocutil.cache.Cache.PruneStrategy;
import aocutil.string.RegexMatcher;

/**
 * A volcano with valves (?) that regulate the pressure inside and a bunch of
 * elephants (??) that can help in this quest (???)
 * 
 * @author Joris
 */
public class Volcano {
	/** The valves in this volcano */
	protected final Map<String, Valve> valves;
	
	/** The tunnel system as a list of mappings from a valve to others*/
	protected final Map<Valve, List<Valve>> tunnels;
	
	/** The distance of each valve to another */
	protected Map<Valve, Map<Valve, Integer>> D;

	/** The currently best known pressure score */
	private int g_opt;
	
	/**
	 * Creates a new volcano system
	 * 
	 * @param valves The valves in the volcano 
	 * @param tunnels The tunnel system connecting the valves 
	 */
	private Volcano( final Map<String, Valve> valves, final Map<Valve, List<Valve>> tunnels ) {
		this.valves = new HashMap<>( valves );
		this.tunnels = tunnels;
	}
	
	/** 
	 * Retrieves the distance between two valves
	 * 
	 * @param v1 The starting valve
	 * @param v2 The target valve
	 * @return The distance between the valves (time to travel)
	 */
	protected int getDistance( final Valve v1, final Valve v2 ) {
		return D.get( v1 ).getOrDefault( v2, -1 );
	}	
	
	/**
	 * Finds the maximal pressure that can be relieved from the volcano within
	 * the given time limit
	 * 
	 * @param time The time limit
	 * @param startvalves The valves at which players start
	 * @return The maximal pressure release
	 */	
	public int findMaxPressure( final int time, final String... startvalves ) {
		// create initial state
		final int N = startvalves.length;
		final Valve[] startV = new Valve[ N ];
		for( int i = 0; i < N; i++ ) startV[i] = valves.get( startvalves[i] );
		final VState initstate = new VState( this, valves.values( ), startV, time );
		
		// initialise best so far to -1 and create a caching mechanism that can
		// hold the best values for a (maximum) set of states
		g_opt = -1;
		final Cache<VState, Integer> cache = new Cache<VState, Integer>( 1000000, 0.2, PruneStrategy.Oldest );
		
		// now run the search!
		return findMaxPressure( cache, initstate );
	}
	
	/**
	 * Recursive function that finds the maximum pressure release from the given
	 * state by considering all potential future states.
	 * 
	 * @param M The cache
	 * @param state The state to consider
	 * @return The maximum pressure release from this state onward
	 */
	private int findMaxPressure( final Cache<VState, Integer> M, final VState state ) {

		// already visited this state?
		if( M.contains( state ) ) return M.get( state );
	
		// find the maximal pressure achievable from the current state by
		// considering all future states, in order of their heuristic that
		// overestimates the maximal pressure release by ignoring actual movement
		// rules and time costs
		final List<VState> newstates = new ArrayList<>( state.getNewStates( ) );
		newstates.sort( (x,y) -> y.getPressureUB( ) - x.getPressureUB( )  );

		// go over all new states and optimise
		int maxpressure = state.pressure;
		for( final VState newstate : newstates ) {			

			// check if it makes sense to try this state, or will it never achieve
			// a pressure level better than we already found?
			final int UB = newstate.getPressureUB( );
			if( UB <= g_opt ) continue;
			
			// it may be better, compute pressure increase from this state onward
			final int newpress = findMaxPressure( M, newstate );
			
			// update bounds
			maxpressure = Math.max( maxpressure, newpress );
		}
		
		// do we have a better global bound?
		if( maxpressure > g_opt ) g_opt = maxpressure;
		
		// set the value of the state and return its value
		M.set( state, maxpressure );
		return maxpressure;
	}
	
	/**
	 * Reconstructs the volcano system from a list of strings
	 * 
	 * @param input The strings describing the valves and tunnels
	 * @return The volcano
	 */
	public static Volcano fromStringList( final List<String> input ) {
		final Map<String, Valve> V = new HashMap<>( input.size( ) );
		
		// first pass, read valves
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "Valve ([A-Z]+) has flow rate=#D", s );
			final Valve v = new Valve( rm.get( 1 ), rm.getInt( 2 ) );
			V.put( v.getLabel( ), v );
		}
		
		// second pass, read tunnels between valves
		final Map<Valve, List<Valve>> tunnels = new HashMap<>( );
		for( final String s : input ) {
			final RegexMatcher rm = RegexMatcher.match( "Valve ([A-Z]+) has flow rate=\\d+; tunnels? leads? to valves? ([A-Z, ]+)", s );
			final Valve from = V.get( rm.get( 1 ) );
			tunnels.put( from, new ArrayList<>( ) );
			for( final String to : rm.get( 2 ).split( ", " ) )
				tunnels.get( from ).add( V.get( to ) );
		}
		
		// done, return the valve map
		final Volcano v = new Volcano( V, tunnels );
		v.buildAPSP( V.values( ), tunnels );
		return v;
	}
	
	/**
	 * Builds the all-pair shortest-path matrix between all the valves in the
	 * volcano
	 * 
	 * @param V The valves
	 * @param T The set of tunnels connecting the valves
	 */
	private void buildAPSP( final Collection<Valve> V, final Map<Valve, List<Valve>> T ) {
		D = new HashMap<>( );
		for( final Valve from : V )
			D.put( from, getDistancesFrom( V, T, from ) );
	}

	/**
	 * Computes the shortest distance from the given valve to all others
	 * 
	 * @param V The set of valves
	 * @param T The set of tunnels
	 * @param from The starting valve
	 * @return The mapping of <valve, distance> to each other valve
	 */
	private Map<Valve, Integer> getDistancesFrom( final Collection<Valve> V, final Map<Valve, List<Valve>> T, final Valve from ) {
		// create empty distance map and set of valves remaining
		final Map<Valve, Integer> dist = new HashMap<>( );
		final Set<Valve> remaining = new HashSet<>( );
		for( final Valve v : V ) remaining.add( v );
		remaining.remove( from );
		
		// create stack to explore next
		final Stack<Valve> E = new Stack<>( );
		E.add( from );
		int d = 0;

		// continue until we've exhausted our stack
		while( !E.isEmpty( ) ) {
			final Stack<Valve> nextE = new Stack<>( );
			
			// explore all valves at the current distance
			while( !E.isEmpty( ) ) {
				// set distance
				final Valve curr = E.pop( );
				dist.put( curr, d );

				// traverse to one of the unvisited neighbours
				for( final Valve n : T.get( curr ) ) {
					if( dist.containsKey( n ) ) continue;
					
					nextE.push( n );					
				}
			}
			
			// increase distance and all all valves to consider in the next round
			d++;
			E.addAll( nextE );
		}
		
		return dist;
	}
}
