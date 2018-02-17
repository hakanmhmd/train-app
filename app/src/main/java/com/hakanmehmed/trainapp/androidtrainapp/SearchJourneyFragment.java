package com.hakanmehmed.trainapp.androidtrainapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


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

    private JourneyFinderApi api;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_journey, container, false);

        ButterKnife.bind(this, view);

        api = new JourneyFinderApi();

        String[] stations = StationUtils.getStations();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_dropdown_item_1line, stations);

        // Auto-completion functionality for the two fields
        to_station.setPaintFlags(INVISIBLE);
        from_station.setPaintFlags(INVISIBLE);
        to_station.setAdapter(adapter);
        from_station.setAdapter(adapter);

        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        recentSearches.setHasFixedSize(true);
        recentSearches.setLayoutManager(new LinearLayoutManager(getContext()));

        showRecentSearches();

        return view;
    }

    private void showRecentSearches() {
        ArrayList<RecentSearch> searches = Utils.getSearches(getContext());

        if(searches == null || searches.size() == 0){
            noRecentSearchTv.setVisibility(View.VISIBLE);
            recentSearches.setVisibility(View.GONE);
        } else {
            //Log.v(TAG, searches);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(R.string.cant_proceed)
                .setMessage(errorMsg)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            AlertDialog ad = builder.create();
            ad.show();

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
        api.getJourneys(JourneyFinderApi.buildApiQuery(from, to), new CustomCallback<JourneySearchResponse>() {
            @Override
            public void onSuccess(Response<JourneySearchResponse> response) {
                Log.d(TAG, "API call successful!");
                showTrains(response.body());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "API call failed!");
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
        searchResults.setAdapter(new SearchJourneyAdapter(results, getContext(), this));
    }

    void displayJourneyInfo(final Journey thisJourney){
        final CustomSearchResultAlertDialog customSearchResultAlertDialog =
                new CustomSearchResultAlertDialog(getActivity());

        customSearchResultAlertDialog.inflateDialog(thisJourney);
        customSearchResultAlertDialog.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisJourney.setReminder(customSearchResultAlertDialog.getReminder());
                Utils.subscribedToJourney(thisJourney, getContext());
                customSearchResultAlertDialog.cancel();
            }
        });

        customSearchResultAlertDialog.show();
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
