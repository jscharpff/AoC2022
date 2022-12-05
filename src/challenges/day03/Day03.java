package challenges.day03;

import java.util.List;

import aocutil.io.FileReader;
import aocutil.string.StringUtil;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day03.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day03.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}

	/**
	 * Computes priority score of a single character
	 * 
	 * @param ch The character
	 * @return It's priority score: a-z -> 1-26 and A-Z -> 27-52
	 */
	private static final long getPriority( final char ch ) {
		return ch < 'a' ? (ch - 'A') + 27 : (ch - 'a') + 1;
	}
	
	/**
	 * Compute total priority score of all items that occur in both halves of the
	 * string-based inventory 
	 * 
	 * @param input List of inventories and their items
	 * @return Sum of priority scores for those items that are in both halves of
	 *   an inventory, one per line
	 */
	private static final long part1( final List<String> input ) {
		long sum = 0;
		for( final String in : input ) {
			// split in halves
			final String[] s = { in.substring( 0, in.length( ) / 2 ), in.substring( in.length( ) / 2 ) };

			// find character in both sides and add to sum (once only)
			final String c = StringUtil.union( s[0], s[1] );
			sum += getPriority( c.charAt( 0 ) );
		}
		
		return sum;
	}
	
	/**
	 * Compute total priority score of all items that occur in every group of
	 * three inventory strings
	 * 
	 * @param input List of inventories and their items
	 * @return Sum of priority scores for those items that are shared in every
	 * group of three inventories
	 */	
	private static final long part2( final List<String> input ) {
		long sum = 0;
		
		// sum over groups of three now
		for( int i = 0; i < input.size( ); i += 3 ) {
			// keep only shared character and add score to sum for that character
			final String union = StringUtil.union( StringUtil.union( input.get( i ), input.get( i + 1 ) ), input.get( i + 2 ) );
			sum += getPriority( union.charAt( 0 ) );
		}
		
		return sum;
	}
}