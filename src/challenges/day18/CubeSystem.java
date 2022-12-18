package challenges.day18;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord3D;

/**
 * Cube system that 
 * 
 * @author Joris
 */
public class CubeSystem {
	/** The actual particle cubes */
	protected final Set<Coord3D> cubes;
	
	/** The min coordinates of each axis */
	protected final Coord3D min;
	
	/** The max coordinates of each axis */
	protected final Coord3D max;
	
	/**
	 * Creates a new cube system of the given set of cubes
	 * 
	 * @param cubes The set of cubes in the system
	 */
	private CubeSystem( final Set<Coord3D> cubes ) {
		this.cubes = new HashSet<>( cubes );
		
		// determine min and max coordinates for every axis
		final int N = 3;
		int[] minC = new int[ N ]; int[] maxC = new int[ N ];
		Arrays.fill( minC, Integer.MAX_VALUE );
		Arrays.fill( maxC, Integer.MIN_VALUE );
		for( final Coord3D c : cubes ) {
			for( int i = 0; i < N; i++ ) {
				minC[i] = Math.min( minC[i], c.values[i] );
				maxC[i] = Math.max( maxC[i], c.values[i] );
			}
		}
		
		// store for future use
		this.min = new Coord3D( minC );
		this.max = new Coord3D( maxC );
	}

	/**
	 * Reconstructs the cube system from a list of cube strings
	 * 
	 * @param input The list of cubes, one per line
	 * @return The cube system
	 */
	public static CubeSystem fromStringList( final List<String> input ) {
		final Set<Coord3D> C = new HashSet<>( input.size( ) );
		input.stream( ).map( Coord3D::fromString ).forEach( C::add );
		return new CubeSystem( C );
	}
	
	/**
	 * Counts the number of exposed sides by per cube summing the exposed
	 * surfaces, i.e., those without any connecting cube
	 * 
	 * @return The sum of exposed surfaces
	 */
	public long getSurfaceArea( ) {
		long count = 0;
		for( final Coord3D c : cubes ) {
			count += 6 - cubes.stream( ).mapToInt( cb -> c.getManhattanDist( cb ) ).filter( i -> i == 1 ).sum( );
		}
		return count;
	}
	
	/**
	 * Uses a breadth-first approach to go over the volume that is spanned by the
	 * cube system and count the number of exposed surfaces while not being
	 * completely enclosed
	 * 
	 * @return The number of surfaces exposed to the outside of the system
	 */
	public long getExternalSurfaceArea( ) {
		
		// start just outside the cube system and then move into the system, all
		// the while counting surfaces we encounter
		long count = 0;
		
		// create stack of next coordinates to explore, and keep track of visited
		// coordinates
		final Set<Coord3D> visited = new HashSet<>( cubes.size( ) );
		final Stack<Coord3D> E = new Stack<>( );
		E.add( min.move( -1, -1, -1 ) );
		
		// go over remaining positions
		while( !E.isEmpty( ) ) {
			final Set<Coord3D> nextE = new HashSet<>( );
			
			while( !E.isEmpty( ) ) {
				final Coord3D curr = E.pop( );
				visited.add( curr );
				
				// get neighbours to explore next
				for( final Coord3D n : getNeighbours( curr ) ) {
					if( visited.contains( n ) ) continue;
					
					// keep search within range of particle system
					if( n.x < min.x - 1 || n.y < min.y - 1 || n.z < min.z - 1 ) continue;
					if( n.x > max.x + 1 || n.y > max.y + 1 || n.z > max.z + 1 ) continue;
					
					// is this a cube in the particle system? if so, count one surface
					if( cubes.contains( n ) ) {
						count++;
						continue;
					}
					
					// add neighbour to list of nodes
					nextE.add( n );
				}
			}
			
			// add all coordinates to explore in the next run
			E.addAll( nextE );
		}
		
		return count;
	}
	
	/**
	 * Constructs the set of neighbouring coordinates
	 * 
	 * @param coord The coordinate to generate neighbours for
	 * @return The set of 6 neighbouring coordinates
	 */
	private Set<Coord3D> getNeighbours( final Coord3D coord ) {
		final Set<Coord3D> N = new HashSet<>( 6 );
		N.add( coord.move( -1,  0,  0 ) );
		N.add( coord.move(  1,  0,  0 ) );
		N.add( coord.move(  0, -1,  0 ) );
		N.add( coord.move(  0,  1,  0 ) );
		N.add( coord.move(  0,  0, -1 ) );
		N.add( coord.move(  0,  0,  1 ) );
		return N;
	}

	/** @return The string that describes the cube system */
	@Override
	public String toString( ) {
		return "[PS " + cubes.size( ) + " cubes spanning from " + min + " to " + max + "]";
	}
}
