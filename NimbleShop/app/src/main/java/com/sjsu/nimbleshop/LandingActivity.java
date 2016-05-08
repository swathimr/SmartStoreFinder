package com.sjsu.nimbleshop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,Serializable {

    private TextView info;
    private GoogleMap mMap;
    private LatLngBounds.Builder bounds;
    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private ArrayList placeDetailsCustomList;
    private ArrayList completeList;
    private TextView locationTv;

    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW;
    private AutocompleteFilter autofilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        info = (TextView)findViewById(R.id.info);
        info.setText("Loggedin successfully");

        //gets current location
        getCurrentLocation();


        //autocomplete code

        mGoogleApiClient = new GoogleApiClient.Builder(LandingActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_id);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        //create filter
        List<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_POSTAL_CODE_PREFIX);
        autofilter = AutocompleteFilter.create(filterTypes);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW,autofilter);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //generates bound for autocomplete based on current location
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        LatLng sw = vr.latLngBounds.southwest;
        LatLng ne = vr.latLngBounds.northeast;
        LatLngBounds bounds = new LatLngBounds(sw, ne);
        BOUNDS_MOUNTAIN_VIEW=bounds;
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.clear();
        locationTv = (TextView) findViewById(R.id.latlongLocation);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationTv.setText("Latitude:" + latitude + ": Longitude:" + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
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

    //autocomplete

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            completeList=new ArrayList();

            for(int i=0;i<mPlaceArrayAdapter.mResultList.size();i++)
            {
                System.out.println("for place id:::"+mPlaceArrayAdapter.mResultList.get(i).placeId);
                final String placeIdVal = String.valueOf(mPlaceArrayAdapter.mResultList.get(i).placeId);
                PendingResult<PlaceBuffer> placeResults = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeIdVal);
                placeResults.setResultCallback(mfetchPlaceDetailsCallback);
            }
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    public void getWaitingTimePredictions(View view)
    {
        System.out.println(mPlaceArrayAdapter.mResultList.get(0));

        /*for(int i=0;i<mPlaceArrayAdapter.mResultList.size();i++)
        {
            System.out.println("for place id:::"+mPlaceArrayAdapter.mResultList.get(i).placeId);
            final String placeId = String.valueOf(mPlaceArrayAdapter.mResultList.get(i).placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mfetchPlaceDetailsCallback);
        }*/

        Intent intent= new Intent(this,WaitListPredictionActivity.class);

        //passing the list directly does not work
        //copied the list and sent the value
        ArrayList val=copyList();
        intent.putStringArrayListExtra("working",completeList);
        startActivity(intent);

    }

    public void getTemplateActivity(View view)
    {
        System.out.println("in Template Activity");


        Intent intent= new Intent(this,TemplateActivity.class);
        //intent.putStringArrayListExtra("working",completeList);
        startActivity(intent);

    }


    public ArrayList copyList()
    {
        ArrayList values= new ArrayList();
        for(Object value:mPlaceArrayAdapter.mResultList)
        {
            values.add(value.toString());
        }
        return values;
    }


    private ResultCallback<PlaceBuffer> mfetchPlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            //creates list and inserts into parent list
            placeDetailsCustomList=new ArrayList();
            placeDetailsCustomList.add(place.getName());
            placeDetailsCustomList.add(place.getAddress() + "");
            if(place.getRating()>0) {
                placeDetailsCustomList.add(Float.valueOf(place.getRating()) * 10);
            }
            else
            {
                placeDetailsCustomList.add(0.0);
            }
            String[] currentlocation=locationTv.getText().toString().split(":");
            ArrayList LatLng=new ArrayList();
            LatLng currentlatLng = new LatLng(Double.valueOf(currentlocation[1]),Double.valueOf(currentlocation[3]));
            LatLng.add(0,currentlatLng.latitude);
            LatLng.add(1, currentlatLng.longitude);

            placeDetailsCustomList.add(LatLng);

            ArrayList destLatLng=new ArrayList();
            destLatLng.add(0,place.getLatLng().latitude);
            destLatLng.add(1,place.getLatLng().longitude);
            placeDetailsCustomList.add(destLatLng);

            completeList.add(placeDetailsCustomList);
        }
    };

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
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        System.out.println("location manager is:::::"+locationManager);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        /*location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        System.out.println("network enabled iss:::::"+isNetworkEnabled);*/
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        System.out.println("Location"+location);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }



}
