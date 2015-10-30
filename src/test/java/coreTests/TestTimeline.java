package coreTests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Timeline;

public class TestTimeline {

	private Timeline t1;
	private String t1BoxNotation;
	private String t1OnsetIntervalString;

	@Before
	public void setup() {
		t1 = new Timeline(3, 3, 2);
		t1BoxNotation = "[x..x..x.]";
		t1OnsetIntervalString = "[3,3,2]";
	}

	@Test
	public void testBoxNotation() {
		assertTrue(t1.getBoxNotationString().equals(t1BoxNotation));

	}
	
	@Test
	public void testOnsetIntervalString(){
		System.out.println(t1.getInterOnsetIntervalString());
	}

}
