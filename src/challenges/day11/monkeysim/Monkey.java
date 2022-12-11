package challenges.day11.monkeysim;

import java.util.LinkedList;
import java.util.function.Function;

import aocutil.string.RegexMatcher;

/**
 * A single monkey that is involved in the monkey business of inspecting and
 * throwing items. Their logic is weirdly based upon the worry level of each
 * item that is being thrown.
 * 
 * @author Joris
 */
public class Monkey {
	/** The ID number of the monkey */
	protected final int ID;
	
	/** The items it is currently holding */
	protected LinkedList<Long> items;
	
	/** The priority score calculation function */
	private final Function<Long, Long> worryfunc;
	
	/** The divisor to use in the test for new target */
	protected final int divisor;
	
	/** The target to throw item to if true */
	private int iftrue;
	
	/** The target to throw item to if test is false */
	private int iffalse;
	
	/** Counter that tracks the number of items thrown */
	protected long throwcount;

	/**
	 * Brings a new monkey to life
	 * 
	 * @param ID The identification number of the monkey
	 * @param worryfunc The function that is used to compute new worry scores
	 * @param divisor The divisor it uses to determine the target of each throw
	 * @param iftrue The ID of the monkey that is the target if the worry score
	 *   is perfectly divisible by the divisor
	 * @param iffalse The ID of the monkey that is the target if the worry score
	 *   is not perfectly divisible by the divisor
	 */
	private Monkey( final int ID, final Function<Long, Long> worryfunc, final int divisor, final int iftrue, final int iffalse ) {
		this.ID = ID;
		this.items = new LinkedList<>( );
		this.worryfunc = worryfunc;
		
		this.divisor = divisor;
		this.iftrue = iftrue;
		this.iffalse = iffalse;
		
		this.throwcount = 0;
	}
	
	/** @return The ID of the monkey */
	public int getID( ) {
		return ID;
	}
	
	/**
	 * Goes over the list of items that this monkey has in his possession. For
	 * every item the worry score is updated, the target monkey is determined and
	 * the item is passed on. This continues until the item list is empty.
	 * 
	 * @param monkeys The list of other monkeys so that items can be thrown
	 */
	public void throwItems( ) {
		while( !items.isEmpty( ) ) {
			throwcount++;
			
			// pop the item from my inventory and update its worry score
			long i = items.removeFirst( );
			i = MonkeySim.getInstance( ).worrylimit ? Math.floorDiv( worryfunc.apply( i ), 3 ) : worryfunc.apply( i );
			
			// then determine the target to throw it at and throw it
			final int target = i % divisor == 0 ? iftrue : iffalse;
			MonkeySim.getInstance( ).monkeys.get( target ).throwTo( i );
		}
	}

	/**
	 * Throws the item to this monkey
	 * 
	 * @param item The item to add to the list (actually its worry score)
	 */
	public void throwTo( final long item ) {
		items.add( item );
	}
	
	/**
	 * Normalises the item worry scores by keeping only the modulo that results
	 * from dividing through the common divisor of all monkey rules
	 * 
	 * @param div The common divisor to use
	 */
	protected void normalise( final long div ) {
		for( int i = 0; i < items.size( ); i++ )
			items.set( i, items.get( i ) % div );
	}

	/**
	 * Reconstructs a single monkey from its string description formatted as:
	 * 
	 * Monkey 0:
	 *   Starting items: 79, 98
	 *   Operation: new = old * 19
	 *   Test: divisible by 23
	 *     If true: throw to monkey 2
	 *     If false: throw to monkey 3
	 * 
	 * @param input The semicolon-separated string that holds all properties and
	 *   logic of the money to construct
	 * @return The instantiated monkey object
	 */
	public static Monkey fromString( final String input ) {
		final String[] s = input.split( ";" );
	
		// parse its ID
		final int number = Integer.parseInt( RegexMatcher.extract( "Monkey (#D):", s[0] ) );

		// parse the worry score function into an actual function
		final String func = s[2].split( ": " )[1];
		final String[] felems = func.split( " = " )[1].split( " " );
		final char op = felems[1].charAt( 0 );

		final Function<Long, Long> sfunc = new Function<Long, Long>( ) {
			@Override
			public Long apply( Long t ) {
				final long val2 = felems[2].equals( "old" ) ? t : Long.parseLong( felems[2] );
				return op == '*' ? t * val2 : t + val2;
			}
		};

		// determine divisor and target IDs for throws
		final int divby = Integer.parseInt( RegexMatcher.extract( "divisible by (#D)", s[3] ) );
		final int truetarget = Integer.parseInt( RegexMatcher.extract( "throw to monkey (#D)", s[4] ) );
		final int falsetarget = Integer.parseInt( RegexMatcher.extract( "throw to monkey (#D)", s[5] ) );
		
		
		// create the monkey with all parsed characteristics and then add all items
		final Monkey m = new Monkey( number, sfunc, divby, truetarget, falsetarget );
		for( final String i : s[1].split( "items: " )[1].split( ", " ) )
			m.throwTo( Long.parseLong( i ) );
		
		return m;
	}
	
	/** @return The string describing this monkey */
	@Override
	public String toString( ) {
		return "Monkey " + ID;
	}
}
