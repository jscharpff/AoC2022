package challenges.day13;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A packet receiver that can help in ordering packets
 * 
 * @author Joris
 */
public class PacketReceiver {
	/** The list of packets in the transmission */
	protected final List<Packet> packets;

	/**
	 * Creates a new receiver 
	 */
	public PacketReceiver( ) {
		this.packets = new ArrayList<>( );
	}

	/**
	 * Adds packages to the receiver, formatted as pairs in a list 
	 * 
	 * @param input The list of packet pairs, with packages separated a semicolon
	 */
	public void addPacketPairs( final List<String> input ) {
		for( final String str : input )
			addPackets( str.split( ";" ) );
	}
	
	/**
	 * Adds one or more packets to the receiver by their contents
	 * 
	 * @param pdata The array of packet data, one element per packet 
	 */
	public void addPackets( final String... pdata ) {
		Stream.of( pdata ).map( Packet::fromString ).forEach( packets::add );
	}

	/**
	 * Checks for every pair of packets in the receiver if they are correctly
	 * ordered.
	 *  	
	 * @return The sum of pair indexes that are correctly ordered (base 1)
	 */
	public int checkOrdered( ) {
		int sum = 0;
		for( int i = 0; i < packets.size( ); i += 2 )
			if( packets.get( i ).compareTo( packets.get( i + 1 ) ) == 1 ) sum += (i/2 + 1);
		return sum;
	}
	
	/**
	 * Sorts the packages in the receiver
	 */
	public void sort( ) {
		packets.sort( (x,y) -> y.compareTo( x ) );
	}
	
	/**
	 * Finds the index of the packet that contains the given data
	 * 
	 * @param packetdata The data the packet contains 
	 * @return The index of the packet that contains exactly the specified data,
	 *   -1 if no such packet exists
	 */
	public int getPacketIndex( final String packetdata ) {
		final Packet p = Packet.fromString( packetdata );
		return packets.indexOf( p );
	}
	
	/** @return The list of packets as a newline-separated string */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		for( final Packet p : packets ) {
			sb.append( p.toString( ) );
			sb.append( '\n' );
		}
		return sb.toString( );
	}
}
