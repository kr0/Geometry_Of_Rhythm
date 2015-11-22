package coreTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Timeline;

public class TestTimeline {

	private Timeline t1;
	private Timeline t2;
	private String t1Box;
	private String t1IOI;

	@Before
	public void setUp() throws Exception {
		// TODO 
		t1 = new Timeline();
		t1.addOnset(3);
		t1.addOnset(3);
		t1.addOnset(2);
		t1Box = "[x..x..x.]";
		t1IOI = "[3-3-2]";
		
		t2 = new Timeline(3,3,2);
		
		
	}
	
	@Test
	public void testBoxNotation() throws Exception {
		assertTrue(t1.getBoxNotation().equals(t1Box));
		assertTrue(t1.getBoxNotation().equals(t2.getBoxNotation()));
	}
	
	@Test
	public void testInterOnsetIntervalString() throws Exception {
		assertTrue(t1.getInterOnsetIntervals().equals(t1IOI));
		assertTrue(t1.getInterOnsetIntervals().equals(t2.getInterOnsetIntervals()));
	}
	

	@Test
	public void testAddOnset() throws CloneNotSupportedException {
		// number of onsets increases
		Timeline tmp = (Timeline)t1.clone();
		assertTrue(t1.getNumberOfOnsets() == 3);
		assertTrue(tmp.getNumberOfOnsets() == t1.getNumberOfOnsets());
		int count = 3;
		for(int i = 0; i<count; i++){
			tmp.addOnset(1);
		}
		int newSize = t1.getNumberOfOnsets() + count;
		assertTrue(tmp.getNumberOfOnsets() == newSize);
		
		// Adds pulses in correct place
		assertTrue(tmp.getBoxNotation().equals("[x..x..x.xxx]"));
		
		// DataStructure resizes without issue
		count = 100;
		for(int i = 0; i<count; i++){
			tmp.addOnset(1);
		}
		newSize = newSize + count;
		assertTrue(tmp.getNumberOfOnsets() == newSize);
		
	}
	
	@Test
	public void testRotate() throws Exception{
		// TODO
		Timeline tmp = (Timeline)t1.clone();
		assertTrue(tmp.getInterOnsetIntervals().equals("[3-3-2]"));
		assertTrue(tmp.getBoxNotation().equals("[x..x..x.]"));
		assertTrue(tmp.getNumberOfOnsets() == 3);
		
	}
	
	@Test
	public void testRemoveOnset() throws Exception {
		Timeline tmp = (Timeline)t1.clone();
		
		// Remove onsets from right to left
		// Shows that removeOnset changes number of elements
		// and replaces with rests. 

		assertTrue(tmp.getInterOnsetIntervals().equals("[3-3-2]"));
		assertTrue(tmp.getBoxNotation().equals("[x..x..x.]"));
		assertTrue(tmp.getNumberOfOnsets() == 3);
		tmp.removeOnset(2);
		assertTrue(tmp.getInterOnsetIntervals().equals("[3-5]"));
		assertTrue(tmp.getBoxNotation().equals("[x..x....]"));
		assertTrue(tmp.getNumberOfOnsets() == 2);
		tmp.removeOnset(1);
		assertTrue(tmp.getInterOnsetIntervals().equals("[8]"));
		assertTrue(tmp.getBoxNotation().equals("[x.......]"));
		assertTrue(tmp.getNumberOfOnsets() == 1);
		tmp.removeOnset(0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[]"));
		assertTrue(tmp.getBoxNotation().equals("[........]"));
		assertTrue(tmp.getNumberOfOnsets() == 0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[]"));
		
		// Remove onsets from left to right
		tmp = (Timeline) t1.clone();
		assertTrue(tmp.getInterOnsetIntervals().equals("[3-3-2]"));
		assertTrue(tmp.getBoxNotation().equals("[x..x..x.]"));
		assertTrue(tmp.getNumberOfOnsets() == 3);
		tmp.removeOnset(0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[5-2]"));
		assertTrue(tmp.getBoxNotation().equals("[...x..x.]"));
		assertTrue(tmp.getNumberOfOnsets() == 2);
		tmp.removeOnset(0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[8]"));
		assertTrue(tmp.getBoxNotation().equals("[......x.]"));
		assertTrue(tmp.getNumberOfOnsets() == 1);
		tmp.removeOnset(0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[]"));
		assertTrue(tmp.getBoxNotation().equals("[........]"));
		assertTrue(tmp.getNumberOfOnsets() == 0);
		assertTrue(tmp.getInterOnsetIntervals().equals("[]"));
		
		

	}

}
