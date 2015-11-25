package core;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ImmutableTimeline implements Timeline {

	private static final int EXPECTED_NUMBER_OF_ONSETS = Necklace.DEFAULT_CAPACITY;

	private Necklace<Pulse> pulses;
	private BiMap<Integer, Onset> onsets;
	private int numberOfOnsets;

	/**
	 * Empty Timeline
	 */
	public ImmutableTimeline() {
		pulses = new Necklace<Pulse>();
		onsets = HashBiMap.create(EXPECTED_NUMBER_OF_ONSETS);
		numberOfOnsets = 0;
	}

	public ImmutableTimeline(int... interOnsetInterval) {
		this();
		for (int duration : interOnsetInterval) {
			addOnset(duration);
		}
	}
	
	protected ImmutableTimeline(Necklace<Pulse> pulses, BiMap<Integer, Onset> onsets){
		this.pulses = pulses;
		this.onsets = onsets;
		numberOfOnsets = this.onsets.size();
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
	public Timeline addOnset(int duration) throws IllegalArgumentException {
		return addOnset(duration, false);
	
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
	public Timeline addOnset(int duration, boolean isAccent)
			throws IllegalArgumentException {
		return insertOnset(pulses.size(), duration, isAccent);

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
	public Timeline insertOnset(int pulse, Onset o) {
		return insertOnset(pulse, o.duration(), o.isAccent());
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
	public Timeline insertOnset(int pulse, int duration, boolean isAccent)
			throws IllegalArgumentException {

		BiMap<Integer, Onset> newOnsets = HashBiMap.create(onsets);
		Necklace<Pulse> newPulses = new Necklace<>(pulses);
		
		// Create onset
		Onset onset = new Onset(pulse, duration, numberOfOnsets, isAccent);

		// Put onset into map and necklace
		newOnsets.put(onset.id(), onset);
		newPulses.add(isAccent ? Pulse.ACCENT : Pulse.ATTACK, pulse);
		for (int i = 1; i < onset.duration(); i++) {
			newPulses.add(Pulse.REST, pulse + 1);
		}
		
		Timeline t = new ImmutableTimeline(newPulses, newOnsets);
		return t;
		
	}

	/**
	 * Removes this onset from the timeline.
	 * @param removeOnset
	 * @throws IllegalArgumentException
	 */
	public Timeline removeOnset(Onset removeOnset) throws IllegalArgumentException {
		BiMap<Integer, Onset> newOnsets = HashBiMap.create(onsets);
		Necklace<Pulse> newPulses = new Necklace<>(pulses);
		
		int numberToRemove = getOnsetNumber(removeOnset);
		int lengthRemoved = removeOnset.duration();

		// Replace onset pulses with rests
		newPulses.set(Pulse.REST, removeOnset.start(), removeOnset.end());

		// Adjust previous onset
		Onset prevOnset = getOnset(getOnsetNumber(removeOnset) - 1);
		newOnsets.forcePut(prevOnset.id(), 
				new Onset(prevOnset.start(),
						  prevOnset.end() + lengthRemoved,
						  prevOnset.id(),
						  prevOnset.isAccent()));

		// Remove
		newOnsets.remove(numberToRemove);

		// Renumber onsets
		for (int i = 0; i < numberOfOnsets; i++) {
			if (i > numberToRemove) {
				Onset adjOnset = newOnsets.get(wrapIndex(i));
				adjOnset.setId(i - 1);
				newOnsets.forcePut(adjOnset.id(), adjOnset);
			}
		}

		Timeline t = new ImmutableTimeline(newPulses, newOnsets);
		return t;
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
	public Timeline replaceOnset(int i, int duration, boolean isAccent)
			throws IllegalArgumentException {
		// TODO
		return null;
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
		return onsets.get(wrapIndex(i));
	}

	@Override
	public int getOnsetNumber(Onset onset) {
		return wrapIndex(onsets.inverse().get(onset));
	}

	@Override
	public Map<Integer, Onset> getOnsets() {
		return new HashMap<Integer, Onset>(onsets);
	}

	
	@Override
	public Necklace<Pulse> getNecklace() {
		return new Necklace<Pulse>(pulses);
	}

	
	@Override
	public int wrapIndex(int i) {
		return Math.floorMod(i, numberOfOnsets);
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		ImmutableTimeline clone = new ImmutableTimeline();
		clone.numberOfOnsets = new Integer(this.numberOfOnsets);
		clone.onsets = HashBiMap.create(this.onsets);
		clone.pulses = (Necklace<Pulse>) this.pulses.clone();
		return clone;
	}

}
