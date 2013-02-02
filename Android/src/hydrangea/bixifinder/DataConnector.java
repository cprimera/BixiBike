package hydrangea.bixifinder;

import hydrangea.bixifinder.models.Station;

import java.util.ArrayList;

public class DataConnector {
	
	private static DataConnector sInstance;

	// Actual Data
	ArrayList<Station> mStations = new ArrayList<Station>();
	
	public static synchronized DataConnector getInstance() {
		if(sInstance == null){
			sInstance = new DataConnector();
		}
		return sInstance;
	}

	private DataConnector(){

	}
	
	public ArrayList<Station> getStations() {
		return mStations;
	}

	public void downloadStations() {
		
		
		for(int i = 0; i < 10; i++) {
			Station s = new Station("Station " + i, i, 10-i, i, i);
			mStations.add(s);
		}
	}
}
