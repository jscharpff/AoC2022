package challenges.day08;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

public class Forest {
	/** The data grid containing the tree heights */
	protected final CoordGrid<Integer> trees;
	
	/**
	 * Creates a new forest with the griven tree map
	 * 
	 * @param trees The map of tree heights
	 */
	private Forest( final CoordGrid<Integer> trees ) {
		this.trees = trees;
	}
	
	/**
	 * Creates a new forest from a grid that contains the tree heights
	 * 
	 * @param input The list of strings that contain the tree heights, one string
	 *   per row of the grid
	 * @return The forest
	 */
	public static Forest fromGrid( final List<String> input ) {
		return new Forest( CoordGrid.fromDigitGrid( input ) );
	}
	
	/**
	 * Counts the number of visible trees in the forest. A tree is visible if
	 * all trees in the same row and column are lower in height
	 * 
	 * @return The count of visible trees
	 */
	public long countVisible( ) {
		return trees.countIf( this::isVisible );
	}
	
	/**
	 * Checks if a tree is visible by checking in all directions whether there is
	 * another tree of similar or greater height that is blocking it. If not in
	 * at least one direction, it is visible
	 * 
	 * @param coord The coordinate of the tree to check
	 * @return True if the tree at the given coordinate is visible 
	 */
	public boolean isVisible( final Coord2D coord ) {
		final int h = trees.get( coord );
		boolean visible = true;
		for( Integer d : new int[] { -1, +1 } ) {
			visible = true;
			for( int x = coord.x + d; x >= 0 && x < trees.size( ).x; x += d ) 
				if( trees.get( x, coord.y ) >= h ) { visible = false; break; }
			if( visible ) return true; 

			visible = true;
			for( int y = coord.y + d; y >= 0 && y < trees.size( ).y; y += d ) 
				if( trees.get( coord.x, y ) >= h ) { visible = false; break; }
			if( visible ) return true; 
		}
		return visible;
	}
	
	/**
	 * Determines the maximum viewing distance score over all trees in the
	 * forest.
	 * 
	 * @return The maximum score
	 */
	public long maxViewScore( ) {
		long max = -1;
		for( final Coord2D c : trees ) {
			final long score = getViewScore( c );
			if( score > max ) max = score;
		}
		return max;
	}

	/**
	 * Computes the viewing distance score for a single tree position. The
	 * viewing distance score is defined as the product of viewing distances in
	 * every non-diagonal direction from a given tree location. The viewing
	 * distance itself is the distance from the tree up to (and including) the
	 * first tree that is equal or greater height in a given direction.
	 * 
	 * @param coord The coordinate of the tree to compute distance for
	 * @return The viewing distance score of that tree
	 */
	public long getViewScore( final Coord2D coord ) {
		final int h = trees.get( coord );
		long vd = 1;
		for( Integer d : new int[]{ -1, +1 } ) {
			long mult = 0;
			for( int x = coord.x + d; x >= 0 && x < trees.size( ).x; x += d ) { mult++; if( trees.get( x, coord.y ) >= h ) break; }
			vd *= mult; 
			
			mult = 0;
			for( int y = coord.y + d; y >= 0 && y < trees.size( ).y; y += d ) { mult++; if( trees.get( coord.x, y ) >= h ) break; }
			vd *= mult;
		}
		return vd;
	}
}
