package com.example.e_commerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Activity.message.MessagesAdapter;
import com.example.e_commerce.Model.NotificationModel;
import com.example.e_commerce.R;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private final Context context;
    private List<NotificationModel> list;

    public NotificationAdapter(List<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.notificationTitle);
        TextView content = convertView.findViewById(R.id.notificationContent);

        NotificationModel notification = list.get(position);

        title.setText(notification.getTitle());
        content.setText(notification.getContent());

        return convertView;
    }

    public void updateNotification(List<NotificationModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
