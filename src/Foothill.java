import cs_1c.*;

public class Foothill
{
   // ------- main --------------
   public static void main(String[] args) throws Exception
   {
      String outString;
      int k, arraySize, row, col;
      double maxX, minX, maxY, minY, maxZ, minZ,
      xRange, yRange, zRange, 
      xConversion, yConversion, zConversion;
      final int NUM_COLS = 70;
      final int NUM_ROWS = 35;
   
      StarNearEarthReader  starInput 
         = new StarNearEarthReader("nearest_stars.txt");

      if (starInput.readError())
      {
         System.out.println("couldn't open " + starInput.getFileName()
            + " for input.");
         return;
      }

      // do this just to see if our read went well
      System.out.println(starInput.getFileName());
      System.out.println(starInput.getNumStars());

      // create an array of objects for our own use:
      arraySize = starInput.getNumStars();
      SNE_Analyzer[] starArray = new SNE_Analyzer[arraySize];
      for (k = 0; k < arraySize; k++)
         starArray[k] =  new SNE_Analyzer( starInput.getStar(k) );

      // display cartesian coords
      for (k = 0; k < arraySize; k++)
      {
         System.out.println(starArray[k].getNameCommon() + " "
               + starArray[k].coordToString());
      }

      // now for the graphing
      // get max and min coords for scaling
      maxX = minX = maxY = minY = maxZ = minZ = 0;
      double xVal, yVal, zVal;
      for (k = 0; k < arraySize; k++)
      {
         // not shown;
         SNE_Analyzer star = starArray[k];
         xVal = star.getX();
         yVal = star.getY();
         zVal = star.getZ();
         if (xVal > maxX)
         {
            maxX = star.getX();
         }
         if (xVal < minX)
         {
            minX = xVal;
         }
         if (yVal > maxY)
         {
            maxY = star.getY();
         }
         if (yVal < minY)
         {
            minY = yVal;
         }
         if (zVal > maxZ)
         {
            maxZ = star.getZ();
         }
         if (zVal < minZ)
         {
            minZ = zVal;
         }
      }
      xRange = maxX - minX;
      yRange = maxY - minY;
      zRange = maxZ - minZ;
      SparseMat<Character> starMap = 
            new SparseMat<Character>(NUM_ROWS, NUM_COLS, ' ');

      System.out.println();
      ///*
      System.out.println("x-y projection");
      xConversion = (NUM_COLS - 1) / xRange;
      yConversion = (NUM_ROWS - 1) / yRange;

      int rank;
      for (k = 0; k < arraySize; k++)
      {
         SNE_Analyzer star = starArray[k];
         rank = star.getRank();
         xVal = star.getX();
         yVal = star.getY();
         row = (int) (yConversion * (yVal - minY));
         col = (int) (xConversion * (xVal - minX));

         if (rank < 10)
         {
            starMap.set(row, col, Character.forDigit(rank, 10));
         }
         else
         {
            starMap.set(row, col, '*');
         }
      }
      //*/
      /*
      System.out.println("x-z projection");
      xConversion = (NUM_COLS - 1) / xRange;
      zConversion = (NUM_ROWS - 1) / zRange;

      int rank;
      for (k = 0; k < arraySize; k++)
      {
         SNE_Analyzer star = starArray[k];
         rank = star.getRank();
         xVal = star.getX();
         zVal = star.getZ();
         row = (int) (zConversion * (zVal - minZ));
         col = (int) (xConversion * (xVal - minX));

         if (rank < 10)
         {
            starMap.set(row, col, Character.forDigit(rank, 10));
         }
         else
         {
            starMap.set(row, col, '*');
         }
      }
      */
      /*
      System.out.println("y-z projection");
      yConversion = (NUM_COLS - 1) / yRange;
      zConversion = (NUM_ROWS - 1) / zRange;

      int rank;
      for (k = 0; k < arraySize; k++)
      {
         SNE_Analyzer star = starArray[k];
         rank = star.getRank();
         //xVal = star.getX();
         yVal = star.getY();
         zVal = star.getZ();
         row = (int) (zConversion * (zVal - minZ));
         col = (int) (yConversion * (yVal - minY));

         if (rank < 10)
         {
            starMap.set(row, col, Character.forDigit(rank, 10));
         }
         else
         {
            starMap.set(row, col, '*');
         }
      }
      */

      // set sun at center
      row = NUM_ROWS / 2;
      col = NUM_COLS / 2;
      starMap.set( row, col, 'S' );

      for (row = 0; row < NUM_ROWS; row++)
      {
         outString = "";
         // inner loop that builds outString not shown
         for (col = 0; col < NUM_COLS; col++)
         {
            outString += starMap.get(row, col);
         }
         System.out.println( outString );
      }
   }
}

class SNE_Analyzer extends StarNearEarth
{
   private double x, y, z;

   // constructors

   public SNE_Analyzer()
   {
      x = 0;
      y = 0;
      z = 0;
   }

   public SNE_Analyzer(StarNearEarth sne)
   {
      setRank(sne.getRank());
      setNameCns(sne.getNameCns());
      setNumComponents(sne.getNumComponents());
      setNameLhs(sne.getNameLhs());
      setRAsc(sne.getRAsc());
      setDec(sne.getDec());
      setPropMotionMag(sne.getPropMotionMag());
      setPropMotionDir(sne.getPropMotionDir());
      setParallaxMean(sne.getParallaxMean());
      setParallaxVariance(sne.getParallaxVariance());
      SetBWhiteDwarfFlag(sne.getWhiteDwarfFlag());
      setSpectralType(sne.getSpectralType());
      setMagApparent(sne.getMagApparent());
      setMagAbsolute(sne.getMagAbsolute());
      setMass(sne.getMass());
      setNotes(sne.getNotes());
      setNameCommon(sne.getNameCommon());
      calcCartCoords();
   }

   // accessors
   double getX()
   {
      return x;
   }

   double getY()
   {
      return y;
   }

   double getZ()
   {
      return z;
   }

   public void calcCartCoords()
   {
      double LY = 3.262 / getParallaxMean();
      double RArad = Math.PI / 180 * getRAsc();
      double DECrad = Math.PI / 180 * getDec();
      x = LY * Math.cos(DECrad) * Math.cos(RArad);
      y = LY * Math.cos(DECrad) * Math.sin(RArad);
      z = LY * Math.sin(DECrad);
   }

   public String coordToString()
   {
      return "(" + x + ", " + y + ", " + z + ")";
   }
}