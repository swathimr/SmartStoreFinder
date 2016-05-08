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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjsu.nimbleshop.Vo.DataWrapper;
import com.sjsu.nimbleshop.Vo.StoreVo;
import com.sjsu.nimbleshop.Vo.TemplateVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaitListPredictionTemplateActivity extends AppCompatActivity {
    ArrayList<Integer> list = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public void initTable( List<TemplateVo> listTemplate ){

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableTemplate);

        for (int i = 0; i < listTemplate.size(); i++) {

            TemplateVo templateVo = listTemplate.get(i);
            System.out.println( "Name " + templateVo.getName() );
            if( null != templateVo.getStores() && !templateVo.getStores().isEmpty() ){

                TableRow tbrow = new TableRow(this);
                TextView tv =  new TextView(this);
                tv.setText( templateVo.getName() );
                tbrow.addView(tv);
                tableLayout.addView(tbrow);
                //hh
                boolean isNa = false;
                for(StoreVo storeVo : templateVo.getStores() ){

                    if( null != storeVo.getListProducts() && !storeVo.getListProducts().isEmpty() ){

                        TableRow tbr = new TableRow(this);

//                        tv0 = (TextView) findViewById(R.id.storeNameD);
//                        tv0.setText( String.valueOf(storeVo.getStoreId()) );
//                        tv0.setTextColor(Color.BLACK);
//                        tbr.addView(tv0);

                        TextView tv1 = new TextView(this);
                        tv1.setText( storeVo.getStoreName() );
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
                        tv2.setText( String.valueOf( storeVo.getListProducts().get(0).getCost()) );
                        //tv2.setTextColor(Color.BLACK);
                        tbr.addView(tv2);

                        TextView tv3 = new TextView(this);
                        tv3.setText( String.valueOf(storeVo.getListProducts().get(0).getQuantity()) );
                        //tv3.setTextColor(Color.BLACK);
                        tbr.addView(tv3);

                        tableLayout.addView(tbr);
                    }
                    else{
                        isNa = true;
                    }
                }
                if( isNa ){
                    TableRow tbr = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText( "NA" );
                    //tv0.setTextColor(Color.BLACK);
                    //tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tbr.addView(tv0);


                    tableLayout.addView(tbr);
                }

            }


        }

    }

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

            listTemplate = new Gson().fromJson(response, new TypeToken<List<TemplateVo>>(){}.getType());
            System.out.println( " Kandarp " + listTemplate.size() ) ;

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON");
        }

        // Populating tables
        initTable( listTemplate );


        /*


        String row = "1";
        String val = "1";
        try {
            //for (int i = 0; i <3; i++) {
            //RadioGroup group = new RadioGroup(this);
           // group.setOrientation(RadioGroup.HORIZONTAL);
//            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
//                    RadioGroup.LayoutParams.WRAP_CONTENT,
//                    RadioGroup.LayoutParams.WRAP_CONTENT);


            for (int i = 0; i < obj.getJSONObject(0).getJSONArray("stores").length(); i++) {

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                //RadioButton btn1 = new RadioButton(this);
                //btn1.setText("BTN1");
                //group.addView(btn1,layoutParams);
                TextView tx1 = new TextView(this);
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
                tx1.append(obj.getJSONObject(0).getJSONArray("stores").getJSONObject(i).getString("storeName") + "\n");
                //tx1.append("dsf" + "\n");
                tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tx1.setTypeface(Typeface.DEFAULT_BOLD);
                tx1.setWidth(width);
                TableRow.LayoutParams lparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                tx1.setLayoutParams(lparams);
                tx1.setGravity(Gravity.CENTER);
                tx1.setId(Integer.valueOf(row + val));
                System.out.println(Integer.valueOf(row + val));

                TextView tx2=new TextView(this);
                tx2.setText("5 min");
                tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tx2.setTypeface(Typeface.DEFAULT_BOLD);
                int value =(int)(3 * getResources().getDisplayMetrics().density);
                tx2.setPadding(0, 0, value, 0);
                tx2.setLayoutParams(lparams);
                tx2.setGravity(Gravity.CENTER);
                tx2.setId(Integer.valueOf(row + val));


                    TextView tx3=new TextView(this);
                    tx3.setText("10 min");
                    tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tx3.setTypeface(Typeface.DEFAULT_BOLD);
                    int value1 =(int)(3 * getResources().getDisplayMetrics().density);
                    tx3.setPadding(0, 0, value1, 0);
                    tx3.setLayoutParams(lparams);
                    tx3.setGravity(Gravity.CENTER);
                    tx3.setId(Integer.valueOf(row + val));

                    TextView tx4=new TextView(this);
                    tx4.setText("3$");
                    tx4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tx4.setTypeface(Typeface.DEFAULT_BOLD);
                    int value2 =(int)(3 * getResources().getDisplayMetrics().density);
                    tx4.setPadding(0, 0, value2, 0);
                    tx4.setLayoutParams(lparams);
                    tx4.setGravity(Gravity.CENTER);
                    tx4.setId(Integer.valueOf(row + val));

                    TextView tx5=new TextView(this);
                    tx5.setText("20");
                    tx5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tx5.setTypeface(Typeface.DEFAULT_BOLD);
                    int value3 =(int)(3 * getResources().getDisplayMetrics().density);
                    tx5.setPadding(0, 0, value3, 0);
                    tx5.setLayoutParams(lparams);
                    tx5.setGravity(Gravity.CENTER);
                    tx5.setId(Integer.valueOf(row + val));


                View v = new View(this);
                v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                v.setBackgroundColor(Color.rgb(51, 51, 51));

                //tr.addView(group);
                tr.addView(tx1);
                tr.addView(tx2);
                tr.addView(tx3);
                    tr.addView(tx4);
                    tr.addView(tx5);
                   // t.addView(group);
                    t.addView(tr);
                t.addView(v);

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }*/


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
