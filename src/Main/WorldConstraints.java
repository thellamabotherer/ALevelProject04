package Main;

public class WorldConstraints {
	
	// for making the heightmap only with tectonics
	
	public static final float seaLevel = 0;
	public static final float continentalBase = (float) 0.2;
	public static final float oceanicBase = (float) -0.4;
	
	public static float maxHeight = continentalBase;
	public static float minHeight = oceanicBase;
	
	public static final float continentOnContinentBase = 1;
	public static final float continentOnContinentSteep = (float) 0.01;
	public static final float continentOnContinentRand = (float) 0.1;
	
	
	public static final float continentOnOceanBase = (float) 0.5;
	public static final float continentOnOceanSteep = (float) 0.05;
	public static final float continentOnOceanSetback = (float) 0.01;
	public static final float continentOnOceanRand = (float) 0.05;
	
	
	public static final float oceanOnContinentBase = (float) 0.8;
	public static final float oceanOnContinentSteep = (float) 0.01;
	public static final float oceanOnContinentRand = (float) 0.01;
	
	
	public static final float oceanOnOceanBase = (float) 0.45;
	public static final float oceanOnOceanSteep = (float) 0.1;
	public static final float oceanOnOceanRand = (float) 0.2;
	
	
	// these bits have now been deprecated but removing them means loads of errors
	
	public static final float bigMountainsLower = (float) 0.2;
	public static final float bigMountainsHigher = (float) 0.7;
	public static final float smallMountainsLower = (float) 0.05;
	public static final float smallMountainsHigher = (float) 0.2;
	public static final float mediumMountainsLower = (float) 0.15;
	public static final float mediumMountainsHigher = (float) 0.4;
	
	public static final float riftValleyLower = (float) -0.01;
	public static final float riftValleyHigher = (float) -0.05;
	public static final float oceanTrenchLower = (float) -0.4;
	public static final float oceanTrenchHigher = (float) -0.6;
	
	
	
}
