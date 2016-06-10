package TPE;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

import TPE.AirportManager.Node;
import TPE.TimeAVL.FlightEl;
/**
 * Esta clase se encarga del problema del tiempo total, llevando a cabo el algoritmo especificado en el informe.
 * 
 * En un inicio decide el intervalo discreto de posibles tiempos de salida que representan los vuelos optimos
 * del aeropuerto origen, y luego arma las arriveFunctions para todos los aeropuertos
 * en el metodo timeRefinement, es decir arma por cada aeropuerto las funciones que a partir de un tiempo de partida especifico,devuelven el minimo
 * tiempo de llegada desde el origen hasta este.
 * 
 */
public class PathFinder {
	private static final int dayMins = 60*24;/** minutos en un dia**/
	
	
	public static List<Flight> findPath(Collection<Node> airports,Node source,Node dest,List<Day> departDays){
		NavigableSet<Integer> interval = new TreeSet<Integer>();
		int min = Integer.MAX_VALUE;
		int optTi = -1;
		Map<Node,Entry> aFunctions = null;
		boolean look = false;
 		for(int i = 0;i<departDays.size();i++){
			for(Airport a : source.priceFlight.keySet()){
					for(Flight f : source.priceFlight.get(a).get(departDays.get(i))){/**si se trata de la fuente chequea que sea el vuelo optimo que sale el dia especificado,no uno siguiente**/		
						FlightEl minF =source.waitingTimes.get(a).earliestArrivalTime(f.getDepartureTime()+f.getCurrentDayIndex()*dayMins,((f.getCurrentDayIndex()+1)*dayMins)-1);
						if(minF.getF().getDepartureTime()+minF.getF().getCurrentDayIndex()*dayMins == f.getDepartureTime()+f.getCurrentDayIndex()*dayMins){
							
							interval.add(f.getDepartureTime()+f.getCurrentDayIndex()*dayMins);
						}
					}	
			}
			if(interval.size() == 0){
	 			look = false;
	 		}else
	 			look = true;
			if(look){
			 Map<Node,Entry> aFunctionsL = timeRefinement(airports,source,dest,interval);
			  int minLocal = Integer.MAX_VALUE;
			 int  optTiLocal = -1 ;
			 for(Integer k : interval){
				Integer g = aFunctionsL.get(dest).af.getDepartToArrival().get(k);
				if(g != Integer.MAX_VALUE){
					if(minLocal > g-k){
						minLocal = g - k ;
						optTiLocal = k;
					}
				}
			 }
			 if(minLocal < min){
				 aFunctions = aFunctionsL;
				 min = minLocal;
				 optTi = optTiLocal;
			 }
			 interval.clear();
			}
		}
 		
		 if(min != Integer.MAX_VALUE){
			 List<Flight> f = pathSelection(airports,aFunctions ,source,dest,optTi);
			 return f;
	 }
 		return null;
	}
/**
 * Busca el camino cuyo tiempo de salida del origen es igual al tiempo min.
 * y corrobora en cada iteracion que las aristas elegidas sean las que corresponden
 * al minimo tiempo total,usando para ellos las arriveFunctions
 * @param airports
 * @param af
 * @param source
 * @param dest
 * @param min
 * @return
 */
	private static List<Flight> pathSelection(Collection<Node> airports, Map<Node, Entry> aFunctions, Node source, Node dest,
			int min) {
		Node current = dest;
		List<Flight> f = new LinkedList<Flight>();
		while(!current.equals(source)){
			for(Node n : current.incidentAirports){
					if(n.equals(source)){
						int gj = aFunctions.get(current).af.getDepartToArrival().get(min);
						int gI = aFunctions.get(n).af.getDepartToArrival().get(min);
						int max = (((gI/dayMins)+1)*dayMins)-1;
						if(n.waitingTimes.get(current.airport).earliestArrivalTime(gI,max).getArrivalTime() == gj){		
							f.add(0, n.waitingTimes.get(current.airport).earliestArrivalTime(gI,max).getF());					
							current = n;
							break;
						}
					}else{
						int gj = aFunctions.get(current).af.getDepartToArrival().get(min);
						int gI = aFunctions.get(n).af.getDepartToArrival().get(min);
						if(n.waitingTimes.get(current.airport).earliestArrivalTime(gI).getArrivalTime() == gj){
							f.add(0, n.waitingTimes.get(current.airport).earliestArrivalTime(gI).getF());
							current = n;
							break;
						}
				}
			}
				
				
		
	}
		return f;
	}
	/**
	 * Crea las arriveFunctions optimas, implementando una especie de algoritmo de dijkstra.
	 * @param airports
	 * @param source
	 * @param dest
	 * @param interval
	 * @return
	 */
	private static Map<Node,Entry> timeRefinement(Collection<Node> airports, Node source, Node dest,
			NavigableSet<Integer> interval) {
		Map<Node,Entry> res = new HashMap<Node,Entry>();
		TreeSet<Integer> subInterval = new TreeSet<Integer>();
		subInterval.add(interval.first());
	PriorityQueue<Entry> pq = new PriorityQueue<>(new Comparator<Entry>() {
		
		@Override
		public int compare(Entry o1, Entry o2) {
			return o1.compareTo(o2);
		}
	});
		/**
		 * Inicializa los entrys (tiempo de salida,arriveFunction) de cada nodo
		 * en caso de ser la fuente la arriveFunction representa la funcion identidad,
		 * caso contrario se les asigna "infinito" pues aun no han sido alcanzados
		 */
		for(Node airport : airports){
			if(!airport.equals(source)){
				ArriveFunction a = new ArriveFunction(source,airport);
				for(Integer i : interval){
					a.getDepartToArrival().put(i, Integer.MAX_VALUE);
				}
				res.put(airport, new Entry(subInterval.first(),a));
				pq.offer(new Entry(subInterval.first(),a));
			}else{
				ArriveFunction a = new ArriveFunction(source,source);
				for(Integer i : interval){
					a.getDepartToArrival().put(i, i);
				}
				res.put(airport, new Entry(subInterval.first(),a));
				pq.offer(new Entry(subInterval.first(),a));
			}
		}
			
			while(pq.size() >= 2){
				Entry i = pq.poll();
				Entry head = pq.peek();
				
				Integer minEdgeW = getMinEdgeW(source,airports,head.af.getDepartToArrival().get(head.time),i.af.getDst());
				Integer maxT = null;
				if(minEdgeW ==Integer.MAX_VALUE || head.af.getDepartToArrival().get(head.time) == Integer.MAX_VALUE )
					 maxT = interval.last();
				else
					 maxT = getMaxT(interval.subSet(i.time,true, interval.last(),true) ,minEdgeW+head.af.getDepartToArrival().get(head.time) , i);	
					for(Airport n : i.af.getDst().waitingTimes.keySet()){				
									for(Integer k : interval.subSet(i.time,true, maxT,true)){
										Integer gI = i.af.getDepartToArrival().get(k);
										FlightEl min;
										if(!i.af.getDst().equals(source))
											 min = i .af.getDst().waitingTimes.get(n).earliestArrivalTime(gI);
										else
											min = i .af.getDst().waitingTimes.get(n).earliestArrivalTime(gI,(((gI/(dayMins))+1)*dayMins)-1);
										if(min.getF() != null){
											Integer aux =  min.getArrivalTime();
											if(res.get(AirportManager.getInstance().getAirports().get(n.getName())).af.getDepartToArrival().get(k) > aux){							
												res.get(AirportManager.getInstance().getAirports().get(n.getName())).af.getDepartToArrival().put(k, aux);
											}
										}
									}						
					
								if(pq.remove(res.get(AirportManager.getInstance().getAirports().get(n.getName()))))
									pq.offer(res.get(AirportManager.getInstance().getAirports().get(n.getName())));																		
					}
					boolean offer = false;
					if(i.time != maxT){
						i.time = maxT;
						offer = true;
					}
					if(maxT == interval.last()){
						if(i.af.getDst() == dest){
							return res;
						}
					}else{
						if(offer)
							pq.offer(new Entry(i.time,i.af));
					}
					
					}
												
				return res;
			}
		
		
		

/**
 * Busca el mayor tiempo dentro del intervalo de salida que cumple la condicion pedida en el algoritmo.
 * @param interval
 * @param time
 * @param i
 * @return
 */
	private static Integer getMaxT(SortedSet<Integer> interval, Integer time, Entry i) {
		Integer ans = null;
		for(Integer j : interval){

			if(i.af.getDepartToArrival().get(j) <= time){
				 ans = j;
			}else
				return ans;
		}
		return ans;
	}
	/**
	 * Busca el menor peso de cualquier arista hacia el nodo dest,que es el que actualmente se llega mas rapido
	 * desde el origen.	 
	 * Este peso se evalua en el tiempo en que tarda en llegar al segundo nodo mas rapido de alcanzar.
	 * 
	 * @param source
	 * @param airports
	 * @param time
	 * @param dest
	 * @return
	 */
	private static Integer getMinEdgeW(Node source,Collection<Node> airports ,Integer time, Node dest) {
		Integer min = Integer.MAX_VALUE;
		for(Node n: dest.incidentAirports){
			FlightEl f =  n.waitingTimes.get(dest.airport).earliestArrivalTime(time);
			if( f.getF() != null){
				Integer aux = f.getF().getFlightTime() ;
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

			int c = this.af.getDepartToArrival().get(this.time)-o.af.getDepartToArrival().get(o.time);
			if(c == 0){
				if(af.equals(o.af)){
					return 0;
				}
				return this.af.hashCode()-o.af.hashCode();
			}	

			return c;
		}
		public String toString(){
			return af.getDst()+" TIME "+time+" "+af.getDepartToArrival().get(time);
		}
		
		
	}

	


} 

