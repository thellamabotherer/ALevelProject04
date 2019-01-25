package Data;

import java.util.Random;

public class Biome {

	private int type;
	
	public Biome (Area a) { // stage 1 
		
		// decide biome type based on the parameters relating to the area 
		
		// set the biome to start growing 
		
	}public Biome (Area a, int type) { // stage 2
		
	}
	
	public float getWeight (Area a) {
		// finds the weight required to grow into this area based on the biome type
		
	}
	
	public float getSize (int type) {
		Random rand = new Random();
		float s = rand.nextFloat();
		switch (type) {
		case 0:
			return s*100;
		case 1:
			return s*20;
		case 2:
			return s*80;
		case 3:
			return s*140;
		case 4:
			return s*150;
		case 5:
			return s*100;
		case 6:
			return s*80;
		case 7:
			return s*60;
		case 8:
			return s*80;
		case 9:
			return s*20;
		case 10:
			return s*20;
		case 11:
			return s*20;
		case 12:
			return s*20;
		case 13:
			return s*20;
		case 14:
			return s*20;
		case 15:
			return s*20;
		case 16:
			return s*20;
	}
	
	/* Types
	 *  
	 * 0 = grasslands
	 * 1 = floodplain - s2
	 * 2 = arid
	 * 3 = desert
	 * 4 = polar
	 * 5 = plains 
	 * 6 = hills 
	 * 7 = badlands
	 * 8 = mountains
	 * 9 = glacial - s2
	 * 
	 * 10 = woodland
	 * 11 = forest
	 * 12 = jungle 
	 * 
	 * 13 - shallow seas
	 * 14 - ocean
	 * 15 - coastline - s2
	 * 16 - coral reefs - s2
	 * 
	 */
	
}
