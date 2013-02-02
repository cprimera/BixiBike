package hydrangea.bixifinder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.*;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment implements OnStationsFetchedListener, OnMarkerClickListener {

	private int mSelected;
	private Station mStation;
	private ArrayList<Station> mStations;
	private ArrayList<Marker> mMarkers;
	private GoogleMap map;
	
	private Marker mUserMarker;

	public MapFragment() {
		// req'd empty constructor
	}

	public MapFragment(Station station) {
		mStation = station;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map_fragment_stuffer, container);

		Activity ac = getActivity();

		map = ((SupportMapFragment) (getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map))).getMap();

		// Center the map around user's last known location
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// We just need the rough location, don't need an accurate location
		String locationProvider = LocationManager.NETWORK_PROVIDER;

		// Finding the location takes time, last known location would be
		// sufficient for our needs
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(locationProvider);

		double latitude = lastKnownLocation.getLatitude();
		double longitude = lastKnownLocation.getLongitude();

		LatLng pos = new LatLng(latitude, longitude);
		
	    // Move the camera instantly to user position with a zoom of 5.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

	    
	    mUserMarker = map.addMarker(new MarkerOptions().
				position(pos).
				title("You're Here").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
	    
	    
	    // Add listeners for markers
	    map.setOnMarkerClickListener(this);
	    
		return view;
	}

	public void updateDetails() {
		LatLng pos = new LatLng(mStation.getLat(), mStation.getLng());
		map.animateCamera(CameraUpdateFactory.newLatLng(pos), 1000, null);
		mMarkers.get(mSelected).showInfoWindow();
	}

	public void setStation(int station) {
		mSelected = station;
		mStation = mStations.get(station);
	}

	@Override
	public void onStationsFetched() {
		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();
		mMarkers = new ArrayList<Marker>();
		for (Station s : mStations) {
			LatLng pos = new LatLng(s.getLat(), s.getLng());

			Marker stationMarker = map.addMarker(new MarkerOptions().
					position(pos).
					title(s.getStationName()).
					snippet("Free Bikes: " + s.getBikes() + "\t Free Docks: " + s.getDocks())
					);
			
			mMarkers.add(stationMarker);

		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		
		return false;
	}
}
