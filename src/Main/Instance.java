package Main;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

public class Instance {
	
	private int fps;
	private Window window;
	
	public Instance (int fps, Window window) {
		this.fps = fps;
		this.window = window;
		
	}
	
	public Window getCurrentWindow() {
		return this.window;
	}
	
	public boolean run () {
		
		if (Display.isCloseRequested()) {
			return false;
		}
		window.changeColour(new Vector4f ((float)1,(float)1,(float)1,(float)1));
		Display.update();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClear(0);
		Display.sync(fps);
		return true;
	}
	
	// This is probably how I'll deal with going through the separate stages of the map (maybe just a big case switch)

}
