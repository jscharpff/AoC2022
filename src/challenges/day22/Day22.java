package challenges.day22;

import java.util.ArrayList;
import java.util.List;

import aocutil.io.FileReader;
import challenges.day22.monkeymap.MonkeyCube;
import challenges.day22.monkeymap.MonkeyMap;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day22.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Navigates a MonkeyMap using a predefined list of movements
	 * 
	 * @param input The map layout and list of movements as a list of strings
	 * @return The coordinate and direction code after processing all moves
	 */
	protected static long part1( final List<String> input ) {
		final List<String> map = new ArrayList<>( input );
		for( int i = 0; i < 2; i++ ) map.remove( map.size( ) - 1 );
		final MonkeyMap mm = MonkeyMap.fromStringList( map );
		return mm.navigate( input.get( input.size( ) - 1 ) );
	}
	
	
	/**
	 * Same as part 1 but now we are actually navigating a cubic map, with a
	 * different type of wrap around when going off-grid
	 * 
	 * @param input The map layout and list of movements as a list of strings
	 * @return The coordinate and direction code after processing all moves
	 */
	protected static long part2( final List<String> input ) {
		final List<String> map = new ArrayList<>( input );
		for( int i = 0; i < 2; i++ ) map.remove( map.size( ) - 1 );
		final MonkeyCube mm = MonkeyCube.fromStringList( map );
		return mm.navigate( input.get( input.size( ) - 1 ) );	}
}