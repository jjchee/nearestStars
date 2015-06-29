package cs_1c;

class AvlNode<E extends Comparable< ? super E > >
extends FHs_treeNode<E>
{
   protected int height;

   public AvlNode( 
         E x, 
         FHs_treeNode<E> lftChild, 
         FHs_treeNode<E> rtChild, 
         int ht )
   {
      super(x, lftChild, rtChild);
      if (!setHeight(ht)) 
         height = 0;
   }

   public AvlNode()
   {
      this(null, null, null, 0);
   }

   public int getHeight() { return height; }
   public boolean setHeight(int height)
   {
      if (height < -1) 
         return false;
      this.height = height;
      return true;
   }
};

public class FHavlTree<E extends Comparable< ? super E > >
extends FHsearch_tree<E>
{
   // public methods of AVL Tree --------------------------------
   // constructor
   public FHavlTree() { super(); }

   // a fun and informative touch
   public int showHeight() { return heightOf(mRoot); }

   // note that public insert(), remove() and clone() do not need to be
   // overridden because they would be identical method bodies
   // the private method calls from those base class methodswill call the AVL 
   // versions supplied here

   // private helper methods of AVL tree ----------------------------

   // overrides base class by incorporating height information
   protected AvlNode<E> cloneSubtree(FHs_treeNode<E> root)
   {
      AvlNode<E> newNode;
      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new AvlNode<E>
      (
            root.data, 
            cloneSubtree(root.lftChild), 
            cloneSubtree(root.rtChild),
            root.getHeight()
      );
      return newNode;
   }

   // we'll make it static and remove the parameter, E
   protected static int heightOf(FHs_treeNode t)
      { return t == null? -1 : t.getHeight(); }


   protected FHs_treeNode<E> rotateWithLeftChild( 
      FHs_treeNode<E> k2 )
   {
      FHs_treeNode<E> k1 = k2.lftChild;
      k2.lftChild = k1.rtChild;
      k1.rtChild = k2;
      k2.setHeight( Math.max( heightOf(k2.lftChild),  heightOf(k2.rtChild) ) + 1 );
      k1.setHeight( Math.max( heightOf(k1.lftChild),  k2.getHeight() ) + 1 );
      return k1;
   }

   protected FHs_treeNode<E> doubleWithLeftChild( 
      FHs_treeNode<E> k3 )
   {
      k3.lftChild = rotateWithRightChild(k3.lftChild);
      return rotateWithLeftChild(k3);
   }

   protected FHs_treeNode<E> rotateWithRightChild( 
      FHs_treeNode<E> k2 )
   {
      FHs_treeNode<E> k1 = k2.rtChild;
      k2.rtChild = k1.lftChild;
      k1.lftChild = k2;
      k2.setHeight( Math.max( heightOf(k2.lftChild),  heightOf(k2.rtChild) ) + 1 );
      k1.setHeight( Math.max( heightOf(k1.rtChild),  k2.getHeight() ) + 1 );
      return k1;
   }

   protected FHs_treeNode<E> doubleWithRightChild( 
      FHs_treeNode<E> k3 )
   {
      k3.rtChild = rotateWithLeftChild(k3.rtChild);
      return rotateWithRightChild(k3);
   }

   protected FHs_treeNode<E> insert( FHs_treeNode<E> root, E x )
   {
      int compareResult;

      if (root == null)
      {
         mSize++;
         return new AvlNode<E>(x, null, null, 0);
      }

      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
      {
         root.lftChild = insert(root.lftChild, x);
         if(heightOf(root.lftChild) - heightOf(root.rtChild) == 2)
            if( x.compareTo(root.lftChild.data) < 0 )
               root = rotateWithLeftChild(root);
            else
               root = doubleWithLeftChild(root); 
      }
      else if ( compareResult > 0 )
      {
         root.rtChild = insert(root.rtChild, x);
         if(heightOf(root.rtChild) - heightOf(root.lftChild) == 2)
            if( x.compareTo(root.rtChild.data) > 0 )
               root = rotateWithRightChild(root);
            else
               root = doubleWithRightChild(root);
      }
      else
         return root; // duplicate

      // successfully inserted at this or lower level; adjust height
      root.setHeight( 
            Math.max( heightOf(root.lftChild), heightOf(root.rtChild))
            + 1);
      return root;
   }

   protected FHs_treeNode<E> remove( FHs_treeNode<E> root, E x  )
   {
      int compareResult;

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
      {
         root.lftChild = remove(root.lftChild, x);

         // rebalance - shortened left branch so right may now be higher by 2
         if(heightOf(root.rtChild) - heightOf(root.lftChild) == 2)
            if(heightOf(root.rtChild.rtChild) < heightOf(root.rtChild.lftChild))
               root = doubleWithRightChild(root);
            else
               root = rotateWithRightChild(root);
      }
      else if ( compareResult > 0 )
      {
         root.rtChild = remove(root.rtChild, x);

         // rebalance - shortened right branch so left may now be higher by 2
         if(heightOf(root.lftChild) - heightOf(root.rtChild) == 2)
            if(heightOf(root.lftChild.lftChild) < heightOf(root.lftChild.rtChild))
               root = doubleWithLeftChild(root);
            else
               root = rotateWithLeftChild(root);
      }

      // found the node
      else if (root.lftChild != null && root.rtChild != null)
      {
         root.data = findMin(root.rtChild).data;
         root.rtChild = remove(root.rtChild, root.data);

         // rebalance - shortened right branch so left may now be higher by 2
         if(heightOf(root.lftChild) - heightOf(root.rtChild) == 2)
            if(heightOf(root.lftChild.lftChild) < heightOf(root.lftChild.rtChild))
               root = doubleWithLeftChild(root);
            else
               root = rotateWithLeftChild(root);
      }
      else
      {
         // no rebalancing needed at this external (1 + null children) node
         root =
            (root.lftChild != null)? root.lftChild : root.rtChild;
         mSize--;
         return root;
      }
      
      // successfully removed and rebalanced at this and lower levels;
      // now adjust height
      root.setHeight( 
            Math.max( heightOf(root.lftChild), heightOf(root.rtChild))
            + 1);
      return root;
   }
}
