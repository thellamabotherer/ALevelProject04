package Main;

public class WorldConstraints {
	
	// for making the heightmap only with tectonics
	
	public static final float seaLevel = 0;
	public static final float continentalBase = (float) 0.2;
	public static final float oceanicBase = (float) -0.4;
	
	public static final float oceanProb = (float) 0.5;
	
	public static float maxHeight = continentalBase;
	public static float minHeight = oceanicBase;
	
	public static final float contOnContHeight = 1;
	public static final float contOnContSteep = 1;
	
	public static final float contOnOceanHeight = (float) 0.5;
	public static final float contOnOceanSteep = (float)1;
	public static final float contOnOceanSetback = (float)1.5;
	
	public static final float oceanOnContHeight = (float)0.5;
	public static final float oceanOnContSteep = (float)0.8;
	
	public static final float oceanOnOceanHeight = (float)0.4;
	public static final float oceanOnOceanSteep = (float)0.5;
	
	public static final float islandArcHeight = (float)0.3;
	public static final float islandArcSteep = 1;
	public static final float islandArcSetback = 2;
	
	public static final float riftValleyHeight = (float) 0.1;
	public static final float riftValleySteep = (float)1;
	public static final float riftValleySetback = (float)4.4;
	public static final float riftValleyBase = (float)2;
	
	
	public static final float contFromOceanSetback = (float) 1; // a
	public static final float contFromOceanSteep = (float) 0.5; // b
	public static final float contFromOceanHeight = (float) 0.4; // f
	
	public static final float oceanFromContSetback = (float) 2; 
	public static final float oceanFromContSteep = (float) 0.4; 
	public static final float oceanFromContHeight = (float) 0.2; 
	
	public static final float oceanRidgeHeight = (float)0.4;
	public static final float oceanRidgeSteep = 0.5;
	
	
}
