package challenges.day21.monkeymath;

/**
 * Simple expression that models a single unknown
 * 
 * @author Joris
 */
public class MMVariable extends MMExpr {
	/**
	 * Creates a new variable
	 * 
	 * @param resvar The name of the variable
	 */
	protected MMVariable( final String resvar ) {
		super( resvar );
	}
	
	/**
	 * @throws RuntimeException A variable cannot be evaluated itself
	 */
	@Override
	protected long eval( ) {
		throw new RuntimeException( "Cannot evaluate variable: " + resvar );
	}

	/** @return The name of the variable */
	@Override
	protected String toExprString( ) {
		return resvar;
	}	
}
