package TPE;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;



/** Implementacion particular de un AVL de vuelos, en la que se ordena por tiempo de llegada, 
 * pero cada nodo tiene referencia al maximo y al minimo tiempo de salida de sus hijos, 
 * para facilitar la eleccion del vuelo que menor tiempo tarda pero que puedo tomar**/
public class TimeAVL implements Iterable<Flight>{
	private int size = 0;
	private final int dayMins = 60*24;/** minutos en un dia**/
	private final int weekMins = 7*60*24;/**minutos en una semana**/
	private Node root;
	private Comparator<Flight> cmp;
	
	 private static class Node {
		  private Flight elem;
		  private Node left;
		  private Node right;
		  private Node parent;
		  private Integer maxDepTime = Integer.MIN_VALUE;
		  private Integer minDepTime = Integer.MAX_VALUE;
		  private int height;
	    
	  
	    public Node (Flight elem){
	      this.elem = elem;
	    } 
	    
	    public String toString(){
	    	return this == null ? null : this.elem.toString();
	    }
	  }

	 

	  
	  public TimeAVL(Comparator<Flight> cmp) {
		  this.cmp = cmp;
	
	  }
	  public int height (Node t){
	    return t == null ? -1 : t.height;
	  }
	  
	 
	  public int max (int a, int b){
	    return a > b ? a : b;
	  }
	  

	  public boolean insert(Flight elem){
		  Node insert = new Node(elem);
	      root = insert(insert, root);
	      size += elem.getDays().size();
	      return true;
	  }
	  
	 

	private Node insert (Node insert, Node current) {
		    if (current == null)
		    	return insert;	
		    else if (cmp.compare(insert.elem,current.elem) <= 0){
		    	Integer lastMax = null;
		    	if(current.left != null){
		    		 lastMax = current.left.maxDepTime;
		    	}
		    	current.left = insert(insert, current.left);
		    	current.left.parent = current;
		    	   if(lastMax != null && lastMax != current.left.maxDepTime){
		    		   
		    		   if(current.left.minDepTime < current.minDepTime)
				    		  current.minDepTime = current.left.minDepTime;
		    		   
		    		   
				    	  if(current.left.maxDepTime > current.maxDepTime)
				    		  current.maxDepTime = current.left.maxDepTime;
				      }
				      
		    	   if(lastMax == null){			    	
			    		current.maxDepTime = (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24) > current.maxDepTime ?  (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24)  : current.maxDepTime;			    	
			    		current.minDepTime = (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24) < current.minDepTime ?  (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24)  : current.minDepTime;		
		    	   }	
		    	
		      
		     if (height(current.left) - height(current.right) > 1){
		        if (cmp.compare(insert.elem,current.left.elem) <= 0){
		          current = rotateWithLeftChild(current);
		        }
		        else {
		          current = doubleWithLeftChild(current);
		        }
		      }
		    }
		    else if (cmp.compare(insert.elem,current.elem) > 0){
		    	Integer lastMax = null;
		    	if(current.right != null){
		    		 lastMax = current.right.maxDepTime;
		    	}
			      current.right = insert(insert ,current.right); 
			      current.right.parent = current;
			      if(lastMax != null && lastMax != current.right.maxDepTime){
			    	  if(current.right.maxDepTime > current.maxDepTime)
			    		  current.maxDepTime = current.right.maxDepTime;
			    	  if(current.right.minDepTime < current.minDepTime)
			    		  current.minDepTime = current.right.minDepTime;
			      }
			 
			      if(lastMax == null){					    			
			    	  current.maxDepTime = (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*dayMins)%weekMins > current.maxDepTime ?  (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*dayMins)%weekMins  : current.maxDepTime;
			    	  current.minDepTime = (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*dayMins)%weekMins < current.minDepTime ?  (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*dayMins)%weekMins  : current.minDepTime;			
			    	}			     
			      if ( height(current.right) - height(current.left) > 1)
			        if (cmp.compare(insert.elem,current.right.elem) > 0){
			          current = rotateWithRightChild(current);		       
			        }
			        else{
			          current = doubleWithRightChild(current);
			        }
		    }       
		    current.height = max (height(current.left), height(current.right)) + 1;
		    return current;
	  }
	  

	  private Node rotateWithLeftChild (Node n){
	    Node aux = n.left;
	    aux.parent = n.parent;
	    n.parent = aux;
	    if(aux.right != null)
	    	aux.right.parent = n;
	    n.left = aux.right;
	    aux.right = n;
	    n.height = max (height (n.left), height(n.right)) + 1;
	    aux.height = max(height (aux.left), height(aux.right)) + 1;  
	    updateMaxAndMinDep(n);
	    updateMaxAndMinDep(aux);
	    return aux;
	  }
	  

	  private Node doubleWithLeftChild (Node n){
	    n.left = rotateWithRightChild(n.left);
	    return rotateWithLeftChild (n);
	  }
	  
