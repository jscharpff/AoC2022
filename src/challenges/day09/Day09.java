package challenges.day09;

import java.util.List;

import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "example.txt" ) ).readLines( );
		final List<String> ex2_input = new FileReader( Day09.class.getResource( "example2.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day09.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + simRopeBridge( ex_input, 2 ) );
		System.out.println( "Answer : " + simRopeBridge( input, 2 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + simRopeBridge( ex2_input, 10 ) );
		System.out.println( "Answer : " + simRopeBridge( input, 10 ) );
	}
	
	/**
	 * Simulates the movement of a series of knots in a rope bridge
	 * 
	 * @param input The moves of the head knot
	 * @param length The length of the rope bridge
	 * @return The number of unique positions that are visited by the tail knot
	 */
	protected static long simRopeBridge( final List<String> input, final int length ) {
		final RopeSim rs = new RopeSim( length );
		return rs.processMoves( input );
	}
}