package coreTests;

import static org.junit.Assert.*;

import org.junit.Test;

import core.Onset;

public class TestOnset {

	
	@Test
	public void test() {
		Onset o1 = new Onset(3);
		assertTrue(o1.toString().equals("x.."));
		assertEquals(o1.isAccented(), false);
		
		
	}

}
