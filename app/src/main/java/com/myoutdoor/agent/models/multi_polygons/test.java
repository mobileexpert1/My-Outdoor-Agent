package com.myoutdoor.agent.models.multi_polygons;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class test {
    private ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>> data=new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>>();


    public  void calculate(){
        try {
            JSONObject object=new JSONObject("");
            JSONArray features=object.getJSONArray("features");

            for(int i=0;i<features.length();i++){

                JSONObject featureObject=features.getJSONObject(i).getJSONObject("geometry");
                if(featureObject.getString("type")=="MultiPolygon"){



                    Iterator<JSONArray> iterator = (Iterator<JSONArray>) featureObject.getJSONArray("coordinates");

                    while (iterator.hasNext()) {
                        System.out.println(iterator.next().toString());
                    }

                }
                if(featureObject.getString("type")=="Polygon"){

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
