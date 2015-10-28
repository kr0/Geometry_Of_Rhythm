package core;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Timeline implements ITimeline {

	private int mPulsenumber;
	private int[] mInterOnsetIntervals;
	private ArrayList<String> mBoxNotation;
	
	
	
	public Timeline(int... interonsetIntervals){
		mPulsenumber = 0;
		mInterOnsetIntervals = interonsetIntervals;
		for(int onset: mInterOnsetIntervals){
			mPulsenumber+=onset;
			
			// make box notation
			mBoxNotation.add("x");
			for(int i = 1; i < onset; i++){
				mBoxNotation.add(".");
			}
			
		}
	}
	
	@Override
	public void insertAtPulse(int pulse, int duration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteOnset(int onsetNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInterOnsetIntervalString() {
		return mInterOnsetIntervals.toString();
	}

	@Override
	public String getBoxNotationString() {
		String sb = "[";
		sb += mBoxNotation.stream().collect(Collectors.joining(","));
		sb += "]";
		return sb;
	}

	@Override
	public int getOnsetNumber() {
		return mInterOnsetIntervals.length;
	}

	@Override
	public int getPulseNumber() {
		return mPulsenumber;
	}

}
