package TPE;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AirportManager {
	private Map<String,Node> airports = new HashMap<String,Node>();
	private List<Node> airportsL = new LinkedList<Node>(); 
	private Map<Entry,Flight> flights = new HashMap<Entry,Flight>();

	public void addAirport(Airport airport){
		if(!airports.containsKey(airport.getName())){
			airports.put(airport.getName(),new Node(airport));
			airportsL.add(new Node(airport));
		}
	}
	
	public void deleteAirport(String name){
		if(airports.containsKey(name)){
			Airport aux = airports.get(name).airport;
			airports.remove(name);
			for(Node a : airportsL){
				if(a.priceFlight.containsKey(aux)){
					a.priceFlight.remove(aux);
					a.timeFlight.remove(aux);
				}
			}
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
		Node origin = airports.get(f.getOrigin());
		Node target = airports.get(f.getTarget());
		if(origin == null || target == null)
			return;
		if(flights.containsKey(f))
			return;
		if(origin.priceFlight.containsKey(f.getTarget())){
			for(int i = 0;i < f.getDays().length;i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays()[i]).insert(f); 
				origin.timeFlight.get(f.getTarget()).get(f.getDays()[i]).insert(f); 
			}
		}else{
				HashMap<Day,Structure<Flight>> priceDay = new HashMap<Day,Structure<Flight>>();
				HashMap<Day,Structure<Flight>> timeDay = new HashMap<Day,Structure<Flight>>();
				for(int i = 0;i < Day.size;i++){
					priceDay.put(Day.getDay(i), new Structure<Flight>());
					timeDay.put(Day.getDay(i),new Structure<Flight>());
				}
				origin.priceFlight.put(f.getTarget(), priceDay);
				origin.timeFlight.put(f.getTarget(),timeDay);
			}						
		}
		
	
	
	public void deleteFlight(String airline,String flightNumber){
//		Entry e = new Entry(airline, flightNumber);
//		if(flights.containsKey(e)){
//			Node origin = airports.get(flights.get(e).getOrigin());
//			origin.flight.remove(flights.get(e));
//		}
		return;
	}
	





	private static class Node{
		Airport airport;
		Map<Airport,Map<Day,Structure<Flight>>> priceFlight = new HashMap<Airport,Map<Day,Structure<Flight>>>();
		Map<Airport,Map<Day,Structure<Flight>>> timeFlight = new HashMap<Airport,Map<Day,Structure<Flight>>>();
	
		public boolean visited;
			
		public Node(Airport airport) {
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
