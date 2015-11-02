package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.randname.RandomNameGenerator;

public class Timeline implements ITimeline {

	private List<Onset> mOnsets;
	private String mName;
	private final static RandomNameGenerator nameGen = new RandomNameGenerator();
	

	public Timeline() {
		mOnsets = new ArrayList<Onset>();
		mName = nameGen.next();
	}

	public Timeline(int... interonsetIntervals) {
		this();
		for (int duration : interonsetIntervals) {
			mOnsets.add(new Onset(duration));
		}
	}

	@Override
	public void insertAtPulse(int pulse, int duration) {
		insertAtPulse(pulse,duration, false);

	}
	@Override
	public void insertAtPulse(int insertpulse, int duration, boolean isAccented) {
		// Handle case where insertpulse is outside the total duration of this timeline
		if(insertpulse > this.getPulseNumber()){
			
		}
		
		
		int startPulseOfThisOnset = 0;
		int onsetNumber = 0;
		for(int i = 0; i<mOnsets.size(); i++){
			
			int endPulseOfThisOnset = startPulseOfThisOnset + mOnsets.get(i).getDuration();
			
			// insertpulse is conincident with this onset, i.e. replace this onset with the new one
			// this is equivalent to replaceOnset
			if(insertpulse == startPulseOfThisOnset){
				mOnsets.set(i, new Onset(duration, isAccented));
				return;
			}
			
			// insertpulse is in range. make this onset shorter.
			else if(insertpulse < endPulseOfThisOnset){
				int newDuration = insertpulse - startPulseOfThisOnset;
				Onset modifiedOnset = new Onset(newDuration, isAccented);
				mOnsets.set(i, modifiedOnset);
				break;
			}
			
			startPulseOfThisOnset += mOnsets.get(i).getDuration();
			onsetNumber++;
		}
		
		// add new onset after the onset that was modified
		onsetNumber++;
		mOnsets.add(onsetNumber, new Onset(duration, isAccented));
	}

	@Override
	public void deleteOnset(int... onsetNumber) {
		// check numbers are in range
		for(int number: onsetNumber){
			checkOnsetNumberInRange(number);
		}
		
		// mark the onsets for deletion
		for(int number: onsetNumber){
			mOnsets.set(number, null);
		}
		mOnsets.removeIf(o -> o == null);
	}
	
	private void checkOnsetNumberInRange(int number) {
		if(number < 0 || number >= this.getOnsetNumber()){
			throw new IndexOutOfBoundsException(String.format("Invalid Onset number, index: %s", number));
		}
		
	}

	@Override
	public void replaceOnset(int onsetNumberToReplace, int duration, boolean isAccented){
		mOnsets.set(onsetNumberToReplace, new Onset(duration, isAccented));
	}
	
	@Override
	public void replaceOnset(int onsetNumberToReplace, int duration){
		this.replaceOnset(onsetNumberToReplace, duration, false);
	}

	@Override
	public String getInterOnsetIntervalString() {
		return mOnsets.stream().map(o -> Integer.toString(o.getDuration())).collect(Collectors.joining("-", "[", "]"));
	}

	@Override
	public String getBoxNotationString() {
		
		String joined = mOnsets.stream()
							   .map(Onset::toString)
							   .collect(Collectors.joining());
		return "[" + joined + "]";
	}

	@Override
	public int getOnsetNumber() {
		return mOnsets.size();
	}

	@Override
	public int getPulseNumber() {
		return mOnsets.stream().map(Onset::getDuration).reduce(0, (Integer a, Integer b) -> (a + b));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Timeline)){
			return false;
		}
		return this.getBoxNotationString().equals(((Timeline)obj).getBoxNotationString());
	}

}
