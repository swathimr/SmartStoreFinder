package com.sjsu.nimbleshop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjsu.nimbleshop.Vo.StoreVo;
import com.sjsu.nimbleshop.Vo.TemplateVo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WaitListPredictionTemplateActivity extends AppCompatActivity {
    ArrayList<Integer> list = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    Map<Integer, StoreVo> mapStores = new HashMap<>();
    List<Integer> listStores = new ArrayList<Integer>();
    Map<Integer, List<RadioButton>> mapGroupRadio = new LinkedHashMap<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    public void initTable(List<TemplateVo> listTemplate) {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTemplate);


        for (int i = 0; i < listTemplate.size(); i++) {

            TemplateVo templateVo = listTemplate.get(i);
            System.out.println("Name " + templateVo.getName());
            if (null != templateVo.getStores() && !templateVo.getStores().isEmpty()) {

                TableRow tbrow = new TableRow(this);
                TextView tv = new TextView(this);
                tv.setText(templateVo.getName());
                tbrow.addView(tv);
                tableLayout.addView(tbrow);
                //hh
                boolean isNa = false;

                mapGroupRadio.put(templateVo.getTemplateId(), new ArrayList<RadioButton>());


                for (StoreVo storeVo : templateVo.getStores()) {

                    if (null != storeVo.getListProducts() && !storeVo.getListProducts().isEmpty()) {

                        TableRow tbr = new TableRow(this);

                        RadioButton rd = new RadioButton(this);
                        StringBuffer sb = new StringBuffer("");
                        sb.append(templateVo.getTemplateId());
                        sb.append("00");
                        sb.append(storeVo.getStoreId());
                        rd.setId(Integer.parseInt(sb.toString()));

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

                        TextView tv1 = new TextView(this);
                        tv1.setText(storeVo.getStoreName());
                        //tv1.setTextColor(Color.BLACK);
                        tbr.addView(tv1);

                        TextView tv4 = new TextView(this);
                        tv4.setText(" Wait time ");
                        //tv4.setTextColor(Color.BLACK);
                        tbr.addView(tv4);

                        TextView tv5 = new TextView(this);
                        tv5.setText(" Total time ");
                        //tv5.setTextColor(Color.BLACK);
                        tbr.addView(tv5);

                        TextView tv2 = new TextView(this);
                        tv2.setText(String.valueOf(storeVo.getListProducts().get(0).getCost()));
                        //tv2.setTextColor(Color.BLACK);
                        tbr.addView(tv2);

                        TextView tv3 = new TextView(this);
                        tv3.setText(String.valueOf(storeVo.getListProducts().get(0).getQuantity()));
                        //tv3.setTextColor(Color.BLACK);
                        tbr.addView(tv3);

                        tableLayout.addView(tbr);
                    } else {
                        isNa = true;
                    }
                }

                if (isNa) {
                    TableRow tbr = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText("NA");
                    //tv0.setTextColor(Color.BLACK);
                    //tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tbr.addView(tv0);


                    tableLayout.addView(tbr);
                }

                //tableLayout.addView(rdg);
            }


        }


        Button navBtn = new Button(this);
        navBtn.setText("Continue");
        //navBtn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

                System.out.println("----------------------------------------");
                System.out.println("Addresses to go to.");
                for (Integer i : listStores) {
                    System.out.println(mapStores.get(i).getAddress().getLatitude() + "," + mapStores.get(i).getAddress().getLongitude());
                    locations.add(new LatLng(mapStores.get(i).getAddress().getLatitude(), mapStores.get(i).getAddress().getLongitude()));
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WaitListPredictionTemplate Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sjsu.nimbleshop/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WaitListPredictionTemplate Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sjsu.nimbleshop/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
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
