package com.sjsu.nimbleshop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TemplateActivity extends AppCompatActivity {
    private List<CheckBox> listCheckBox = new ArrayList<>();
    private CheckBox Bread, Eggs, Chicken, Milk, Wine, Whiskey, Beer, Coffee;
    private Button getWaitTimes;
    ArrayList<Integer> list = new ArrayList<>();
    JSONArray responseArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        Bread = (CheckBox) findViewById(R.id.Bread);
        listCheckBox.add(Bread);

        Eggs = (CheckBox) findViewById(R.id.Eggs);
        listCheckBox.add(Eggs);

        Milk = (CheckBox) findViewById(R.id.Milk);
        listCheckBox.add(Milk);

        Chicken = (CheckBox) findViewById(R.id.Chicken);
        listCheckBox.add(Chicken);

        Beer = (CheckBox) findViewById(R.id.Beer);
        listCheckBox.add(Beer);

        Wine = (CheckBox) findViewById(R.id.Wine);
        listCheckBox.add(Wine);

        Whiskey = (CheckBox) findViewById(R.id.Whiskey);
        listCheckBox.add(Whiskey);

        Coffee = (CheckBox) findViewById(R.id.Coffee);
        listCheckBox.add(Coffee);

    }

    public void getWaitTimes(View view) throws JSONException {

        list.clear();

        int i = 0;
        for (CheckBox cb : listCheckBox) {
            if (cb.isChecked())
                list.add(i + 1);

            i++;
        }

        if (!list.isEmpty()) {
            getPublicTimeline();
        }

    }


    Location location = null;

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        System.out.println("location manager is:::::" + locationManager);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        /*location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        System.out.println("network enabled iss:::::"+isNetworkEnabled);*/
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        System.out.println("Location" + location);

        //locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    public void getPublicTimeline() throws JSONException {

        final Intent intent = new Intent(this, WaitListPredictionTemplateActivity.class);
        AsyncHttpClient client = new AsyncHttpClient();

        StringBuffer sb = new StringBuffer("");
        for (Integer i : list) {
            sb.append(String.valueOf(i));
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        getCurrentLocation();

        StringBuffer url = new StringBuffer("http://54.191.223.255:8080/template/getStores?items=");
        //StringBuffer url = new StringBuffer("http://localhost:8080/template/getStores?items=");
        url.append(sb.toString());
        url.append("&lat=");
        url.append("" + location.getLatitude());
        //url.append(37.3357190);
        url.append("&long=");
        //url.append(-121.8867080);
        url.append("" + location.getLongitude());

        System.out.println("URL hitting is " + url.toString());

        client.get(url.toString(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);

                if (response != null) {
                    Bundle b = new Bundle();
                    b.putString("responseArray", response.toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response);
                responseArray = response;
                if (responseArray != null) {
                    Bundle b = new Bundle();
                    b.putString("responseArray", responseArray.toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                //System.out.println(response);
                //responseArray = response;
                //if(responseArray!=null) {
                Bundle b = new Bundle();
                b.putString("responseArray", responseArray.toString());
                intent.putExtras(b);
                startActivity(intent);
                //}
            }

        });
    }
}
