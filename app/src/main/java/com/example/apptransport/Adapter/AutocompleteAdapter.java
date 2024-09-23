package com.example.apptransport.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.AutoCompletePredictionWithType;
import com.apreciasoft.mobile.asremis.R;

import com.apreciasoft.mobile.asremis.Util.Globales;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AutocompleteAdapter extends RecyclerView.Adapter<AutocompleteAdapter.MyViewHolder>{

    private List<AutoCompletePredictionWithType> mDataset;
    private Context mContext;

    public AutocompleteAdapter(List<AutoCompletePredictionWithType> dataset){
        this.mDataset=dataset;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_address_rv, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        mContext= parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(mDataset.get(position).autocompleteType== Globales.AutocompleteType.ADDRESS)
        {
            holder.mPrimaryText.setText(mDataset.get(position).autocompletePrediction.getPrimaryText(null));
            holder.mSecondaryText.setText(mDataset.get(position).autocompletePrediction.getSecondaryText(null));
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIconSelectLocationInMap.setVisibility(View.INVISIBLE);

        }else if(mDataset.get(position).autocompleteType== Globales.AutocompleteType.SELECT_ADDRESS_FROM_MAP)
        {
            holder.mPrimaryText.setText(mContext.getString(R.string.fija_ubicacion_en_mapa));
            holder.mSecondaryText.setText("");
            holder.mIcon.setVisibility(View.INVISIBLE);
            holder.mIconSelectLocationInMap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mPrimaryText;
        public TextView mSecondaryText;
        public ImageView mIcon;
        public ImageView mIconSelectLocationInMap;

        public MyViewHolder(View v) {
            super(v);
            mPrimaryText =  v.findViewById(R.id.primary_text);
            mSecondaryText =  v.findViewById(R.id.secondary_text);
            mIcon = v.findViewById(R.id.imgIcon);
            mIconSelectLocationInMap= v.findViewById(R.id.imgIconSelectLocationInMap);
        }
    }
}
