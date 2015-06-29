package cs_1c;
import java.util.*;

public class FHkruskal<E>
{
   private PriorityQueue< FHedge<E> > edgeHeap;
   FHgraph<E> inGraph;

   public FHkruskal (FHgraph<E> grph)
   {
      this();
      setInGraph(grph); 
   }

   public FHkruskal ()
   {
      edgeHeap = new PriorityQueue< FHedge<E> >();
      inGraph = null;
   }

   public void clear()
   {
      edgeHeap.clear();
   }

   public void setInGraph(FHgraph<E> grph)
   { 
      inGraph = grph;
      clear();
   }

   // algorithms
   public FHgraph<E> genKruskal()
   {
      Iterator< FHvertex<E> > iter;
      LinkedList< HashSet<FHvertex<E>> > vertexSets
          = new LinkedList< HashSet<FHvertex<E>> >();
      Iterator< HashSet<FHvertex<E>> > fIter;
      HashSet<FHvertex<E>> vertsInGraph, singleton, vertSet,
         vertSetSrc = null, vertSetDst = null;
      FHedge<E> smallestEdge;
      FHvertex<E> src, dst, vert;
      ArrayList< FHedge<E> > newEdges = new ArrayList< FHedge<E> >();
      int k, numVertsFound;
      FHgraph<E> outGraph = new FHgraph<E>();

      if (inGraph == null)
         return null;

      // get a local list of vertices
      vertsInGraph = inGraph.getVertSet();

      // form a forest of sets, initializing each with only 
      // one vertex from the graph
      for (k = 0, iter = vertsInGraph.iterator(); 
         iter.hasNext(); k++)
      {
         vert = iter.next(); 
         singleton = new HashSet<FHvertex<E>>();
         singleton.add(vert);
         vertexSets.add( singleton );
      }

      // form a binary min heap of edges so we can easily find min costs
      if (!buildEdgeHeap())
         return null;

      // test for empty to avoid inf. loop resulting from disconnected graph
      while (!edgeHeap.isEmpty() && vertexSets.size() > 1)
      {
         // pop smallest edge left in heap
         smallestEdge = edgeHeap.remove();
         src = smallestEdge.source;
         dst = smallestEdge.dest;

         // see if src and dst are in different sets.  if so, take union
         for (fIter = vertexSets.iterator(), numVertsFound = 0 ; 
            fIter.hasNext()  &&  (numVertsFound < 2) ; )
         {
            vertSet = fIter.next();
            if ( vertSet.contains(src) )
            {
               vertSetSrc = vertSet;
               numVertsFound++;
            }

            if ( vertSet.contains(dst) )
            {
               vertSetDst = vertSet;
               numVertsFound++;
            }
         }
         if (vertSetSrc == vertSetDst)  // same sets: reject
            continue;

         newEdges.add(smallestEdge);
         vertSetSrc.addAll(vertSetDst);
         vertexSets.remove(vertSetDst);
      }
      
      outGraph.setGraph(newEdges);
      return outGraph;
   }

   private boolean buildEdgeHeap()
   {
      HashSet< FHvertex<E> > vertsInGraph;
      Iterator< FHvertex<E> > vertIter;
      Iterator< Pair<FHvertex<E>, Double> > edgeIter;
      FHvertex<E> src, dst;
      Pair<FHvertex<E>, Double> edge;
      double cost;
      
      if (inGraph == null)
         return false;
      
      vertsInGraph = inGraph.getVertSet();
      for (vertIter = vertsInGraph.iterator(); vertIter.hasNext(); )
      {
         src =  vertIter.next();
         for (edgeIter = src.adjList.iterator(); edgeIter.hasNext(); )
         {
            edge = edgeIter.next();
            dst = edge.first;
            cost = edge.second;
            edgeHeap.add( new FHedge<E>(src, dst, cost) );
         }
      }
      return true;
   }
}
