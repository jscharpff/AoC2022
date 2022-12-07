package challenges.day07;

import java.util.List;

/**
 * Container for a tree-based file system
 *  
 * @author Joris
 */
public class FileSystem {
	/** The root FSNode of the file system */
	protected final FSNode root;
	
	/**
	 * Creates a new, empty file system
	 * 
	 * @param rootlabel The label of the root FSNode
	 */
	protected FileSystem( final String rootlabel ) {
		root = new FSNode( null, rootlabel, true );
	}
	
	/** @return The root FSNode */
	private FSNode getRoot( ) {
		return root;
	}
	
	/**
	 * Goes over all directories in the file system and returns the sum of
	 * directory sizes that have at most the given maximum size.
	 * 
	 * @param maxsize The maximum folder size to consider
	 * @return The total sum of directory sizes under 100k
	 */
	public long sumMaxSize( final long maxsize ) {
		return getRoot( ).sumMaxSize( maxsize );
	}
	
	/**
	 * Finds the minimal space to free to have the required amount available.
	 * Here, the minimal space to free is given by the first directory size that
	 * will at least free up the necessary amount.
	 * 
	 * @param maxspace The file system max storage capacity
	 * @param reqspace The required free storage capacity
	 * @return The minimal space we have to free, -1 if no such directory exists
	 */
	public long minSpaceToFree( final long maxspace, final long reqspace ) {
		// first determine space to free up by considering also available space
		final long free = reqspace - (maxspace - getRoot( ).size( ));
		
		// then find the directory with the smallest size that will free up the
		// required space
		final FSNode d = getRoot( ).findMinSize( free );
		return d != null ? d.size( ): -1;
	}
	
	/**
	 * Reconstructs a file system from a command line interaction
	 * 
	 * @param cmd The command line input
	 * @return The file system
	 */
	public static FileSystem fromCMD( final List<String> cmd ) {
		final FileSystem fs = new FileSystem( "/" );
		FSNode curr = fs.getRoot( ); 
		
		for( int i = 1; i < cmd.size( ); i++ ) {
			final String c = cmd.get( i );
			
			if( c.startsWith( "$ cd " ) ) {
				// change directory
				curr = curr.dir( c.substring( 5 ) );
			} else if( c.equals( "$ ls" ) ) {
				// file listing, create all nodes
				while( i < cmd.size( ) - 1 ) {
					final String listing = cmd.get( ++i );
					
					// end of listing, back to processing of commands
					if( listing.startsWith( "$" ) ) { i--; break; } 
					// folder
					else if( listing.startsWith( "dir" ) ) 
						curr.mkdir( listing.split( " " )[1] );
					// file
					else {
						final String[] f = listing.split( " " );
						curr.mkfile( f[1], Integer.parseInt( f[0] ));
					}
				}
			}
			
		}
		
		return fs;
	}
}
