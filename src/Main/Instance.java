package Main;

import org.lwjgl.opengl.Display;

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
		
		Display.update();
		
		if (Display.isCloseRequested()) {
			return false;
		}
		Display.sync(fps);
		return true;
	}
	
	// This is probably how I'll deal with going through the separate stages of the map (maybe just a big case switch)

}
