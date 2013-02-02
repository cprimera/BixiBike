package hydrangea.bixifinder;

import hydrangea.bixifinder.models.Station;
import java.util.Collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class DataConnector {

	private static DataConnector sInstance;
	private static final String url = "https://toronto.bixi.com/data/bikeStations.xml";

	// Actual Data
	ArrayList<Station> mStations = new ArrayList<Station>();

	public static synchronized DataConnector getInstance() {
		if (sInstance == null) {
			sInstance = new DataConnector();
		}
		return sInstance;
	}

	private DataConnector() {

	}

	public ArrayList<Station> getStations() {
		return mStations;
	}

	public void downloadStations(Activity activity) {
			
		// Center the map around user's last known location
		LocationManager locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);

		// We just need the rough location, don't need an accurate location
		String locationProvider = LocationManager.NETWORK_PROVIDER;

		// Finding the location takes time, last known location would be
		// sufficient for our needs
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(locationProvider);

		Parser p = new Parser();
		try {
			ArrayList<Station> list = p.parse(downloadUrl(url));
			for (Station s : list) {
				Location loc = new Location("");
				loc.setLatitude(s.getLat());
				loc.setLongitude(s.getLng());
				
				double dist = loc.distanceTo(lastKnownLocation);
				s.setDist(dist);
				
				mStations.add(s);
			}
		} catch (Exception e) {

		}
		
		Collections.sort(mStations);
	}

	private InputStream downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("DEBUG_TAG", "The response is: " + response);
			is = conn.getInputStream();
			return is;
//			String str = readIt(is, len);
//			
//			Log.d("HUGE ASS STRING", str);
//
//			return str;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				//is.close();
			}
		}
	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}
}
