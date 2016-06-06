package TPE;

import java.util.LinkedList;
import java.util.List;

public class FlightCreator {
	AirportManager airportM = AirportManager.getInstance();

	public void addFlight(String command) {
		String[] res = command.split(" ");
		String[] days = res[4].split("-");
		if(!Day.checkDays(days)){
			System.out.println("Ingreso dias repetidos");
		}
		List<Day> newDays = Day.getDays(days); 
		String[] hoursAndMin = res[7].split(":");
		Integer departureTime = new Integer(hoursAndMin[0])*60+ new Integer(hoursAndMin[1]);
		String[] hours = res[8].split("h");
		Integer flightTime;
		if(hours.length == 1){
			String[] mins = hours[0].split("m");
			 flightTime = new Integer(mins[0]);
		}else{
			String[]mins = hours[1].split("m");
			flightTime = new Integer(hours[0])*60+ new Integer(mins[0]);
		}
		Double price = new Double(res[9]);
//		System.out.println(flightTime);
//		System.out.println(price);
//		
//		System.out.println(departureTime);
		Flight f = new Flight(res[2], res[3], newDays, res[5], res[6], departureTime, flightTime, price);
		airportM.addFlight(f);
		System.out.println(airportM.getFlights());
	} 
	
	

	public void addFlights(List<String> data) {
		for(int i = 0;i<data.size();i+=8){
			String[] days = data.get(i+2).split("-");
			if(!Day.checkDays(days)){
				System.out.println("Ingreso dias repetidos");
			}
			List<Day> newDays = Day.getDays(days); 
			String[] hoursAndMin = data.get(i+5).split(":");
			Integer departureTime = new Integer(hoursAndMin[0])*60+ new Integer(hoursAndMin[1]);
			String[] hours = data.get(i+6).split("h");
			Integer flightTime;
			if(hours.length == 1){
				String[] mins = hours[0].split("m");
				 flightTime = new Integer(mins[0]);
			}else{
				String[]mins = hours[1].split("m");
				flightTime = new Integer(hours[0])*60+ new Integer(mins[0]);
			}
			Double price = new Double(data.get(i+7));
//			System.out.println(flightTime);
//			System.out.println(price);
//			
//			System.out.println(departureTime);
			Flight f = new Flight(data.get(i+0), data.get(i+1), newDays, data.get(i+3), data.get(i+4), departureTime, flightTime, price);
			airportM.addFlight(f);
		}
		System.out.println(airportM.getFlights());
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
