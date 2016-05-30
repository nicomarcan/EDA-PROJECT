package TPE;

import java.util.LinkedList;
import java.util.List;

public class FlightCreator {
	AirportManager airportM = AirportManager.getInstance();

	public void addFlight(String command) {
		String[] res = command.split(" ");
		String[] days = res[4].split("-");
		if(!checkDays(days)){
			System.out.println("Ingreso dias repetidos");
		}
		List<Day> newDays = getDays(days); 
		String[] hoursAndMin = res[7].split(":");
		Integer departureTime = new Integer(hoursAndMin[0])*60+ new Integer(hoursAndMin[1]);
		System.out.println(departureTime);
	} 
	
	private List<Day> getDays(String[] days) {
		List<Day> ans = new LinkedList<Day>();
		for(int i = 0; i <days.length;i++){
			switch(days[i]){
			case "Lu": ans.add(Day.MONDAY);
						break;
			case "Ma": ans.add(Day.TUESDAY);
						break;
			case "Mi": ans.add(Day.WEDNESDAY);
						break;
			case "Ju": ans.add(Day.THURSDAY);
						break;
			case "Vi": ans.add(Day.FRIDAY);
						break;
			case "Sa": ans.add(Day.SATURDAY);
						break;
			case "Do": ans.add(Day.SUNDAY);
						break;		
			}
		}
		return ans;
	}

	private boolean checkDays(String[] days) {
		for(int i = 0; i < days.length;i++){
			for(int j = i+1 ; j < days.length;j++){
				if(days[i].equals(days[j])){
					return false;
				}
			}
		}
		return true;
		
	}

	public void addFlights(List<String> data) {
		
	}
	
	public void deleteFlight(String command) {
		String[] res = command.split(" ");
		String airline = res[2];
		String flightNumber = res[3];
		System.out.println(airline+" "+flightNumber);
		airportM.deleteFlight(airline, flightNumber);
	}
	
}
