package com.example.apptransport.Adapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 19/1/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<InfoTravelEntity> mDataset;
    private Context context;
    public Currency currency;
    public int PARAM_26_VER_PRECIO_EN_VIAJE =0;
    public int profileUser;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView mtv_blah;
        public TextView mtv_amount;
        public TextView mtv_isProcesCurrentAcount;
        public TextView textViewCliente;
        public TextView textViewDestino;
        public TextView textViewFecha;
        public TextView textPaymentType;
        public TextView textPaymentStatus;
        public TextView textPaymentTypeTitle;
        public TextView textPaymentStatusTitle;



        public MyViewHolder(View v) {
            super(v);

            mCardView =  v.findViewById(R.id.car_travel);
            mTextView =  v.findViewById(R.id.tv_text);
            mtv_amount =  v.findViewById(R.id.tv_amount);
            mtv_blah = v.findViewById(R.id.tv_blah);
            textViewCliente= v.findViewById(R.id.text_cliente);
            mtv_isProcesCurrentAcount =  v.findViewById(R.id.tv_isProcesCurrentAcount);
            textViewDestino= v.findViewById(R.id.tv_destino);
            textViewFecha= v.findViewById(R.id.tv_fecha);

            textPaymentType =v.findViewById(R.id.tv_forma_de_pago_value);
            textPaymentStatus = v.findViewById(R.id.tv_estado_de_pago_value);

            textPaymentTypeTitle =v.findViewById(R.id.tv_forma_de_pago);
            textPaymentStatusTitle = v.findViewById(R.id.tv_estado_de_pago);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(List<InfoTravelEntity> myDataset, int param26_verPrecioEnViaje, int profileUser) {
        this.mDataset = myDataset;
        this.PARAM_26_VER_PRECIO_EN_VIAJE = param26_verPrecioEnViaje;
        this.profileUser = profileUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_history, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        context= parent.getContext();
        currency = Utils.getCurrency(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d(""+position+": ",mDataset.get(position).makeJson());

        holder.mTextView.setText(mDataset.get(position).getCodTravel());
        holder.mtv_blah.setText(mDataset.get(position).getNameOrigin());
        holder.textViewDestino.setText(mDataset.get(position).getNameDestination());
        holder.textViewFecha.setText(mDataset.get(position).getDateM().concat(" ").concat(mDataset.get(position).getHourM()));

        if (mDataset.get(position).getClientM()!=null){
            holder.textViewCliente.setText(mDataset.get(position).getClientM());
        }

        String totalAmount = getTotalAmount(mDataset.get(position));
        //TODO: (Me faltaría bandera para empresa, y flete).
        if (!"".equals(totalAmount) && (Utils.isCliente(profileUser) || Utils.isShowPrice(PARAM_26_VER_PRECIO_EN_VIAJE, mDataset.get(position).isTravelComany==1))) {
            holder.mtv_amount.setText(currency.symbol.concat(" ").concat(totalAmount));
        }
        else{
            holder.mtv_amount.setText(context.getString(R.string.informaión_reservada));
        }

        if(mDataset.get(position).getNameStatusTravel() != null) {

            if (mDataset.get(position).getIsProcesCurrentAcount() == 1) {
                holder.mtv_isProcesCurrentAcount.setText("(" + mDataset.get(position).getNameStatusTravel() + ") - ".concat(context.getString(R.string.liquidado)));
            } else {

                if(mDataset.get(position).getIdStatusTravel() == 2 &&
                        mDataset.get(position).getIsAceptReservationByDriver() == 1    ){
                    holder.mtv_isProcesCurrentAcount.setText("(".concat(context.getString(R.string.chofer_asignado)).concat(")"));

                }else{
                    holder.mtv_isProcesCurrentAcount.setText("(" + mDataset.get(position).getNameStatusTravel() + ")");

                }

            }
        }

        setPaymentInfo(holder,position);
    }

    private void setPaymentInfo(MyViewHolder holder, int position){
        if(Utils.isCliente(profileUser))
        {
            if(Globales.StatusTravel.VIAJE_FINALIZADO ==  mDataset.get(position).getIdStatusTravel()){
                String paymentType = TextUtils.isEmpty(mDataset.get(position).getIdPayment()) ? context.getString(R.string.efectivo):context.getString(R.string.tarjeta);
                holder.textPaymentType.setText(paymentType);
                if(context.getString(R.string.tarjeta).equals(paymentType))
                {
                    holder.textPaymentStatus.setText(mDataset.get(position).getStatusPayment()!=null ? Utils.getStatusPaymentFromPlaceToPayStatus(mDataset.get(position).getStatusPayment(), holder.textPaymentStatus.getContext()) :"");
                }
                else{
                    holder.textPaymentStatus.setText(context.getString(R.string.pagado));
                }
            }
            else{
                holder.textPaymentType.setText("");
                holder.textPaymentStatus.setText("");
            }
        }
        else {
            holder.textPaymentType.setVisibility(View.GONE);
            holder.textPaymentStatus.setVisibility(View.GONE);
            holder.textPaymentTypeTitle.setVisibility(View.GONE);
            holder.textPaymentStatusTitle.setVisibility(View.GONE);
        }


    }


    private String getTotalAmount(InfoTravelEntity entity)
    {
        String result="";
        double total=0d;
        try
        {
            String  amountPayment = !TextUtils.isEmpty(entity.getAmountPayment()) ? entity.getAmountPayment(): "";
            double totalAmount = !TextUtils.isEmpty(entity.getTotalAmount()) ? Double.parseDouble(entity.getTotalAmount()): 0d;
            double toll = !TextUtils.isEmpty(entity.getAmountToll()) ? Double.parseDouble(entity.getAmountToll()): 0d;
            double parking = !TextUtils.isEmpty(entity.getAmountParking()) ? Double.parseDouble(entity.getAmountParking()): 0d;
            double timeSleep = !TextUtils.isEmpty(entity.getAmountTimeSleep()) ? Double.parseDouble(entity.getAmountTimeSleep()):0d;
            total =  !TextUtils.isEmpty(amountPayment) ? Double.parseDouble(amountPayment) : totalAmount+toll+parking+timeSleep;
            result = String.format(Locale.US, "%.2f", total);
        }
        catch ( Exception ex){
            ex.printStackTrace();
            result="";
        }
        return result;
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}