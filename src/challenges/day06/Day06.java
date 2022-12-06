package challenges.day06;

import java.util.List;

import aocutil.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day06.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day06.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + findInBuffer( s, 4 ) );
		System.out.println( "Answer : " + findInBuffer( input.get( 0 ), 4 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		for( final String s : ex_input )
			System.out.println( "Example: " + findInBuffer( s, 14 ) );
		System.out.println( "Answer : " + findInBuffer( input.get( 0 ), 14 ) );
	}

	/**
	 * Finds a signal marker in the buffer that is given by the first time any
	 * sequence of given size of unique characters is found.  
	 * 
	 * @param input The input buffer
	 * @param size The size of the unique sequence to find
	 * @return The first index after the unique sequence of requested size 
	 */
	protected static long findInBuffer( final String input, final int size ) {
		String buff = input.substring( 0, size );
		int idx = size - 1;
		while( ++idx < input.length( ) ) {
			// check if we found a marker
			if( checkUnique( buff ) ) return idx;
			
			// no, are we done
			if( idx == input.length( ) - 1 ) break;
			
			// check next char
			buff = buff.substring( 1 ) + input.charAt( idx );
		}
		return -1;
	}
	
	/**
	 * Checks if all characters are unique in a buffer string
	 * 
	 * @param str the buffer string
	 * @return True if no character occurs twice, false otherwise
	 */
	protected static boolean checkUnique( final String str ) {
		// do a simple pairwise check to find duplicate characters
		for( int i = 0; i < str.length( ); i++ )
			for( int j = i + 1; j < str.length( ); j++ )
				if( str.charAt( i ) == str.charAt( j ) ) return false;
		
		return true;
	}
}