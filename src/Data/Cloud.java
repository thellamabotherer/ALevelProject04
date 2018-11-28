package Data;

public class Cloud extends Weather {
	
	protected Area lastArea;
	protected Area area;
	private float heat;
	private float water;
	protected float ttl;
	
	public Cloud (Area a) {
		// give the current an amount of heat 
		this.area = a;
		this.heat = a.getOceanTemp();
		if (a.isOcean()) {
			this.water = this.heat;
		}else {
			this.water = this.heat * a.getWater();
		}
		this.ttl = 50;
	}
	
	protected Area findNext () {
		return area.getNextOcean();
	}
	
	protected void changeClimate () {
		// put some new heat into the local area
		area.setOceanTemp((float) (area.getOceanTemp() + this.heat + 0.03));
		this.heat = this.heat * (float)0.97;
	
		if (area.getAltitude() > lastArea.getAltitude() || this.water > 0) {
			area.setWater(area.getWater() + (this.water * 10 * (area.getAltitude() - lastArea.getAltitude())));
			this.water = this.water * 10 * (1 - (area.getAltitude() - lastArea.getAltitude()));
		}area.setWater((float) (area.getWater() + 0.05 * this.water));
		this.water = this.water * 95;
		
	}
	
}
