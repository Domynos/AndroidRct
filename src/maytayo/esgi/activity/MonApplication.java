package maytayo.esgi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import maytayo.esgi.bean.Meteo;


import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class MonApplication extends Application {

	public Set favoris = new HashSet();
	public Meteo currentMeteo;
	public List<Meteo> cache_meteo = new ArrayList<Meteo>();

	@Override
	public void onCreate()
	{
		super.onCreate();

		SharedPreferences settings = getSharedPreferences("PREFS_METEO", 0);
		if(settings.getStringSet("favoris", null) != null){
		favoris = settings.getStringSet("favoris", null);
		Log.v("Application", "favoris set");

			/*Iterator<String> it = favoris.iterator();
			while (it.hasNext()) {
				Log.v("APPLICATION",it.next());
			}*/
		}
		initSingletons();
	}

	public MonApplication(){}

	protected void initSingletons()
	{
		// Initialize the instance of MySingleton
		MySingleton.initInstance();
	}
	
}
