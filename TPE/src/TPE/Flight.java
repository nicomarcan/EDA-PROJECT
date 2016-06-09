package TPE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Flight {

	private String airline;
	private String flightNumber;
	private List<Day> days;
	private String target;
	private Double price;
	private Integer flightTime;
	
	/** Dia en el que se esta usando el vuelo**/
	private int currentDayIndex;
	
	




	public int getCurrentDayIndex() {
		return currentDayIndex;
	}



	public void setCurrentDayIndex(int currentDayIndex) {
		this.currentDayIndex = currentDayIndex;
	}



	public Double getPrice() {
		return price;
	}

	

	public String getAirline() {
		return airline;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public Integer getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(int flightTime) {
		this.flightTime = flightTime;
	}

	public int getDepartureTime() {
		return departureTime;
	}

	private int departureTime;
	private String origin;
	
	public Flight(String airline, String flightNumber, List<Day> days, String origin, String target,
			int departureTime, int flightTime, double price) {
		super();
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.days = days;
		this.target = target;
		this.price = price;
		this.flightTime = flightTime;
		this.departureTime = departureTime;
		this.origin = origin;
	}

	public String getTarget() {
		return target;
	}

	public String getOrigin() {
		return origin;
	}
	public Flight clone(){
		Flight ans = new Flight(airline, flightNumber, days, origin, target, departureTime, flightTime, price);
		return ans;
	}



	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airline == null) ? 0 : airline.hashCode());
		result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (airline == null) {
			if (other.airline != null)
				return false;
		} else if (!airline.equals(other.airline))
			return false;
		if (flightNumber == null) {
			if (other.flightNumber != null)
				return false;
		} else if (!flightNumber.equals(other.flightNumber))
			return false;
		return true;
	}

	public List<Day> getDays() {
		return days;
	}

	public String toString(){
		return airline+" "+flightNumber+" "+origin+" "+target+" "+currentDayIndex;
	}


	// Dummies para el Dijkstra
	public Airport getOriginAirport() { return null; }
	public Airport getDestinationAirport() { return null; }
}
