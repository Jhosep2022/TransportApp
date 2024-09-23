package com.example.apptransport.Adapter;

import androidx.fragment.app.Fragment;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.apreciasoft.mobile.asremis.Entity.notification;
import com.apreciasoft.mobile.asremis.R;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by usario on 25/4/2017.
 */



public class NorificationAdapter extends RecyclerView.Adapter<NorificationAdapter.MyViewHolder>  {

    private List<notification> mDataset;
    private Context context;
    private Fragment fragment;
    private final ListenerNotification listenerNotification;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewTitulo,textViewContenido;
        ImageButton imageButtonNotificacion;


        public MyViewHolder(View v) {
            super(v);

            textViewTitulo =  v.findViewById(R.id.text_titulo);
            textViewContenido =  v.findViewById(R.id.text_contenido);
            imageButtonNotificacion= v.findViewById(R.id.imageButton_notificacion);
        }

        @Override
        public void onClick(View v) {

        }
    }


    public NorificationAdapter(List<notification> myDataset, Fragment fragment, Context context,ListenerNotification listenerNotification) {

        this.context = context;
        mDataset = myDataset;
        this.fragment = fragment;
        this.listenerNotification = listenerNotification;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_notifications, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textViewTitulo.setText(mDataset.get(position).getTitle());
        holder.textViewContenido.setText(mDataset.get(position).getSubTitle());

        holder.imageButtonNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerNotification.marcarNotifiacionVista(mDataset.get(position).getIdNotification());
            }
        });
    }



    @Override
    public int getItemCount() {

        int r = 0;
        if(mDataset != null)
        {
            r = mDataset.size();
        }
        return r;
    }
}