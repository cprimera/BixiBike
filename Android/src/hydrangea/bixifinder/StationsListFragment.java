package hydrangea.bixifinder;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hydrangea.bixifinder.models.Station;

public class StationsListFragment extends ListFragment {

	ArrayList<Station> mStations;
	StationAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstacneState) {

		View view = inflater.inflate(R.layout.list_fragment_stations,
				container, false);

		mStations = getStations();
		
		// Create Array adapter with mStations
		mAdapter = new StationAdapter(getActivity(), R.layout.list_item_station, mStations);
		
		

		return view;
	}

	private ArrayList<Station> getStations() {
		ArrayList<Station> list = new ArrayList<Station>();
		
		for(int i = 0; i < 10; i++) {
			Station s = new Station("Station " + i, i, 10-i);
			list.add(s);
		}
		
		return list;
	}

	
	// Creates an adapter for the Station List
	private class StationAdapter extends ArrayAdapter<Station> {

		private ArrayList<Station> mItems;
		private Context mContext;
		int mTextViewResource;

		public StationAdapter(Context context, int textViewResource, ArrayList<Station> items) {
			super(context, textViewResource, items);

			this.mItems = items;
			this.mContext = context;
			this.mTextViewResource = textViewResource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(mTextViewResource, null);
			}

			Station station  = mItems.get(position);
			((TextView) view.findViewById(R.id.stationName)).setText(station.getStationName());

			// Get the necessary views from the layout, that we want to change

			return view;
		}

	}
	
	
	public interface OnStationSelectedListener {
		public void onStationSelected(Station station);
	}
}
