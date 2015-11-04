package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Represents a timeline like those used in Geometry of Rhythm by
 * Godfried Toussaint. A timeline is sequence of onsets used
 * to represent a rhythm.
 * @author kr0
 *
 */
public class Timeline {

	// Start Pulse --> Onset Object
	private Map<Integer, Onset> mPulse2Onsets;
	// Onset Object --> Start Pulse
	private Map<Onset, Integer> mOnset2Pulse;
	// Array of Onset Objects
	private List<Onset> mOnsets;

	public Timeline() {
		mPulse2Onsets = new LinkedHashMap<Integer, Onset>();
		mOnset2Pulse = new LinkedHashMap<Onset, Integer>();
		mOnsets = new ArrayList<Onset>();

	}

	public Timeline(int... interOnsetsIntervals) {
		this();
		int pulse = 0;
		for (int onsetDuration : interOnsetsIntervals) {
			Onset onset = new Onset(onsetDuration);
			mPulse2Onsets.put(pulse, onset);
			mOnset2Pulse.put(onset, pulse);
			pulse += onsetDuration;
		}
		
		fixOnsetArray();

	}

	public int getOnsetStartPulse(Onset onset) {
		return mOnset2Pulse.containsKey(onset) ? mOnset2Pulse.get(onset) : -1;
	}

	public String getInterOnsetIntervalString() {
		return mOnsets.stream().map(Onset::getDuration).map(Object::toString)
				.collect(Collectors.joining("-", "[", "]"));
	}

	public String getBoxNotationString() {
		return mOnsets.stream().map(Onset::getBoxNotation).collect(Collectors.joining("", "[", "]"));
	}

	public int getOnsetNumber() {
		return mOnsets.size();
	}

	public int getPulseNumber() {
		return mOnsets.stream().map(Onset::getDuration).reduce(0, (a, b) -> a + b);
	}

	public void deleteOnset(int... onsetNumbers) {
		if (!isOnsetNumberInRange(onsetNumbers)) {
			throw new IndexOutOfBoundsException("Onset number does not exist or is less than 0.");
		}
	
		// Delete from maps and mark for deletion in mOnsets
		for (int index : onsetNumbers) {
			Onset onsetToDelete = mOnsets.get(index);
			int onsetPulse = getOnsetStartPulse(onsetToDelete);
			mPulse2Onsets.remove(onsetPulse);
			mOnset2Pulse.remove(onsetToDelete);
			mOnsets.set(index, null);
		}
	
		// Delete from mOnsets
		mOnsets.removeIf(o -> o == null);
	
	}

	public void insertAtPulse(int insertPulse, int duration, boolean isAccented) throws IllegalArgumentException {
		if (insertPulse < 0) {
			throw new IllegalArgumentException(String.format("Can not insert at pulse: %d", insertPulse));
		}

		Onset newOnset = new Onset(duration, isAccented);

		// Adjust previous and post onsets
		for (Entry<Integer, Onset> onsetPulseEntry : mPulse2Onsets.entrySet()) {
			Onset onsetAtThisPulse = onsetPulseEntry.getValue();
			int onsetStartPulse = onsetPulseEntry.getKey();
			int durationOfThisOnset = onsetAtThisPulse.getDuration();
			int onsetEndPulse = onsetStartPulse + durationOfThisOnset - 1;

			// insertPulse is coincident with the start of this onset
			// we simply break this loop to replace this onset.
			if(insertPulse == onsetStartPulse){
				break;
			}
			
			// insertPulse is in range of this event
			else if (insertPulse <= onsetEndPulse && insertPulse > onsetStartPulse) {
				// modify this onset
				int modifiedOnsetDuration = insertPulse - onsetStartPulse;
				Onset modifiedNewOnset = new Onset(modifiedOnsetDuration);
				onsetPulseEntry.setValue(modifiedNewOnset);
				break;
			}
		}

		// Insert new onset to pulse maps
		mPulse2Onsets.put(insertPulse, newOnset);
		mOnset2Pulse.put(newOnset, insertPulse);

		// Fix overlaps in Map
		fixOverLaps();
		// Fix onset array
		fixOnsetArray();

	}

	public void insertAtPulse(int insertPulse, int duration){
		insertAtPulse(insertPulse, duration, false);
	}
	
	public void replaceOnset(int onsetNumberToReplace, int duration, boolean isAccented) {
		Onset onset = mOnsets.get(onsetNumberToReplace);
		Onset newOnset = new Onset(duration, isAccented);
		int onsetPulse = mOnset2Pulse.get(onset);
		mPulse2Onsets.put(onsetPulse, newOnset);
		mOnset2Pulse.remove(onset);
		mOnset2Pulse.put(newOnset, onsetPulse);
		mOnsets.set(onsetNumberToReplace, newOnset);
	
	}

	public void replaceOnset(int onsetNuberToReplace, int duration) {
		replaceOnset(onsetNuberToReplace,duration,false);
	}

	private boolean isOnsetNumberInRange(int[] onsetNumbers) {
		for (int onset : onsetNumbers) {
			if (onset < 0 || onset >= getOnsetNumber()) {
				return false;
			}
		}
		return true;
	}

	private void fixOnsetArray() {
		mOnsets.clear();
		// Add all the onsets in the pulse maps in order of their pulse
		mOnsets.addAll(
				mPulse2Onsets.keySet().stream()
				.sorted()
				.map(mPulse2Onsets::get)
				.collect(Collectors.toList()));
	}

	/**
	 * 
	 */
	private void fixOverLaps() {
		Iterator<Integer> it = mPulse2Onsets.keySet().iterator();
		while (it.hasNext()) {
			int startPulse = it.next();
			Onset onsetAtThisPulse = mPulse2Onsets.get(startPulse);
			int endPulse = startPulse + onsetAtThisPulse.getDuration() - 1;

			if (it.hasNext()) {
				int nextStartPulse = it.next();
				Onset onsetAtNextPulse = mPulse2Onsets.get(nextStartPulse);
				if (nextStartPulse <= endPulse) {
					nextStartPulse = endPulse + startPulse;
					mOnset2Pulse.put(onsetAtNextPulse, nextStartPulse);
				}
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Timeline)) {
			return false;
		}
		return this.getBoxNotationString().equals(((Timeline) obj).getBoxNotationString());
	}

}
