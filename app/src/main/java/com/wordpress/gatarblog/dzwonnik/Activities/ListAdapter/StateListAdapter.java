package com.wordpress.gatarblog.dzwonnik.Activities.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.RingtoneState;

import java.util.ArrayList;

/**
 * Adaptor for ListView of all in-memory ringtone states (it's in MainActivity).
 */

public class StateListAdapter extends ArrayAdapter<RingtoneState> {

    private final Context context;
    private final int layoutResourceId;

    private final String SILENT;
    private final String VIBRA;

    public StateListAdapter(Context context, int layoutResourceId, ArrayList<RingtoneState> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        SILENT = context.getString(R.string.silent_mode);
        VIBRA = context.getString(R.string.vibration_mode);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RingtoneState state = getItem(position);
        StatusRowHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResourceId, null);
            holder = new StatusRowHolder();
            holder.textListAdapterTime = (TextView) convertView.findViewById(R.id.textListAdapterTime);
            holder.textListAdapterWeekdays = (TextView) convertView.findViewById(R.id.textListAdapterWeekdays);
            holder.textListAdapterOption = (TextView) convertView.findViewById(R.id.textListAdapterOption);
            convertView.setTag(holder);
        }else{
            holder = (StatusRowHolder) convertView.getTag();
        }
        holder.textListAdapterTime.setText(getTime(state));
        holder.textListAdapterWeekdays.setText(getWeekdays(state));
        holder.textListAdapterOption.setText(createTextOption(state));
        changeOptionColor(holder.textListAdapterOption);
        return convertView;
    }

    private String getTime(RingtoneState state){
        return state.getHour() + " : " + ((state.getMinute() > 9) ? state.getMinute() : ("0" + state.getMinute()));
    }

    private String getWeekdays(RingtoneState state){
        String weekdays = "";
        final String[] weekDayNameShort = {
                context.getString(R.string.monday),
                context.getString(R.string.tuesday),
                context.getString(R.string.wednesday),
                context.getString(R.string.thursday),
                context.getString(R.string.friday),
                context.getString(R.string.saturday),
                context.getString(R.string.sunday)};

        for (int i = 0; i < state.getWeekDays().length; i++) {
            if(state.getWeekDays()[i]) weekdays += weekDayNameShort[i] + " ";
        }
        return  weekdays;
    }

    private String createTextOption(RingtoneState state){
        if(state.isSilent()) return SILENT;
        if(state.isVibration()) return VIBRA;
        return state.getVolumeValue() + " / 7";
    }

    private void changeOptionColor(TextView textView){
        if(textView.getText().equals(SILENT)) textView.setTextColor(Color.RED);
        else if(textView.getText().equals(VIBRA)) textView.setTextColor(Color.BLUE);
        else textView.setTextColor(Color.WHITE);
    }

    private static class StatusRowHolder{
        TextView textListAdapterTime;
        TextView textListAdapterWeekdays;
        TextView textListAdapterOption;
    }
}
