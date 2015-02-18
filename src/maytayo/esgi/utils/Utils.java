package maytayo.esgi.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import maytayo.esgi.activity.MeteoActivity;
import maytayo.esgi.activity.MonApplication;
import maytayo.esgi.bean.Meteo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public final static String TAG = "Parse XML";
	
	static String[] citys = {"Paris","Nice","Milan","Berlin","Munich","Madrid","Barcelone","Nantes","Pekin","Tokyo",
		"New York","Punta Cana","Londres","Vienne","Reykjavik","Bamako","Lavillequexistepas"};

	public static boolean checkXML(Document xml){
		if(xml.getElementsByTagName("current") != null)
			return true;
		else
			return false;
	}

	public static void FillCityList(List<String> _list){
		_list.clear();
		for (String aCity : citys) {
			_list.add(aCity);
		}
	}



	public static void FillCitySearchList(List<String> _list,String query){
		_list.clear();
		for (String aCity : citys) {
			if(aCity.toLowerCase().contains(query.toLowerCase()))
				_list.add(aCity);
		}
	}

	public static Meteo getCityMeteo(double _lat, double _long, MonApplication app){
		Meteo returnMeteo;

		String url = "http://api.openweathermap.org/data/2.5/weather?lat="+_lat+"&lon="+_long+"&mode=xml";
		Log.v("UTILS", "APPEL DE L'URL POISTION COURANTE !!!!!!!");
		// or if you prefer DOM:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			if(!checkXML(doc))
				return null;
			Node current = doc.getElementsByTagName("current").item(0);

			Node cityNode = current.getChildNodes().item(1);
			Element cityElement = (Element) cityNode;
			String name = cityElement.getAttribute("name");
			
			Node countryNode = cityNode.getChildNodes().item(3);
			Element countryElement = (Element)countryNode;
			String country = countryElement.getTextContent();

			Node temperatureNode = current.getChildNodes().item(3);
			Element eElement = (Element) temperatureNode;
			Float temperature = Float.parseFloat(eElement.getAttribute("value"));
			temperature = (float) (temperature - 272.15);
			temperature = (float) (Math.floor(temperature*10));
			temperature = temperature / 10;

			Node cielNode = current.getChildNodes().item(17);
			Element cElement = (Element) cielNode;
			String icone = cElement.getAttribute("icon");
			String ciel = cElement.getAttribute("value");

			returnMeteo = new Meteo(name,country,temperature, ciel, icone, "");
			app.cache_meteo.add(returnMeteo);
			return returnMeteo;

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Meteo getCityMeteo(String name,MonApplication app){

		Meteo returnMeteo;

		String url = "http://api.openweathermap.org/data/2.5/weather?q="+myReplace(name.toLowerCase()," ","%20")+"&mode=xml";
		// or if you prefer DOM:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());
			if(!checkXML(doc))
				return null;

			Node current = doc.getElementsByTagName("current").item(0);

			Node cityNode = current.getChildNodes().item(1);
			Node countryNode = cityNode.getChildNodes().item(3);
			Element countryElement = (Element)countryNode;
			String country = countryElement.getTextContent();
			
			Node temperatureNode = current.getChildNodes().item(3);
			Element eElement = (Element) temperatureNode;
			Float temperature = Float.parseFloat(eElement.getAttribute("value"));
			temperature = (float) (temperature - 272.15);
			temperature = (float) (Math.floor(temperature*10));
			temperature = temperature / 10;

			Node cielNode = current.getChildNodes().item(17);
			Element cElement = (Element) cielNode;
			String icone = cElement.getAttribute("icon");
			String ciel = cElement.getAttribute("value");

			returnMeteo = new Meteo(name,country,temperature, ciel, icone, "");
			app.cache_meteo.add(returnMeteo);
			return returnMeteo;

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static void FillCityListFavoris(List<String> listValues,MonApplication app) {
		// TODO Auto-generated method stub
		listValues.clear();
		Iterator<String> it = app.favoris.iterator();
		while (it.hasNext()) {
			listValues.add(it.next());
		}

	}

	static String myReplace(String theString, String target, String newString){
		int begin = theString.indexOf(target);
		if(begin != -1)
		{
			int end  = theString.indexOf(target)+target.length();
			String tempB = theString.substring(0,begin);
			String tempE = theString.substring(end);

			String returnString = tempB+newString+tempE;
			returnString = myReplace(returnString,target,newString);
			return returnString;
		}

		return theString;
	}

	public static void goToMeteo(Activity activity, String selectedItem, MonApplication app) {
		// TODO Auto-generated method stub
		boolean findInCache = false, fakeCity = false;
		
		//Gestion du cache
		for (Meteo aMeteo : app.cache_meteo) {
			if(aMeteo.cityName.toLowerCase().equals(selectedItem.toLowerCase())){
				findInCache = true;
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				cal1.setTime(aMeteo.lastUpdate);
				cal2.setTime(new java.util.Date());
				boolean sameHour = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
						cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
						cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
				if(!sameHour){
					app.cache_meteo.remove(aMeteo);
					app.currentMeteo = Utils.getCityMeteo(selectedItem,app);
				}
				else
					app.currentMeteo = aMeteo;
			}
		}
		if(!findInCache){
			Meteo newMeteo;
			if((newMeteo = Utils.getCityMeteo(selectedItem,app))!= null){
				app.currentMeteo = newMeteo;
				app.favoris.add(selectedItem);
			}
			else{
				String message = selectedItem+" : ville non valide !";
				Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				fakeCity = true;
			}
		}
		if(!fakeCity){
			Intent intent = new Intent(activity,MeteoActivity.class);
			activity.startActivity(intent);
		}
	}
}
