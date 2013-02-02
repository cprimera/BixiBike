package hydrangea.bixifinder;

import java.util.ArrayList;

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
import hydrangea.bixifinder.MainActivity.OnStationsFetchedListener;
import hydrangea.bixifinder.models.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment implements OnStationsFetchedListener{

	private Station mStation;
	private ArrayList<Station> mStations;
	private GoogleMap map;
	
	public MapFragment() {
		//req'd empty constructor
	}
	
	public MapFragment(Station station) {
		mStation = station;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment_stuffer, container);
		
		Activity ac = getActivity();
		
		map = ((SupportMapFragment)(getActivity().getSupportFragmentManager().findFragmentById(R.id.map))).getMap();
		return view;
	}
	
	public void updateDetails() {
		// Do something here Will
	}
	
	public void setStation(Station station) {
		mStation = station;
	}

	@Override
	public void onStationsFetched() {
		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();
		for(Station s: mStations) {
			LatLng pos = new LatLng(s.getLat(), s.getLng());		
			
			Marker stationMarker = map.addMarker(new MarkerOptions().position(pos)
			        .title(s.getStationName()));
			
			
		}
	}
}
