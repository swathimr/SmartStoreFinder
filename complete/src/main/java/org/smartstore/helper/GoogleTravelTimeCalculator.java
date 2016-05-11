package org.smartstore.helper;

import org.joda.time.DateTime;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

public class GoogleTravelTimeCalculator implements TravelTimeCalculator{

	@Override
	public long getTravelTime(String userLong, String userLat, double destLat, double destLong) {

		GeoApiContext context = new GeoApiContext();
		context.setApiKey("AIzaSyCrR_pX2va9EkyMuH7TFYL1pVkBMnk2iYE");

		try {

			double userLatD = new Double(userLong).doubleValue();
			double userLongD = new Double(userLat).doubleValue();

			DistanceMatrix mat = DistanceMatrixApi.newRequest(context).origins(new LatLng(userLatD, userLongD))
					.destinations(new LatLng(destLat, destLong)).mode(TravelMode.DRIVING).departureTime(new DateTime())
					.await();

			for (DistanceMatrixRow d : mat.rows) {
				if (null != d) {
					for (DistanceMatrixElement de : d.elements) {
						if (null != de) {
							return (de.durationInTraffic != null ? de.durationInTraffic.inSeconds
									: ( null != de.duration ? de.duration.inSeconds : 0) );
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

}
