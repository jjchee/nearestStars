package cs_1c;
import java.util.*;

// generic FHarrayList class ------------------------------------
public class FHarrayList<E> implements Cloneable, Iterable<E>, Collection<E>
{
   private static final int DEFAULT_CAPACITY = 100;
   private static final int NOT_FOUND = -1;
   private int modCount = 0;

   private int mSize;
   private E[] mObjects;

   // constructors
   public FHarrayList() { clear(); }
   public FHarrayList(int initCapacity) 
   { 
      clear(); 
      ensureCapacity(initCapacity);
   }
 
   public FHarrayList( Collection<? extends E> rhs )
   {
      clear();
      addAll(rhs);
   }
   
   // fundamental List operations
   public int size() { return mSize; }
   public boolean isEmpty() { return mSize == 0; }
   public void trimToSize() { ensureCapacity(mSize); }
   
   public void clear()
   {
      mSize = 0;
      mObjects = (E[])new Object[DEFAULT_CAPACITY];
      modCount++;
   }
   
   public void ensureCapacity(int minCapacity)
   {
      if (mObjects != null)
      {
         if( minCapacity <= mObjects.length )
            return;
      }

      E[] srcArray = mObjects;
      mObjects = (E[])new Object[minCapacity];
      if (mSize > 0)
         System.arraycopy(srcArray, 0, mObjects, 0, mSize);
   }
   
   // accessors and mutators
   public E get(int index)
   {
      // can't rely on mObjects[] to throw because mSize is < mCapacity!
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      return mObjects[index];
   }

   public E set(int index, E x)
   {
      E retVal;
      
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      retVal = mObjects[index];
      mObjects[index] = x;
      return retVal;
   }

   public boolean contains(Object o)
   {
      return ( indexOf(o) != NOT_FOUND );
   }
   
   public boolean add(E x)
   {
      // low-level methods should be efficient - don't call other add() 
      if( mObjects.length == mSize )
         ensureCapacity(2*mSize + 1);
      mObjects[mSize++] = x;
      modCount++;
      return true;
   }

   public void add(int index, E x)
   {
      if (index < 0 || index > mSize)
         throw new IndexOutOfBoundsException();
      
      if( mObjects.length == mSize )
         ensureCapacity(2*mSize + 1);
      
      System.arraycopy(mObjects, index, mObjects, index + 1,
         mSize - index);

      mObjects[index] = x;
      mSize++;
      modCount++;
   }

   public E remove(int index)
   {
      E retVal;
      
      if (index < 0 || index >= mSize)
         throw new IndexOutOfBoundsException();
      
      retVal = mObjects[index];
      System.arraycopy(mObjects, index + 1, mObjects, index,
         mSize - index - 1);

      mSize--;
      modCount++;
      return retVal;
   }
   
   public boolean remove(Object o)
   {
      int k = indexOf(o);
      
      if (k == NOT_FOUND)
         return false;
      
      remove(k);
      return true;
   }
   
   protected void removeRange(int fromK, int toK)
   {
      if (fromK < 0 || fromK >= mSize || toK > mSize || toK < fromK)
         throw new IndexOutOfBoundsException();
      
      System.arraycopy(mObjects, toK, mObjects, fromK,
         mSize - toK );
      modCount++;
      mSize -= (toK - fromK);
   }
   
   public boolean addAll( Collection<? extends E> rhs )
   {
      if (rhs.size() == 0)
         return false;
      for(E x : rhs)
         add( x );
      return true;
   }
   
   public boolean addAll( int index, Collection<? extends E> rhs )
   {
      if (rhs.size() == 0)
         return false;
      
      // not so efficient - consider System.arraycopy + explicit assignment
      int k = index;
      for(E x : rhs)
         add(k++, x);

      return true;
   }
   
   // other FHarrayList methods to satisfy java.util.collection
   public boolean containsAll(Collection<?> c)
   {
      for(Object o : c)
         if ( !contains(o) )
            return false;
      return true;
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHarrayList<E> newObject = (FHarrayList<E>)super.clone();

      // shallow copy according to Java spec ... but not TOO shallow
      // this is a shallow copy - we are not duplicating/cloning objects
      newObject.mObjects = (E[])mObjects.clone();

      return newObject;
   }
   
