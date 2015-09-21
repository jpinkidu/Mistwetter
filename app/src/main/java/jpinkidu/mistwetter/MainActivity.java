package jpinkidu.mistwetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "27974c4bc33201748eaf542a6769c3b7"; // from forecast.io
        double langitude = 52.379189;
        double latitude = 4.899431;  //Amsterdam
        String forecastURL = "https://api.forecast.io/forecast/" + apiKey +"/"+ langitude + ","
                +langitude;
        //create new okhttp client
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastURL)
                .build();

        Call response = client.newCall(request);




    }


}
