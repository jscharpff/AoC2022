package challenges.day07;

import java.util.HashMap;
import java.util.Map;

/**
 * Node of the file system
 * 
 * @author Joris
 */
public class FSNode {
	/** The directory containing the file/folder */
	protected final FSNode parent;
	
	/** The file/folder name */
	protected final String name;
	
	/** The type */
	protected final boolean isDir;
	
	/** THe file or folder size cached after computing it once */
	private long cachedsize;
	
	/** The child directories, mapped by their name */
	private final Map<String, FSNode> subdirs;

	/** The files contained directly by this node*/
	private final Map<String, FSNode> files;

	/**
	 * Creates a new file system node
	 * 
	 * @param name The name of the node
	 */
	protected FSNode( final FSNode parent, final String name, final boolean isDir ) {
		this.parent = parent;
		this.name = name;
		this.isDir = isDir;
		this.subdirs = new HashMap<String, FSNode>( );
		this.files = new HashMap<String, FSNode>( );
	}
	
	/**
	 * Opens the child directory with the given name
	 * 
	 * @param dirname The child directory name
	 * @return The node of the child directory
	 */
	public FSNode dir( final String dirname ) {
		if( dirname.equals( ".." ) ) return parent;
		
		return subdirs.get( dirname );
	}
	
	/**
	 * Creates a new child directory of this FSNode
	 * 
	 * @param dirname The directory name 
	 */
	public void mkdir( final String dirname ) {
		final FSNode dir = new FSNode( this, dirname, true );		
		subdirs.put( dirname, dir );
	}
	
	/**
	 * Creates a new file in the directory
	 * 
	 * @param fname The file name
	 * @param fsize The file size
	 */
	public void mkfile( final String fname, final long fsize ) {
		final FSNode file = new FSNode( this, fname, false );
		file.cachedsize = fsize;
		files.put( fname, file );
	}
	
	/** 
	 * Determines the size of the file or folder, including present sub
	 * directories. The computed size will be cached for future use after
	 * computing it once.
	 * 
	 * @return The size of this directory or file, will recurse into sub
	 *   directories if unknown
	 */
	public long size( ) {
		// already computed?
		if( isDir && cachedsize != 0 ) return cachedsize;
		
		// nope, compute it
		for( final FSNode d : subdirs.values( ) ) cachedsize += d.size( );
		for( final FSNode f : files.values( ) ) cachedsize += f.size( );
		return cachedsize;
	}

	/**
	 * Finds all sub directories with a maximum given folder size and returns the
	 * sum of these folder sizes
	 * 
	 * @param maxsize The maximum folder size
	 * @return Sum of directory sizes that are below max
	 */
	protected long sumMaxSize( final long maxsize ) {
		long sum = 0;
		if( size( ) < maxsize ) sum += size( );
		
		// go over sub directories
		for( final FSNode d : subdirs.values( ) ) sum += d.sumMaxSize( maxsize );
		return sum;
	}
	
	/**
	 * Finds the smallest sub directory of minimal given size
	 * 
	 * @param minsize The minimal directory size
	 * @return The sub directory of smallest size that at least has the given
	 *   minimal size 
	 */
	protected FSNode findMinSize( final long minsize ) {
		// folder size big enough? use it as starting point
		if( size( ) < minsize ) return null;
		FSNode smallest = this;
		
		// check if any of the sub directories is even smaller and still meets the
		// requirement
		for( final FSNode d : subdirs.values( ) ) {
			FSNode fd = d.findMinSize( minsize );
			if( fd == null ) continue;
			if( smallest == null || fd.size( ) < smallest.size( ) ) smallest = fd;
		}
		return smallest;
	}
	
	/** @return The file system node description */
	@Override
	public String toString( ) {
		if( isDir ) {
			return "[" + name + "]" + (cachedsize > 0 ? ": " + cachedsize : "");
		} else {
			return name + ": " + cachedsize;
		}
	}
}
