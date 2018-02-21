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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<Journey> subscribedJourneys;

    //@BindView(R.id.floating_search_view)
    //FloatingSearchView floating_search_view;
    @BindView(R.id.searchRoutes)
    InstantAutoComplete searchRoutes;

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

            subscribedJourneys = Utils.getSubscribedJourneys(getContext());
            String[] routes = new String[subscribedJourneys.size()];
            for (int i = 0; i < routes.length; i++) {
                Journey currentJourney = subscribedJourneys.get(i);
                routes[i] = currentJourney.getOrigin() + " to " + currentJourney.getDestination();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.select_dialog_item, routes);
            searchRoutes.setAdapter(adapter);
            searchRoutes.setInputType(InputType.TYPE_NULL);

            searchRoutes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchRoutes.setText("");
                }
            });

            searchRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Journey journey = subscribedJourneys.get(i);
                    map.clear();
                    drawRoute(journey);
                }
            });
        } else {
            // layout for not supported
            view = inflater.inflate(R.layout.not_supported, container, false);
        }

        return view;
    }

    private void drawRoute(Journey route) {
        List<JourneyLeg> legs = route.getLegs();
        for (JourneyLeg leg : legs) {
            String origin = StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode());
            String dest = StationUtils.getNameFromStationCode(leg.getDestination().getStationCode());
            String deptTime = leg.getOrigin().getScheduledTime();

            String url = getDirectionsURL(origin, dest, Utils.getMilliseconds(deptTime));
            DirectionsData dd = new DirectionsData();
            dd.execute(map, url, this);
        }
    }

    private void goToLocation(double lat, double lon, float zoom) {
        LatLng ll = new LatLng(lat, lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(cameraUpdate);
    }

    private void initMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(this);
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

        map.setPadding(0, 18, 0, 0);
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


    public String getDirectionsURL(String origin, String dest, long milliseconds) {
        StringBuilder directions = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        directions.append("origin=").append(origin.replaceAll("\\s", "+")).append(",UK");
        directions.append("&destination=").append(dest.replaceAll("\\s", "+")).append(",UK");
        directions.append("&departure_time=").append(milliseconds);
        directions.append("&key=AIzaSyB9bCyV8KuYf87ov1r0EBgpdBob8sildxo");
        directions.append("&mode=transit").append("&transit_mode=train");

        Log.v(TAG, directions.toString());

        return directions.toString();
    }

    public void notifyNoDirections() {
        Toast.makeText(getContext(), "Can't draw the route.", Toast.LENGTH_LONG).show();
    }
}
