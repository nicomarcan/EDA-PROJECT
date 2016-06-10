

package TPE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import TPE.AirportManager.Node;

public class FileManager {

	public void save(String outputAirports, String outputFlights) {
		String newLine = System.getProperty("line.separator");
		AirportManager manager = AirportManager.getInstance();
		
		if(manager.getAirportsDijkstra().isEmpty()) {
			System.out.println("NotFound");
		} else {
			try {//C:/Users/SantiagoPC/git/eda-2016-04/TPE/src/Datos
				File airportFile = new File("C:/Users/Marcos/git/eda-2016-042/TPE/src/Datos",outputAirports);
				FileWriter AirportWriter = new FileWriter(airportFile, true);
				
				for(Node air : manager.getAirportsDijkstra()){
					AirportWriter.write(air.airport.getName() + "#" + air.airport.getLatitude() + "#" + air.airport.getLongitude() + newLine);
				}
				AirportWriter.close();//C:/Users/SantiagoPC/git/eda-2016-04/TPE/src/Datos
				if(!manager.getFlights().values().isEmpty()) {
					File flightFile = new File("C:/Users/Marcos/git/eda-2016-042/TPE/src/Datos",outputFlights);
					FileWriter flightWriter = new FileWriter(flightFile, true);
					manager = AirportManager.getInstance();
					for(Flight fl : manager.getFlights().values()){
						flightWriter.write(fl.getAirline() + "#" + fl.getFlightNumber() + "#" + Day.getDays(fl.getDays())/*days*/ + "#" + fl.getOrigin() + "#" + fl.getTarget() + "#" + getDepartureTimeFormat(fl.getDepartureTime())/*dt*/ + "#" + getFlightTimeFormat(fl.getFlightTime())/*ft*/ + "#" + fl.getPrice() + newLine);
					}
					flightWriter.close();
				} else {
					System.out.println("NotFound");
				}
				
			} catch (IOException e) {
				System.out.println("NotFound");
			}
		}
		
		return;
	}
	
	private String getDepartureTimeFormat(int t) {
		Integer hours = t/60;
		Integer minutes = t%60;
		String s = new String();
		
		if(hours < 10) {
			s = s.concat("0" + hours.toString() + ":");
		} else {
			s = s.concat(hours.toString() + ":");
		}
		if(minutes < 10) {
			s = s.concat("0" + minutes.toString());
		} else {
			s = s.concat(minutes.toString());
		}
		return s;
	}
	
	private String getFlightTimeFormat(int t) {
		Integer hours = t/60;
		Integer minutes = t%60;
		String s = new String();
		
		if(hours > 0) {
			s = s.concat(hours.toString() + "h");
		}
		if(minutes < 10) {
			s = s.concat("0" + minutes.toString() + "m");
		} else {
			s = s.concat(minutes.toString() + "m");
		}
		return s;
	}
	
