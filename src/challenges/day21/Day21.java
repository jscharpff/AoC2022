package challenges.day21;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day21.monkeymath.MonkeyMath;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day21.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day21.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Reduces the system of MonkeyMath expressions, given by a list of strings,
	 * and returns the resulting value of the root node 
	 * 
	 * @param input The list of equations in the MonkeyMath system
	 * @return The value of the root node, after reducing all equations
	 */
	protected static long part1( final List<String> input ) {
		final MonkeyMath mm = new MonkeyMath( );
		return mm.reduce( input, "root" );
	}
	
	
	/**
	 * Solves the equation for the root variable to determine the value of the
	 * (single) unknown "humn". 
	 * 
	 * @param input The list of equations in the MonkeyMath system
	 * @return The value for humn such that the root equation is satisfied
	 */
	protected static long part2( final List<String> input ) {
		final MonkeyMath mm = new MonkeyMath( );
				
		// now find the value of the unknown
		return mm.findSingleUnknown( input, "root", "humn" );
	}
}