package hydrangea.bixifinder;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import hydrangea.bixifinder.models.Station;

public class MapFragment extends Fragment {

	private Station mStation;
	
	public MapFragment(Station station) {
		mStation = station;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment_stuffer, container);
		return view;
	}
	
	public void updateDetails() {
		// Do something here Will
	}
	
	public void setStation(Station station) {
		mStation = station;
	}
}
