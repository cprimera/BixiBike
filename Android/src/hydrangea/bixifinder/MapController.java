package hydrangea.bixifinder;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import hydrangea.bixifinder.models.Station;

import java.util.ArrayList;

public class MapController extends SupportMapFragment{

	private int mSelected;
	private Station mStation;
	private ArrayList<Station> mStations;
	private ArrayList<Marker> mMarkers;
	private GoogleMap map;
	
	private Marker mUserMarker;

    private Activity mActivity;

	public MapController(Activity activity) {
        mActivity = activity;
	}

	public void initialize(GoogleMap gmap) {

        map = gmap;
//		map = ((SupportMapFragment) (((FragmentActivity)mActivity).getSupportFragmentManager()
//				.findFragmentById(R.id.map))).getMap();

		// Center the map around user's last known location
		LocationManager locationManager = (LocationManager) mActivity
				.getSystemService(Context.LOCATION_SERVICE);

		// We just need the rough location, don't need an accurate location
		String locationProvider = LocationManager.NETWORK_PROVIDER;

		// Finding the location takes time, last known location would be
		// sufficient for our needs
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(locationProvider);

        double latitude = 43.6481;
        double longitude = 79.4042;
//
//        if(lastKnownLocation != null) {
//		 latitude = lastKnownLocation.getLatitude();
//		 longitude = lastKnownLocation.getLongitude();
//        }

		LatLng pos = new LatLng(latitude, longitude);
		
	    // Move the camera instantly to user position with a zoom of 5.
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5));

	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

	    
	    mUserMarker = map.addMarker(new MarkerOptions().
				position(pos).
				title("You're Here")
                );
	    
	    
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

	public void onStationsFetched() {
		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();
		mMarkers = new ArrayList<Marker>();

        if(map != null) {
            for (Station s : mStations) {
                LatLng pos = new LatLng(s.getLat(), s.getLng());

                Marker stationMarker = map.addMarker(new MarkerOptions().
                        position(pos).
                        title(s.getStationName()).
                        snippet("Free Bikes: " + s.getBikes() + "\t Free Docks: " + s.getDocks()).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling))
                        );

                mMarkers.add(stationMarker);

            }
        }
	}
}
