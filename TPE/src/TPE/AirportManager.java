package TPE;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;


public class AirportManager {
	public static int  i = 0;
	
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
	
	private Comparator<Flight> w = new Comparator<Flight>(){

		@Override
		public int compare(Flight o1, Flight o2) {		
			return new Integer((o1.getCurrentDayIndex()*(60*24)+o1.getDepartureTime()+o1.getFlightTime())%(7*60*24)).compareTo((o2.getCurrentDayIndex()*(60*24)+o2.getFlightTime()+o2.getDepartureTime())%(7*60*24));
		}
		
	};
	
	private Map<String,Node> airports = new HashMap<String,Node>();
	
	private Set<Node> airportsL = new HashSet<Node>(); 
	
	private Map<Entry,Flight> flights = new HashMap<Entry,Flight>();
	
	private final int dayMins = 60*24;
	
	private static AirportManager instance = new AirportManager();
	
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
			airports.put(airport.getName(),new Node(airport));
			airportsL.add(new Node(airport));
		}else{
			//System.out.println("quisiste agregar repetido "+airport.getName());
			i++;
		}
	}
	/**
	 * Borra el aeropuerto name, y recorre todos los dem�s aeropuertos ,
	 * borrando los vuelos cuyo destino fueran el aeropuerto name 
	 * @param name
	 */
	public void deleteAirport(String name){
		if(airports.containsKey(name)){
			Airport aux = airports.get(name).airport;
		
			
			for(Node a : airportsL){
				if(a.priceFlight.containsKey(aux)){
					a.priceFlight.remove(aux);
					a.timeFlight.remove(aux);
					a.waitingTimes.remove(aux);
			
				}
			};
			airportsL.remove(airports.get(name));
			airports.remove(name);
			System.out.println(airportsL);
		}
	}
	
	public void deleteFlights(){
		flights.clear();
		for(Node n : airportsL){
			n.priceFlight.clear();
			n.timeFlight.clear();
			n.waitingTimes.clear();
		}
		System.out.println(airports);
	}

	public void deleteAirports(){
		airportsL.clear();
		airports.clear();
		flights.clear();
	}
	
	/** personalizar el error**/
	
	public void addFlight(Flight f){
		Node origin = airports.get(f.getOrigin());
		Node target = airports.get(f.getTarget());
		if(origin == null || target == null){
			System.out.println("alguno de los aeropuertos es invalido: "+f.getOrigin()+ " o "+f.getTarget());
			return;
		}
		if(flights.containsKey(new Entry(f.getAirline(),f.getFlightNumber())))
			return;
		flights.put(new Entry(f.getAirline(),f.getFlightNumber()),f);
		/** si ya habian vuelos hacia ese destino,s�lo lo agrega a las estructuras de tiempo, precio y tiempo total**/
		if(origin.priceFlight.containsKey(airports.get(f.getTarget()).airport)){
			for(int i = 0;i < f.getDays().size();i++){
				Flight g = f.clone();
				g.setCurrentDayIndex(Day.getIndex(f.getDays().get(i)));		
				target.flightsByDep.add(g);
				origin.priceFlight.get(airports.get(f.getTarget()).airport).get(f.getDays().get(i)).add(g); 
				origin.timeFlight.get(airports.get(f.getTarget()).airport).get(f.getDays().get(i)).add(g);
				origin.waitingTimes.get(airports.get(f.getTarget()).airport).insert(g);
			}
		}else{/**Sino agrega en el nodo origen las 3 estructuras para el aeropuerto destino**/
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
					origin.flightsByDep.add(g);
					//System.out.println((Day.getIndex(f.getDays().get(j))+k)%7);
					timeAVL.insert(g);
				}
				origin.priceFlight.put(airports.get(f.getTarget()).airport, priceDay);
				origin.timeFlight.put(airports.get(f.getTarget()).airport, timeDay);
				origin.waitingTimes.put(airports.get(f.getTarget()).airport, timeAVL);
		}
		target.incidentAirports.add(origin);
	}
	
	
									
		
		
	
	
	public void deleteFlight(String airline,String flightNumber){
		Entry e = new Entry(airline, flightNumber);
		Flight f = flights.get(e);
		if(f != null){
			flights.remove(e);
			Node origin = airports.get(f.getOrigin());
			Airport target = airports.get(f.getTarget()).airport;
			for(int i = 0;i < f.getDays().size();i++){
				f.setCurrentDayIndex(Day.getIndex(f.getDays().get(i)));
				origin.priceFlight.get(target).get(f.getDays().get(i)).remove(f);
				origin.timeFlight.get(target).get(f.getDays().get(i)).remove(f);	
				origin.flightsByDep.remove(f);
			}
			origin.waitingTimes.get(target).remove(f);
			if(origin.waitingTimes.get(target).size() == 0){
				airports.get(f.getTarget()).incidentAirports.remove(origin);
			}
		
		}
		System.out.println(flights);
		return;
	}
	


	

	public Map<Entry, Flight> getFlights() {
		return flights;
	}

	public static class Box {
		double weight;
		Node airport;
		Flight flight;
		
		public Box(Node n, Flight f, double w) {
			weight = w;
			airport = n;
			flight = f;
		}
	}

	public void findRoute(String source,String target,RoutePriority priority,List<Day> days, OutputFormat outputFormat, String output){
		Node sourceN = airports.get(source);
		Node targetN = airports.get(target);
		System.out.println(output+" "+outputFormat);
		if(sourceN == null || targetN == null){
			System.out.println("Alguno de los aeropuertos es invalido");
		}
		Dijkstra d = new Dijkstra(sourceN.airport,targetN.airport,priority,days);
		FileManager fm = new FileManager();
		fm.writeRoute(d.findRoute(), output, outputFormat);
		return ;
	}

	protected static class Node{
		Airport airport;
		Map<Airport,Map<Day,TreeSet<Flight>>> priceFlight = new HashMap<Airport,Map<Day,TreeSet<Flight>>>();/** vuelos ordenados por precio**/
		Map<Airport,Map<Day,TreeSet<Flight>>> timeFlight = new HashMap<Airport,Map<Day,TreeSet<Flight>>>();/** vuelos ordenados por tiempo de vuelo**/
		Map<Airport,TimeAVL> waitingTimes = new HashMap<Airport,TimeAVL>();/** vuelos ordenados por horario de llegada, es decir 
																								que un vuelo que est� en el d�a x no necesariamente sali�
																								ese d�a**/
		TreeSet<Flight> flightsByDep= new TreeSet<Flight>(new Comparator<Flight>(){

			@Override
			public int compare(Flight o1, Flight o2) {
				int c =  o1.getDepartureTime()+o1.getCurrentDayIndex()*(60*24)-o2.getDepartureTime()+o2.getCurrentDayIndex()*(60*24);
				if(c == 0){
					if(o1.equals(o2))
						return c;
					else
						return o1.hashCode() -o2.hashCode();
				}
				return c;
			}
			
		});																					
		Set<Node> incidentAirports = new HashSet<Node>();
		public boolean visited;
			
		public Node(Airport airport) {
			this.airport = airport;
		}		
		
		public String toString(){
			String s =  airport.toString();
//			s+=" [";
//			for(Node n : incidentAirports){
//				s+=n.airport;
//			}
//			s+="]";
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
	
	
	private static class Entry{
		private String airline;
		private String flightNumber;
		public Entry(String airline, String flightNumber) {
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
			Entry other = (Entry) obj;
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
	
	// Dummies para el Dijkstra 
			public Set<Flight> getFlightsDijkstra() { return null; }		
			public Collection<Node> getAirportsDijkstra() { return airports.values();}
			
	public static void main(String[] args) throws ClassNotFoundException, IOException {
//		AirportManager airportM = AirportManager.getInstance();
//		Airport a = new Airport("BUE", -80.0, 100.0);
//		Airport b = new Airport("LON", 80.0, 25.0);
//		Airport c = new Airport("ARG", 80.0, 25.0);
//		ArrayList<Day> days = new ArrayList<Day>();
//		days.add(Day.TUESDAY);
		ArrayList<Day> da = new ArrayList<Day>();
		da.add(Day.MONDAY);
//		airportM.addAirport(a);
//		airportM.addAirport(b);
//		airportM.addAirport(c);
//		Flight f = new Flight("AA","1234", da, a.getName(), b.getName(), 719,18, 12.58);
//		Flight f2 = new Flight("AAB","1234", days, b.getName(), c.getName(), 718,16, 1222.0);
//		Flight f3 = new Flight("AAC","1234", da, a.getName(), b.getName(), 10,1, 2000.0);
//	//	Flight f4 = new Flight("AAC","124", days, a.getName(), b.getName(), 717,20, 12.58);
//		airportM.addFlight(f);
//		airportM.addFlight(f2);
		//airportM.addFlight(f3);
	//	airportM.addFlight(f4);
		//System.out.println(airportM.airports.get(a.getName()).priceFlight);
		//airportM.getAirports().get("BUE").waitingTimes.get(b).print();
		AirportManager airportM = AirportManager.getInstance();
		Parser.parseCommand("insert all airport juli.txt");
		Parser.parseCommand("insert all flight julif.txt");
		//Parser.parseCommand("findRoute src=VEA dst=JIA priority=ft");
		PathFinder.findPath(airportM.getAirports().values(), airportM.getAirports().get("NPK"),airportM.getAirports().get("DHB"),Day.getAllDays());
		//System.out.println(airportM.getAirports().get(a.getName()).waitingTimes.get(b).earliestArrivalTime(719));
	}
	
	
}
