package challenges.day02;

import java.util.List;

import aocutil.io.FileReader;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2022
	 * 
	 * https://adventofcode.com/2022/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + countScore( ex_input ) );
		System.out.println( "Answer : " + countScore( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + countScoreOutcome( ex_input ) );
		System.out.println( "Answer : " + countScoreOutcome( input ) );
	}

	/**
	 * Go over all played games and sum the outcome scores when playing the sign
	 * that is instructed by the strategy guide (in the input)
	 * 
	 * @param input The list of rock, paper, scissor games played as pairs
	 *   <in out> that describe the input sign and the one we should play
	 * @return The total of all outcome scores
	 */
	private final static long countScore( final List<String> input ) {
		long result = 0;
		for( final String s : input ) {		
			final int diff = ((s.charAt( 0 ) - 'A') - (s.charAt( 2 ) - 'X') + 3) % 3;
			
			// compute score, start with played symbol and add result score
			result += (s.charAt( 2 ) - 'X') + 1;			
			if( diff == 0 ) result += 3;
			else if( diff == 2 ) result += 6;
		}
		return result;
	}
	
	/**
	 * Go over all played games and sum the outcome scores, now when making sure
	 * to get the outcome instructed by the strategy guide (in the input)
	 * 
	 * @param input The list of rock, paper, scissor games played, now with the
	 *   desired outcome as second parameter of the pair <in outcome>
	 * @return The total of all outcome scores
	 */
	
	private final static long countScoreOutcome( final List<String> input ) {
		long result = 0;
		for( final String s : input ) {
			final int in = s.charAt( 0 ) - 'A';
			
			// update score with the character we should play and the (already known)
			// outcome score
			switch( s.charAt( 2 ) ) {
				case 'X': result += (in + 2) % 3 + 1; break; // loss
				case 'Y': result += in + 1 + 3; break; // draw
				case 'Z': result += (in + 4) % 3 + 1 + 6; break; // win
				
				// this shouldn't happen...
				default: throw new RuntimeException( "Invalid character" );
			}
		}
		return result;
	}
}