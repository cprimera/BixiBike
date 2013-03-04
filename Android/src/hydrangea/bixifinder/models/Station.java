package hydrangea.bixifinder.models;

public class Station implements Comparable{

	private String mStationName;
	private int mBikes;
	private int mDocks;
	private int mId;
	private int id;
	private boolean mInstalled;
	private boolean mLocked;
	private double mLat;
	private double mLong;
	private double mDistanceToUser;
	
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
	
	public double getDist() {
		return mDistanceToUser;
	}
	
	public void setDist(double dist) {
		this.mDistanceToUser = dist;
	}

	@Override
	public int compareTo(Object other) {
		Station s = (Station) other;
		
		if(s.getDist() == this.getDist()) return 0;
		if(s.getDist() < this.getDist()) return 1;
		
		return -1;
	}
}
