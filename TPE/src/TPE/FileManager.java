package TPE;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileManager {

	
	public List<String> readFlights(String file){
		return null;
	}
	public boolean writeRoute(List<Flight> route,String file){
		return true;
		
	}
	
	  public  List<String> readAirports(String name) throws IOException, ClassNotFoundException {
	        File toRead = new File(name);
	        Scanner sc = new Scanner(toRead);
	        List<String> res = new LinkedList<String>();
	        while(sc.hasNext()){
	        	String s = sc.nextLine();
	        	String format = "[a-z A-Z]{3}#-?[0-9]+\\.[0-9]+#-?[0-9]+\\.[0-9]+";
	        	if(!Pattern.matches(format, s)){
	        		System.out.println("formato no valido");
	        	}
	        	res.addAll(split(s)); 
	        }
	        sc.close();
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