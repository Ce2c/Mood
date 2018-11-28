package com.example.ice2c.mood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String apiSentence = "api_key=d52ff92c5f7b97bab473655b0ec1141e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBestMovies();
    }

    public void getBestMovies(){
        String url;
        url = "https://api.themoviedb.org/3/discover/movie?"+apiSentence+"&sort_by=popularity.desc";
        getDataFromAPI(url);
    }

    public void readMapsInsideList(List list){
        HashMap<String, Object> innerMap;
        for(int i=0;i<list.size();i++)
        {
            innerMap = (HashMap<String, Object>) list.get(i);
            System.out.println("______________NEW MOVIE (Infos are below)________________");
            for (Map.Entry<String, Object> entry : innerMap.entrySet())
            {
                System.out.println("KEY : "+entry.getKey() + " ; VALUE : "  + entry.getValue());
            }
        }
    }

    public void getDataFromAPI (String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Map> list = new ArrayList<>();
                            String results = response.getString("results");
                            results = results.replace("[{", "").replace("}]", "");
                            String[] resultArray = results.split("\\},\\{");
                            for (String aResultArray : resultArray) {
                                String[] arr = aResultArray.split(",(?=(?:\"[^\"]*\"|\\([^()]*\\)|\\[[^\\[\\]]*\\]|\\{[^{}]*\\}|[^\"\\[{}()\\]])*$)");
                                Map<String, String> dictionary = new HashMap<>();
                                for (String str : arr) {
                                    str = str.replace("\"","");
                                    String[] keyValueResult = str.split(":");
                                    dictionary.put(keyValueResult[0],keyValueResult[1]);
                                }
                                list.add(dictionary);
                            }
                            readMapsInsideList(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
