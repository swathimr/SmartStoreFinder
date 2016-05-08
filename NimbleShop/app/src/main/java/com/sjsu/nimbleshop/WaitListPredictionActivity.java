package com.sjsu.nimbleshop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class WaitListPredictionActivity extends AppCompatActivity {

    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_list_prediction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        results=(TextView)findViewById(R.id.info2);
        results.setText("");
        ArrayList obj = getIntent().getStringArrayListExtra("resultList");
        int selectedPlace=getIntent().getIntExtra("SelectedPos", 0);
        TableLayout tl = (TableLayout) findViewById(R.id.table);
        String row="1";int val=0;

        //adding table rows dynamically
        for(Object result:obj) {
            final ArrayList listValue = (ArrayList) result;

            //rowNum="tr"+"row";
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            TextView tx1 = new TextView(this);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
            tx1.append(listValue.get(0) + "\n");
            tx1.append(listValue.get(1) + "\n");
            tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tx1.setTypeface(Typeface.DEFAULT_BOLD);
            tx1.setWidth(width);
            TableRow.LayoutParams lparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            tx1.setLayoutParams(lparams);
            tx1.setGravity(Gravity.CENTER);
            tx1.setId(Integer.valueOf(row + val));
            System.out.println(Integer.valueOf(row + val));

            TextView tx2 = new TextView(this);
            if(val==selectedPlace)
            {
                try {
                    WaitTimeAsyncTask waittime = new WaitTimeAsyncTask(getApplicationContext());
                    String waitTime=waittime.execute().get();
                    tx2.setText(waitTime);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                tx2.setText("5 min");
            }
            tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tx2.setTypeface(Typeface.DEFAULT_BOLD);
            int value = (int) (3 * getResources().getDisplayMetrics().density);
            tx2.setPadding(0, 0, value, 0);
            tx2.setLayoutParams(lparams);
            tx2.setGravity(Gravity.CENTER);
            tx2.setId(Integer.valueOf(row + val));

            TextView tx3 = new TextView(this);
            ArrayList from = (ArrayList) listValue.get(3);
            ArrayList toLoctn = (ArrayList) listValue.get(4);
            ArrayList<Double> loctnValues = new ArrayList<>();
            loctnValues.add(Double.valueOf(from.get(0).toString()));
            loctnValues.add(Double.valueOf(from.get(1).toString()));
            loctnValues.add(Double.valueOf(toLoctn.get(0).toString()));
            loctnValues.add(Double.valueOf(toLoctn.get(1).toString()));
            try {
                DistanceMatrixAsyncTask getDistance = new DistanceMatrixAsyncTask();
                String dist = getDistance.execute(loctnValues).get();
                tx3.setText(dist);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tx3.setTypeface(Typeface.DEFAULT_BOLD);
            tx3.setPadding(0, 0, value, 0);
            tx3.setGravity(Gravity.CENTER);
            tx3.setId(Integer.valueOf(row + val));

            RatingBar rb = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
            rb.setLayoutParams(new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            rb.setNumStars(5);
            rb.setPadding(0, 50, 0, 0);
            System.out.println("rating value is::" + listValue.get(2));
            rb.setRating(Float.valueOf(listValue.get(2).toString()));

            Button prodBtn=new Button(this);
            prodBtn.setText("View More");
            prodBtn.setAllCaps(false);
            prodBtn.setTextColor(getResources().getColor(R.color.darkgreen));
            prodBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            prodBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WaitListPredictionActivity.this,ProdrecmndtnActivity.class);
                    startActivity(intent);
                }
            });

            Button navBtn = new Button(this);
            navBtn.setText("Start Nav");
            navBtn.setAllCaps(false);
            navBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            navBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList from = (ArrayList) listValue.get(3);
                    ArrayList toLoctn = (ArrayList) listValue.get(4);
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + from.get(0) + "," + from.get(1) + "&daddr=" + toLoctn.get(0) + "," + toLoctn.get(1);
                    System.out.println(uri);
                    System.out.println(from.get(0).getClass().getName());
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    i.setClassName("com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity");
                    startActivity(i);
                }
            });

            //separation line in each row
            View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
            v.setBackgroundColor(Color.rgb(51, 51, 51));

            tr.addView(tx1);
            tr.addView(rb);
            tr.addView(tx2);
            tr.addView(tx3);
            tr.addView(prodBtn);
            tr.addView(navBtn);
            tl.addView(tr);
            tl.addView(v);
            val++;
        }
    }
}
