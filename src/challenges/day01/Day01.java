package challenges.day01;

import java.util.ArrayList;
import java.util.List;

import aocutil.io.FileReader;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day01.class.getResource( "example.txt" ) ).readLineGroups( "," );
		final List<String> input = new FileReader( Day01.class.getResource( "input.txt" ) ).readLineGroups( "," );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + mostCalories( ex_input, 1 ) );
		System.out.println( "Answer : " + mostCalories( input, 1 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + mostCalories( ex_input, 3 ) );
		System.out.println( "Part 2 : " + mostCalories( input, 3 ) );
	}
	
	/**
	 * Sums the weights of the top x elves that hold the most total snack weight
	 *   
	 * @param input The list of snacks per elf, comma-separated
	 * @param entries The number of entries to sum over 
	 * @return The sum of the weights of the x entries that hold the most weight,
	 *   sorted from most to least weight
	 */
	private static long mostCalories( final List<String> input, final int entries ) {
		// create array of elves, each holding the sum of their snack weights
		final List<Elf> elves = new ArrayList<>( );
		for( String i : input ) elves.add( new Elf( i ) );
		
		// and return the sum of the top x weights
		return elves.stream( ).sorted( (x,y) -> Long.compare( y.getWeight( ), x.getWeight( ) ) )
				.limit( entries ).mapToLong( Elf::getWeight ).sum( );
	}
}