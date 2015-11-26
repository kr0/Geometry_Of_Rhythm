package core;

import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * A utility class for manipulating Timelines.
 * @author kr0
 *
 */
public final class Timelines {

	
	
	public static Timeline applyRhythmicContour(Timeline t, List<Integer> contour){
		BiMap<Integer, Onset> onsets = t.getOnsets();
		Necklace<Pulse> pulses = t.getNecklace();
		
		onsets.inverse().forEach((onset, id) ->
			onsets.merge(id, onset, (k, v) ->
				{
					int offset = contour.get(id);
					Onset newOnset = new Onset(onset.start(), onset.duration() + offset, onset.id());
					
					if(offset > 0){
						pulses.extend(onset.start(), Pulse.REST, offset);
					} else if (offset < 0){
						pulses.shrink(onset.end(), onset.end() + offset);
					}
					return newOnset;
				}));
		
		return new ImmutableTimeline(pulses, onsets);
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
				val.shift(1 * amount);
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
