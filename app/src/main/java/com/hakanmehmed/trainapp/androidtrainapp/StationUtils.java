package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import android.util.Log;

/**
 * Created by hakanmehmed on 11/02/2018.
 */

public class StationUtils {
    private static final String TAG = "StationUtils";
    private static HashMap<String, String> stations = null;

    public StationUtils(){

    }

    static String[] getStations(Context context){
        if(stations == null){
            stations = new HashMap<>();
            populateMap(context);
        }
        Set<String> set = stations.keySet();
        Log.v(TAG, String.valueOf(set.size()));
        String[] stationNames = new String[set.size()];

        int i=0;
        for (String stationName : set) {
            stationNames[i++] = stationName;
        }
        return stationNames;
    }

    static String getNameFromStationCode(String code, Context context){
        if(stations == null){
            stations = new HashMap<>();
            populateMap(context);
        }
        if(stations.containsValue(code)){
            for (String key : stations.keySet()) {
                if (stations.get(key).equals(code)) {
                    return key;
                }
            }
        }
        return null;
    }

    static String getCodeFromStationName(String station){
        String code = stations.get(station);
        if(code == null){
            Log.v(TAG, "INVALID STATION");
        }
        return code;
    }

    private static void populateMap(Context context) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("stations.txt")));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String key = parts[0];
                String value = parts[1];
                stations.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String isInputValid(String from, String to) {
        if(from == null || from.isEmpty()){
            return "From station is missing.";
        }

        if(to == null || to.isEmpty()){
            return "To station is missing";
        }
        // Check if stations exist
        if(!stations.containsKey(from)){
            return "From station is not valid.";
        }
        if(!stations.containsKey(to)){
            return "To station is not valid.";
        }

        return null;

    }
}

