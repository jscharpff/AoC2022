package challenges.day19.robotfact;

/**
 * Mineral types
 * 
 * @author Joris
 */
public enum Material {
	/** Available materials */
	Ore, Clay, Obsidian, Geode;
	
	public static Material fromString( final String str ) {
		switch( str.toLowerCase( ) ) {
			case "ore": return Ore;
			case "clay": return Clay;
			case "obsidian": return Obsidian;
			case "geode": return Geode;
			default: throw new RuntimeException( "Unknown material: " + str );
		}
	}
}
