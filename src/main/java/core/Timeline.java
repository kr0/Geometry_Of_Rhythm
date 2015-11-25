package core;

import java.util.Map;

public interface Timeline {


	/**
	 * Number of pulses in this timeline
	 * @return
	 */
	public int getNumberOfPulses();

	/**
	 * Number of onsets in this timeline.
	 * @return
	 */
	public int getNumberOfOnsets();

	/**
	 * Returns the onset with the specific number
	 * wrapped modulo the number of onsets in this timeline.
	 */
	public Onset getOnset(int i);

	/**
	 * Returns the onset id associated with this onset. The value returned is
	 * modulo the number of onsets when method was invoked.
	 * 
	 * @param onset
	 * @return
	 */
	public int getOnsetNumber(Onset onset);

	/**
	 * Unmodifiable view of the onsets in this timeline
	 * @return
	 */
	public Map<Integer, Onset> getOnsets();

	/**
	 * Unmodifiable view of the pulses in this timeline
	 * @return
	 */
	public Necklace<Pulse> getNecklace();

	/**
	 * Wraps index to circular timeline.
	 * 
	 * @param i
	 * @return
	 */
	public int wrapIndex(int i);
	


}