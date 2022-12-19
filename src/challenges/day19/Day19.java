package challenges.day19;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day19.robotfact.RobotFactory;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day19.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) + "\n" );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) + "\n" );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Determines the quality score of all blueprints by testing the maximum
	 * number of geodes we can produce for every blueprint and combining them
	 * into a quality level
	 * 
	 * @param input The list of blueprints, one per line
	 * @return The quality score after 24 steps of geode collection
	 */
	protected static long part1( final List<String> input ) {
		final RobotFactory rf = RobotFactory.fromStringList( input );
		return rf.getBlueprintQaulityScore( 24 );
	}
	
	
	/**
	 * Determines the product of quality scores of the first 3 blueprints in the
	 * list (or less if there are not sufficient blueprints)
	 * 
	 * @param input The list of blueprints, one per line
	 * @return The product of geode counts that the first 3 blueprints achieve
	 *   within 32 minutes
	 */
	protected static long part2( final List<String> input ) {
		final RobotFactory rf = RobotFactory.fromStringList( input );
		return rf.getMaxGeodeProduct( 32 );
	}
}