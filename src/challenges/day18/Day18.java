package challenges.day18;

import java.util.List;

import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day18.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day18.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Computes the number of exposed surfaces by checking the cubes in the
	 * system and counting their (lack of) neighbours
	 * 
	 * @param input The cube system
	 * @return The total exposed surface count
	 */
	protected static long part1( final List<String> input ) {
		final CubeSystem C = CubeSystem.fromStringList( input );
		return C.getSurfaceArea( );
	}
	
	
	/**
	 * Computes the number of surfaces that are exposed to the outside, thus not
	 * including the surfaces that are enclosed by the cube system
	 * 
	 * @param input The cube system
	 * @return The number of externally exposed surfaces
	 */
	protected static long part2( final List<String> input ) {
		final CubeSystem C = CubeSystem.fromStringList( input );
		return C.getExternalSurfaceArea( );
	}
}