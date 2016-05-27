package TPE;

import java.io.IOException;
import java.util.Scanner;

public class FlightAssisstant {
	
	
	public void start(AirportManager airportM) throws ClassNotFoundException, IOException{	
		boolean quit = false;
		Parser p = new Parser();
		Scanner sc = new Scanner(System.in);
		String c;
		//while(!quit){
			printScreen();
			c = sc.nextLine();
			quit = p.parse(c, airportM);
	//	}
		sc.close();
	}
		
	
	private void printScreen() {
		System.out.println("Bienvenido al asistente de viajes");
		System.out.println("Apreta H para ver los comandos");
		return;
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		AirportManager airportM = new AirportManager();
		if(args.length != 0){
			Parser p = new Parser();
			p.parseArguments(args);
		}
		FlightAssisstant fa = new FlightAssisstant();
		fa.start(airportM);
	}
}
