package org.smartstore.controller;
import com.beust.jcommander.JCommander;
import org.json.simple.JSONObject;
import org.smartstore.helper.RecommendationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//saasd
import java.util.concurrent.atomic.AtomicLong;

import static org.smartstore.helper.RecommendationManager.queryAPI;

@RestController
public class RecommendationController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private static final String CONSUMER_KEY = "vqKh_Ym4IuAyl9YYXLlAhQ";
    private static final String CONSUMER_SECRET = "THEXi3oe6-aYhNjl3IBk_RVv0gQ";
    private static final String TOKEN = "Bwwu_PB9mVpYThL6mbWqHPy37IfoC1Ya";
    private static final String TOKEN_SECRET = "6n6_E8M9lbg7-K8x8gNp1dWUqU0";


    @RequestMapping(value = "/searchYelp",produces = "application/json")
    public ResponseEntity<String> searchAPI()
    {
        RecommendationManager.YelpAPICLI yelpApiCli = new RecommendationManager.YelpAPICLI();
        //String location = "1370 Blossom Hill Road, San Jose, CA 95118";
        //String term = "taco bell";
        //String term = "Menlo Cafe";
        //String location = "620 Santa Cruz Ave, Menlo Park, CA 94025";

        /* for checking with deals*/
        String term = "taco bell";
        String location = "Pier 39, Space P-214, North Beach, Telegraph Hill, San Francisco, CA 94133";

         //String term = "Amor cafe and tea";
         //String location = "110 E San Fernando St, San Jose, CA 95112";
        /*Double longi = -121.886615;
        Double latit = 37.335351 ;*/
        yelpApiCli.location = location;
        yelpApiCli.term = term;

        new JCommander(yelpApiCli);
        RecommendationManager yelpApi = new RecommendationManager(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        JSONObject result = queryAPI(yelpApi, yelpApiCli);
        System.out.println(result.toString());
        return new ResponseEntity(result.toString(), HttpStatus.OK);
    }
}