package com.example.apptransport.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usario on 26/4/2017.
 */

public class TravelDialog extends DialogFragment {

    public static InfoTravelEntity currentTravel;
    public GlovalVar gloval;
    private LinearLayout contenidoMonto, contenidoPasajeros, contenidoMultiOrigen, contenidoMultiDestino,
            contenidoTelefono;
    private TextView textViewIdViaje, textViewNameInfo, textViewTelefono, textViewOrigen, textViewDestino,
            textViewMonto, textViewFechaTiempo,textViewPasajeros,textViewMultiOrigen,textViewMultiDestino;
    private TextView textTravelByHour;
    private Button buttonAceptar,buttonRechazar,buttonPasajeros,buttonOrigen,buttonDestino;
    private View viewTravelByHour;
    private View rootView;
    private ImageView imageView;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        rootView = inflater.inflate(R.layout.dialog_travel, null);
        builder.setView(rootView);

        addView();
        managerDialog();

        return builder.create();
    }

    private void addView(){
        // variable global
        gloval = ((GlovalVar) getActivity().getApplicationContext());
        currentTravel = gloval.getGv_travel_current();

        imageView =  rootView.findViewById(R.id.img_face_client);
        contenidoTelefono= rootView.findViewById(R.id.conten_telefono);
        contenidoMonto= rootView.findViewById(R.id.conten_monto);
        contenidoPasajeros= rootView.findViewById(R.id.conten_pasajeros);
        contenidoMultiOrigen= rootView.findViewById(R.id.conten_multi_origen);
        contenidoMultiDestino= rootView.findViewById(R.id.conten_multi_destino);

        textViewIdViaje = rootView.findViewById(R.id.text_id_viaje);
        textViewNameInfo = rootView.findViewById(R.id.text_name_info);
        textViewTelefono = rootView.findViewById(R.id.txt_calling_info);
        textViewOrigen = rootView.findViewById(R.id.txt_origin);
        textViewDestino = rootView.findViewById(R.id.txt_detination);
        textViewMonto = rootView.findViewById(R.id.txt_monto);
        textViewFechaTiempo = rootView.findViewById(R.id.txt_date_info);
        textViewPasajeros = rootView.findViewById(R.id.text_pasajeros);
        textViewPasajeros.setVisibility(View.GONE);
        textViewMultiOrigen = rootView.findViewById(R.id.text_multiorigen);
        textViewMultiOrigen.setVisibility(View.GONE);
        textViewMultiDestino = rootView.findViewById(R.id.text_multidestino);
        textViewMultiDestino.setVisibility(View.GONE);

        textTravelByHour = rootView.findViewById(R.id.txt_viaje_x_hora_value);
        viewTravelByHour = rootView.findViewById(R.id.content_travel_by_hour);

        buttonRechazar = rootView.findViewById(R.id.btn_refut);
        buttonRechazar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((HomeChofer) getActivity()).cancelTravel(currentTravel.getIdTravel());
            }
        });

        if (HomeChofer.PARAM_66_VER_PRECIO_VIAJE_EN_APP == 1) {
            buttonRechazar.setEnabled(true);
        } else {
            buttonRechazar.setEnabled(false);
        }

        buttonAceptar = rootView.findViewById(R.id.btn_acept);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((HomeChofer) getActivity()).verifickTravelCancel(currentTravel.getIdTravel());
            }
        });

        buttonPasajeros= rootView.findViewById(R.id.button_pasajeros);
        buttonPasajeros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewPasajeros.getVisibility()== View.VISIBLE){
                    textViewPasajeros.setVisibility(View.GONE);
                }else {
                    textViewPasajeros.setVisibility(View.VISIBLE);
                    textViewMultiOrigen.setVisibility(View.GONE);
                    textViewMultiDestino.setVisibility(View.GONE);
                }
            }
        });


        buttonOrigen= rootView.findViewById(R.id.button_multi_origen);
        buttonOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewMultiOrigen.getVisibility()== View.VISIBLE){
                    textViewMultiOrigen.setVisibility(View.GONE);
                }else {
                    textViewPasajeros.setVisibility(View.GONE);
                    textViewMultiOrigen.setVisibility(View.VISIBLE);
                    textViewMultiDestino.setVisibility(View.GONE);
                }
            }
        });

        buttonDestino= rootView.findViewById(R.id.button_multi_destino);
        buttonDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewMultiDestino.getVisibility()== View.VISIBLE){
                    textViewMultiDestino.setVisibility(View.GONE);
                }else {
                    textViewPasajeros.setVisibility(View.GONE);
                    textViewMultiOrigen.setVisibility(View.GONE);
                    textViewMultiDestino.setVisibility(View.VISIBLE);
                }
            }
        });

        textViewTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialer(getActivity(), textViewTelefono.getText().toString());
            }
        });

        ImageView imgPhone =  rootView.findViewById(R.id.imgDialer);
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialer(getActivity(), textViewTelefono.getText().toString());
            }
        });
        setPhoto();
    }

    private void setPhoto()
    {
        try
        {
            String url = Utils.getUrlImageUser(currentTravel.getIdUserClient());
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_update)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.ic_user)
                    //.centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void managerDialog(){

        int param49 = Integer.parseInt(gloval.getGv_param().get(48).getValue());// SE PUEDE VER TELEFONO DE PASAJEROS

        textViewIdViaje.setText(currentTravel.getCodTravel());
        textViewNameInfo.setText(currentTravel.getClient());
        textViewOrigen.setText(currentTravel.getNameOrigin());
        textViewDestino.setText(currentTravel.getNameDestination());
        textViewFechaTiempo.setText(currentTravel.getMdate());

        if (currentTravel.getPhoneNumber() != null && param49 == 1) {
            textViewTelefono.setText(currentTravel.getPhoneNumber());
        } else {
            contenidoTelefono.setVisibility(View.GONE);
        }

        int param25 = Integer.parseInt(gloval.getGv_param().get(25).getValue());
        if (param25 == 1) {
            textViewMonto.setText(currentTravel.getAmountCalculate());
        } else {
            contenidoMonto.setVisibility(View.GONE);
        }

        if (currentTravel.getIsExitSleepIntravel() == 1) {
            // txt_issleep_dialog.setText(getString(R.string.yes));
        } else {
            // txt_issleep_dialog.setText(getString(R.string.no));
        }

        if (currentTravel.getIsTravelFromReturn() == 1) {
            //  txt_isretunr_dialog.setText(getString(R.string.yes));
        } else {
            //  txt_isretunr_dialog.setText(getString(R.string.no));
        }

        ///PASAJEROS
        if (currentTravel.getPassenger1().equals("") &&
                currentTravel.getPassenger2().equals("") &&
                currentTravel.getPassenger3().equals("") &&
                currentTravel.getPassenger4().equals("")) {

            contenidoPasajeros.setVisibility(View.GONE);

        } else {
            List<String> subItem = new ArrayList<>();
            String textoPasajero="";
            if (!currentTravel.getPassenger1().equals("")) {
                subItem.add(currentTravel.getPassenger1());
                textoPasajero= currentTravel.getPassenger1();
            }
            if (!currentTravel.getPassenger2().equals("")) {
                subItem.add(currentTravel.getPassenger2());
                textoPasajero= textoPasajero+"\n\n"+currentTravel.getPassenger2();
            }
            if (!currentTravel.getPassenger3().equals("")) {
                subItem.add(currentTravel.getPassenger3());
                textoPasajero= textoPasajero+"\n\n"+currentTravel.getPassenger3();
            }
            if (!currentTravel.getPassenger4().equals("")) {
                subItem.add(currentTravel.getPassenger4());
                textoPasajero= textoPasajero+"\n\n"+currentTravel.getPassenger4();
            }

            textViewPasajeros.setText(textoPasajero);

        }

        //MULTI DESTINO
        try {
            if (currentTravel.getMultiDestination().equals("")){
                contenidoMultiDestino.setVisibility(View.GONE);
            }else {
                List<String> subItem2 = new ArrayList<>();
                if(!currentTravel.getMultiDestination().equals("")){subItem2.add(currentTravel.getMultiDestination());}

                textViewMultiDestino.setText(currentTravel.getMultiDestination());
            }
        }catch (Exception e){
            Log.e("ERROR MULTI DESTINO",e.toString());
            contenidoMultiDestino.setVisibility(View.GONE);
        }


        //MULTI ORIGEN
        if (currentTravel.getOriginMultipleDesc1().equals("")&&currentTravel.getOriginMultipleDesc2().equals("")&&
                currentTravel.getOriginMultipleDesc3().equals("")&& currentTravel.getOriginMultipleDesc4().equals("") ){

            contenidoMultiOrigen.setVisibility(View.GONE);

        }else {

            List<String> subItem3 = new ArrayList<>();
            String textoOrgient="";
            if (!currentTravel.getOriginMultipleDesc1().equals("")) {
                subItem3.add(currentTravel.getOriginMultipleDesc1());
                textoOrgient= currentTravel.getOriginMultipleDesc1();
            }
            if (!currentTravel.getOriginMultipleDesc2().equals("")) {
                subItem3.add(currentTravel.getOriginMultipleDesc2());
                textoOrgient= textoOrgient+"\n\n"+currentTravel.getOriginMultipleDesc2();
            }
            if (!currentTravel.getOriginMultipleDesc3().equals("")) {
                subItem3.add(currentTravel.getOriginMultipleDesc3());
                textoOrgient= textoOrgient+"\n\n"+currentTravel.getOriginMultipleDesc3();
            }
            if (!currentTravel.getOriginMultipleDesc4().equals("")) {
                subItem3.add(currentTravel.getOriginMultipleDesc4());
                textoOrgient= textoOrgient+"\n\n"+currentTravel.getOriginMultipleDesc4();
            }

            textViewMultiOrigen.setText(textoOrgient);

        }

        showHideTravelByHour();

    }

    private void showHideTravelByHour()
    {
        if(currentTravel.getIsTravelByHour()==1)
        {
            viewTravelByHour.setVisibility(View.VISIBLE);
            textTravelByHour.setText(currentTravel.getCantHours());
        }
        else{
            viewTravelByHour.setVisibility(View.GONE);
        }
    }

}
