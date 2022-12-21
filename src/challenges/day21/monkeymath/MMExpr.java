package challenges.day21.monkeymath;

import java.util.Map;

/**
 * Base class for all MonkeyMath expressions
 * 
 * @author Joris
 */
public abstract class MMExpr {
	/** The variable to store the result in */
	protected final String resvar;
	
	/**
	 * Creates a new expression that results in a value for the given variable
	 * 
	 * @param resvar The name of the variable that will hold the result of this
	 *   expression
	 */
	protected MMExpr( final String resvar ) {
		this.resvar = resvar;
	}
		
	/**
	 * Parses an expression from a string description.
	 * 
	 * @param E The mapping of already parsed expressions, required to process
	 *   possible sub-elements
	 * @param input The string to parse
	 * @return The expression or null if not all sub-elements are known yet
	 */
	public static MMExpr parse( final Map<String, MMExpr> E, final String input ) {
		final String[] i = input.split( ": " );
		final String res = i[0];
		final String[] s = i[1].split( " " );
		if( s.length == 3 ) {
			// can we parse this already?
			if( !E.containsKey( s[0] ) || !E.containsKey( s[2] ) ) return null;
			
			// can we compute the answer already?
			final MMExpr v1 = E.get( s[0] );
			final MMExpr v2 = E.get( s[2] );
			final char op = s[1].charAt( 0 );
			final MMFunc f = new MMFunc( res, op, v1, v2 );
			
			// yes, simply return the result then
			if( v1 instanceof MMValue && v2 instanceof MMValue ) {
				return new MMValue( res, f.eval( ) );
			} else {
				// no, hold the expression
				return f;
			}
		} else {
			// simple literal value, we can parse this
			return new MMValue( res, Long.parseLong( s[0] ) );
		}
	}
	
	/**
	 * Evaluates the expression
	 * 
	 * @return The value this expression evaluates to
	 */
	protected abstract long eval( );
	
	/** @return The string description of the expression */
	@Override
	public String toString( ) {
		return toExprString( );
	}
	
	/**
	 * Explicit abstract function that must be implemented by all expressions to
	 * make sure they supply a toString function
	 * 
	 * @return The string description of the expression 
	 */
	protected abstract String toExprString( );
}
