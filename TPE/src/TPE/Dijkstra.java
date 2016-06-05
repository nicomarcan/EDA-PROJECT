package TPE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


/*
 * Algoritmo de Dijsktra con min-priority queue. Use el PriorityQueue de java por ahora, se puede cambiar.
 * 
 * El algoritmo crea una instancia de Vertex para cada aeropuerto y luego le agrega a cada vertex su lista de vuelos 
 * salientes, asi cuando el algoritmo se para en cada vertex solo visita los vertex que tiene de destino.
 * 
 * Calcula la distancia hacia vertex vecinos una vez para cada vuelo saliente (quizas mas de un vuelo para el mismo
 * vecino) y le guarda a ese vertex vecino el vuelo de procedencia optimo que se uso para llegarle si hubo. Al final 
 * reconstruye el camino desde el ultimo vertex hacia atras usando los vuelos de procedencia que guardo en cada vertex.
 * 
 * Usa el mismo algoritmo para los 3 casos de prioridad, pero la de TotalTime es un poco distinta. Al tiempo del vuelo le 
 * suma el tiempo de espera desde que aterrizo el vuelo de procedencia hasta que despega el otro (tomando el dia mas 
 * cercano). falta hacer los dos metodos q calculan ese tiempo de espera, estan en Vertex.
 * 
 * Falta agregar que para el primer vertex solo considere vuelos en los dias especificados por comando, si les parece
 * q esta bien este dijsktra mas tarde lo agrego.
 */
public class Dijkstra {
	
	private RoutePriority priority;
	private Vertex startingVertex;
	private Vertex finalVertex;
	private Set<Flight> flights = new HashSet<Flight>();
	private Set<Airport> airports = new HashSet<Airport>();
	private Queue<Vertex> unvisitedVertexes = new PriorityQueue<Vertex>();
	private Map<Airport, Vertex> airportToVertex = new HashMap<Airport, Vertex>();

	AirportManager airportManager = AirportManager.getInstance();

	/* arma un vertex para cada airport, luego a cada vertex le agrega los flights q le salen */
	public Dijkstra(Airport sourceAirport, Airport destinationAirport, RoutePriority priority) {
		this.priority = priority;
		//this.departureDays = departureDays;
		this.flights = airportManager.getFlightsDijkstra(); // no implementado
		this.airports = airportManager.getAirportsDijkstra(); // no implementado
		
		for(Airport airport : airports) {
			Vertex vertex = new Vertex(airport);
			unvisitedVertexes.add(vertex);
			airportToVertex.put(airport, vertex);
		}
		
		for(Flight flight : flights) {
			Airport origin = flight.getOriginAirport();
			airportToVertex.get(origin).addFlight(flight);
		}
		
		this.startingVertex = airportToVertex.get(sourceAirport);
		this.finalVertex = airportToVertex.get(destinationAirport);
	}
	
	public List<Flight> findRoute() {
		Vertex currentVertex = null;
		Vertex destinationVertex = null;
		Double alternateDistance;	// distance puede ser time o price

		updateDistance(startingVertex, (double) 0);
		
		while(!unvisitedVertexes.isEmpty()) {
			currentVertex = unvisitedVertexes.poll();	// agarra el primero del priority queue (menor distancia)
			
			if(currentVertex != finalVertex) {
				unvisitedVertexes.remove(currentVertex);
				
				for(Flight flight : currentVertex.getFlights()) {
					
					if(priority.equals(RoutePriority.PRICE)) {
						alternateDistance = currentVertex.getTotalDistance() + flight.getPrice();
					} 
					else if(priority.equals(RoutePriority.TIME)) {
						alternateDistance = (double) (currentVertex.getTotalDistance() + flight.getFlightTime());
					}
					else {   /* RoutePriority.TOTALTIME */
						alternateDistance = (double) (currentVertex.getTotalDistance() + flight.getFlightTime()
								+ currentVertex.getWaitTimeTillFlight(flight));
					}
					
					destinationVertex = getDestinationVertex(flight);
					
					if(alternateDistance < destinationVertex.getTotalDistance()) {
						updateDistance(destinationVertex, alternateDistance);
						destinationVertex.setSourceFlight(flight);
						
						if(priority.equals(RoutePriority.TOTALTIME)) {
							destinationVertex.setSourceFlightDepartureDay(currentVertex.getFlightDepartureDay(flight));
							// esto solo hace falta para calcular el tiempo de espera en totaltime
						}				
						
					}
				}
			}
		}
		
		if(finalVertex.getSourceFlight() == null) {
			return null; // no hay ruta
		}
		
		return constructRouteToDestination(finalVertex);
	}
	
	/* construye el camino agarrando sourceflights desde el vertex final hacia atras */
	public List<Flight> constructRouteToDestination(Vertex destination) {
		List<Flight> route = new LinkedList<Flight>();
		Vertex current = destination;
		
		while(current.getSourceFlight() != null) {
			route.add(0, current.getSourceFlight()); // agrega al comienzo de la lista
			current = airportToVertex.get(current.getSourceFlight().getOriginAirport()); // retrocede un nodo
		}
		
		return route;
	}
	
	/* updatea el priority queue cuando se le cambia la distancia a un vertex. nose si hay una mejor manera de hacerlo
	 * con el priorityqueue de java */
	public void updateDistance(Vertex vertex, Double distance) {
		vertex.setTotalDistance(distance);
		unvisitedVertexes.remove(vertex);
		unvisitedVertexes.add(vertex);
	}
	
	/* devuelve el vertex destino de algun flight */
	public Vertex getDestinationVertex(Flight flight) {
		return airportToVertex.get(flight.getDestinationAirport());
	}
	
}