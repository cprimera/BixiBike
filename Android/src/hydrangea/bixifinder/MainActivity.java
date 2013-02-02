package hydrangea.bixifinder;

import android.os.Bundle;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import hydrangea.bixifinder.StationsListFragment.OnStationSelectedListener;
import hydrangea.bixifinder.models.Station;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;

public class MainActivity extends FragmentActivity implements
		OnStationSelectedListener {
	int SHOW_ERROR = 0;
	final private String BIXI = "Bixi";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (result == com.google.android.gms.common.ConnectionResult.SUCCESS) {
			//apk is up to date and we can proceed as normal.
		} else {
			GooglePlayServicesUtil.getErrorDialog(result, this, SHOW_ERROR);
		}

		// If fragment_container exists, then we're on a phone
		if (findViewById(R.id.fragment_container) != null) {

			// No need to do anything if we're resuming
			if (savedInstanceState != null) {
				return;
			}

			StationsListFragment listFragment = new StationsListFragment();

			// Pass on extras from the intent that
			// started the activity to the fragment
			listFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the fragment_container
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, listFragment).commit();

		}
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
	public void onStationSelected(Station station) {

		// Create the map fragment

		MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_fragment);

		// If mapFragment is not null, we're on a tablet
		// else we're on a phone
		if (mapFragment != null) {
			// Update the information displayed
			mapFragment.setStation(station);
			mapFragment.updateDetails();

		} else {

			MapFragment newFragment = new MapFragment(station);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}

	}

}
