package com.example.apptransport.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by usario on 25/4/2017.
 */


public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.MyViewHolder>  {

    private List<InfoTravelEntity> mDataset;
    private Context contextView;
    private ListenerReserva listenerReserva;
    private boolean isClient;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitulo;
        public TextView textViewCliente;
        public TextView textViewOrigen;
        public TextView textViewDestino;
        private TextView textViewFecha;
        public TextView textViewStatus;
        private ImageButton imageButtonMore;
        private ImageView imageViewTipeView;

        public MyViewHolder(View v) {
            super(v);

            textViewTitulo =  v.findViewById(R.id.tv_text);
            textViewCliente =  v.findViewById(R.id.text_cliente);
            textViewOrigen =  v.findViewById(R.id.text_origen);
            textViewDestino =  v.findViewById(R.id.text_destino);
            textViewFecha = v.findViewById(R.id.text_fecha);
            textViewStatus =  v.findViewById(R.id.text_status_reserva);
            imageViewTipeView = v.findViewById(R.id.imageButton);
            imageButtonMore = v.findViewById(R.id.imageButton3);
        }

    }

    public ReservationsAdapter(List list,ListenerReserva listenerReserva,boolean isClient) {
        mDataset= list;
        this.listenerReserva= listenerReserva;
        this.isClient= isClient;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_reservation, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        contextView= parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textViewTitulo.setText(mDataset.get(position).getCodTravel());

        if (isClient){
            holder.imageViewTipeView.setImageResource(R.drawable.ic_taxi);
        }

        if (isClient){

            if (mDataset.get(position).getDriver()!=null && !mDataset.get(position).getDriver().trim().isEmpty()){
                holder.textViewCliente.setText(mDataset.get(position).getDriver());
            }

        }else {
            if (mDataset.get(position).getClient()!=null && !mDataset.get(position).getClient().trim().isEmpty()){
                holder.textViewCliente.setText(mDataset.get(position).getClient());
            }
        }


        if(mDataset.get(position).getNameOrigin() != null) {
            if (mDataset.get(position).getNameOrigin().length() > 0) {
                holder.textViewOrigen.setText(mDataset.get(position).getNameOrigin());
            }
        }

        holder.textViewDestino.setText(mDataset.get(position).getNameDestination());

        holder.textViewFecha.setText(mDataset.get(position).getMdate());


        if(mDataset.get(position).getIsAceptReservationByDriver() == 1)
        {
            holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorVerde));
            if(isClient)
            {
                holder.textViewStatus.setText(contextView.getString(R.string.reserva_chofer_asignado));
            }
            else{
                holder.textViewStatus.setText(contextView.getString(R.string.reserva_confirmada));
            }
        } else {
            if (isClient){
                if(mDataset.get(position).getIdSatatusTravel()==Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA)
                {
                    holder.textViewStatus.setText(contextView.getString(R.string.reserva_sin_chofer_asignado));
                    holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorRojo));
                }
                else if (mDataset.get(position).getIdSatatusTravel()== Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER){
                    holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorAzulOscuro));
                    holder.textViewStatus.setText(contextView.getString(R.string.busca_cliente));
                }
                else if(mDataset.get(position).getIdSatatusTravel()== Globales.StatusTravel.CHOFER_ASIGNADO){
                    holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorRojo));
                    holder.textViewStatus.setText(contextView.getString(R.string.reserva_no_confirmada_por_chofer));
                }
                else{
                    holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorRojo));
                    holder.textViewStatus.setText(contextView.getString(R.string.reserva_no_confirmada));
                }

            }else {
                holder.textViewStatus.setTextColor(contextView.getResources().getColor(R.color.colorRojo));
                holder.textViewStatus.setText(contextView.getString(R.string.reserva_no_confirmada));
            }

        }

        holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerReserva.reservaSelecion(mDataset.get(position));
            }
        });

    }





    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}