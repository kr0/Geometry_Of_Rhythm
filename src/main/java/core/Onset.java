package core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an onset with a duration. Can be accented or not.
 * This is a set of pulses with an id.
 * 
 * @author kr0
 *
 */
public class Onset {
	private List<Pulse> pulses;
	
	public Onset(int duration) {
		this(duration, false);
		
	}

	public Onset(int duration, boolean isAccented) throws IllegalArgumentException {
		if(duration <= 0) {throw new IllegalArgumentException("Duration must a postive non-zero number");}
		pulses = new ArrayList<Pulse>();
		
		pulses.add(Pulse.ATTACK);
		for(int i = duration - 1; i > 0; i--){
			pulses.add(Pulse.REST);
		}
		
	}

	public Integer getDuration() {
		return pulses.size();
	}

	public boolean isAccented() {
		return pulses.get(0) == Pulse.ACCENT;
	}
	
	public void setAccent(boolean accent){
		pulses.set(0,(accent ? Pulse.ACCENT : Pulse.ATTACK));
	}

	
	public String getBoxNotation() {
		return pulses.stream().map(p -> p.toString()).collect(Collectors.joining());
	}

	@Override
	public String toString() {
		return getBoxNotation();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Onset)){
			return false;
		}
		Onset o = (Onset) obj;
		return this.hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
