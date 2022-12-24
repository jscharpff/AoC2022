package challenges.day24;

import java.util.List;

import aocutil.io.FileReader;

public class Day24 {

	/**
	 * Day 24 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day24.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Navigates through a basin filled with blizzards to reach the exit on the
	 * opposite side
	 * 
	 * @param input The layout of the basin as a list of strings
	 * @return The least number of steps required to reach the exit while still
	 *   avoiding all storms
	 */
	protected static long part1( final List<String> input ) {
		final BlizzardBasin bb = BlizzardBasin.fromStringList( input );
		return bb.navigate( );
	}
	
	
	/**
	 * Oh no, one of the elves forgot his snacks. Hence we navigate to the exit,
	 * back to the entrance to pick up the snacks and go back to the exit again,
	 * all the while avoiding the blizzards.
	 * 
	 * @param input The layout of the basin as a list of strings
	 * @return The least number of steps required to navigate the basin twice
	 */
	protected static long part2( final List<String> input ) {
		final BlizzardBasin bb = BlizzardBasin.fromStringList( input );
		return bb.navigateTwice( );
	}
}