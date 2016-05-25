package TPE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileManager {

	
	public List<String> readFlights(String file) throws FileNotFoundException{
		File toRead = new File("F:/git/eda-2016-04/TPE/src/Datos",file);
        Scanner sc = new Scanner(toRead);
        List<String> res = new LinkedList<String>();
        while(sc.hasNextLine()){
        	String s = sc.nextLine();
        	String format = "[a-z A-Z]{1,3}#[0-9]{1,7}#(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*#[a-z A-Z]{3}#[a-z A-Z]{3}#([0-1][0-9]|2[0-4]):[0-5][0-9]#([1-9]h|[1-9][0-9]h])?[0-5][0-9]m$";
	        if(!Pattern.matches(format, s)){
	        	System.out.println("formato no valido");
	        }
	        res.addAll(split(s)); 	        		        	
        }
        return res;
	}
	public boolean writeRoute(List<Flight> route,String file){
		return true;
		
	}
	
	  public  List<String> readAirports(String name) throws IOException, ClassNotFoundException {
	        File toRead = new File("F:/git/eda-2016-04/TPE/src/Datos",name);
	        Scanner sc = new Scanner(toRead);
	        List<String> res = new LinkedList<String>();
	        while(sc.hasNextLine()){
	        	String s = sc.nextLine();
	        	String format = "[a-z A-Z]{3}#-?[0-9]+\\.[0-9]+#-?[0-9]+\\.[0-9]+$";
		        if(!Pattern.matches(format, s)){
		        	System.out.println("formato no valido");
		        }
		        res.addAll(split(s)); 	        		        	
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
