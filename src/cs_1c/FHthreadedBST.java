package cs_1c;
import java.util.*;

class FHthreadedNode<E extends Comparable< ? super E > >
{
   // use protected access so the tree, in the same package, 
   // or derived classes can access members 
   protected FHthreadedNode<E> lftChild, rtChild;
   protected E data;
   protected boolean lftThread, rtThread;

   public FHthreadedNode( E d, FHthreadedNode<E> lt, FHthreadedNode<E> rt,
         boolean lftThr, boolean rtThr, int ht)
   {
      lftChild = lt; 
      rtChild = rt;
      data = d;
      lftThread = lftThr;
      rtThread = rtThr;
   }
   
   public FHthreadedNode()
   {
      this(null, null, null, true, true, 0);
   }
}

public class FHthreadedBST<E extends Comparable< ? super E > >
   implements Cloneable
{
   protected int mSize;
   protected FHthreadedNode<E> mRoot;
   
   public FHthreadedBST() { clear(); }
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
      FHthreadedNode<E> resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }
   
   public boolean contains(E x)  { return find(mRoot, x) != null; }
   
   public boolean insert( E x )
   {
      int compareResult;
      
      if (mRoot == null)
      {
         mRoot = new FHthreadedNode<E>(x, null, null, true, true, 0);
         mSize++;
         return true;
      }
      
      FHthreadedNode<E> newNode, parent;
      parent = mRoot;
      
      while(true) 
      {
         compareResult = x.compareTo(parent.data); 
         if ( compareResult < 0 )
         {
            if( !(parent.lftThread) )
               parent = parent.lftChild;
            else
            {
               // place as new left child
               newNode = new FHthreadedNode<E>
               (x, parent.lftChild, parent, true, true, 0);
               parent.lftChild = newNode;
               parent.lftThread = false;
               break;
            }
         }
         else if ( compareResult > 0 )
         {
            if( !(parent.rtThread) )
               parent = parent.rtChild;
            else
            {
               // place as new right child
               newNode = new FHthreadedNode<E>
               (x, parent, parent.rtChild, true, true, 0);
               parent.rtChild = newNode;
               parent.rtThread = false;
               break;
            }
         }
         else
            return false;  // duplicate
      }

      mSize++;
      return true;
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
      if (mRoot == null)
         return;

      FHthreadedNode<E> node = findMin(mRoot);
      do
      {
         func.visit(node.data);
         node = successor(node);
      } while (node != null);
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHthreadedBST<E> newObject = (FHthreadedBST<E>)super.clone();
      newObject.clear();  // can't point to other's data

      // builds a new subtree through insertion
      cloneSubtree(mRoot, newObject);

      return newObject;
   }
   
   // private helper methods ----------------------------------------
   protected FHthreadedNode<E> findMin( FHthreadedNode<E> root ) 
   {
      if (root == null)
         return null;
      while ( !(root.lftThread) )
         root = root.lftChild;
      return root;
   }
   
   protected FHthreadedNode<E> findMax( FHthreadedNode<E> root ) 
   {
      if (root == null)
         return null;
      while ( !(root.rtThread) )
         root = root.rtChild;
      return root;
   }

   // very hard to remove recursion, so only adjust pred/succ links
   protected FHthreadedNode<E> remove( FHthreadedNode<E> root, E x  )
   {
      int compareResult;  // avoid multiple calls to compareTo()
      FHthreadedNode<E> tempRoot;
     
      if (root == null)
         return null;

      compareResult = x.compareTo(root.data);
      if ( compareResult < 0 )
      {
         if (!root.lftThread)
            root.lftChild = remove(root.lftChild, x);
      }
      else if ( compareResult > 0 )
      {
         if (!root.rtThread)
            root.rtChild = remove(root.rtChild, x);
      }

      // found the node
      else if (!(root.lftThread) && !(root.rtThread))
      {
         // two real children
         root.data = findMin(root.rtChild).data;
         root.rtChild = remove(root.rtChild, root.data);
      }
      else
      {
         // one or two "fake" children => at least one thread
         redirectThreadsPointingToMe(root);

         // if a full leaf, we have to modify one of parent's thread flags
         if (root.lftThread && root.rtThread)
         {
            tempRoot = adjustParentThreadFlagsAndUnlink(root);
            
            // in case this was final node in tree
            if (root.lftChild == null && root.rtChild == null)
               mRoot = null;
            
            root = tempRoot;
         }
         else
            // at least one real child, so we copy to parent
            root = ( !(root.lftThread) )? root.lftChild : root.rtChild;
         
         mSize--;
      }
      return root;
   }
   
   void redirectThreadsPointingToMe( FHthreadedNode<E> nodeToRemove )
   {
      FHthreadedNode<E>  minNode, maxNode, node;

      // adjust nodes in root's subtree that "thread directly up" to root
      minNode = findMin(nodeToRemove);
      maxNode = findMax(nodeToRemove);
      
      for (node = minNode; node != null; node = successor(node))
         if (node.lftThread && node.lftChild == nodeToRemove)
         {
            node.lftChild = predecessor(nodeToRemove);
            break;  // last of only two possible threads pointing up
         }
         else if (node.rtThread && node.rtChild == nodeToRemove)
         {
            node.rtChild = successor(nodeToRemove);
         }
   }

   // called when both flags are true, meaning one MUST be parent. find out
   // which one, so we can set parent's left of right thread flag to true
   protected FHthreadedNode<E> adjustParentThreadFlagsAndUnlink( 
         FHthreadedNode<E> nodeToRemove )
   {
      FHthreadedNode<E> node;

      node = nodeToRemove.rtChild;  // successor is parent?
      if ( node != null )
      {
         if ( node.lftChild == nodeToRemove )
         {
            node.lftThread = true;
            return nodeToRemove.lftChild;
         }
      }

      // test both in case mRoot is leaf
      node = nodeToRemove.lftChild;  // predecessor is parent?
      if ( node != null )
      {
         if ( node.rtChild == nodeToRemove )
         {
            node.rtThread = true;
            return nodeToRemove.rtChild;
         }
      }
      
      return null;  // shouldn't happen
   }
   
   protected FHthreadedNode<E> find( FHthreadedNode<E> root, E x )
   {
      int compareResult;

      if (root == null)
         return null;

      while (true)
      {
         if (root == null)
            return null;
         
         compareResult = x.compareTo(root.data); 
         if (compareResult < 0)
         {
            if(root.lftThread)
               return null;
            root = root.lftChild;
         }
         else if (compareResult < 0)
         {
            if(root.rtThread)
               return null;
            root = root.rtChild;
         }
         else 
            break;
      }
      return root;   // found
   }
   
   protected void cloneSubtree(FHthreadedNode<E> root,
         FHthreadedBST<E> newTree)
   {
      // to overcome complex threading, simply add node into a new tree
      // and let the insert() algorithm naturally set the threads.
      // nodes will go into new tree root in equivalent order as old

      newTree.insert(root.data);
      if ( !(root.lftThread) )
         cloneSubtree(root.lftChild, newTree);
      if ( !(root.rtThread) )
         cloneSubtree(root.rtChild, newTree);
   }
   
   protected int findHeight( FHthreadedNode<E> treeNode, int height ) 
   {
      int leftHeight, rightHeight;
      if (treeNode == null)
         return height;
      height++;
      leftHeight  = treeNode.lftThread ? height 
            : findHeight(treeNode.lftChild, height);
         rightHeight = treeNode.rtThread ? height 
            : findHeight(treeNode.rtChild, height);
      return (leftHeight > rightHeight)? leftHeight : rightHeight;
   }
   
   static protected FHthreadedNode successor( FHthreadedNode node ) 
   {
      if (node == null)
         return null;
      
      if (node.rtThread)
         return node.rtChild;
      
      node = node.rtChild;
      while ( !(node.lftThread) )
         node = node.lftChild;
      return node;
   }
   
   static protected FHthreadedNode predecessor( FHthreadedNode node ) 
   {
      if (node == null)
         return null;
      
      if (node.lftThread)
         return node.lftChild;
      
      node = node.lftChild;
      while ( !(node.rtThread) )
         node = node.rtChild;
      return node;
   }
}

