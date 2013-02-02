package hydrangea.bixifinder;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hydrangea.bixifinder.models.Station;

public class StationsListFragment extends ListFragment {

	ArrayList<Station> mStations;
	//StationListAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstacneState) {

		View view = inflater.inflate(R.layout.list_fragment_stations,
				container, false);

		mStations = getStations();
		
		// Create Array adapter with mStations
		// mAdapter = new StationListAdapter();
		
		// Create list view with mAdapter and R.layout.list_item_station
		// also create R.layout.list_item_station

		return view;
	}

	private ArrayList<Station> getStations() {
		ArrayList<Station> list = new ArrayList<Station>();
		
		for(int i = 0; i < 10; i++) {
			Station s = new Station();
			list.add(s);
		}
		
		return list;
	}

	
	// Array Adapter here
	
	
	public interface OnStationSelectedListener {
		public void onStationSelected(Station station);
	}
}
