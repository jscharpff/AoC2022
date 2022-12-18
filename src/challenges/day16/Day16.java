package challenges.day16;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day16.volcano.Volcano;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day16.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day16.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Finds the maximum pressure that can be released within 30 minutes in the
	 * volcano system described by the input string.
	 * 
	 * @param input The volcano system
	 * @return The maximum pressure that can be released
	 */
	protected static long part1( final List<String> input ) {
		final Volcano v = Volcano.fromStringList( input );
		return v.findMaxPressure( 30, "AA" );
	}
	
	
	/**
	 * Finds the maximum pressure that can be released in the volcano system
	 * described by the input string, now within 26 minutes but with the help of
	 * an elephant.
	 * 
	 * @param input The volcano system
	 * @return The maximum pressure that can be released
	 */
	protected static long part2( final List<String> input ) {
		final Volcano v = Volcano.fromStringList( input );
		return v.findMaxPressure( 26, "AA", "AA" );
	}
}