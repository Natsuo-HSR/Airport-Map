package module6;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;

import CSVParser.AirportModel;
import CSVParser.CSVParser;
import CSVParser.FlightModel;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.GeoMapApp;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.ImmoScout;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.MapBox;
import de.fhpotsdam.unfolding.providers.MapQuestProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenMapSurferProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.OpenWeatherProvider;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.providers.ThunderforestProvider;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import controlP5.*;

public class AirportMap extends PApplet {
	
	public UnfoldingMap map;
	public static List<Marker> airportList;
	public static List<Marker> routeList = new ArrayList<Marker>();
	public static List<FlightModel> flights;
	public static List<ShapeFeature> routes;
	public static HashMap<Integer, Location> airports;
	public List<PointFeature> my_features;
	public List<Marker> needToHide = new ArrayList<>();
	public Map<FlightModel, String> listOfFlights = new LinkedHashMap<>();
	public static Map<String, String> nameToCompany = new LinkedHashMap<>();
	public static Map<String, Integer> rating = new LinkedHashMap<>();
	
	static String filePath = "D:\\Программирование\\Eclipse Workspace\\UCSDUnfoldingMaps\\data\\USA_march_scheduled.txt";
	
	public static CommonMarker lastClicked;
	public static CommonMarker lastSelected;
	
