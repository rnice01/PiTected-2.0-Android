package com.example.rnice01.pitected2.logs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.rnice01.pitected2.R;
import com.example.rnice01.pitected2.objects.SystemLog;

/**
 * Created by rnice01 on 3/17/2016.
 */
public class SystemLogAdapter extends BaseAdapter {
    ArrayList<SystemLog> list;
    private Activity context1;
    ViewHolder viewHolder;

    public SystemLogAdapter(Activity context, ArrayList<SystemLog> items)
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
            convertView = LayoutInflater.from(context1).inflate(R.layout.system_log_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.status = (TextView) convertView.findViewById(R.id.system_status);
            viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.system_timestamp);

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


        if(list.get(position).getStatus().equals("0")){
            viewHolder.status.setText("Disarmed system at");
        }
        else if(list.get(position).getStatus().equals("1")){
            viewHolder.status.setText("Armed system at");
        }
        viewHolder.username.setText("By: " + list.get(position).getUsername());
        viewHolder.timestamp.setText(list.get(position).getTimeStamp());



        return convertView;
    }

    public class ViewHolder{
        public TextView username;
        public TextView timestamp;
        public TextView status;
    }
}
