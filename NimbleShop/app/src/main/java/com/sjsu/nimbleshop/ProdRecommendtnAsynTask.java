package com.sjsu.nimbleshop;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by SwathiBala on 5/8/16.
 */
public class ProdRecommendtnAsynTask extends AsyncTask<ArrayList<String>,Void,ArrayList<String>> {

    private Context myctx;
    public ProdRecommendtnAsynTask(Context ctx)
    {
        this.myctx=ctx;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        StringBuffer response=new StringBuffer();
        ArrayList<String> list=new ArrayList<>();
        try {
            Map<String, String> para = new HashMap<>();
            para.put("term",params[0].get(0));
            para.put("location",params[0].get(1));
            RestTemplate restTemplate=new RestTemplate();
            String urlVal= Utils.getProperty("getProdRecommendation", myctx);

            String result=restTemplate.getForObject(urlVal, String.class, para);
            JSONObject jsonResult=new JSONObject(result);

            list.add(jsonResult.get("Food Type:").toString());
            list.add("this is deals and she is not sending");
            list.add(jsonResult.get("Rating").toString());
            list.add(jsonResult.get("Review:").toString());
            list.add(jsonResult.get("Visit at:").toString());
            //return list;
        }
        catch (Exception e) {
            Log.i("message:", "Exception occured " + e.toString());
            //return false;
        }
        return list;
    }

}
