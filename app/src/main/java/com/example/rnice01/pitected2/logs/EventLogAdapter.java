package com.example.rnice01.pitected2.logs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.rnice01.pitected2.R;
import com.example.rnice01.pitected2.objects.Events;

/**
 * Created by rnice01 on 3/17/2016.
 */
public class EventLogAdapter extends BaseAdapter{
    ArrayList<Events> list;
    private Activity context1;
    ViewHolder viewHolder;

    public EventLogAdapter(Activity context, ArrayList<Events> items)
    {
        context1 = context;
        this.list = items;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context1).inflate(R.layout.events_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.sensor_name);
            viewHolder.status = (TextView) convertView.findViewById(R.id.sensor_status);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.sensor_timestamp);
            /**When the List View is first created, create a row with the custom layout
             * instance and store it to later add texts and images
             */
            convertView.setTag(viewHolder);
        }
        else{
            /**
             * Once the instance of the row item's control it will use from
             * already created controls which are stored in convertView as a
             * ViewHolder Instance
             * */
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(list.get(position).getEventSensor());
        if(list.get(position).getEventStatus().equals("1")){
            viewHolder.status.setText("Opened/Motion Detected");
        }
        else if(list.get(position).getEventStatus().equals("0")){
            viewHolder.status.setText("Closed/No Motion Detected");
        }
        viewHolder.timestamp.setText(list.get(position).getEventDate());



        return convertView;
    }

    public class ViewHolder{
        public TextView name;
        public TextView status;
        public TextView timestamp;
    }
}
