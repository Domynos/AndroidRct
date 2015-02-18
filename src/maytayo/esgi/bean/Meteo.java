package maytayo.esgi.bean;

import java.util.Date;

public class Meteo {
	
	public String cityName;
	public String countryName;
	public float temperature;
	public String ciel;
	public String icone;
	public String vent;
	public Date lastUpdate;
	
	public Meteo(String cityName, String countryName, float temp, String ciel, String icone, String vent) {
		super();
		this.cityName = cityName;
		this.countryName = countryName;
		this.temperature = temp;
		this.icone = icone;
		this.ciel = ciel;
		this.vent = vent;
		lastUpdate = new java.util.Date();
	}
	
}
