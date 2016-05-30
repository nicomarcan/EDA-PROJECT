package TPE;

import java.util.ArrayList;
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
					a.timeFlight.remove(aux);
					a.waitingTimes.remove(aux);
			
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
		Node origin = airports.get(f.getOrigin().getName());
		Node target = airports.get(f.getTarget().getName());
		if(origin == null || target == null)
			return;
		if(flights.containsKey(new Entry(f.getAirline(),f.getFlightNumber())))
			return;
		flights.put(new Entry(f.getAirline(),f.getFlightNumber()),f);
		if(origin.priceFlight.containsKey(f.getTarget())){
			for(int i = 0;i < f.getDays().size();i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays().get(i)).insert(f); 
				origin.timeFlight.get(f.getTarget()).get(f.getDays().get(i)).insert(f);
				int k = (f.getFlightTime()+f.getDepartureTime())/(60*24);
				origin.waitingTimes.get(f.getTarget()).get(Day.getDay((Day.getIndex(f.getDays().get(i))+k)%7)).insert(f);
			}
		}else{
				HashMap<Day,Structure<Flight>> priceDay = new HashMap<Day,Structure<Flight>>();
				HashMap<Day,Structure<Flight>> timeDay = new HashMap<Day,Structure<Flight>>();
				HashMap<Day,AVL<Flight>> waitingTimeDay = new HashMap<Day,AVL<Flight>>();
				for(int i = 0;i < Day.size;i++){
					priceDay.put(Day.getDay(i), new Structure<Flight>(new Comparator<Flight>(){
							@Override
							public int compare(Flight o1, Flight o2) {
								return o2.getPrice().compareTo(o1.getPrice());
							}
						
						
					}));
					
					timeDay.put(Day.getDay(i), new Structure<Flight>(new Comparator<Flight>(){

						@Override
						public int compare(Flight arg0, Flight arg1) {
							return arg1.getFlightTime().compareTo(arg0.getFlightTime());
						}
						
					}));
					
					waitingTimeDay.put(Day.getDay(i), new AVL<Flight>(new Comparator<Flight>(){

						@Override
						public int compare(Flight o1, Flight o2) {
							return new Integer(o2.getDepartureTime()+o2.getFlightTime()%(60*24)).compareTo(o1.getFlightTime()+o1.getDepartureTime()%(60*24));
						}
						
					}));
					
					
				}
				for(int j = 0; j < f.getDays().size(); j++ ){
					priceDay.get(f.getDays().get(j)).insert(f);
					timeDay.get(f.getDays().get(j)).insert(f);
					int k = (f.getFlightTime()+f.getDepartureTime())/(60*24); 
					//System.out.println((Day.getIndex(f.getDays().get(j))+k)%7);
					waitingTimeDay.get(Day.getDay((Day.getIndex(f.getDays().get(j))+k)%7)).insert(f);
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
			flights.remove(e);
			Node origin = airports.get(f.getOrigin().getName());
			for(int i = 0;i < f.getDays().size();i++){
				origin.priceFlight.get(f.getTarget()).get(f.getDays().get(i)).remove(f);
				origin.timeFlight.get(f.getTarget()).get(f.getDays().get(i)).remove(f);
				int k = (f.getFlightTime()+f.getDepartureTime())/(60*24);
				origin.waitingTimes.get(f.getTarget()).get(Day.getDay((Day.getIndex(f.getDays().get(i))+k)%7)).remove(f);
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
		
		public String toString(){
			return airport.toString();
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
	public static void main(String[] args) {
		AirportManager airportM = new AirportManager();
		Airport a = new Airport("BUE", -80.0, 100.0);
		Airport b = new Airport("LON", 80.0, 25.0);
		ArrayList<Day> days = new ArrayList<Day>();
		days.add(Day.MONDAY);
		days.add(Day.FRIDAY);
		airportM.addAirport(a);
		airportM.addAirport(b);
		Flight f1 = new Flight("AA", "1234", days, b, 750, 360, 1200, a);
		Flight f2 = new Flight("ABA", "1234", days, b, 1000, 359, 1200, a);
		Flight f3 = new Flight("ACA", "1234", days, b, 800, 358, 1200, a);
		airportM.addFlight(f1);
		airportM.addFlight(f2);
		airportM.addFlight(f3);
		airportM.deleteFlight("ACA", "1234");
		airportM.deleteFlight("ABA", "1234");
	//	System.out.println(airportM.flights);
		System.out.println(airportM.airports.get(a.getName()).priceFlight.get(b).get(Day.MONDAY).getBestOne());
		//airportM.airports.get(a.getName()).priceFlight.get(b).get(Day.MONDAY).getElems().print();
		System.out.println(airportM.airports.get(a.getName()).timeFlight.get(b).get(Day.MONDAY).getBestOne());
	
	
		airportM.airports.get(a.getName()).waitingTimes.get(b).get(Day.TUESDAY).print();
	}
	
	 
}
