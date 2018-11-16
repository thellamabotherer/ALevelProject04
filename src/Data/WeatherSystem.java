package Data;

import org.lwjgl.util.vector.Vector2f;

public class WeatherSystem { // an epicentre of the weather around which air or water tries to spin 
	
	// I'll probably have to make subclasses of these for water and air
	
	public static final boolean current = true;
	public static final boolean air = false;
	
	private float x;
	private float y;
	
	public WeatherSystem (boolean type, Area area) {
		
	}
	
	public Vector2f getCoords () {
		return new Vector2f (this.x, this.y);
	}
	

}
