package com.hakanmehmed.trainapp.androidtrainapp;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hakanmehmed on 20/02/2018.
 */

class DirectionsData extends AsyncTask<Object, String, String>{
    private String url;
    private GoogleMap map;
    private String directions;
    private LocationFragment fragment;

    @Override
    protected String doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        url = (String) objects[1];
        fragment = (LocationFragment) objects[2];

        directions = readUrl(url);
        return directions;
    }


    @Override
    protected void onPostExecute(String s) {
        String[] directionsList = parseData(s);
        if(directionsList == null){
            fragment.notifyNoDirections();
        } else {
            displayDirections(directionsList);
        }
    }

    private void displayDirections(String[] directionsList) {
        for (int i = 0; i < directionsList.length; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(15);
            options.addAll(PolyUtil.decode(directionsList[i]));

            map.addPolyline(options);
        }
    }

    private String readUrl(String urlString) {
        String data = "";
        InputStream is = null;
        HttpURLConnection httpUrlConn = null;
        try {
            URL url = new URL(urlString);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.connect();

            is = httpUrlConn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();

            String line;
            while((line = br.readLine()) != null) sb.append(line);
            data = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpUrlConn.disconnect();
        }

        return data;
    }

    private String[] parseData(String json) {
        JSONArray jsonArray = null;
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            JSONArray routes = obj.getJSONArray("routes");
            if(routes.length() == 0) return null;
            jsonArray = routes.getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0)
                    .getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPaths(jsonArray);
    }

    private String[] getPaths(JSONArray path){
        String[] polylines = new String[path.length()];

        for (int i = 0; i < polylines.length; i++) {
            try {
                polylines[i] = path.getJSONObject(i).getJSONObject("polyline").getString("points");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

}
