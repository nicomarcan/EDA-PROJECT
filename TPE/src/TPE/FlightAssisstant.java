package TPE;

import java.io.IOException;
import java.util.Scanner;

public class FlightAssisstant {
	
	
	public void start() throws ClassNotFoundException, IOException{
		AirportManager airportM = new AirportManager();
		boolean quit = false;
		Parser p = new Parser();
		Scanner sc = new Scanner(System.in);
		String c;
		//while(!quit){
			printScreen();
			c = sc.nextLine();
			p.parse(c, airportM);
	//	}
		sc.close();
	}
		
	
	private void printScreen() {
		System.out.println("Bienvenido al asistente de viajes");
		System.out.println("Apreta H para ver los comandos");
		return;
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		if(args.length != 0){
			
		}
		FlightAssisstant fa = new FlightAssisstant();
		fa.start();
	}
}
