package com.sjsu.nimbleshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TemplateActivity extends AppCompatActivity {
    private CheckBox Bread, Eggs, Chicken, Milk, Wine, Whiskey, Beer, Coffee;
    private Button getWaitTimes;
    ArrayList<Integer> list = new ArrayList<>();
    JSONArray responseArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Bread = (CheckBox) findViewById(R.id.Bread);
        Bread.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(1);

            }
        });
        Eggs = (CheckBox) findViewById(R.id.Eggs);
        Eggs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(2);

            }
        });

        Milk = (CheckBox) findViewById(R.id.Milk);
        Milk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(3);

            }
        });

        Chicken = (CheckBox) findViewById(R.id.Chicken);
        Chicken.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(4);

            }
        });

        Beer = (CheckBox) findViewById(R.id.Beer);
        Beer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(5);

            }
        });

        Wine = (CheckBox) findViewById(R.id.Wine);
        Wine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(6);


            }
        });
        Whiskey = (CheckBox) findViewById(R.id.Whiskey);
        Whiskey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(7);

            }
        });

        Coffee = (CheckBox) findViewById(R.id.Coffee);
        Coffee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    list.add(9);

            }
        });

        //Rest api call





    }

    public void getWaitTimes (View view) throws JSONException {

        if( !list.isEmpty() ){
            getPublicTimeline();
        }

    }


    public void getPublicTimeline() throws JSONException {

        final Intent intent= new Intent(this, WaitListPredictionTemplateActivity.class);
        AsyncHttpClient client = new AsyncHttpClient();

        StringBuffer sb = new StringBuffer("");
        for( Integer i : list ){
            sb.append(String.valueOf(i));
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        StringBuffer url = new StringBuffer( "http://54.191.223.255:8080/template/getStores?items=" );
        url.append(sb.toString());
        url.append("&lat=");
        url.append(37.3357190);
        url.append("&long=");
        url.append(-121.8867080);

        System.out.println("URL hitting is " + url.toString() );

        client.get(url.toString(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);

                if(response!=null) {
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
                if(responseArray!=null) {
                    Bundle b = new Bundle();
                    b.putString("responseArray", responseArray.toString());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){

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
