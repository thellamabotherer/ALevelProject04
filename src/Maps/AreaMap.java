package Maps;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.lwjgl.opengl.Display;

import java.util.Comparator;

import Data.Area;
import Data.Cloud;
import Data.Current;
import Data.Plate;
import Data.Polygon;
import Data.Weather;
import Data.WeatherSystem;
import Main.Main;
import Main.Window;
import Main.WorldConstraints;
import Renderers.HeightRenderer;

public class AreaMap {

	private ArrayList<Area> areas = new ArrayList();
	private HeightRenderer HR;

	private WeatherSystem[] epicentres;

	public AreaMap(HeightMap heightMap, Window w, HeightRenderer hr) {
		double start;
		double end;
		this.HR = hr;
		start = System.nanoTime();
		for (Plate P : heightMap.getPlates()) {
			for (Polygon p : P.getPolys()) {
				areas.add(p.toArea());
			}
		}
		end = System.nanoTime();
		//System.out.println("Setup area map takes " + (end - start));
		start = System.nanoTime();
		for (Area a : areas) {
			a.setupAdjacencies();
		}
		end = System.nanoTime();
		//System.out.println("Setup adj takes " + (end - start));
		start = System.nanoTime();
		this.simWeather();
		end = System.nanoTime();
		//System.out.println("Weather sim 	takes " + (end - start));
		for (Area a : areas) {
			a.getStartConditions();
			a.setupNext(true);
			a.setupNext(false);
		}
		for (int i = 0; i < WorldConstraints.currentSims; i++) {
			runCurrentSim();
			//System.out.println("Current sim");
		}
		for (int i = 0; i < WorldConstraints.airSims; i++) {
			runAirSim();
			//System.out.println("Air sim");
		}
		smoothSeaTemp();
		smoothAirTemp();
		smoothRainfall();
	}

	private void runCurrentSim() {
		ArrayList<Weather> weathers = new ArrayList();
		for (Area a : areas) {
			if (a.isOcean()) {
				weathers.add(new Current(a));
			}
		}
		for (Weather c : weathers) {
			c.walk();
		}
	}

	private void runAirSim() {
		ArrayList<Weather> weathers = new ArrayList();
		for (Area a : areas) {
			weathers.add(new Cloud(a));
		}
		for (Weather c : weathers) {
			c.walk();
		}
	}

	private void simWeather() {

		epicentres = genCentres(WorldConstraints.seaCentres, true);
		for (Area a : this.areas) {
			if (a.isOcean()) {
				a.getCurrentVect(epicentres);
			}
		}
		epicentres = genCentres(WorldConstraints.airCentres, false);
		for (Area a : this.getAreas()) {
			a.getWindVect(epicentres);
		}
		
		for (Area a : this.areas) {
			System.out.println(a.weightedDistToSea(0, 100));
			a.setWater(1 / a.weightedDistToSea(0, 100));
		}

		// walk this area's weather object from here and deposit heat and moisture based
		// on change in altitude until weather is depleted

	}

	private WeatherSystem[] genCentres(int num, boolean sea) {
		Random rand = new Random();
		int temp;
		WeatherSystem[] epicentres = new WeatherSystem[num];
		for (int i = 0; i < num; i++) {
			WeatherSystem epicentre = null;
			while (epicentre == null) {
				temp = rand.nextInt(this.areas.size());
				if ((this.areas.get(temp).isOcean() || !sea)
						&& this.areas.get(temp).getLatitude() > WorldConstraints.HEIGHT * 0.3
						&& this.areas.get(temp).getLatitude() < WorldConstraints.HEIGHT * 0.7) {
					epicentre = new WeatherSystem(WeatherSystem.air, this.areas.get(temp));
				}
			}
			epicentres[i] = epicentre;
		}
		this.epicentres = epicentres;
		return epicentres;
	}

