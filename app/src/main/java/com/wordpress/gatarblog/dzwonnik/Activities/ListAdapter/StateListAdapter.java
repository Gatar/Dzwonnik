package com.wordpress.gatarblog.dzwonnik.Activities.ListAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wordpress.gatarblog.dzwonnik.R;
import com.wordpress.gatarblog.dzwonnik.RingtoneState;

import java.util.ArrayList;

/**
 * Adaptor for ListView of all in-memory ringtone states (it's in MainActivity).
 */

public class StateListAdapter extends ArrayAdapter<RingtoneState> {

    private Context context;
    private int layoutResourceId;

    private final String SILENT = "Cisza";
    private final String VIBRA = "Vibra";

    public StateListAdapter(Context context, int layoutResourceId, ArrayList<RingtoneState> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
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
        String[] weekDayNameShort = {"Pon", "Wt", "Sr", "Czw", "Pt", "So", "Nd"};
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
        else textView.setTextColor(Color.BLACK);
    }

    private static class StatusRowHolder{
        TextView textListAdapterTime;
        TextView textListAdapterWeekdays;
        TextView textListAdapterOption;
    }
}
