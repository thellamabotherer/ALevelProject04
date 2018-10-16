package Maps;

import java.util.ArrayList;
import java.util.Random;

import Data.Point;

public class TestMap {

	private ArrayList<Point> sites;
	private Point root;

	public TestMap(int WIDTH, int HEIGHT, int numSites) {

		this.sites = new ArrayList();
		Random rand = new Random();

		Point root = new Point (rand.nextDouble() * WIDTH, rand.nextDouble() * HEIGHT);
		this.root = root;
		
		for (int i = 0; i < numSites; i++) {
			Point newPoint = new Point (rand.nextDouble() * WIDTH, rand.nextDouble() * HEIGHT);
			addToTree(newPoint, root);
		}
		
		listInOrder(root, this.sites);

	} public TestMap (ArrayList<Point> sites) { // just put them in the tree
		
		this.sites = new ArrayList();
		//Random rand = new Random();

		Point root = new Point (sites.get(0).getX(), sites.get(0).getY());
		this.root = root;
		
		for (int i = 1; i < sites.size(); i++) {
			Point newPoint = new Point (sites.get(i).getX(), sites.get(i).getY(), sites.get(i).getLeft(), sites.get(i).getUp());
			addToTree(newPoint, root);
		}
		
		listInOrder(root, this.sites);
		
	}

	/*public TestMap (TestMap oldMap, int WIDTH, int HEIGHT) {
		
		Point root = oldMap.sites.get(0);
		root.slide(1, WIDTH, HEIGHT);
		
		for (int i = 1; i < oldMap.sites.size(); i++) {
			
			Point point = new Point (oldMap.sites.get(i).getX(), oldMap.sites.get(i).getY());
			point.slide(1, WIDTH, HEIGHT);
			
			addToTree(point, root);
			
		}this.sites = new ArrayList();
		listInOrder(root, this.sites);  // this method is useless, never use this, it is now more of an archaeological exhibit than anything else
		
	}*/
	
	public ArrayList<Point> getSites() {
		return this.sites;
	}public Point getRoot () {
		return this.root;
	}
	
	private static void addToTree (Point point, Point current) {
		
		if (point.getY() == current.getY()) {
			
			point.setY(point.getY() + 0.001);
			
		}
		
		if (point.getY() < current.getY()) {
			
			if (current.getLeftChild() == null) {
				
				current.setLeftChild(point);
				return;
				
			}
			addToTree(point, current.getLeftChild());
			return;
		}if (current.getRightChild() == null) {
			
			current.setRightChild(point);
			return;
		}
		addToTree(point, current.getRightChild());
		
	}private static void listInOrder (Point point, ArrayList<Point> list) {
		
		if (point.getLeftChild() != null) { listInOrder(point.getLeftChild(), list); }
		list.add(point);
		if (point.getRightChild() != null) { listInOrder(point.getRightChild(), list); }
	}
	
	public void slideAll (float dist, int WIDTH, int HEIGHT) {
		for (Point p : this.sites) {
			p.slide(dist, WIDTH, HEIGHT);
		}
	}

}
