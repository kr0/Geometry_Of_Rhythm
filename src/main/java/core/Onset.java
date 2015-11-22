package core;

import com.google.common.collect.Range;

/**
 * An Onset is an attack followed by some number of rests.
 * It is a span of time. Conceptually, the Onset class
 * is a partition of a Necklace with an id, which is 
 * usually called the onset number in the Geometry of Rhythm.
 * @author kr0
 *
 */
public final class Onset implements Comparable<Onset>{

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
	
	public Integer duration(){
		return (range.upperEndpoint() - range.lowerEndpoint()) + 1;
	}
	
	public Integer id(){
		return this.id;
	}
	
	public Integer start(){
		return range.lowerEndpoint();
	}
	
	public Integer end(){
		return range.upperEndpoint();
	}

	public boolean isAccent(){
		return isAccent;
	}
	
	@Override
	public String toString() {
		return String.format("{# %d : %s : %d}", 
				id(), range.toString(), duration());
	}

	@Override
	public int compareTo(Onset o) {
		return this.range.lowerEndpoint().compareTo(o.range.lowerEndpoint());
	}

	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Onset o = new Onset(start(), duration(), id());
		return o;
	}

	public void setRange(Integer start, Integer end) {
		this.range = Range.closed(start, end);
		
	}

	public void setId(int i) {
		this.id = i;
		
	}

	public void shift(Integer offset) {
		this.range = Range.closed(start() + offset, end() + offset)
		
	}
	
	
	
	
	
}