	  private Node rotateWithRightChild (Node n){
	    Node aux = n.right;
	    aux.parent = n.parent;
	    n.parent = aux;
	    n.right = aux.left;
	    if(aux.left != null)
	    	aux.left.parent = n;
	    aux.left = n;   
	    n.height = max (height (n.right), height(n.left)) + 1; 
	    aux.height = max (height(aux.left), height(aux.right)) + 1;
	    updateMaxAndMinDep(n);
	    updateMaxAndMinDep(aux);
	    return aux;
	  }

	 
	  private Node doubleWithRightChild (Node n){
	    n.right = rotateWithLeftChild (n.right);
	    return rotateWithRightChild (n);
	  }

	  public boolean isEmpty(){
	    return root == null;
	  }

	  private class Box{
		  private Node node;
		  private Flight max;
		
		  public Box(Node node, Flight max) {
			super();
			this.node = node;
			this.max = max;
		}
		  
		  
	  }


	 /**
	  * Metodo usado para el remove del avl,busca el maximo,remueve el nodo que lo contiene y devuelve su valor y el nodo de inicio.
	  * @param node
	  * @return
	  */
	  private Box findMaxAndRemoveNode(Node node) {
	        Node current = node;
	        if(current == null)
	        	return null;
	        while(current.right!=null){
	        	current = current.right;
	        }
	        Flight max = current.elem;
	        if(current.left != null)
	        	current.left.parent = current.parent;
	        if(current != node){
	        	current.parent.right = current.left;
	        	if(current.parent.right != null)
	        		current.parent.maxDepTime = current.parent.right.maxDepTime;
	        	current = current.parent;
	        	while(current != node){
	        		current.height = Math.max(height(current.left), height(current.right))+1;
	        		updateMaxAndMinDep(current);
	        		current = current.parent;
	        	}
	        }
	        else{
	        	node = current.left;
	        }
	       
	        return new Box(node,max);    
	       }



	/**
	 * Actualiza los tiempos maximos y minimos de salidas de los hijos del nodo current.
	 * @param current
	 */
	  private void updateMaxAndMinDep(Node current) {
		  current.maxDepTime = Integer.MIN_VALUE;
		  current.minDepTime = Integer.MAX_VALUE;
		if(current.left != null ){
			current.maxDepTime = (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24);
			current.maxDepTime = current.left.maxDepTime > current.maxDepTime ? current.left.maxDepTime : current.maxDepTime;
			
			current.minDepTime = (current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*(60*24))%(7*60*24);
			current.minDepTime = current.left.minDepTime < current.minDepTime ? current.left.minDepTime : current.minDepTime;
		}if(current.right != null){
			current.maxDepTime = (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*(60*24))%(7*60*24)> current.maxDepTime ? (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*(60*24))%(7*60*24): current.maxDepTime;
			current.maxDepTime = current.right.maxDepTime > current.maxDepTime ? current.right.maxDepTime : current.maxDepTime;
			
			current.maxDepTime = (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*(60*24))%(7*60*24)< current.minDepTime ? (current.right.elem.getDepartureTime()+current.right.elem.getCurrentDayIndex()*(60*24))%(7*60*24): current.minDepTime;
			current.maxDepTime = current.right.minDepTime < current.minDepTime ? current.right.minDepTime : current.minDepTime;
		}	
	}
	public void remove( Flight x ) {
	      root = remove(x, root);
	      size-= x.getDays().size();
	  }

	  private Node remove(Flight elem, Node current) {
		  if(current == null){
				return null;
			}
			int c = cmp.compare(elem,current.elem);
			if(c > 0){
				current.right = remove(elem,current.right);
				updateMaxAndMinDep(current);			
				if (height(current.left) - height(current.right) > 1){
			        if (height(current.left.left) > height(current.left.right)){
			          current = rotateWithLeftChild(current);
			        }
			        else {
			          current = doubleWithLeftChild(current);
			        }
			      }																												
				current.height = max (height(current.left), height(current.right)) + 1;
				return current;
			}else if(c < 0){
				current.left = remove(elem,current.left);
				updateMaxAndMinDep(current);			
				 if ( height(current.right) - height(current.left) > 1){
				        if (height(current.right.right) > height(current.right.left)){
				          current = rotateWithRightChild(current);		       
				        }
				        else{
				          current = doubleWithRightChild(current);
				        }
				 }
				 current.height = max (height(current.left), height(current.right)) + 1;			
				return current;
			}
			if(!current.elem.equals(elem)){
				current.left = remove(elem,current.left);
				return current;
			}
			if(current.right == null && current.left == null)
				return null;
			if(current.right != null && current.left == null){
				current.right.parent = current.parent;
				return current.right;
			}
			if(current.left != null && current.right == null){
				current.left.parent = current.parent;
				return current.left;
			}
			Box b = findMaxAndRemoveNode(current.left);
			current.left = b.node;
			current.elem = b.max;
			if (height(current.right) - height(current.left) > 1){
		        current = rotateWithRightChild(current);
		      }
		 current.height = max (height(current.left), height(current.right)) + 1;	
			return current;
	  } 
	  
	  
	 
