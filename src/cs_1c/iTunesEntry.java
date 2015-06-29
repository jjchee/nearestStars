package cs_1c;

//class iTunesEntry -----------------------------------------------------
public class iTunesEntry implements Comparable<iTunesEntry>
{
   private String title, artist;
   private int tuneTime;

   public static final int MIN_STRING = 1;
   public static final int MAX_STRING = 300;
   public static final int MAX_TIME = 10000000;
   
   // comparable tools
   public static final int SORT_BY_TITLE = 0;
   public static final int SORT_BY_ARTIST = 1;
   public static final int SORT_BY_TIME = 2;

   private static int sortKey; 
   
   // default constructor
   iTunesEntry()
   {
      title = "";
      artist = "";
      tuneTime = 0;
   }
   
   // accessors
   public String getTitle()  { return title; }
   public String getArtist()  { return artist; }
   public int getTime()  { return tuneTime; }   
   
   // mutators
   public boolean setTitle(String sArg)
   {
      if ( !validString(sArg) )
         return false; 
      title = sArg;
      return true;
   }
   
   public boolean setArtist(String sArg)
   {
      if ( !validString(sArg) )
         return false;
      artist = sArg;
      return true;
   }
   public boolean setTime(int nArg)
   {
      if (nArg < 0 || nArg > MAX_TIME)
         return false;
      tuneTime = nArg;
      return true;
   }
   
   public String getArtistLastName()
   {
      // search for first blank from end of string
      // assume no trailing spaces
      int k, length;

      length = artist.length();
      if ( length < 1 )
         return "";

      for (k = length-1; k >= 0; k--)
      {
         if (artist.charAt(k) == ' ')
            break;
      }

      if (k >= length-1 )
         return "";

      return artist.substring(k + 1, artist.length()-1);
   }
   
   private static int convertStringToSeconds(String strToCnvrt)
   {
      int colonPos;
      int minutes, seconds;

      if (strToCnvrt.length() < 1)
         return 0;
      colonPos = strToCnvrt.indexOf(":");
      if (colonPos < 0 || colonPos > iTunesEntry.MAX_STRING)
         return 0;

      try
      {
         minutes = Integer.parseInt( strToCnvrt.substring(0, colonPos) );
         seconds = Integer.parseInt(
            strToCnvrt.substring(colonPos + 1, 
                  strToCnvrt.length()-1) );
      }
      catch (NumberFormatException e)
      {
         return 0;
      }

      return minutes*60 + seconds;
   }
   
   public String convertTimeToString() 
   {
      int minutes, seconds;
      String strSeconds, strMinutes;

      minutes = tuneTime / 60;
      seconds = tuneTime % 60;

      strMinutes = "" + minutes;
      strSeconds = "" + seconds;

      if (strSeconds.length() < 2)
         strSeconds = "0" + strSeconds;

      return strMinutes + ":" + strSeconds;
   }

  
   public static boolean setSortType( int whichType )
   {
      switch (whichType)
      {
      case SORT_BY_TITLE:
      case SORT_BY_ARTIST:
      case SORT_BY_TIME:
         sortKey = whichType;
         return true;
      default:
         return false;
      }
   }
   
  // we allow this to be defined by one field, not full identity - a choice
  public int compareTo(iTunesEntry other)
   {
      switch (sortKey)
      {
      case SORT_BY_TITLE:
         return (title.compareToIgnoreCase(other.title));
      case SORT_BY_ARTIST:
         // get last name from string
         // stack the last name before the first - no worries about trailing last
         return (getArtistLastName() + artist).compareToIgnoreCase(
            other.getArtistLastName() + other.artist );

      case SORT_BY_TIME:
         return (tuneTime - other.tuneTime);
      default:
         return 0;
      }
   }
   
   public String toString()
   {
      return artist + " | "
         + title + " | "
         + " " + convertTimeToString();
   }
   
   // utility for testing all String mutator  args
   private static boolean validString(String sArg)
   {
      if (sArg == null)
         return false;
      if (sArg.length() < MIN_STRING || sArg.length() > MAX_STRING)
         return false;
      return true;
   }
}