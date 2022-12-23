package challenges.day22.monkeymap;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;

/**
 * Simple class to hold position and orientation
 * 
 * @author Joris
 */
public class Player {
	/** The position of the player */
	protected Coord2D pos;
	
	/** The direction the player is facing */
	protected Direction dir;
	
	/**
	 * Creates a new Player
	 * 
	 * @param startpos The starting position
	 * @param startdir The direction in which the player is initially facing
	 */
	public Player( final Coord2D startpos, final Direction startdir ) {
		this.pos = startpos;
		this.dir = startdir;
	}

	/**@return The position and direction of the player */
	@Override
	public String toString( ) {
		return "" + pos + "," + dir;
	}
}