	private void smoothSeaTemp() {

		float maxHeight = Float.MIN_VALUE;
		float minHeight = Float.MIN_VALUE;

		float buffer1;
		int buffer2;

		for (Area a : this.areas) {
			if (a.isOcean()) {
				a.setOceanTemp(a.getOceanTemp() * (2 + a.getAltitude()));
			}
		}

		for (Area a : this.areas) {
			buffer1 = 0;
			buffer2 = 0;
			for (Area b : a.getAdjacencies()) {
				if (b.isOcean()) {
					buffer1 = buffer1 + b.getOceanTemp();
					buffer2++;
				}
			}

			if (buffer2 != 0) {
				a.setTempTemp(buffer1 / buffer2);
			} else {
				a.setTempTemp(a.getOceanTemp());
			}
		}

		for (Area a : this.areas) {
			a.setOceanTemp(a.getTempTemp());
		}

		for (Area a : this.areas) {

			if (a.getOceanTemp() > maxHeight) {
				maxHeight = a.getOceanTemp();
			}
			if (a.getOceanTemp() < minHeight) {
				minHeight = a.getOceanTemp();
			}

		}

		//System.out.println(maxHeight);
		//System.out.println(minHeight);

		for (Area a : this.areas) {

			if (a.getOceanTemp() > 0) {
				a.setOceanTemp((float) Math.sqrt(a.getOceanTemp() / (maxHeight)));
			} else {
				a.setOceanTemp((float) -Math.sqrt(a.getOceanTemp() / (minHeight)));
			}
			
			a.setOceanTemp((float) (1.5 * a.getOceanTemp() * Math.sqrt(1 - Math.abs(WorldConstraints.HEIGHT/2 - a.getLatitude()) / WorldConstraints.HEIGHT)));
			
		}
	}

	private void smoothAirTemp() {

		float maxHeight = Float.MIN_VALUE;
		float minHeight = Float.MIN_VALUE;

		float buffer1;
		int buffer2;

		for (Area a : this.areas) {
			if (!a.isOcean()) {
				a.setAirTemp(a.getAirTemp() * (1 - a.getAltitude()));
			}
		}

		for (Area a : this.areas) {

			buffer1 = 0;
			buffer2 = 0;
			for (Area b : a.getAdjacencies()) {
				buffer1 = buffer1 + b.getAirTemp();
				buffer2++;
			}

			if (buffer2 != 0) {
				a.setTempTemp(buffer1 / buffer2);
			} else {
				a.setTempTemp(a.getAirTemp());
			}
		}

		for (Area a : this.areas) {
			a.setAirTemp(a.getTempTemp());
		}

		for (Area a : this.areas) {

			if (a.getAirTemp() > maxHeight) {
				maxHeight = a.getAirTemp();
			}
			if (a.getAirTemp() < minHeight) {
				minHeight = a.getAirTemp();
			}

		}

		//System.out.println(maxHeight);
		//System.out.println(minHeight);

		for (Area a : this.areas) {

			if (a.getAirTemp() > 0) {
				a.setAirTemp((float) Math.sqrt(a.getAirTemp() / (maxHeight)));
			} else {
				a.setAirTemp((float) -Math.sqrt(a.getAirTemp() / (minHeight)));
			}
		

			a.setAirTemp((float) (1.5 * a.getAirTemp() * Math.sqrt(1 - Math.abs(WorldConstraints.HEIGHT/2 - a.getLatitude()) / WorldConstraints.HEIGHT)));
			
		
		}
	}

	private void smoothRainfall() {

		float maxHeight = Float.MIN_VALUE;
		float minHeight = Float.MIN_VALUE;

		for (Area a : this.areas) {

			if (a.getWater() > maxHeight) {
				maxHeight = a.getWater();
			}
			if (a.getWater() < minHeight) {
				minHeight = a.getWater();
			}

		}
		for (Area a : this.areas) {

			if (a.getWater() > 0) {
				a.setWater(a.getWater() / maxHeight);
			} else {
				a.setWater(a.getWater() / -minHeight);
			}
		}

		for (Area a : this.areas) {
			if (a.getOceanTemp() < 0.1) {
				int n = 0;
				float t = 0;
				for (Area b : a.getAdjacencies()) {
					if (b.isOcean()) {
						n++;
						t = t + b.getWater();
					}
				}
				if (n == 0) {
					a.setWater(1);
				} else {
					a.setWater(t / n);
				}
			}
		}
	}

	// ----------------------- graphical stuff :( ----------------------------------

	// -------------

	public ArrayList<Area> getAreas() {
		return areas;
	}

	public void setAreas(ArrayList<Area> areas) {
		this.areas = areas;
	}

	public WeatherSystem[] getEpicentres() {
		return epicentres;
	}

	public void setEpicentres(WeatherSystem[] epicentres) {
		this.epicentres = epicentres;
	}

	// --------------------------

}