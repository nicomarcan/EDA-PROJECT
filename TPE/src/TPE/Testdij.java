package TPE;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class Testdij {

	List<Day> ma = new ArrayList<Day>();
	List<Day> mi = new ArrayList<Day>();
	AirportManager airportM = AirportManager.getInstance();	
	
	@Before
	public void initial(){
		
		airportM.addAirport(new Airport("NOR", 49.9348, 64.41));
		airportM.addAirport(new Airport("WAL", 82.5884, 87.2821));
		airportM.addAirport(new Airport("WIN", -74.1735, -86.5338));
		airportM.addAirport(new Airport("KLA", 18.3018, 41.0472));
		airportM.addAirport(new Airport("MEE", -22.0665, -88.0582));
		
		ma.add(Day.TUESDAY);
		mi.add(Day.WEDNESDAY);
		
	
		airportM.addFlight(new Flight("AA","1547", ma, "NOR", "WAL", 1200, 320, 150.58  ));
		airportM.addFlight(new Flight("AAB","1368", ma, "WAL", "WIN", 1200, 320, 150.0  ));
		airportM.addFlight(new Flight("AA","7543", ma, "WIN", "KLA", 900, 240, 500.0  ));
		airportM.addFlight(new Flight("AA","2367", ma, "KLA", "MEE", 1200, 140, 2000.0  ));
		airportM.addFlight(new Flight("AA","1748", ma, "MEE", "NOR", 100, 230, 1000.0  ));
		airportM.addFlight(new Flight("AA","2753", ma, "NOR", "WIN", 500, 230, 600.0  ));
		airportM.addFlight(new Flight("AA","7523", mi, "NOR", "KLA", 1200, 230, 2500.0  ));
		airportM.addFlight(new Flight("AA","9463", ma, "NOR", "MEE", 2200, 50, 1200.0  ));
		airportM.addFlight(new Flight("AA","3572", ma, "KLA", "NOR", 800, 30, 1200.0  ));
		
		
	}
	
	@Test
	public void testDijPrice(){
		Dijkstra dstra= new Dijkstra(airportM.getAirports().get("MEE").airport, airportM.getAirports().get("KLA").airport, RoutePriority.PRICE, ma);
		List<Flight> flights = dstra.findRoute();
		
		double price = 0.0;
		int time = 0;
		for (Flight flight: flights){
			
			price += flight.getPrice();
			time += flight.getFlightTime();
			
		}
		
		assertEquals(price,1800.58, 0.01);
		assertEquals(time,1110);
	}
	
	@Test
	public void testDijTime(){
		Dijkstra dstra= new Dijkstra(airportM.getAirports().get("MEE").airport, airportM.getAirports().get("KLA").airport, RoutePriority.TIME, ma);
		List<Flight> flights = dstra.findRoute();
		
		double price = 0.0;
		int time = 0;
		for (Flight flight: flights){
			
			price += flight.getPrice();
			time += flight.getFlightTime();
			
		}	
		
		assertEquals(price,3500.0, 0.01);
		assertEquals(time,460);
	}
	
}
