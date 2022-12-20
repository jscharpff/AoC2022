package challenges.day20;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is able to decrypt messages sent with the Mixed Encryption
 * protocol, used by Santa's elves
 * 
 * @author Joris
 */
public class MixedDecryptor {
	/** The input values fed to the decryption algorithm */
	protected final List<Long> values;
	
	/** The current indexes of the values */
	protected final LinkedList<Integer> indexes;

	/**
	 * Creates a new MixedDecryptor with the given input message numbers
	 * 
	 * @param input The numbers to decrypt
	 */
	private MixedDecryptor( final List<Long> input ) {
		this.values = new ArrayList<>( input );

		// create list of indexes that hold the index of original element 
		indexes = new LinkedList<>( );
		for( int i = 0; i < size( ); i++ ) indexes.add( i );
	}
	
	/**
	 * Creates a new decryption run from a list of encrypted inputs
	 * 
	 * @param input The list of inputs
	 * @return The MixedDecryptor with the values as its input
	 */
	public static MixedDecryptor fromStringList( final List<String> input ) {
		final List<Long> in = new ArrayList<>(  );
		for( final String s : input) in.add( Long.parseLong( s ) ); 
		return new MixedDecryptor( in );
	}
	
	/**
	 * Applies the specified encryption key to all values in the input
	 * 
	 * @param key The encryption key
	 */
	public void applyKey( final long key ) {
		for( int i = 0; i < size( ); i++ )
			values.set( i, values.get( i ) * key );
	}
	
	/**
	 * Runs the decryption algorithm once
	 */
	public void decrypt( ) {		
		// start the decryption process
		for( int i = 0; i < size( ); i++ ) {
			// fond the node that now holds the index
			final LLNode<Integer> old = indexes.find( i );
			
			// compute new index and keep it within list bounds
			final long move = values.get( i );
			indexes.shift( old, move );
		}
	}
	
	/**
	 * @return The value of the decrypted message
	 */
	public long getDecrypted( ) {		
		// produce the sum of values at the positions 1000, 2000 and 3000 after
		// value 0
		final List<Long> V = toValues( );
		final int idx0 = V.indexOf( 0l );
		long sum = 0;
		for( final int i : new int[] { 1000, 2000, 3000 } )
			sum += V.get( (idx0 + i) % V.size( ) );
		return sum;
	}
	
	/**
	 * Converts the indexes list into list of values they correspond to
	 * 
	 * @return The list of input values in their current order, possibly affected
	 *   by the decryption algorithm 
	 */
	protected List<Long> toValues( ) {
		final List<Long> V = new ArrayList<>( indexes.size( ) );
		for( final int idx : indexes.toList( ) ) {
			V.add( values.get( idx ) );			
		}
		return V;
	}
	
	/** @return The size of the input message */
	protected int size( ) {
		return values.size( );
	}
}
