package com.sjsu.nimbleshop;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SwathiBala on 5/5/16.
 */
public class WaitTimeAsyncTask extends AsyncTask<Void,Void,String> {

    private Context myctx;
    public WaitTimeAsyncTask(Context ctx)
    {
        this.myctx=ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        StringBuffer response=new StringBuffer();
        try {
            String urlVal= Utils.getProperty("getWaitTime", myctx);
            URL url = new URL(urlVal);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            int ResponseCode=urlConnection.getResponseCode();
            System.out.println("Response code is::::::::::"+ResponseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            //response =

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

        }
        catch (Exception e) {
            Log.i("message:", "Exception occured " + e.toString());
            //return false;
            }
    return response.toString();
    }
}
