package cs_1c;

import java.text.NumberFormat;
import java.util.*;

//class StarNearEarth -----------------------------------------------------
public class StarNearEarth implements Comparable<StarNearEarth>
{
   private String nameCns, spectralType, notes, nameCommon;
   private int rank, nameLhs, numComponents;
   private double rAsc, decl,  propMotionMag, propMotionDir, 
      parallaxMean,  parallaxVariance, magApparent, magAbsolute, 
      mass;
   private boolean whiteDwarfFlag;

   public static final int MIN_STRING = 1;
   public static final int MAX_STRING = 100;
   public static final int MIN_DUB  = -1000;
   public static final int MAX_DUB  = 1000;
   
   // comparable tools
   public static final int SORT_BY_NAME_CNS = 0;
   public static final int SORT_BY_SPECTRAL_TYPE = 1;
   public static final int SORT_BY_NAME_COMMON = 2;
   public static final int SORT_BY_RANK = 3;
   public static final int SORT_BY_NAME_LHS = 4;
   public static final int SORT_BY_NUM_COMPONENTS = 5;
   public static final int SORT_BY_RA = 6;
   public static final int SORT_BY_DEC = 7;
   public static final int SORT_BY_PROP_MOTION_MAG = 8;
   public static final int SORT_BY_PROP_MOTION_DIR = 9;
   public static final int SORT_BY_PARALLAX_MEAN = 10;
   public static final int SORT_BY_PARALLAX_VARIANCE = 11;
   public static final int SORT_BY_MAG_APPARENT = 12;
   public static final int SORT_BY_MAG_ABSOLUTE = 13;
   public static final int SORT_BY_MASS = 14;
   private static int sortKey = SORT_BY_RANK; 
   
   // default constructor
   public StarNearEarth()
   {
      nameCns = spectralType = notes = nameCommon = "";
      
      rank = nameLhs = numComponents = 0;
      rAsc = decl = propMotionMag = propMotionDir = parallaxMean 
         = parallaxVariance =  magApparent =  magAbsolute = mass;
      whiteDwarfFlag = false;
   }
   
   // accessors
   public String getNameCns() { return nameCns; }
   public String getSpectralType() { return spectralType; }
   public String getNotes() { return notes; }
   public String getNameCommon() { return nameCommon; }

   public int getRank() { return rank; }
   public int getNameLhs() { return nameLhs; }
   public int getNumComponents() { return numComponents; }

   public double getRAsc() { return rAsc; }
   public double getDec() { return decl; }
   public double getPropMotionMag() { return propMotionMag; }
   public double getPropMotionDir() { return propMotionDir; }
   public double getParallaxMean() { return parallaxMean; }
   public double getParallaxVariance() { return parallaxVariance; }
   public double getMagApparent() { return magApparent; }
   public double getMagAbsolute() { return magAbsolute; }
   public double getMass() { return mass; } 
   public boolean getWhiteDwarfFlag() { return whiteDwarfFlag; } 
   
   // mutators
   public boolean setNameCns(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      nameCns = strArg;
      return true;
   }
   
   public boolean setSpectralType(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      spectralType = strArg;
      return true;
   }

   public boolean setNotes(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      notes = strArg;
      return true;
   }
  
   public boolean setNameCommon(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      nameCommon = strArg;
      return true;
   }
   
   public boolean setRank(int intArg)
   {
      if (intArg < 1 || intArg > 999)
         return false;
      rank = intArg;
      return true;
   }
   
   public boolean setNameLhs(int intArg)
   {
      if (intArg < 1 || intArg > 10000)
         return false;
      nameLhs = intArg;
      return true;
   }
   
   public boolean setNumComponents(int intArg)
   {
      if (intArg < 1 || intArg > 10)
         return false;
      numComponents = intArg;
      return true;
   }
   
   public boolean setRAsc(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      rAsc = dblArg;
      return true;
   }
   
   public boolean setDec(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      decl = dblArg;
      return true;
   }
   
   public boolean setPropMotionMag(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      propMotionMag = dblArg;
      return true;
   }
   
   public boolean setPropMotionDir(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      propMotionDir = dblArg;
      return true;
   }
   
   public boolean setParallaxMean(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      parallaxMean = dblArg;
      return true;
   }
   
   public boolean setParallaxVariance(double dblArg)
   {
      if (dblArg < MIN_DUB || dblArg > MAX_DUB)
         return false;
      parallaxVariance = dblArg;
      return true;
   }
   
