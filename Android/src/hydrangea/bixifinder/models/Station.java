package hydrangea.bixifinder.models;

public class Station {

	private String mStationName;
	private int mBikes;
	private int mDocks;
	private int mId;
	private boolean mInstalled;
	private boolean mLocked;

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
