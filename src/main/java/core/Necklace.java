package core;

/**
 * A k-ary combinatorial necklace. Each bead represents a pulse.
 * @author kr0
 *
 */
public class Necklace {

	private int[] repr;
	
	/**
	 * Create empty Necklace of size numberOfPulsess
	 * @param numberOfPulses
	 */
	public Necklace(int numberOfPulses) {
		repr = new int[numberOfPulses];
		
	}
	
	/**
	 * Create necklace that corresponds to the time unit box system
	 * string.
	 * @param tubString
	 */
	public Necklace(String tubString){
		
		this(tubString.length());
		
		// remove spaces and brackets
		tubString.replaceAll(" ", "");
		char[] cs = tubString.substring(1, tubString.length()-1).toCharArray(); 

		for(int i = 0; i < cs.length; i++){
			if(cs[i] == '.'){
				continue;
			}
			putOnsetOnBeat(i);
		}
	}

	/**
	 * Puts an onset at the given beat.
	 * @param beat
	 * @throws IndexOutOfBoundsException
	 */
	public void putOnsetOnBeat(int beat) throws IndexOutOfBoundsException {
		if(0 <= beat && beat < repr.length){
			repr[beat] = 1;
		}
		throw new IndexOutOfBoundsException(String.format("This necklace has 0-%d pulses", repr.length));
	}
	
	public int[] getInterOnsetDistances(){
		// TODO
		return null;
	}
	
	public int[] getIntervalVector(){
		// TODO
		return null;
	}
	
	@Override
	public String toString() {
		String sb = "[ ";
		for(int onset: repr){
			sb += (onset == 1 ? "x " : ". ");
		}
		return sb+="]";
	}

}
