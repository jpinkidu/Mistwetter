package jpinkidu.mistwetter.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import jpinkidu.mistwetter.R;

import jpinkidu.mistwetter.adapter.HourAdapter;

import jpinkidu.mistwetter.weatherInfo.Hour;

public class HourlyForecastActivity extends AppCompatActivity {

    private Hour[] mHours;
    @Bind(R.id.recylerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(this,mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // for performance reasons
        mRecyclerView.setHasFixedSize(true);

    }

   }
