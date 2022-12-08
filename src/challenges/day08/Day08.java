package challenges.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day08.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Computes the number of trees that are visible in the given forest. A tree
	 * is visible if at least one line of sight to it is not blocked by a higher
	 * tree.
	 * 
	 * @param input The list of strings that describe the grid of tree heights
	 * @return The number of visible trees
	 */
	protected static long part1( final List<String> input ) {
		final Forest f = Forest.fromGrid( input );
		return f.countVisible( );
	}
	
	
	/**
	 * Finds the maximum viewing distance score over all tree positions. The
	 * VDS is computed by multiplying the view distance in every direction from
	 * all of the trees
	 * 
	 * @param input The list of strings that describe the grid of tree heights
	 * @return The highest viewing distance score
	 */
	protected static long part2( final List<String> input ) {
		final Forest f = Forest.fromGrid( input );
		return f.maxViewScore( );
	}
}