package TPE;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AirportManager {
	private Map<String,Node> airports = new HashMap<String,Node>();
	private Map<Entry,Flight> flights = new HashMap<Entry,Flight>();

	public void addAirport(Airport airport){
		if(!airports.containsKey(airport.getName())){
			airports.put(airport.getName(),new Node(airport));
		}
	}
	
	public void deleteAirport(String name){
		if(airports.containsKey(name)){
			airports.remove(name);
		}
	}
	
	public void addAirports(List<Airport> airports){
		for(Airport a : airports){
			addAirport(a);
		}
	}

	public void deleteAirports(List<String> airports){
		for(String s : airports){
			deleteAirport(s);
		}
	}
	
	public void addFlight(Flight f){
		if(!airports.containsKey(f.getOrigin()) || ! airports.containsKey(f.getTarget()))
			return;
		if(!airports.get(f.getOrigin()).priceOrder.contains(f)){
			airports.get(f.getOrigin()).priceOrder.add(f);
			airports.get(f.getOrigin()).timeOrder.add(f);
		}
		
	}
	
	public void deleteFlight(String airline,String flightNumber){
		Entry e = new Entry(airline, flightNumber);
		if(flights.containsKey(e)){
			Node origin = airports.get(flights.get(e).getOrigin());
			origin.priceOrder.remove(flights.get(e));
			origin.timeOrder.remove(flights.get(e));
		}
	}
	





	private static class Node{
		Airport airport;
		Set<Flight> priceOrder = new TreeSet<Flight>();
		Set<Flight> timeOrder = new TreeSet<Flight>();
		public boolean visited;
		
		public Node(Airport airport) {
			super();
			this.airport = airport;
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
		
	}
	 
}
