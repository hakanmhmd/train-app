package com.hakanmehmed.trainapp.androidtrainapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    // TODO: change colors of layouts

    @BindView(R.id.main_nav)
    BottomNavigationView main_nav;
    @BindView(R.id.main_frame)
    FrameLayout main_frame;

    private LocationFragment locationFragment;
    private SearchJourneyFragment searchJourneyFragment;
    private SubscribeJourneyFragment savedJourneyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        locationFragment = new LocationFragment();
        searchJourneyFragment = new SearchJourneyFragment();
        savedJourneyFragment = new SubscribeJourneyFragment();
        setFragment(locationFragment);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_locate:
                        setFragment(locationFragment);
                        return true;
                    case R.id.nav_search_journey:
                        setFragment(searchJourneyFragment);
                        return true;
                    case R.id.nav_saved_journey:
                        setFragment(savedJourneyFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        StationUtils.init(getApplicationContext());

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

}
