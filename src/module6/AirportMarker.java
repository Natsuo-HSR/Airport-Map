package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	private int routsNumber;
		
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(255, 165, 0);
		pg.stroke(0, 0, 0);
		pg.strokeWeight(1);
		pg.ellipse(x, y, 7, 7);
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		String name = "Name: " + getName();
		pg.pushStyle();
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-41, pg.textWidth(name) + 6, 17);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-7-33);
		
		pg.popStyle();
	}
	
	private String getName() {
		return getProperty("name").toString();
	}
}
