package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchJourneyFragment extends Fragment {

    private static final String TAG = "SearchJourneyFragment";

    @BindView(R.id.searchFormLayout)
    RelativeLayout searchFormLayout;
    @BindView(R.id.to_station) // TODO: no correction on these field and change color of cursor
    AutoCompleteTextView to_station;
    @BindView(R.id.from_station)
    AutoCompleteTextView from_station;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.reverseIcon)
    ImageView reverseIcon;

    @BindView(R.id.recentSearchesLayout)
    RelativeLayout recentSearchesLayout;
    @BindView(R.id.recentSearches)
    RecyclerView recentSearches;
    @BindView(R.id.noRecentSearchTv)
    TextView noRecentSearchTv;

    @BindView(R.id.resultsLayout)
    LinearLayout resultsLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.loadingTv)
    TextView loadingTv;
    @BindView(R.id.loadingTv2)
    TextView loadingTv2;
    @BindView(R.id.searchResults)
    RecyclerView searchResults;


    @OnClick(R.id.reverseIcon) void iconPressed() {
        swapSearchInputTextField();
    }
    
    @OnClick(R.id.searchButton) void buttonPressed(){
        findTrains();
    }

    private TrainFinderAPI api;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_journey, container, false);

        ButterKnife.bind(this, view);

        api = new TrainFinderAPI();

        String[] stations = StationUtils.getStations(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, stations);

        // Auto-completion functionality for the two fields
        to_station.setAdapter(adapter);
        from_station.setAdapter(adapter);

        return view;
    }

    private void findTrains() {
        String from = from_station.getText().toString();
        String to = to_station.getText().toString();

        String errorMsg = StationUtils.isInputValid(from, to);
        if(errorMsg != null){

            // TODO: pop up dialog instead of toast
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
//            new AlertDialog.Builder(getActivity().getApplicationContext())
//            .setCancelable(false)
//            .setTitle(getString(R.string.error_text))
//            .setMessage(errorMsg)
//            .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();

            return;
        }

        hideKeyboard(getActivity());
        //StationUtils.saveRecentSearch(from, to, getContext());

        recentSearchesLayout.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        loadingTv.setVisibility(View.VISIBLE);
        loadingTv2.setVisibility(View.VISIBLE);
        loadingTv.setText(R.string.finding_trains_text);
        loadingTv2.setText("LALAL");

        getTrains(from, to);

    }

    private void hideKeyboard(FragmentActivity context) {
        View view = context.getCurrentFocus();
        if(view != null){
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getTrains(String from, String to) {
        Log.v(TAG, "Making api calls.");

        api.getTrains(TrainFinderAPI.buildApiQuery(from, to), view);

//                new CustomListeners.TrainlineCallback() {
//            @Override
//            public void onResponse(Response<JourneyData> response) {
//                Log.d(TAG, context.getString(R.string.requestSuccess, context.getString(R.string.endpointJourneys)));
//                view.showJourneys(response.body());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(TAG, context.getString(R.string.requestFail, context.getString(R.string.endpointJourneys)));
//                view.failedGettingJourneys();
//                t.printStackTrace();
//            }
//        });
    }

    public void swapSearchInputTextField(){
        String from = from_station.getText().toString();
        String to = to_station.getText().toString();

        from_station.setText(to);
        to_station.setText(from);

        from_station.clearFocus();
        to_station.clearFocus();
    }


    public SearchJourneyFragment() {
        // Required empty public constructor
    }







}
