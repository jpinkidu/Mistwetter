package jpinkidu.mistwetter.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;

import jpinkidu.mistwetter.R;
import jpinkidu.mistwetter.adapter.DayAdapter;
import jpinkidu.mistwetter.weatherInfo.Day;

public class DailyForecastActivity extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        /*String[] daysOfTheWeek ={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysOfTheWeek);
        setListAdapter(adapter); */
        DayAdapter adapter = new DayAdapter(this,mDays );
        setListAdapter(adapter);
    }


}
