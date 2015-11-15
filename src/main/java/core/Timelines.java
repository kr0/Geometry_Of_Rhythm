package core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Library of static methods to manipulate/generate
 * rhythmic timelines.
 * @author kr0
 *
 */
public final class Timelines {

	
	public static final double ROTATION_HALF_CYCLE = .5;
	public static final double ROTATION_QUARTER_CYCLE = .25;
	
	public static Timeline union(Timeline t1, Timeline t2){
		Map<Integer, Onset> pulse2Onsets1 = t1.mPulse2Onsets;
		Map<Integer, Onset> pulse2Onsets2 = t2.mPulse2Onsets;
		Map<Integer, Onset> newMap = new LinkedHashMap<Integer, Onset>();
		
		pulse2Onsets1.forEach((p,o) -> newMap.putIfAbsent(p, o));
		pulse2Onsets2.forEach((p,o) -> newMap.putIfAbsent(p, o));
		
		return new Timeline(newMap);
	}
	
	public static Timeline rotate(Timeline t, int amount){
		Map<Integer, Onset> pulse2Onsets = t.mPulse2Onsets;
		LinkedHashMap<Integer, Onset> newMap = new LinkedHashMap<>();
		for(Integer pulse: pulse2Onsets.keySet()){
			newMap.put((pulse + amount) % pulse2Onsets.size(), pulse2Onsets.get(pulse));
		}
		return new Timeline(pulse2Onsets);
	}
	
	public static int[] mainbeats(Timeline t){
		return null;
	}
	
	public int elementaryPulse(Timeline t){
		return 0;
	}
	
	

	public static final String CLAVE_SON = "[x..x..x...x.x...]";
	public static final String SHIKO = "[x...x.x...x.x...]";
	public static final String SOUKOUS = "[x..x..x...xx....]";
	public static final String RUMBA = "[x..x...x..x.x...]";
	public static final String BOSSA = "[x..x..x...x..x..]";
	public static final String GAHU = "[x..x..x...x...x.]";
	
	
}
