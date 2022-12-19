package challenges.day19.robotfact;

import java.util.HashMap;
import java.util.Map;

import aocutil.string.RegexMatcher;

/**
 * A single mineral-collecting robot
 * 
 * @author Joris
 */
public class Robot {
	/** The material required to produce the robot */
	protected final Map<Material, Integer> requires;
	
		/** The material it produces */
	protected final Material produces;

	/**
	 * Creates a new robot
	 * 
	 * @param requires The map of materials and their quantities required to
	 *   construct this type of robot
	 * @param produces The material it produces
	 */
	private Robot( final Map<Material, Integer> requires, final Material produces ) {
		this.requires = new HashMap<>( requires );
		this.produces = produces;
	}
	
	/**
	 * Recreates the robot from a string description in the format: 
	 *   "Each (\\w+) robot costs ([0-9a-z ]+)" 
	 * such that the first group contains the material produced and the second
	 * the required materials and quantities as [amount material], separated by
	 * " and " in case of multiple materials required 
	 * 
	 * @param input The string to reconstruct the robot from
	 * @return The robot
	 */
	public static Robot fromString( final String input ) {
		final RegexMatcher r = RegexMatcher.match( "Each (\\w+) robot costs ([0-9a-z ]+)", input );
		final Material prod = Material.fromString( r.get( 1 ) );
		
		// parse required materials
		final Map<Material, Integer> mq = new HashMap<>( );
		for( final String s : r.get( 2 ).split( " and " ) ) {
			final String[] ms = s.split( " " );
			mq.put( Material.fromString( ms[1] ), Integer.parseInt( ms[0] ) );
		}
		return new Robot( mq, prod );
	}
	
	/** @return The string describing the robot */
	@Override
	public String toString( ) {
		return requires.toString( ) + " => " + produces;
	}
}
