package core;

import java.util.stream.Collectors;


/**
 * A utility class for geometric analysis
 * of rhythmic timelines.
 * @author kr0
 *
 */
public final class Geometry {

	public static int[] rhythmicContour(Timeline t){
		// TODO
		return null;
	}
	
	
	public String boxNotation(Timeline t) {
		return t.getNecklace().toString();
	}

	public static String interOnsetIntervals(Timeline t) {
		return t.getOnsets().values().stream()
				.sorted(Onset.getComparator(t))
				.map(Onset::duration)
				.map(Object::toString)
				.collect(Collectors.joining("-", "[", "]"));
	}
	
}
