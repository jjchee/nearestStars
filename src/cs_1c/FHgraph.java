package cs_1c;
import java.util.*;

// --- assumes definition of simple class Pair<E, F> in cis27a

// --- FHvertex class ------------------------------------------------------
class FHvertex<E>
{
   public static Stack<Integer> keyStack = new Stack<Integer>();
   public static final int KEY_ON_DATA = 0, KEY_ON_DIST = 1;
   public static int keyType = KEY_ON_DATA;
   public static final double INFINITY = Double.MAX_VALUE;
   public HashSet< Pair<FHvertex<E>, Double> > adjList 
      = new HashSet< Pair<FHvertex<E>, Double> >();
   public E data;
   public double dist;
   public FHvertex<E> nextInPath;  // for client-specific info

   public FHvertex( E x )
   {
      data = x;
      dist = INFINITY;
      nextInPath = null;
   }
   public FHvertex() { this(null); }

   public void addToAdjList(FHvertex<E> neighbor, double cost)
   {
      adjList.add( new Pair<FHvertex<E>, Double> (neighbor, cost) );
   }
   
   public void addToAdjList(FHvertex<E> neighbor, int cost)
   {
      addToAdjList( neighbor, (double)cost );
   }
   
   public boolean equals(Object rhs)
   {
      FHvertex<E> other = (FHvertex<E>)rhs;
      switch (keyType)
      {
      case KEY_ON_DIST:
         return (dist == other.dist);
      case KEY_ON_DATA:
         return (data.equals(other.data));
      default:
         return (data.equals(other.data));
      } 
   }
   
   public int hashCode()
   {
      switch (keyType)
      {
      case KEY_ON_DIST:
         Double d = dist;
         return (d.hashCode());
      case KEY_ON_DATA:
         return (data.hashCode());
      default:
         return (data.hashCode());
      }  
   }

   public void showAdjList()
   {
      Iterator< Pair<FHvertex<E>, Double> > iter ;
      Pair<FHvertex<E>, Double> pair;

      System.out.print( "Adj List for " + data + ": ");
      for (iter = adjList.iterator(); iter.hasNext(); )
      {
         pair = iter.next();
         System.out.print( pair.first.data + "(" 
            + String.format("%3.1f", pair.second)
            + ") " );
      }
      System.out.println();
   }
   
   public static boolean setKeyType( int whichType )
   {
      switch (whichType)
      {
      case KEY_ON_DATA:
      case KEY_ON_DIST:
         keyType = whichType;
         return true;
      default:
         return false;
      }
   }
   public static void pushKeyType() { keyStack.push(keyType); }
   public static void popKeyType() { keyType = keyStack.pop(); };
}

//--- FHedge class ------------------------------------------------------

class FHedge<E> implements Comparable< FHedge<E> >
{
   FHvertex<E> source, dest;
   double cost;
 
   FHedge( FHvertex<E> src, FHvertex<E> dst, Double cst)
   {
      source = src;
      dest = dst;
      cost = cst;
   }
   
   FHedge( FHvertex<E> src, FHvertex<E> dst, Integer cst)
   {
      this (src, dst, cst.doubleValue());
   }
   
   FHedge()
   {
      this(null, null, 1.);
   }
   
   public int compareTo( FHedge<E> rhs ) 
   {
      return (cost < rhs.cost? -1 : cost > rhs.cost? 1 : 0);
   }
}

public class FHgraph<E>
{
   // the graph data is all here --------------------------
   protected HashSet< FHvertex<E> > vertexSet;

   // public graph methods --------------------------------
   public FHgraph ()
   {
      vertexSet = new HashSet< FHvertex<E> >();
   }
   
   public FHgraph( FHedge<E>[] edges )
   {
      this();
      int k, numEdges;
      numEdges = edges.length;

      for (k = 0; k < numEdges; k++)
         addEdge( edges[k].source.data,  
            edges[k].dest.data,  edges[k].cost);
   }
   
   public void addEdge(E source, E dest, double cost)
   {
      FHvertex<E> src, dst;

      // put both source and dest into vertex list(s) if not already there
      src = addToVertexSet(source);
      dst = addToVertexSet(dest);

      // add dest to source's adjacency list
      src.addToAdjList(dst, cost);
   }
   
   public void addEdge(E source, E dest, int cost)
   {
      addEdge(source, dest, (double)cost);
   }
   
