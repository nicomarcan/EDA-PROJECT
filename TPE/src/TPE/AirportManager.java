package TPE;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**Esta clase es la que maneja el sistema de vuelos, en sintesis, representa el grafo generado a partir de los vuelos y los aeropuertos 
 * 
 * Cada nodo tiene una referencia a su aeropuerto, a los aeropuertos que inciden en el y almacena sus vuelos en tres estructuras diferentes,que se especifican m�s abajo.
 *  
 *  Se encarga tanto de agregar y remover vuelos y aeropuertos, como de encontrar las rutas entre dos aeropuertos.
 */
public class AirportManager {


	private Map<String,Node> airports = new HashMap<String,Node>();
	private Set<Node> airportsL = new HashSet<Node>(); 
	private Map<FlightID,Flight> flights = new HashMap<FlightID,Flight>();	
	private final int dayMins = 60*24;/** minutos en un dia**/
	private final int weekMins = 7*60*24;/**minutos en una semana**/
	private static AirportManager instance = new AirportManager();

	/** comparator por precio**/
	private Comparator<Flight> p = new Comparator<Flight>(){
		@Override
		public int compare(Flight o1, Flight o2) {
			int c = o2.getPrice().compareTo(o1.getPrice());
			if( c == 0){
				if(o1.equals(o2)){
					return c;
				}else{
					return o1.hashCode() - o2.hashCode();
				}
			}
			return c;
		}
	
	
};
	/** comparator por tiempo**/
	private Comparator<Flight> t = new Comparator<Flight>(){

		@Override
		public int compare(Flight o1, Flight o2) {
			int c = o2.getFlightTime().compareTo(o1.getFlightTime());
			if( c == 0){
				if(o1.equals(o2)){
					return c;
				}else{
					return o1.hashCode() - o2.hashCode();
				}
			}
			return c;
		}
		
	};
	/** comparator por tiempo de llegada**/
	private Comparator<Flight> w = new Comparator<Flight>(){

		@Override
		public int compare(Flight o1, Flight o2) {		
			return new Integer((o1.getCurrentDayIndex()*(dayMins)+o1.getDepartureTime()+o1.getFlightTime())%(weekMins)).compareTo((o2.getCurrentDayIndex()*(dayMins)+o2.getFlightTime()+o2.getDepartureTime())%(weekMins));
		}
		
	};
	
	
	private AirportManager() {
		
	}
	
	public static AirportManager getInstance() {
		return instance;
	}

	public Map<String, Node> getAirports() {
		return airports;
	}

	public void addAirport(Airport airport){
		if(!airports.containsKey(airport.getName())){
			Node a = new Node(airport);
			airports.put(airport.getName(),a);
			airportsL.add(a);
		}
	}
	
	/**
	 * Borra el aeropuerto name, y recorre todos los demas aeropuertos ,borrando los vuelos cuyo destino fueran el aeropuerto name, 
	 * y luego recorre todos los nodos destino de sus vuelos, borrando en ellos la referencia de que el aeropuerto incide.
	 * 
	 * @param name
	 */
	public void deleteAirport(String name){
		if(airports.containsKey(name)){
			Airport aux = airports.get(name).airport;
		
			
			for(Node a : airports.get(name).incidentAirports){
					a.priceFlight.remove(aux);
					a.timeFlight.remove(aux);
					a.waitingTimes.remove(aux);		
			}
			for(Airport a : airports.get(name).priceFlight.keySet()){
				airports.get(a.getName()).incidentAirports.remove(airports.get(name));
			}
			airportsL.remove(airports.get(name));
			airports.remove(name);
			System.out.println(airportsL);
		}
	}
	
	public void deleteFlights(){
		flights.clear();
		for(Node n : airportsL){
			n.incidentAirports.clear();
			n.priceFlight.clear();
			n.timeFlight.clear();
			n.waitingTimes.clear();
		}
	}

	public void deleteAirports(){
		airportsL.clear();
		airports.clear();
		flights.clear();
	}
	

