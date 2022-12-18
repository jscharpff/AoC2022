package challenges.day16.volcano;

import aocutil.object.LabeledObject;

public class Valve extends LabeledObject {
	protected final int pressureInc;
	
	public Valve( final String label, final int pressureInc ) {
		super( label );
		this.pressureInc = pressureInc;
	}
}
