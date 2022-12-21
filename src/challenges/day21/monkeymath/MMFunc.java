package challenges.day21.monkeymath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.stream.LongStream;

/**
 * Simple binary function
 * 
 * @author Joris
 */
public class MMFunc extends MMExpr {
	/** The variables in the expression */
	protected final List<MMExpr> elems;
	
	/** The operation */
	protected final char op;
	
	/**
	 * Creates a new expression
	 * 
	 * @param elems The elements in the operation
	 * @param operand The operand of the expression
	 */
	protected MMFunc( final String resvar, final char operand, final MMExpr... elements ) {
		super( resvar );
		this.elems = new ArrayList<>( );
		for( final MMExpr e : elements ) elems.add( e );
		this.op = operand;
	}
	
	/**
	 * Evaluates the expression
	 * 
	 * @return The operand applied to each of its elements
	 */
	@Override
	protected long eval( ) {
		final LongStream values = elems.stream( ).mapToLong( MMExpr::eval );
		
		final LongBinaryOperator func;
		switch( op ) {
			case '+': func = (x,y) -> x + y; break;
			case '-': func = (x,y) -> x - y; break;
			case '*': func = (x,y) -> x * y; break;
			case '/': func = (x,y) -> x / y; break;
			default: throw new RuntimeException( "Unknown operand: " + op );
		}
		
		return values.reduce( func ).orElse( elems.get( 0 ).eval( ) );
	}
	
	/**
	 * Modifies the given value by the inverse of this function. E.g., if the
	 * original function is given by f( x ), this function returns f^-1( x )
	 * 
	 * @param value The value to modify
	 * @return The value after applying the inverse of this function
	 */
	protected long inverse( final long value ) {
		// check which element holds the value and which the expression
		final boolean lhs = elems.get( 1 ) instanceof MMValue;
		final long val = lhs ? elems.get( 1 ).eval( ) : elems.get( 0 ).eval( );
		
		// compute the inverse
		switch( op ) {
			case '+': return value - val;
			case '-': return lhs ? value + val : -1 * (value - val);
			case '*': return value / val;
			case '/': return lhs ? value * val : val / value;
			
			default: throw new RuntimeException( "Unknown operand: " + op );
		}
	}
	
	/** @return The expression as a string */
	@Override
	protected String toExprString( ) {
		final StringBuilder el = new StringBuilder( );
		for( final MMExpr e : elems ) {
			el.append( " " );
			el.append( e.toString( ) );
		}
		return "(" + op + el + ")";
	}
}
