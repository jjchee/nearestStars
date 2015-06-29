package cs_1c;

//class EBookEntry -----------------------------------------------------
public class EBookEntry implements Comparable<EBookEntry>
{
   private String title, creator, subject;
   private int eTextNum;

   public static final int MIN_STRING = 1;
   public static final int MAX_STRING = 300;
   public static final int MAX_ENTRY_LENGTH = 10000;
   public static final int MAX_ID = 100000;

   // comparable tools
   public static final int SORT_BY_TITLE = 0;
   public static final int SORT_BY_CREATOR = 1;
   public static final int SORT_BY_SUBJECT = 2;
   public static final int SORT_BY_ID = 3;

   private static int sortKey = SORT_BY_CREATOR; 

   // default constructor
   EBookEntry()
   {
      title = "";
      creator = "";
      subject = "";
      eTextNum = 0;
   }

   // accessors
   public String getTitle()  { return title; }
   public String getCreator()  { return creator; }
   public String getSubject()  { return subject; }
   public int getETextNum()  { return eTextNum; }   

   // mutators
   public boolean setTitle(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      title = strArg;
      return true;
   }
   public boolean setCreator(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      creator = strArg;
      return true;
   }
   public boolean setSubject(String strArg)
   {
      if ( !validString(strArg) )
         return false;
      subject = strArg;
      return true;
   }
   public boolean setEtextNum(int intArg)
   {
      if (intArg < 1 || intArg > MAX_ID)
         return false;
      eTextNum = intArg;
      return true;
   }

   public static boolean setSortType( int whichType )
   {
      switch (whichType)
      {
      case SORT_BY_TITLE:
      case SORT_BY_CREATOR:
      case SORT_BY_SUBJECT:
      case SORT_BY_ID:
         sortKey = whichType;
         return true;
      default:
         return false;
      }
   }

   public int compareTo(EBookEntry other)
   {
      switch (sortKey)
      {
      case SORT_BY_TITLE:
         return (title.compareToIgnoreCase(other.title));
      case SORT_BY_CREATOR:
         return (creator.compareToIgnoreCase(other.creator));
      case SORT_BY_SUBJECT:
         return (subject.compareToIgnoreCase(other.subject));

      case SORT_BY_ID:
         return (eTextNum - other.eTextNum);
      default:
         return 0;
      }
   }

   public String toString()
   {
      return "   # " + eTextNum + "  ---------------\n"
      + "   \"" + title + "\"\n"
      + "   by " + creator  + "\n"
      + "   re: " + subject + "\n\n";
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