package cs_1c;

import java.io.*;
import java.util.*;

//class iTunesEntryReader -----------------------------------------------------
public class iTunesEntryReader
{
   ArrayList<iTunesEntry> tunes = new ArrayList<iTunesEntry>();
   private int numTunes;
   private boolean fileOpenError;
   private String tuneFile;
   
   // helper
   private boolean readOneEntry(BufferedReader infile, iTunesEntry tune)
   {
      String fileTitle, fileArtist, fileTime;
      int tuneTime;

      try
      {
      if ( infile.ready() )
         fileArtist = infile.readLine();
      else
         return false;

      if ( infile.ready() )
         fileTitle = infile.readLine();
      else
         return false;

      if ( infile.ready() )
         fileTime = infile.readLine();
      else
         return false;
      }
      catch (IOException e)
      {
         return false;
      }
      
      // convert string to int
      try
      {
         tuneTime = Integer.parseInt(fileTime);
      }
      catch (NumberFormatException e)
      {
         return false;
      }

      tune.setArtist(fileArtist);
      tune.setTitle(fileTitle);
      tune.setTime(tuneTime);

      return true;
   }
   
   // helper
   private boolean isDataLine(String line)
   {
      if (line.length() < 1)
         return false;  
      if (line.equals("#") )
         return true;
      return false;    
   }
   
   // constructor
   public iTunesEntryReader(String fileName)
   {
      iTunesEntry tune;
      BufferedReader inFile;
      String line;

      numTunes = 0;
      fileOpenError = false;
      tuneFile = "NO FILE NAME PROVIDED";

      if (fileName.length() == 0)
      {
         fileOpenError = true;
         return;
      }
      tuneFile = fileName;

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
               tune = new iTunesEntry();  // we allocate a new tune
               if ( !readOneEntry(inFile, tune) )
               {
                  fileOpenError = true;
                  break;
               }
               tunes.add(tune);
               numTunes++;
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
   public iTunesEntry getTune(int k)
   {
      if (k < 0 || k >= numTunes)
         return new iTunesEntry();  // dummy return
      return tunes.get(k);
   }
   
   public String getFileName() { return tuneFile; }
   public boolean readError() { return fileOpenError; }
   public int getNumTunes() { return numTunes; }

}
