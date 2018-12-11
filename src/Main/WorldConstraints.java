package Main;

public class WorldConstraints {
	
	// Main
	
	public static final int HEIGHT = 1080;
	public static final int WIDTH = 1920;
	
	// for making the heightmap only with tectonics
	
	public static final float seaLevel = 0;
	public static final float continentalBaseMax = (float) 0.3;
	public static final float continentalBaseMin = (float) 0.1;
	public static final float oceanicBaseMax = (float) -0.2;
	public static final float oceanicBaseMin = (float) -0.5;
	
	public static final float contOnContHeight = 1;
	public static final float contOnContSteep = 1;
	
	public static final float contOnOceanHeight = (float) 0.1;
	public static final float contOnOceanSteep = (float)1.2;
	public static final float contOnOceanSetback = (float)1.5;
	
	public static final float oceanOnContHeight = (float)0.5;
	public static final float oceanOnContSteep = (float)0.8;
	
	public static final float oceanOnOceanHeight = (float)0.4;
	public static final float oceanOnOceanSteep = 1;
	public static final float islandArcHeight = (float)0.3;
	public static final float islandArcSudden = 2;
	
	public static final float riftValleyHeight = (float) 0.2;
	public static final float riftValleyBase = 1;
	public static final float riftValleySteep = 1;
	public static final float riftValleyDeepest = 1;
	
	public static final float contFromOceanHeight = (float) 0.5;
	public static final float contFromOceanSteep = (float) 3.6;
	public static final float contFromOceanStart = (float) 1.5;
	
	public static final float oceanFromContHeight = (float) 0.8;
	public static final float oceanFromContSteep = (float) 3.6;
	public static final float oceanFromContStart = (float) 1.5;
	
	public static final float oceanRidgeHeight = (float)0.4;
	public static final float oceanRidgeSteep = 1;
	
	// for the weather sims 
	
	public static final int seaCentres = 5;
	public static final int airCentres = 8;
	
	public static final float epicentreWeightCoefficient = (float)2;
	
	public static final int currentSims = 1;
	public static final int airSims = 1;
	
	public static final float baseWeight = 1;
	public static final float windPower = 2;
	public static final float heightPower = 2;
	
	
	
}