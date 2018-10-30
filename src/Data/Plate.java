package Data;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Main.Settings;
import Main.Window;
import Main.WorldConstraints;

public class Plate {

	private ArrayList<Polygon> polys;
	private ArrayList<Plate> adj;
	
	private Vector2f direction;
	private float speed;
	private boolean continental;
	private boolean majorPlate;
	
	private Vector4f Colour;
	private double height;
	
	private int size;
	private Vector2f centroid;
	
	private Vector2f tilt;
	
	public Plate (Polygon start, boolean majorPlate) {
		
		Random rand = new Random();
		//Colour = Main.ColourPalette.Colours[rand.nextInt(Main.ColourPalette.Colours.length)];
		
		Colour = new Vector4f (rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
		
		this.polys = new ArrayList();
		
		this.polys.add(start);
		this.majorPlate = majorPlate;
		
		float type = rand.nextFloat();
		if (type < WorldConstraints.oceanProb) {
			this.continental = false;
		}else {
			this.continental =true;
		}
		
		this.tilt = new Vector2f (rand.nextFloat()/2, rand.nextFloat()/2);
		this.direction = new Vector2f (rand.nextFloat() * 2 - 1, rand.nextFloat() * 2 - 1);
		
		
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
					this.size ++;
					used = true;
				}
			}
		}return used;
	}
	
	public void findAdj () {
		this.adj = new ArrayList();
		for (Polygon p : this.polys) {
			if (p.isEdgeOfPlate()) {
				for (Polygon q : p.getAdjacencies()) {
					if (q.getPlate() != this && !(this.adj.contains(q.getPlate()))) {
						this.adj.add(q.getPlate());
					}
				}
			}
		}
	}
	
	public void findCentroid () {
		float x = 0;
		float y = 0;
		float n = 0;
		for (Polygon p : this.polys) {
			x = x + p.getCentroid().x;
			y = y + p.getCentroid().y;
		}this.centroid = new Vector2f (x/n, y/n);
	}
	
	public float getTilt (Vector3f pos) {
		float tiltX = ((this.getCentroid().x - pos.x) * (tilt.x)) / Settings.screenWidth;
		float tiltY = ((this.getCentroid().y - pos.y) * (tilt.y)) / Settings.screenHeight;
		return (tiltX + tiltY);
	}
	
	public Vector2f getCentroid() {
		return centroid;
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
	public ArrayList<Plate> getAdj() {
		return adj;
	}public void uncheck () {
		for (Polygon p : this.polys) {
			p.uncheck();
		}
	}
	
}