   public boolean setMagApparent(double dblArg)
   {
      if (dblArg < -30 || dblArg > 30)
         return false;
      magApparent = dblArg;
      return true;
   }
   
   public boolean setMagAbsolute(double dblArg)
   {
      if (dblArg < -30 || dblArg > 30)
         return false;
      magAbsolute = dblArg;
      return true;
   }
   
   public boolean setMass(double dblArg)
   {
      if (dblArg < 0 || dblArg > 100)
         return false;
      mass = dblArg;
      return true;
   }
   
   public void SetBWhiteDwarfFlag(boolean boolArg)
   {
      whiteDwarfFlag = boolArg;
   }
   
  
   public static boolean setSortType( int whichType )
   {
      switch (whichType)
      {
      case SORT_BY_NAME_CNS: case SORT_BY_SPECTRAL_TYPE: 
      case SORT_BY_NAME_COMMON: case SORT_BY_RANK: 
      case SORT_BY_NAME_LHS: case SORT_BY_NUM_COMPONENTS: 
      case SORT_BY_RA: case SORT_BY_DEC: case SORT_BY_PROP_MOTION_MAG: 
      case SORT_BY_PROP_MOTION_DIR: case SORT_BY_PARALLAX_MEAN: 
      case SORT_BY_PARALLAX_VARIANCE: case SORT_BY_MAG_APPARENT: 
      case SORT_BY_MAG_ABSOLUTE: case SORT_BY_MASS:
         sortKey = whichType;
         return true;
      default:
         return false;
      }
   }
   
   public int compareTo(StarNearEarth other)
   {
      switch (sortKey)
      {
      case SORT_BY_NAME_CNS:
         return (nameCns.compareToIgnoreCase(other.nameCns));
      case SORT_BY_SPECTRAL_TYPE:
         return (spectralType.compareToIgnoreCase(other.spectralType));
      case SORT_BY_NAME_COMMON:
         return (nameCommon.compareToIgnoreCase(other.nameCommon));
      case SORT_BY_RANK:
         return (rank - other.rank);
      case SORT_BY_NAME_LHS:
         return (nameLhs - other.nameLhs);
      case SORT_BY_NUM_COMPONENTS:
         return (numComponents - other.numComponents);
      case SORT_BY_RA:
         return (int)(rAsc - other.rAsc);
      case SORT_BY_DEC:
         return (int)(decl - other.decl) * 1000;
      case SORT_BY_PROP_MOTION_MAG:
         return (int)(propMotionMag - other.propMotionMag) * 1000;
      case SORT_BY_PROP_MOTION_DIR:
         return (int)(propMotionDir - other.propMotionDir) * 1000;
      case SORT_BY_PARALLAX_MEAN:
         return (int)(parallaxMean - other.parallaxMean) * 1000;
      case SORT_BY_PARALLAX_VARIANCE:
         return (int)(parallaxVariance - other.parallaxVariance) * 1000;
      case SORT_BY_MAG_APPARENT:
         return (int)(magApparent - other.magApparent) * 1000;
      case SORT_BY_MAG_ABSOLUTE:
         return (int)(magAbsolute - other.magAbsolute) * 1000;
      case SORT_BY_MASS:
         return (int)(mass - other.mass) * 1000;
      default:
         return 0;
      }
   }
   
   // a partial star implementation
   public String toString()
   {
      NumberFormat tidy = NumberFormat.getInstance(Locale.US);
      tidy.setMaximumFractionDigits(4);
      String retVal
       = new String("   #" + getRank() + ".  \""  
          + getNameCommon() + "\"  ----------\n"
          + "    LHS Name: " + getNameLhs()
          + " CNS Name: " + getNameCns() + "\n"
          + "    Apparent Mag: " + getMagApparent() + "\n"
          + "    Parallax Mean: " + getParallaxMean() 
          + " variance: " + getParallaxVariance() + "\n"
          + "    Right Asc: " + tidy.format(getRAsc()) 
          + " Dec: " + tidy.format(getDec()) + "\n"
          + "    Mass: " + getMass() 
          + " Prop Mot Mag: " + getPropMotionMag() + "\n");
      return retVal;
   }
   
   // utility for testing all String mutator  args
   private static boolean validString(String strArg)
   {
      if (strArg == null)
         return false;
      if (strArg.length() < MIN_STRING || strArg.length() > MAX_STRING)
         return false;
      return true;
   }
}