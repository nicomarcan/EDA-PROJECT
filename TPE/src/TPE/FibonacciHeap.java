package TPE;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FibonacciHeap<T> {
	
	private static class Node<T> {
		Node<T> left;
		Node<T> right;
		Node<T> parent;
		Node<T> child;
		T value;
		boolean marked;
		int degree;
		
		public Node(T v) {
			value = v;
		}
	}
	
	private static final double LOG_PHI = Math.log( ( 1 + Math.sqrt( 5 ) ) / 2 );
	private Node<T> minNode;
	private int size;
	private Comparator<T> cmp;
	
	public FibonacciHeap(Comparator<T> cmp) {
		this.cmp = cmp;
	}
	
	public boolean isEmpty() {
		return minNode == null;
	}
	
	public void clear() {
		minNode = null;
		size = 0;

	}
	
	private void moveToRoot(Node<T> node) {
		if(isEmpty()) {
			minNode = node;
			node.left = node;
			node.right = node;
		} else {
			node.right = minNode;
			node.left = minNode.left;
			minNode.left = node;
			minNode.left.right = node;
			if(cmp.compare(minNode.value, node.value) > 0) {
				minNode = node;
			}
		}
	}
	
	public void offer(T elem) {
		if(elem == null) {
			return;
		}
		
		Node<T> node = new Node<T>(elem);
		moveToRoot(node);
		size ++;
	}
	
	public T peek() {
		return minNode.value;
	}
		
	public T poll() {
		 if(isEmpty()) {
			 return null;
		 }

		 Node<T> minN = minNode;
		 int numOfKids = minN.degree;

		 Node<T> child = minN.child;
		 Node<T> tempRight;

		 while(numOfKids > 0) {
			 tempRight = child.right;
			 moveToRoot(child);

			 child.parent = null;

			 child = tempRight;
			 numOfKids--;
		 }

		 minN.left.right = minN.right;
		 minN.right.left = minN.left;

		 if( minN == minN.right ) {
			 minNode = null;
		 } else {
			 minNode = minN.right;
			 consolidate();
		 }
		 
		 size--;
		 T min = minN.value;
		 return min;
	 }

	private void consolidate() {
        if(isEmpty()) {
            return;
        }

        int arraySize = ((int)Math.floor(Math.log(size)/LOG_PHI));

        List<Node<T>> nodeSequence = new ArrayList<Node<T>>(arraySize);
        for(int i = 0; i < arraySize; i++) {
            nodeSequence.add(i, null);
        }

        Node<T> current = minNode.right;
        boolean first = true;
		
        while(current != minNode || first) {
        	
        	int degree = current.degree;
            Node<T> next = current.right;
            first = false;
            
            while(nodeSequence.get(degree) != null) {
                Node<T> aux = nodeSequence.get(degree);

                if(cmp.compare(current.value, aux.value) > 0) {
                    Node<T> pointer = aux;
                    aux = current;
                    current = pointer;
                }
                
                link(aux, current);
                nodeSequence.set(degree, null);
                degree++;
            }

            nodeSequence.set(degree, current);
            current = next;
        }
        
        minNode = null;

        for(Node<T> node : nodeSequence) {
            if(node != null) {
            	moveToRoot(node);
            }
        }
	}
	    
    private void link(Node<T> c, Node<T> p) {
    	c.left.right = c.right;
    	c.right.left = c.left;

        c.parent = p;

        if(p.child == null) {
            p.child = c;
            c.right = c;
            c.left = c;
        } else {
            c.left = p.child;
            c.right = p.child.right;
            p.child.right = c;
            c.right.left = c;
        }

        p.degree ++;

        c.marked = false;
    }
    
    public void decreaseKey(Node<T> node, T val) {
        if(cmp.compare(val, node.value) > 0) {
        	throw new IllegalArgumentException("entered value is wrong");
        }
        node.value = val;

        Node<T> tempParent = node.parent;

        if((tempParent != null) && (cmp.compare(node.value, tempParent.value) < 0)) {
            cut(node,tempParent);
            cascadingCut(tempParent);
        }
    }

	private void cut(Node<T> node, Node<T> tempParent) {
		moveToRoot(node);

        tempParent.degree--;
        node.parent = null;

        node.marked = false;;
	}

	private void cascadingCut(Node<T> node) {
        Node<T> tempParent = node.parent;

        if(tempParent != null) {
            if(!node.marked) {
                node.marked = true;
            } else {
                cut(node, tempParent);
                cascadingCut(tempParent);
            }
        }
		
	}
	
	public void delete(Node<T> node) {
		decreaseKey(node, null);
		poll();
	}

}
