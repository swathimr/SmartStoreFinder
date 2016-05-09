package org.smartstore.helper;

import com.beust.jcommander.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Code for accessing the Yelp API V2.
 *
 */
public class RecommendationManager {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "food";
    private static final String DEFAULT_LOCATION = "San Francisco, CA";
    private static final int SEARCH_LIMIT = 3;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    /*
     * OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "vqKh_Ym4IuAyl9YYXLlAhQ";
    private static final String CONSUMER_SECRET = "THEXi3oe6-aYhNjl3IBk_RVv0gQ";
    private static final String TOKEN = "Bwwu_PB9mVpYThL6mbWqHPy37IfoC1Ya";
    private static final String TOKEN_SECRET = "6n6_E8M9lbg7-K8x8gNp1dWUqU0";

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public RecommendationManager(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(SocialMediaManager.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Creates and sends a request to the Search API by term and location.
     * <p>
     * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
     * for more info.
     *
     * @param term <tt>String</tt> of the search term to be queried
     * @param location <tt>String</tt> of the location
     * @return <tt>String</tt> JSON Response
     */
    public String searchForBusinessesByLocation(String term,String location,String getDeals) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("deals_filter",getDeals);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p>
     * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
     * for more info.
     *
     * @param businessID <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    public String searchBySearchId(String searchID)
    {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH + "?location=" + searchID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    public OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    public String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Queries the Search API based on the command line arguments and takes the first result to query
     * the Business API.
     *
     * @param yelpApi <tt>YelpAPI</tt> service instance
     * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
     */
    public static JSONObject queryAPI(RecommendationManager yelpApi, YelpAPICLI yelpApiCli) {
        String searchResponseJSON = yelpApi.searchForBusinessesByLocation(yelpApiCli.term,yelpApiCli.location,yelpApiCli.getDeals);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            System.exit(1);
        }

        //https://api.yelp.com/v2/search/?term=taco bell&location=3690 Stevens Creek Blvd San Jose, CA 95117&limit=3&deals_filter=true&actionlinks=true

        JSONArray businesses = (JSONArray) response.get("businesses");
        int length = businesses.size();
        System.out.println("********");
        System.out.println("Yelp:"+ yelpApiCli.location);
        String add = yelpApiCli.location;
        String[] parts = add.split(",");

        String businessID = "";

        for(int i =0;i<length;i++) {
            JSONObject obj = (JSONObject) businesses.get(i);
            String name = obj.get("name").toString();
            JSONObject address = (JSONObject) obj.get("location");
            String d = address.get("display_address").toString();

            if(d.contains(parts[0]) && d.contains(parts[parts.length-1]))
            {
                System.out.println("Matched: " + d);
                businessID = obj.get("id").toString();
                break;
            }

        }
        System.out.println("********");


        // Select the business and display business details
        String businessResponseJSON = yelpApi.searchByBusinessId(businessID);
        System.out.println(String.format("Result for business \"%s\" found:", businessID));

        JSONParser parser1 = new JSONParser();
        JSONObject response1 = null;
        JSONObject result = new JSONObject();

        try {
            response1 = (JSONObject) parser.parse(businessResponseJSON);

            result.put("Rating",response1.get("rating").toString());
            String review_text = response1.get("snippet_text").toString();
            String reviewText[] = review_text.split("\n");
            String review = "";

            for(int i =0;i<reviewText.length;i++)
            {
                review+= reviewText[i];
            }
            System.out.println("Review:" + review);

            result.put("Review:", review);
            result.put("Visit at:",response1.get("mobile_url").toString());
            result.put("View Photo at:",response1.get("image_url").toString());
            result.put("Number of reviews:", response1.get("review_count").toString());


            System.out.println("Rating:"+ response1.get("rating").toString());
            System.out.println("Go to:" + response1.get("mobile_url").toString());
            System.out.println("View Photo at:" + response1.get("image_url").toString());
            System.out.println("People said...:" + response1.get("snippet_text").toString());

            JSONArray categories = (JSONArray)response1.get("categories");
            int length_categories = categories.size();
            ArrayList typeOfFood = new ArrayList();
            String keyTerms = "";
            for (int i = 0; i < length_categories; i++) {

                keyTerms+= categories.get(i).toString();
                typeOfFood.add(categories.get(i).toString());
                System.out.println(categories.get(i).toString());
            }

            HashSet<String> keyTermsSet = parseFoodTypes(keyTerms);
            result.put("Food Type:",keyTermsSet);

            if(response1.get("reviews")!=null)
            {
                JSONArray reviews = (JSONArray)response1.get("reviews");
                if(reviews!=null) {
                    int length_reviews = reviews.size();
                    for (int i = 0; i < length_reviews; i++) {
                        JSONObject obj = (JSONObject)reviews.get(i);
                        System.out.println(reviews.get(i).toString());

                    }
                }
            }
            if(response1.get("deals")!=null){
                JSONArray deals = (JSONArray)response1.get("deals");
                if(deals!=null) {
                    int length_deals = deals.size();
                    for (int i = 0; i < length_deals; i++) {
                        JSONObject obj = (JSONObject)deals.get(i);
                        String dealText[] = obj.get("what_you_get").toString().split("\n");
                        String deal = "";
                        for(int k =0;k < dealText.length;k++)
                        {
                            for(int j = 0;j<dealText[k].length();j++) {
                                if (dealText[k].charAt(j) == '<')
                                    break;
                                deal += dealText[k].charAt(j);
                            }
                        }
                        deal += "with the given link";
                        result.put("Available DEAL: ",deal);
                        result.put("DEAL Details:",obj.get("url").toString());
                        System.out.println(deals.get(i).toString());
                    }
                }
            }

        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(businessResponseJSON);
            System.exit(1);
        }

        System.out.println(response1);
        return result;
    }
    /**
     * Command-line interface for the sample Yelp API runner.
     */
    public static class YelpAPICLI {
        @Parameter(names = {"-q", "--term"}, description = "Search Query Term")
        public String term ;

        @Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
        public String location;

        public String getDeals = "false";
    }

    /* For Retrieving unique food types available*/

    public static HashSet<String> parseFoodTypes(String s)
    {
        HashSet<String> resultSet = new HashSet<String>();

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        ArrayList<String> lines = new ArrayList<String>();

        for(int i =0;i<s.length();i++)
        {
            String l = "";
            if(s.charAt(i)=='[')
            {
                i = i+1;
                while(s.charAt(i) != ']') {
                    l = l + s.charAt(i);
                    i++;
                }
                lines.add(l);
                System.out.println(l);
                System.out.println("SIZE OF LINES:"+ lines.size());
            }
        }

        if(lines.size() >= 1)
        {
            for(String line:lines) {
                String[] terms = line.split(",");
                if(terms.length >= 1)
                {
                    for(int j = 1;j<terms.length;j= j+2)
                    {
                        if(!resultSet.contains(terms[j]))
                            resultSet.add(terms[j].toLowerCase());

                        System.out.println(terms[j].toLowerCase());
                    }
                }
            }
        }

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(resultSet.size());
        for(String keyTerms:resultSet)
         System.out.println(keyTerms);

        return resultSet;
    }
}
