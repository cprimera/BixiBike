package hydrangea.bixifinder.models;

public class Station {

	private String mStationName;
	private int mBikes;
	private int mDocks;
	private int mId;
	private boolean mInstalled;
	private boolean mLocked;
	private double mLat;
	private double mLong;
	
	public Station(String name, int bikes, int docks, double lat, double lng) {
		this.mStationName = name;
		this.mBikes = bikes;
		this.mDocks = docks;
		this.mLat = lat;
		this.mLong = lng;
	}

	public String getStationName() {
		return mStationName;
	}

	public int getBikes() {
		return mBikes;
	}

	public int getDocks() {
		return mDocks;
	}

	public boolean getInstalled() {
		return mInstalled;
	}
}
