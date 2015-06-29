package cs_1c;

public class FHtreeNode<E>
{
   // use protected access so the tree, in the same package, 
   // or derived classes can access members 
   protected FHtreeNode<E> firstChild, sib, prev;
   protected E data;
   protected FHtreeNode<E> myRoot;  // needed to test for certain error

   public FHtreeNode( E d, FHtreeNode<E> sb, FHtreeNode<E> chld, FHtreeNode<E> prv )
   {
      firstChild = chld; 
      sib = sb;
      prev = prv;
      data = d;
      myRoot = null;
   }
   
   public FHtreeNode()
   {
      this(null, null, null, null);
   }
   
   public E getData() { return data; }

   // for use only by FHtree (default access)
   protected FHtreeNode( E d, FHtreeNode<E> sb, FHtreeNode<E> chld, 
      FHtreeNode<E> prv, FHtreeNode<E> root )
   {
      this(d, sb, chld, prv);
      myRoot = root;
   }
}
