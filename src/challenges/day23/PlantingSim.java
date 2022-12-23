package challenges.day23;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Class that simulates the movement of elves in a grove, with the goal to
 * plant new plant
 * 
 * @author Joris
 */
public class PlantingSim {
	/** The current state of the simulation */
	protected CoordGrid<Boolean> grid;
	
	/** The array of directions to consider */
	private final static Direction[] DIRS = new Direction[] { Direction.North, Direction.South, Direction.West, Direction.East };
	
	/**
	 * Creates a new sim
	 * 
	 * @param grid The grid with initial elf positions
	 */
	private PlantingSim( final CoordGrid<Boolean> grid ) {
		this.grid = grid;
	}
	
	/**
	 * Recreates the simulation from a grid, given by a list of strings
	 * 
	 * @param input The list of strings describing the initial position
	 * @return The plant sim
	 */
	public static PlantingSim fromStringList( final List<String> input ) {
		return new PlantingSim( CoordGrid.fromBooleanGrid( input, '#' ) );
	}
	
	/**
	 * Runs the simulation for the given number of rounds
	 * 
	 * @param rounds The number of rounds to run
	 * @return The count of empty positions in the smallest area that contains
	 *   the positions of all elves
	 */
	public long run( final int rounds ) {
		for( int r = 0; r < rounds; r++ ) sim( r );
		
		// return the number of empty spaces in the smallest grid that contains
		// all the elves
		return grid.count( false );
	}
	
	/**
	 * Runs the simulation until no elf want to move anymore
	 * 
	 * @return The number of the first round that has no more movement
	 */
	public long runUntilStable( ) {
		int r = 0;
		while( sim( r++ ) ) {};
		return r;
	}
	
	/**
	 * Simulates a single round of the planting process. Performs a two-step
	 * process in which first the intended move is determined for every elf and
	 * then the moves are processed if they do not interfere with other moves
	 * 
	 * @param round The current simulation round number
	 * @return True if any move was processed, false if the simulation is stable.
	 *   That is, it will return false if no elf want to move.
	 */
	protected boolean sim( int round ) {		
		// first determine the action each elf takes
		final List<ElfMove> moves = new ArrayList<>( );
		for( final Coord2D elf : grid.getKeys( ) ) {
			// no action if I have no neighbours at all
			if( elf.getAdjacent( true ).stream( ).mapToInt( c -> grid.hasValue( c ) ? 1 : 0 ).sum( ) == 0 ) continue;
			
			// propose move in every direction
			for( int i = 0; i < 4; i++ ) {
				final Direction d = DIRS[(i + round) % DIRS.length];
				
				// check if the elves has no neighbour in this direction
				final Coord2D m = elf.moveDir( d, 1 );
				if( grid.hasValue( m ) || grid.hasValue( m.moveDir( d.turn( -1 ), 1 ) ) || grid.hasValue( m.moveDir( d.turn( 1 ), 1 ) ) ) continue;
				
				// nope, propose move
				moves.add( new ElfMove( elf, m ) );
				break;
			}
		}
			
		// then try and process the moves
		boolean moved = false;
		for( final ElfMove m1 : moves ) {			
			// check if another move may be in the way
			boolean contains = false;;
			for( final ElfMove m2 : moves ) {
				if( !m1.from.equals( m2.from ) && m2.to.equals( m1.to ) ) {
					contains = true;
					break;
				}
			}
			
			// nope, process it!
			if( !contains ) {
				moved = true;
				grid.unset( m1.from );
				grid.set( m1.to, true );
			}
		}
		
		return moved;
	}

	/** @return The grid that holds the current state of the simulation */
	@Override
	public String toString( ) {
		return grid.toString( c -> c ? "#" : "." );
	}
	
	/** Simple container to hold a proposed move */
	private class ElfMove {
		/** The coordinate to move from */
		protected final Coord2D from;
		
		/** The coordinate to move to */
		protected final Coord2D to;
		
		/**
		 * Creates a new move
		 * 
		 * @param from The coordinate to move from
		 * @param to The coordinate to move to
		 */
		protected ElfMove( final Coord2D from, final Coord2D to ) {
			this.from = from; 
			this.to = to;
		}
		
		/** @return The string description of the move */
		@Override
		public String toString( ) {
			return from + " => " + to;
		}
	}
}
