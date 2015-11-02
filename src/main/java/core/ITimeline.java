package core;

/**
 * Represents a rhythmic timeline like that defined in Geometry of Rhythm
 * by Godfried Toussaint.
 * @author kr0
 *
 */
public interface ITimeline {

	/**
	 * Inserts an onset at a specific pulse in this timeline.
	 * @param pulse the pulse at which to insert this onset
	 * @param duration the duration of the onset
	 */
	public void insertAtPulse(int pulse, int duration);
	
	/**
	 * Deletes the specific onsets.
	 * @param onsetNumber a zero indexed onset number i.e. the first onset of any timeline is called 0.
	 */
	public void deleteOnset(int... onsetNumber);
	
	/**
	 * Interonset interval strings have the form
	 * [3-3-2], representing the box notation string
	 * [x..x..x.]
	 * @return
	 */
	public String getInterOnsetIntervalString();
	
	/**
	 * Box notation strings have the form
	 * [x..x..x.], representing the interonset string
	 * [3-3-2].
	 * @return
	 */
	public String getBoxNotationString();
	
	/**
	 * The number of onsets in this timeline.
	 * @return
	 */
	public int getOnsetNumber();
	
	/**
	 * The total number of pulses in this timeline.
	 * @return
	 */
	public int getPulseNumber();

	/**
	 * Inserts an onset at a specific pulse in this timeline.
	 * @param pulse the pulse at which to insert this onset
	 * @param duration the duration of the onset
	 * @param isAccented whether is accent this onset
	 */
	void insertAtPulse(int pulse, int duration, boolean isAccented);

	
	void replaceOnset(int onsetNumberToReplace, int duration, boolean isAccented);

	void replaceOnset(int onsetNuberToReplace, int duration);

	
	
	

}
