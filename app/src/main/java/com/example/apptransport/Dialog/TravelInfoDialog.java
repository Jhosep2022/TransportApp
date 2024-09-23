package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntityLite;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.CallbackActivity;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

@SuppressLint("ValidFragment")
public class TravelInfoDialog extends DialogFragment {

    private View v;
    private ImageView imageView;
    private TextView textViewNombre,textViewTelefono,textViewOrigen,textViewDestino,textViewTiempo,
                    textViewDominio,textViewKilometro,textViewMonto;
    private InfoTravelEntityLite infoTravelEntity;
    private Button buttonCancelarViaje;
    private CallbackActivity callbackActivity;
    public static GlovalVar gloval;

    public TravelInfoDialog(InfoTravelEntityLite infoTravelEntity, CallbackActivity callbackActivity) {
        this.infoTravelEntity= infoTravelEntity;
        this.callbackActivity= callbackActivity;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_info_travel, null);
        builder.setView(v);

        addView();
        setValores();
        setPhoto();
        return builder.create();
    }

    private void addView(){

        textViewNombre= v.findViewById(R.id.txt_client_info);
        textViewTelefono= v.findViewById(R.id.txt_calling_info);
        textViewOrigen= v.findViewById(R.id.txt_origin_info);
        textViewDestino= v.findViewById(R.id.txt_destination_info);
        textViewTiempo= v.findViewById(R.id.txt_date_info);
        textViewDominio= v.findViewById(R.id.txt_domain);
        textViewKilometro= v.findViewById(R.id.txt_km_info);
        textViewMonto= v.findViewById(R.id.txt_amount_info);
        imageView =  v.findViewById(R.id.img_face_client);

        buttonCancelarViaje= v.findViewById(R.id.button_cancelar_viaje);
        buttonCancelarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackActivity.doSomething();
                dismiss();
            }
        });

        ImageButton buttonCancelar= v.findViewById(R.id.button_cancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        textViewTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(textViewTelefono.getText().toString());
            }
        });

        ImageView imgPhone =  v.findViewById(R.id.imageView8);
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(textViewTelefono.getText().toString());
            }
        });


    }

    private void setPhoto()
    {
        try
        {
            String url = Utils.getUrlImageUser(infoTravelEntity.getIdUserDriver());
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
    private void openDialer(String phoneNumber)
    {
        // Use format with "tel:" and phoneNumber
        if(!TextUtils.isEmpty(phoneNumber))
        {
            Uri u = Uri.parse("tel:" + phoneNumber);
            // Create the intent and set the data for the intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try {
                // Launch the Phone app's dialer with a phone number to dial a call.
                startActivity(i);
            } catch (SecurityException s) {
                // show() method display the toast with exception message.
                Toast.makeText(getActivity().getApplicationContext(), s.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void setValores(){
        if (infoTravelEntity!=null) {

            Currency currency = Utils.getCurrency(getContext());
            textViewNombre.setText(infoTravelEntity.getDriver());
            textViewTelefono.setText(infoTravelEntity.getPhoneNumberDriver());
            textViewDominio.setText(infoTravelEntity.getInfocar());
            textViewTiempo.setText(infoTravelEntity.getMdate());
            textViewDestino.setText(infoTravelEntity.getNameDestination());
            textViewOrigen.setText(infoTravelEntity.getNameOrigin());
            textViewKilometro.setText(infoTravelEntity.getDistanceLabel()+" Km");


            if (infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER || infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO ) {
              obtenerFotoConductor();
            }


            if (infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER || infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA) {// VIAJE EN BUSQUEDA DE CLIENTE
                buttonCancelarViaje.setVisibility(View.VISIBLE);
            }else {
                buttonCancelarViaje.setVisibility(View.INVISIBLE);

            }

            if (infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO) {
                if (infoTravelEntity.getAmountCalculate() != null) {
                   textViewMonto.setText(infoTravelEntity.getTotalAmount().concat(" ").concat(currency.getSymbol()));
                }
            } else {

                if (infoTravelEntity.getAmountCalculate() != null) {
                   textViewMonto.setText(infoTravelEntity.getAmountCalculate().concat(" ").concat(currency.getSymbol()));
                }
            }
        }
    }


    private void obtenerFotoConductor(){
        //infoTravelEntity.getIdUserDriver()
    }


}
