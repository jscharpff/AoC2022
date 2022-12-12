package challenges.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day12.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day12.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Finds the shortest path from the starting position to the end coordinate
	 * in a maze of rocks with varying height
	 * 
	 * @param input The rock maze heights
	 * @return The shortest distance from start to end
	 */
	protected static long part1( final List<String> input ) {
		final HeightMap map = HeightMap.fromString( input );
		return map.findShortestPath( );
	}
	
	
	/**
	 * Finds the shortest possible path length from any zero-elevation starting
	 * point to the end coordinate in the rock maze
	 * 
	 * @param input The rock maze
	 * @return The shortest possible path length from any maze coordinate at
	 *   elevation level zero to the end coordinate 
	 */
	protected static long part2( final List<String> input ) {
		final HeightMap map = HeightMap.fromString( input );
		return map.findBestStartPath( );
	}
}