	/**
	 * Agrega el vuelo f en el nodo origen.Si ya existian vuelos hacia el nodo destino, simplemente actualiza sus 3 estructuras.En caso
	 * contrario, agrega la key del nodo destino a los 3 mapas(priceFlight,timeFlight y waitingTimes),genera las estructuras y agrega los vuelos.
	 * @param f
	 */
	public void addFlight(Flight f){
		Node origin = airports.get(f.getOrigin());
		Node target = airports.get(f.getTarget());
		if(origin == null || target == null){
			if(origin!= null)
				System.out.println("El aeropuerto: "+f.getTarget()+" es invalido");
			else
				System.out.println("El aeropuerto: "+f.getOrigin()+" es invalido");
			return;
		}
		if(flights.containsKey(new FlightID(f.getAirline(),f.getFlightNumber())))
			return;
		flights.put(new FlightID(f.getAirline(),f.getFlightNumber()),f);
		
		if(origin.priceFlight.containsKey(airports.get(f.getTarget()).airport)){
			for(int i = 0;i < f.getDays().size();i++){
				Flight g = f.clone();
				g.setCurrentDayIndex(Day.getIndex(f.getDays().get(i)));		
				origin.priceFlight.get(airports.get(f.getTarget()).airport).get(f.getDays().get(i)).add(g); 
				origin.timeFlight.get(airports.get(f.getTarget()).airport).get(f.getDays().get(i)).add(g);
				origin.waitingTimes.get(airports.get(f.getTarget()).airport).insert(g);
			}
		}else{
				HashMap<Day,TreeSet<Flight>> priceDay = new HashMap<Day,TreeSet<Flight>>();
				HashMap<Day,TreeSet<Flight>> timeDay = new HashMap<Day,TreeSet<Flight>>();
				TimeAVL timeAVL = new TimeAVL(w);
				for(int i = 0;i < Day.size;i++){
					priceDay.put(Day.getDay(i), new TreeSet<Flight>(p));
					
					timeDay.put(Day.getDay(i), new TreeSet<Flight>(t));
					
					
				}
				for(int j = 0; j < f.getDays().size(); j++ ){
					Flight g = f.clone();
					g.setCurrentDayIndex(Day.getIndex(f.getDays().get(j)));	
					priceDay.get(f.getDays().get(j)).add(g);
					timeDay.get(f.getDays().get(j)).add(g);
					timeAVL.insert(g);
				}
				origin.priceFlight.put(airports.get(f.getTarget()).airport, priceDay);
				origin.timeFlight.put(airports.get(f.getTarget()).airport, timeDay);
				origin.waitingTimes.put(airports.get(f.getTarget()).airport, timeAVL);
		}
		target.incidentAirports.add(origin);
	}
	
	
									
		
		
	
	/**
	 * Borra un vuelo y, en caso de que fuera el unico hacia ese destino,borra en el nodo destino
	 * que el nodo origen incide en el.
	 * @param airline
	 * @param flightNumber
	 */
	public void deleteFlight(String airline,String flightNumber){
		FlightID e = new FlightID(airline, flightNumber);
		Flight f = flights.get(e);
		if(f != null){
			flights.remove(e);
			Node origin = airports.get(f.getOrigin());
			Airport target = airports.get(f.getTarget()).airport;
			for(int i = 0;i < f.getDays().size();i++){
				f.setCurrentDayIndex(Day.getIndex(f.getDays().get(i)));
				origin.priceFlight.get(target).get(f.getDays().get(i)).remove(f);
				origin.timeFlight.get(target).get(f.getDays().get(i)).remove(f);	
			}
			origin.waitingTimes.get(target).remove(f);
			if(origin.waitingTimes.get(target).size() == 0){
				airports.get(f.getTarget()).incidentAirports.remove(origin);
			}
		
		}
		return;
	}
	


	
	public Map<FlightID, Flight> getFlights() {
		return flights;
	}

	
/**Se encarga de buscar la ruta,cualquier sea la prioridad elegida.
 * 
 * En caso de ser tiempo total usa un algoritmo adjuntado en el informe, caso contrario usa dijkstra normal, con 
 * la particularidad de que antes de hacer dijkstra recorre sus nodos para sacar los mejores hacia un destino particular,
 * y asi eliminar las multiaristas.
 * 
 */
	public void findRoute(String source,String target,RoutePriority priority,List<Day> days, OutputFormat outputFormat, String output){
		Node sourceN = airports.get(source);
		Node targetN = airports.get(target);
		if(sourceN == null || targetN == null){
			System.out.println("Alguno de los aeropuertos es invalido");
			return;
		}
		FileManager fm = new FileManager();
		List<Flight> route;
		if(priority == RoutePriority.TOTALTIME){
			route = PathFinder.findPath(AirportManager.getInstance().getAirportsDijkstra(), sourceN, targetN,days);
		}else{
			Dijkstra d = new Dijkstra(sourceN.airport,targetN.airport,priority,days);
			route = d.findRoute();
		}
		fm.writeRoute(route, output, outputFormat);
		return ;
	}
	/**Esta clase representa un nodo en el grafo. Contiene una referencia a su aeropuerto
	 * y almacena sus vuelos por tres ordenes distintos: precio,tiempo de vuelo y tiempo de llegada al aeropuerto destino.
	 * Ademas tiene una referencia a los nodos que inciden en el.
	 * 
	 * Las estructuras que ordenan por tiempo y precio poseen los vuelos separados por dias para que el algoritmo de dijkstra sea 
	 * mas rapido en caso de que se especifique el dia de partida.
	 *
	 */
	public static class Node{
		public Airport airport;
		Map<Airport,Map<Day,TreeSet<Flight>>> priceFlight = new HashMap<Airport,Map<Day,TreeSet<Flight>>>();/** vuelos ordenados por precio**/
		Map<Airport,Map<Day,TreeSet<Flight>>> timeFlight = new HashMap<Airport,Map<Day,TreeSet<Flight>>>();/** vuelos ordenados por tiempo de vuelo**/
		Map<Airport,TimeAVL> waitingTimes = new HashMap<Airport,TimeAVL>();/** vuelos ordenados por horario de llegada, es decir 
																								que un vuelo que esta en el dia x no necesariamente salir
																								ese dia**/
																						
		Set<Node> incidentAirports = new HashSet<Node>();
		public boolean visited;
			
		public Node(Airport airport) {
			this.airport = airport;
		}		
		
		public String toString(){
			String s =  airport.toString();
			return s;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((airport == null) ? 0 : airport.hashCode());
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
			Node other = (Node) obj;
			if (airport == null) {
				if (other.airport != null)
					return false;
			} else if (!airport.equals(other.airport))
				return false;
			return true;
		}
		
		
	}
	
	
	private static class FlightID{
		private String airline;
		private String flightNumber;
		public FlightID(String airline, String flightNumber) {
			super();
			this.airline = airline;
			this.flightNumber = flightNumber;
		}
		
		public String toString(){
			return airline + " "+flightNumber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((airline == null) ? 0 : airline.hashCode());
			result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
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
			FlightID other = (FlightID) obj;
			if (airline == null) {
				if (other.airline != null)
					return false;
			} else if (!airline.equals(other.airline))
				return false;
			if (flightNumber == null) {
				if (other.flightNumber != null)
					return false;
			} else if (!flightNumber.equals(other.flightNumber))
				return false;
			return true;
		}
		
	}
			
	public Collection<Node> getAirportsDijkstra() { 
		return airports.values();
	}
			

	
	
}
