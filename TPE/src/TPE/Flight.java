package TPE;

import java.util.ArrayList;
import java.util.List;

public class Flight {
	private String airline;
	private String flightNumber;
	private ArrayList<Day> days;
	private Airport target;
	private Integer price;
	private Integer flightTime;
	
	
	public Integer getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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
	private Airport origin;
	
	public Flight(String airline, String flightNumber, ArrayList<Day> days, Airport target, int price, int flightTime,
			int departureTime, Airport origin) {
		super();
		airline = airline;
		this.flightNumber = flightNumber;
		this.days = days;
		this.target = target;
		this.price = price;
		this.flightTime = flightTime;
		this.departureTime = departureTime;
		this.origin = origin;
	}

	public Airport getTarget() {
		return target;
	}

	public Airport getOrigin() {
		return origin;
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



	
		
	
}
