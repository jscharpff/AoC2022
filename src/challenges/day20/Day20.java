package challenges.day20;

import java.util.List;

import aocutil.io.FileReader;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day20.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Decrypts the message using the Mixed Encryption algorithm
	 * 
	 * @param input The input message to decrypt, given by a list of integers
	 * @return The value of the decrypted message
	 */
	protected static long part1( final List<String> input ) {
		final MixedDecryptor me = MixedDecryptor.fromStringList( input );
		me.decrypt( );
		return me.getDecrypted( );
	}
	
	
	/**
	 * Decrypts the message using the Mixed Encryption algorithm but now using
	 * a predefined encryption key and after running the encryption algorithm 10
	 * times.
	 * 
	 * @param input The input message to decrypt, given by a list of integers
	 * @return The value of the decrypted message
	 */
	protected static long part2( final List<String> input ) {
		final MixedDecryptor me = MixedDecryptor.fromStringList( input );
		me.applyKey( 811589153 );
		for( int i = 0; i < 10; i++ ) me.decrypt( );
		return me.getDecrypted( );
	}
}