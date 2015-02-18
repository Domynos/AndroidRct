package maytayo.esgi.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import maytayo.esgi.bean.Meteo;

import maytayo.esgi.R;
import maytayo.esgi.R.drawable;
import maytayo.esgi.R.id;
import maytayo.esgi.R.layout;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MeteoActivity extends Activity {

	HashMap<String, String> mapTrad = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meteo_activity);
		
		mapTrad.put("light rain", "Averses");
		mapTrad.put("scattered clouds", "Nuages dispersés");
		mapTrad.put("broken clouds", "Nuages dispersés");
		mapTrad.put("few clouds", "Quelques nuages");
		mapTrad.put("overcast clouds", "Nuageux");
		mapTrad.put("sky is clear", "Dégagé");
		//mapTrad.put("")

		MonApplication app = (MonApplication)getApplication();
		Meteo currentMeteo = app.currentMeteo;
		
		ImageView backButton = (ImageView)findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView cityName = (TextView)findViewById(R.id.cityName);
		StringBuilder result = new StringBuilder(currentMeteo.cityName);
		result.replace(0, 1, result.substring(0, 1).toUpperCase());
		currentMeteo.cityName = result.toString();
		cityName.setText(currentMeteo.cityName+" ("+currentMeteo.countryName+")");

		TextView temperature = (TextView)findViewById(R.id.temperatureText);
		temperature.setText(currentMeteo.temperature+" °C");

		TextView ciel = (TextView)findViewById(R.id.ciel);
		ciel.setText(mapTrad.get(currentMeteo.ciel.toLowerCase()));

		ImageView img= (ImageView) findViewById(R.id.imageMeteo);
		int resID = this.getResources().getIdentifier("m"+currentMeteo.icone, "drawable", this.getPackageName());
		img.setImageResource(resID);

		RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout);
		Resources res = getResources();
		Drawable drawable;
		switch(currentMeteo.icone){

		case "13d" :
		case "13n" :
			drawable = res.getDrawable(R.drawable.background_snow);
			temperature.setTextColor(Color.GRAY);
			ciel.setTextColor(Color.GRAY);
			cityName.setTextColor(Color.GRAY);
			rl.setBackgroundDrawable(drawable);
			break;

		case "01n" :
		case "02n" :
		case "03n" :
		case "04n" :
		case "09n" :
		case "10n" :
		case "11n" :
			drawable = res.getDrawable(R.drawable.background_night);
			temperature.setTextColor(Color.WHITE);
			ciel.setTextColor(Color.WHITE);
			cityName.setTextColor(Color.WHITE);
			rl.setBackgroundDrawable(drawable);
			break;

		case "01d":
		case "02d":
		case "03d":
		case "04d":
		default:
			drawable = res.getDrawable(R.drawable.background_clear);
			temperature.setTextColor(Color.WHITE);
			ciel.setTextColor(Color.WHITE);
			cityName.setTextColor(Color.WHITE);
			rl.setBackgroundDrawable(drawable);
			break;

		case "09d":
		case "10d":
		case "11d":
			drawable = res.getDrawable(R.drawable.background_rain);
			temperature.setTextColor(Color.WHITE);
			ciel.setTextColor(Color.WHITE);
			cityName.setTextColor(Color.WHITE);
			rl.setBackgroundDrawable(drawable);
			break;
		}



	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
