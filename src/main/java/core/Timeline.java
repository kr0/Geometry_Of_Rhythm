package core;

import java.util.stream.Collectors;

import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.Range;


public class Timeline {

	private static final int EXPECTED_NUMBER_OF_ONSETS = Necklace.DEFAULT_CAPACITY;

	private Necklace<Pulse> pulses;
	private RangeMap<Integer, Integer> onsets;
	private int numberOfOnsets;
	
	/**
	 * Empty Timeline
	 */
	public Timeline(){
		pulses = new Necklace<Pulse>();
		onsets = TreeRangeMap.create();
		numberOfOnsets = 1; // onsets are indexed from 1 in Geometry of Rhythm book
	}
	
	public Timeline(int... interOnsetInterval){
		this();
		for(int duration: interOnsetInterval){
			addOnset(duration);
		}
	}
	
	/**
	 * Adds an onset to the end of this Timeline.
	 * @param duration
	 * Duration must be >= 1.
	 * @param isAccent
	 * By default onsets are not accented.
	 * @throws IllegalArgumentException
	 */
	public void addOnset(int duration, boolean isAccent) throws IllegalArgumentException{
		insertOnset(pulses.size(), duration, isAccent);
		
	}

	/**
	 * Adds an onset to the end of this Timeline
	 * @param duration
	 * Duration must be >= 1.
	 */
	public void addOnset(int duration) throws IllegalArgumentException {
		addOnset(duration, false);
		
	}

	/**
	 * Insert onset starting at a specific pulse. All subsequent pulses
	 * after this onset are shifted to the right.
	 * @param duration
	 * the length of any onset is >= 1
	 * @param isAccent
	 * default is no accent
	 */
	public void insertOnset(int pulse, int duration, boolean isAccent) throws IllegalArgumentException{
		isValidOnsetDuration(duration);
		Range<Integer> onset = Range.closed(pulse, pulse + duration);
		onsets.put(onset, numberOfOnsets);
		addPulse(pulse, duration, isAccent);
		numberOfOnsets++;
	}
	
	public void placeOnset(int start, int end, boolean isAccent) throws IllegalArgumentException{
		
	}


	public void removeOnset(int onsetnumber){
		
	}
	
	public void extendOnset(int onsetnumber, int n){
		
	}
	
	public int size() {
		return pulses.size();
	}

	public int getOnsetNumber(int pulse){
		return onsets.get(pulse);
	}

	public String getBoxNotation(){
		return pulses.toString();
	}

	public String getOnsetRanges(){
		return onsets.toString();
	}
	
	public String getInterOnsetIntervals(){
		return onsets.asMapOfRanges().keySet().stream()
				.map(r -> r.upperEndpoint() - r.lowerEndpoint())
				.map(Object::toString)
				.collect(Collectors
				.joining("-", "[", "]"));
				
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		Timeline clone = new Timeline();
		clone.numberOfOnsets = this.numberOfOnsets;
		clone.onsets = TreeRangeMap.create();
		clone.onsets.putAll(this.onsets);
		clone.pulses = (Necklace<Pulse>)this.pulses.clone();
		return clone;
	}

	private void isValidOnsetDuration(int duration) {
		if(duration < 1){
			throw new IllegalArgumentException("Duration of onset must be >= 1");
		}
	}

	private void addPulse(int pulse, int duration, Boolean isAccent) {
		pulses.add(isAccent? Pulse.ACCENT : Pulse.ATTACK, pulse);
		for(int i = 1; i < duration; i++){
			pulses.add(Pulse.REST, pulse+1);
		}
	}

	public int getNumberOfOnsets() {
		return numberOfOnsets-1;
	}
	

	
	
}
