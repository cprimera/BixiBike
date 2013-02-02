package hydrangea.bixifinder.models;

public class Station {

	private String mStationName;
	private int mBikes;
	private int mDocks;
	private int mId;
	private int id;
	private boolean mInstalled;
	private boolean mLocked;
	private double mLat;
	private double mLong;
	
	public Station(String name, int bikes, int docks, double lat, double lng, int id) {
		this.mStationName = name;
		this.mBikes = bikes;
		this.mDocks = docks;
		this.mLat = lat;
		this.mLong = lng;
		this.id = id;
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
	
	public double getLat() {
		return mLat;
	}
	
	public double getLng() {
		return mLong;
	}
}