	static {
		flights = new ArrayList<>();
		try {
			flights = CSVParser.ParseFlightsCsv(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		airportList = new ArrayList<Marker>();
		airports = new HashMap<Integer, Location>();
		
		//
		nameToCompany.put("AA", "Ametican Airlines");
		nameToCompany.put("DL", "Delta Airlines");
		nameToCompany.put("F9", "Frontier Airlines");
		nameToCompany.put("EV", "Express Jet Airline");
		nameToCompany.put("YX", "Republic Airline");
		nameToCompany.put("9E", "Endlavor Air");
		nameToCompany.put("G4", "Allegrant Air");
		nameToCompany.put("MQ", "Envoy Air");
		nameToCompany.put("UA", "United Air Lines");
		nameToCompany.put("OO", "Skywest Airlines");
		nameToCompany.put("NK", "Spirit Air Lines");
		nameToCompany.put("YV", "Mesa Airlines");
		nameToCompany.put("OH", "PSA Airlines");
		nameToCompany.put("HA", "Hawaiian Airlines");
		nameToCompany.put("B6", "JetBlue Airlines");
		nameToCompany.put("WN", "SouthWest Airlines");
		nameToCompany.put("AS", "Alaska Airlines");
		//
		rating.put("AA", 3);
		rating.put("DL", 2);
		rating.put("F9", 2);
		rating.put("EV", 3);
		rating.put("YX", 3);
		rating.put("9E", 2);
		rating.put("G4", 3);
		rating.put("MQ", 3);
		rating.put("UA", 3);
		rating.put("OO", 3);
		rating.put("NK", 2);
		rating.put("YV", 3);
		rating.put("OH", 2);
		rating.put("HA", 4);
		rating.put("WN", 5);
		rating.put("B6", 4);
		rating.put("AS", 4);
	}
	
	//CP5
	String choice;
	ControlP5 cp5;

	int myColor = color(255);

	int c1,c2;
	DropdownList d1, d2, d3;
	

	float n,n1;
	
	boolean processed = false;

	 boolean on = false;
	
	//ControlP5 cp5;
	Textarea myTextarea;
	CheckBox checkbox;
	RadioButton r;
	Textlabel myTextlabelA;
	Textlabel myTextlabelB;
	Button p;
	//


	
	public void setup() {
		// setting up PAppler
		size(1150, 700, OPENGL);
		
		///CP5
		if (!processed) {
		noStroke();
		 cp5 = new ControlP5(this);
		 
		 
 // Text label
                 myTextlabelA = cp5.addTextlabel("label")
                         .setText("Flight-configurator")
                         .setPosition(150,10)
                         .setColorValue(color(60))
                         .setFont(createFont("Arial",20))
                         ;
		 
		 
		 myTextarea = cp5.addTextarea("txt")
                 .setPosition(10,170)
                 .setSize(400,300)
                 .setLineHeight(20)
                 .setColor(color(255))
                 .setColorBackground(color(60));
                 myTextarea.getCaptionLabel().getStyle().marginTop = 3;
                 myTextarea.getCaptionLabel().getStyle().marginTop = 3;
                 myTextarea.getCaptionLabel().getStyle().marginLeft = 3;
                 myTextarea.getValueLabel().getStyle().marginTop = 3;
                 myTextarea.setVisible(false);
		 
		 
		 d3 = cp5.addDropdownList("myList-d3")
		          .setPosition(10, 85)
		          .setSize(80, 150)
		          .setOpen(false)
		          ;
		 customizeDate(d3);
		 
		 d1 = cp5.addDropdownList("myList-d1")
		          .setPosition(10, 50)
		          .setSize(180, 150)
		          .setOpen(false)
		          ;
		 customizeFrom(d1);
		 
		 d1.onClick(new CallbackListener() {
			@Override
			public void controlEvent(CallbackEvent arg0) {
				if (!d1.getLabel().equals("City (FROM)")) {
				String t = d1.getLabel();
				if (!t.equals(choice)) {
					choice = t;
					d2.clear();
					customizeTo(d2,t);
					d2.setLock(false);
				}
				}
			}
			 
		 });
		 
		 d2 = cp5.addDropdownList("anotherList")
		          .setPosition(230, 50)
		          .setSize(180, 150)
		          .setLock(true)
		          ;
		 customizeTo(d2);
		 
		 d2.onClick(new CallbackListener() {

			@Override
			public void controlEvent(CallbackEvent arg0) {
				myTextarea.setVisible(true);	
				
				String from = d1.getLabel();
				String to = d2.getLabel();
				for (FlightModel f : flights) {
					if (f.originCity.equals(from) && f.destCity.equals(to)) {
						if (f.depTime.length() > 3) {
							String rate = nameToCompany.get(f.company.substring(1, f.company.length() - 1));
							String comp = f.company.substring(1, f.company.length() - 1);
							if (rating.get(comp) > 2) {
								if (rating.get(comp) > 3) {
									rate += "***";
								}
								else rate += "**";
							} else rate += "*";
							String string = "Date: " + f.date + 
									" | Time: " + f.depTime.substring(0,3) + ":" + f.depTime.substring(3,f.depTime.length()) +
									" - " + f.arrTime.substring(0,3) + ":" + f.arrTime.substring(3,f.depTime.length()) +
									" | From: " + f.originCity +
									" | To: " + f.destCity +
									" | Company: " + rate;
							
							myTextarea.append(string + "\n");
							listOfFlights.put(f, string);
						}
					}
				}
				
				
				ShapeFeature route = null;
				
				for (ShapeFeature f : routes) {
					if (f.getProperty("source").equals(from) && f.getProperty("destination").equals(to)) {
						route = f;
						break;
					}
				}
				
				
				Set<String> g = new HashSet<>();
				for (FlightModel a : flights) {
					g.add(a.company);
				}
				
				
				int source = -1;
				int dest = -1;
				for (FlightModel a : flights) {
					if (a.originCity.equals(to)) {
						source = Integer.valueOf(a.originAirportId);
					}
					if (a.originCity.equals(from))
						dest = Integer.valueOf(a.originAirportId);
				}
				
				String nameTo = to.substring(1, to.length() - 1).replaceAll(" ", "").replace(',', '-');
				String nameFrom = from.substring(1, from.length() - 1).replaceAll(" ", "").replace(',', '-');
				
				for (Marker m : airportList) {
					if (!(m.getProperty("city").equals(nameTo)) && !(m.getProperty("city").equals(nameFrom))) {
						needToHide.add(m);
					}
				}
				
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
				SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
				sl.setColor(color(193, 0, 32));
				routeList.add(sl);
				setup();
				
			}
			 
		 });
		 
		 d3.onClick(new CallbackListener() {
				@Override
				public void controlEvent(CallbackEvent arg0) {
					if (!d3.getLabel().equals("Date")) {
					myTextarea.clear();
					Integer day = Integer.parseInt(d3.getLabel());
						for (FlightModel f : listOfFlights.keySet()) {
							Integer dayF = Integer.parseInt(f.date.split("-")[2]);
							if (dayF >= day) {
								myTextarea.append(listOfFlights.get(f) + "\n");
							}
						}
					}
				}
				 
			 });
		
		  cp5.addButton("Clear")
		     .setPosition(200,490)
		     .setSize(30,19)
		     .setColorBackground(color(60))
		     .setValue(0)
		     .onPress(new CallbackListener() {

				@Override
				public void controlEvent(CallbackEvent arg0) {
					processed = false;
					myTextarea.remove();
					listOfFlights.clear();
					routes.clear();
					needToHide.clear();
					setup();
				}
		     });
		     ;
		 
         
		 
		 processed = true;	 

		}
		// setting up map and default events
		//map = new UnfoldingMap(this, 430, 20, 700, 650);
		//map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
		//map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.RoadProvider());
		map = new UnfoldingMap(this, 430, 20, 700, 650, new GeoMapApp.TopologicalGeoMapProvider());
		//map = new UnfoldingMap(this, 430, 20, 700, 650, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		my_features = ParseFeed.parseMyAirports(this, "airports_db.txt");
		
		
		// create markers from features
		for(PointFeature feature : my_features) {
			AirportMarker m = new AirportMarker(feature);
			m.setRadius((float) 0.2);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		}
		
		
		routes = ParseFeed.parseMyRoutes(this, flights);
		
		
		map.addMarkers(routeList);

		map.addMarkers(airportList);
		hideAirports();
	}
	
	
	void customizeTo(DropdownList ddl, String name) {
		  ddl.setBackgroundColor(color(190));
		  ddl.setItemHeight(20);
		  ddl.setBarHeight(20);
		  ddl.getCaptionLabel().set("City (TO)");
		  ddl.getCaptionLabel().getStyle().marginTop = 3;
		  ddl.getCaptionLabel().getStyle().marginLeft = 3;
		  ddl.getValueLabel().getStyle().marginTop = 3;
		  
		  List<String> availableList = new ArrayList<>();
		  for (FlightModel f : flights) {
			  if(name.equals(f.originCity))
				  availableList.add(f.destCity);
		  }
		  Collections.sort(availableList, String.CASE_INSENSITIVE_ORDER);
		  Set<String> finalList = new LinkedHashSet<>(availableList);
		  
		  Iterator<String> it = finalList.iterator();
		  int i = 0;
		  while(it.hasNext()) {
		    ddl.addItem(it.next(), i++);
		  }
		  
		  
		  ddl.setColorBackground(color(60));
		  ddl.setColorActive(color(255, 128));
		}
	void customizeFrom(DropdownList ddl) {
		  // a convenience function to customize a DropdownList
		  ddl.setBackgroundColor(color(190));
		  ddl.setItemHeight(20);
		  ddl.setBarHeight(20);
		  ddl.getCaptionLabel().set("City (FROM)");
		  ddl.getCaptionLabel().getStyle().marginTop = 3;
		  ddl.getCaptionLabel().getStyle().marginLeft = 3;
		  ddl.getValueLabel().getStyle().marginTop = 3;
		  List<String> availableList = new ArrayList<>();
		  for (FlightModel f : flights) {
			  availableList.add(f.originCity);
		  }
		  Collections.sort(availableList, String.CASE_INSENSITIVE_ORDER);
		  Set<String> finalList = new LinkedHashSet<>(availableList);
		  Iterator<String> it = finalList.iterator();
		  
		  int i = 0;
		  while(it.hasNext()) {
		    ddl.addItem(it.next(), i++);
		  }
		  ddl.setColorBackground(color(60));
		  ddl.setColorActive(color(255, 128));
		}
	void customizeTo(DropdownList ddl) {
		  // a convenience function to customize a DropdownList
		  ddl.setBackgroundColor(color(190));
		  ddl.setItemHeight(20);
		  ddl.setBarHeight(20);
		  ddl.getCaptionLabel().set("City (TO)");
		  ddl.getCaptionLabel().getStyle().marginTop = 3;
		  ddl.getCaptionLabel().getStyle().marginLeft = 3;
		  ddl.getValueLabel().getStyle().marginTop = 3;
		  ddl.setColorBackground(color(60));
		  ddl.setColorActive(color(255, 128));
		}
	
	void customizeDate(DropdownList ddl) {
		  // a convenience function to customize a DropdownList
		  ddl.setBackgroundColor(color(190));
		  ddl.setItemHeight(20);
		  ddl.setBarHeight(20);
		  ddl.getCaptionLabel().set("Date");
		  ddl.getCaptionLabel().getStyle().marginTop = 3;
		  ddl.getCaptionLabel().getStyle().marginLeft = 3;
		  ddl.getValueLabel().getStyle().marginTop = 3;
		  
		  
		  for (int i = 1; i < 32; i++) {
			  ddl.addItem(i+"", i - 1);
		  }
		  //ddl.scroll(0);
		  ddl.setColorBackground(color(60));
		  ddl.setColorActive(color(255, 128));
		}
	
	public void draw() {
		background(255);
		map.draw();
		//addKey();
	}
	
	
	
	public void hideAirports() {
		for (Marker m : airportList) {
			for (int i = 0; i < needToHide.size(); i++) {
				if (needToHide.get(i).getProperty("city").equals(m.getProperty("city")))
					m.setHidden(true);
			}
		}
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

		
}
