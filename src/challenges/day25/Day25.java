package challenges.day25;

import java.util.List;

import aocutil.io.FileReader;

public class Day25 {

	/**
	 * Day 25 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day25.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day25.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );
	}
	
	/**
	 * Decodes all numbers in the input, sums them and encodes the result into SNAFU
	 * 
	 * @param input The list of SNAFU numbers to sum
	 * @return The sum of SNAFU numbers, again encoded in SNAFU
	 */
	protected static String part1( final List<String> input ) {
		return SNAFU.encode( input.stream( ).mapToLong( SNAFU::decode ).sum( ) );
	}
}