	  public int size(){
		  return size;
	  }

	  public boolean contains(Flight elem){
	    return contains(elem, root); 
	  }


	  private boolean contains(Flight elem, Node current) {
	    if (current == null)
	    	return false; 
	    
	    else if (cmp.compare(elem,current.elem) < 0)
	    	 return contains(elem, current.left);
	    
	    else if (cmp.compare(elem,current.elem) > 0)
	    	 return contains(elem, current.right);
	    
	    return true; 
	  }
		
	
		
	
		
		

		@Override
		public Iterator<Flight> iterator(){
			return new PostOrderIterator(this.root);
		}
			private static class PostOrderIterator implements Iterator<Flight>{

				private Deque<Flight> a = new LinkedList<Flight>();
				
				public PostOrderIterator(Node tree){
					getPostOrderDeq(a,tree);
				}
				private void getPostOrderDeq(Deque<Flight> a, Node tree) {
					if(tree == null)
						return;
					getPostOrderDeq(a,tree.left);
					getPostOrderDeq(a, tree.right);
					a.offer(tree.elem);
				}
				@Override
				public boolean hasNext() {
					return !a.isEmpty();
				}

				@Override
				public Flight next() {
					if(!hasNext())
						return null;
					return a.poll();
				}
				
			}
			protected static class FlightEl{
				private int arrivalTime;
				private Flight f;
				public int getArrivalTime() {
					return arrivalTime;
				}
				public void setArrivalTime(int arrivalTime) {
					this.arrivalTime = arrivalTime;
				}
				public Flight getF() {
					return f;
				}
				public void setF(Flight f) {
					this.f = f;
				}
				public FlightEl(int arrivalTime, Flight f) {
					super();
					this.arrivalTime = arrivalTime;
					this.f = f;
				}
				
			}
		/**
		 * Busca el vuelo que llega antes a partir de departureTime
		 * @param departureTime
		 * @return
		 */
		public FlightEl earliestArrivalTime(int departureTime){
			if(departureTime == Integer.MAX_VALUE){
				return new FlightEl(-1,null);
			}
			int weekOffset = departureTime / weekMins;
			int newWeekOffset = weekOffset;
			int auxDep = departureTime%weekMins;
			Flight f = earliestArrivalTime(auxDep,root);
			while(f == null ){
				auxDep=0;
				newWeekOffset++;
				if(newWeekOffset-weekOffset == 2){
					return new FlightEl(-1,null);
				}
				f= earliestArrivalTime(auxDep, root);
			}
			int arrivalTime = f.getDepartureTime()+f.getCurrentDayIndex()*dayMins+f.getFlightTime()+newWeekOffset*weekMins;
			return new FlightEl(arrivalTime, f);
		}
		
		private Flight earliestArrivalTime(int departureTime, Node current) {
			if(current == null)
				return null;
			
			if(current.left != null){
				if(current.left.maxDepTime >= departureTime || current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*60*24 >= departureTime) {
					return earliestArrivalTime(departureTime,current.left);
				}
			}
				if(current.elem.getDepartureTime()+current.elem.getCurrentDayIndex()*dayMins >= departureTime){
					return current.elem;
					
				}
				
			return earliestArrivalTime(departureTime, current.right);
			
		}
		/**
		 * Busca el vuelo que llega antes a partir de departureTime pero que tiene un limite m�ximo de tiempo de partida
		 * @param departureTime
		 * @param maxDepTime
		 * @return
		 */
		public FlightEl earliestArrivalTime(int departureTime,int maxDepTime){
			int weekOffset = departureTime / weekMins;
			int newWeekOffset = weekOffset;
			int auxDep = departureTime%weekMins;
			Flight f = earliestArrivalTime(auxDep,root,maxDepTime);
			while(f == null){
				auxDep=0;
				newWeekOffset++;
				f= earliestArrivalTime(auxDep, root,maxDepTime);
			}
			int arrivalTime = f.getDepartureTime()+f.getCurrentDayIndex()*dayMins+f.getFlightTime()+newWeekOffset*weekMins;
			return new FlightEl(arrivalTime, f);
		}
		
		private Flight earliestArrivalTime(int departureTime, Node current,int max) {
			if(current == null )
				return null;
			
			if(current.left != null){
				if((current.left.maxDepTime >= departureTime && current.left.minDepTime <= max)|| current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*60*24 >= departureTime && current.left.elem.getDepartureTime()+current.left.elem.getCurrentDayIndex()*60*24 <=max) {
					return earliestArrivalTime(departureTime,current.left);
				}
			}
				if(current.elem.getDepartureTime()+current.elem.getCurrentDayIndex()*60*24 >= departureTime && current.elem.getDepartureTime()+current.elem.getCurrentDayIndex()*60*24 <=max){
					return current.elem;
					
				}
				
			return earliestArrivalTime(departureTime, current.right);
			
		}
	
	
}
