package com.sjsu.nimbleshop;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class LocationTemplate extends AppCompatActivity {

    static GoogleMap googleMap;
    //SharedPreferences sharedPreferences;
    int locationCount = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    ArrayList<LatLng> locations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_template);
        Intent i = getIntent();
        locations =  i.getParcelableArrayListExtra("locations");
        System.out.println("locations are"+locations.get(0));

    }

    public  void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                createMarker();
            }
        }



    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void createMarker() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            dialog.show();

        } else { // Google Play Services are available

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            googleMap.setMyLocationEnabled(true);

            getCurrentLocation();
            // Drawing marker on the map
            locations.add(0,new LatLng(location.getLatitude(), location.getLongitude()));


            int k;
            for(k = 0;k<locations.size();k++){
                drawMarker(new LatLng(Double.parseDouble(String.valueOf(locations.get(k).latitude)), Double.parseDouble(String.valueOf(locations.get(k).longitude))), k);
                if( k == 0)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(String.valueOf(locations.get(k).latitude)), Double.parseDouble(String.valueOf(locations.get(k).longitude))),10));

            }

            addLines(locations);

            // Setting the zoom level in the map on last position  is clicked
            googleMap.animateCamera(CameraUpdateFactory.zoomTo((float) 13));
        }



        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void addLines(ArrayList<LatLng> locations) {
        String fullLocation = "";
        for (int i = 0; i < locations.size(); i++) {


            fullLocation = fullLocation + locations.get(i);
            if (i < locations.size() - 1) {
                fullLocation = fullLocation + ",";
            }
        }

        PolylineOptions points = new PolylineOptions();

        points.addAll(locations);
         points.width(15).color(Color.BLUE).geodesic(true);
        googleMap.addPolyline(points);



    }

    private void drawMarker(LatLng point, int index) {
        // Creating an instance of MarkerOptions

        System.out.println("In Drwamarker");
        String name="";
        if(index == 0){name="Your here";}
        else
        {
            name = "Store"+index;
        }

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point).title(name);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
        //markerOptions.title(name);
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    Location location = null;
    private void getCurrentLocation()
    {
        System.out.println(isGooglePlayServicesAvailable());
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        System.out.println("location manager is:::::"+locationManager);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        /*location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        System.out.println("network enabled iss:::::"+isNetworkEnabled);*/
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        System.out.println("Location"+location);

        //locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    public void mapview(View view) {


        // ArrayList from = (ArrayList) listValue.get(3);
        //ArrayList toLoctn = (ArrayList) listValue.get(4);


        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr=" + locations.get(1).latitude + "," + locations.get(1).longitude;
        System.out.println(uri);
        //System.out.println(from.get(0).getClass().getName());
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        i.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(i);
    }

}
