package TPE;

import java.util.List;

public class AirportCreator {
	private FileManager f = new FileManager();
	
	public void addAirport(String command,AirportManager airportM ){
		String[] res = command.split(" ");	
		String name = res[2];
		Double lat = new Double(res[3]);
		Double length = new Double(res[4]);
		if(checkLat(lat) && checkLength(length)){
			airportM.addAirport(new Airport(name, lat, length));
		}
	}
	
	public void addAirports(List<String> data,AirportManager airportM){
		for(int i = 0;i<data.size();i+=3){
			String name = data.get(i);
			Double lat = new Double(data.get(i+1));
			Double length = new Double(data.get(i+2));
			if(checkLat(lat) && checkLength(length)){
				System.out.println("bien");
				airportM.addAirport(new Airport(name,lat,length));
			}
			else{
				System.out.println("mal");
			}
		}
	}
	
	public void deleteAirport(String command,AirportManager airportM){
		String[] res = command.split(" ");
		String name = res [2];
		airportM.deleteAirport(name);
	}
	
	private boolean checkLength(Double length) {
		if(length > 180.0 || length < -180.0){
			System.out.println( "La longitud debe estar entre -180.0 y 180.0");
			return false;
		}
		return true;
	}

	private boolean checkLat(Double lat) {
		if(lat > 90.0 || lat < -90.0){
			System.out.println("La latitud debe estar entre -90.0 y 90.0");
			return false;
		}
		return true;
	}

	public void deleteAirports(AirportManager airportM) {
		//airportM.deleteAllAirports();
	}
}