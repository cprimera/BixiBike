package hydrangea.bixifinder;

import hydrangea.bixifinder.models.Station;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

	public void downloadStations() {

		Parser p = new Parser();
		try {
			ArrayList<Station> list = p.parse(downloadUrl(url));
			for(Station s: list) {
				mStations.add(s);
			}
		} catch (Exception e) {

		}
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

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}
