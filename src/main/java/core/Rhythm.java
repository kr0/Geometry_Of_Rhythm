package core;
/**
 * A class representing a rhythm according to Toussaint's
 * Geometry of musical rhythm.
 * 
 * @author kr0
 *
 */
public class Rhythm {

	private int numberOfPulses;
	private char[] TUBS;
	
	
	Rhythm(int _numberOfpulses){
		numberOfPulses = _numberOfpulses;
		TUBS = new char[numberOfPulses];
	}
	
	
	/**
	 * Set onset on beat
	 * @param beat
	 */
	public void putOnsetOnBeat(int beat){
		TUBS[beat] = 1;
	}
	
	/**
	 * Excepts a string in Time Unit Box System notation.
	 * e.g. [x.x.x..x]
	 * @param tubString - a valid tubs strings
	 */
	public void putOnsetOnBeat(String tubString){
		// TODO check that tubstring is valid
		char[] cs = tubString.substring(1, tubString.length()-1).toCharArray(); 
		for(int i = 0; i < cs.length; i++){
			TUBS[i] = cs[i];
		}
	}
	
	public int getNumberOfPulses(){
		return numberOfPulses;
	}
	
	@Override
	public String toString() {
		return String.copyValueOf(TUBS);
	}
	
	
	
	
	
	
	
	
}
