package maytayo.esgi.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import maytayo.esgi.activity.MonApplication;

import maytayo.esgi.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter { 
	private ArrayList<String> list = new ArrayList<String>(); 
	private Context context; 

	public MyCustomAdapter(ArrayList<String> list, Context context) { 
		this.list = list; 
		this.context = context; 
	} 

	@Override
	public int getCount() { 
		return list.size(); 
	} 

	@Override
	public Object getItem(int pos) { 
		return list.get(pos);
	} 

	@Override
	public long getItemId(int pos) { 
		return 0;
		//just return 0 if your list items do not have an Id variable.
	} 

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			view = inflater.inflate(R.layout.row_favoris_layout, null);
		} 

		//Handle TextView and display string from your list
		TextView listItemText = (TextView)view.findViewById(R.id.listTextFavoris); 
		listItemText.setText(list.get(position)); 

		//Handle buttons and add onClickListeners
		ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.imageDelete);

		deleteBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) { 
				//do something

				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							MonApplication app = (MonApplication) context.getApplicationContext();
							app.favoris.remove(list.get(position));
							list.remove(position); //or some other task
							notifyDataSetChanged();
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							//No button clicked
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Voulez-vous vraiment supprimer cette ville de vos favoris ?").setPositiveButton("Oui", dialogClickListener)
				.setNegativeButton("Non", dialogClickListener).show();


			}
		});

		return view; 
	} 
}
