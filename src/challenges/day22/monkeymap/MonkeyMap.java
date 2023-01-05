package challenges.day22.monkeymap;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * A map that can be navigated and wraps around if movement takes you of the
 * map 
 * 
 * @author Joris
 */
public class MonkeyMap {	
	/** The actual map */
	protected final CoordGrid<Tile> map;
	
	/**
	 * Creates a new MonkeyMap
	 * 
	 * @param map The map as a grid of tiles
	 */
	protected MonkeyMap( final CoordGrid<Tile> map ) {
		this.map = map;
	}
	
	/**
	 * Reconstructs a map form a list of strings
	 * 
	 * @param input The list of strings that describes the map
	 * @return The MonkeyMap
	 */
	public static MonkeyMap fromStringList( final List<String> input ) {
		return new MonkeyMap( CoordGrid.fromStringList( input, "", t -> Tile.fromChar( t.charAt(0 ) ), Tile.Empty ) );
	}
	
	/**
	 * Navigates through the map using the set of given move commands 
	 * 
	 * @param moves The string that describes the movement through the map 
	 * @return The coordination and direction as single value
	 */
	public long navigate( final String moves ) {
		// determine starting position (first top-left coordinate that is open)
		final int y = map.window( ).getMinY( );
		Coord2D startpos = null;
		for( int x = map.window( ).getMinX( ); x <= map.window( ).getMaxX( ); x++ )
			if( map.get( x, y ) == Tile.Open ) {
				startpos = new Coord2D( x, y );
				break;
			}
		
		// process moves input into list of actions 
		final List<Action> A = processMoves( moves );

		// now navigate the map using the given set of moves
		Player p = new Player( startpos, Direction.East );
		for( final Action act : A ) {
			
			// turn action?
			if( act.type == Action.ActionType.TurnLeft || act.type == Action.ActionType.TurnRight ) {
				p.dir = p.dir.turn( act.type == Action.ActionType.TurnLeft ? -1 : 1 );
				continue;
			}

			// move action
			p = move( p, act.steps );
		}
		
		// compute result based on terminal position
		return (p.pos.y + 1) * 1000 + (p.pos.x + 1) * 4 + ((p.dir.ordinal( ) + 3) % 4);
	}
	
	/**
	 * Moves the player x steps in the direction it is currently facing until
	 * it hits a wall. If the move takes the player off the map, a wrap-around
	 * will happen
	 * 
	 * @param steps The number of steps to move
	 * @return The new position (and direction) of the player
	 */
	protected Player move( final Player player, final int steps ) {
		Player p = new Player( player.pos, player.dir );
		for( int i = 0; i < steps; i++ ) {
			// determine target position and check if we can move
			Player next = new Player( p.pos.move( p.dir, 1 ), p.dir );
			
			// next position off the map? then wrap around
			if( map.get( next.pos ) == Tile.Empty ) next = wrap( p );
			
			// check if this is a wall? if so, we cannot move any further
			if( map.get( next.pos ) == Tile.Wall ) break;
			
			// nope, move!
			p = next;
		}
		return p;
	}

	/**
	 * Determines the new position and facing of the player when a wrap-around
	 * occurs
	 * 
	 * @param p The current position of the player, which needs wrapping
	 * @return The new position (and direction) after wrapping
	 */
	protected Player wrap( final Player p ) {
		switch( p.dir ) {
			case East:
				for( int x = map.window( ).getMinX( ); x < p.pos.x; x++ )
					if( map.get( x, p.pos.y ) != Tile.Empty ) return new Player( new Coord2D( x, p.pos.y ), p.dir );
				break;

			case West:
				for( int x = map.window( ).getMaxX( ); x > p.pos.x; x-- )
					if( map.get( x, p.pos.y ) != Tile.Empty ) return new Player( new Coord2D( x, p.pos.y ), p.dir );
				break;
				
			case South:
				for( int y = map.window( ).getMinY( ); y < p.pos.y; y++ )
					if( map.get( p.pos.x, y ) != Tile.Empty ) return new Player( new Coord2D( p.pos.x, y ), p.dir );
				break;
				
			case North:
				for( int y = map.window( ).getMaxY( ); y > p.pos.y; y-- )
					if( map.get( p.pos.x, y ) != Tile.Empty ) return new Player( new Coord2D( p.pos.x, y ), p.dir );
				break;
		}
		
		throw new RuntimeException( "Failed to wrap around!" );
	}
	
	/**
	 * Processes a string of moves into a list of actions that can be executed
	 * 
	 * @param moves The string of moves
	 * @return An actionable list
	 */
	private List<Action> processMoves( final String moves ) {
		final List<Action> A = new ArrayList<>( );
		
		int lastidx = 0;
		int idx = -1;
		while( ++idx < moves.length( ) ) {
			// scan until we find a turn command
			if( moves.charAt( idx ) == 'L' || moves.charAt( idx ) == 'R' ) {

				// add move and turn action
				A.add( Action.move( Integer.parseInt( moves.substring( lastidx, idx ) ) ) );
				A.add( Action.turn( moves.charAt( idx ) == 'L' ) );
				lastidx = idx + 1;
			}
		}
		
		// add remainder as last move
		A.add( Action.move( Integer.parseInt( moves.substring( lastidx ) ) ) );
		return A;
	}
	
	/** @return The gird */
	@Override
	public String toString( ) {
		return map.toString( );
	}
	
	/**
	 * Single action that can be performed while navigating the maze 
	 */
	private static class Action {
		/** The action type */
		protected enum ActionType { Move, TurnLeft, TurnRight; }
		
		/** The type of this action */
		protected final ActionType type;
		
		/** The steps to move, in case of a move action */
		private final int steps;
		
		/**
		 * Creates a new action
		 * 
		 * @param type The action type
		 * @param steps The steps to move (if this is a move action)
		 */
		private Action( final ActionType type, final int steps ) {
			this.type = type;
			this.steps = steps;
		}
		
		/**
		 * Creates a move action
		 * 
		 * @param steps The steps to move
		 * @return The action
		 */
		protected static Action move( final int steps ) { 
			return new Action( ActionType.Move, steps );
		}
		
		/**
		 * Creates a new turn action
		 * 
		 * @param left True to turn left, false to turn  right
		 */
		protected static Action turn( final boolean left ) {
			if( left )
				return new Action( ActionType.TurnLeft, 0 );
			else
				return new Action( ActionType.TurnRight, 0 );
		}
		
		/** @return The string describing the action */
		@Override
		public String toString( ) {
			return type.toString( ) + (type == ActionType.Move ? steps : "");
		}
	}
	
	/** The tiles in the map */
	protected enum Tile { 
		Empty(' '), Open('.'), Wall('#');
		
		/** The character corresponding to the tile */
		private char tilechar;
		
		/**
		 * Creates a new enum value
		 * @param ch The character that corresponds to the tile
		 */
		private Tile( final char ch ) {
			this.tilechar = ch;
		}
	
		/**
		 * Reconstructs a tile from a character
		 * 
		 * @param ch The character
		 * @return The tile that corresponds to the character
		 * @throws IllegalArgumentException if no corresponding tile exists
		 */
		final static Tile fromChar( final char ch ) {
			for( final Tile t : Tile.values( ) )
				if( t.tilechar == ch ) return t;
			throw new IllegalArgumentException( "No such tile: " + ch );
		}
		
		/** @return The tile character */
		@Override
		public String toString( ) {
			return "" + tilechar;
		}
	}
}
