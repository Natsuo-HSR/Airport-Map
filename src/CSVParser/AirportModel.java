package CSVParser;

public class AirportModel {
	public String id;
	public String name;
	public String city;
	@Override
	public String toString() {
		return "ID: " + id + " | Airport: " + name+ " | City: " + city;
	}
}
