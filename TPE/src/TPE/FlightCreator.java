package TPE;

import java.util.List;

public class FlightCreator {

	public void addFlight(String command, AirportManager airportM) {
		String[] res = command.split(" ");
		for(int i=0;i<res.length;i++){
			System.out.println(res[i]);
		}
	}
	
	public void addFlights(List<String> data,AirportManager airportM){
		
	}
	
	public void deleteFlight(String command,AirportManager airportM){
		String[] res = command.split(" ");
		String airline = res[2];
		String flightNumber = res[3];
		System.out.println(airline+" "+flightNumber);
		airportM.deleteFlight(airline, flightNumber);
	}
	
}
