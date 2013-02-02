package hydrangea.bixifinder;

import hydrangea.bixifinder.models.Station;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
			for (Station s : list) {
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
