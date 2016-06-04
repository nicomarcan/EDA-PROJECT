package TPE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileManager {

	public boolean writeRoute(List<Flight> route, String nameFile, boolean stdout, String format){
		String kmlFormat = "kml";
		String txtFormat = "txt";
		String newLine = System.getProperty("line.separator");
		
		double price = 0;
		int flightTime = 0;
		
		for(Flight fl: route){
			price += fl.getPrice();
			flightTime += fl.getFlightTime();
		}
		
		int hoursFlight = flightTime/60;
		int minutesFlight = flightTime%60;
		
		if(Pattern.matches(txtFormat, format)) {
			if(stdout) {
				System.out.println("Precio#" + price);
				System.out.println("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m");
				for(Flight fl: route){
					System.out.println(fl.getOrigin() + "#" + /* aerolinea + "#" + codigo de vuelo + */ "#" + fl.getTarget());
				}
			} else {
				try {
					File toWrite = new File("F:/git/eda-2016-04/TPE/src/Datos",nameFile+ "." + format);
					FileWriter writer = new FileWriter(toWrite, true);
					writer.write("Precio#" + price + newLine);
					writer.write("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m" + newLine);
					for(Flight fl: route){
						writer.write(fl.getOrigin() + "#" + /* aerolinea + "#" + codigo de vuelo + */ "#" + fl.getTarget() + newLine);
					}
					writer.close();
				} catch (IOException e) {
					System.out.println("NotFound");
					return false;
				}
			}
		} else if(Pattern.matches(kmlFormat, format)){
			if(stdout) {
				System.out.println("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>");
				System.out.println("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">");
				System.out.println("<Placemark>");
				System.out.println("<Precio> " + price + " </Precio>");
				System.out.println("<TiempoVuelo> " + hoursFlight + "h" + minutesFlight + "m" + " </TiempoVuelo>");
				System.out.println("<TiempoTotal> " + /*hoursFlight + "h" + minutesFlight + "m" + */ " </TiempoTotal>");
				System.out.println("<Ruta>");
				for(Flight fl: route){
					System.out.println("<Origen> " + fl.getOrigin() + " </Origen>");
					System.out.println("<Aeroliena> " + /* aerolinea + */ " </Aeroliena>");
					System.out.println("<NroVuelo> " + /* codigo de vuelo + */ " </NroVuelo>");
					System.out.println("<Destino> " + fl.getTarget() + " </Destino>");
				}
				System.out.println("</Ruta>");
				System.out.println("</Placemark>");
				System.out.println("</kml>");
			} else {
				try {
					File toWrite = new File("F:/git/eda-2016-04/TPE/src/Datos",nameFile+ "." + format);
					FileWriter writer = new FileWriter(toWrite, true);
					writer.write("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>" + newLine);
					writer.write("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">" + newLine);
					writer.write("<Placemark>" + newLine);
					writer.write("<Precio> " + price + " </Precio>" + newLine);
					writer.write("<TiempoVuelo> " + hoursFlight + "h" + minutesFlight + "m" + " </TiempoVuelo>"  + newLine);
					writer.write("<TiempoTotal> " + /*hoursFlight + "h" + minutesFlight + "m" + */ " </TiempoTotal>"  + newLine);
					writer.write("<Ruta>" + newLine);
					for(Flight fl: route){
						writer.write("<Origen> " + fl.getOrigin() + " </Origen>" + newLine);
						writer.write("<Aeroliena> " + /* aerolinea + */ " </Aeroliena>" + newLine);
						writer.write("<NroVuelo> " + /* codigo de vuelo + */ " </NroVuelo>" + newLine);
						writer.write("<Destino> " + fl.getTarget() + " </Destino>" + newLine);
					}
					writer.write("</Ruta>" + newLine);
					writer.write("</Placemark>" + newLine);
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
	
	public List<String> readFlights(String file) throws FileNotFoundException{
		File toRead = new File("F:/git/eda-2016-04/TPE/src/Datos",file);
		List<String> res = new LinkedList<String>();
		try {
			Scanner sc = new Scanner(toRead);
	        while(sc.hasNextLine()){
	        	String s = sc.nextLine();
	        	String format = "[a-z A-Z]{1,3}#[0-9]{1,7}#(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*#[a-z A-Z]{3}#[a-z A-Z]{3}#([0-1][0-9]|2[0-4]):[0-5][0-9]#([1-9]h|[1-9][0-9]h])?[0-5][0-9]m$";
		        if(!Pattern.matches(format, s)){
		        	System.out.println("formato no valido");
		        }
		        res.addAll(split(s)); 	        		        	
	        }
		} catch (Exception e) {
			System.out.println("NotFound");
		}
		
        return res;
	}
	
	public  List<String> readAirports(String name) throws IOException, ClassNotFoundException {
	        File toRead = new File("C:/Users/Usuario/Documents/eda-2016-04/TPE/src/Datos",name);
	        List<String> res = new LinkedList<String>();
	        try {
	        	Scanner sc = new Scanner(toRead);
	        	while(sc.hasNextLine()){
		        	String s = sc.nextLine();
		        	String format = "[a-z A-Z]{3}#-?[0-9]+\\.[0-9]+#-?[0-9]+\\.[0-9]+$";
			        if(!Pattern.matches(format, s)){
			        	System.out.println("formato no valido");
			        }
			        res.addAll(split(s)); 	        		        	
		        }
			} catch (IOException e) {
				System.out.println("NotFound");
			} 
	        return res;
	    }
	
	  
	  private List<String> split(String s) {
		String[] a = s.split("#");
		List<String> res = new LinkedList<String>();
    	for(int i = 0; i < a.length;i++){
    		res.add(a[i]);
    	}
    	return res;
	}
}
