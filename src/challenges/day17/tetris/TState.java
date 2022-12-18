package challenges.day17.tetris;

import aocutil.grid.CoordGrid;

/**
 * A state of the tetris game
 * 
 * @author Joris
 */
public class TState {
	private final String statestr;
	
	/** The line we've last seen */
	protected final String line;
	
	/** The current shape index */
	protected final int shape;
	
	/** The current move index */
	protected final int move;
	
	/**
	 * Creates a new state of the game
	 * 
	 * @param game The game
	 * @param rows The number of rows to include in the state
	 * @param currheight The current height of the stack, to offset coordinates
	 * @param shape The index of the shape we are dropping
	 * @param move The index of the move we are performing
	 */
	public TState( final CoordGrid<Boolean> game, final int rows, final int currheight, final int shape, final int move ) {
		final int COLS = Tetris.WIDTH;
		
		// build string of the last X rows
		final char[] l = new char[ COLS * rows ];
		game.getKeys( ).stream( ).filter( c -> c.y < -(currheight - rows) )
			.forEach( c -> l[ c.x + (c.y + currheight) * COLS] = '#' );
		this.line = String.valueOf( l );
		
		// store indexes and build a state string for comparison
		this.shape = shape;
		this.move = move;
		this.statestr = this.line + shape + move;
	}
	
	/**
	 * Check if this state is equal to another object
	 * 
	 * @param obj The object to compare against
	 * @return True iff the object is a state and has the same state string
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof TState) ) return false;
		final TState s = (TState) obj;
		
		if( shape != s.shape && move != s.move ) return false;
		return line.equals( s.line );
	}

	/** @return The has code of the state string */
	@Override
	public int hashCode( ) {
		return statestr.hashCode( );
	}
	
	/** @return The state string that uniquely describes this state */
	@Override
	public String toString( ) {
		return statestr;
	}
	
}
