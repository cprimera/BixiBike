package hydrangea.bixifinder;

import android.app.Activity;
import android.util.Log;
import hydrangea.bixifinder.models.Station;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataConnector {

    private static final String LOG_TAG = "BIXI_FINDER";

	private static DataConnector sInstance;
	private static final String url = "https://toronto.bixi.com/data/bikeStations.xml";

    private InputStream is = null;

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

        Log.d(LOG_TAG, "Downloading Stations");

//		// Center the map around user's last known location
//		LocationManager locationManager = (LocationManager) activity
//				.getSystemService(Context.LOCATION_SERVICE);
//
//		// We just need the rough location, don't need an accurate location
//		String locationProvider = LocationManager.NETWORK_PROVIDER;
//
//		// Finding the location takes time, last known location would be
//		// sufficient for our needs
//		Location lastKnownLocation = locationManager
//				.getLastKnownLocation(locationProvider);
//
//        double latitude = 43.6481;
//        double longitude = 79.4042;
//
//        if(lastKnownLocation == null) {
//            lastKnownLocation = new Location("");
//		    lastKnownLocation.setLatitude(latitude);
//		    lastKnownLocation.setLongitude(longitude);
//        }

		Parser p = new Parser();
		ArrayList<Station> list = null;
		try {
			list = p.parse(downloadUrl(url));

            Log.d(LOG_TAG, "Number of stations 1: " + list.size());

			for (Station s : list) {
//				Location loc = new Location("");
//				loc.setLatitude(s.getLat());
//				loc.setLongitude(s.getLng());
//
//				double dist = loc.distanceTo(lastKnownLocation);
//				s.setDist(dist);

			}
		} catch (Exception e) {
            e.printStackTrace();
		}

        Log.d(LOG_TAG, "Number of stations: " + list.size());

		if (list != null) {
//			Collections.sort(list);

			for (Station s : list) {
				mStations.add(s);
			}
		}

        // Close the input stream used to download the stations list
        try {
            if(is != null){
                is.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

	}

	private InputStream downloadUrl(String myurl) throws IOException {
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

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch (Exception e){
            e.printStackTrace();
        }

        return null;
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
