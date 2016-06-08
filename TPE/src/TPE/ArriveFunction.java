package TPE;

import java.util.HashMap;
import java.util.Map;

import TPE.AirportManager.Node;

public class ArriveFunction {
	private Node source;
	private Node dst;
	public ArriveFunction(Node source, Node dst) {
		super();
		this.source = source;
		this.dst = dst;
	}
	private Map<Integer,Integer> departToArrival = new HashMap<Integer,Integer>();
	public Map<Integer, Integer> getDepartToArrival() {
		return departToArrival;
	}
	public Node getDst() {
		return dst;
	}
	
	public String toString(){
		return dst.toString()+" "+departToArrival;
	}
	
	public ArriveFunction clone(){
		ArriveFunction ans = new ArriveFunction(source, dst);
		for(Integer key : departToArrival.keySet()){
			ans.departToArrival.put(key, departToArrival.get(key));
		}
		return ans;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArriveFunction other = (ArriveFunction) obj;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		return true;
	}
	
	
	
	
}
