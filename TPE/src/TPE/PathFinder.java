package TPE;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import TPE.AirportManager.Node;

public class PathFinder {
	public static List<Flight> findPath(Collection<Node> airports,Node source,Node dest,List<Day> departDays){
		NavigableSet<Integer> interval = new TreeSet<Integer>();
		//Set<Node> posibleAirports = new HashSet<Node>();
		//List<Node> nodes = new LinkedList<Node>();
		//Flight last = null;
 		for(int i = 0;i<departDays.size();i++){
			for(Node a : airports){
				if(!a.equals(source) && source.priceFlight.get(a.airport)!=null){
					//System.out.println(departDays.get(i));
					//System.out.println(source.priceFlight.get(a.airport).get(departDays.get(i)));
					for(Flight f : source.priceFlight.get(a.airport).get(departDays.get(i))){
						Flight min =source.waitingTimes.get(a.airport).earliestArrivalTime(f.getDepartureTime()+f.getCurrentDayIndex()*60*24);
						if(min.getDepartureTime()+min.getCurrentDayIndex()*(60*24) == f.getDepartureTime()+f.getCurrentDayIndex()*(60*24)){
							
							interval.add(f.getDepartureTime()+f.getCurrentDayIndex()*60*24);
							//last = null;
						}else{
							if(min.getCurrentDayIndex() != Day.getIndex(departDays.get(i))){
								//last = f;
								interval.add(f.getDepartureTime()+f.getCurrentDayIndex()*60*24);
							}
						}
					}
//					if(last != null){
//						interval.add(last.getDepartureTime()+last.getCurrentDayIndex()*60*24);
//					}
				}
			}
		}
 		System.out.println(interval);
		 Map<Node,Entry> af = timeRefinement(airports,source,dest,interval);
		// System.out.println(af);
		 int min = Integer.MAX_VALUE;
		 for(Integer i : interval){
			Integer g = af.get(dest).af.getDepartToArrival().get(i);
			if(g != Integer.MAX_VALUE){
				if(min > g-i){
					min = g - i ;
				}
			}
		 }
		 if(min != Integer.MAX_VALUE){
			 System.out.println(min);
			// List<Flight> f = pathSelection(airports,af,source,dest,min);
	 }
		 System.out.println("NOPE");
 		return null;
	}

	private static List<Flight> pathSelection(Set<Node> airports, Map<Node, ArriveFunction> af, Node source, Node dest,
			int min) {
		Node current = dest;
		List<Flight> f = new LinkedList<Flight>();
		while(!current.equals(source)){
			for(Node n : airports){
				for(Flight fl : n.waitingTimes.get(current.airport)){
					if(af.get(n).getDepartToArrival().get(min)+n.waitingTimes.get(current.airport).earliestArrivalTime(min).getFlightTime() == af.get(source).getDepartToArrival().get(min)){
						current = n;
						f.add(0,fl);
						break;
					}
				}
				
			}
		}
		return f;
	}

