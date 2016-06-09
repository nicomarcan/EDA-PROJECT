package TPE;

import java.io.Serializable;


public class Airport {
	
	private String name;
	private Double lat;
	private Double length;
	
	
	public Airport(String name, double lat, double length) {
		super();
		this.name = name;
		this.lat = lat;
		this.length = length;
	}	
	
	public String getName() {
		return name;
	}

	public String toString(){
		return name+" "+lat+" "+length;
	}
	
	public Double getLatitude() {
		return lat;
	}
	
	public Double getLongitude() {
		return length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Airport other = (Airport) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
