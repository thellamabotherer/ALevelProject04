package Data;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.ColourPalette;
import Main.Main;
import Main.Window;
import Main.WorldConstraints;

public class WeatherSystem { // an epicentre of the weather around which air or water tries to spin 
	
	// I'll probably have to make subclasses of these for water and air
	
	public static final boolean current = true;
	public static final boolean air = false;
	
	public static final boolean clockwise = true;
	
	private float x;
	private float y;
	private boolean type;
	private boolean spin;
	
	public WeatherSystem (boolean type, Area area) {
		Random rand = new Random();
		this.x = area.getLongditude();
		this.y = area.getLatitude();
		this.type = type;
		this.spin = rand.nextBoolean();
	}
	
	public Vector2f getCoords () {
		return new Vector2f (this.x, this.y);
	}
	
	public boolean getSpin () {
		return this.spin;
	}
	
	public void draw (Window w) {
		w.beginRender();
		w.changeColour(new Vector4f (1, 1, 1, 0));
		w.addVertex(new Vector3f (this.x - 5, this.y - 5, 0));
		w.addVertex(new Vector3f (this.x, this.y + 10, 0));
		w.addVertex(new Vector3f (this.x + 5, this.y - 5, 0));
		w.endRender();
	}

}
