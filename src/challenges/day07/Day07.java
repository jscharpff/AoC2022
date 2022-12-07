package challenges.day07;

import java.util.List;

import aocutil.io.FileReader;

public class Day07 {

	/**
	 * Day 7 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day07.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day07.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Reconstructs the file system from the command line output and then finds
	 * the sum of all directories that have a maximum size of 100k.
	 * 
	 * @param input The file system command line output
	 * @return The sum of directory sizes that are smaller than 100k
	 */
	protected static long part1( final List<String> input ) {
		final FileSystem fs = FileSystem.fromCMD( input );
		return fs.sumMaxSize( 100000 );
	}
	
	
	/**
	 * Reconstructs the file system from the command line output and then finds
	 * the smallest folder that can be removed to free up sufficient space for
	 * the upgrade we plan to do.
	 * 
	 * @param input The file system command line output
	 * @return The size of the smallest directory that will free up enough space
	 *   if deleted 
	 */
	protected static long part2( final List<String> input ) {
		final FileSystem fs = FileSystem.fromCMD( input );
		return fs.minSpaceToFree( 70000000, 30000000 );
	}
}