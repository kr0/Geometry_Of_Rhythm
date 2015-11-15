package coreTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.Timeline;

public class TestTimeline {

	private Timeline t1;
	private String t1BoxNotation;
	private String t1OnsetIntervalString;

	private Timeline t2;
	private String t2BoxNotation;
	private String t2OnsetIntervalString;
	private int t1OnsetNumber;
	private int t1PulseNumber;
	private int t2PulseNumber;
	private int t2OnsetNumber;

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		t1 = new Timeline(3, 3, 2);
		t1BoxNotation = "[x..x..x.]";
		t1OnsetIntervalString = "[3-3-2]";
		t1OnsetNumber = 3;
		t1PulseNumber = 8;

		t2 = new Timeline();
		t2BoxNotation = "[]";
		t2OnsetIntervalString = "[]";
		t2OnsetNumber = 0;
		t2PulseNumber = 0;
	}

	@Test
	public void testBoxNotation() {
		assertTrue(t1.getBoxNotationString().equals(t1BoxNotation));

	}

	@Test
	public void testEmptyBoxNotation() {
		assertTrue(t2.getBoxNotationString().equals(t2BoxNotation));

	}

	@Test
	public void testOnsetIntervalString() {
		assertTrue(t1.getInterOnsetIntervalString().equals(t1OnsetIntervalString));
		assertTrue(t2.getInterOnsetIntervalString().equals(t2OnsetIntervalString));
	}

	@Test
	public void testOnsetNumber() {
		assertTrue(t1OnsetNumber == t1.getOnsetNumber());
		assertTrue(t2OnsetNumber == t2.getOnsetNumber());
	}

	@Test
	public void testPulseNumber() {
		assertTrue(t1PulseNumber == t1.getPulseNumber());
		assertTrue(t2PulseNumber == t2.getPulseNumber());
	}

	@Test
	public void testEquals() {
		assertFalse(t1.equals(t2));
		assertTrue(t1.equals(t1));
		assertFalse(t2.equals(t1));
	}

	@Test
	public void testDeleteOnset() {
		// remove until empty
		Timeline t = new Timeline(3, 3, 2);
		t.removeOnset(0);
		t.removeOnset(0);
		t.removeOnset(0);
		assertTrue(t.getOnsetNumber() == 0);
		assertTrue(t.getPulseNumber() == 0);

		// remove center onset
		t = new Timeline(3, 3, 2);
		t2 = new Timeline(3, 2);
		t.removeOnset(1);
		assertTrue(t.equals(t2));

		// remove edges
		t = new Timeline(3, 3, 2);
		t2 = new Timeline(3);
		t.removeOnset(0, 2);
		assertTrue(t.equals(t2));

		// remove center
		t = new Timeline(3, 3, 2);
		t2 = new Timeline(3,2);
		t.removeOnset(1);
		assertTrue(t.equals(t2));

	}
	
	@Test
	public void testReplaceOnset(){
		Timeline t = new Timeline(3,3,2);
		t.replaceOnset(0, 5);
		t.replaceOnset(1, 5);
		t.replaceOnset(2, 5);
		
		assertTrue(t.equals(new Timeline(5,5,5)));
		
		t = new Timeline(3,3,2);
		try {
			t.replaceOnset(10, 30);
		} catch (IndexOutOfBoundsException e) {
			
		}
		thrown.expect(IllegalArgumentException.class);
		t.replaceOnset(0, 0);
		
	}
	@Test
	public void testputOnset(){
		
		Timeline t =  new Timeline(3,3,2);
		t.putOnset(5, 1);
		assertTrue(t.getInterOnsetIntervalString().equals("[3-2-1-2]"));
		
		t = new Timeline(3,3,2);
		t.putOnset(5, 3);
		assertTrue(t.getInterOnsetIntervalString().equals("[3-2-3-2]"));
		
		t = new Timeline(3,3,2);
		t.putOnset(0, 6);
		assertTrue(t.getInterOnsetIntervalString().equals("[6-3-2]"));
		
		t = new Timeline(3,3,2);
		t.putOnset(8, 6);
		assertTrue(t.getInterOnsetIntervalString().equals("[3-3-2-6]"));
		
	}

	@Test
	public void testDeleteOnsetThrowsOutOfBounds1() {
		thrown.expect(IndexOutOfBoundsException.class);
		Timeline t = new Timeline(3, 3, 2);
		t.removeOnset(-1);
	}

	@Test
	public void testDeleteOnsetThrowsOutOfBounds2() {
		thrown.expect(IndexOutOfBoundsException.class);
		Timeline t = new Timeline(3, 3, 2);
		t.removeOnset(0, 1, 2, 3);
	}

}
