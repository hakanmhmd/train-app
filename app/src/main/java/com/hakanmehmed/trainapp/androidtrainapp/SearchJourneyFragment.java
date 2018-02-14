package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


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
    @BindView(R.id.loadingTvBig)
    TextView loadingTvBig;
    @BindView(R.id.loadingTvSmall)
    TextView loadingTvSmall;
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

        from_station.setText("London Euston");
        to_station.setText("Hatfield");


        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        recentSearches.setHasFixedSize(true);
        recentSearches.setLayoutManager(new LinearLayoutManager(getContext()));

        showRecentSearches();

        return view;
    }

    private void showRecentSearches() {
        ArrayList<RecentSearch> searches = Utils.getSearches(getContext());
//        List<RecentSearch> list = new ArrayList<>();
//        for (RecentSearch recentSearch : searches) {
//            if (recentSearch.getFrom() != null && recentSearch.getTo() != null) {
//                list.add(recentSearch);
//            }
//        }
//
//        searches = list.toArray(new RecentSearch[list.size()]);

        if(searches == null || searches.size() == 0){
            noRecentSearchTv.setVisibility(View.VISIBLE);
            recentSearches.setVisibility(View.GONE);
        } else {
            //Log.v(TAG, Arrays.toString(searches));
            noRecentSearchTv.setVisibility(View.GONE);
            recentSearches.setVisibility(View.VISIBLE);
            recentSearches.setAdapter(new RecentSearchAdapter(searches, getContext(), this));
        }
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
        Utils.saveSearch(from, to, getContext());

        recentSearchesLayout.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        loadingTvBig.setVisibility(VISIBLE);
        loadingTvBig.setText(R.string.finding_trains_text);
        loadingTvSmall.setVisibility(GONE);
        getTrains(from, to);

    }

    private void hideKeyboard(FragmentActivity context) {
        View view = context.getCurrentFocus();
        if(view != null){
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void fillUpFields(RecentSearch recentSearch) {
        from_station.setText(recentSearch.getFrom());
        to_station.setText(recentSearch.getTo());
        from_station.clearFocus();
        to_station.clearFocus();
    }

    private void getTrains(String from, String to) {
        Log.v(TAG, "Making api calls.");

        api.getTrains(TrainFinderAPI.buildApiQuery(from, to), new CustomCallback<JourneySearchResponse>() {
            @Override
            public void onSuccess(Response<JourneySearchResponse> response) {
                Log.d(TAG, "Api call successful!");
                showTrains(response.body());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "Api call failed!");
                showFailMessage();
            }
        });
    }

    private void showTrains(JourneySearchResponse response) {
        searchResults.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        loadingTvBig.setVisibility(GONE);
        loadingTvSmall.setVisibility(GONE);

        // response should be parsed to a list of Journey objects
        List<Journey> results = response.getJourneys();

        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        searchResults.setLayoutAnimation(animation);
        searchResults.setAdapter(new JourneySearchAdapter(results, getContext()));
    }

    private void showFailMessage() {
        searchResults.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        loadingTvBig.setVisibility(VISIBLE);
        loadingTvBig.setText(R.string.no_trains);
        loadingTvSmall.setVisibility(VISIBLE);
        loadingTvSmall.setText(R.string.connection_error_message);
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
