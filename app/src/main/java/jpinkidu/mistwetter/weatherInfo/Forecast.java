package jpinkidu.mistwetter.weatherInfo;

/**
 * Created by pinkilu on 22.09.2015.
 */
public class Forecast {
    private CurrentWeather mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public CurrentWeather getCurrent() {
        return mCurrent;
    }

    public void setCurrent(CurrentWeather current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }
}
