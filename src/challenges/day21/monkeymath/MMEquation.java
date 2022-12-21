package challenges.day21.monkeymath;

/**
 * Simple equation that holds an expression and constant value
 * 
 * @author Joris
 */
public class MMEquation extends MMExpr {
	/** The expression containing the variable we are trying to find */
	protected final MMExpr expr;
	
	/** The value that it equates to */
	protected final long value;
	
	/**
	 * Creates a new equation with a function/variable as its left hand
	 * expression and a value as its right hand.
	 * 
	 * @param unknown The variable we are trying to solve
	 * @param e1
	 * @param e2
	 */
	protected MMEquation( final String unknown, final MMExpr e1, final MMExpr e2 ) {
		super( unknown );
			
		this.expr = (e1 instanceof MMValue) ? e2 : e1;
		this.value = (e1 instanceof MMValue) ? e1.eval( ) : e2.eval( );
	}
	
	/**
	 * Solves the equation by recursively applying the inverse of the left hand
	 * expression (the function) to the right hand expression (the value) until
	 * only the unknown itself remains.
	 * 
	 * @return The final value for the variable that we are solving for
	 */
	protected long solve( ) {
		return solve( expr, value );
	}
	
	/**
	 * The recursive part of the solve process that inverts the current left hand
	 * term and applies it to the value so far until only the variable remains.
	 * 
	 * @param lhand The current left hand value to inverse
	 * @param currval The value of the right hand, so far
	 * @return The final value for the variable that we are solving for
	 */
	protected long solve( final MMExpr lhand, final long currval ) {
		// if left hand is a variable we are done, otherwise reduce the expression 
		if( lhand instanceof MMVariable ) return currval;
		
		// get expressions in left hand of equation
		final MMExpr e1 = ((MMFunc) lhand).elems.get( 0 );
		final MMExpr e2 = ((MMFunc) lhand).elems.get( 1 );
		final boolean lhs = e2 instanceof MMValue;
				
		// continue the process
		return solve( lhs ? e1 : e2, ((MMFunc)lhand).inverse( currval ) );
	}
	
	/**
	 * Evaluates the equation, but can only be called if the equation is fully
	 * solved.
	 * 
	 * @return The right hand side value of the equation
	 * @throws RuntimeException if the equation is unsolved
	 */
	@Override
	protected long eval( ) {
		if( !(expr instanceof MMVariable ) ) throw new RuntimeException( "Equation is unsolved!" );
		return value;
	}
	
	/** @return The expression as a string */	
	@Override
	protected String toExprString( ) {
		return expr.toExprString( ) + " = " + value;
	}
}
