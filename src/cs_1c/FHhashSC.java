package cs_1c;
import java.util.*;

public class FHhashSC<E>
{
   static final int INIT_TABLE_SIZE = 97;
   static final double INIT_MAX_LAMBDA = 1.5;
   
   protected FHlinkedList<E>[] mLists;
   protected int mSize;
   protected int mTableSize;
   protected double mMaxLambda;

   public FHhashSC(int tableSize)
   {
      int k;
      
      if (tableSize < INIT_TABLE_SIZE)
         mTableSize = INIT_TABLE_SIZE;
      else
         mTableSize = nextPrime(tableSize);

      allocateMListArray();  // uses mTableSize;
      mMaxLambda = INIT_MAX_LAMBDA;
   }
   
   public FHhashSC()
   {
      this(INIT_TABLE_SIZE);
   }
   

   public boolean contains( E x)
   { 
      FHlinkedList<E> theList = mLists[myHash(x)];

      return theList.contains(x);
   }
   
   public void makeEmpty()
   {
      int k, size = mLists.length;

      for(k = 0; k < size; k++)
         mLists[k].clear();
      mSize = 0;  
   }
   
   public boolean insert( E x)
   {
      FHlinkedList<E> theList = mLists[myHash(x)];

      if ( theList.contains(x) )
         return false;

      // not found so we insert
      theList.addLast(x);

      // check load factor
      if( ++mSize > mMaxLambda * mTableSize )
         rehash();

      return true; 
   }
   
   public boolean remove( E x)
   {
      ListIterator<E> iter; 
      FHlinkedList<E> theList = mLists[myHash(x)];

      // do not use contains() because it involves two passes through list
      for (iter = theList.listIterator(); iter.hasNext(); )
         if ( x.equals(iter.next()) )
         {
            iter.remove();
            mSize--;
            return true;
         }

      // not found
      return false;   
   }
   

   
   public int size()  { return mSize; }
   public boolean setMaxLambda( double lam )
   {
      if (lam < .1 || lam > 100.)
         return false;
      mMaxLambda = lam;
      return true;
   }

   // protected methods of class ----------------------
   protected void rehash()
   {
      // we save old list and size then we can reallocate freely
      FHlinkedList<E>[] oldLists = mLists;
      int k, oldTableSize = mTableSize;
      ListIterator<E> iter;

      mTableSize = nextPrime(2*oldTableSize);
      
      // allocate a larger, empty array
      allocateMListArray();  // uses mTableSize;

      // use the insert() algorithm to re-enter old data
      mSize = 0;
      for(k = 0; k < oldTableSize; k++)
         for(iter = oldLists[k].listIterator(); iter.hasNext() ; )
            insert( iter.next());
   }
   
   protected int myHash( E x)
   {
      int hashVal;

      hashVal = x.hashCode() % mTableSize;
      if(hashVal < 0)
         hashVal += mTableSize;

      return hashVal;
   }
   
   protected static int nextPrime(int n)
   {
      int k, candidate, loopLim;

      // loop doesn't work for 2 or 3
      if (n <= 2 )
         return 2;
      else if (n == 3)
         return 3;

      for (candidate = (n%2 == 0)? n+1 : n ; true ; candidate += 2)
      {
         // all primes > 3 are of the form 6k +/- 1
         loopLim = (int)( (Math.sqrt((double)candidate) + 1)/6 );

         // we know it is odd.  check for divisibility by 3
         if (candidate%3 == 0)
            continue;

         // now we can check for divisibility of 6k +/- 1 up to sqrt
         for (k = 1; k <= loopLim; k++)
         {
            if (candidate % (6*k - 1) == 0)
               break;
            if (candidate % (6*k + 1) == 0)
               break;
         }
         if (k > loopLim)
            return candidate;
      }
   }
   
   void allocateMListArray()
   {
      int k;
      
      mLists = new FHlinkedList[mTableSize];
      for (k = 0; k < mTableSize; k++)
         mLists[k] = new FHlinkedList<E>();
   }
}
