package challenges.day20;

/**
 * A single LinkedList element
 * 
 * @param <V> The type of the linked list value
 */
public class LLNode<V> {
	/** Its successor */
	LLNode<V> next;
	
	/** Its predecessor */
	LLNode<V> prev;
	
	/** The value of the node */
	private V value;
	
	/**
	 * Creates a new element with given value
	 * 
	 * @param value The value of the node
	 */
	public LLNode( final V value ) {
		this.value = value;
	}
	
	/** @return The value of the node */
	public V getValue( ) { return value; }
	
	/** @return The predecessor of this node */
	public LLNode<V> getPrev( ) { return prev; }
	
	/** @return The successor of this node */
	public LLNode<V> getNext( ) { return next; }
	
	/** @return A string describing this node and its neighbours */
	@Override
	public String toString( ) {
		return prev.getValue( ) + " -> (" + getValue( ) + ") -> " + next.getValue( );
	}
}