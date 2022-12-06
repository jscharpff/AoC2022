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
	 * sequence of given size of unique characters is found. This uses a window
	 * based scanning.
	 * 
	 * @param input The input buffer
	 * @param size The size of the unique sequence to find
	 * @return The first index after the unique sequence of requested size 
  */
	protected static long findInBuffer( final String input, final int size ) {
		int moreThanOne = 0;
		final int[] counts = new int[ 26 ];
		int idx = -1;
		
		while( ++idx < input.length( ) ) {
			// add new character, and keep adding until we have reached minimum buffer size
			final int cidx = input.charAt( idx ) - 'a';
			if( ++counts[ cidx ] == 2 ) moreThanOne++;
			if( idx < size ) continue;
			
			// drop the oldest one
			final int oldcidx = input.charAt( idx - size ) - 'a';
			if( --counts[ oldcidx ] == 1 ) moreThanOne--;
			
			if( idx >= size && moreThanOne == 0 ) return idx+1;
		}
		return -1;
	}
}