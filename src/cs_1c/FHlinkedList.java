package cs_1c;
import java.util.*;

// generic FHlinkedList class ------------------------------------
public class FHlinkedList<E> implements Cloneable, Iterable<E>, Collection<E>,
   Queue<E>, Deque<E>, List<E>
{
// definition of inner Node class ----------------------
   private class Node
   {
      Node prev, next;
      E data;

      Node( E x, Node prv, Node nxt )
      {
         prev = prv; next = nxt; data = x;
      }
     
      // defined in Node class because it only involves neighbor links
      void insertBefore(E x)
      {
         Node pNew = new Node(x, prev, this);
         prev.next = pNew;
         prev = pNew;
      }
      
      // defined in Node class because it only involves neighbor links
      E remove()
      {
         prev.next = next;
         next.prev = prev;
         return this.data;
      }
   };
   
   // Utility Pair class for returning (Node, index} pairs
   private class Pair
   {
      Node node;
      int index;
      
      Pair(Node node, int index) {this.node = node; this.index = index;}
   };
   
   // principal private data for FHlist
   private int mSize;
   private Node mHead, mTail;
   
   // for internal use
   private static final int NOT_FOUND = -1;
   
   // for iterator concurrency testing
   private int modCount = 0;

   // constructors
   public FHlinkedList() { clear(); } 
   public FHlinkedList( Collection<? extends E> rhs )
   {
      clear();
      addAll(rhs);
   }
   
   // fundamental List operations
   public int size() { return mSize; }
   public boolean isEmpty() { return mSize == 0; }
   
   public void clear()
   {
      mSize = 0;
      mHead = new Node(null, null, null);
      mTail = new Node(null, null, null);  // could set prev here...
      mHead.next = mTail;
      mTail.prev = mHead;  // ... but  do it here for clarity
      modCount++;
   }
   
   // private helper methods --------------------------
   
   // returns the Node in the index-th position of the list
   private Node getNode(int index)
   {
      Node p;
      int k;
      
      // we can't throw exception here because different callers have
      // different bounds they must obey -- so we let them throw it
      
      // use mHead or mTail, whichever is closer
      if (index < mSize/2)
         for (k = 0, p = mHead.next; k < index; p = p.next, k++)
            ;
      else
         for (k = mSize, p = mTail; k > index; p = p.prev, k--)
            ;
      return p;   
   }
   
   // returns both the node and index of first occurrence of Object o
   private Pair getFirstOccurrence(Object o)
   {
      Node p;
      int k;
      
      if(o != null)
      {
         for (k = 0, p = mHead.next; k < mSize; p = p.next, k++)
            if (o.equals(p.data) )
               return new Pair(p, k);
      }
       else
       {
          for (k = 0, p = mHead.next; k < mSize; p = p.next, k++)
             if (p.data == null )
                return new Pair(p, k);
       }
      return new Pair(null,NOT_FOUND);    
   }
  
   // returns both the node and index of first occurrence of Object o
   private Pair getLastOccurrence(Object o)
   {
      Node p;
      int k;

      if(o != null)
      {
         for (k = mSize-1, p = mTail.prev; k > 0; p = p.prev, k--)
            if (o.equals(p.data) )
               return new Pair(p, k);
      }
      else
      {
         for (k = mSize-1, p = mTail.prev; k > 0; p = p.prev, k--)
            if (p.data == null )
               return new Pair(p, k);   
      }
      return new Pair(null,NOT_FOUND);    
   }
   
   // FHlinkedList public methods ------------------------------------
   
   // accessors and mutators
   public E get(int index)
   {
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      return getNode(index).data;
   }

   public E getFirst()
   {
      if (mSize == 0)
         throw new NoSuchElementException();
      return mHead.next.data;
   }
   
   public E getLast()
   {
      if (mSize == 0)
         throw new NoSuchElementException();
      return mTail.prev.data;
   }
   
   public E peek()
   {
      if (mSize == 0)
         return null;
      return mHead.next.data;
   }

   public E peekFirst()
   {
      if (mSize == 0)
         return null;
      return mHead.next.data;
   }
   
   public E peekLast()
   {
      if (mSize == 0)
         return null;
      return mTail.prev.data;
   }
   
   // poll() equiv. to pollFirst()
   public E poll()
   {
      if (mSize == 0)
         return null;
      E retVal = mHead.next.data;
      mHead.next.remove();
      modCount++;
      mSize--;
      return retVal;
   }

   public E pollFirst()
   {
      if (mSize == 0)
         return null;
      E retVal = mHead.next.data;
      mHead.next.remove();
      modCount++;
      mSize--;
      return retVal;
   }
   
   public E pollLast()
   {
      if (mSize == 0)
         return null;
      E retVal = mTail.prev.data;
      mTail.prev.remove();
      modCount++;
      mSize--;
      return retVal;
   }

   
   public boolean contains(Object o)
   {
      return ( indexOf(o) != NOT_FOUND );
   }
   
   public E set(int index, E x)
   {
      E retVal;
      Node p;
      
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      p = getNode(index);
      retVal = p.data;
      p.data = x;
      return retVal;
   }
   
   public boolean add(E x)
   {
      // low-level methods should be efficient - don't call other add()
      mTail.insertBefore(x);
      mSize++;
      modCount++;
      return true;
   }

   public void add(int index, E x)
   {
      if (index < 0 || index > mSize) // == mSize allowed
         throw new IndexOutOfBoundsException();
      
      getNode(index).insertBefore(x);
      modCount++;
      mSize++;
   }
   
   public void addFirst(E x)
   {
      // low-level methods should be efficient - don't call other add()
      mHead.next.insertBefore(x);
      mSize++;
      modCount++; 
   }

   public void addLast(E x)
   {
      // low-level methods should be efficient - don't call other add()
      mTail.insertBefore(x);
      mSize++;
      modCount++; 
   }
   
   public boolean offer(E x)
   {
      // low-level methods should be efficient - don't call add()
      mTail.insertBefore(x);
      mSize++;
      modCount++;
      return true;
   }
   
   public boolean offerFirst(E x)
   {
      // low-level methods should be efficient - don't call addFirst()
      mHead.next.insertBefore(x);
      mSize++;
      modCount++; 
      return true;
   }

   public boolean offerLast(E x)
   {
      // low-level methods should be efficient - don't call other addLast()
      mTail.insertBefore(x);
      mSize++;
      modCount++;
      return true;
   }
   
   // equiv. to removeFirst()
   public E pop()
   {
      // low-level methods should be efficient - don't call  rmvFst()
     if (mSize == 0)
         throw new NoSuchElementException();
      mSize--;
      modCount++;
      return mHead.next.remove();
   }
   
   // equiv. to addFirst()
   public void push(E x)
   {
      // low-level methods should be efficient - don't call other addFst()
      mHead.next.insertBefore(x);
      mSize++;
      modCount++; 
   }
   
   public E remove()
   {
      // low-level methods should be efficient - don't call other rmv()
      if (mSize == 0)
         throw new NoSuchElementException();
      mSize--;
      modCount++;
      return mHead.next.remove();
   }
   
   public E removeFirst()
   {
      // low-level methods should be efficient - don't call other rmv()
     if (mSize == 0)
         throw new NoSuchElementException();
      mSize--;
      modCount++;
      return mHead.next.remove();
   }
   
   public E removeLast()
   {
      // low-level methods should be efficient - don't call other rmv()
     if (mSize == 0)
         throw new NoSuchElementException();
      mSize--;
      modCount++;
      return mTail.prev.remove();
   }
   
   public E remove(int index)
   {
      Node removedNode;
      
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      
      removedNode = getNode(index);
      mSize--;
      modCount++;
      return removedNode.remove();
   }
   
   public boolean remove(Object o)
   {
      Pair result = getFirstOccurrence(o);
      
      if (result.index == NOT_FOUND)
         return false;
      
      result.node.remove();
      mSize--;
      modCount++;
      return true;
   }
   
   public boolean removeFirstOccurrence(Object o)
   {
      // Since remove(o) is O(N), calling it adds minute overhead
      return remove(o);
   }
   
   public boolean removeLastOccurrence(Object o)
   {
      Pair result = getLastOccurrence(o);
      
      if (result.index == NOT_FOUND)
         return false;
      
      result.node.remove();
      mSize--;
      modCount++;
      return true;
   }
   
   protected void removeRange(int fromK, int toK)
   {
      Node p;
      int k;
      
      if (fromK < 0 || fromK >= mSize || toK > size() || toK < fromK)
         throw new IndexOutOfBoundsException();
      
      for (k = fromK, p = getNode(fromK); k < toK; k++, p = p.next)
         p.remove();
      mSize -= (toK - fromK);
      modCount++;
   }
   
   public boolean addAll( Collection<? extends E> rhs )
   {
      // this is not a low-level or common function, so ok to call add()
      if (rhs.size() == 0)
         return false;
      for(E x : rhs)
         add( x );
      return true;
   }
   
   public boolean addAll( int index, Collection<? extends E> rhs )
   {
      // this is not a low-level or common function, so ok to call add()
      if (rhs.size() == 0)
         return false;
      
      int k = 0;
      for(E x : rhs)
         add(k++, x);
      return true;
   }
   
   // other FHlinkedList methods to satisfy java.util.collection
   public boolean containsAll(Collection<?> c)
   {
      for(Object o : c)
         if ( !contains(o) )
            return false;
      return true;
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHlinkedList<E> newObject = (FHlinkedList<E>)super.clone();
      
      // this is a shallow copy - we are not duplicating/cloning objects
      newObject.clear();  // but, we have to separate the head/tail/size
      newObject.addAll(this);  // and have distinct nodes
      
      return newObject;
   }
   
   public int indexOf(Object o)
   {
      return getFirstOccurrence(o).index;
   }
   
   public int lastIndexOf(Object o)
   {
      return getLastOccurrence(o).index;
   }
   
   public boolean equals(Object o)
   {
      Node p1, p2;
      FHlinkedList<E> that;
      E thisData, thatData;
      
      if ( !(o instanceof FHlinkedList<?>) )
         return false;
      
      that = (FHlinkedList<E>)o;
      if (that.size() != mSize)
         return false;
      
      for(p1 = mHead.next, p2 = that.mHead.next; p1 != mTail;  
         p1 = p1.next, p2 = p2.next)
      {
         thisData = p1.data;
         thatData = p2.data;
         // we allow null values, so we have to test null==null first
         if (thisData == null || thatData == null)
         {
            if (thisData != null || thatData != null)
               return false;
         }
         else
         {
            if ( ! thisData.equals(thatData) )
               return false;
         }
      }
      return true;
   }
   
   public Object[] toArray() 
   {
      int k;
      Node p;
      
      Object[] retArray = new Object[mSize];
      for (k = 0, p = mHead.next; k < mSize; k++, p = p.next)
         retArray[k] = p.data;
      return retArray;
   }
   
   public <T> T[] toArray(T[] userArray)
   {
      int k;
      Node p;
      Object[] retArray;
      
      if (mSize > userArray.length)
         retArray = new Object[mSize];
      else
         retArray = userArray;
      
      for (k = 0, p = mHead.next; k < mSize; k++, p = p.next)
         retArray[k] = p.data;
      
      if (mSize < userArray.length)
         retArray[mSize] = null;
      
      return (T[])userArray;
   }
   
   public E element()
   {
      if (mSize == 0)
         throw new NoSuchElementException();
      return mHead.next.data;
   }
   
   
   // must be defined because this implements Collection
   public boolean retainAll(Collection<?> c)
   {
      throw new UnsupportedOperationException();
   }
   public boolean removeAll(Collection<?> c)
   {
      throw new UnsupportedOperationException();
   }
   
   // this is the only method that should really be supported, but I don't
   public List<E> subList(int fromIndex, int toIndex)
   {
      throw new UnsupportedOperationException();
   }      
   
   // ------------- Iterator / ListIterator --------------
   public java.util.Iterator<E> iterator() { return new FHiterator(); }
   public java.util.ListIterator<E> listIterator() 
   { 
      return new FHlistIterator();
   }
   
   public java.util.ListIterator<E> listIterator(int index) 
   {
      if ( index < 0 || index >= mSize )
         throw new ArrayIndexOutOfBoundsException();
      return new FHlistIterator(index); 
   }

   public java.util.Iterator<E> descendingIterator()
   { 
      return new FHdescendingIterator();
   }

   // internal Iterator class
   private class FHiterator implements java.util.Iterator<E>
   {
      // for use with derived FHlistIterator methods remove(), set().
      protected static final int NOT_VALID = -1;
      protected static final int NEXT = 10;
      protected static final int PREVIOUS = 11;
      
      protected Node mCurrentNode;
      protected int mCurrentIndex;
      
      // for ConcurrentModificationExceptions
      protected int mIterModCount = modCount;
      
      // for IllegalStateExceptions
      protected Node mLastNodeReturned = null; // valid: any Node object
      protected int mLastIteration = NOT_VALID; // valid: NEXT or PREVIUOS 
      
      // required interface implementations
      public boolean hasNext() { return mCurrentIndex < mSize; }

      public E next()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if( !hasNext() ) 
            throw new java.util.NoSuchElementException();
         mLastNodeReturned = mCurrentNode;
         mCurrentNode = mCurrentNode.next;
         mCurrentIndex++;
         mLastIteration = NEXT;
         return mLastNodeReturned.data;
      }

      public void remove()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if (mLastNodeReturned == null)
            throw new IllegalStateException ();
         mLastNodeReturned.remove();
         if (mLastIteration == NEXT)
            mCurrentIndex--;
         mSize--;
         
         // since we don't call host remove, do not incr. mIterModCount
         mLastNodeReturned = null;
      }
      
      //  constructors (default access for package only)
      FHiterator() 
      {
         mCurrentNode = mHead.next;
         mCurrentIndex = 0;
      }
   }
   
   // internal ListIterator class
   private class FHlistIterator extends FHiterator 
      implements java.util.ListIterator<E>
   {
      
      //  constructors (default access for package only)
      FHlistIterator() { super(); }
      
      FHlistIterator(int index)
      {
         super();
         if ( index < 0 || index >= mSize )
            return;
         mCurrentNode = getNode(index);
         mCurrentIndex = index;
      }
      
      public boolean hasPrevious() { return mCurrentIndex > 0; }

      public E previous()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if( !hasPrevious() ) 
            throw new java.util.NoSuchElementException();
         mCurrentNode = mCurrentNode.prev;
         mLastNodeReturned = mCurrentNode;
         mCurrentIndex--;
         mLastIteration = PREVIOUS;
         return mLastNodeReturned.data;
      }
      
      // next() and previous() guarantee 0 <= mCurrentIndex < mSize
      public int nextIndex() { return mCurrentIndex; }
      public int previousIndex() { return mCurrentIndex - 1; }
      
      // set and add
      public void set(E x) 
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if (mLastNodeReturned == null)
            throw new IllegalStateException ();
         mLastNodeReturned.data = x;
      }
      
      public void add(E x) 
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         mCurrentNode.insertBefore(x);
         mCurrentIndex++;
         mSize++;
         
         // since we don't call host add, do not incr. mIterModCount
         mLastNodeReturned = null;
      }
   }
   
   // internal Descending Iterator class
   private class FHdescendingIterator extends FHlistIterator 
   {
      // this is required by Dequeu; it does everything backwards
      public E next()
      {
         return previous();
      }
   
      FHdescendingIterator() 
      {
         mCurrentNode = mTail;
         mCurrentIndex = mSize;
      }
   }
}
