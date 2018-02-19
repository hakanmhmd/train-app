package com.hakanmehmed.trainapp.androidtrainapp;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "LocationFragment";
    private static final int MY_LOCATION_PERMISSION_CODE = 101;
    private GoogleMap map;
    private FragmentActivity myContext;

    @BindView(R.id.floating_search_view)
    FloatingSearchView floating_search_view;

    @Override
    public void onAttach(Context activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        ButterKnife.bind(this, view);
        if (googleServicesAvailable()) {
            initMap();
            floating_search_view.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                }

                @Override
                public void onSearchAction(String currentQuery) {
                    Log.v(TAG, "CLICKED");
                    pointToLocation();
                }
            });

//            new View.OnKeyListener() {
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    // If the event is a key-down event on the "enter" button
//                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                        Log.v(TAG, "CLICKED");
//                        pointToLocation();
//                        return true;
//                    }
//                    return false;
//                }
//            });
        } else {
            // layout for not supported
            view = inflater.inflate(R.layout.not_supported, container, false);
        }

        return view;


    }

    private void pointToLocation() {
        String location = floating_search_view.getQuery();
        Log.v(TAG, location);

//            double latitude = addressList.get(0).getLatitude();
//            double longitude = addressList.get(0).getLongitude();
            goToLocation(51.509865, -0.118092, 6);
            //Toast.makeText(getContext(), "Could not pinpoint the train.", Toast.LENGTH_LONG).show();

    }

    private void goToLocation(double lat, double lon, float zoom) {
        LatLng ll = new LatLng(lat, lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(cameraUpdate);
    }

    private void initMap() {
        Log.v(TAG, "initialising map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);
        //MapFragment supportFragmentManager = (MapFragment) myContext.getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        //mapFragment.getMapAsync(this);
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Can not connect to Play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        goToLocation(52.837681, -1.4829797, 6);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
            }
        }

        map.setPadding(0, 10, 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "This app requires location permissions to be granted!", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;
        }
    }

}
