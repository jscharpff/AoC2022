package challenges.day16.volcano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple caching implementation that supports a hash-based mapping of key and
 * value pairs and offers pruning mechanisms to keep the cache size manageable
 * 
 * @author Joris
 *
 * @param <K> The data type of the key
 * @param <V> The data type of the value
 */
public class Cache<K, V> {
	/** The max cache size in number of entries */
	protected final int maxsize;
	
	/** The max size after pruning */
	protected final int prunesize;
	
	/** Strategies to prune the cache */
	public enum PruneStrategy {
		LeastHits, Oldest;
	}
	
	/** The configured prune strategy */
	protected final PruneStrategy pruning;
	
	/** The actual cache entries */
	protected Map<K, CacheEntry> entries;
	
	/**
	 * Creates a new cache with pruning strategy 'oldest'
	 * 
	 * @param maxsize The maximum cache entries
	 * @param prunefactor The factor to keep after pruning
	 */
	public Cache( final int maxsize, final double prunefactor ) {
		this( maxsize, prunefactor, PruneStrategy.Oldest );
	}
	
	/**
	 * Creates a new cache
	 * 
	 * @param maxsize The maximum cache entries
	 * @param prunefactor The factor to keep after pruning
	 * @param pruneStrategy The prune strategy to prune entries if the size limit
	 *   is hit
	 */
	public Cache( final int maxsize, final double prunefactor, final PruneStrategy pruneStrategy ) {
		this.entries = new HashMap<>( maxsize );
		this.maxsize = maxsize;
		this.prunesize = (int)((double)maxsize * prunefactor);
		this.pruning = pruneStrategy;
	}
	
	/**
	 * Checks if a key is present in the cache
	 * 
	 * @param key The key to search
	 * @return True iff an entry <k,v> exists for which k.equals( key )
	 */
	public boolean contains( final K key ) {
		return entries.containsKey( key );
	}

	/**
	 * Retrieves the value of the cached entry
	 * 
	 * @param key The key to search
	 * @return The value of the key
	 */
	public V get( final K key ) {
		final CacheEntry entry = entries.get( key );
		entry.hit( );
		return entry.value;
	}
	
	/**
	 * Adds or overwrites an entry in the cache. Will call the prune function if
	 * the size limit is exceeded
	 * 
	 * @param key The key to store the value at
	 * @param value The value to store in the cache
	 */
	public void set( final K key, final V value ) {
		if( entries.size( ) + 1 >= maxsize ) prune( );
		entries.put( key, new CacheEntry( key, value ) );
	}
	
	/**
	 * Prunes the cache according to the chosen strategy and will preserve the
	 * configured number of entries
	 */
	private void prune( ) {
		System.out.println( "PRUNING" );
		final List<CacheEntry> E = new ArrayList<>( entries.values( ) );
		
		// apply chosen pruning strategy
		switch( pruning ) {
			// sort entries from most to least cache hits
			case LeastHits:
				E.sort( (x,y) -> y.hits - x.hits );
				break;

			// sort entries from most recent to oldest cache hit
			case Oldest:
				E.sort( (x,y) -> Long.compare( y.lasthit, x.lasthit ) );
				break;
		}

		// prune every entry after the specified pruning size
		while( E.size( ) > prunesize ) {
			entries.remove( E.remove( E.size( ) - 1 ).key );
		}		
	}
	
	/**
	 * Class that holds a single cache entry
	 */
	private class CacheEntry {
		/** The key to store the value of */
		protected final K key;
		
		/** The stored value */
		protected final V value;
		
		/** The number of cache hits (successful lookups) */
		protected int hits;
		
		/** The last cache hit */
		protected long lasthit;
		
		/**
		 * Creates a new cache entry
		 * 
		 * @param key The key to store the entry at
		 * @param value The value to hold
		 */
		public CacheEntry( final K key, final V value ) {
			this.key = key;
			this.value = value;
			this.hits = 0;
			this.lasthit = 0;
		}
		
		/** Marks a hit of this cache entry */
		protected void hit( ) {
			hits++;
			lasthit = System.currentTimeMillis( );
		}
		
		/** @return The string description of the cache entry */
		@Override
		public String toString( ) {
			return key + ": " + value + " (hits: " + hits + ", lasthit: " + lasthit + ")";
		}
	}

}
