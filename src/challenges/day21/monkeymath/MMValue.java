package challenges.day21.monkeymath;

/**
 * Expression that holds a literal value, that is, a constant
 * 
 * @author Joris
 */
public class MMValue extends MMExpr {
	/** The literal value */
	protected final long value;
	
	/**
	 * Creates a new literal value
	 * 
	 * @param resvar The variable the result is stored in
	 * @param value The literal value
	 */
	protected MMValue( final String resvar, final long value ) {
		super( resvar );
		this.value = value;
	}
	
	/** @return The literal value */
	@Override
	protected long eval( ) {
		return value;
	}

	/**
	 * @return The string of the value
	 */
	@Override
	protected String toExprString( ) {
		return "" + value;
	}
}
