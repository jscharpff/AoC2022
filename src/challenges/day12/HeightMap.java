package challenges.day12;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * HeightMap that stores the rock heights of a 2D grid and enables computation
 * of the shortest path through the rock maze
 * 
 * @author Joris
 */
public class HeightMap {
	/** The grid that holds the heights */
	protected final CoordGrid<Integer> heights;
	
	/** The distance matrix from all positions to the end coordinate */
	protected final CoordGrid<Integer> D;
	
	/** The start coordinate */
	protected final Coord2D start;
	
	/** The end coordinate */
	protected final Coord2D end;
	
	/**
	 * Creates a new HeighMap
	 * 
	 * @param heightmap The map of rock heights
	 * @param start The coordinate of the starting point
	 * @param end The coordinate of the end point
	 */
	private HeightMap( final CoordGrid<Integer> heightmap, final Coord2D start, final Coord2D end ) {
		this.heights = heightmap;
		this.start = start;
		this.end = end;
		
		// in preparation of future queries, start by computing a distance matrix
		// from the end coordinate to all other coordinates in the grid :)
		this.D = buildDistMatrix( end );
	}
	
	/**
	 * Goes over all potential starting coordinates that have zero height level
	 * ('a') and returns the distance of the starting point that reaches the end
	 * in the minimal amount of steps
	 * 
	 * @return The minimal distance from any zero-level height to the end
	 */
	public long findBestStartPath( ) {
		long best = Long.MAX_VALUE;
		for( final Coord2D c : heights ) {
			if( heights.get( c ) == 0 ) {
				final long length = D.get( c );
				if( length != -1 && length < best ) best = length;
			}
		}
		return best;
	}
	
	/**
	 * @return Finds the shortest path length from the start coordinate to the
	 *   end coordinate.
	 */
	public long findShortestPath( ) {
		return D.get( start );
	}
	
	/**
	 * Builds a distance matrix from the given target position to all other
	 * reachable coordinates in the height map
	 * 
	 * @param target The target position to start epxloration from
	 * @return The map of distances from each coordinate to the target
	 */
	private CoordGrid<Integer> buildDistMatrix( final Coord2D target ) {
		final CoordGrid<Integer> visited = new CoordGrid<Integer>( Integer.MAX_VALUE );
		
		int dist = 0;
		final Stack<Coord2D> E = new Stack<>( );
		visited.set( target, dist );
		E.add( target );
		
		// explore all coordinates from here!
		while( !E.isEmpty( ) ) {
			final Set<Coord2D> newE = new HashSet<>( );
			
			// explore at the current distance level
			while( !E.isEmpty( ) ) {
				// next coordinate to check
				final Coord2D curr = E.pop( );
				final int h = heights.get( curr );
				visited.set( curr, dist );
				
				// check whether we can traverse to its neighbours
				final Set<Coord2D> N = new HashSet<>( curr.getAdjacent( false ) );
				for( final Coord2D n : N ) {
					// only check positions within the grid
					if( !heights.contains( n ) ) continue;
					
					// explore only if the height is at most one lower than the current height 
					if( heights.get( n ) < h - 1 ) continue;
					
					// and only if this was not visited before
					if( visited.hasValue( n ) ) continue;				
					
					// valid position to explore next
					newE.add( n );
				}
			}
			
			// swap sets, increase distance and continue until we've explored all 
			dist++;
			E.addAll( newE );
		}
		
		return visited;
	}

	/**
	 * Reconstructs the height map from a list of strings that represent the
	 * heights of all coordinates
	 * 
	 * @param input The list of strings with height levels, one per row
	 * @return The reconstructed height map
	 */
	public static HeightMap fromString( final List<String> input ) {
		// input character map and convert to heights'
		final CoordGrid<Integer> map = new CoordGrid<>( 0 );
		final CoordGrid<Character> charmap = CoordGrid.fromCharGrid( input, '-' );
		Coord2D start = null;
		Coord2D end = null;
		
		// go over the input (as grid now) and process it into proper heights
		for( final Coord2D c : charmap ) {
			final char ch = charmap.get( c );
			if( ch == 'S' ) {
				map.set( c, 0 );
				start = c;				
			} else if( ch == 'E' ) {
				map.set( c, 25 );
				end = c;
			} else {
				map.set( c, charmap.get( c ) - 'a' );
			}
		}
		
		// reconstruct the height map and return it
		return new HeightMap( map, start, end );
	}	
}
