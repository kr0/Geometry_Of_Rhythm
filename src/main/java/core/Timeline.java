package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.BiMap;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

public class Timeline {

	private static final int EXPECTED_NUMBER_OF_ONSETS = Necklace.DEFAULT_CAPACITY;

	private Necklace<Pulse> pulses;
	private RangeMap<Integer, Integer> onsets;
	private BiMap<Integer, Range<Integer>> onsetnumber2Range;
	private int numberOfOnsets;

	/**
	 * Empty Timeline
	 */
	public Timeline() {
		pulses = new Necklace<Pulse>();
		onsets = TreeRangeMap.create();
		onsetnumber2Range = HashBiMap.create(EXPECTED_NUMBER_OF_ONSETS);
		numberOfOnsets = 1; // onsets are indexed from 1 in Geometry of Rhythm
							// book
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

	/**
	 * Insert onset starting at a specific pulse. All subsequent pulses after
	 * this onset are shifted to the right.
	 * 
	 * @param duration
	 *            the length of any onset is >= 1
	 * @param isAccent
	 *            default is no accent
	 */
	public void insertOnset(int pulse, int duration, boolean isAccent)
			throws IllegalArgumentException {
		isValidOnsetDuration(duration);
		Range<Integer> onset = Range.closed(pulse, pulse + duration - 1);
		onset.canonical(DiscreteDomain.integers());
		onsets.put(onset, numberOfOnsets);
		addPulse(pulse, duration, isAccent);
		onsetnumber2Range.put(numberOfOnsets, onset);
		numberOfOnsets++;
	}

	public void extendOnset(int onsetnumber, int n) {

	}

	public void placeOnset(int start, int end, boolean isAccent)
			throws IllegalArgumentException {

	}

	public void addPulse(int pulse, int duration, Boolean isAccent) {
		pulses.add(isAccent ? Pulse.ACCENT : Pulse.ATTACK, pulse);
		for (int i = 1; i < duration; i++) {
			pulses.add(Pulse.REST, pulse + 1);
		}
	}

	public void removeOnset(int onsetNumberToRemove)
			throws IllegalArgumentException {
		Range<Integer> onsetToRemove = getRangeOf(onsetNumberToRemove);
		if (onsetToRemove == null) {
			return;
		}
		// remove from rangemap
		onsets.remove(onsetToRemove);
		onsetnumber2Range.remove(onsetNumberToRemove);
		setPulse(onsetToRemove, Pulse.REST);
		numberOfOnsets--;

		// make adjustments to reflect removal
		if (onsetNumberToRemove == 1) {
			// Case where we removed first onset

			// Rotate timeline left by length of removed onset
			int lengthOfOnsetRange = Math.abs(onsetToRemove.upperEndpoint()
					- onsetToRemove.lowerEndpoint());
			rotateBy(-1 * lengthOfOnsetRange);

			// Decrease all onset numbers by 1;
			applyToOnsetNumbers(new Function<Integer, Integer>() {
				@Override
				public Integer apply(Integer input) {
					input--;
					return input;
				}

			}, Predicates.alwaysTrue());

		} else {
			// Case where we removed any onset after the firsts

			// Extend previous
			Range<Integer> prevOnset = getRangeOf(onsetNumberToRemove - 1);
			Range<Integer> extendedPreviousOnset = Range.closed(
					prevOnset.lowerEndpoint(), onsetToRemove.upperEndpoint());
			onsets.put(extendedPreviousOnset,
					getOnsetNumberFromRange(prevOnset));
			onsetnumber2Range.put(getOnsetNumberFromRange(prevOnset),
					extendedPreviousOnset);

			// Increase onset numbers to the right of remove point
			applyToOnsetNumbers(new Function<Integer, Integer>() {
				@Override
				public Integer apply(Integer input) {
					input--;
					return input;
				}

			}, new Predicate<Integer>() {
				@Override
				public boolean apply(Integer input) {
					input = wrapOnsetIndex(input);
					return input > onsetNumberToRemove;
				}
			});
		}

	}

	public void rotateBy(int i) {
		applyToRanges(new Function<Range<Integer>, Range<Integer>>() {

			@Override
			public Range<Integer> apply(Range<Integer> input) {
				// Shift endpoints by rotation amount
				int lower = input.lowerEndpoint() + i;
				lower = pulses.wrapindex(lower);
				int upper = input.upperEndpoint() + i;
				upper = pulses.wrapindex(upper);
				return Range.closed(lower, upper);
			}

		}, Predicates.alwaysTrue());

		renumberOnsets();
		pulses.rotateBy(i);

	}

	private void renumberOnsets() {
		int newOnsetNumber = 1;
		
		for (Entry<Range<Integer>, Integer> entry : onsetnumber2Range
				.inverse().entrySet()) {
			entry.setValue(newOnsetNumber);
			onsets.put(entry.getKey(), newOnsetNumber);
			newOnsetNumber++;
		}

		
		

	}

	protected void applyToRanges(Function<Range<Integer>, Range<Integer>> f,
			Predicate<Range<Integer>> p) {
		Set<Range<Integer>> transformedOnsets = new HashSet<Range<Integer>>();
		for (Entry<Integer, Range<Integer>> entry : onsetnumber2Range
				.entrySet()) {
			Integer onsetnumber = entry.getKey();
			Range<Integer> onsetrange = entry.getValue();

			if (p.apply(onsetrange)) {
				Range<Integer> newOnsetRange = f.apply(onsetrange);
				onsets.remove(onsetrange);
				onsets.put(newOnsetRange, onsetnumber);
				transformedOnsets.add(onsetrange);
			}
		}

		for (Range<Integer> onsetrange : transformedOnsets) {
			onsetnumber2Range.compute(getOnsetNumberFromRange(onsetrange), (k,
					v) -> f.apply(v));
		}

		// TODO make adjustments to necklace

	}

	/**
	 * Wraps the input to an onset number modulo the number of onsets + 1. Note
	 * that onset "0" maps to 1 because conceptually we refer to onset numbers
	 * starting from an index of 1. This is in accordance with the language of
	 * Geometry of Rhythm.
	 * 
	 * @param input
	 * @return
	 */
	protected Integer wrapOnsetIndex(Integer input) {
		return Math.floorMod(input, numberOfOnsets) + 1;
	}

	private void setPulse(Range<Integer> onsetToRemove, Pulse rest) {
		pulses.set(rest, onsetToRemove.lowerEndpoint(),
				onsetToRemove.upperEndpoint());

	}

	public int size() {
		return pulses.size();
	}

	public int getOnsetNumber(int pulse) {
		return onsets.get(pulse);
	}

	public String getBoxNotation() {
		return pulses.toString();
	}

	public String getOnsetRanges() {
		return onsets.toString();
	}

	public String getInterOnsetIntervals() {
		return onsets.asMapOfRanges().keySet().stream()
				.map(r -> r.upperEndpoint() - r.lowerEndpoint() + 1)
				.map(Object::toString)
				.collect(Collectors.joining("-", "[", "]"));

	}

	public int getNumberOfOnsets() {
		return numberOfOnsets - 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() throws CloneNotSupportedException {
		Timeline clone = new Timeline();
		clone.numberOfOnsets = this.numberOfOnsets;
		clone.onsets = TreeRangeMap.create();
		clone.onsets.putAll(this.onsets);
		clone.pulses = (Necklace<Pulse>) this.pulses.clone();
		clone.onsetnumber2Range = this.onsetnumber2Range;
		return clone;
	}

	/**
	 * Applies a function f to every onset number in this timeline if it matches
	 * some predicate p.
	 * 
	 * @param f
	 * @param p
	 */
	protected void applyToOnsetNumbers(Function<Integer, Integer> f,
			Predicate<Integer> p) {

		Set<Integer> transformedOnsets = new HashSet<Integer>();
		for (Entry<Integer, Range<Integer>> entry : onsetnumber2Range
				.entrySet()) {
			int onsetnumber = entry.getKey();
			Range<Integer> onsetrange = entry.getValue();

			if (p.apply(onsetnumber)) {
				int newOnsetnumber = f.apply(onsetnumber);
				onsets.remove(onsetrange);
				onsets.put(onsetrange, newOnsetnumber);
				transformedOnsets.add(onsetnumber);
			}
		}

		for (Integer onsetnumber : transformedOnsets) {
			onsetnumber2Range.inverse().compute(getRangeOf(onsetnumber),
					(k, v) -> f.apply(v));
		}
	}

	protected Range<Integer> getRangeOf(int number)
			throws IllegalArgumentException {
		isValidOnsetNumber(number);
		return onsetnumber2Range.get(number);
	}

	protected Integer getOnsetNumberFromRange(Range<Integer> onset) {
		return onsetnumber2Range.inverse().get(onset);
	}

	public void isValidOnsetDuration(int duration) {
		if (duration < 1) {
			throw new IllegalArgumentException("Duration of onset must be >= 1");
		}
	}

	private void isValidOnsetNumber(int number) {
		if (number < 1) {
			throw new IllegalArgumentException(
					"Onsets are indexed starting from 1.");
		}

	}
}
