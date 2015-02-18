package maytayo.esgi.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import maytayo.esgi.bean.Meteo;
import maytayo.esgi.utils.Utils;

import maytayo.esgi.R;
import maytayo.esgi.R.id;
import maytayo.esgi.R.layout;

import android.app.AlertDialog;
import android.app.Application;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener{

	private TextView text;
	private List<String> listValues;
	private SearchView mSearchView;
	protected MonApplication app;
	protected LocationManager lm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		app = (MonApplication)getApplication();

		listValues = new ArrayList<String>();
		Utils.FillCityList(listValues);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//Search
		mSearchView = (SearchView) findViewById(R.id.searchView1);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);

		//Favoris
		ImageButton imageFavoris = (ImageButton) findViewById(R.id.imageFav);
		imageFavoris.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MonApplication app = (MonApplication)v.getContext().getApplicationContext();
				if(app.favoris.iterator().hasNext()){
					Intent intent = new Intent(v.getContext(),FavorisActivity.class);
					startActivity(intent);
				}
				else
				{
					String message = "Vous n'avez pas encore de Favoris.\nSélectionnez une ville pour qu'elle s'ajoute aux favoris";
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}

		});

		//Location
		ImageButton imageLocation = (ImageButton) findViewById(R.id.imageLocation);
		imageLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(location != null)
				{
					double longitude = location.getLongitude();
					double latitude = location.getLatitude();
					app.currentMeteo = Utils.getCityMeteo(latitude,longitude,app);
					Intent intent = new Intent(v.getContext(),MeteoActivity.class);
					startActivity(intent);
				}
				else
				{
					String message = "Désolé!\nNous ne parvenons pas à vous localiser.";
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
			}
		});

		// initiate the listadapter
		ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this, 
				R.layout.row_layout, R.id.listText, listValues);

		// assign the list adapter
		setListAdapter(myAdapter);

	}

	public boolean onQueryTextChange(String newText) {

		if(!newText.equals("")){
			Utils.FillCitySearchList(listValues, newText);
			ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this, 
					R.layout.row_layout, R.id.listText, listValues);
			setListAdapter(myAdapter);
		}
		else
		{
			Utils.FillCityList(listValues);
			ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this, 
					R.layout.row_layout, R.id.listText, listValues);
			setListAdapter(myAdapter);

		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		Utils.goToMeteo(this, arg0, app);
		return true;
	}

	// when an item of the list is clicked
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		String selectedItem = (String) getListView().getItemAtPosition(position);
		Utils.goToMeteo(this,selectedItem,app);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences settings = getSharedPreferences("PREFS_METEO", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove("favoris");
		editor.putStringSet("favoris", app.favoris);
		editor.commit();
	}

}
