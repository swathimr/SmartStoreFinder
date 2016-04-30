package com.sjsu.nimbleshop;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by SwathiBala on 4/12/16.
 */
public class DistanceMatrixAsyncTask extends AsyncTask<ArrayList<Double>,Void,String> {

    @Override
    protected String doInBackground(ArrayList<Double>... params) {
        String distance="";
        try {
            String distanceMatrixurl="http://maps.googleapis.com/maps/api/distancematrix/json?origins="+params[0].get(0)+","+params[0].get(1)+"&destinations="+params[0].get(2)+","+params[0].get(3)+"&mode=driving&language=en-EN&sensor=false";
            System.out.println(distanceMatrixurl);
            URL url = new URL(distanceMatrixurl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                distance=parseJSON(stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            //return null;
        }

    return distance;
    }


    //parces json response from the distance matrix google api url
    private String parseJSON(String jsonResponse) throws JSONException{
        JSONObject object = (JSONObject) new JSONTokener(jsonResponse).nextValue();
        String distance=object.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").get("text").toString();
        System.out.println("travel distance from API::"+distance);
        return distance;
    }
}
