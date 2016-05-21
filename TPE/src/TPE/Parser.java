package TPE;



import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
	public boolean parse(String command,AirportManager airportM ) throws ClassNotFoundException, IOException{ 
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		String addAirExp = "insert airport [a-z A-Z]{3} -?[0-9]+\\.[0-9]+ -?[0-9]+\\.[0-9]+";
		String delAirExp = "delete airport [a-z A-Z]{3}";
		String addAllAirExp = "insert all airport [a-z A-Z 0-9]+\\.txt";
		String delAllAirExp = "delete all airport";
		String addFlExp = "insert flight [a-z A-Z]{1,3} [1-9][0-9]* [A-Z][a-z](-[A-Z][a-z])* [a-z A-Z]{3} [a-z A-Z]{3} [0-2]{2}:[0-2]{2}";
		String delFlExp = "delete flight [a-z A-Z]{1,3} [1-9][0-9]*";
		String addAllFlExp = "insert all flight [a-z A-Z 0-9]+\\.txt";
		String delAllFlExp = "delete all flight";
		String quitExp = "quit";
		FileManager f = new FileManager();
		
		if(Pattern.matches(addAirExp,command)){
			airportC.addAirport(command,airportM);
			return false;
		}
		else if(Pattern.matches(addAllAirExp,command)){
			String[] res = command.split(" ");
			List<String> data = f.readAirports(res[3]);
			airportC.addAirports(data,airportM);
			return false;
		}
		else if(Pattern.matches(delAirExp,command)){
			airportC.deleteAirport(command, airportM);
			return false;
		}
		else if(Pattern.matches(delAllAirExp, command)){
			airportC.deleteAirports(airportM);
			return false;
		}
		else if(Pattern.matches(addFlExp, command)){
			flightC.addFlight(command,airportM);
			return false;
		}
		else if(Pattern.matches(delFlExp, command)){
			flightC.deleteFlight(command, airportM);
			return false;
		}
		else if(Pattern.matches(addAllFlExp, command)){
			String[] res = command.split(" ");
			List<String> data = f.readFlights(res[3]);
			flightC.addFlights(data,airportM);
			return false;
		}
		else if(Pattern.matches(delAllFlExp, command)){
			//airportM.deleteAllFlights();
			return false;
		}
		else if(Pattern.matches(quitExp, command)){
			return true;
		}
		else
			System.out.println("Ingreso un comando no v�lido");
		return false;
	}


	
	
}