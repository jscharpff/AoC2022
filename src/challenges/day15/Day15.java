package challenges.day15;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day15.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input, 10) );
		System.out.println( "Answer : " + part1( input, 2000000 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input, 20 ) );
		System.out.println( "Answer : " + part2( input, 4000000 ) );
	}
	
	/**
	 * 
	 * @param input
	 * @return 
	 */
	protected static long part1( final List<String> input, final int row ) {
		final SensorArray array = SensorArray.fromStringList( input );
		return array.getCoverage( row, true ).stream( ).mapToInt( Range::size ).sum( );
	}
	
	
	/**
	 * 
	 * @param input
	 * @return 
	 */
	protected static long part2( final List<String> input, final int maxrange ) {
		final SensorArray array = SensorArray.fromStringList( input );
		final Coord2D c = array.getNotCovered( maxrange );
		return (long)c.x * 4000000 + (long)c.y;
	}
}