package com.sjsu.nimbleshop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ProdrecmndtnActivity extends AppCompatActivity {
    private TextView shopName;
    private TextView address;
    private TextView product;
    private RatingBar rating;
    private TextView reviews;
    private TextView viewMoreLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodrecmndtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shopName=(TextView)findViewById(R.id.shopNameID);
        address=(TextView)findViewById(R.id.shopAddressID);
        product=(TextView)findViewById(R.id.ProdRecoID);
        rating=(RatingBar)findViewById(R.id.ratingBar);
        reviews=(TextView)findViewById(R.id.ProdReviewID);
        viewMoreLink=(TextView)findViewById(R.id.viewmorelink);
        try {
            ArrayList<String> list=new ArrayList<>();
            String shopNameVal=getIntent().getExtras().get("shopname").toString();
            String addressVal=getIntent().getExtras().get("address").toString();
            list.add(shopNameVal);
            list.add(addressVal);
            ProdRecommendtnAsynTask prod=new ProdRecommendtnAsynTask(getApplicationContext());
            ArrayList<String> result=prod.execute(list).get();
                shopName.setText(shopNameVal);
                address.setText(addressVal);
                String hs=result.get(0).replace("[", "").replace(']', ' ');
                product.setText(hs + "\n" + result.get(1));
                rating.setNumStars(5);
                rating.setMax(5);
                rating.setStepSize(1);
                rating.setRating(Float.valueOf(result.get(2)));
                reviews.setText(result.get(3));

                viewMoreLink.setText("View More At : "+result.get(4));

        } catch(Exception e) {
            e.printStackTrace();
        }

    }


}
