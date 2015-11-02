package coreTests;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.Onset;

public class TestOnset {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testAccents() {
		Onset o1 = new Onset(3);
		assertTrue(o1.toString().equals("x.."));
		assertEquals(o1.isAccented(), false);
		
		Onset o2 = new Onset(3, true);
		assertTrue(o2.toString().equals("X.."));
		assertEquals(o2.isAccented(), true);
		
		Onset o3 = new Onset(1, true);
		assertTrue(o3.toString().equals("X"));
		
	}
	
	@Test
	public void testThrowsExceptionForZeroDuration(){
		thrown.expect(IllegalArgumentException.class);
		Onset zero = new Onset(0);
	}
	
	@Test
	public void testThrowsExceptionForNegativeDuration(){
		thrown.expect(IllegalArgumentException.class);
		Onset neg = new Onset(-1);
	}

	@Test
	public void testEquals(){
		Onset o1 = new Onset(3);
		Onset o2 = new Onset(3, true);
		assertTrue(o1.equals(o1));
		assertFalse(o1.equals(o2));
		assertFalse(o2.equals(o1));
		
	}
}
