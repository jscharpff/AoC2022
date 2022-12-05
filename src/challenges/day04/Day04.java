package challenges.day04;

import java.util.List;

import aocutil.io.FileReader;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day04.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + count( ex_input, true ) );
		System.out.println( "Answer : " + count( input, true )  );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + count( ex_input, false ) );
		System.out.println( "Answer : " + count( input, false ) );
	}
	
	/**
	 * Counts the number of range overlaps in the input pairs
	 * 
	 * @param input List of range pairs a-b,c-d
	 * @param contained True if the overlap must fully contain the other
	 * @return The count of overlaps
	 */
	private final static long count( final List<String> input, final boolean contained ) {
		return input.stream( ).mapToInt( 
				in -> {
					final String[] s = in.split( "," );
					final Range r1 = Range.fromString( s[0] );
					final Range r2 = Range.fromString( s[1] );
					
					if( contained )
						return r1.contains( r2 ) || r2.contains( r1 ) ? 1 : 0;
					else
						return r1.overlaps( r2 ) ? 1 : 0;
				}
			).sum( );
	}
}