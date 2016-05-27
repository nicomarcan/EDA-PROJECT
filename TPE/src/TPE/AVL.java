package TPE;

import java.util.Comparator;
import java.util.Iterator;

public class AVL<T> implements Iterable<T> {
 
  private static class Node<T> {
	  private T elem;
	  private Node<T> left;
	  private Node<T> right;
	  private int height;
    
  
    public Node (T elem){
      this.elem = elem;
    } 
  }

  public Node<T> root;
  private Comparator<T> cmp;
  public int countInsertions;
  public int countSingleRotations;
  public int countDoubleRotations;
  
  public AVL(Comparator<T> cmp) {
	  this.cmp = cmp;
  }
  public int height (Node<T> t){
    return t == null ? -1 : t.height;
  }
  
 
  public int max (int a, int b){
    return a > b ? a : b;
  }
  

  public boolean insert(T elem){
      root = insert(elem, root);
      countInsertions++;
      return true;
  }
  
 
  private Node<T> insert (T elem, Node<T> current) {
	    if (current == null)
	    	current = new Node<T> (elem);
	    else if (cmp.compare(elem,current.elem) <= 0){
	    	current.left = insert(elem, current.left);
	      
	     if (height(current.left) - height(current.right) > 1){
	        if (cmp.compare(elem,current.left.elem) < 0){
	          current = rotateWithLeftChild(current);
	          countSingleRotations++;
	        }
	        else {
	          current = doubleWithLeftChild(current);
	          countDoubleRotations++;
	        }
	      }
	    }
	    else if (cmp.compare(elem,current.elem) > 0){
		      current.right = insert(elem, current.right);   
		      if ( height(current.right) - height(current.left) > 1)
		        if (cmp.compare(elem,current.right.elem) > 0){
		          current = rotateWithRightChild(current);
		          countSingleRotations++;
		        }
		        else{
		          current = doubleWithRightChild(current);
		          countDoubleRotations++;
		        }
	    }       
	    current.height = max (height(current.left), height(current.right)) + 1;
	    return current;
  }
  

  private Node<T> rotateWithLeftChild (Node<T> n){
    Node<T> aux = n.left;
    n.left = aux.right;
    aux.right = n;
    n.height = max (height (n.left), height(n.right)) + 1;
    aux.height = max(height (aux.left), n.height) + 1;  
    return aux;
  }
  

  private Node<T> doubleWithLeftChild (Node<T> n){
    n.left = rotateWithRightChild(n.left);
    return rotateWithLeftChild (n);
  }
  
  private Node<T> rotateWithRightChild (Node<T> n){
    Node<T> aux = n.right;   
    n.right = aux.left;
    aux.left = n;   
    aux.height = max (height(aux.left), height(aux.right)) + 1;
    n.height = max (height (n.right), n.height) + 1; 
    return aux;
  }

 
  private Node<T> doubleWithRightChild (Node<T> n){
    n.right = rotateWithLeftChild (n.right);
    return rotateWithRightChild (n);
  }

  public boolean isEmpty(){
    return root == null;
  }

  private class Box<T>{
	  private Node<T> node;
	  private T max;
	
	  public Box(Node<T> node, T max) {
		super();
		this.node = node;
		this.max = max;
	}
	  
	  
  }

 
    private Box<T> findMaxAndRemoveNode(Node<T> node){
        if(isEmpty())
        	return null;
        Node<T> max = new Node<T>(null);
        node = findMaxAndRemoveNode(node,max);
        return new Box<T>(node,max.elem);
    }
 
    private Node<T> findMaxAndRemoveNode(Node<T> current,Node<T> max)    {
        if( current == null )
            return current;
       current.right = findMaxAndRemoveNode(current.right,max);
        if(current.right == null && max.elem == null){
        	max.elem = current.elem;
        	return current.left;
        }
        return current;
    }



  public void remove( T x ) {
      root = remove(x, root);
  }

  private Node<T> remove(T elem, Node<T> current) {
	  if(current == null){
			return null;
		}
		int c = cmp.compare(elem,current.elem);
		if(c > 0){
			current.right = remove(elem,current.right);
			return current;
		}else if(c < 0){
			current.left = remove(elem,current.left);
			return current;
		}
		if(current.right == null && current.left == null)
			return null;
		if(current.right != null && current.left == null)
			return current.right;
		if(current.left != null && current.right == null)
			return current.left;
		Box<T> b = findMaxAndRemoveNode(current.left);
		current.left = b.node;
		current.elem = b.max;
		return current;
  } 


  public boolean contains(T elem){
    return contains(elem, root); 
  }


  private boolean contains(T elem, Node<T> current) {
    if (current == null)
    	return false; 
    
    else if (cmp.compare(elem,current.elem) < 0)
    	 return contains(elem, current.left);
    
    else if (cmp.compare(elem,current.elem) > 0)
    	 return contains(elem, current.right);
    
    return true; 
  }
  public void print(){
		print(root);
	}
	private  void print(Node<T> current){
		if(current.left == null && current.right == null ){
			System.out.println(current.elem);
			return;
		}else if (current.left!= null && current.right != null){
			System.out.println("Padre: "+current.elem+", Hijo izquierdo: "+current.left.elem+",Hijo derecho: "+current.right.elem);
			print(current.left);
			print(current.right);
		}else if(current.left != null){
			System.out.println("Padre: "+current.elem+", Hijo izquierdo: "+current.left.elem);
			print(current.left);
		}else{
			System.out.println("Padre: "+current.elem+",Hijo derecho: "+current.right.elem);
			print(current.right);
		}
		
	}
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	public T updateMax() {
		Node<T> max = new Node<T>(null);
		Box<T> b = updateMax(root,max);
		root = b.node;
		return	b.max;
	}
	private Box<T> updateMax(Node<T> current,Node<T> lastMax) {
		 if(current == null)
	            return new Box<T>(current,null);
	     Box<T> b = updateMax(current.right,lastMax);
	     current.right = b.node;
	     if(current.right == null){
	    	 	if(lastMax.elem == null){
	    	 		lastMax.elem = current.elem;
	        		return new Box<T>(current.left,null);
	        	}
	    	 	return new Box<T>(current,current.elem);
	      }
	        return new Box<T>(current,b.max);
	}
	
	
	public static void main(String[] args) {
		AVL<Integer> tree = new AVL<Integer>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
			
		});
		tree.insert(2);
		tree.insert(3);
		tree.insert(4);
		tree.insert(5);
		System.out.println(tree.updateMax());
		System.out.println(tree.updateMax());
		
		tree.print();
		

	}
  

 
}