package challenges.day13;

import java.util.ArrayList;
import java.util.List;

/**
 * A single data packet that may contain an integer value or a list of child
 * packets
 *  
 * @author Joris
 */
public class Packet implements Comparable<Packet> {
	/** The list of child packets, null if it holds an integer value */
	private final List<Packet> data;

	/** The integer value, -1 if it holds a list of child packets */ 
	private final int value;

	/**
	 * Creates a new packet that holds a list of child packets
	 * 
	 * @param data The list of child packets
	 */
	private Packet( final List<Packet> data ) {
		this.data = new ArrayList<>( data );
		this.value = -1;
	}

	/**
	 * Creates a new packet with an integer value
	 * 
	 * @param value The value it holds
	 */
	private Packet( final int value ) {
		this.data = null;
		this.value = value;
	}
	
	/** 
	 * @return True if this packet holds a list of sub packets, false if it
	 *   represents an integer value  
	 */
	public boolean isList( ) { return this.data != null; }
	
	/** @return The size of the packet */
	public int size( ) { return isList( ) ? data.size( ) : 1; }
	
	/**
	 * Returns the packet contents is in list form, even though it holds an
	 * integer value
	 * 
	 * @return The packet with a list as its data 
	 */
	private Packet toList( ) {
		if( isList( ) ) return this;
		
		// make sure that its integer value is in a list
		final List<Packet> list = new ArrayList<>( );
		list.add( this );
		return new Packet( list );
	}
	
	/**
	 * Compares this packet to another specified one to determine ordering
	 * 
	 * @param other The packet to compare to
	 * @return -1 if the other packet should be first, 0 if their order is equal
	 *   and 1 if this packet should be ordered first 
	 */
	@Override
	public int compareTo( final Packet other ) {
		// compare integer values?
		if( !isList( ) && !other.isList( ) ) {
			return Integer.signum( other.value - value );
		}
		
		// make sure both sides are list to compare further
		final Packet l = toList( );
		final Packet r = other.toList( );
		for( int i = 0; i < Math.max( l.size( ), r.size( ) ); i++ ) {
			// left ran out, so that should be ordered first
			if( i >= l.size( ) ) return 1;
			
			// right ran out, right should be ordered first
			if( i >= r.size( ) ) return -1;
			
			// both still have elements, compare them
			final int res = l.data.get( i ).compareTo( r.data.get( i ) );
			if( res != 0 ) return res;
		}
		
		// both pairs are exhausted, no result yet
		return 0;
	}
	
	/**
	 * Reconstructs the packet data from a string. This procedure will recurse if
	 * its data is a list of packets
	 * 
	 * @param str The packet data to parse
	 * @return The reconstructed packet
	 */
	public static Packet fromString( final String str ) {
		if( str.charAt( 0 ) == '[' ) {
			return parseList( str.substring( 1, str.length( ) - 1 ) );
		} else {
			return new Packet( Integer.parseInt( str ) );
		}
	}
	
	/**
	 * Parses a string that describes a list of packets
	 * 
	 * @param str The input string
	 * @return The list with parsed packets
	 */
	private static Packet parseList( final String str ) {
		// parse by scanning over the string and processing all elements between
		// commas at depth 0, i.e., not within a sub list
		final List<Packet> elems = new ArrayList<>( );
		int depth = 0;
		int lastidx = 0;
		int idx = -1;
		
		// add a comma to the end to include the last part in parsing
		final String s = str + ",";
		
		// scan until end of string
		while( ++idx < s.length( ) ) {
			final char c = s.charAt( idx );
			
			if( c == '[' ) depth++;
			else if( c == ']' ) depth--;
			else if( c == ',' ) {
				// we found a comma, parse it if this is at depth 0
				if( depth == 0 && lastidx != idx ) {
					elems.add( Packet.fromString( s.substring( lastidx, idx ) ) );
					lastidx = idx + 1;
				}
			}
		}
		
		// create the packet and return it
		return new Packet( elems );
	}
	
	/** @return The string that (recursively) describes the packet contents */
	@Override
	public String toString( ) {
		return isList( ) ? data.toString( ) : "" + value;
	}

	/**
	 * Compares this packet to another one
	 * 
	 * @param obj The object to compare against
	 * @return True iff both are packets and their contents are equal  
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( obj == null || !(obj instanceof Packet) ) return false;
		final Packet p = (Packet)obj;
		
		return toString( ).equals( p.toString( ) );
	}
}
