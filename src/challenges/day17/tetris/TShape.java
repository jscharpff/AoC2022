package challenges.day17.tetris;

import java.util.HashSet;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * A rock shape that is used by the tetris game
 * 
 * @author Joris
 */
public class TShape {
	/** The coordinates in this shape (relative to top left) */
	protected final Set<Coord2D> shape;
	
	/** The width it spans */
	protected final int width;
	
	/** The height it spans */
	protected final int height;

	/**
	 * Creates a new shape
	 * 
	 * @param shape The set of coordinates that are the 'pixels' in the shape
	 */
	public TShape( final Set<Coord2D> shape ) {
		this.shape = new HashSet<>( shape );
		this.width = shape.stream( ).mapToInt( c -> c.x ).max( ).orElse( 0 ) + 1;
		this.height = shape.stream( ).mapToInt( c -> c.y ).max( ).orElse( 0 ) + 1;
	}

	/**
	 * Reconstructs a shape object from a semicolon separated string
	 * 
	 * @param input The string of '.' and '#' characters that describe the shape.
	 *   Rows are separated by semicolons
	 * @return The shape object
	 */
	public static TShape fromString( final String input ) {
		final Set<Coord2D> S = new HashSet<>( );
		
		final String[] s = input.split( ";" );
		for( int y = 0; y < s.length; y++ ) {
			for( int x = 0; x < s[y].length( ); x++ ) {
				if( s[y].charAt( x ) == '#' ) S.add( new Coord2D( x, y ) );
			}
		}
		
		return new TShape( S );
	}
	
	/** @return The string description of the shape */
	@Override
	public String toString( ) {
		final CoordGrid<Character> grid = new CoordGrid<>( '.' );
		for( final Coord2D c : shape ) grid.set( c, '#' );
		return grid.toString( );
	}
}
