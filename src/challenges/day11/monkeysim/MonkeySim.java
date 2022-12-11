package challenges.day11.monkeysim;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulation of the monkey business involved with throwing items based upon
 * worry scores. The simulation holds a group of monkeys and lets them decide
 * what to do with their items in a round-based fashion.
 * 
 * @author Joris
 */
public class MonkeySim {
	/** The instance of the simulation that is active */
	private static MonkeySim instance;
	
	/** The list of monkeys in the simulation */
	protected List<Monkey> monkeys;
	
	/** True to enable limiting of worry scores */
	protected boolean worrylimit;
	
	/** The common divisor of all monkeys */
	private final long commdiv;
	
	/**
	 * Creates a new monkey simulation
	 * 
	 * @param monkeys The list of monkeys partaking in the simulation
	 */
	private MonkeySim( final List<Monkey> monkeys ) {
		instance = this;
		this.worrylimit = false;
		this.monkeys = new ArrayList<>( monkeys );
		
		// compute common divisor once, used to normalise worry scores
		long div = 1;
		for( final Monkey m : monkeys ) div *= m.divisor;
		commdiv = div;
	}
	
	/** @return The active instance of the MonkeySim */
	public static MonkeySim getInstance( ) { return instance; }
	
	/**
	 * Sets the worry limit flag
	 * @param enable True to enable to limit, false to disable
	 */
	public void setWorryLimit( final boolean enable ) {
		this.worrylimit = enable;
	}
	
	/**
	 * Runs the monkey throwing simulation for the given number of rounds
	 * 
	 * @param rounds The number of rounds to run the simulation
	 */
	public void run( int rounds ) {
		for( int r = 0; r < rounds; r++ ) {
			// process every monkey's thoughts in the round
			for( final Monkey m : monkeys ) m.throwItems( );
			
			// normalise worry scores after the round by their common divisor
			for( final Monkey m : monkeys ) m.normalise( commdiv );
		}
	}
	
	/**
	 * Returns the product of the n monkeys that have inspected the most items
	 * at the point of calling this function
	 * 
	 * @param topn The number of monkeys to include in the top
	 * @return The product of their inspection counts
	 */
	public long getMostInspected( final int topn ) {
		// create sorted copy of monkey list, based upon counts
		final List<Monkey> M = new ArrayList<>( monkeys );
		M.sort( (m1,m2) -> Long.compare( m2.throwcount, m1.throwcount ) );
		
		// and return the product of the top N
		long score = 1;
		for( int i = 0; i < topn; i++ ) score *= M.get( i ).throwcount;
		return score;
	}
	
	/**
	 * Constructs a monkey throwing simulation from a group of strings that
	 * describes the individual monkeys
	 * 
	 * @param input List of strings that hold semicolon-separated descriptions of
	 *   the monkeys partaking in this simulation
	 * @return The simulation container
	 */
	public static MonkeySim fromString( final List<String> input ) {
		final List<Monkey> M = new ArrayList<>( );		
		for( final String s : input )
			M.add( Monkey.fromString( s ) );

		return new MonkeySim( M );
	}
	
	/** @return The monkey list as a string */
	@Override
	public String toString( ) {
		return monkeys.toString( );
	}
}
