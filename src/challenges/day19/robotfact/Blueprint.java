package challenges.day19.robotfact;

import java.util.ArrayList;
import java.util.List;

/**
 * Blueprint that holds the costs of robots that can be produced in order to
 * collect and process minerals
 *  
 * @author Joris
 */
public class Blueprint {
	/** The ID of the blueprint */
	private final int ID;
	
	/** The list of robots it can produce */
	protected final List<Robot> robots;
	
	/**
	 * Creates a new blueprint
	 * 
	 * @param ID The ID of the blueprint
	 * @param robots The list of robots it can produce
	 */
	private Blueprint( final int ID, final List<Robot> robots ) {
		this.ID = ID;
		this.robots = new ArrayList<>( robots );
	}
	
	/** @return The ID of the blueprint */
	public int getID( ) {
		return ID;
	}
	
	/**
	 * Retrieves the robot that is used to produce the given material
	 * 
	 * @param mat The material that is to be produced
	 * @return The robot that produces it
	 */
	public Robot getRobotProducing( final Material mat ) {
		for( final Robot r : robots )
			if( r.produces == mat ) return r;
		return null;
	}
	
	/**
	 * Recreates a blueprint from a string description
	 * 	
	 * @param input The blueprint description with its ID and all robot
	 *   production rules separated by periods
	 * @return The blueprint
	 */
	public static Blueprint fromString( final String input ) {
		// parse ID
		final String[] s = input.split(  ": " );
		final int ID = Integer.parseInt( s[0].split( " " )[1] );
		
		// parse robot rules
		final List<Robot> R = new ArrayList<>( ); 
		for( final String b : s[1].split( "\\." ) ) {
			if( b.length( ) == 0 ) continue;
			R.add( Robot.fromString( b.trim( ) ) );
		}
		
		return new Blueprint( ID, R );
	}
	
	/** @return The description of the blueprint */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "Blueprint "  );
		sb.append( ID );
		for( final Robot r : robots ) {
			sb.append( '\n' );
			sb.append( r.toString( ) );
		}
		
		return sb.toString( );
	}
}
