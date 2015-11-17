package coreTests;

import static org.junit.Assert.*;

import org.junit.Test;
import core.Necklace;
import core.Pulse;

public class TestNecklace {

	@Test
	public void testAdd() {

		Necklace<Pulse> t = new Necklace<Pulse>();

		// Reflects number of elements
		assertTrue(t.size() == 0);
		t.add(Pulse.Rest());
		assertTrue(t.size() == 1);
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Attack());
		t.add(Pulse.Rest());
		assertTrue(t.size() == 5);

		// respects order
		assertTrue(t.toString().equals("[...x.]"));
		t.add(Pulse.Rest());
		t.add(Pulse.Attack());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		assertTrue(t.toString().equals("[...x..x.......]"));

		// array resizes and maintains order
		t.add(Pulse.Attack());
		t.add(Pulse.Attack());
		t.add(Pulse.Attack());
		t.add(Pulse.Attack());
		t.add(Pulse.Rest());
		t.add(Pulse.Attack());
		assertTrue(t.size() == 20);
		assertTrue(t.toString().equals("[...x..x.......xxxx.x]"));

	}

	@Test
	public void testAddAtIndex() throws Exception {
		Necklace<Pulse> t = new Necklace<Pulse>();

		// Reflects number of elements
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Rest(), 0);
		assertTrue(t.toString().equals("[.....]"));
		assertTrue(t.size() == 5);
		t.add(Pulse.Attack(), 0);
		assertTrue(t.toString().equals("[x.....]"));
		assertTrue(t.size() == 6);

		// Indices shift appropriately
		t.add(Pulse.Attack(), 0);
		assertTrue(t.toString().equals("[xx.....]"));
		assertTrue(t.size() == 7);
		t.add(Pulse.Rest(), 1);
		assertTrue(t.toString().equals("[x.x.....]"));

		// Indices reflect circular nature of necklace
		Necklace<Pulse> tmp = (Necklace<Pulse>) t.clone();
		int index1 = Math.floorMod(-1, t.size());
		int index2 = Math.floorMod(-9, tmp.size());
		assertTrue(t.size() == tmp.size());
		assertTrue(index1 == index2);
		t.add(Pulse.Attack(), -1);
		tmp.add(Pulse.Attack(), -9);
		assertTrue(t.size() == 9);
		assertTrue(tmp.size() == 9);
		assertTrue(t.equals(tmp));

		// array resizes and respects order
		assertTrue(t.toString().equals("[x.x....x.]"));
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		t.add(Pulse.Rest(), 0);
		t.add(Pulse.Attack(), 0);
		assertTrue(t.size() == 21);
		assertTrue(t.toString().equals("[x.x.x.x.x.x.x.x....x.]"));

	}

	@Test
	public void testRemove() throws Exception {
		Necklace<Pulse> t = new Necklace<Pulse>();

		// number of elements reflects remove()
		for (int i = 0; i < 16; i++) {
			if ((i % 2) == 0) {
				t.add(Pulse.Attack());
			} else {
				t.add(Pulse.Rest());
			}
		}
		Necklace<Pulse> tmp = (Necklace<Pulse>) t.clone();
		assertTrue(t.size() == 16);
		assertTrue(t.toString().equals("[x.x.x.x.x.x.x.x.]"));
		for (int i = 0; i < 16; i++) {
			tmp.remove(0);
		}
		assertTrue(tmp.size() == 0);

		// number of elements is never less than 0
		// also fails to remove graciously
		tmp.remove(0);
		assertTrue(tmp.size() == 0);

		// remove shifts indices appropriately
		assertTrue(t.toString().equals("[x.x.x.x.x.x.x.x.]"));
		t.remove(1);
		assertTrue(t.toString().equals("[xx.x.x.x.x.x.x.]"));
		t.add(Pulse.Rest(), 1);
		assertTrue(t.toString().equals("[x.x.x.x.x.x.x.x.]"));
		t.remove(1);
		t.remove(2);
		t.remove(3);
		t.remove(4);
		t.remove(5);
		t.remove(6);
		t.remove(7);
		t.remove(8);
		t.add(Pulse.Rest(), 1);
		t.add(Pulse.Rest(), 3);
		t.add(Pulse.Rest(), 5);
		t.add(Pulse.Rest(), 7);
		t.add(Pulse.Rest(), 9);
		t.add(Pulse.Rest(), 11);
		t.add(Pulse.Rest(), 13);
		t.add(Pulse.Rest(), 15);
		assertTrue(t.toString().equals("[x.x.x.x.x.x.x.x.]"));

		// reflects circular nature of necklace
		tmp = (Necklace<Pulse>) t.clone();
		int index1 = 5;
		Math.floorMod(5, t.size());
		int index2 = 2 * tmp.size() + index1;
		assertTrue(t.size() == tmp.size());
		assertTrue(index1 != index2);
		assertTrue(Math.floorMod(index1, t.size()) == Math.floorMod(index2,
				t.size()));
		t.remove(index1);
		tmp.remove(index2);
		assertTrue(t.equals(tmp));

	}

	@Test
	public void testSet() throws Exception {
		Necklace<Pulse> t = new Necklace<Pulse>();
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		assertTrue(t.toString().equals("[....]"));
		t.set(Pulse.Attack(), 0);
		assertTrue(t.toString().equals("[x...]"));
		t.set(Pulse.Attack(), 1);
		assertTrue(t.toString().equals("[xx..]"));
		t.set(Pulse.Attack(), 2);
		assertTrue(t.toString().equals("[xxx.]"));
		t.set(Pulse.Attack(), 3);
		assertTrue(t.toString().equals("[xxxx]"));

		// set range
		t.set(Pulse.Rest(), 0, 3);
		assertTrue(t.toString().equals("[....]"));

		// set respects circular list
		t.set(Pulse.Attack(), 3, 0);
		assertTrue(t.toString().equals("[x..x]"));
		t.set(Pulse.Rest(), -4, -1);
		assertTrue(t.toString().equals("[....]"));
		t.set(Pulse.Attack(), -1, -4);
		assertTrue(t.toString().equals("[x..x]"));
		t.set(Pulse.Attack(), 2);
		assertTrue(t.toString().equals("[x.xx]"));
		t.set(Pulse.Rest(), -1, 1);
		assertTrue(t.toString().equals("[..x.]"));
		t.set(Pulse.Rest(), -8, -5);
		assertTrue(t.toString().equals("[....]"));
		t.set(Pulse.Attack(), -1, 1);
		assertTrue(t.toString().equals("[xx.x]"));
	}

	@Test
	public void testExtend() throws Exception {
		Necklace<Pulse> t = new Necklace<Pulse>();
		t.add(Pulse.Attack());
		t.add(Pulse.Attack());
		t.add(Pulse.Rest());
		t.add(Pulse.Rest());
		assertTrue(t.toString().equals("[xx..]"));

		// extend inside
		t.extend(0, Pulse.Rest(), 3);
		assertTrue(t.toString().equals("[x...x..]"));

		// extend at edges
		t.add(Pulse.Attack());
		assertTrue(t.toString().equals("[x...x..x]"));
		t.extend(7, Pulse.Rest(), 3);
		assertTrue(t.toString().equals("[x...x..x...]"));

		// extend respects circular list
		t.extend(-1, Pulse.Attack(), 3);
		assertTrue(t.toString().equals("[x...x..x...xxx]"));
		t.extend(-2, Pulse.Rest(), 3);
		assertTrue(t.toString().equals("[x...x..x...xx...x]"));

	}
}
