package core;

public class Timeline {

	private Necklace<Pulse> pulses;
	
	/**
	 * Empty Timeline
	 */
	public Timeline(){
		pulses = new Necklace<Pulse>();
	}
	
	/**
	 * Adds onset to the end of this Timeline.
	 * @param duration
	 * the length of any onset is >= 1
	 * @param isAccent
	 * default is no accent
	 */
	public void addOnset(int duration, Boolean isAccent) throws IllegalArgumentException{
		
	}
	
	/**
	 * Adds onset to the end of this Timeline.
	 * @param duration
	 * the length of any onset is >= 1
	 * @param isAccent
	 * Default is no accent
	 */
	public void addOnset(int duration) throws IllegalArgumentException{
		addOnset(duration, false);
	}
	
	/**
	 * Adds an onset starting at the specified pulse.
	 * @param pulse
	 * @param duration
	 * @param isAccent
	 */
	public void addOnsetAtPulse(int pulse, int duration, Boolean isAccent){
		
		
	}
	
	
	public void removeOnset(int onsetnumber){
		
	}
	
	public void extendOnset(int onsetnumber, int n){
		
	}
	
	public String getBoxNotation(){
		return pulses.toString();
	}
	
	
	
}
