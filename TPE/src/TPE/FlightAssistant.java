package TPE;

import java.io.IOException;
import java.util.Scanner;
/**
 * Es la interfaz con la que el usuario se comunica
 *
 */
public class FlightAssistant {
	
	public void start() throws ClassNotFoundException, IOException {	
		boolean quit = false;
		Scanner sc = new Scanner(System.in);
		String input;
		
		System.out.println("Bienvenido al asistente de viajes.");
		
		while(!quit) {
			System.out.println("Ingrese un comando (H para menu):");
			input = sc.nextLine();
			quit = Parser.parseCommand(input);
		}
		
		sc.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		if(args.length != 0){
			if(Parser.parseArguments(args) == false) {
				System.out.println("*Formato invalido de argumentos*");
			}
		}
		
		FlightAssistant fa = new FlightAssistant();
		fa.start();
	}
}

