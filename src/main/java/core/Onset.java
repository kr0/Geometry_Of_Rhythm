package core;

/**
 * Represents an onset with a duration. Can be accented or not.
 * 
 * @author kr0
 *
 */
public final class Onset {
	private int mDuration;
	private boolean mAccent;
	private String boxNotation;

	public Onset(int duration) {
		this(duration, false);
	}

	public Onset(int duration, boolean isAccented) throws IllegalArgumentException {
		if(duration <= 0) {throw new IllegalArgumentException("Duration must a postive non-zero number");}
		
		mDuration = duration;
		mAccent = isAccented;
		boxNotation = mAccent ? "X" : "x";
		for (int i = 1; i < mDuration; i++) {
			boxNotation += ".";
		}
	}

	public Integer getDuration() {
		return mDuration;
	}

	public boolean isAccented() {
		return mAccent;
	}

	@Override
	public String toString() {
		return boxNotation;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Onset)){
			return false;
		}
		return this.boxNotation.equals(((Onset)obj).boxNotation);
	}

	@Override
	public int hashCode() {
		return boxNotation.hashCode();
	}
}
