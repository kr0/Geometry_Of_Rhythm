package core;

import java.util.Map;

/**
 * A utility class for manipulating Timelines.
 * @author kr0
 *
 */
public final class Timelines {

	
	
	public static Timeline applyRhythmicContour(Timeline t, int[] contour){
		return t;
	}
	
	public static Timeline union(Timeline t1, Timeline t2){
		return t1;
	}
	
	public static Timeline intersection(Timeline t1, Timeline t2){
		return t1;
	}
	
	public static Timeline difference(Timeline t1, Timeline t2){
		return t1;
	}
	
	public static Timeline rotate(Timeline t1, int amount){
		
		Map<Integer, Onset> onsets = t1.getOnsets();
		Necklace<Pulse> pulses = t1.getNecklace();
		Timeline newT = new ResizeableTimeline(pulses, onsets);
		
		onsets.forEach((onsetnumber, onset) -> onsets.merge(onsetnumber, onset,
				(key, val) -> {
					// temporary rest
				pulses.set(Pulse.REST, val.start(newT));
				val.doShift(1 * amount);
				return val;
			}));
		
		onsets.forEach((onsetnumber, onset) -> onsets.merge(onsetnumber, onset,
				(key, val) -> {
					// fill in attack in new location
				pulses.set((val.isAccent() ? Pulse.ACCENT : Pulse.ATTACK),
						val.start(newT));
				return val;
			}));

		
		return newT;
	}
	
}
