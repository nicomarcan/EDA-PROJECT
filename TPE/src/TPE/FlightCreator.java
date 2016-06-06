package TPE;

import java.util.LinkedList;
import java.util.List;

public class FlightCreator {
	AirportManager airportM = AirportManager.getInstance();

	public void addFlight(String airline, String flightNumber, String daysS, String origin, String target,
					String departureTimeS, String flightTimeS, double price) {
		String[] days = daysS.split("-");
		if(!Day.checkDays(days)){
			System.out.println("Ingreso dias repetidos");
		}
		List<Day> newDays = Day.getDays(days); 
		String[] hoursAndMin = departureTimeS.split(":");
		Integer departureTime = new Integer(hoursAndMin[0])*60+ new Integer(hoursAndMin[1]);
		String[] hours = flightTimeS.split("h");
		Integer flightTime;
		if(hours.length == 1){
			String[] mins = hours[0].split("m");
			 flightTime = new Integer(mins[0]);
		}else{
			String[]mins = hours[1].split("m");
			flightTime = new Integer(hours[0])*60+ new Integer(mins[0]);
		}
//		System.out.println(flightTime);
//		System.out.println(price);
//		
//		System.out.println(departureTime);
		Flight f = new Flight(airline, flightNumber, newDays, origin, target, departureTime, flightTime, price);
		airportM.addFlight(f);
		//System.out.println(airportM.getFlights());
	} 
	
	

	
	public void deleteFlight(String command) {
		String[] res = command.split(" ");
		String airline = res[2];
		String flightNumber = res[3];
		System.out.println(airline+" "+flightNumber);
		airportM.deleteFlight(airline, flightNumber);
	}

	public void deleteFlights() {
		airportM.deleteFlights();
		
	}
	
}
