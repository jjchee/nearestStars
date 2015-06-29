package cs_1c;

import java.io.*;
import java.util.*;

//class EBookEntryReader -----------------------------------------------------
public class EBookEntryReader
{
   ArrayList<EBookEntry> books = new ArrayList<EBookEntry>();
   private int numBooks;
   private boolean fileOpenError;
   private String bookFile;

   // constructor
   public EBookEntryReader(String fileName)
   {
      EBookEntry book;
      BufferedReader inFile;
      String line, entryString;

      numBooks = 0;
      fileOpenError = false;
      bookFile = "NO FILE NAME PROVIDED";

      if (fileName.length() == 0)
      {
         fileOpenError = true;
         return;
      }
      bookFile = fileName;

      // open file for reading
      try
      {  
         // ------- open and read the file
         inFile = new BufferedReader( 
               new FileReader(fileName) );

         while ( inFile.ready() )
         {
            line = inFile.readLine();
            if (isDataLine(line))
            {
               entryString = readOneEntry(inFile, line); // expands line to entry
               if (entryString == null)
               {
                  fileOpenError = true;
                  break;
               }
               // if not an English entry, we'll have prob with chars
               if ( !entryString.contains( ">en<" ) )
                  continue;

               book = readOneBook(entryString);
               books.add(book);
               numBooks++;
            }
         }
         inFile.close();
      }
      catch( FileNotFoundException e)
      {
         fileOpenError = true;
      } 
      catch( IOException e)
      {
         fileOpenError = true;
      }       
   }

   // accessors
   public EBookEntry getBook(int k)
   {
      if (k < 0 || k >= numBooks)
         return new EBookEntry();  // dummy return
      return books.get(k);
   }

   public String getFileName() { return bookFile; }
   public boolean readError() { return fileOpenError; }
   public int getNumBooks() { return numBooks; }

   // helpers
   // reads lines from the input stream, concatenating until it finds
   // the terminating tag, "</pgterms:etext>".  Result is a single long
   // line containing entire record wrapped in 
   // "<pgterms:etext> ... </pgterms:etext>" which it returns to client.
   // assumes first line containing <pgterms:etext> is already in 
   // line parameter - required for call
   private String readOneEntry(BufferedReader infile, String line)
   {
      String fullEntry = line;
      String nextLine = "";
      try
      {
         while ( infile.ready() 
               && fullEntry.length() < EBookEntry.MAX_ENTRY_LENGTH - 20 )
         {

            nextLine = infile.readLine();
            fullEntry += nextLine;
            if ( nextLine.equals("</pgterms:etext>") )
               break;
         }
      }
      catch (IOException e)
      {
         return null;
      }

      // if we have an unterminated entry, force it to be terminated.
      if ( !nextLine.equals("</pgterms:etext>") )
         fullEntry += "</pgterms:etext>";

      return fullEntry;
   }

   // reads one string in the above record (all lines of the record
   // are assumed to be concatenated into a single line prior to 
   // this call surrounded by <pgterms:etext> ... </pgterms:etext> )
   // and leaves data in an EBookEntry object for return to client
   private EBookEntry readOneBook(String entryString)
   {
      EBookEntry book = new EBookEntry();

      book.setEtextNum(readIDFromLine(entryString));
      book.setCreator(readStringFromEntry(entryString, "<dc:creator"));
      book.setTitle(readStringFromEntry(entryString, "<dc:title"));
      book.setSubject(readStringFromEntry(entryString, "<dc:subject"));

      return book;
   }

   // helpers
   private boolean isDataLine(String line)
   {
      if (line.length() < 15)
         return false;

      // check for a line of the form "<pgterms:etext --- "

      if ( line.substring(0,14).equals("<pgterms:etext") )
         return true;
      return false;    
   }

   int readIDFromLine(String line)
   {
      int startNumPos;
      int numLength;

      startNumPos = line.indexOf("ID=\"etext") + 9;
      numLength = line.substring(startNumPos).indexOf( "\"");

      if (startNumPos < 0 || startNumPos > EBookEntry.MAX_STRING 
            || numLength < 0 || numLength > 20 )
         return 0;
      else
         return Integer.valueOf(line.substring(startNumPos, 
               startNumPos + numLength));
   }

   String readStringFromEntry (String entryString, String delimiter)
   {
      int start, stop, k;
      String stringAfterDelimiter;

      if (delimiter.length() < 1 || entryString.length() < delimiter.length())
         return "(no data found)";

      // first find "<dc:title", e.g.
      start = entryString.indexOf(delimiter);
      if (start < 0)
         return "(no data found)";
      stringAfterDelimiter = entryString.substring(start+delimiter.length());

      // we're looking for something like ">A ..." rather than ">< ... "
      for (k = 0; k < stringAfterDelimiter.length() - 1; k++)
      {
         // rather than using isLetter() we check manually to avoid foreign
         if (stringAfterDelimiter.charAt(k) == '>' 
            &&
            // home-made isLetter()
            (
                  (stringAfterDelimiter.charAt(k+1) >='a' 
                     && stringAfterDelimiter.charAt(k+1) <= 'z')
                                       ||
                  (stringAfterDelimiter.charAt(k+1) >='A' 
                     && stringAfterDelimiter.charAt(k+1) <= 'Z') 
            )
         )
            break;
      }
      if (k == stringAfterDelimiter.length() - 1)
         return "(no data found)";

      // we've got the starting position for the raw data
      start = k + 1;
      stringAfterDelimiter = stringAfterDelimiter.substring(start); // cut tags
      stop = stringAfterDelimiter.indexOf("<");   // by above condition, cannot be 0

      return stringAfterDelimiter.substring(0, stop);
   }
}
