package com.hakanmehmed.trainapp.androidtrainapp;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "LocationFragment";
    private static final int MY_LOCATION_PERMISSION_CODE = 101;
    private GoogleMap map;
    private FragmentActivity myContext;
    private ArrayList<Journey> subscribedJourneys;
    private LiveDataFeedApi api;
    private LatLng myPosition;

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
            api = new LiveDataFeedApi();
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
                    getCurrentLocation(journey);
                }
            });
        } else {
            // layout for not supported
            view = inflater.inflate(R.layout.not_supported, container, false);
        }

        return view;
    }

    public void getCurrentLocation(final Journey journey){
        final List<LiveDataSearchResponse> liveDataSearchResponses = new ArrayList<>();

        for(JourneyLeg leg : journey.getLegs()){
            final String trainId = leg.getTrainId();

            if(leg.getTransportMode().equals("Walk") || trainId == null){
                liveDataSearchResponses.add(null);
                continue;
            }

            // if this is not the current leg, continue
            String currentTime = Utils.getCurrentTime();
            String originScheduledTime = leg.getOrigin().getScheduledTime();
            String destScheduledTime = leg.getDestination().getScheduledTime();
            if(!Utils.isDateBetween(currentTime, originScheduledTime, destScheduledTime)){
                continue;
            }

            String time = journey.getDepartureDateTime();
            api.getLiveData(trainId, time, new CustomCallback<LiveDataSearchResponse>() {
                @Override
                public void onSuccess(Response<LiveDataSearchResponse> response) {
                    liveDataSearchResponses.add(response.body());
                    if(liveDataSearchResponses.size() == 1){
                        putOnMap(journey, liveDataSearchResponses);
                    }
                }


                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }


    }

    private void putOnMap(Journey journey, List<LiveDataSearchResponse> liveDataSearchResponses) {
        HashMap<String, LiveDataSearchResponse> legInfo = new HashMap<>();
        for(LiveDataSearchResponse train : liveDataSearchResponses){
            if(train == null || train.getService() == null) continue;

            String trainId = train.getService().getServiceUid();
            legInfo.put(trainId, train);
        }

        String currentStation = "";
        String message = "";
        for (String trainId : legInfo.keySet()) {
            LiveDataSearchResponse data = legInfo.get(trainId);
            List<Stop> stops = data.getService().getStops();
            for (int i = 0; i < stops.size(); i++) {
                Stop stop = stops.get(i);
                String station = stop.getLocation().getCrs();
                boolean isStartingStation = stop.getArrival().getNotApplicable() != null
                        && stop.getArrival().getNotApplicable();
                boolean isEndingStation = stop.getDeparture().getNotApplicable() != null
                        && stop.getDeparture().getNotApplicable();

                boolean arrived = isStartingStation || (stop.getArrival().getRealTime() != null
                        && stop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrived());

                boolean departed = isEndingStation || (stop.getDeparture().getRealTime() != null
                        && stop.getDeparture().getRealTime().getRealTimeServiceInfo().getHasDeparted());

                if(arrived && !departed && !isStartingStation && !isEndingStation){
                    currentStation = station;
                    message = "Currently at " + StationUtils.getNameFromStationCode(station);
                }

                if(!isEndingStation) {
                    Stop nextStop = stops.get(i + 1);
                    boolean nextArrived = nextStop.getArrival().getRealTime() != null
                            && nextStop.getArrival().getRealTime().getRealTimeServiceInfo().getHasArrived();

                    String nextStation = nextStop.getLocation().getCrs();
                    if (departed && !nextArrived) {

                        //between station and nextStation
                        currentStation = station;
                        message = "Travelling between " + StationUtils.getNameFromStationCode(station)
                                + " and " + StationUtils.getNameFromStationCode(nextStation);

                    }
                }
            }
        }

        Log.v(TAG, message);
        LatLng ll = StationUtils.getLatLngFromStationCode(currentStation);
        if(ll != null){
            Marker marker = map.addMarker(new MarkerOptions()
                            .title(journey.getOrigin() + " to " + journey.getDestination())
                            .snippet(message)
                            .draggable(false)
                            .position(ll)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            startMarkerAnim(marker);

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            getLocation();
            b.include(this.myPosition);
            b.include(marker.getPosition());
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 150));
            map.moveCamera(CameraUpdateFactory.zoomTo(map.getCameraPosition().zoom - 0.5f));
        } else {
            Toast.makeText(getContext(), "This journey has not started or has ended.", Toast.LENGTH_LONG).show();
        }

    }

    private void startMarkerAnim(final Marker marker) {
        final LatLng target = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point targetPoint = proj.toScreenLocation(target);
        final long duration = (long) (200 + (targetPoint.y * 0.6));
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.y = 0;
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearOutSlowInInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later == 60 frames per second
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void drawRoute(Journey route) {
        List<JourneyLeg> legs = route.getLegs();
        for (JourneyLeg leg : legs) {
            String origin = StationUtils.getNameFromStationCode(leg.getOrigin().getStationCode());
            String dest = StationUtils.getNameFromStationCode(leg.getDestination().getStationCode());
            String deptTime = leg.getOrigin().getScheduledTime();

            String url = getDirectionsURL(origin, dest, Utils.getSeconds(deptTime));
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
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);

            getLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
            }
        }

        map.setPadding(0, 18, 0, 0);
    }

    private void getLocation(){
        LocationManager locationManager = (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        android.location.Location location =
                locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            this.myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        }
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


    public String getDirectionsURL(String origin, String dest, long seconds) {
        StringBuilder directions = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        directions.append("origin=").append(origin.replaceAll("\\s", "+")).append(",UK");
        directions.append("&destination=").append(dest.replaceAll("\\s", "+")).append(",UK");
        directions.append("&departure_time=").append(seconds-60); // one minute before
        directions.append("&key=AIzaSyB9bCyV8KuYf87ov1r0EBgpdBob8sildxo");
        directions.append("&mode=transit").append("&transit_mode=train");

        Log.v(TAG, directions.toString());

        return directions.toString();
    }

    public void notifyNoDirections() {
        Toast.makeText(getContext(), "Can't draw the route.", Toast.LENGTH_LONG).show();
    }
}
