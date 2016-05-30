package TPE;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
	
	public static boolean parseCommand(String command) throws ClassNotFoundException, IOException { 
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		FileManager f = new FileManager();
		
		String HELP_MESSAGE = "***HELP MESSAGE***";
		String helpExp = "[hH]";
		String addAirExp = "insert airport [a-z A-Z]{1,3} -?[0-9]+\\.[0-9]+ -?[0-9]+\\.[0-9]+";
		String delAirExp = "delete airport [a-z A-Z]{1,3}";
		String addAllAirExp = "insert all airport [a-z A-Z 0-9]+\\.txt";
		String delAllAirExp = "delete all airport";
		String addFlExp = "insert flight [a-z A-Z]{1,3} [0-9]{1,7} (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-z A-Z]{1,3} [a-z A-Z]{1,3} ([0-1][0-9]|2[0-3]):[0-5][0-9] ([1-9]h|[1-9][0-9]h)?[0-5][0-9]m$";
		String delFlExp = "delete flight [a-z A-Z]{1,3} [1-9][0-9]*";
		String addAllFlExp = "insert all flight [a-z A-Z 0-9]+\\.txt";
		String delAllFlExp = "delete all flight";
		String findRouteExp = "findRoute src=[a-z A-Z]{3} dst=[a-z A-Z]{3} priority=(((pr)|(tt))|(ft)) (weekdays=((((((Lu)(-Ma)?(-Mi)?(-Ju)?(-Vi)?(-Sa)?(-Do)?)|((Ma)(-Mi)?(-Ju)?(-Vi)?(-Sa)?(-Do)?))|((Mi)(-Ju)?(-Vi)?(-Sa)?(-Do)?))|((Ju)(-Vi)?(-Sa)?(-Do)?|((Vi)(-Sa)?(-Do)?))|((Sa)(-Do)?))|(Do)))?";
		String outputFormatExp = "outputFormat ((text)|(KML))";
		String outputExp = "output ((stdout)|(file [a-z A-Z 0-9]+\\.txt))";
		String exitAndSaveExp = "exitAndSave";
		String quitExp = "quit";
		
		if(Pattern.matches(helpExp, command)) {
			System.out.println(HELP_MESSAGE);
			return false;
		}
		if(Pattern.matches(addAirExp,command)){
			airportC.addAirport(command);
			return false;
		}
		else if(Pattern.matches(addAllAirExp,command)){
			String[] res = command.split(" ");
			List<String> data = f.readAirports(res[3]);
			airportC.addAirports(data);
			return false;
		}
		else if(Pattern.matches(delAirExp,command)){
			airportC.deleteAirport(command);
			return false;
		}
		else if(Pattern.matches(delAllAirExp, command)){
			airportC.deleteAirports();
			return false;
		}
		else if(Pattern.matches(addFlExp, command)){
			flightC.addFlight(command);
			return false;
		}
		else if(Pattern.matches(delFlExp, command)){
			flightC.deleteFlight(command);
			return false;
		}
		else if(Pattern.matches(addAllFlExp, command)){
			String[] res = command.split(" ");
			List<String> data = f.readFlights(res[3]);
			flightC.addFlights(data);
			return false;
		}
		else if(Pattern.matches(delAllFlExp, command)){
			//airportM.deleteAllFlights();
			return false;
		}
		else if(Pattern.matches(findRouteExp, command)){
			System.out.println("*matches find route command*");
			return false;
		}
		else if(Pattern.matches(outputFormatExp, command)){
			System.out.println("*matches output format command*");
			return false;
		}
		else if(Pattern.matches(outputExp, command)){
			System.out.println("*matches output command*");
			return false;
		}
		else if(Pattern.matches(exitAndSaveExp, command)){
			System.out.println("*matches exit and save command*");
			return false;
		}
		else if(Pattern.matches(quitExp, command)){
			return true;
		}
		else
			System.out.println("Ingreso un comando no valido");

		return false;
	}

	public static boolean parseArguments(String[] args) {
		
		if(args.length == 1) {
			if(args[0].equals("--delete-airports")) {
				//airportC.deleteAirports(airportM);
			}
			else if(args[0].equals("--delete-flights")) {
				//airportM.deleteAllFlights();
			}
			else {
				return false;
			}
		}
		else if(args.length == 3) {
			if(args[0].equals("--airport-file")) {
				if(args[2].equals("--append-airports")) {
					System.out.println("APPEND AIRPORTS FROM FILE " + args[1]);
				}
				else if(args[2].equals("--replace-airports")) {
					System.out.println("REPLACE AIRPORTS FROM FILE " + args[1]);
				}
				else {
					return false;
				}
			}
			else if(args[0].equals("--flight-file")) {
				if(args[2].equals("--append-flights")) {
					System.out.println("APPEND FLIGHTS FROM FILE " + args[1]);
				}
				else if(args[2].equals("--replace-flights")) {
					System.out.println("REPLACE FLIGHTS FROM FILE " + args[1]);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
		return true;
		
	}
	
}

