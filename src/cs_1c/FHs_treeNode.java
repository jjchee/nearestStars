package cs_1c;

public class FHs_treeNode<E extends Comparable< ? super E > >
{
   // use public access so the tree or other classes can access members 
   public FHs_treeNode<E> lftChild, rtChild;
   public E data;
   public FHs_treeNode<E> myRoot;  // needed to test for certain error

   public FHs_treeNode( E d, FHs_treeNode<E> lft, FHs_treeNode<E> rt )
   {
      lftChild = lft; 
      rtChild = rt;
      data = d;
   }
   
   public FHs_treeNode()
   {
      this(null, null, null);
   }
   
   // function stubs -- for use only with AVL Trees when we extend
   public int getHeight() { return 0; }
   boolean setHeight(int height) { return true; }
}
