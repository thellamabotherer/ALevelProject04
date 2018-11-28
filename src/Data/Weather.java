package Data;

public class Weather {

	protected Area lastArea;
	protected Area area;
	protected int ttl;

	public Weather() {
		// I think the subclases handle this
	}

	public void walk() {
		if (ttl > 0) {
			this.lastArea = this.area;
			try {
				this.area = this.findNext();
				this.changeClimate();
				ttl--;
				this.walk();
			} catch (NullPointerException e) {
				ttl = 0;
			}
		}
	}

	protected Area findNext() {

		// subclasses will deal with this
		return null;

	}

	protected void changeClimate() {

	}

}
