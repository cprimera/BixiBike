package hydrangea.bixifinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	private int mSelected = -1;
	private Station mStation;
	private ArrayList<Station> mStations;
	private ArrayList<Marker> mMarkers;
	private GoogleMap map;

    public MapController() {
        super();
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        map = this.getMap();

        LatLng pos = DataConnector.getInstance().getUserLocation(getActivity());

        if(DataConnector.getInstance().getStations().size() > 0) {
            populateStations();
        }

        if(mSelected == -1){
            // Move the camera instantly to user position with a zoom of 5.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 5));

            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }


        map.addMarker(new MarkerOptions().
                position(pos).
                title("You're Here")
        );

        updateDetails();

        return view;
    }

	public void updateDetails() {
        if(mSelected != -1){
            if(mStation == null) mStation = mStations.get(mSelected);
            LatLng pos = new LatLng(mStation.getLat(), mStation.getLng());
            map.animateCamera(CameraUpdateFactory.newLatLng(pos), 1000, null);
            mMarkers.get(mSelected).showInfoWindow();

            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
	}

	public void setStation(int station) {
		mSelected = station;
		if(mStations != null) mStation = mStations.get(station);
	}

	public void populateStations() {
		DataConnector dc = DataConnector.getInstance();
		mStations = dc.getStations();
		mMarkers = new ArrayList<Marker>();

        if(map != null) {
            for (Station s : mStations) {
                LatLng pos = new LatLng(s.getLat(), s.getLng());

                Marker stationMarker = map.addMarker(new MarkerOptions().
                        position(pos).
                        title(s.getStationName()).
                        snippet("Bikes: " + s.getBikes() + "\t Docks: " + s.getDocks()).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling))
                        );

                mMarkers.add(stationMarker);

            }
        }
	}
}
