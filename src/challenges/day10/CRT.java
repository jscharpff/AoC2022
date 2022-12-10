package challenges.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A CRT-based display with a single horizontal register and cyclic CPU that
 * can output pixels on a screen
 * 
 * @author Joris
 */
public class CRT {
	/** The value of the X register over time */
	protected Map<Integer, Integer> X;
	
	/** The amount of cycles processed */
	protected int cycles;
	
	/**
	 * Creates a new CRT-based display
	 * 
	 * @param initval The initial value for the X register
	 */
	public CRT( final int initval ) {
		this.X = new HashMap<>( );
		X.put( 0, initval );
		cycles = 1;
	}
	
	/**
	 * Processes the list of CRT commands and stores the value of X for all of the
	 * cycles that occur during the processing in a list
	 * 
	 * @param input The commands to process
	 */
	public void processInput( final List<String> input ) {
		int cycle = 0;
		int lastval = X.get( cycle );
		
		for( final String s : input ) {			
			// process the command
			if( s.equals( "noop" ) ) {
				// no-op, X value is unchanged for this cycle
				cycle++;
			} else if( s.startsWith( "addx" ) ) {
				// addx, performs a noop and then changes the value for X in the next cycle
				cycle += 2;
				lastval += Integer.parseInt( s.split( " " )[1] );
				X.put( cycle, lastval );
			} else {
				throw new RuntimeException( "Unknown command: " + s );
			}
		}
		
		// store the total number of cycles that have occurred
		cycles = cycle;		
	}
	
	/**
	 * Retrieves the value of register X for a given cycle by going through the
	 * cycle history and returning the last set value for X starting from the
	 * specified cycle and going back.
	 * 
	 * @param cycle The cycle to get the value for
	 * @return The value of the X register at the given cycle
	 */
	private int getX( final int cycle ) {
		if( cycle < 0 ) throw new RuntimeException( "Invalid value for cycle " + cycle );
		
		// start at the given cycle and go back in X value history until we find
		// the last value change
		int idx = cycle;
		while( !X.containsKey( idx ) ) idx--;
		return X.get( idx );
	}
	
	/**
	 * Computes the signal strength after the input commands have been processed
	 * 
	 * @return The signal strength of the pixels at intervals 20 + 40 * n
	 */
	public long getSignalStrengths( ) {
		if( cycles <= 1 ) throw new RuntimeException( "The commands have not been processed yet!" );
		long sum = 0;
		for( int i = 20; i <= 220; i+= 40 ) sum += getX( i - 1 ) * i;
		return sum;
	}
	
	/**
	 * Determines the pixels to draw given the position of the X register in
	 * every cycle of the processing.
	 * 
	 * @return The output on the screen as a list of rows with pixels
	 */
	public List<String> draw( ) {
		if( cycles <= 1 ) throw new RuntimeException( "The commands have not been processed yet!" );
		final List<String> output = new ArrayList<>( );
		final int ROWSIZE = 40;
		
		// go over all values and test whether we should draw a pixel or not
		StringBuilder row = new StringBuilder( );
		for( int i = 0; i < cycles; i++ ) {
			if( i > 0 && i % ROWSIZE == 0 ) { output.add( row.toString( ) ); row = new StringBuilder( ); }

			// the sprite position on the row and pixel we want to draw
			final int x = getX( i ) % ROWSIZE;
			final int cycle = i % ROWSIZE;
			
			// set pixel value to on only if it is within range of the sprite
			row.append( Math.abs( cycle - x ) <= 1 ? '#' : '.' );
		}
		
		// add remainder in the row buffer and return the list
		output.add( row.toString( ) );
		return output;
	}
}
