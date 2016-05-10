package org.smartstore.helper;

public interface TravelTimeCalculator {
	
	long getTravelTime(String userLong, String userLat, double destLat, double destLong);

}
