package jpinkidu.mistwetter.weatherInfo;

/**
 * Created by pinkilu on 22.09.2015.
 */
public class Hour {

    private String mIcon;
    private String mTimezone;
    private String mSummary;
    private long mTime;
    private double mTemperature;


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

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }
}
