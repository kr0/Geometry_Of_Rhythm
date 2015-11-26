package core;

import java.util.Comparator;

import com.google.common.collect.Range;

/**
 * An Onset is an attack followed by some number of rests.
 * It is a span of time. Conceptually, the Onset class
 * is a partition of a Necklace with an id, which is 
 * usually called the onset number in the Geometry of Rhythm.
 * @author kr0
 *
 */
public final class Onset{

	protected Range<Integer> range;
	private boolean isAccent;
	protected Integer id;
	
	public Onset(int start, int duration, int id, boolean isAccent) throws IllegalArgumentException{
		if(duration < 1){
			throw new IllegalArgumentException("Duration must be >= 1. An Onset is an attack plus some number of rests.");
		}
		this.range = Range.closed(start, (start + duration - 1));
		this.isAccent = isAccent;
		this.id = id;
	}
	
	public Onset(int start, int duration, int id) throws IllegalArgumentException{
		this(start,duration,id,false);
	}
	
	/**
	 * This onset's id.
	 * @return
	 */
	public Integer id(){
		return this.id;
	}

	/**
     * An Onset is a closed range [Start, End].
	 * This method returns the absolute startpoint
	 * with no context. 
	 * @return
	 */
	public Integer start(){
		return range.lowerEndpoint();
	}

	/**
	 * The absolute duration of this onset independent of
	 * any timeline.
	 * @return
	 */
	public Integer duration(){
		return end() - start() + 1;
	}
	
	/**
     * An Onset is a closed range [Start, End].
	 * This method returns the absolute endpoint
	 * with no context. 
	 * @return
	 */
	public Integer end(){
		return range.upperEndpoint();
	}

	/**
     * An Onset is a closed range [Start, End].
	 * This method returns the endpoint with respect
	 * to a given context, that is, the index end is
	 * wrapped modulo the size of the timeline.
	 * @param context
	 * @return
	 */
	public Integer start(Timeline context){
		return context.getNecklace().wrapindex(range.lowerEndpoint());
	}

	/**
	 * The length of this onset with respect to a given timeline.
	 * The length is calculated based on the start and end points
	 * in the context of a timeline.
	 * @param context
	 * @return
	 * The minimum length of an onset must be 1 by definition.
	 */
	public Integer duration(Timeline context){
		return (end(context) - start(context)) + 1;
	}

	/**
	 * An Onset is a closed range [Start, End].
	 * This method returns the endpoint with respect
	 * to a given context, that is, the index end is
	 * wrapped modulo the size of the timeline.
	 * @param context
	 * @return
	 */
	public Integer end(Timeline context){
		return context.getNecklace().wrapindex(range.upperEndpoint());
	}

	/**
	 * Is this Onset accented?
	 * @return
	 */
	public boolean isAccent(){
		return isAccent;
	}
	
	@Override
	public String toString() {
		return String.format("{# %d : %s : %d}", 
				id(), range.toString(), duration());
	}


	/**
	 * Sets the range of this Onsets
	 * @param start
	 * @param end
	 */
	public void setRange(Integer start, Integer end) {
		this.range = Range.closed(start, end);
		
	}

	/**
	 * Sets the id of this Onset.
	 * @param i
	 */
	public void setId(int i) {
		this.id = i;
		
	}

	/**
	 * Shifts the start and end points of this Onset's range
	 * by some offset.
	 * @param offset
	 * A positive of negative integer representing how many pulses
	 * left or right to shift by.
	 */
	public void shift(Integer offset) {
		this.range = Range.closed(start() + offset, end() + offset);
		
	}

	/**
	 * Modifies the length of this onset by appending rests to the
	 * the end.
	 * @param length
	 */
	public void extend(int length) {
		this.range = Range.closed(start(), end() + length);
		
	}
	
	/**
	 * Returns a comparator that takes into account what Timeline
	 * this onset exists on. That is, start and end indices
	 * are wrapped to the Timeline.
	 * @param t
	 * @return
	 */
	public static Comparator<Onset> getComparator(Timeline t){
		return new Comparator<Onset>() {

			@Override
			public int compare(Onset o1, Onset o2) {
				return o1.start(t).compareTo(o2.start(t));
			}
		};
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Onset o = new Onset(start(), duration(), id());
		return o;
	}

	

	
	
	
	
	
	
}
