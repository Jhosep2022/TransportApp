package com.example.apptransport.Adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;

import java.util.List;

/**
 * Created by Admin on 19/1/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<InfoTravelEntity> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mtv_blah;
        public TextView mtv_amount;
        public TextView mtv_isProcesCurrentAcount;
        public TextView textViewCliente;


        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.car_travel);
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            mtv_amount = (TextView) v.findViewById(R.id.tv_amount);
            mtv_blah = (TextView) v.findViewById(R.id.tv_blah);
            textViewCliente= v.findViewById(R.id.text_cliente);
            mtv_isProcesCurrentAcount = (TextView) v.findViewById(R.id.tv_isProcesCurrentAcount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<InfoTravelEntity> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_history, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context= parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getCodTravel());
        holder.mtv_blah.setText(mDataset.get(position).getNameOrigin());

        if (mDataset.get(position).client!=null){
            holder.textViewCliente.setText(mDataset.get(position).client);
        }

        if(HomeChofer.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP == 1)
        {
            if (mDataset.get(position).getTotalAmount()!=null){
                holder.mtv_amount.setText(mDataset.get(position).getTotalAmount());
            }

        }else {
            holder.mtv_amount.setText(context.getString(R.string.informaión_reservada));
        }


        if(mDataset.get(position).getNameStatusTravel() != null) {

            if (mDataset.get(position).getIsProcesCurrentAcount() == 1) {
                holder.mtv_isProcesCurrentAcount.setText("(" + mDataset.get(position).getNameStatusTravel() + ") - Liquidado!");
            } else {

                if(mDataset.get(position).getIdStatusTravel() == 2 &&
                        mDataset.get(position).getIsAceptReservationByDriver() == 1    ){
                    holder.mtv_isProcesCurrentAcount.setText("(Chofer asignado)");

                }else{
                    holder.mtv_isProcesCurrentAcount.setText("(" + mDataset.get(position).getNameStatusTravel() + ")");

                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}