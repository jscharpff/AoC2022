package challenges.day23;

import java.util.List;

import aocutil.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day23.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Runs the planting simulation for 10 rounds and returns the number of empty
	 * spaces contained in the area that holds all elves
	 * 
	 * @param input The list of string describing the initial simulation state
	 * @return The number of empty spaces in the elves' area
	 */
	protected static long part1( final List<String> input ) {
		final PlantingSim p = PlantingSim.fromStringList( input );
		return p.run( 10 );
	}
	
	
	/**
	 * Runs the simulation until no elf wants to move
	 * 
	 * @param input The list of string describing the initial simulation state
	 * @return The number of rounds required to achieve the stable situation
	 */
	protected static long part2( final List<String> input ) {
		final PlantingSim p = PlantingSim.fromStringList( input );
		return p.runUntilStable( );
	}
}