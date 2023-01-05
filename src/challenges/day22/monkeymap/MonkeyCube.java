package challenges.day22.monkeymap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Direction;
import aocutil.grid.CoordGrid;

/**
 * Class that again helps navigating a MonkeyMap, however now the wrapping
 * around is based upon a cubic map model
 * 
 * @author Joris
 */
public class MonkeyCube extends MonkeyMap {
	/** The set of cube faces */
	protected final List<CubeFace> faces;
	
	/** The size of each cube side */
	protected final int cubesize;

	/**
	 * Creates a new MonkeyCube
	 * 
	 * @param map The map that is to be navigated
	 * @param cubesize The size of the cube sides
	 */
	public MonkeyCube( final CoordGrid<Tile> map, final int cubesize ) {
		super( map );
		this.faces = new ArrayList<>( 6 );
		this.cubesize = cubesize; 
		
		// parse cube structure, first classify faces
		int ID = 0;
		for( int y = 0; y <= 3 * cubesize; y += cubesize ) {
			for( int x = 0; x <= 3 * cubesize; x += cubesize ) {
				final Coord2D tl = new Coord2D( x, y );
				if( map.get( tl ) == Tile.Empty ) continue;
				faces.add( new CubeFace( ID++, tl ) );
			}
		}
		
		// reconstruct 'simple' neighbours from every face
		for( final CubeFace f : faces ) findNeighbours( f );
				
		// then reconstruct remaining neighbours
		final Set<CubeFace> remaining = new HashSet<>( faces );
		while( !remaining.isEmpty( ) ) {
			// remove faces if all neighbours are known
			remaining.removeIf( f -> f.Tmap.size( ) == 4 );
			
			// try to infer neighbours of all remaining faces
			for( final CubeFace f : remaining ) {
				for( final Direction d : Direction.values( ) ) {
					// direction already known?
					if( f.Tmap.containsKey( d ) ) continue;

					// no, infer connection by testing neighbours
					infer( f, d );
				}
			}
		}
	}
	
	/**
	 * Finds all 'simple' neighbours of the face. That is, all neighbours that
	 * share an edge in the 'flattened' cube map without rotation
	 * 
	 * @param f The face to check neighbours of
	 * @param t The current transformation that is applied
	 */
	private void findNeighbours( final CubeFace f ) {
		for( final Direction d : Direction.values( ) ) {
			// skip if this direction is already explored
			if( f.Tmap.containsKey( d ) ) continue;
			
			// check all reachable neighbours in the flat map
			for( final CubeFace next : faces ) {
				if( next.equals( f ) ) continue;
				
				if( next.topleft.equals( f.topleft.move( d, cubesize ) ) ) {
					f.Tmap.put( d, new Transform( next, 0 ) );					
					findNeighbours( next );
				}
			}
		}
	}	
	
	/**
	 * Tries to infer a neighbour in the given direction by trying to find it in
	 * via another neighbouring face
	 * 
	 * @param face The face to infer a neighbour of
	 * @param d The direction of the neighbour to find
	 */
	private void infer( final CubeFace face, final Direction d ) {
		// try to infer from directions one rotated to the left or right of the
		// neighbour we want to find
		for( final int turn : new int[] { -1, 1 } ) {
			final Direction dt = d.turn( turn );
			
			// face available in this direction?
			final Transform tn = face.Tmap.getOrDefault( dt, null );
			if( tn == null ) continue;
			
			// it is available, try and see if it knows the neighbour in the
			// direction we are trying to find (accounting for rotation)
			final Transform tnn = tn.face.Tmap.getOrDefault( d.turn( -tn.rotation ), null );
			if( tnn == null ) continue;
			
			// yes! we found a new neighbour. Create the relationship with the
			// correct transformation
			face.Tmap.put( d, new Transform( tnn.face, (tnn.rotation + tn.rotation - turn) % 4 ) );
		}
	}


	/**
	 * Reconstructs the cubic map from a list of strings
	 * 
	 * @param input The list of strings describing the map tiles and cube layout
	 * @return The MonkeyCube
	 */
	public static MonkeyCube fromStringList( final List<String> input ) {
		// first determine cube size. An unfolded cube must always be 4 faces wide
		// and 3 faces tall or vice versa, so find the biggest of the two and
		// divide by 4 to get the edge size
		final int H = input.size( );
		final int W = input.stream( ).mapToInt( i -> i.length( ) ).max( ).orElse( 0 );		
		int csize = Math.max( H, W ) / 4;		
		
		// then reconstruct the Monkey Cube
		return new MonkeyCube( CoordGrid.fromStringList( input, "", t -> Tile.fromChar( t.charAt( 0 ) ), Tile.Empty ), csize );
	}

