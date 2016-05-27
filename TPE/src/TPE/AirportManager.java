package TPE;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportManager {
	private Map<String,Node> airports = new HashMap<String,Node>();
	private AVL<Node> airportsL = new AVL<Node>(new Comparator<Node>(){

		@Override
		public int compare(Node o1, Node o2) {
			return o1.airport.getName().compareTo(o2.airport.getName());
		}
		
	}); 
	private Map<Entry,Flight> flights = new HashMap<Entry,Flight>();

	public void addAirport(Airport airport){
		if(!airports.containsKey(airport.getName())){
			airports.put(airport.getName(),new Node(airport));
			airportsL.insert(new Node(airport));
		}
	}
	
	public void deleteAirport(String name){
		if(airports.containsKey(name)){
			Airport aux = airports.get(name).airport;
			airports.remove(name);
			for(Node a : airportsL){
				if(a.priceFlight.containsKey(aux)){
					a.priceFlight.remove(aux);
			
				}
			}
			airportsL.remove(airports.get(name));
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
		if(flights.containsKey(new Entry(f.getAirline(),f.getFlightNumber())))
			return;
		if(origin.priceFlight.containsKey(f.getTarget())){
			for(int i = 0;i < f.getDays().length;i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays()[i]).insert(f); 
			}
		}else{
				HashMap<Day,Structure<Flight>> priceDay = new HashMap<Day,Structure<Flight>>();
				for(int i = 0;i < Day.size;i++){
					priceDay.put(Day.getDay(i), new Structure<Flight>(new Comparator<Flight>(){
							@Override
							public int compare(Flight o1, Flight o2) {
								return o1.getPrice().compareTo(o2.getPrice());
							}
						
						
					}));
				}
				for(int j = 0; j < f.getDays().length ; j++ ){
					priceDay.get(f.getDays()[j]).insert(f);
				}
				origin.priceFlight.put(f.getTarget(), priceDay);
		}
	}
									
		
		
	
	
	public void deleteFlight(String airline,String flightNumber){
		Entry e = new Entry(airline, flightNumber);
		Flight f = flights.get(e);
		if(f != null){
			Node origin = airports.get(f.getOrigin());
			for(int i = 0;i < f.getDays().length;i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays()[i]).remove(f);
			}
		}
		return;
	}
	





	private static class Node{
		Airport airport;
		Map<Airport,Map<Day,Structure<Flight>>> priceFlight = new HashMap<Airport,Map<Day,Structure<Flight>>>();
	
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
