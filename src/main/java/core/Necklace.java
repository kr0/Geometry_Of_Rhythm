package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

/**
 * A necklace is a mutable circular array list of objects.
 * 
 * @author kr0
 * @param <E>
 *
 */
public class Necklace<E> implements Iterable<E> {

	static final int DEFAULT_CAPACITY = 16;
	ArrayList<E> list;

	/**
	 * Creates an empty necklace with default capacity of 16.
	 */
	public Necklace() {
		list = new ArrayList<>(DEFAULT_CAPACITY);

	}

	public Necklace(int capacity) {
		list = new ArrayList<>(capacity);
	}

	public Necklace(Necklace<E> necklace) {
		this.list = new ArrayList<E>(necklace.list);

	}

	/**
	 * The size of this Necklace
	 * 
	 * @return
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Returns the node at this index modulo the number of elements in this list
	 * 
	 * @param index
	 * @return
	 */
	public E get(int index) {
		index = wrapindex(index);
		return list.get(index);
	}

	/**
	 * Appends element to end of Necklace. This operation increases the size of
	 * the necklace by 1.
	 * 
	 * @param elem
	 * @return
	 */
	public void add(E elem) {

		list.add(elem);

	}

	/**
	 * Adds an element at a specific index. Shifts the element currently at that
	 * position (if any) and any subsequent elements to the right (adds one to
	 * their indices). <br>
	 * <b>Note -</b> adding at element at index = 0 or index = size() is
	 * equivalent to add(index).
	 * 
	 * @param elem
	 * @param index
	 *            note that necklaces are zero-indexed and that index values are
	 *            understood as indices modulo the number of elements before the
	 *            add is complete.
	 * @return
	 */
	public void add(E elem, int index) {
		if (size() == 0 || index == size()) {
			add(elem);
			return;
		}
		index = wrapindex(index);
		list.add(index, elem);
	}

	public void add(Collection<E> collection) {
		for (E elem : collection) {
			add(elem);
		}

	}

	/**
	 * Removes the index from necklace. All indices to the right of remove index
	 * are shifted left by 1.
	 * 
	 * @param index
	 * @return element removed. returns null if no elements to remove
	 */
	public E remove(int index) {
		if (size() == 0) {
			return null;
		}
		return list.remove(wrapindex(index));
	}

	/**
	 * Sets index to elem.
	 * 
	 * @param elem
	 *            elem to set
	 * @param index
	 *            index to replace modulo the size of this necklace.
	 * @return
	 */
	public boolean set(E elem, int index) {
		if (size() == 0) {
			return false;
		}
		index = wrapindex(index);
		list.set(index, elem);
		return true;
	}

	/**
	 * Sets all datapoints between start and end inclusive to some element. This
	 * operation does not extend the size of this necklace.
	 * 
	 * 
	 * 
	 * @param elem
	 *            Elem to set
	 * @param start
	 *            index to replace modulo the size of this necklace.
	 * @param start
	 *            index to replace modulo the size of this necklace.
	 * @return
	 */
	public boolean set(E elem, int start, int end) {

		if (size() == 0) {
			return false;
		}

		start = wrapindex(start);
		end = wrapindex(end);
		set(elem, start);
		while (start != end) {
			start++;
			start = wrapindex(start);
			set(elem, start);
		}
		return true;
	}

	/**
	 * Increases the size of this necklace by appending n copies of an element
	 * after the start index. All elements to right of the extension have their
	 * indices shifted by n.
	 * 
	 * @param elem
	 *            Element to copy
	 * @param start
	 *            index to append to modulo the size of this necklace before
	 *            extension
	 * @param n
	 *            number of copies of elem
	 * @return
	 */
	public void extend(int start, E elem, int n) {
		start = wrapindex(start);
		start++;
		for (int i = 0; i < n; i++) {
			add(elem, start);
		}
	}

	/**
	 * Decreases the size of this necklace by removing pulses between
	 * end points.
	 * @param end
	 * must be less or equal to  end2
	 * @param end2
	 * must be greater that or equal to end
	 */
	public void shrink(int end, int end2) {
		if(end2 > end){
			throw new IllegalArgumentException("Can not shrink in that direction");
		}
		
		set(null, end, end2);
		list.removeIf(p -> p==null);
	}

	/**
	 * Wraps this index to this circular list.
	 * 
	 * @param index
	 * @return
	 */
	public int wrapindex(int index) {
		return Math.floorMod(index, size());
	}

	/**
	 * Returns an iterator that cycles indefinitely over the elements of
	 * iterable. The returned iterator supports remove() if the provided
	 * iterator does. After remove() is called, subsequent cycles omit the
	 * removed element, which is no longer in iterable. The iterator's hasNext()
	 * method returns true until iterable is empty. <br>
	 * <b>Warning:</b> Typical uses of the resulting iterator may produce an
	 * infinite loop. You should use an explicit break or be certain that you
	 * will eventually remove all the elements.
	 * 
	 * @return
	 */
	public Iterator<E> cycle() {
		return Iterators.cycle(this);
	}

	public void rotateBy(int i) {
		Collections.rotate(list, i);

	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Necklace<E> newNecklace = new Necklace<>();
		newNecklace.list = Lists.newArrayList(this.list);
		return newNecklace;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Necklace<?>)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return toString("");
	}

	/**
	 * A custom toString method. You can specify what delimiter to use.s
	 * @param delimiter
	 * @return
	 */
	public String toString(String delimiter) {
		return list.stream().map(Object::toString)
				.collect(Collectors.joining(delimiter, "[", "]"));
	}

}
