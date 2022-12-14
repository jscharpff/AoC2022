package challenges.day13;

import java.util.List;

import aocutil.io.FileReader;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "example.txt" ) ).readLineGroups( ";" );
		final List<String> input = new FileReader( Day13.class.getResource( "input.txt" ) ).readLineGroups( ";" );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Checks for every pair of packets whether they are correctly ordered 
	 * according to the ruling in the receiver.
	 * 
	 * @param input The packet pairs as semicolon-separated strings 
	 * @return The sum of pair indexes that are correctly ordered
	 */
	protected static long part1( final List<String> input ) {
		final PacketReceiver p = new PacketReceiver( );
		p.addPacketPairs( input );
		return p.checkOrdered( );
	}
	
	
	/**
	 * Inserts two new packets into the receiver, orders the packets based upon
	 * the previous ruling and returns the product of indexes at which the new
	 * packets end up
	 * 
	 * @param input The packet pairs as semicolon-separated strings 
	 * @return The product of the indexes of the inserted packets after ordering
	 */
	protected static long part2( final List<String> input ) {
		final String p2 = "[[2]]";
		final String p6 = "[[6]]";
		
		final PacketReceiver p = new PacketReceiver( );
		p.addPacketPairs( input );
		p.addPackets( p2, p6 );
		p.sort( );
		return (p.getPacketIndex( p2 ) + 1) * (p.getPacketIndex( p6 ) + 1);
	}
}