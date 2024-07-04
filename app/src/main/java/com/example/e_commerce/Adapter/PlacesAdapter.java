package com.example.e_commerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.e_commerce.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends BaseAdapter {
    public final List<AutocompletePrediction> predictions = new ArrayList<>();
    public final Context context;

    public PlacesAdapter(Context context) {
        this.context = context;
    }

    public void setPredictions(List<AutocompletePrediction> predictions){
        this.predictions.clear();
        this.predictions.addAll(predictions);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return predictions.size();
    }

    @Override
    public Object getItem(int i) {
        return predictions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_place_item, viewGroup, false);
        TextView txtShortAddress = v.findViewById(R.id.txtShortAddress);
        TextView txtLongAddress = v.findViewById(R.id.txtLongAddress);

        txtShortAddress.setText(predictions.get(i).getPrimaryText(null));
        txtLongAddress.setText(predictions.get(i).getSecondaryText(null));
        return v;
    }
}