package cs_1c;
import java.util.*;

public class FHsearch_tree<E extends Comparable< ? super E > >
   implements Cloneable
{
   protected int mSize;
   protected FHs_treeNode<E> mRoot;
   
   public FHsearch_tree() { clear(); }
   public boolean empty() { return (mSize == 0); }
   public int size() { return mSize; }
   public void clear() { mSize = 0; mRoot = null; }
   public int showHeight() { return findHeight(mRoot, -1); }
   
   public E findMin() 
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMin(mRoot).data;
   }
   
   public E findMax() 
   {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMax(mRoot).data;
   }
   
   public E find( E x )
   {
      FHs_treeNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }
   public boolean contains(E x)  { return find(mRoot, x) != null; }
   
   public boolean insert( E x )
   {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }
   
   public boolean remove( E x )
   {
      int oldSize = mSize;
      mRoot = remove(mRoot, x);
      return (mSize != oldSize);
   }
   
   public < F extends Traverser<? super E > > 
   void traverse(F func)
   {
      traverse(func, mRoot);
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHsearch_tree<E> newObject = (FHsearch_tree<E>)super.clone();
      newObject.clear();  // can't point to other's data

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      
      return newObject;
   }
   
   // private helper methods ----------------------------------------
   protected FHs_treeNode<E> findMin( FHs_treeNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;
      return findMin(root.lftChild);
   }
   
   protected FHs_treeNode<E> findMax( FHs_treeNode<E> root ) 
   {
      if (root == null)
         return null;
      if (root.rtChild == null)
         return root;
      return findMax(root.rtChild);
   }
   
   protected FHs_treeNode<E> insert( FHs_treeNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()
      
      if (root == null)
      {
         mSize++;
         return new FHs_treeNode<E>(x, null, null);
      }
      
      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         root.lftChild = insert(root.lftChild, x);
      else if ( compareResult > 0 )
         root.rtChild = insert(root.rtChild, x);

      return root;
   }

   protected FHs_treeNode<E> remove( FHs_treeNode<E> root, E x  )
   {
      int compareResult;  // avoid multiple calls to compareTo()
     
      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if ( compareResult < 0 )
         root.lftChild = remove(root.lftChild, x);
      else if ( compareResult > 0 )
         root.rtChild = remove(root.rtChild, x);

      // found the node
      else if (root.lftChild != null && root.rtChild != null)
      {
         root.data = findMin(root.rtChild).data;
         root.rtChild = remove(root.rtChild, root.data);
      }
      else
      {
         root =
            (root.lftChild != null)? root.lftChild : root.rtChild;
         mSize--;
      }
      return root;
   }
   
   protected <F extends Traverser<? super E>> 
   void traverse(F func, FHs_treeNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      traverse(func, treeNode.lftChild);
      func.visit(treeNode.data);
      traverse(func, treeNode.rtChild);
   }
   
   protected FHs_treeNode<E> find( FHs_treeNode<E> root, E x )
   {
      int compareResult;  // avoid multiple calls to compareTo()

      if (root == null)
         return null;

      compareResult = x.compareTo(root.data); 
      if (compareResult < 0)
         return find(root.lftChild, x);
      if (compareResult > 0)
         return find(root.rtChild, x);
      return root;   // found
   }
   
   protected FHs_treeNode<E> cloneSubtree(FHs_treeNode<E> root)
   {
      FHs_treeNode<E> newNode;
      if (root == null)
         return null;

      // does not set myRoot which must be done by caller
      newNode = new FHs_treeNode<E>
      (
         root.data, 
         cloneSubtree(root.lftChild), 
         cloneSubtree(root.rtChild)
      );
      return newNode;
   }
   
   protected int findHeight( FHs_treeNode<E> treeNode, int height ) 
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight = findHeight(treeNode.lftChild, height);
      rightHeight = findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight)? leftHeight : rightHeight;
   }
}