	/**
	 * Wraps around the player by moving it one step in its current direction,
	 * then determining the new face, position and orientation.
	 *  
	 * @param player The current player position and direction
	 * @return The new player position and direction
	 */
	@Override
	protected Player wrap( final Player player ) {
		// get the face we are one and the transform that corresponds to moving to
		// the neighbour in the current player direction
		final CubeFace curr = getFace( player.pos );
		final Transform t = curr.Tmap.get( player.dir );
		
		// get new player coordinates against (0,0)-(size-1,size-1) reference frame
		final Coord2D move = player.pos.move( player.dir, 1 ).move( -curr.topleft.x, -curr.topleft.y );
		final int refx = (move.x + cubesize) % cubesize;
		final int refy = (move.y + cubesize) % cubesize;

		// get rotation in [0,3] range and apply it to the reference coordinates
		final int x; final int y;
		final int R = ((-t.rotation % 4) + 4) % 4;
		if( R == 1 ) { x = cubesize - refy - 1; y = refx;  }
		else if( R == 2 ) { x = cubesize - refx - 1; y = cubesize - refy - 1; }
		else if( R == 3 ) {x = refy; y = cubesize - refx - 1; }
		else { x = refx; y = refy; }
		
		// change the player position and orientation
		final Coord2D newpos = t.face.topleft.move( x, y );
		final Direction newdir = player.dir.turn( R );

		// apply the transformation and return the result
		return new Player( newpos, newdir );
	}
	
	/**
	 * Determines the cube face this position is located on
	 * 
	 * @param pos The position
	 * @return The face of the cube this position is located upon
	 * @throws IllegalArgumentException if the position is not on any cube face
	 */
	private CubeFace getFace( final Coord2D pos ) {
		for( final CubeFace f : faces )
			if( f.contains( pos ) ) return f;
		
		throw new IllegalArgumentException( "Position " + pos + " is not on the cube!" );
	}
	
	/**
	 * Container for a single face of the cubical map 
	 */
	protected class CubeFace { 
		/** The identifier of the cube face */
		protected final int ID;
		
		/** The top left coordinate of the face (wrt the flat map)*/
		protected final Coord2D topleft;
		
		/** The map of neighbouring faces and required rotations */
		protected final Map<Direction, Transform> Tmap;
		
		/**
		 * Creates a new cube face
		 * 
		 * @param ID The ID of the face for reference
 		 * @param topleft The top left coordinate of the cube face
		 */
		protected CubeFace( final int ID, final Coord2D topleft ) {
			this.ID = ID;
			this.topleft = topleft;
			this.Tmap = new HashMap<>( 4 );
		}
		
		/**
		 * Checks if the coordinate is somewhere on this cube face
		 * 
		 * @param coord The coordinate to test
		 * @return True iff the coordinate is located upon this face, i.e., it is
		 *   within (topleft)x(topleft + cubesize)
		 */
		public boolean contains( final Coord2D coord ) {
			if( coord.x < topleft.x || coord.y < topleft.y ) return false;
			if( coord.x >= topleft.x + cubesize || coord.y >= topleft.y + cubesize ) return false;
			return true;
		}
			
		/** @return The string description of the face */
		@Override
		public String toString( ) {
			return "[" +  ID + " " + topleft + "]";
		}
		
		/** @return The complete string description of the face */
		public String toFullString( ) {
			return toString( ) + ": " + Tmap.toString( );
		}
		
		/**
		 * Tests if the face has the same ID
		 *
		 * @param obj The other object
		 * @return True iff the object is a valid MonkeyCube.CubeFace
		 */
		@Override
		public boolean equals( final Object obj ) {
			if( obj == null || !(obj instanceof CubeFace) ) return false;
			return ((CubeFace)obj).ID == ID;
		}
		
		/** @return The ID as unique hash code */
		@Override
		public int hashCode( ) {
			return ID;
		}
	}
	
	/**
	 * Simple transform that stores the face and rotation applied to it
	 */
	private static class Transform {
		/** The face we end up as result of this transform */
		private final CubeFace face;
		
		/** The rotation of the face */
		private final int rotation; 
		
		/**
		 * Creates a new transform object
		 * 
		 * @param face The face that the transform applies to
		 * @param rotation The rotation of the face
		 */
		protected Transform( final CubeFace face, final int rotation ) {
			this.face = face;
			this.rotation = rotation;
		}
		
		/** @return The string describing the transformation */
		@Override
		public String toString( ) {
			return "<" + face.ID + "@" + rotation + ">";
		}
	}
}
