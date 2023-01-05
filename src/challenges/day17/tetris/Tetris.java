package challenges.day17.tetris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Simulates a game of tetris
 * 
 * @author Joris
 */
public class Tetris {
	/** The shapes in the tetris game */
	protected final List<TShape> shapes;
	
	/** The grid that contains the state of the current game */
	protected final CoordGrid<Boolean> game;
	
	/** The width of the game window */
	protected static final int WIDTH = 7;
	
	/**
	 * Creates a new game with the given shapes
	 * 
	 * @param shapes The set of available shapes to drop
	 */
	private Tetris( final List<TShape> shapes ) {
		this.shapes = new ArrayList<>( shapes );
		this.game = new CoordGrid<Boolean>( false );
	}
	
	/**
	 * Reconstructs the game from a list of shapes
	 * 
	 * @param shapes The list that holds all available game shapes
	 * @return The tetris game
	 */
	public static Tetris fromShapeList( final List<String> shapes ) {
		final List<TShape> S = new ArrayList<>( shapes.size( ) );
		for( final String s : shapes ) S.add( TShape.fromString( s ) );
		
		return new Tetris( S );
	}
	
	/**
	 * Runs the game of tetris and determines the final stack height
	 * 
	 * @param moves The set of moves to use
	 * @param rocks The number of rocks to drop
	 * @return The height of the stack after all rocks have been dropped
	 */
	public long run( final String moves, final long rocks ) {
		// keep history of states
		final Map<TState, TValue> H = new HashMap<>( );
		final int ROWS = 20;
		
		// simulate the rocks falling while processing the input moves
		int currshape = -1;
		int currmove = -1;
		for( long r = 0; r < rocks; r++ ) {		
			/// initialise the next shape to drop
			currshape = (currshape + 1) % shapes.size( );
			final TShape s = shapes.get( currshape );
			Coord2D p = new Coord2D( 2, -getHeight( ) - (3 + s.height) );
			
			// build the game state and check if we've seen it before
			final TState currstate = new TState( game, ROWS, getHeight( ), currshape, currmove );
			final TValue prev = H.getOrDefault( currstate, null );
			
			// if we've seen this game state before, we can synthesise the resulting
			// height from the height increase and frequency
			if( prev != null ) {
				// we seen this one before, continue dropping rocks until we are at a
				// integer divisor of the required number of rocks
				final long freq = r - prev.round;
				if( (rocks - r) % freq == 0 ) {
					return (long)(getHeight( ) - prev.height) * ((rocks - r) / freq) + getHeight( );
				}
			} else {
				if( getHeight( ) >= ROWS )
					H.put( currstate, new TValue( r, getHeight() ) );
			}
						
			// start the simulation of dropping it
			boolean falling = true;
			while( falling ) {
				// next move to process
				currmove = (currmove + 1) % moves.length( );
				final Direction m = Direction.fromSymbol( moves.charAt( currmove ) );
				
				// first try to move it sideways
				if( canMove( s, p, m ) ) p = p.move( m, 1 );
				
				// then check if it can move downwards
				if( canMove( s, p, Direction.South ) ) {
					p = p.move( Direction.South, 1 );
				} else {
					// no longer possible, next block
					falling = false;
				}
			}
			
			// the shape can no longer move, fix it to the grid
			fixate( s, p );
		}

		// game ended without recurring pattern, return resulting height
		return getHeight( );
	}
	
	/**
	 * Check if the given shape can move in the specified direction
	 * 
	 * @param shape The shape we are dropping
	 * @param pos Its current position
	 * @param dir The direction in which to move it
	 * @return True iff the rock can still move, false if it is blocked
	 */
	protected boolean canMove( final TShape shape, final Coord2D pos, final Direction dir ) {
		final Coord2D tpos = pos.move( dir, 1 );
		
		// check boundaries to rough outline
		if( tpos.x < 0 ) return false;
		if( tpos.x + shape.width > WIDTH ) return false;
		if( tpos.y + shape.height > 0 ) return false;
		
		// check all shape coordinates
		for( final Coord2D c : shape.shape )
			if( game.get( tpos.move( c ) ) ) return false;
		
		return true;
	}
	
	/**
	 * Ends the falling by including the rock in the game state
	 * 
	 * @param shape The shape of the rock
	 * @param pos The position to fixate it at
	 */
	protected void fixate( final TShape shape, final Coord2D pos ) {
		for( final Coord2D c : shape.shape )
			game.set( c.move( pos ), true );
	}
	
	/** @return The total height of the stack of rocks in the game */
	public int getHeight( ) {
		return game.getKeys( ).size( ) == 0 ? 0 : -game.window( ).getMinY( );
	}
	
	/** @return The visual description of the current game state */
	@Override
	public String toString( ) {		
		final CoordGrid<Character> G = new CoordGrid<>( ' ' );
		for( int x = -1; x <= WIDTH; x++ ) G.set( x, 0, '-' );
		for( int y = 1; y <= getHeight( ); y++ ) {
			G.set( -1, -y, '|' ); 
			G.set( WIDTH, -y, '|' );
		}
		
		for( final Coord2D c : game.getKeys( ) ) G.set( c, '#' );
		return G.toString( );
	}
	
	/**
	 * A simple game state value 
	 */
	private class TValue {
		/** The round we are in */
		protected final long round;
		
		/** The height of the stack */
		protected final long height;
		
		/**
		 * Creates a new game state
		 * 
		 * @param round The number of rounds (rocks dropped)
		 * @param height The height of the current stack
		 */
		public TValue( final long round, final long height ) {
			this.round = round;
			this.height = height;
		}

		/** @return The string description of the state value */
		@Override
		public String toString( ) {
			return round + " (" + height + ")";
		}
	}
}
