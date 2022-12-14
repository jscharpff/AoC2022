package challenges.day14;

import java.util.List;

import aocutil.io.FileReader;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day14.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day14.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + simParticles( ex_input, true ) );
		System.out.println( "Answer : " + simParticles( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + simParticles( ex_input, false ) );
		System.out.println( "Answer : " + simParticles( input, false ) );
	}
	
	/**
	 * Simulates the falling of sand particles onto rocks until (part 1) they
	 * fall through to the infinite floor or (part 2) they are stacked an block
	 * the entrance.  
	 * 
	 * @param input The list of strings describing the rock layout
	 * @param infiniteFloor True to use an infinite floor, false to have a floor
	 *   at fixed Y value (2 lower than lowest rock)
	 * @return If no floor is used, this function returns the number of particles
	 *   that have fallen before the first particle fell through into the
	 *   infinite void. If false, the function returns the number of particles
	 *   that have fallen until the entrance was blocked. 
	 */
	protected static long simParticles( final List<String> input, final boolean infiniteFloor ) {
		final SandBox sb = SandBox.fromStringList( input );
		return sb.sim( infiniteFloor );
	}
}