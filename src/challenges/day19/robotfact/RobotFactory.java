package challenges.day19.robotfact;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory that produces mineral-processing robots, given a list of
 * predefined blueprints. The factory is also able to determine the quality
 * level of blueprints by determining the number of geodes it can produce
 * 
 * @author Joris
 */
public class RobotFactory {
	/** The blueprints of this factory */
	protected final List<Blueprint> blueprints;
		
	/**
	 * Creates the robot factory
	 * 
	 * @param blueprints The list of blueprints to construct robots
	 */
	private RobotFactory( final List<Blueprint> blueprints ) {
		this.blueprints = new ArrayList<>( blueprints );
	}

	/**
	 * Creates a robot factory by parsing the blueprints from a list of strings
	 * 
	 * @param input The list of blueprints, one per strings 
	 * @return The robot factory
	 */
	public static RobotFactory fromStringList( final List<String> input ) {
		final List<Blueprint> B = new ArrayList<>( input.size( ) );
		for( final String s : input ) B.add( Blueprint.fromString( s ) );
		return new RobotFactory( B );
	}
	
	
	/**
	 * Computes the quality score over all blueprints by summing for each
	 * blueprint its ID and the number of geodes it can maximally produce within
	 * 24 minutes 
	 * 
	 * @param time The number of time steps to run the robot factory
	 * @return The total quality score of the blueprints
	 */
	public long getBlueprintQaulityScore( final int time ) {
		long result = 0;
		for( final Blueprint b : blueprints ) {
			System.out.print( "Blueprint " + b.getID( ) + ": " );
			final RobotCollector rc = new RobotCollector( b );
			final int score = rc.collect( time );
			result += b.getID( ) * score;
			System.out.println( score + " geode(s)" );
		}
		
		return result;
	}
	
	/**
	 * Computes the product of the geode counts of the first three blueprints of
	 * the factory (or less if there are not enough blueprints) that can be
	 * produced if the factory runs for 32 minutes
	 * 
	 * @param time The number of time steps to run the robot factory
	 * @return The product of geode counts
	 */
	public long getMaxGeodeProduct( final int time ) {
		long result = 1;
		for( int i = 0; i < Math.min( blueprints.size( ), 3 ); i++ ) {
			final Blueprint b = blueprints.get( i );
			System.out.print( "Blueprint " + b.getID( ) + ": " );
			final RobotCollector rc = new RobotCollector( b );
			final int score = rc.collect( time );
			result *= score;
			System.out.println( score + " geode(s)" );
		}
		
		return result;
	}
}
