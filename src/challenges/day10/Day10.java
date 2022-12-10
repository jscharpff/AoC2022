package challenges.day10;

import java.util.List;

import aocutil.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day10.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example:\n" + part2( ex_input ) ); 
		System.out.println( "Answer :\n" + part2( input ) );
	}
	
	/**
	 * Feeds the given input commands to a CRT screen processor and computes
	 * the resulting signal strength of selected pixels.
	 * 
	 * @param input The list of CRT commands to process
	 * @return The resulting signal strength after the processing of the commands
	 */
	protected static long part1( final List<String> input ) {
		final CRT crt = new CRT( 1 );
		crt.processInput( input );
		return crt.getSignalStrengths( );
	}
	
	
	/**
	 * Feeds the given input commands to a CRT screen processor and outputs the
	 * resulting activated pixels as a newline-separated string.
	 * 
	 * @param input The list of CRT commands to process
	 * @return The resulting pixels on the screen
	 */
	protected static String part2( final List<String> input ) {
		// determine active pixels
		final CRT crt = new CRT( 1 );
		crt.processInput( input );
		final List<String> out = crt.draw( );
		
		// return the output as single string with newlines
		final StringBuilder sb = new StringBuilder( );
		for( final String s : out ) {
			sb.append( s );
			sb.append( '\n' );
		}
		return sb.toString( );
	}
}