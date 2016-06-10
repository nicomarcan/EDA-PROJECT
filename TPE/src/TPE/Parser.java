package TPE;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Esta clase se encarga del analisis sintáctico de los comandos y argumentos enviados por el usuario.
 *
 */
public class Parser {
	public static OutputFormat outputFormat = null;
	public static String output = null;
	private static final String AIRPORTFILE = "airports.txt";/**Nombre del archivo donde guardan los aeropuertos**/
	private static final String FLIGHTFILE = "flights.txt";/**Nombre del archivo donde guardan los vuelos**/

	public static boolean parseCommand(String command) throws ClassNotFoundException, IOException { 
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		FileManager f = new FileManager();
		
		
		
		String HELP_MESSAGE = 	"*Commandos: \n" +
				"*insert airport [nombre] [lat] [lng]\n" +
				"*insert all airport [FILE]\n" +
				"*delete airport [nombre]\n" +
				"*delete all airport\n" +
				"*insert flight [aerolinea] [nroVuelo] [diasSemana] [origen] [destino] [horaSalida] [duracion] [precio]\n" +
				"*insert all flight [FILE]\n" +
				"*delete flight [aerolinea] [nroVuelo]\n" +
				"*delete all flight\n" +
				"*findRoute src=[origen] dst=[destino] priority={ft|pr|tt} *weekdays=[diasSemana]\n" +
				"*outputFormat {text|KML}\n" +
				"*output {stdout|file [nombreArchivo]}\n" +
				"*load\n" +
				"*exitAndSave\n" +
				"*quit";
		String helpExp = "[hH]";
		String addAirExp = "insert airport [a-z A-Z]{1,3} -?[0-9]+\\.[0-9]+ -?[0-9]+\\.[0-9]+";
		String delAirExp = "delete airport [a-z A-Z]{1,3}";
		String addAllAirExp = "insert all airport [a-z A-Z 0-9]+\\.txt";
		String delAllAirExp = "delete all airport";
		String addFlExp = "insert flight [a-z A-Z]{1,3} [0-9]{1,7} (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-z A-Z]{1,3} [a-z A-Z]{1,3} ([0-1][0-9]|2[0-3]):[0-5][0-9] ([1-9]h|[1-9][0-9]h)?[0-5][0-9]m [0-9]+\\.[0-9]+$";
		String delFlExp = "delete flight [a-z A-Z]{1,3} [1-9][0-9]*";
		String addAllFlExp = "insert all flight [a-z A-Z 0-9]+\\.txt";
		String delAllFlExp = "delete all flight";
		String findRouteExp = "findRoute src=[a-z A-Z]{3} dst=[a-z A-Z]{3} priority=(pr|tt|ft)( weekdays=(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*)?$";
		String outputFormatExp = "outputFormat ((text)|(KML))";
		String outputExp = "output stdout";
		String outputExpFile = "output file [a-z A-Z 0-9]+(\\.txt|\\.kml)";
		String exitAndSaveExp = "exitAndSave";
		String loadExp = "load";
		String quitExp = "quit";
		
		if(Pattern.matches(helpExp, command)) {
			System.out.println(HELP_MESSAGE);
			return false;
		}
		
	
		if(Pattern.matches(addAirExp,command)){
			String[] res = command.split(" ");
			airportC.addAirport(res[2],new Double(res[3]),new Double(res[4]));
			return false;
		}
	
		else if(Pattern.matches(addAllAirExp,command)){
			String[] res = command.split(" ");
			f.readAirports(res[3]);
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
			String[] res = command.split(" ");
			flightC.addFlight(res[2], res[3], res[4],res[5], res[6],res[7], res[8], new Double(res[9]));
			return false;
		}
		
		else if(Pattern.matches(delFlExp, command)){
			flightC.deleteFlight(command);
			return false;
		}
	
		else if(Pattern.matches(addAllFlExp, command)){
			String[] res = command.split(" ");
			f.readFlights(res[3]);
			return false;
		}
	
		else if(Pattern.matches(delAllFlExp, command)){
			flightC.deleteFlights();
			return false;
		}
	
		else if(Pattern.matches(findRouteExp, command)){
			String[] res = command.split(" ");
			String source = res[1].split("=")[1];
			String target = res[2].split("=")[1];
			String p = res[3].split("=")[1];
			
			RoutePriority priority;
			if(output==null || outputFormat == null){
				if(output !=null)
					System.out.println("Falta indicar el formato de salida");
				else
					System.out.println("Falta indicar donde generar la salida");
				return false;
			}
			if(p.equals("ft"))
				priority = RoutePriority.TIME;
			else if (p.equals("pr"))
				priority = RoutePriority.PRICE;
			else
				priority = RoutePriority.TOTALTIME;
				
			if(res.length == 5){
				String[] days = res[4].split("=");
				days = days[1].split("-");
				if(!Day.checkDays(days)){
					System.out.println("Ingreso dias repetidos");
					return false;
				}
				List<Day> newDays = Day.getDays(days); 
				AirportManager.getInstance().findRoute(source,target,priority,newDays, outputFormat, output);
			}else
				AirportManager.getInstance().findRoute(source,target,priority,Day.getAllDays(), outputFormat, output);
			return false;
		}
		
		else if(Pattern.matches(outputFormatExp, command)){
			String[] strings = command.split(" ");
			if(strings[1].equals("text")) {
				outputFormat = OutputFormat.TEXT;
			} else {
				outputFormat = OutputFormat.KML;
			}
			return false;
		}
		else if(Pattern.matches(outputExp, command)){
			output = "stdout";
		}
		else if(Pattern.matches(outputExpFile, command)){
			String[] strings = command.split(" ");
			String[] strings2 = strings[2].split("\\.");
			String extension = strings2[1];
			
			
				if(extension.equals("kml") && outputFormat.equals(OutputFormat.TEXT)) {
					System.out.println("Error: el nombre de archivo termina con .kml pero el formato esta seteado en texto.");
					return false;
				}
				if(extension.equals("txt") && outputFormat.equals(OutputFormat.KML)) {
					System.out.println("Error: el nombre de archivo termina con .txt pero el formato esta seteado en kml.");
					return false;
				}
				output = strings[2];
			
		
			return false;
		}
		else if(Pattern.matches(loadExp, command)) {
			f.load(AIRPORTFILE, FLIGHTFILE);
		}
		else if(Pattern.matches(exitAndSaveExp, command)){
			f.deleteExistingAirportFile(AIRPORTFILE);
			f.deleteExistingFlightFile(FLIGHTFILE);
			f.save(AIRPORTFILE, FLIGHTFILE);
			return true;	// true = exit
		}
		else if(Pattern.matches(quitExp, command)){
			System.out.println("Saliendo....");
			return true;	// true = exit
		}
		else
			System.out.println("Ingreso un comando no valido");

		return false;
	}
	


	
	public static boolean parseArguments(String[] args) throws ClassNotFoundException, IOException {
		AirportCreator airportC = new AirportCreator();
		FlightCreator flightC = new FlightCreator();
		FileManager f = new FileManager();

		if(args.length == 1) {
			if(args[0].equals("--delete-airports")) {
				f.load(AIRPORTFILE, FLIGHTFILE);
				airportC.deleteAirports();
			}
			else if(args[0].equals("--delete-flights")) {
				f.load(AIRPORTFILE, FLIGHTFILE);
				flightC.deleteFlights();
			}
			else {
				return false;
			}
		}
		else if(args.length == 3) {
			if(args[0].equals("--airport-file")) {
				if(!Pattern.matches("[a-z A-Z 0-9]+\\.txt", args[1]))
					return false;
				if(args[2].equals("--append-airports")) {
					f.load(AIRPORTFILE, FLIGHTFILE);
					f.readAirports(args[1]);
				}
				else if(args[2].equals("--replace-airports")) {
					f.load(AIRPORTFILE, FLIGHTFILE);
					airportC.deleteAirports();
					f.readAirports(args[1]);
				}
				else {
					return false;
				}
			}
			else if(args[0].equals("--flight-file")) {
				if(!Pattern.matches("[a-z A-Z 0-9]+\\.txt", args[1]))
					return false;
				if(args[2].equals("--append-flights")) {
					f.load(AIRPORTFILE, FLIGHTFILE);
					f.readFlights(args[1]);
				}
				else if(args[2].equals("--replace-flights")) {
					f.load(AIRPORTFILE, FLIGHTFILE);
					flightC.deleteFlights();
					f.readFlights(args[1]);
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

