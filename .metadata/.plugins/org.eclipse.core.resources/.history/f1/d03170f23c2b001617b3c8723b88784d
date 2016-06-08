package TPE;

import java.util.LinkedList;
import java.util.List;

/*
 * Cada vertex corresponde a un aeropuerto y tiene una lista de los vuelos que salen de el.
 * El algoritmo de Dijkstra le guarda al vertex el vuelo que se utilizo para llegar en el camino optimo (sourceFlight).
 */
public class Vertex implements Comparable<Vertex> {
	Airport airport;
	Double totalDistance;  // la distancia total al primer vertice de dijkstra
	Flight sourceFlight; // el mejor flight segun dijkstra para llegar a este vertex
	Day sourceFlightDepartureDay; // para calcular el tiempo de espera para Priority.TOTALTIME
	List<Flight> flights = new LinkedList<Flight>(); // los vuelos que parten desde este vertex
	
	public Vertex(Airport airport) {
		this.airport = airport;
		this.totalDistance = Double.MAX_VALUE;
		this.sourceFlight = null;
	}
	
	public Airport getAirport() {
		return this.airport;
	}
	
	public void addFlight(Flight flight) {
		flights.add(flight);
	}
	
	public List<Flight> getFlights() {
		return this.flights;
	}
	
	public void setSourceFlight(Flight flight) {
		sourceFlight = flight;
	}
	
	public Flight getSourceFlight() {
		return this.sourceFlight;
	}
	
	public void setTotalDistance(Double distance) {
		this.totalDistance = distance;
	}
	
	public Double getTotalDistance() {
		return this.totalDistance;
	}
	
	public void setSourceFlightDepartureDay(Day day) {
		this.sourceFlightDepartureDay = day;
	}
	
	public Day getSourceFlightDepartureDay() {
		return this.sourceFlightDepartureDay;
	}
	
	public Double getWaitTimeTillFlight(Flight flight) {
		// calcula el menor tiempo de espera a partir de el day y time del source flight,
		// y a partir del day mas cercano y time del flight en cuestion
		return null;
	}
	
	public Day getFlightDepartureDay(Flight flight) {
		//devuelve el mejor dia de departure para el flight en cuestion
		return null;
	}
	
	// para el priorityQueue
	public int compareTo(Vertex vertex) {
		Double difference = this.getTotalDistance() - vertex.getTotalDistance();
		
		if(difference < 0) {
			return -1;
		} else if(difference > 0) {
			return 1;
		} else {
			return 0;
		}
	}

}