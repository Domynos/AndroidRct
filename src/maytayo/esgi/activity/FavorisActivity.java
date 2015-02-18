package maytayo.esgi.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import maytayo.esgi.adapter.MyCustomAdapter;
import maytayo.esgi.bean.Meteo;
import maytayo.esgi.utils.Utils;

import maytayo.esgi.R;
import maytayo.esgi.R.id;
import maytayo.esgi.R.layout;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class FavorisActivity extends ListActivity {

	private List<String> listValues;
	protected MonApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favoris);

		//Back boutton
		ImageButton backImage = (ImageButton) findViewById(R.id.backButton);
		backImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		app = (MonApplication)getApplication();

		listValues = new ArrayList<String>();
		
		Utils.FillCityListFavoris(listValues,app);
		
		//Supprimer une ville
	    MyCustomAdapter myAdapter = new MyCustomAdapter((ArrayList<String>)listValues, this);

		// assign the list adapter
		setListAdapter(myAdapter);
	}

	// when an item of the list is clicked
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
			super.onListItemClick(list, view, position, id);
			String selectedItem = (String) getListView().getItemAtPosition(position);
			Utils.goToMeteo(this, selectedItem, app);
		}
}
