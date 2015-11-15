package coreTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.Timeline;
import core.Timelines;

public class TestTimelines {

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
	private int t1RotateBy;
	private String t1Rotated;

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		t1 = new Timeline(3, 3, 2);
		t1BoxNotation = "[x..x..x.]";
		t1OnsetIntervalString = "[3-3-2]";
		t1OnsetNumber = 3;
		t1PulseNumber = 8;
		t1RotateBy = 3;
		t1Rotated = "[.x.x..x.]";

		t2 = new Timeline();
		t2BoxNotation = "[]";
		t2OnsetIntervalString = "[]";
		t2OnsetNumber = 0;
		t2PulseNumber = 0;
	}
	
	@Test
	public void testRotate() {
	}
}
