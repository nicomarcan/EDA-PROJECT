package TPE;



public class Flight{
	private String airline;
	private int flightNumber;
	private Day[] days;
	private Airport target;
	private int price;
	private int flightTime;
	private int departureTime;
	private Airport origin;
	
	public Flight(String airline, int flightNumber, Day[] days, Airport target, int price, int flightTime,
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
		result = prime * result + flightNumber;
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
		if (flightNumber != other.flightNumber)
			return false;
		return true;
	}
		
	
}