/**
 * Amanda Stensland amst8501
 * Veronica Stensland vest915
 * 
 * Implements a binary heap. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 */
public class DHeap<AnyType extends Comparable<? super AnyType>> {
  private static final int DEFAULT_MAX_CHILDREN = 2;
  private int currentSize;
  private AnyType[] array;
  private int maxChildren;

  /**
   * Construct the d-ary heap.
   * Om vi inte skickar in maxChildren när vi skapar vår DHeap,
   * sätt maxChildren till ett defaultvärde på 2.
   */
  public DHeap() {
    this(DEFAULT_MAX_CHILDREN);
  }

  /**
   * Construct the d-ary heap.
   * 
   * @param capacity the max size of the d-ary heap's array.
   * @param maxChildren the maximum number of children per node.
   */
  public DHeap(int maxChildren) {
    if (maxChildren < 2) throw new IllegalArgumentException();
    this.currentSize = 0;
    this.array = (AnyType[]) new Comparable[maxChildren + 2];
    this.maxChildren = maxChildren;
  }

  /**
   * Construct the d-ary heap given an array of items.
   */
  public DHeap(AnyType[] items) {
    this.currentSize = items.length;
    this.array = (AnyType[]) new Comparable[(currentSize + 2) * 11 / 10];
    int i = 1;
    for (AnyType item : items)
      array[i++] = item;
    buildHeap();
  }

  /**
   * Insert into the priority queue, maintaining heap order. Duplicates are
   * allowed.
   * 
   * @param item the item to insert.
   */
  public void insert(AnyType item) {
    if (currentSize == array.length - 1)
      enlargeArray(array.length * 2 + 1);
    array[++currentSize] = item;
    percolateUp(currentSize);
  }

  /**
   * Enlarges the heap array.
   * 
   * @param newSize the new size to enlarge to.
   */
  private void enlargeArray(int newSize) {
    AnyType[] old = array;
    array = (AnyType[]) new Comparable[newSize];
    for (int i = 0; i < old.length; i++) array[i] = old[i];
  }

  /**
   * Find the smallest item in the priority queue.
   * 
   * @return the smallest item, or throw an UnderflowException if empty.
   */
  public AnyType findMin() {
    if (isEmpty()) throw new UnderflowException();
    return array[1];
  }

  /**
   * Remove the smallest item from the priority queue.
   * 
   * @return the smallest item, or throw an UnderflowException if empty.
   */
  public AnyType deleteMin() {
    if (isEmpty()) throw new UnderflowException();
    AnyType removedValue = array[1]; // Spara så värdet kan returneras när det tagits bort.
    array[1] = array[currentSize]; // Ersätt värdet i root-index med värdet från tail-index.
    array[currentSize] = null;
    currentSize--; // Förminska arrayen, dsv. ta bort tail-index.
    if (currentSize > 1) percolateDown(1); // Bubbla ner värdet från root-index till rätt index.
    return removedValue; // Returnera värdet som tagits bort.
  }

  /**
   * Establish heap order property from an arbitrary arrangement of items. Runs in
   * linear time.
   */
  private void buildHeap() {
    for (int i = currentSize / 2; i > 0; i--)
      percolateDown(i);
  }

  /**
   * Test if the priority queue is logically empty.
   * 
   * @return true if empty, false otherwise.
   */
  public boolean isEmpty() {
    return currentSize == 0;
  }

  /**
   * Make the priority queue logically empty.
   */
  public void makeEmpty() {
    currentSize = 0;
  }

  /**
   * Internal method for swapping values between index.
   * 
   * @param firstIndex first value to be swapped.
   * @param secondIndex second value to be swapped.
   */
  private void swap(int firstIndex, int secondIndex) {
    AnyType firstValue = this.array[firstIndex];
    this.array[firstIndex] = this.array[secondIndex];
    this.array[secondIndex] = firstValue;
  }

  /**
   * Internal method to percolate down in the heap.
   * 
   * @param index the index at which the percolate begins.
   */
  private void percolateUp(int index) {

    // Om index är mindre än 2, dsv. root-index, gör ingenting.

    if (index < 2)
      return;

    int parentIndex = parentIndex(index);
    AnyType parentValue = array[parentIndex];
    AnyType value = array[index];

    if (parentValue == null) return;

    if (value.compareTo(parentValue) < 0) {
      swap(index, parentIndex);
      index = parentIndex;
      percolateUp(index);
    }
  }

  /**
   * Internal method to percolate down in the heap.
   * 
   * @param index the index at which the percolate begins.
   */
  private void percolateDown(int index) {
    int firstChildIndex = firstChildIndex(index);
    int lastChildIndex = firstChildIndex + maxChildren - 1;
    int smallestIndex = index;
    AnyType smallestValue = array[index];

    if (currentSize < lastChildIndex)
      lastChildIndex = currentSize;

    // Loopa igenom alla barn och hitta det minsta värdet.

    for (int i = firstChildIndex; i <= lastChildIndex; i++) {
      AnyType value = array[i];

      // Om barnet har ett mindre värde, markera det som minsta värdet.

      if (value.compareTo(smallestValue) < 0) {
        smallestIndex = i;
        smallestValue = array[smallestIndex];
      }
    }
      
    // Om det tidigare visade sig finnas ett barn med ett mindre värde,
    // byt plats på barnet och föräldern.
    
    AnyType parentValue = array[index];
    if (smallestValue.compareTo(parentValue) < 0) {
      swap(index, smallestIndex);
      percolateDown(smallestIndex);
    }
  }

  public int parentIndex(int index) {
    if (index < 2) throw new IllegalArgumentException();
    return (index - 2) / maxChildren + 1;
  }

  public int firstChildIndex(int index) {
    if (index < 1) throw new IllegalArgumentException();
    return index * maxChildren + 2 - maxChildren;
  }

  public int size() {
    return currentSize;
  }

  public AnyType get(int index) {
    return array[index];
  }
}
