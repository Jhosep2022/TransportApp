package com.example.apptransport.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle;
import com.apreciasoft.mobile.asremis.Entity.CarSelectionItem;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;

import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleSelectionAdapter  extends RecyclerView.Adapter<VehicleSelectionAdapter.MyViewHolder>{

    private List<CarSelectionItem> mDataset;
    private Context mContext;

    public VehicleSelectionAdapter(List<CarSelectionItem> dataset){
        this.mDataset=dataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_vehicle_selection, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        mContext= parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.mPrimaryText.setText(mDataset.get(position).getName());
            holder.mCantPassengers.setText(" ".concat(mDataset.get(position).getCantPassengers()));

            setImageVehicleWithUrl(holder, position);

            if (mDataset.get(position).isSelected()) {
                holder.itemBackground.setBackgroundColor(ContextCompat.getColor(holder.itemBackground.getContext(), R.color.carSelected));
            } else {
                holder.itemBackground.setBackgroundColor(ContextCompat.getColor(holder.itemBackground.getContext(), R.color.colorBlanco));
            }
            holder.txtPrice.setText(Utils.getPriceWithMoneySymbol(holder.txtPrice.getContext(), mDataset.get(position).getPrice()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setImageVehicle(@NonNull MyViewHolder holder, int position) {
        if(mDataset.get(position).getVehicleType()!=null && mDataset.get(position).getVehicleType().getVehicleImage()!=null)
        {
            switch (mDataset.get(position).getVehicleType().getVehicleImage()) {
                case "0":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_00);
                    break;
                case "1":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_01);
                    break;
                case "2":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_02);
                    break;
                case "3":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_03);
                    break;
                case "4":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_03);
                    break;
                case "5":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_05);
                    break;
                case "6":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_06);
                    break;
                case "7":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_07);
                    break;
                case "8":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_08);
                    break;
                case "9":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_09);
                    break;
                case "10":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_10);
                    break;
                case "11":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_11);
                    break;
                case "12":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_12);
                    break;
                case "13":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_13);
                    break;
                case "14":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_14);
                    break;
                case "15":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_15);
                    break;
                case "16":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_16);
                    break;
                case "17":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_17);
                    break;
                case "18":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_18);
                    break;
                case "19":
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_19);
                    break;
                default:
                    holder.imgVehicle.setImageResource(R.drawable.ic_carros_00);
            }
        }
    }

    private void setImageVehicleWithUrl(@NonNull MyViewHolder holder, int position) {
        if(mDataset.get(position).getVehicleType()!=null && mDataset.get(position).getTypeVehicle()!=0)
        {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        String url = getImageUrl(mDataset.get(position).getVehicleType().getVehicleImage(), mDataset.get(position).getTypeVehicle(), "1".equals(mDataset.get(position).getVehicleType().isFleet));
                        ((HomeClienteNewStyle)holder.imgVehicle.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(holder.imgVehicle.getContext())
                                        .load(url)
                                        .placeholder(R.drawable.ic_carros_00)
                                        .error(R.drawable.ic_car)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        //.skipMemoryCache(true)
                                        .into(holder.imgVehicle);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private String getImageUrl(String  vehicleImage, int vehicleType, boolean isFleet)
    {
        String result="";
        try{
            String urlVehicleType=HttpConexion.getUrlBaseForVehiclesImages().concat("images/categoryVehicle/").concat(String.valueOf(vehicleType)).concat(".png");
            String urlFleetType=HttpConexion.getUrlBaseForVehiclesImages().concat("images/img_fleet_type/").concat(String.valueOf(vehicleImage)).concat(".png");
            String urlFleetCategoryType=HttpConexion.getUrlBaseForVehiclesImages().concat("images/img_fleet_category/").concat(String.valueOf(vehicleImage)).concat(".png");

            if(isURLExists(urlVehicleType))
            {
                result = urlVehicleType;
            }
            else if(isFleet)
            {
                result=urlFleetCategoryType;
            }
            else{
                result=urlFleetType;
            }
            Log.d("img", urlVehicleType);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean isURLExists(String URLName) {

        boolean result=false;
        try {

            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();

            HttpURLConnection.setFollowRedirects(false);
            con.setRequestMethod("HEAD");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result=true;
            }
        }
        catch(UnknownHostException unknownHostException){
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mPrimaryText;
        public TextView mCantPassengers;
        public View itemBackground;
        public ImageView imgVehicle;
        public TextView txtPrice;


        public MyViewHolder(View v) {
            super(v);
            mPrimaryText =  v.findViewById(R.id.txt_name_vehicle);
            itemBackground = v.findViewById(R.id.item_background);
            imgVehicle = v.findViewById(R.id.img_foto_vehiculo);
            mCantPassengers=  v.findViewById(R.id.txt_cant_pasajeros);
            txtPrice = v.findViewById(R.id.txt_price);
        }
    }
}