   // adds vertex with x in it, and always returns ref to it
   public FHvertex<E> addToVertexSet(E x)
   {
      FHvertex<E> retVal, vert;
      boolean successfulInsertion;
      Iterator< FHvertex<E> > iter;

      // save sort key for client
      FHvertex.pushKeyType();
      FHvertex.setKeyType(FHvertex.KEY_ON_DATA);

      // build and insert vertex into master list
      retVal = new FHvertex<E>(x);
      successfulInsertion = vertexSet.add(retVal);
      
      if ( successfulInsertion )
      {
         FHvertex.popKeyType();  // restore client sort key
         return retVal;
      }

      // the vertex was already in the set, so get its ref
      for (iter = vertexSet.iterator(); iter.hasNext(); )
      {
         vert = iter.next();
         if (vert.equals(retVal))
         {
            FHvertex.popKeyType();  // restore client sort key
            return vert;
         }
      }

      FHvertex.popKeyType();  // restore client sort key
      return null;   // should never happen
   }
   
   
   public void showAdjTable()
   {
      Iterator< FHvertex<E> > iter;

      System.out.println( "------------------------ ");
      for (iter = vertexSet.iterator(); iter.hasNext(); )
         (iter.next()).showAdjList();
      System.out.println();
   }
   
   public HashSet< FHvertex<E> > getVertSet() 
   { 
      return (HashSet< FHvertex<E> > )vertexSet.clone(); 
   }

   public void clear()
   {
      vertexSet.clear();
   }
   
   public void setGraph( ArrayList< FHedge<E> > edges )
   {
      int k, numEdges;
      numEdges = edges.size();

      clear();
      for (k = 0; k < numEdges; k++)
         addEdge( edges.get(k).source.data,  
            edges.get(k).dest.data,  edges.get(k).cost);
   }

   // algorithms
   public boolean dijkstra(E x)
   {
      FHvertex<E> w, s, v;
      Pair<FHvertex<E>, Double> edge;
      Iterator< FHvertex<E> > iter;
      Iterator< Pair<FHvertex<E>, Double> > edgeIter;
      Double costVW;
      Deque< FHvertex<E> > partiallyProcessedVerts
       = new LinkedList< FHvertex<E> >();

      s = getVertexWithThisData(x);
      if (s == null)
         return false;

      // initialize the vertex list and place the starting vert in p_p_v queue
      for (iter = vertexSet.iterator(); iter.hasNext(); )
         iter.next().dist = FHvertex.INFINITY;
      
      s.dist = 0;
      partiallyProcessedVerts.addLast(s);

      // outer dijkstra loop
      while( !partiallyProcessedVerts.isEmpty() )
      {
         v = partiallyProcessedVerts.removeFirst(); 
         
         // inner dijkstra loop: for each vert adj to v, lower its dist 
         // to s if you can
         for (edgeIter = v.adjList.iterator(); edgeIter.hasNext(); )
         {
            edge = edgeIter.next();
            w = edge.first;
            costVW = edge.second;
            if ( v.dist + costVW < w.dist )
            {
               w.dist = v.dist + costVW;
               w.nextInPath = v; 
               
               // w now has improved distance, so add w to PPV queue
               partiallyProcessedVerts.addLast(w); 
            }
         }
       }
      return true;
   }
   
   // applies dijkstra, print path - could skip dijkstra()
   public boolean showShortestPath(E x1, E x2)
   {
      FHvertex<E> start, stop, vert;
      Stack< FHvertex<E> > pathStack = new Stack< FHvertex<E> >();

      start = getVertexWithThisData(x1);
      stop = getVertexWithThisData(x2);
      if (start == null || stop == null)
         return false;

      // perhaps add argument opting to skip if pre-computed
      dijkstra(x1);
      
      if (stop.dist == FHvertex.INFINITY)
      {
         System.out.println("No path exists.");
         return false;
      }

      for (vert = stop; vert != start; vert = vert.nextInPath)
         pathStack.push(vert);
      pathStack.push(vert);

      System.out.println( "Cost of shortest path from " + x1 
            + " to " + x2 + ": " + stop.dist );
      while (true)
      {
         vert = pathStack.pop();
         if ( pathStack.empty() )
         {
            System.out.println( vert.data );
            break;
         }
         System.out.print( vert.data + " -> ");
      }
      return true;
   }

   // applies dijkstra, prints distances - could skip dijkstra()
   public boolean showDistancesTo(E x)
   {
      
      Iterator< FHvertex<E> > iter;
      FHvertex<E> vert;

      if (!dijkstra(x))
         return false;
 
      for (iter = vertexSet.iterator(); iter.hasNext(); )
      {
         vert = iter.next();
         System.out.println( vert.data + " " + vert.dist); 
      }
      System.out.println();
      return true;
   }

   protected FHvertex<E> getVertexWithThisData(E x)
   {
      FHvertex<E> searchVert, vert;
      Iterator< FHvertex<E> > iter;

      // save sort key for client
      FHvertex.pushKeyType();
      FHvertex.setKeyType(FHvertex.KEY_ON_DATA);

      // build vertex with data = x for the search
      searchVert = new FHvertex<E>(x);


      // the vertex was already in the set, so get its ref
      for (iter = vertexSet.iterator(); iter.hasNext(); )
      {
         vert = iter.next();
         if (vert.equals(searchVert))
         {
            FHvertex.popKeyType();
            return vert;
         }
      }
      
      FHvertex.popKeyType();
      return null;   // not found
   }
}
