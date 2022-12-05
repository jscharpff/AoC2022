package challenges.day05;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import aocutil.string.RegexMatcher;

public class CargoHold {
	/** The version of the crate is 9000? */
	private final boolean is9000;
	
	/** The stacks with cargo crates*/
	private final List<Stack<Character>> stacks;
	
	/**
	 * Creates a new, empty Cargo hold
	 * 
	 * @param is9000 Is this the 9000 or 9001 version?
	 */
	public CargoHold( final boolean is9000 ) {
		this.stacks = new ArrayList<>( );
		this.is9000 = is9000;
	}

	/**
	 * Adds a single stack, initialised with the list of crate names
	 * 
	 * @param crates The crate names to add
	 */
	public void addStack( final String crates ) {
		final Stack<Character> S = new Stack<>( );
		for( final char c : crates.toCharArray( ) ) S.push( c );		
		stacks.add( S );
	}
	
	/**
	 * Performs a single crate move, as described by the action string. The move
	 * action differs for the CraneMover version. The 9000 does them one by one,
	 * while the 9001 can move a stack of crates at ones
	 * 
	 * @param action The move to perform
	 */
	public void move( final String action ) {
		final RegexMatcher rm = RegexMatcher.match( "move #D from #D to #D", action );
		
		if( is9000 ) {
			// move crates one by one
			for( int i = 0; i < rm.getInt( 1 ); i++ )
				stacks.get( rm.getInt( 3 ) - 1 ).push( stacks.get( rm.getInt( 2 ) - 1 ).pop( ) );
		} else {
			// use intermediate stack to still move one by one while preserving the
			// original order
			final Stack<Character> temp = new Stack<>( );
			for( int i = 0; i < rm.getInt( 1 ); i++ )
				temp.push( stacks.get( rm.getInt( 2 ) - 1 ).pop( ) );
			while( !temp.isEmpty( ) )
				stacks.get( rm.getInt( 3 ) - 1 ).push( temp.pop( ) );							
		}
	}
	
	/**
	 * @return String containing the crate letters that are on top of each stack 
	 */
	public String getOnTop( ) {
		final StringBuilder sb = new StringBuilder( );
		for( final Stack<Character> s : stacks ) sb.append( s.peek( ) );
		return sb.toString( );
	}
	
	/**
	 * @return String describing the current cargo hold
	 */	
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		int idx = 1;
		for( final Stack<Character> s : stacks ) {
			sb.append( idx ); sb.append( ' ' );
			sb.append( s.toString( ) );
			sb.append( '\n' );
		}
		sb.deleteCharAt( sb.length( ) - 1 );
		
		return sb.toString( );
	}
}
