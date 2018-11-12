package Data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Main;
import Main.Window;
import Main.WorldConstraints;

public class Current extends Weather {

	protected Area area;
	
	private float temp;
	private int life;
	
	public Current (Area a) {
		
		this.life = WorldConstraints.timeToLive;
		this.temp = a.getOceanTemp();
		this.area = a;
		
	}
	@Override
	public void walk () {
		//this.draw(30, Main.window);
		//Display.update();
		Random rand = new Random();
		ArrayList<Float> prefs = this.getPrefList();
		if (prefs == null) {	
			return;
		}
		float dir = rand.nextFloat();
		try {
		this.area = this.area.getAdjacencies().get(findDirection(dir, prefs, 0));
		}catch (IndexOutOfBoundsException ex) {
			return;
		}
		this.moveInto();
		this.life --;
		if (this.life > 0) {
			this.walk();
		}
	}
	
	protected ArrayList<Float> getPrefList () {
		return this.area.getOceanPrefs();
	}
	
	@Override
	protected int findDirection (float dir, ArrayList<Float> prefs, int checked) {
		if (checked == prefs.size()) {
			return (Integer) null;
		}
		if (dir - prefs.get(checked) <= 0) {
			return checked;
		}return findDirection (dir - prefs.get(checked), prefs, checked + 1) ;
	}
	
	protected void moveInto () {
		
		float t;
		t = area.getOceanTemp();
		area.setOceanTemp((9 * t + this.temp)/10);
		this.temp = (9 * this.temp + t)/10;
		
	}
	
	// --------------------------- graphical ----------------------------------------
	
	private void draw (int frames, Window window) {
		window.beginRender();
		window.changeColour(new Vector4f (1, 0, 0, 0));
		window.addVertex(new Vector3f(this.area.getLongditude(), this.area.getLatitude() + 5, 0));
		window.addVertex(new Vector3f(this.area.getLongditude() + 5, this.area.getLatitude() - 2, 0));
		window.addVertex(new Vector3f(this.area.getLongditude() - 5, this.area.getLatitude() - 2, 0));
		window.endRender();
		window.beginRender();
		window.addVertex(new Vector3f(this.area.getLongditude(), this.area.getLatitude() - 5, 0));
		window.addVertex(new Vector3f(this.area.getLongditude() - 5, this.area.getLatitude() + 2, 0));
		window.addVertex(new Vector3f(this.area.getLongditude() + 5, this.area.getLatitude() + 2, 0));
		window.endRender();
		try {
			Thread.sleep(1000/frames);
		} catch (InterruptedException e) {
			System.out.println("Boop");
		}
	}
	
}
