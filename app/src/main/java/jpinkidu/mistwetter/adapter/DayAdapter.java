package jpinkidu.mistwetter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jpinkidu.mistwetter.R;
import jpinkidu.mistwetter.weatherInfo.Day;

/**
 * Created by pinkilu on 22.09.2015.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    //constructor
    public DayAdapter(Context context, Day[] days){
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.iconImageView);
            holder.tempLabel = (TextView)convertView.findViewById(R.id.tempLabel);
            holder.dayLabel = (TextView)convertView.findViewById(R.id.dayNameLabel);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        Day day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.tempLabel.setText(day.getTemperatureMax() + "");

        if (position == 0){
            holder.dayLabel.setText("Today");
        }else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        return convertView;
    }

    public static class ViewHolder{
        ImageView iconImageView;
        TextView tempLabel;
        TextView dayLabel;


    }
}