	public void load(String airportFile, String flightFile) throws ClassNotFoundException, IOException {
		readAirports(airportFile);
		readFlights(flightFile);
	}
	public boolean writeRoute(List<Flight> route, String output, OutputFormat outputFormat){
		if(route == null) {
			System.out.println("NotFound");
			return false;
		}
		String newLine = System.getProperty("line.separator");
		double price = 0;
		int flightTime = 0;
		int totalTime = 0;/** falta hacer esto**/
		for(Flight fl: route){
			price += fl.getPrice();
			flightTime += fl.getFlightTime();
		}
		
		int hoursFlight = flightTime/60;
		int minutesFlight = flightTime%60;
		
		if(outputFormat.equals(OutputFormat.TEXT)) {
			if(output.equals("stdout")) {
				System.out.println("Precio#" + price);
				System.out.println("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m");
				for(Flight fl: route){
					System.out.println(fl.getOrigin() + "#" + fl.getAirline() + "#" + fl.getFlightNumber()+ "#" + fl.getTarget()+"#"+fl.getDays());
				}
			} else {
				try {
					File toWrite = new File("C:/Users/SantiagoPC/git/eda-2016-04/TPE/src/Datos",output);
					FileWriter writer = new FileWriter(toWrite, true);
					writer.write("Precio#" + price + newLine);
					writer.write("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m" + newLine);
					for(Flight fl: route){
						writer.write(fl.getOrigin() + "#" + fl.getAirline() + "#" + fl.getFlightNumber() + "#" + fl.getTarget() + newLine);
					}
					writer.close();
				} catch (IOException e) {
					System.out.println("NotFound");
					return false;
				}
			}
		} else if(outputFormat.equals(OutputFormat.KML)){
			if(output.equals("stdout")) {
				System.out.println("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>");
				System.out.println("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">");
				System.out.println("<Document>");	// no se si hace falta este
				// TODO el tiempo vuelo y tiempo total en la descripcion estan iguales
				System.out.println("<description>" + "Precio: " + price + " TiempoVuelo: " + hoursFlight + "h" + minutesFlight + "m" + " TiempoTotal: " + hoursFlight + "h" + minutesFlight + "m" + "</description>");
				// El primer aeropuerto
				System.out.println("<Placemark>");
				System.out.println("<name>" + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getName() + "</name>");
				System.out.println("<Point>");
				System.out.println("<Description></Description>");
				System.out.println("<coordinates>" + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0" + "</coordinates>");
				System.out.println("</Point>");
				System.out.println("</Placemark>");
				for(Flight fl : route) {
					// El vuelo
					System.out.println("<Placemark>");
					System.out.println("<name>" + fl.getAirline() + "#" + fl.getFlightNumber() + "</name>");
					System.out.println("<LineString>");
					System.out.println("<tessellate>0</tessellate>");
					System.out.println("<coordinates>" + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0");
					System.out.println(AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>");
					System.out.println("</LineString>");
					System.out.println("</Placemark>");
					// El aeropuerto destino
					System.out.println("<Placemark>");
					System.out.println("<name>" + fl.getTarget() + "</name>");
					System.out.println("<Point>");
					System.out.println("<Description></Description>");
					System.out.println("<coordinates>" + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>");
					System.out.println("</Point>");
					System.out.println("</Placemark>");
				}
				System.out.println("</Document>");	// no se si hace falta este
				System.out.println("</kml>");
			} else {
				try {
					File toWrite = new File("C:/Users/SantiagoPC/git/eda-2016-04/TPE/src/Datos",output);
					FileWriter writer = new FileWriter(toWrite, true);
					writer.write("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>" + newLine);
					writer.write("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">" + newLine);
					writer.write("<Document>" + newLine);	// no se si hace falta este
					// TODO el tiempo vuelo y tiempo total en la descripcion estan iguales
					writer.write("<description>" + "Precio: " + price + " TiempoVuelo: " + hoursFlight + "h" + minutesFlight + "m" + " TiempoTotal: " + hoursFlight + "h" + minutesFlight + "m" + "</description>" + newLine);
					// El primer aeropuerto
					writer.write("<Placemark>" + newLine);
					writer.write("<name>" + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getName() + "</name>" + newLine);
					writer.write("<Point>" + newLine);
					writer.write("<Description></Description>" + newLine);
					writer.write("<coordinates>" + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
					writer.write("</Point>" + newLine);
					writer.write("</Placemark>" + newLine);
					for(Flight fl : route) {
						// El vuelo
						writer.write("<Placemark>" + newLine);
						writer.write("<name>" + fl.getAirline() + "#" + fl.getFlightNumber() + "</name>" + newLine);
						writer.write("<LineString>" + newLine);
						writer.write("<tessellate>0</tessellate>" + newLine);
						writer.write("<coordinates>" +  AirportManager.getInstance().getAirports().get(fl.getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(fl.getOrigin()).airport.getLongitude() + ",0" + newLine);
						writer.write(AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
						writer.write("</LineString>" + newLine);
						writer.write("</Placemark>" + newLine);
						// El aeropuerto destino
						writer.write("<Placemark>" + newLine);
						writer.write("<name>" + fl.getTarget() + "</name>" + newLine);
						writer.write("<Point>" + newLine);
						writer.write("<Description></Description>" + newLine);
						writer.write("<coordinates>" + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
						writer.write("</Point>" + newLine);
						writer.write("</Placemark>" + newLine);
					}
					writer.write("</Document>" + newLine);	// no se si hace falta este
					writer.write("</kml>" + newLine);
					writer.close();
				} catch (IOException e) {
					System.out.println("NotFound");
					return false;
				} 
			} 
		} else {
			System.out.println("NotFound");
			return false;
		}
		
		return true;	
	}
	
	public  void readFlights(String file) throws FileNotFoundException{
		FlightCreator flightC = new FlightCreator();
		//System.out.println(file);
		File toRead = new File("C:/Users/Marcos/git/eda-2016-042/TPE/src/Datos",file);
		try {
			int i = 1;
			Scanner sc = new Scanner(toRead);
	        while(sc.hasNextLine()){
	        	String s = sc.nextLine();
	        	String format = "[a-z A-Z]{1,3}#[0-9]{1,7}#(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*#[a-z A-Z]{1,3}#[a-z A-Z]{1,3}#([0-1][0-9]|2[0-3]):[0-5][0-9]#([1-9]h|[1-9][0-9]h)?[0-5][0-9]m#[0-9]+\\.[0-9]+$";
		        if(!Pattern.matches(format, s)){
		        	System.out.println("formato no valido, tiene un error en la linea "+i);
		        	System.out.println(s);
		        } else {
		        	String[] res =  s.split("#");
			        flightC.addFlight(res[0], res[1], res[2], res[3], res[4], res[5], res[6], new Double(res[7]));
		        }
		        i++;
		        
		        
		    	        		        	
	        }
		} catch (FileNotFoundException e) {
			System.out.println("NotFound");
		}
		
   
	}
	
	public  void readAirports(String name) throws IOException, ClassNotFoundException{
			AirportCreator airportC = new AirportCreator();
	        File toRead = new File("C:/Users/Marcos/git/eda-2016-042/TPE/src/Datos",name);
	        try {
	        	Scanner sc = new Scanner(toRead);
	        	while(sc.hasNextLine()){
		        	String s = sc.nextLine();
		        	String format = "[a-z A-Z]{3}#-?[0-9]+\\.[0-9]+#-?[0-9]+\\.[0-9]+$";
			        if(!Pattern.matches(format, s)){
			        	System.out.println("formato no valido");
			        	System.out.println(s);
			        } else {
			        	String[] res = s.split("#");
					    airportC.addAirport(res[0], new Double(res[1]), new Double(res[2]));
			        }
		        }
			} catch (IOException e) {
				System.out.println("NotFound");
			} 
	    } 
	
}

