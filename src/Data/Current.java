package Data;

public class Current extends Weather {

	protected Area lastArea;
	protected Area area;
	private float heat;
	protected float ttl;
	
	public Current (Area a) {
		// give the current an amount of heat 
		this.area = a;
		this.heat = a.getOceanTemp();
		this.ttl = 50;
	}
	
	protected Area findNext () {
		return area.getNextOcean();
	}
	
	protected void changeClimate () {
		// put some new heat into the local area
		area.setOceanTemp((float) (area.getOceanTemp() + this.heat + 0.05));
		this.heat = this.heat * (float)0.95;
	}
	
}
