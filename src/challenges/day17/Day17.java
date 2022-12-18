package challenges.day17;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day17.tetris.Tetris;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> shapes = new FileReader( Day17.class.getResource( "shapes.txt" ) ).readLineGroups( ";" );
		final String ex_input = new FileReader( Day17.class.getResource( "example.txt" ) ).readLines( ).get( 0 );
		final String input = new FileReader( Day17.class.getResource( "input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + tetris( shapes, ex_input, 2022 ) );
		System.out.println( "Answer : " + tetris( shapes, input, 2022 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + tetris( shapes, ex_input, 1000000000000l ) );
		System.out.println( "Answer : " + tetris( shapes, input, 1000000000000l ) );
	}
	
	/**
	 * Runs the tetris game for the given rounds and determines the height of the
	 * resulting shape stack. Each round means dropping a tetris shape until it
	 * comes to rest.
	 * 
	 * @param shapes The set of available tetris shapes
	 * @param input The movements to perform in the game
	 * @param rocks The number of rocks to drop
	 * @return The height of the resulting stack after 2022 rounds
	 */
	protected static long tetris( final List<String> shapes, final String input, final long rocks ) {
		final Tetris T = Tetris.fromShapeList( shapes );
		return T.run( input, rocks );
	}
}