package com.hakanmehmed.trainapp.androidtrainapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @BindView(R.id.to_station)
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

    private void findTrains() {
        String from = from_station.getText().toString();
        String to = to_station.getText().toString();

        String error = StationUtils.isInputValid(from, to);
        if(error != null){
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        if(currentFocus != null){
            manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        //StationUtils.saveRecentSearch(from, to, getContext());

        recentSearchesLayout.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        loadingTv.setVisibility(View.VISIBLE);
        loadingTv2.setVisibility(View.VISIBLE);
        loadingTv.setText(R.string.finding_trains_text);
        loadingTv2.setText("LALAL");

        makeAPIcalls();

    }

    private void makeAPIcalls() {
        Log.v(TAG, "Making api calls.");
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_journey, container, false);

        ButterKnife.bind(this, view);


        String[] stations = StationUtils.getStations(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, stations);

        // Auto-completion functionality for the two fields
        to_station.setAdapter(adapter);
        from_station.setAdapter(adapter);

        return view;
    }



}
