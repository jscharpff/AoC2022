package challenges.day21.monkeymath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that holds a system of equations, can reduce simple equations and
 * solve a simple equation with a single unknown variable 
 * 
 * @author Joris
 */
public class MonkeyMath {
	/** The set of (parsed) equations in the system */ 
	private final Map<String, MMExpr> E;
	
	/**
	 * Creates a new empty system of equations
	 */
	public MonkeyMath( ) {
		E = new HashMap<>( ); 
	}
	
	/**
	 * Processes a list of strings and fills the set of equations
	 * 
	 * @param input The list of strings that contain all equations in the system
	 */
	private void parseInput( final List<String> input ) {
		final List<String> rem = new ArrayList<>( input );
	
		// keep parsing the input until we've fully reconstructed all variable
		// expressions
		final Set<String> parsed = new HashSet<>( );
		do {
			// remove all and clear the parsed list
			rem.removeAll( parsed );
			parsed.clear( );
			
			for( final String e : rem ) {
				final MMExpr ex = MMExpr.parse( E, e );
				if( ex == null ) continue;

				// add to set of parsed expressions and remove the var
				E.put( ex.resvar, ex );
				parsed.add( e );
			}
			
		} while( parsed.size( ) > 0 );
	}
	
	/**
	 * Reduces the set of equations to find the value for the specified target variable
	 * 
	 * @param input The list of equations, one string per equation 
	 * @param targetvar The variable of which the value is to be found
	 * @return The value of the target variable after reduction of the equations
	 */
	public long reduce( final List<String> input, final String targetvar ) {
		// process the input and return the result of evaluating it from the target
		// variable
		parseInput( input );
		return E.get( targetvar ).eval( );
	}
	
	/**
	 * Finds the value for the unknown variable such that the equation, defined
	 * by the expression for the eqvar, is satisfied. This function can only
	 * solve simple equations in which the unknown variable occurs exactly once
	 * in the equation.
	 * 
	 * @param input The set of equations
	 * @param eqvar The variable that holds the equation to solve (thereby
	 *   replacing its original operand)  
	 * @param unknown The name of the unknown variable
	 * @return The value for the unknown variable such that the equation defined
	 *   by eqvar is satisfied
	 */
	public long findSingleUnknown( final List<String> input, final String eqvar, final String unknown ) {
		// remove the value for the humn from the input and replace the eqvar
		final List<String> in = new ArrayList<>( input );
		String equation = null;
		for( int i = in.size( ) - 1; i >= 0; i-- ) {
			if( in.get( i ).startsWith( eqvar + ": " ) ) equation = in.remove( i );
			if( in.get( i ).startsWith( unknown + ": " ) ) in.remove( i );
		}
		if( equation == null ) throw new RuntimeException( "No equation in input" );
		
		// add a variable for the unknown and parse the input
		E.put( unknown, new MMVariable( unknown ) );
		parseInput( in );
		
		// construct the equation expression
		final String[] s = equation.split( ": " )[1].split( " \\+ " );
		final MMExpr e1 = E.get( s[0] );
		final MMExpr e2 = E.get( s[1] );
		final MMEquation eq = new MMEquation( unknown, e1, e2 );
		
		// and solve it
		return eq.solve( );
	}
}