	private static Map<Node,Entry> timeRefinement(Collection<Node> airports, Node source, Node dest,
			NavigableSet<Integer> interval) {
		Map<Node,Entry> res = new HashMap<Node,Entry>();
		TreeSet<Integer> subInterval = new TreeSet<Integer>();
		subInterval.add(interval.first());
	PriorityQueue<Entry> fb = new PriorityQueue<>(new Comparator<Entry>() {

		@Override
		public int compare(Entry o1, Entry o2) {
			return o1.compareTo(o2);
		}
	});
		
		for(Node airport : airports){
			if(!airport.equals(source)){
				ArriveFunction a = new ArriveFunction(source,airport);
				for(Integer i : interval){
					a.getDepartToArrival().put(i, Integer.MAX_VALUE);
				}
				res.put(airport, new Entry(subInterval.first(),a));
				fb.offer(new Entry(subInterval.first(),a));
			}else{
				ArriveFunction a = new ArriveFunction(source,source);
				for(Integer i : interval){
					a.getDepartToArrival().put(i, i);
				}
				res.put(airport, new Entry(subInterval.first(),a));
				fb.offer(new Entry(subInterval.first(),a));
			}
		}
			int pollTimes = 0;
			Entry lastPoll = null;
			while(fb.size() >= 2){
				Entry i = fb.poll();
				Entry head = fb.peek();
				if(lastPoll!= null &&i.af.getDst().airport.equals(lastPoll.af.getDst().airport)){
					pollTimes++;
				}else{
					pollTimes = 0;
					lastPoll = i;
				}
				Integer minEdgeW = getMinEdgeW(source,airports,head.af.getDepartToArrival().get(head.time),i.af.getDst());
				Integer maxT = null;
				if(minEdgeW ==Integer.MAX_VALUE || pollTimes > interval.size() ){
					 maxT = interval.last();
				}
				else{
					 maxT = getMaxT(interval.subSet(i.time,true, interval.last(),true) ,minEdgeW+head.af.getDepartToArrival().get(head.time) , i);
					// System.out.println(maxT);
				}
					for(Airport n : i.af.getDst().waitingTimes.keySet()){
						
							
								//Entry a = new Entry(res.get(AirportManager.getInstance().getAirports().get(n.getName())).time,res.get(AirportManager.getInstance().getAirports().get(n.getName())).af.clone());
								for(Flight f : i.af.getDst().waitingTimes.get(n)){
									//System.out.println("entro con f " +f);
								//	System.out.println(interval.subSet(i.time,true, maxT,true));
									for(Integer k : interval.subSet(i.time,true, maxT,true)){
										Integer gI = i.af.getDepartToArrival().get(k);							
										Flight min = i .af.getDst().waitingTimes.get(n).earliestArrivalTime(gI);
										if(min != null){
											Integer aux =  min.getFlightTime() + min.getDepartureTime()+min.getCurrentDayIndex()*60*24;
											if(res.get(AirportManager.getInstance().getAirports().get(n.getName())).af.getDepartToArrival().get(k) > aux){							
												res.get(AirportManager.getInstance().getAirports().get(n.getName())).af.getDepartToArrival().put(k, aux);
											}
										}
									}						
								}
								//System.out.println(pq);
							//	System.out.println(res.get(n).af);
								//pq.remove(res.get(AirportManager.getInstance().getAirports().get(n.getName())));
								//System.out.println(pq);
								//	System.out.println(AirportManager.getInstance().getAirports().get(n.getName()) +n.getName());
							//	pq.offer(res.get(AirportManager.getInstance().getAirports().get(n.getName())));
								//System.out.println(res.get(AirportManager.getInstance().getAirports().get(n.getName())));
								fb.remove(res.get(AirportManager.getInstance().getAirports().get(n.getName())));
								fb.offer(res.get(AirportManager.getInstance().getAirports().get(n.getName())));
								
							
						
					}
					i.time = maxT;
					if(maxT == interval.last()){
						if(i.af.getDst() == dest){
							return res;
						}
					//	airports.remove(i.af.getDst());
					}else{
						fb.offer(new Entry(i.time,i.af));
					}
					lastPoll=i;
					pollTimes++;
					}
												
				return res;
			}
		
		
		
	

	
//	private static int getPredecesorArrivalTime(Node source,Entry i,Integer k){
//		for(Flight f : source.flightsByDep){
//			if(f.getDepartureTime() == k){
//				int arriveF = f.getDepartureTime()+f.getCurrentDayIndex()*(60*24)+f.getFlightTime();
//				int arrivalTime = getPredecesorArrivalTimeR(AirportManager.getInstance().getAirports().get(f.getTarget()), i,arriveF);
//				if(arrivalTime != -1){
//					return arrivalTime;
//				}
//			}
//		}
//		return -1;//nunca se da
//	}
//
//	
//
//	private static int getPredecesorArrivalTimeR(Node source,Entry i, Integer k) {
//		if(source == i.af.getDst() && k == i.af.getDepartToArrival().get(i.time)){
//			return k;
//		}if(k>i.af.getDepartToArrival().get(i.time)){
//			return -1;
//		}
//		for(TimeAVL t : source.waitingTimes.values()){
//				Flight b = t.earliestArrivalTime(k);				
//				int arriveF = b.getDepartureTime()+b.getCurrentDayIndex()*(60*24)+b.getFlightTime();
//				int arrivalTime = getPredecesorArrivalTimeR(AirportManager.getInstance().getAirports().get(b.getTarget()), i,arriveF);
//				if(arrivalTime != -1){
//					return arrivalTime;
//				}
//		}	
//		return -1;//no se da nunca
//	}

	private static Integer getMaxT(SortedSet<Integer> interval, Integer time, Entry i) {
		Integer ans = null;
		for(Integer j : interval){
			//System.out.println(time+"jeje");
		//	System.out.println(i.af.getDepartToArrival().get(j));
			if(i.af.getDepartToArrival().get(j) <= time){
				 ans = j;
			}
			return ans;
		}
		return ans;
	}

	private static Integer getMinEdgeW(Node source,Collection<Node> airports ,Integer time, Node dest) {
		Integer min = Integer.MAX_VALUE;
		for(Node n: dest.incidentAirports){
			Flight f =  n.waitingTimes.get(dest.airport).earliestArrivalTime(time);
			if( f != null){
				Integer aux = f.getFlightTime() ;
				if(aux < min){
					min = aux;
				}
			}
		}
		return min;
		
	}


	private static class Entry implements Comparable<Entry>{
		private Integer time;
		private ArriveFunction af;
		
		public Entry(Integer time, ArriveFunction af) {
			super();
			this.time = time;
			this.af = af;
		}
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((af == null) ? 0 : af.hashCode());
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
			Entry other = (Entry) obj;
			if (af == null) {
				if (other.af != null)
					return false;
			} else if (!af.equals(other.af))
				return false;
			return true;
		}


		@Override
		public int compareTo(Entry o) {
			//System.out.println(this.af.getDst());
		//	System.out.println(o.af.getDst());
			if(this.af.getDst().airport.equals(o.af.getDst().airport)){
				return 0;
			}	
		//	System.out.println(time+" "+af);
			//System.out.println(this.af.getDepartToArrival().get(o.time));
			//System.out.println(this.af.getDepartToArrival().get(this.time));
			return this.af.getDepartToArrival().get(this.time)-o.af.getDepartToArrival().get(o.time);
		}
		public String toString(){
			return af.toString();
		}
		
		
	}

	


} 

