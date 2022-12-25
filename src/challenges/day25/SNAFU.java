package challenges.day25;

/**
 * Class to encode/decode "Special Numeral-Analogue Fuel Units" (SNAFU) numbers
 * into base 10 numbers and vice versa
 * 
 * @author Joris
 */
public class SNAFU {
	/**
	 * Converts a SNAFU number into a regular number
	 * 
	 * @param input The SNAFU number
	 * @return The number in base-10
	 */
	public static long decode( final String input ) {
		final int N = input.length( );
		long num = 0;
		
		for( int i = 0; i < N; i++ ) {
			final long p = (long) Math.pow( 5.0, i );
			final char ch = input.charAt( N - i - 1 );
			switch( ch ) {
				case '=': num -= 2 * p; break;
				case '-': num -= p; break;
				case '0': break; 
				case '1': num += p; break;
				case '2': num += 2 * p; break;
				default: throw new IllegalArgumentException( "Illegal character in SNAFU number: " + ch );
			}
		}
		
		return num;
	}
	
	/**
	 * Converts a regular number into a SNAFU number
	 * 
	 * @param number The regular, base 10 number to convert
	 * @return The SNAFU encoding 
	 */
	public static String encode( final long num ) {
		// find first power of 5 that exceeds the number if multiplied twice
		int n = 0;
		while( Math.pow( 5, (++n + 1) ) / 2 < num ) { }
		
		// then encode the number by working backwards
		return encode( "", n, num );	
	}
	
	/**
	 * Recursive function to encode a number into a SNAFU number, one power of 5
	 * at the time
	 * 
	 * @param curr The currently encoded number
	 * @param n The power of five to consider now
	 * @param num The (remainder) of the number to encode
	 * @return The SNAFU encoding of the (remainder) of the number
	 */
	private static String encode( final String curr, final int n, final long num ) {
		// last bit can be encoded directly
		if( n < 1 ) return curr + toChar( (int)num );
		
		// get the current power of 5 and the value of half this power, used as
		// threshold
		final long p = (long)Math.pow( 5, n );
		final long half = Long.divideUnsigned( p, 2 );
		final int v;

		// determine if we need one or two (negative) amounts of the current power
		if( num > half ) {
			// if the number is bigger than half, we need at least one of this power
			v = (int)Math.min( Math.round( num / (double)p ), 2 );
		} else if( num < -half ) { 
			// the same applies if the negative number if smaller than the negative half
			v = (int)Math.max( -Math.round( num / (double)-p ), -2 );
		} else {
			// if neither applies, we do not need this power of 5 in the encoding
			v = 0;
		}

		// continue the process with the next bit
		return encode( curr + toChar( v ), n - 1, num - (v * p) );
	}
	
	/**
	 * Converts an integer value in the [-2, 2] range to the SNAFU character
	 * 
	 * @param v The value
	 * @return The SNAFU-encoded character
	 */
	private static char toChar( final int v ) {
		switch( v ) {
			case -2: return '=';
			case -1: return '-';
			case  0: return '0';
			case  1: return '1';
			case  2: return '2';
			default: throw new IllegalArgumentException( "Invalid number to SNAFU: " + v );
		}
	}

}
