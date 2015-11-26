package core;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;


/**
 * A utility class for geometric analysis
 * of rhythmic timelines.
 * @author kr0
 *
 */
public final class Geometry {

	public static List<Integer> rhythmicContour(Timeline t){
		List<Integer> distances = Lists.newLinkedList();
		Integer lastInterval = null;
		for(Integer interval: Geometry.interOnsetIntervals(t)){
			if(lastInterval != null){
				int distance = interval - lastInterval;
				distances.add(distance);
			}
			lastInterval = interval;
		}
		
		
		return distances;
	}
	
	
	public String boxNotation(Timeline t) {
		return t.getNecklace().toString();
	}
	

	public static List<Integer> interOnsetIntervals(Timeline t) {
		return t.getOnsets().values().stream()
				.sorted(Onset.getComparator(t))
				.map(Onset::duration)
				.collect(Collectors.toList());
	}
	public static String interOnsetIntervalString(Timeline t){
		return t.getOnsets().values().stream()
				.sorted(Onset.getComparator(t))
				.map(Onset::duration)
				.map(Object::toString)
				.collect(Collectors.joining("-", "[", "]"));
	}
	
}
