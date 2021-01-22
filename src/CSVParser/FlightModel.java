package CSVParser;

public class FlightModel {
	public String date;
	public String company;
	public String originAirportId;
	public String originCity;
	public String destAirportId;
	public String destCity;
	public String depTime;
	public String arrTime;
	
	@Override
	public String toString() {
		return "Date: " + date + " | Air Company: " + company + " | originAirportId: " + originAirportId +
				" | originCity: " + originCity + " | destAirportId: " + destAirportId + " | destCity: " +
				destCity + " | depTime: " + depTime + " | arrTime: " + arrTime;
	}
}
