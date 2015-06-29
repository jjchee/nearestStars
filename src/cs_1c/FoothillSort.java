package cs_1c;

public class FoothillSort
{
   // returns true if a modification was made to the array
   public static < E extends Comparable<? super E > > 
   boolean floatLargestToTop(E[] data, int top)
   {
      boolean changed = false;
      E temp;
      
      if ( !(data[0] instanceof String) )
      {
         // stop at top-1 = (worst case) arraySize-2 due to k+1 in loop
         for (int k = 0; k < top; k++)
            if (data[k].compareTo(data[k+1]) > 0)
            {
               temp = data[k];
               data[k] = data[k+1];
               data[k+1] = temp;
               changed = true;
            }
      }
      else
      {
         // For strings, ignore case (a choice we make)
         for (int k = 0; k < top; k++)
         {
            String strK = (String)data[k];
            String strKplus1 = (String)data[k+1];
            if ( strK.compareToIgnoreCase(strKplus1) > 0)
            {
               temp = data[k];
               data[k] = data[k+1];
               data[k+1] = temp;
               changed = true;
            }
         }
      }        
      return changed;
   }
   
   public static <E extends Comparable<? super E>> 
   void arraySort(E[] array)
   {
      for (int k = 0; k < array.length; k++)
         if ( !floatLargestToTop(array, array.length - 1 - k) )
            return;
   }
    
   // print out array with string as a title for the message box
   public static <E>
   void printArray(String title, E[] data)
   {      
      for (int k = 0; k < data.length; k++)
         System.out.print( " " + data[k] );
      System.out.println();
   }
}
