package hydrangea.bixifinder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import hydrangea.bixifinder.models.Station;
import java.util.ArrayList;

public class StationsListFragment extends ListFragment{

	ArrayList<Station> mStations;
	StationAdapter mAdapter;
	OnStationSelectedListener mCallback;

    private static String LOG_TAG = "BIXI_FINDER";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstacneState) {

		View view = inflater.inflate(R.layout.list_fragment_stations,
				container, false);

		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();

		// Create Array adapter with mStations
		mAdapter = new StationAdapter(getActivity(),
				R.layout.list_item_station, mStations);

		this.setListAdapter(mAdapter);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnStationSelectedListener) activity;

		} catch (Exception e) {

		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		mCallback.onStationSelected(position);
	}

	// Creates an adapter for the Station List
	private class StationAdapter extends ArrayAdapter<Station> {

		private ArrayList<Station> mItems;
		private Context mContext;
		int mTextViewResource;

		public StationAdapter(Context context, int textViewResource,
				ArrayList<Station> items) {
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

			Station station = mItems.get(position);
			((TextView) view.findViewById(R.id.stationName)).setText(station
					.getStationName());
			
			((TextView) view.findViewById(R.id.numBikes)).setText("Free Bikes: " + station.getBikes());
			
			((TextView) view.findViewById(R.id.numDocks)).setText("Free Docks: " + station.getDocks());

			// Get the necessary views from the layout, that we want to change

			return view;
		}

		public void updateStationsList(ArrayList<Station> list) {
			this.mItems = list;
			Log.d("TAG ITEMS", Integer.toString(mItems.size()));
		}

	}

	public interface OnStationSelectedListener {
		public void onStationSelected(int station);
	}

	public void onStationsFetched() {
		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();

        Log.d(LOG_TAG, "Updating mStations list, entries: " + mStations.size());

		mAdapter.updateStationsList(mStations);
		mAdapter.notifyDataSetChanged();
	}
}
