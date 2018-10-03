package Maps;

import java.util.ArrayList;
import java.util.Random;

import Data.Point;

public class TestMap {
	
	private ArrayList <Point> sites;
	
	public TestMap (int WIDTH, int HEIGHT, int numSites) {
		
		this.sites = new ArrayList();
		Random rand = new Random();
		Point newSite;
		boolean isValid;
		int x; int y;
		
		for (int i = 0; i < numSites; i++) {
			
			isValid = true;
			
			x = rand.nextInt(WIDTH); y = rand.nextInt(HEIGHT);
			
			newSite = new Point (x, y);
			
			for (int j = 0; j < this.sites.size(); j++) {
				
				if (newSite == this.sites.get(j)) {
					isValid = false;
				}
				
			}if (isValid) {
				this.sites.add(newSite);
			}else {
				System.out.println("Fail");
				i--;
			}

			
		}
		
	}
	
	public ArrayList<Point> getSites () {
		return this.sites;
	}

}
