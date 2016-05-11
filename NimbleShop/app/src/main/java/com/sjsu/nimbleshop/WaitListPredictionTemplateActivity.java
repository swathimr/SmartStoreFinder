package com.sjsu.nimbleshop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjsu.nimbleshop.Vo.DataWrapper;
import com.sjsu.nimbleshop.Vo.StoreVo;
import com.sjsu.nimbleshop.Vo.TemplateVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WaitListPredictionTemplateActivity extends AppCompatActivity {


    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    Map<Integer, StoreVo> mapStores = new HashMap<>();
    Set<Integer> listStores = null;
    Map<Integer, List<RadioButton>> mapGroupRadio = new LinkedHashMap<>();

    // To display  table
    public void initTable(List<TemplateVo> listTemplate) {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTemplate);


        for (int i = 0; i < listTemplate.size(); i++) {

            TemplateVo templateVo = listTemplate.get(i);
            System.out.println("Name " + templateVo.getName());

            TableRow emptyRow = new TableRow(this);
            emptyRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

            TableRow.LayoutParams params = (TableRow.LayoutParams) emptyRow.getLayoutParams();
            params.span = 4;
            TextView tv = new TextView(this);
            tv.setText(null);
            tv.setLayoutParams(params); // causes layout update
            emptyRow.addView(tv);
            tableLayout.addView(emptyRow);

            if (null != templateVo.getStores() && !templateVo.getStores().isEmpty()) {

                TableRow tbrow = new TableRow(this);
                tbrow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                tv = new TextView(this);
                tv.setText(templateVo.getName());
                tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);

                params = (TableRow.LayoutParams) tbrow.getLayoutParams();
                params.span = 4;
                tv.setLayoutParams(params); // causes layout update

                tbrow.addView(tv);
                tableLayout.addView(tbrow);

                //hh
                boolean isNa = false;
                mapGroupRadio.put(templateVo.getTemplateId(), new ArrayList<RadioButton>());


                for (StoreVo storeVo : templateVo.getStores()) {

                    if (null != storeVo.getListProducts() && !storeVo.getListProducts().isEmpty()) {

                        TableRow tbr = new TableRow(this);
                        tbr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        // Radio button

                        RadioButton rd = new RadioButton(this);
                        StringBuffer sb = new StringBuffer("");
                        sb.append(templateVo.getTemplateId());
                        sb.append("00");
                        sb.append(storeVo.getStoreId());
                        rd.setId(Integer.parseInt(sb.toString()));
                        rd.setGravity(View.TEXT_ALIGNMENT_INHERIT);
                        rd.setWidth(10);

                        mapGroupRadio.get(templateVo.getTemplateId()).add(rd);


                        rd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton rb = (RadioButton) v;
                                int wholeId = rb.getId();
                                String sTempId = String.valueOf(wholeId).substring(0, String.valueOf(wholeId).lastIndexOf("00"));
                                int tempId = Integer.parseInt(sTempId);

                                for (RadioButton rdb : mapGroupRadio.get(tempId)) {
                                    if (wholeId == rdb.getId())
                                        rdb.setChecked(true);
                                    else
                                        rdb.setChecked(false);

                                }
                            }
                        });

                        tbr.addView(rd);

                        //Store Name
                        TextView tv1 = new TextView(this);
                        tv1.setText(storeVo.getStoreName().trim());
                        tv1.setTextAppearance(this, android.R.style.TextAppearance);
                        tv1.setTextSize(15);
                        tv1.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
                        //tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        tbr.addView(tv1);

                        // Wait time

                        TextView tv4 = new TextView(this);
                        tv4.setText("NA");
                        Long waitTime = new Long(0L);
                        if( null != storeVo.getWaitTime() && 0 != storeVo.getWaitTime() ){
                            waitTime = (storeVo.getWaitTime()/60);
                            String waitTimeStr =  waitTime == 1 ? waitTime.toString()+" min" : waitTime.toString()+ " mins";
                            tv4.setText(waitTimeStr);
                        }
                        tbr.addView(tv4);

                        // Total time
                        TextView tv5 = new TextView(this);
                        Long totalTime = waitTime + (storeVo.getTravelTime()/60);
                        String totalTimeValue =  totalTime == 1 ? totalTime.toString()+" min" : totalTime.toString()+ " mins";
                        tv5.setText( totalTimeValue );
                        tbr.addView(tv5);


                        tableLayout.addView(tbr);


                        TableRow tbrCost = new TableRow(this);
                        tbrCost.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        TextView tvE1 = new TextView(this);
                        tvE1.setText(null);
                        tbrCost.addView(tvE1);

                        TextView tv2 = new TextView(this);
                        tv2.setText("Cost: $" + String.valueOf(storeVo.getListProducts().get(0).getCost()));
                        tbrCost.addView(tv2);

                        //tbrCost.setBackgroundResource(R.drawable.bluebg);

                        tableLayout.addView(tbrCost);

                        TableRow tbrQty = new TableRow(this);
                        tbrQty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        TextView tvEmptyQty = new TextView(this);
                        tvEmptyQty.setText("");
                        tbrQty.addView(tvEmptyQty);

                        TextView tv3 = new TextView(this);
                        tv3.setText("Qty: " + String.valueOf(storeVo.getListProducts().get(0).getQuantity()));
                        tbrQty.addView(tv3);

                        tableLayout.addView(tbrQty);

                        isNa = true;


                    }
                }

                if (!isNa) {
                    TableRow tbr = new TableRow(this);
                    tbr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                    TextView tv0 = new TextView(this);
                    tv0.setText("Not Available in any store.");

                    params = (TableRow.LayoutParams) tbr.getLayoutParams();
                    params.span = 4;
                    tv0.setLayoutParams(params); // causes layout update


                    tbr.addView(tv0);
                    tableLayout.addView(tbr);
                }

                //tableLayout.addView(rdg);
            }


        }


        Button navBtn = new Button(this);
        navBtn.setText("Navigation");
        navBtn.setBackgroundResource(R.drawable.roundedbutton);
        navBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listStores = new LinkedHashSet<Integer>();
                for (Map.Entry<Integer, List<RadioButton>> entry : mapGroupRadio.entrySet()) {

                    int templateId = entry.getKey();
                    int storeId = 0;
                    for (RadioButton rb : entry.getValue()) {
                        if (rb.isChecked()) {
                            storeId = Integer.parseInt(String.valueOf(rb.getId()).substring(String.valueOf(rb.getId()).lastIndexOf("00")));
                            //storeId = rb.getId()%10;
                            listStores.add(storeId);
                            System.out.println(" Chosen store  : " + templateId + " -- " + storeId);
                            break;
                        }

                    }
                }

                List<StoreVo> list = new ArrayList<StoreVo>();

                System.out.println("----------------------------------------");
                System.out.println("Addresses to go to.");
                for (Integer i : listStores) {
                    list.add( mapStores.get(i) );
                    System.out.println(mapStores.get(i).getAddress().getLatitude() + "," + mapStores.get(i).getAddress().getLongitude());
                    //locations.add(new LatLng(mapStores.get(i).getAddress().getLatitude(), mapStores.get(i).getAddress().getLongitude()));
                }

                // Sorting of Stores
                Collections.sort(list, new Comparator<StoreVo>() {
                    @Override
                    public int compare(StoreVo lhs, StoreVo rhs) {
                        if( lhs.getTravelTime() > rhs.getTravelTime() )
                            return 1;
                        else if( lhs.getTravelTime() < rhs.getTravelTime() )
                            return -1;
                        return 0;
                    }
                });

                for( StoreVo storeVo : list ){
                    locations.add(new LatLng(storeVo.getAddress().getLatitude(), storeVo.getAddress().getLongitude()));
                }

                gotoTemplate(locations);
            }
        });

        tableLayout.addView(navBtn);
    }

    public void gotoTemplate(ArrayList<LatLng> locations)
    {
        Intent intent = new Intent(this, LocationTemplate.class);
        intent.putParcelableArrayListExtra("locations", locations);
        startActivity(intent);
        locations.clear();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_list_prediction_template);
        Bundle b = getIntent().getExtras();
        String response = b.getString("responseArray");
        JSONArray obj = null;
        List<TemplateVo> listTemplate = null;
        try {

            obj = new JSONArray(response);
            Log.d("My App", obj.toString());

            listTemplate = new Gson().fromJson(response, new TypeToken<List<TemplateVo>>() {
            }.getType());

            for (TemplateVo t : listTemplate) {
                if (null != t.getStores()) {
                    for (StoreVo storeVo : t.getStores()) {
                        mapStores.put(storeVo.getStoreId(), storeVo);
                    }
                }
            }
            System.out.println(" Kandarp " + listTemplate.size());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON");
        }

        // Populating tables
        initTable(listTemplate);

    }

    public void mapview(View view) {


        // ArrayList from = (ArrayList) listValue.get(3);
        //ArrayList toLoctn = (ArrayList) listValue.get(4);


        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + 37.345185 + "," + -121.936453 + "&daddr=" + 37.33819 + "," + -121.884142 + "&daddr=" + 37.33868 + "," + -121.904431;
        System.out.println(uri);
        //System.out.println(from.get(0).getClass().getName());
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        i.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        startActivity(i);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "WaitListPredictionTemplate Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.sjsu.nimbleshop/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "WaitListPredictionTemplate Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.sjsu.nimbleshop/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