   public int indexOf(Object o)
   {
      int k;
      
      if (o != null)
      {
         for(k = 0; k < mSize;  k++)
            if (o.equals(mObjects[k]) )
               return k;
      }
      else
      {
         for(k = 0; k < mSize;  k++)
            if (mObjects[k] == null )
               return k;
      }   
      return NOT_FOUND;
   }
   
   public int lastIndexOf(Object o)
   {
      int k;
      
      if (o != null)
      {
         for(k = mSize - 1; k >= 0;  k--)
            if (o.equals(mObjects[k]) )
               return k;
      }
      else
      {
         for(k = mSize - 1; k >= 0;  k--)
            if (mObjects[k] == null )
               return k;
      }
      
      return NOT_FOUND;
   }
   
   public boolean equals(Object o)
   {
      int k;
      FHarrayList<E> that;
      E thisData, thatData;
      
      if ( !(o instanceof FHarrayList<?>) )
         return false;
      
      that = (FHarrayList<E>)o;
      if (that.size() != mSize)
         return false;
      
      for(k = 0; k < mSize;  k++)
      {
         thisData = mObjects[k];
         thatData = that.get(k);
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
      Object[] retArray = mObjects.clone();
      return retArray;
   }
   
   public <T> T[] toArray(T[] userArray)
   {
      int k;
      Object[] retArray;
      
      if (mSize > userArray.length)
         retArray = new Object[mSize];
      else
         retArray = userArray;
      
      for (k = 0; k < mSize; k++)
         retArray[k] = mObjects[k];
      
      if (mSize < userArray.length)
         retArray[mSize] = null;
      
      return (T[])userArray;
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

   // internal Iterator class
   private class FHiterator implements java.util.Iterator<E>
   {
      // for use with derived FHlistIterator methods remove(), set().
      protected static final int NOT_VALID = -1;
      protected static final int NEXT = 10;
      protected static final int PREVIOUS = 11;
      
      protected int mCurrent;
      
      // for ConcurrentModificationExceptions
      protected int mIterModCount = modCount;
      
      // for IllegalStateExceptions
      protected int mLastIndexReturned = NOT_VALID; // valid: 0 to mSize-1
      protected int mLastIteration = NOT_VALID;   // valid: NEXT or PREVIOUS 
      
      // required interface implementations
      public boolean hasNext() { return mCurrent < mSize; }

      public E next()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if( !hasNext() ) 
            throw new java.util.NoSuchElementException();
         mLastIndexReturned = mCurrent++;
         mLastIteration = NEXT;
         return mObjects[mLastIndexReturned];
      }

      public void remove()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if (mLastIndexReturned == NOT_VALID)
            throw new IllegalStateException ();
         FHarrayList.this.remove(mLastIndexReturned);
         if (mLastIteration == NEXT)
            mCurrent--;
         mIterModCount++;
         mLastIndexReturned = NOT_VALID;
      }
      
      //  constructors (default access for package only)
      FHiterator() { mCurrent = 0; }
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
         mCurrent = index;
      }
      
      public boolean hasPrevious() { return mCurrent > 0; }

      public E previous()
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if( !hasPrevious() ) 
            throw new java.util.NoSuchElementException();
         mLastIndexReturned = --mCurrent;
         mLastIteration = PREVIOUS;
         return mObjects[mLastIndexReturned];
      }
      
      // next() and previous() guarantee 0 <= mCurrent <= mSize
      public int nextIndex() { return mCurrent; }
      public int previousIndex() { return mCurrent - 1; }
      
      // set and add
      public void set(E x) 
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         if (mLastIndexReturned == NOT_VALID)
            throw new IllegalStateException ();
         mObjects[mLastIndexReturned] = x;
      }
      
      public void add(E x) 
      {
         if (mIterModCount != modCount)
            throw new ConcurrentModificationException();
         FHarrayList.this.add(mCurrent++, x);
         mIterModCount++;
         mLastIndexReturned = NOT_VALID;
      }
   }
}
