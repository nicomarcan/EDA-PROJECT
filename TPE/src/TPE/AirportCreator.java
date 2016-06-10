package TPE;

/**Esta clase se encarga de crear y borrar aeropuertos a partir de la entrada elegida por el usuario
 * (archivo o entrada estándar)
 */
public class AirportCreator {
	private AirportManager airportM = AirportManager.getInstance();
		
	public void addAirport(String name,Double lat,Double length){
			if(checkLat(lat) && checkLength(length)){		
				airportM.addAirport(new Airport(name,lat,length));
			}
			else{
				System.out.println( "La longitud debe estar entre -180.0 y 180.0");
			}
	}
	
	public void deleteAirport(String command){
		String[] res = command.split(" ");
		String name = res [2];
		airportM.deleteAirport(name);
	}
	
	private boolean checkLength(Double length) {
		if(length > 180.0 || length < -180.0){
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

	public void deleteAirports() {
		airportM.deleteAirports();
	}
}
