package jpinkidu.mistwetter.weatherInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pinkilu on 21.09.2015.
 */
public class CurrentWeather {

    private String mIcon;
    private String mTimezone;
    private String mSummery;
    private String mPrecipType;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private double mPressure;
    private double mWindSpeed;




//Getter and Getter
    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getSummery() {
        return mSummery;
    }

    public void setSummery(String summery) {
        mSummery = summery;
    }

    public long getTime() {
        return mTime;
    }

    //format the time
    public String getFormattedTime(){
       SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
       formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        //miliseconds * 1000
       Date dateTime = new Date(getTime() * 1000);
       String timeString = formatter.format(dateTime);

        return timeString;
   }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return (int)Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        double humidityValue = mHumidity *100;
        return humidityValue;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPrecipChance() {
        double precipValue = mPrecipChance*100;
        return precipValue;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getPrecipType() {
        return mPrecipType;
    }

    public void setPrecipType(String mPrecipType) {
        this.mPrecipType = mPrecipType;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        mPressure = pressure;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }
}
