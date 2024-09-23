package com.example.apptransport.Adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.LiquidationEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.List;


public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {
    private List<LiquidationEntity> mDataset;
    private Context context;
    private Currency currency;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView minfo_text;
        public TextView mtxt_type;
        public TextView mtxt_cod;
        public TextView mtxt_date;
        public TextView mtxt_symbol;


        public ViewHolder(View v) {
            super(v);
            mtxt_symbol = v.findViewById(R.id.txt_symbol);
            minfo_text = v.findViewById(R.id.info_text);
            mtxt_type =  v.findViewById(R.id.txt_type);
            mtxt_cod =  v.findViewById(R.id.txt_cod);
            mtxt_date = v.findViewById(R.id.txt_date);

        }
    }

    public PayAdapter(List<LiquidationEntity> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_pay, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context= parent.getContext();
        currency = Utils.getCurrency(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.minfo_text.setText(mDataset.get(position).getTotalLiquidation());
        holder.mtxt_cod.setText(mDataset.get(position).getCodeCardx());
        holder.mtxt_date.setText(mDataset.get(position).getDateTrasaction());
        holder.mtxt_symbol.setText(currency.getSymbol());

        if (mDataset.get(position).getIdTipeOperation() == 1) {
            holder.mtxt_type.setText(context.getString(R.string.liquidacion));
            holder.mtxt_type.setTextColor(ContextCompat.getColor(context, R.color.colorVerde));
        }else  {
            holder.mtxt_type.setText(context.getString(R.string.adelanto));
            holder.mtxt_type.setTextColor(ContextCompat.getColor(context, R.color.colorRojo));
        }

        if (mDataset.get(position).getIsProcesPayment() == 1) {
            holder.mtxt_type.setText(context.getString(R.string.pago_liquidacion));
            holder.mtxt_type.setTextColor(ContextCompat.getColor(context, R.color.colorVerde));
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}



