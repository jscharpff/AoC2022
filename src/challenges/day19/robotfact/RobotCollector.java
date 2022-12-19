package challenges.day19.robotfact;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.cache.Cache;
import aocutil.cache.Cache.PruneStrategy;

/**
 * The actual "engine" of the robot factory. The collector finds for the
 * maximal number of geodes that can be produced for the given blueprint
 * 
 * @author Joris
 */
public class RobotCollector {
	/** The robots as described by the blueprint */
	final Map<Material, Robot> robots;
	
	/** The maximum number of geodes found so far */
	private int g_opt;
	
	/**
	 * Creates a new RobotCollector that will analyse the given blueprint
	 * 
	 * @param blueprint The blueprint describing the robot production and costs
	 */
	protected RobotCollector( final Blueprint blueprint ) {
		// setup the collection process
		robots = new EnumMap<Material, Robot>( Material.class );
		for( final Material m : Material.values( ) ) robots.put( m, blueprint.getRobotProducing( m ) );
	}	

	/**
	 * Starts the collection optimisation process
	 * 
	 * @param time The time allowed to collect and process minerals
	 * @return The maximal number of geodes that can be mined
	 */
	public int collect( final int time ) {
		// initialise the search
		final RCState initialstate = new RCState( time );
		final Cache<RCState, Integer> M = new Cache<>( 1000000, 0.5, PruneStrategy.Oldest );
		g_opt = -1;
		
		// and run it!
		return collect( M, initialstate );
	}
	
	/**
	 * The actual DFS algorithm that optimises the maximal geode count according
	 * to the robot production rules from the blueprint
	 * 
	 * @param M The cache of recently visited states
	 * @param state The current state the DFS is in
	 * @return The maximal number of geodes that can be produced from this state
	 */
	private int collect( final Cache<RCState, Integer> M, final RCState state ) {
		// done?
		if( state.timerem <= 0 ) return state.getResource( Material.Geode );
		
		// already know this state?
		if( M.contains( state ) ) return M.get( state );
		
		// go over all new states
		final List<RCState> newstates = new ArrayList<>( generateNewStates( state ) );
		newstates.sort( (x,y) -> x.estimateValue( ) - y.estimateValue( ) );
		int maxgeodes = state.getResource( Material.Geode ); 
		for( final RCState s : newstates ) {
			// can we improve on the maximal value following this new state?
			if( s.getValueUB( ) <= g_opt ) continue;
			
			// possibly, continue the state computation to make sure
			maxgeodes = Math.max( maxgeodes, collect( M, s ) );
			
			// update optimum value if possible
			if( maxgeodes > g_opt ) g_opt = maxgeodes;
		}
		
		
		// return best state
		M.set( state, maxgeodes );

		return maxgeodes;
	}
	
	/**
	 * Generates all new states that may be explored from the current one
	 * 
	 * @param state The current state
	 * @return The set of (unique) new states
	 */
	protected Set<RCState> generateNewStates( final RCState state ) {
		final Set<RCState> N = new HashSet<>( );
		
		// out of time?
		if( state.timerem <= 0 ) return N;

		// add the state in which we buy no robot
		N.add( state.buy( null ) );
		
		// and the state in which we buy exactly one bot, if we wanted to buy more
		// we should have done so earlier
		for( final Robot r : robots.values( ) ) {
			if( state.canBuy( r ) == 1 ) N.add( state.buy( r ) );
		}
		return N;
	}

	/**
	 * The actual state of the collection process
	 */
	private class RCState {
		/** The time remaining */
		protected final int timerem;
		
		/** The count of robot per material type */
		private final Map<Material, Integer> robotcount;
		
		/** The resource count per material */
		private final Map<Material, Integer> resources;
		
		/** The state string that uniquely describes it */
		protected final String statestr;

		/**
		 * Creates a new state
		 * 
		 * @param time The time remaining in this state
		 * @param rc The robot counts per material
		 * @param rs The resources per material
		 */
		private RCState( final int time, Map<Material, Integer> rc, Map<Material, Integer> rs ) {
			this.timerem = time;
			robotcount = new EnumMap<Material, Integer>( rc );
			resources = new EnumMap<Material, Integer>( rs );	

			statestr = toStateString( );
		}
		
		/** 
		 * Creates a new initial state
		 * 
		 * @param time The time remaining in the initial state
		 */
		protected RCState( final int time ) {
			this.timerem = time;
			
			// initialise counts to 0 except for the single ore robot we start with
			robotcount = new EnumMap<Material, Integer>( Material.class );
			resources = new EnumMap<Material, Integer>( Material.class );			
			for( final Material m : Material.values( ) ) {
				robotcount.put( m, 0 );
				resources.put( m, 0 );
			}
			robotcount.put( Material.Ore, 1 );
			
			statestr = toStateString( );
		}
		
