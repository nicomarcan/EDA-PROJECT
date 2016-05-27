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
			for(int i = 0;i < f.getDays().size();i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays().get(i)).insert(f); 
				origin.timeFlight.get(f.getTarget()).get(f.getDays().get(i)).insert(f);
				origin.waitingTimes.get(f.getTarget()).get(f.getDays().get(i)).insert(f);
			}
		}else{
				HashMap<Day,Structure<Flight>> priceDay = new HashMap<Day,Structure<Flight>>();
				HashMap<Day,Structure<Flight>> timeDay = new HashMap<Day,Structure<Flight>>();
				HashMap<Day,AVL<Flight>> waitingTimeDay = new HashMap<Day,AVL<Flight>>();
				for(int i = 0;i < Day.size;i++){
					priceDay.put(Day.getDay(i), new Structure<Flight>(new Comparator<Flight>(){
							@Override
							public int compare(Flight o1, Flight o2) {
								return o1.getPrice().compareTo(o2.getPrice());
							}
						
						
					}));
					
					timeDay.put(Day.getDay(i), new Structure<Flight>(new Comparator<Flight>(){

						@Override
						public int compare(Flight arg0, Flight arg1) {
							return arg0.getFlightTime().compareTo(arg1.getFlightTime());
						}
						
					}));
					
					waitingTimeDay.put(Day.getDay(i), new AVL<Flight>(new Comparator<Flight>(){

						@Override
						public int compare(Flight o1, Flight o2) {
							return new Integer(o1.getDepartureTime()+o1.getFlightTime()).compareTo(o2.getFlightTime()+o2.getDepartureTime());
						}
						
					}));
					
					
				}
				for(int j = 0; j < f.getDays().size(); j++ ){
					priceDay.get(f.getDays().get(j)).insert(f);
					timeDay.get(f.getDays().get(j)).insert(f);
					//falta chekear en que dia insertarlo 
					waitingTimeDay.get(f.getDays().get(j)).insert(f);
				}
				origin.priceFlight.put(f.getTarget(), priceDay);
				origin.timeFlight.put(f.getTarget(), timeDay);
				origin.waitingTimes.put(f.getTarget(), waitingTimeDay);
		}
	}
									
		
		
	
	
	public void deleteFlight(String airline,String flightNumber){
		Entry e = new Entry(airline, flightNumber);
		Flight f = flights.get(e);
		if(f != null){
			Node origin = airports.get(f.getOrigin());
			for(int i = 0;i < f.getDays().size();i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays().get(i)).remove(f);
				origin.timeFlight.get(f.getTarget()).get(f.getDays().get(i)).remove(f);
				origin.waitingTimes.get(f.getTarget()).get(f.getDays().get(i)).remove(f);
			}
		}
		return;
	}
	





	private static class Node{
		Airport airport;
		Map<Airport,Map<Day,Structure<Flight>>> priceFlight = new HashMap<Airport,Map<Day,Structure<Flight>>>();
		Map<Airport,Map<Day,Structure<Flight>>> timeFlight = new HashMap<Airport,Map<Day,Structure<Flight>>>();
		Map<Airport,Map<Day,AVL<Flight>>> waitingTimes = new HashMap<Airport,Map<Day,AVL<Flight>>>();
		
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
