package TPE;

import java.io.IOException;
import java.util.Scanner;

public class FlightAssistant {
	
	public void start() throws ClassNotFoundException, IOException{	
		boolean quit = false;
		Parser p = new Parser();
		Scanner sc = new Scanner(System.in);
		String c;
		
		while(!quit){
			promptMessage();
			c = sc.nextLine();
			quit = p.parseCommand(c);
		}
		
		sc.close();
	}
		
	private void promptMessage() {
		System.out.println("Bienvenido al asistente de viajes.");
		System.out.println("Ingrese H para ver los comandos.");
		return;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		if(args.length != 0){
			Parser p = new Parser();
			if(p.parseArguments(args) == false) {
				System.out.println("Formato invalido de argumentos");
			}
		}
		
		FlightAssistant fa = new FlightAssistant();
		fa.start();
	}
}

