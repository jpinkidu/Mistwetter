package jpinkidu.mistwetter.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Import OkHttp
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jpinkidu.mistwetter.weatherInfo.CurrentWeather;
import jpinkidu.mistwetter.R;
import jpinkidu.mistwetter.weatherInfo.Day;
import jpinkidu.mistwetter.weatherInfo.Forecast;
import jpinkidu.mistwetter.weatherInfo.Hour;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    //private CurrentWeather mCurrentWeather;
    private Forecast mForecast;

    //Connect UI with data
    //mTimeLabel, mTempLabel
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.tempLabel) TextView mTempLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.windspeedValue) TextView mWindspeedValue;
    @Bind(R.id.pressureValue) TextView mPressureValue;
    @Bind(R.id.iconImage) ImageView mIconImage;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 52.379189;
        final double longitude = 4.899431;  //Amsterdam
        //refresh data for the given coordinates
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });



        getForecast(latitude, longitude);

        Log.d(TAG, "UI code is running");
    }

    private void getForecast(double latitude, double longitude) {
        //API key
        String apiKey = "27974c4bc33201748eaf542a6769c3b7"; // from forecast.io
        //URL ; convert default us units in si - units
        String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + ","
                + longitude+"?units=si";

        if (isNetworkAvailable()) {
            //refresh button
            toggleRefresh();
            //create new okhttp client
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {

                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    }catch (IOException e){
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }else{
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
         }
    }

    //setting visibility of the refresh button and progressbar
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {
        CurrentWeather currentWeather = mForecast.getCurrent();
        mTempLabel.setText(currentWeather.getTemperature() + "");
        mTimeLabel.setText("At " +currentWeather.getFormattedTime() + " it will be:");
        mHumidityValue.setText(currentWeather.getHumidity()+ " %");
        mPrecipValue.setText(currentWeather.getPrecipChance()+" %");
        mWindspeedValue.setText(currentWeather.getWindSpeed()+" m/s");
        mPressureValue.setText(currentWeather.getPressure()+" hPa");
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        mIconImage.setImageDrawable(drawable);

    }
    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for (int i = 0; i < data.length(); i++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }

        return days;
    }


    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for (int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;
    }


    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        //create new JSONObject
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);


        JSONObject currently = forecast.getJSONObject("currently");
        //get information from json
        //currently: time, summary, icon, precipProbability, mprecipType, temperature, humidity, windSpeed, pressure
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setSummery(currently.getString("summary"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        //urrentWeather.setPrecipType(currently.getString("mprecipType"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setWindSpeed(currently.getDouble("windSpeed"));
        currentWeather.setPressure(currently.getDouble("pressure"));
        currentWeather.setTimezone(timezone);


        return currentWeather;

    }


    private void alertUserAboutError(){
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    /*@OnClick (R.id.weekButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        startActivity(intent);
    } */


}

