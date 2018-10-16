package Data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import Main.Window;

public class Plate {

	private ArrayList<Polygon> polys;
	private Vector2f direction;
	private float speed;
	private boolean continental;
	private boolean majorPlate;
	
	private Vector4f Colour;
	private double height;
	
	
	public Plate (Polygon start, boolean majorPlate) {
		
		Random rand = new Random();
		//Colour = Main.ColourPalette.Colours[rand.nextInt(Main.ColourPalette.Colours.length)];
		
		Colour = new Vector4f (rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
		
		this.polys = new ArrayList();
		
		this.polys.add(start);
		this.majorPlate = majorPlate;
		this.continental = rand.nextBoolean();
		
		this.direction = new Vector2f (rand.nextFloat(), rand.nextFloat());
		
		
	}public boolean floodFill () {
		boolean used = false;
		ArrayList<Polygon> oldList = new ArrayList();
		for (Polygon p : this.polys) {
			oldList.add(p);
		}
		for (Polygon p : oldList) {
			for (Polygon a : p.getAdjacencies()) {
				if (!a.isInPlate()) {
					a.setInPlate(true);
					a.setPlate(this);
					this.polys.add(a);
					used = true;
				}
			}
		}return used;
	}
	
	public void draw (Window window) {
		for (Polygon p : polys) {
			p.draw(window, Colour);
		}
	}public boolean isMajor () {
		return this.majorPlate;
	}public ArrayList<Polygon> getPolys () {
		return this.polys;
	}
	public Vector2f getDirection() {
		return this.direction;
	}public boolean isContinental () {
		return this.continental;
	}
	
}