		/**
		 * Creates the new state that results when buying the specified robot type
		 * 
		 * @param r The robot to buy, can be null to buy nothing
		 * @return The new state
		 */
		protected RCState buy( final Robot r ) {
			final Map<Material, Integer> rc = new EnumMap<Material, Integer>( Material.class );
			final Map<Material, Integer> rs = new EnumMap<Material, Integer>( Material.class );			
			
			// first copy values and apply current robot production
			for( final Material m : Material.values( ) ) {
				final int rcount = getRobots( m );
				rc.put( m, rcount );
				rs.put( m, getResource( m ) + rcount );
			}
			
			// then buy the robot
			if( r != null ) {
				rc.put( r.produces, rc.get( r.produces ) + 1 );
				for( final Material m : r.requires.keySet( ) )
					rs.put( m, rs.get( m ) - r.requires.get( m ) );
			}
			
			return new RCState( timerem - 1, rc, rs );			
		}
		
		
		/**
		 * Retrieves the robot count that produces the material 
		 *  
		 * @param m The material 
		 * @return The robot count for the given material
		 */
		protected int getRobots( final Material m ) {
			return robotcount.get( m );
		}
		
		/**
		 * Retrieves the quantity of the material in stock 
		 *  
		 * @param m The material 
		 * @return The amount of units of the given material
		 */
		protected int getResource( final Material m ) {
			return resources.get( m );
		}
		
		/**
		 * Checks how many robots of the type we can buy in this state, given the
		 * current resources
		 *  
		 * @param r The robot type to buy
		 * @return The number of robots we can buy
		 */
		protected int canBuy( final Robot r ) {
			int minqty = Integer.MAX_VALUE;
			for( final Material m : r.requires.keySet( ) ) {
				minqty = Math.min( minqty, resources.get( m ) / r.requires.get( m ) );
			}
			
			return minqty;
		}
		
		/**
		 * @return Quick estimation of state value to sort next states on  
		 */
		protected int estimateValue( ) {
			int offset = 1;
			int val = 0;
			for( final Material m : Material.values( ) ) {
				val += offset * getResource( m );
				offset *= 1000;
			}
			return val;
		}
		
		/**
		 * Computes an upper bound on the number of geodes that can be produced
		 * from this state onwards by assuming that we have infinite Ore
		 * 
		 * @return The upper bound on the number of geodes that can be produced
		 */
		protected int getValueUB( ) {
			final int m = Material.values( ).length;
			final int[] maxprod = new int[ m ];
			
			// for each material check how many we could produce given the upper bound
			// on the previous material
			for( int i = 0; i < m; i++ ) {
				final Material mat = Material.values( )[ i ];
				
				// start with max production if no bot is added
				maxprod[i] = getResource( mat ) + getRobots( mat ) * timerem;
				
				// determine how many of the previous ore we need per robot
				// and keep building robots, all the while also producing materials
				final int req = robots.get( mat ).requires.get( Material.values()[ Math.max( i - 1, 0) ] );				
				int in = i == 0 ? Integer.MAX_VALUE : maxprod[ i-1 ];
				for( int t = timerem - 1; t > 0 && in >= req; t-- ) {
					in -= req;
					maxprod[i] += t;
				}				
			}
			
			return maxprod[ Material.Geode.ordinal( ) ];			
		}
		
		
		/**
		 * @return The string that uniquely describes the state
		 */
		private String toStateString( ) {
			final StringBuilder bots = new StringBuilder( );
			final StringBuilder res = new StringBuilder( );
			for( final Material m : Material.values( ) ) {
				bots.append( robotcount.get( m ) + "," );
				res.append( resources.get( m ) + "," );
			}
			bots.delete( bots.length( ) - 1, bots.length( ) );
			res.delete( res.length( ) - 1, res.length( ) );
			return String.format( "%2d", timerem ) + "|" + bots.toString( ) + "|" + res.toString( );
		}
		
		/**
		 * Compares this state to another object
		 * 
		 * @param obj The object to compare against
		 * @return True iff the other object is a state and has the same state
		 *   string
		 */
		@Override
		public boolean equals( Object obj ) {
			if( obj == null || !(obj instanceof RCState ) ) return false;
			return statestr.equals( ((RCState)obj).statestr );
		}

		/** @return The hash code of the state string */
		@Override
		public int hashCode( ) {
			return statestr.hashCode( );
		}
		
		/** @return The string describing the state */
		@Override
		public String toString( ) {
			return statestr;
		}
	}
}
