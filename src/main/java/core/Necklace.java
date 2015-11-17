package core;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A necklace is an circular array list
 * 
 * @author kr0
 * @param <E>
 *
 */
public class Necklace<E> {

	static final int DEFAULT_CAPACITY = 16;
	private int numberOfElements;
	ArrayList<Node<E>> list;

	/**
	 * Creates an empty necklace with default capacity of 16.
	 */
	public Necklace() {
		list = new ArrayList<>(DEFAULT_CAPACITY);
		numberOfElements = 0;

	}

	Necklace(int capacity) {
		list = new ArrayList<>(capacity);
		numberOfElements = 0;
	}

	/**
	 * The size of this Necklace
	 * @return
	 */
	public int size() {
		return numberOfElements;
	}

	/**
	 * Returns the node at this index modulo the number of elements in this list
	 * 
	 * @param index
	 * @return
	 */
	public E get(int index) {
		index = Math.floorMod(index, size());
		return list.get(index).data;
	}

	/**
	 * Appends element to end of Necklace. This operation increases the size of
	 * the necklace by 1.
	 * 
	 * @param elem
	 * @return
	 */
	public void add(E elem) {
		Node<E> newNode;
		if (list.size() == 0) {
			newNode = new Node<E>(elem);
			newNode.prev = newNode;
			newNode.next = newNode;
		} else {
			newNode = new Node<E>(elem, getLastNode(), getFirstNode());
		}
		numberOfElements++;
		list.add(newNode);

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
		index = Math.floorMod(index, size());
		Node<E> newNode = new Node<E>(elem);
		Node<E> tail = getNode(index - 1);
		Node<E> head = getNode(index);
		newNode.next = head;
		tail.next = newNode;
		head.prev = newNode;
		list.add(index, newNode);
		numberOfElements++;
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
		} else if (size() == 1) {
			index = Math.floorMod(index, size());
			Node<E> res = list.remove(index);
			numberOfElements--;
			return res.data;
		}
		index = Math.floorMod(index, size());
		Node<E> tail = getNode(index - 1);
		Node<E> head = getNode(index + 1);
		tail.next = head;
		head.prev = tail;
		Node<E> res = list.remove(index);
		numberOfElements--;
		return res.data;
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
		index = Math.floorMod(index, size());
		Node<E> n = getNode(index);
		n.data = elem;
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

		start = Math.floorMod(start, size());
		end = Math.floorMod(end, size());
		set(elem, start);
		while(start!=end){
			start++;
			start = Math.floorMod(start, size());
			set(elem,start);
		}
		return true;
	}

	/**
	 * Increases the size of this necklace by appending n copies of an element
	 * after the start index. All elements to right of the extension have
	 * their indices shifted by n.
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
		start = Math.floorMod(start, size());
		start++;
		for(int i = 0; i < n; i++){
			add(elem,start);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Necklace<E> newNecklace = new Necklace<>();
		newNecklace.list = new ArrayList<>(this.list);
		newNecklace.numberOfElements = this.numberOfElements;
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

	public String toString(String delimiter) {
	
		return list.stream().map(Object::toString)
				.collect(Collectors.joining(delimiter, "[", "]"));
	}

	private class Node<E> {
		E data;
		Node<E> prev;
		Node<E> next;

		Node() {
			this.prev = this;
			this.next = this;
			this.data = null;
		}

		Node(E elem) {
			this();
			this.data = elem;
		}

		Node(E elem, Node<E> prev, Node<E> next) {
			this(elem);
			this.prev = prev;
			this.next = next;
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}

	private Node<E> getFirstNode() {
		return list.get(0);
	}

	private Node<E> getLastNode() {
		return list.get(numberOfElements - 1);
	}

	private Node<E> getNode(int index) {
		index = Math.floorMod(index, size());
		return list.get(index);
	}
}
