package Maps;

import java.util.ArrayList;
import java.util.Random;

import Data.Point;

public class TestMap {

	private ArrayList<Point> sites;

	public TestMap(int WIDTH, int HEIGHT, int numSites) {

		this.sites = new ArrayList();
		Random rand = new Random();
		Point newSite;
		boolean isValid;
		double x;
		double y;

		Point root = new Point (rand.nextDouble() * WIDTH, rand.nextDouble() * HEIGHT);
		
		for (int i = 0; i < numSites; i++) {
			Point newPoint = new Point (rand.nextDouble() * WIDTH, rand.nextDouble() * HEIGHT);
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
	}
	
	private static void addToTree (Point point, Point current) {
		
		if (point.getY() == current.getY()) {
			
			point.setY(point.getY() + 0.001);
			
		}
		
		if (point.getY() < current.getY()) {
			
			if (current.getLeftChild() == null) {
				
				current.setLeftChild(point);
				return;
				
			}addToTree(point, current.getLeftChild());
			return;
		}if (current.getRightChild() == null) {
			
			current.setRightChild(point);
			return;
		}addToTree(point, current.getRightChild());
		
	}private static void listInOrder (Point point, ArrayList<Point> list) {
		
		if (point.getLeftChild() != null) { listInOrder(point.getLeftChild(), list); }
		list.add(point);
		if (point.getRightChild() != null) { listInOrder(point.getRightChild(), list); }
	}

}
