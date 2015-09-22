package jpinkidu.mistwetter.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Import OkHttp
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import jpinkidu.mistwetter.weatherInfo.CurrentWeather;
import jpinkidu.mistwetter.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    //Connect UI with data
    //mTimeLabel, mTempLabel
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.tempLabel) TextView mTempLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.windspeedValue) TextView mWindspeedValue;
    @Bind(R.id.pressureValue) TextView mPressureValue;
    @Bind(R.id.iconImage) ImageView mIconImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //API key
        String apiKey = "27974c4bc33201748eaf542a6769c3b7"; // from forecast.io
        //coordinates
        double latitude = 52.379189;
        double longitude = 4.899431;  //Amsterdam
        //URL
        String forecastURL = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + ","
                + longitude+"?units=si";

        if (isNetworkAvailable()) {
            //create new okhttp client
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
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
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
         }
        Log.d(TAG, "UI code is running");
    }

    private void updateDisplay() {
        mTempLabel.setText(mCurrentWeather.getTemperature() + "");
        mTimeLabel.setText("At " +mCurrentWeather.getFormattedTime() + " it will be:");
        mHumidityValue.setText(mCurrentWeather.getHumidity()+ " %");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance()+" %");
        mWindspeedValue.setText(mCurrentWeather.getWindSpeed()+" m/s");
        mPressureValue.setText(mCurrentWeather.getPressure()+" hPa");

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
        //currentWeather.setPrecipType(currently.getString("mprecipType"));
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


    }

