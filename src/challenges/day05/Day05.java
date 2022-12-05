package challenges.day05;

import java.util.List;

import aocutil.io.FileReader;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day05.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day05.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + reorder( ex_input, true ) );
		System.out.println( "Answer : " + reorder( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + reorder( ex_input, false ) );
		System.out.println( "Answer : " + reorder( input, false ) );
	}
	
	/**
	 * Reorders crates in the cargo hold using a given set of move instructions
	 * 
	 * @param input The crate stacks and moves
	 * @param version9000 true to use CrateMover 9000, false for CrateMover 9001
	 * @return The letters on the crates that are on top after all moves
	 */
	private final static String reorder( final List<String> input, final boolean version9000 ) {
		// create the CargoHold with the required version of CrateMover 
		final CargoHold cargo = new CargoHold( version9000 );
		
		// first initialise all crate stacks from the input
		int idx = 0;
		while( idx < input.size( ) ) {
			final String in = input.get( idx++ );
			if( in.length( ) == 0 ) break; // stop initialising stacks if we find an empty line
			
			// add a new stack of crates
			cargo.addStack( in.split( " " )[1] );
		}
		
		// then process all moves
		while( idx < input.size( ) ) cargo.move( input.get( idx++ ) );
		
		// and finally return the crates that are on top
		return cargo.getOnTop( );
	}
}