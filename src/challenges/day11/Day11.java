package challenges.day11;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day11.monkeysim.MonkeySim;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day11.class.getResource( "example.txt" ) ).readLineGroups( ";" );
		final List<String> input = new FileReader( Day11.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + simMonkeyBusiness( ex_input, true, 20 ) );
		System.out.println( "Answer : " + simMonkeyBusiness( input, true, 20 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + simMonkeyBusiness( ex_input, false, 10000 ) );
		System.out.println( "Answer : " + simMonkeyBusiness( input, false, 10000 ) );
	}
	
	/**
	 * Runs the simulation of monkeys throwing items to each other based upon
	 * worry scores
	 * 
	 * @param input The input describing the monkey's initial inventory and their
	 *   logic for picking the target of a throw
	 * @param limiter True to enable worry score limiter that divides the worry
	 *   score for items by 3 after every inspection, false to disable it
	 * @param rounds The number of rounds to simulate
	 * @return The product of the number of times that items were inspected by
	 *   the two monkeys that threw the most items during the simulation
	 */
	protected static long simMonkeyBusiness( final List<String> input, final boolean limiter, final int rounds ) {
		final MonkeySim sim = MonkeySim.fromString( input );
		sim.setWorryLimit( limiter );
		sim.run( rounds );
		return sim.getMostInspected( 2 );
	}
}