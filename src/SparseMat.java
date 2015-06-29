//SparseMat class *** FROM INSTRUCTOR'S SOLUTIONS ****

import cs_1c.*;
import java.util.*;
class SparseMat<E> implements Cloneable
{
   // protected enables us to safely make col/data public
   protected class MatNode implements Cloneable
   {
      public int col;
      public E data;

      // we need a default constructor for lists
      MatNode()
      {
         col = 0;
         data = null;
      }

      MatNode(int cl, E dt)
      {
         col = cl;
         data = dt;
      }

      public Object clone() throws CloneNotSupportedException
      {
         // shallow copy
         MatNode newObject = (MatNode)super.clone();
         return (Object) newObject;
      }
   }

   static public final int MIN_SIZE = 1;
   protected int rowSize, colSize;
   protected E defaultVal;
   protected FHarrayList < FHlinkedList< MatNode > > rows;

   public int getRowSize() { return rowSize; }
   public int getColSize() { return colSize; }

   // constructor creates an empty Sublist (no indices)
   public SparseMat( int numRows, int numCols, E defaultVal)
   {
      if ( numRows < MIN_SIZE || numCols < MIN_SIZE || defaultVal == null)
         throw new IllegalArgumentException();

      rowSize = numRows;
      colSize = numCols;
      allocateEmptyMatrix();
      this.defaultVal = defaultVal;
   }

   protected void allocateEmptyMatrix()
   {
      int row;
      rows = new FHarrayList < FHlinkedList< MatNode > >();
      for (row = 0; row < rowSize; row++)
         rows.add( new FHlinkedList< MatNode >());   // add a blank row
   }

   public void clear()
   {
      int row;

      for (row = 0; row < rowSize; row++)
         rows.get(row).clear();
   }
 
   // optional method
   public Object clone() throws CloneNotSupportedException
   {
      int row;
      ListIterator<MatNode> iter;
      FHlinkedList < MatNode > newRow;

      // shallow copy
      SparseMat<E> newObject = (SparseMat<E>)super.clone();

      // create all new lists for the new object
      newObject.allocateEmptyMatrix();

      // deep stuff
      for (row = 0; row < rowSize; row++)
      {
         newRow = newObject.rows.get(row);
         for (
             iter =  (ListIterator<MatNode>)rows.get(row).listIterator() ;
             iter.hasNext() ;
             // iterate in loop body
             )
         {
            newRow.add( (MatNode) iter.next().clone() );
         }
      }

      return newObject;
   }

   protected boolean valid(int row, int col)
   {
      if (row >= 0 && row < rowSize && col >= 0 && col < colSize)
         return true;
      return false;
   }

   public boolean set(int row, int col, E x)
   {
      if (!valid(row, col))
         return false;

      ListIterator<MatNode> iter;

      // iterate along the row, looking for column col
      for (
          iter =  (ListIterator<MatNode>)rows.get(row).listIterator() ;
          iter.hasNext() ;
          // iterate in loop body
          )
      {
         if ( iter.next().col == col )
         {
            if ( x.equals(defaultVal) )
               iter.remove();
            else
               iter.previous().data = x;
            return true;
         }
      }

      // not found
      if ( !x.equals(defaultVal) )
         rows.get(row).add( new MatNode(col, x) );
      return true;
   }

   public E get(int row, int col)
   {
      if (!valid(row, col))
         throw new IndexOutOfBoundsException();

      ListIterator<MatNode> iter;

      // iterate along the row, looking for column col
      for (
          iter =  (ListIterator<MatNode>)rows.get(row).listIterator() ;
          iter.hasNext() ;
          // iterate in loop body
          )
      {
         if ( iter.next().col == col )
            return iter.previous().data;
      }
      // not found
      return defaultVal;
   }

   public void showSubSquare(int start, int size)
   {
      int row, col;

      if (start < 0 || size < 0
            || start + size > rowSize
            || start + size > colSize )
         return;

      for (row = start; row < start + size; row++)
      {
         for (col = start; col < start + size; col++)
            System.out.print( String.format("%5.1f",
                  (Double)get(row, col)) + " " );
         System.out.println();
      }
      System.out.println();
   }
}