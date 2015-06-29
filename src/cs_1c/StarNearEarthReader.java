package cs_1c;

import java.io.*;
import java.util.*;

//class iTunesEntryReader -----------------------------------------------------
public class StarNearEarthReader
{
   ArrayList<StarNearEarth> stars = new ArrayList<StarNearEarth>();
   private int numStars;
   private boolean fileOpenError;
   private String starFile;
   
   // accessors
   public StarNearEarth getStar(int k)
   {
      if (k < 0 || k >= numStars)
         return new StarNearEarth();  // dummy return
      return stars.get(k);
   }
   
   public String getFileName() { return starFile; }
   public boolean readError() { return fileOpenError; }
   public int getNumStars() { return numStars; }
   
   // helper
   private StarNearEarth readOneStar(String line)
   {
      int endOfField;
      StarNearEarth star = new StarNearEarth();

      // we show the end column by not pre-computing string length:  END_COL - START_COL + 1
      star.setRank((int)convertToDouble(line.substring(0, 3))); 
      star.setNameCns(line.substring(5, 20));
      star.setNumComponents((int)convertToDouble(line.substring(20, 22)));
      star.setNameLhs((int)convertToDouble(line.substring(23, 29))); 
      star.setRAsc(convertRA(line.substring(32, 42)));
      star.setDec(convertDEC(line.substring(43, 52)));
      star.setPropMotionMag(convertToDouble(line.substring(56, 62)));
      star.setPropMotionDir(convertToDouble(line.substring(63, 68)));
      star.setParallaxMean(convertToDouble(line.substring(73, 80)));
      star.setParallaxVariance(convertToDouble(line.substring(81, 88)));
      star.SetBWhiteDwarfFlag((line.substring(95, 96).equals("D")));
      star.setSpectralType(line.substring(96, 105));
      star.setMagApparent(convertToDouble(line.substring(108, 113)));
      star.setMagAbsolute(convertToDouble(line.substring(116, 121)));
      star.setMass(convertToDouble(line.substring(123, 128)));      
      if (line.length() < 132)
         star.setNotes("");
      else
      {
         endOfField = line.length() < 151? line.length() : 150; 
         star.setNotes(line.substring(131, endOfField));
      }

      if (line.length() < 153)
         star.setNameCommon("(no common name)");
      else    
        star.setNameCommon(line.substring(152, line.length())); 

      return star; 
   }
   
   // helper
   private double convertToDouble(String strToCnvrt)
   {
      double retDbl;
      try
      {
         retDbl = Double.parseDouble(strToCnvrt);
      }
      catch (Exception e)
      {
         return 0.;
      }
      return retDbl;
   }
   
   private boolean isDataLine(int lineNum, String line)
   {
      String s = String.format("%3d", lineNum) + ".";
      
      if (line.length() < 4)
         return false; 
      
      // check for a line of the form " k.  --- "
      if ( line.substring(0, 4).equals(s) )
         return true;
      return false;    
   }
   
   // constructor
   public StarNearEarthReader(String fileName)
   {
      int k;
      StarNearEarth star;
      BufferedReader inFile;
      String line;

      numStars = 0;
      fileOpenError = false;
      starFile = "NO FILE NAME PROVIDED";

      if (fileName.length() == 0)
      {
         fileOpenError = true;
         return;
      }
      starFile = fileName;

      // open file for reading
      try
      {  
         // ------- open and read the file
         inFile = new BufferedReader( 
            new FileReader(fileName) );
         
         // for each line that starts #. read and add to ArrayList
         for ( k = 1; inFile.ready(); )
         {
            line = inFile.readLine();
            if (isDataLine(k, line))
            {
               star = readOneStar(line);
               stars.add(star);
               k++;
               numStars++;
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
   
   // static methods for converting strings and RA/DEC to single floats
   private double hmsToFloatDegree(int hr, int min, double sec)
   {
      double retDbl;

      retDbl = hr + min/60. + sec/3600.;
      retDbl*= 15;
      return retDbl;
   }

   private double dmsToFloatDegree(int deg, int min, double sec)
   {
      double retDbl;
      boolean sign = (deg > 0);

      retDbl = Math.abs(deg) + min/60. + sec/3600.;
      return sign? retDbl : - retDbl;
   }

   private double convertRA(String sRa)
   {
      int hr, min;
      double sec, retVal;

      // cout << " [" << sRa << "] ";
      hr = (int)convertToDouble(sRa.substring(0,3));
      min = (int)convertToDouble(sRa.substring(3,6));
      sec = (double)convertToDouble(sRa.substring(6,9));
      if (sec < 0 || sec > 60.001)  // sanity - for blank seconds
         sec = 0;
      retVal = hmsToFloatDegree(hr, min, sec);
      return retVal;
   }

   private double convertDEC(String sDms)
   {
      int deg, min;
      double sec, retVal;

      // cout << " [" << sDms << "] ";
      deg = (int)convertToDouble(sDms.substring(0,3));
      min = (int)convertToDouble(sDms.substring(3,6));
      sec = (double)convertToDouble(sDms.substring(6,9));
      if (sec < 0 || sec > 60.001)  // sanity - for blank seconds
         sec = 0;
      retVal = dmsToFloatDegree(deg, min, sec);
      return retVal;
   }
}
