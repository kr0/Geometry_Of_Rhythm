package core;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ResizeableTimeline implements Timeline {

	private static final int EXPECTED_NUMBER_OF_ONSETS = Necklace.DEFAULT_CAPACITY;

	private Necklace<Pulse> pulses;
	private BiMap<Integer, Onset> onsets;
	private int numberOfOnsets;

	/**
	 * Empty Timeline
	 */
	public ResizeableTimeline() {
		pulses = new Necklace<Pulse>();
		onsets = HashBiMap.create(EXPECTED_NUMBER_OF_ONSETS);
		numberOfOnsets = 0;
	}

	public ResizeableTimeline(int... interOnsetInterval) {
		this();
		for (int duration : interOnsetInterval) {
			addOnset(duration);
		}
	}
	
	protected ResizeableTimeline(Necklace<Pulse> pulses, Map<Integer, Onset> onsets){
		this.pulses = pulses;
		this.onsets = HashBiMap.create(onsets);
	}

	/**
	 * Adds an onset to the end of this Timeline.
	 * 
	 * @param duration
	 *            Duration must be >= 1.
	 * @param isAccent
	 *            By default onsets are not accented.
	 * @throws IllegalArgumentException
	 */
	public void addOnset(int duration) throws IllegalArgumentException {
		addOnset(duration, false);
	
	}

	/**
	 * Adds an onset to the end of this Timeline.
	 * 
	 * @param duration
	 *            Duration must be >= 1.
	 * @param isAccent
	 *            By default onsets are not accented.
	 * @throws IllegalArgumentException
	 */
	public void addOnset(int duration, boolean isAccent)
			throws IllegalArgumentException {
		insertOnset(pulses.size(), duration, isAccent);

	}

	/**
	 * Inserts a new onset starting at a specific pulse. All subsequent pulses
	 * are shifted to the right to make space.
	 * 
	 * @param duration
	 *            the length of any onset is >= 1
	 * @param isAccent
	 *            default is no accent
	 */
	public void insertOnset(int pulse, Onset o) {
		insertOnset(pulse, o.duration(), o.isAccent());
	}

	/**
	 * Inserts a new onset starting at a specific pulse. All subsequent pulses
	 * are shifted to the right to make space.
	 * 
	 * @param duration
	 *            the length of any onset is >= 1
	 * @param isAccent
	 *            default is no accent
	 */
	public void insertOnset(int pulse, int duration, boolean isAccent)
			throws IllegalArgumentException {

		// Create onset
		Onset onset = new Onset(pulse, duration, numberOfOnsets, isAccent);

		// Put onset into map and necklace
		onsets.put(onset.id(), onset);
		pulses.add(isAccent ? Pulse.ACCENT : Pulse.ATTACK, pulse);
		for (int i = 1; i < onset.duration(); i++) {
			pulses.add(Pulse.REST, pulse + 1);
		}

		// Increment
		numberOfOnsets++;
	}

	/**
	 * Removes this onset from the timeline.
	 * @param removeOnset
	 * @throws IllegalArgumentException
	 */
	public void removeOnset(Onset removeOnset) throws IllegalArgumentException {
		int numberToRemove = getOnsetNumber(removeOnset);
		int lengthRemoved = removeOnset.duration();

		// Replace onset pulses with rests
		pulses.set(Pulse.REST, removeOnset.start(), removeOnset.end());

		// Adjust previous onset
		Onset prevOnset = getOnset(getOnsetNumber(removeOnset) - 1);
		prevOnset.extend(lengthRemoved);
		onsets.forcePut(prevOnset.id(), prevOnset);

		// Remove
		onsets.remove(numberToRemove);

		// Renumber onsets
		for (int i = 0; i < numberOfOnsets; i++) {
			if (i > numberToRemove) {
				Onset adjOnset = getOnset(i);
				adjOnset.setId(i - 1);
				onsets.forcePut(adjOnset.id(), adjOnset);
			}
		}

		numberOfOnsets--;

	}

	/**
	 * Removes a specific onset. All pulses of the onset
	 * become rests.
	 * @param i
	 */
	public void removeOnset(int i) {
		removeOnset(getOnset(i));
	}

	/**
	 * Replaces a specific onset with a new onset. This
	 * Timeline will extend/shrink to accommodate the new onset.
	 * Neighboring onsets are not affected.
	 * @param i
	 * @param duration
	 * @param isAccent
	 * @throws IllegalArgumentException
	 */
	public void replaceOnset(int i, int duration, boolean isAccent)
			throws IllegalArgumentException {
		// TODO
	}

	
	@Override
	public int getNumberOfPulses() {
		return pulses.size();
	}
	
	@Override
	public int getNumberOfOnsets() {
		return numberOfOnsets;
	}

	@Override
	public Onset getOnset(int i) {
		return onsets.get(wrapOnsetIndex(i));
	}

	@Override
	public int getOnsetNumber(Onset onset) {
		return wrapOnsetIndex(onsets.inverse().get(onset));
	}

	@Override
	public BiMap<Integer, Onset> getOnsets() {
		return HashBiMap.create(onsets);
	}

	
	@Override
	public Necklace<Pulse> getNecklace() {
		return new Necklace<Pulse>(pulses);
	}

	
	@Override
	public int wrapOnsetIndex(int i) {
		return Math.floorMod(i, numberOfOnsets);
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		ResizeableTimeline clone = new ResizeableTimeline();
		clone.numberOfOnsets = new Integer(this.numberOfOnsets);
		clone.onsets = HashBiMap.create(this.onsets);
		clone.pulses = (Necklace<Pulse>) this.pulses.clone();
		return clone;
	}

}
