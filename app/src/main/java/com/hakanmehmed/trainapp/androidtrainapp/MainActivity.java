package com.hakanmehmed.trainapp.androidtrainapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity  {
    // TODO: change colors of layouts
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private LocationFragment locationFragment;
    private SearchJourneyFragment searchJourneyFragment;
    private SavedJourneyFragment savedJourneyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        locationFragment = new LocationFragment();
        searchJourneyFragment = new SearchJourneyFragment();
        savedJourneyFragment = new SavedJourneyFragment();
        setFragment(locationFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_locate:
                        setFragment(locationFragment);
                        return true;
                    case R.id.nav_search_journey:
                        setFragment(searchJourneyFragment);
//                        Intent in = new Intent(getBaseContext(), ScheduleActivity.class);
//                        startActivity(in);
                        return true;
                    case R.id.nav_saved_journey:
                        setFragment(savedJourneyFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

}
