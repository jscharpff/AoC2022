package challenges.day20;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Linked list implementation with nodes to hold its values
 * 
 * @author Joris
 * @param <V> The data type of the node values
 */
public class LinkedList<V> implements Iterable<LLNode<V>> {
	/** The starting list element */
	private LLNode<V> head;
	
	/** The size of the list */
	private int size;
	
	/**
	 * Creates a new, empty linked list
	 */
	public LinkedList( ) {
		clear( );
	}
	
	/**
	 * Clears the list
	 */
	public void clear( ) {
		this.head = null;
		this.size = 0;
	}
	
	/** @return The number of elements in the list */
	public int size( ) { return size; }
	
	/** @return True iff the list is empty */
	public boolean isEmpty( ) { return size == 0; }
	
	/**
	 * Adds a new element to the back of the list
	 * 
	 * @param value The value of the element to add
	 */
	public void add( final V value ) {
		final LLNode<V> n = new LLNode<V>( value );
		size++;
		
		// first element?
		if( head == null ) {
			head = n.prev = n.next = n;
		} else {
			// nope, insert it after the tail (right before the head)
			add( head.prev, value );
		}
	}
	
	/**
	 * Adds a new element after the specified list node
	 * 
	 * @param node The node to insert after
	 * @param value The value to insert 
	 */
	public void add( final LLNode<V> node, final V value ) {
		if( node == null ) throw new IndexOutOfBoundsException( "Node to insert after cannot be null (use add( value ) instead)" );
		
		// insert node
		final LLNode<V> n = new LLNode<V>( value );
		node.next.prev = n;
		n.prev = node;
		n.next = node.next;
		node.next = n;
	}
	
	/** @return The first element of the list, null if it is empty */
	public LLNode<V> getFirst( ) { return head; }
	
	/**
	 * Returns the first node that holds the given value, starting from the head
	 * of the list
	 * 
	 * @param value The value to find
	 * @return The node that holds the value, null if not found
	 */
	public LLNode<V> find( final V value ) {
		return find( head, value );
	}

	/**
	 * Returns the first node that holds the given value, starting from the given
	 * node
	 * 
	 * @param node The node to start search from
	 * @param value The value to find
	 * @return The node that holds the value, null if not found
	 */
	public LLNode<V> find( final LLNode<V> node, final V value ) {
		if( isEmpty( ) ) return null;
		if( node == null ) throw new NullPointerException( );
		
		// keep track of max iterations
		int maxiter = size( );
		LLNode<V> curr = node;
		while( maxiter-- > 0 ) {
			if( curr.getValue( ).equals( value ) ) return curr;
			curr = curr.next;
		}
		
		// not found
		return null;
	}

	
	/**
	 * 'Shifts' the specified element in the list. It will update the head if
	 * the original node was the head of the list
	 * 
	 * @param node The node to shift. 
	 * @param steps The number of steps to shift. Positive values will move it in
	 *   the direction of the tail, negative towards the head. The shift will
	 *   wrap around if it exceeds the list size (or hits 0)
	 */
	public void shift( final LLNode<V> node, final long steps ) {
		// compute required moves but keep it within the list boundaries. Note that
		// shifting will consider 1 position less than the actual list size as we
		// consider positions in between nodes
		int s = (int)(steps % (long)(size() - 1));
		if( s == 0 ) return;
		
		// move head by one if that is the node we are shifting
		if( node.equals( head ) ) 
			if( steps < 0 ) { head = head.prev; } 
			else { head = head.next; }
		
		// keep shifting for the desired number of steps
		while( s != 0 ) {
			// remove from old position
			node.prev.next = node.next;
			node.next.prev = node.prev;		
			
			// shift the node into the right position
			if( s > 0 ) {
				s--;
				node.prev = node.next;
				node.next = node.next.next;
			} else {
				s++;
				node.next = node.prev;
				node.prev = node.prev.prev;
			}
			
			// insert in new position
			node.prev.next = node;
			node.next.prev = node;		
		}
		
	}
	
	/**
	 * Retrieves the node that is X steps away from the current node
	 * 
	 * @param node The node to start from
	 * @param offset The offset to move from the given node, positive numbers
	 *   will move in the direction of the tail, negative towards the head
	 * @return The node that is offset steps away from the start node
	 */
	public LLNode<V> getByOffset( final LLNode<V> node, final long offset ) {
		if( isEmpty( ) ) return null;
		if( node == null ) throw new NullPointerException( );
		
		long off = offset % (long)size( );
		if( off < 0 ) off += (long)size();
		System.out.println( off );
		LLNode<V> curr = node;
		while( off-- > 0 ) curr = curr.next;
		
		return curr;
	}
	
	/**
	 * Creates a List of the elements in the linked list, starting from the head
	 * 
	 * @return List of items, backed by an ArrayList
	 */
	public List<V> toList( ) {
		final List<V> list = new ArrayList<>( size( ) );
		if( isEmpty( ) ) return list;
		
		LLNode<V> curr = head;
		for( int i = 0; i < size(); i++ ) {
			list.add( curr.getValue( ) );
			curr = curr.next;
		}
		
		return list;
	}
	
	/**
	 * @return The iterator over the linked list
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public Iterator<LLNode<V>> iterator( ) {
		return (Iterator<LLNode<V>>) toList( ).iterator( );
	}
	
	/** @return The string description of the list elements */
	@Override
	public String toString( ) {
		return toList( ).toString( ); 
	}
}
