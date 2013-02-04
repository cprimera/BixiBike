package hydrangea.bixifinder;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import com.google.android.gms.common.GooglePlayServicesUtil;
import hydrangea.bixifinder.StationsListFragment.OnStationSelectedListener;

public class MainActivity extends FragmentActivity implements
		OnStationSelectedListener {

	int SHOW_ERROR = 0;

	final private String BIXI = "Bixi";

	private Activity mActivity;
    private MapController mapController;
    private StationsListFragment listFragment;

    private static final String LOG_TAG = "BIXI_FINDER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mActivity = this;

		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (result == com.google.android.gms.common.ConnectionResult.SUCCESS) {
			// apk is up to date and we can proceed as normal.
		} else {
			GooglePlayServicesUtil.getErrorDialog(result, this, SHOW_ERROR);
		}

		// If fragment_container exists, then we're on a phone
		if (findViewById(R.id.fragment_container) != null) {

			// No need to do anything if we're resuming
			if (savedInstanceState != null) {
				return;
			}

			listFragment = new StationsListFragment();

			// Pass on extras from the intent that
			// started the activity to the fragment
			listFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the fragment_container
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, listFragment).commit();

		} else {
            // We're on a tablet

			 listFragment = (StationsListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.list_fragment);
		}

		// Start loading the data in the background
		new GetStationsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Callback function for when a list item is clicked
	 */
	@Override
	public void onStationSelected(int station) {

		// Create the map fragment
		Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);

		Log.d("MYTAG", "Done loading fragment");

		// If mapFragment is not null, we're on a tablet
		if (mapFragment != null) {
			// Update the information displayed
			mapController.setStation(station);
			mapController.updateDetails();

		} else {
            // On a phone
            mapController = new MapController();

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.fragment_container, mapController);
			transaction.addToBackStack(null);

			transaction.commit();

//            mapController.initialize(((SupportMapFragment)mapFragment).getMap());
//            mapController.setStation(station);
//            mapController.updateDetails();
		}

	}

	class GetStationsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg) {

			try {
				DataConnector dc = DataConnector.getInstance();
				dc.downloadStations(mActivity);

			} catch (Exception e) {
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {

            Log.d(LOG_TAG, "Retrived the list, updating list and map");

			listFragment.onStationsFetched();
            if(mapController != null) mapController.onStationsFetched();
		}

	}

}