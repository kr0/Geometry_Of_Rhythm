package core;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

public class Timeline {

	private static final int EXPECTED_NUMBER_OF_ONSETS = Necklace.DEFAULT_CAPACITY;

	private Necklace<Pulse> pulses;
	private BiMap<Integer, Onset> onsets;
	private int numberOfOnsets;

	/**
	 * Empty Timeline
	 */
	public Timeline() {
		pulses = new Necklace<Pulse>();
		onsets = HashBiMap.create(EXPECTED_NUMBER_OF_ONSETS);
		numberOfOnsets = 0;
	}

	public Timeline(int... interOnsetInterval) {
		this();
		for (int duration : interOnsetInterval) {
			addOnset(duration);
		}
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
	 * Adds an onset to the end of this Timeline
	 * 
	 * @param duration
	 *            Duration must be >= 1.
	 */
	public void addOnset(int duration) throws IllegalArgumentException {
		addOnset(duration, false);

	}

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

	public void removeOnset(Onset removeOnset) throws IllegalArgumentException {
		int numberToRemove = getOnsetNumber(removeOnset);
		int lengthRemoved = removeOnset.duration();

		// Replace onset pulses with rests
		pulses.set(Pulse.REST, removeOnset.start(), removeOnset.end());

		// Adjust previous onset
		Onset prevOnset = getOnset(getOnsetNumber(removeOnset) - 1);
		prevOnset.doExtend(lengthRemoved);
		onsets.forcePut(prevOnset.id(), prevOnset);

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

	public void replaceOnset(Onset onset, boolean isAccent)
			throws IllegalArgumentException {
		// TODO
	}

	public void removeOnset(int i) {
		removeOnset(getOnset(i));
	}

	public void doRotatation(int i) {
		onsets.forEach((onsetnumber, onset) -> onsets.merge(onsetnumber, onset,
				(key, val) -> {
					// temporary rest
				pulses.set(Pulse.REST, val.start(this));
				val.doShift(1 * i);
				// fill in attack in new location
				return val;
			}));
		
		onsets.forEach((onsetnumber, onset) -> onsets.merge(onsetnumber, onset,
				(key, val) -> {
					// temporary rest
				pulses.set((val.isAccent() ? Pulse.ACCENT : Pulse.ATTACK),
						val.start(this));
				return val;
			}));

		// pulses.rotateBy(i);

	}

	public String getBoxNotation() {
		return pulses.toString();
	}

	public String getInterOnsetIntervals() {
		return onsets.values().stream()
				.sorted(Onset.getComparator(this))
				.map(Onset::duration)
				.map(Object::toString)
				.collect(Collectors.joining("-", "[", "]"));
	}

	public int getNumberOfPulses() {
		return pulses.size();
	}

	public int getNumberOfOnsets() {
		return numberOfOnsets;
	}

	public Onset getOnset(int i) {
		return onsets.get(wrapOnsetIndex(i));
	}

	/**
	 * Returns the onset id associated with this onset. The value returned is
	 * modulo the number of onsets when method was invoked.
	 * 
	 * @param onset
	 * @return
	 */
	public int getOnsetNumber(Onset onset) {
		return wrapOnsetIndex(onsets.inverse().get(onset));
	}

	public Map<Integer, Onset> getOnsets() {
		return onsets;
	}

	public Necklace<Pulse> getNecklace() {
		return pulses;
	}

	/**
	 * Wraps index to circular timeline.
	 * 
	 * @param i
	 * @return
	 */
	public int wrapOnsetIndex(int i) {
		return Math.floorMod(i, numberOfOnsets);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		Timeline clone = new Timeline();
		clone.numberOfOnsets = new Integer(this.numberOfOnsets);
		clone.onsets = HashBiMap.create(this.onsets);
		clone.pulses = (Necklace<Pulse>) this.pulses.clone();
		return clone;
	}

}
