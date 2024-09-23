package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.ikovac.timepickerwithseconds.TimePicker;


import java.text.DecimalFormat;
import java.util.List;


@SuppressLint("ValidFragment")
public class FinalTravelDialog  extends DialogFragment {
    private View v;
    private EditText editTextPejae,editTextEstacionamiento;
    private TextView textViewMonto,textViewTiempoEspera,textViewFlete,textViewKmIda,textViewKmVuelta,
    textViewIfo, txtTimeElapsed, txtIsMinimum, txtContainOriginPac;
    private Button buttonTarjeta,buttonFirma,buttonEfectivo,buttonFinalizar, buttonTarjetaCliente;
    private LinearLayout linearLayoutPagos;
    private ImageButton btnAddWaitingTime;
    private View layout_fleet;
    private double montoTotal,amounCalculateGps, extraTimePrice,kilometros_ida,kilometros_vuelta,priceFlet,
            bandera;
    private int extraTime = 0;
    public DecimalFormat df = new DecimalFormat("####0.00");

    private InfoTravelEntity currentTravel;
    private ListenerDialogFinalTravel listenerDialogFinalTravel;
    private GlovalVar gloval;
    private int PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR =0;
    private ConstraintLayout constraintLayoutPeaje;
    private String timeElpasedInTravelByHour;
    private boolean isMinimum;
    private double peajeMonto=0,estacionamientoMonto=0;
    private double amountOriginPac;
    boolean isEditable;

    public FinalTravelDialog(InfoTravelEntity currentTravel, double kilometros_ida, double kilometros_vuelta,
                             double extraTime,double priceFlet,double bandera, ListenerDialogFinalTravel listenerDialogFinalTravel,
                             GlovalVar gloval,double amounCalculateGps,int tiempoTxt, String timeElpasedInTravelByHour, boolean isPriceMinimum, double amountOriginPac) {
        this.currentTravel = currentTravel;
        this.kilometros_ida = kilometros_ida;
        this.kilometros_vuelta = kilometros_vuelta;
        this.extraTimePrice = extraTime;
        this.priceFlet = priceFlet;
        this.listenerDialogFinalTravel = listenerDialogFinalTravel;
        this.gloval= gloval;
        this.amounCalculateGps= amounCalculateGps;
        this.extraTime = tiempoTxt;
        this.bandera=bandera;
        this.timeElpasedInTravelByHour=timeElpasedInTravelByHour;
        this.isMinimum=isPriceMinimum;
        this.amountOriginPac=amountOriginPac;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_final_travel, null);
        builder.setView(v);

        isEditable = true;
        if (currentTravel != null) {
            isEditable = currentTravel.getIsPreFinish() != Globales.PreFinishValues.WEB_FINALIZED;
        }

        addView();
        validarPermisoPagoTarjeta();
        setInfo();
        return builder.create();
    }

    private void addView() {
        layout_fleet = v.findViewById(R.id.layout_fleet);
        textViewMonto= v.findViewById(R.id.txt_mount);
        textViewTiempoEspera= v.findViewById(R.id.txtTimeslep);
        textViewFlete= v.findViewById(R.id.amount_flet_txt);
        textViewKmIda= v.findViewById(R.id.distance_txt);
        textViewKmVuelta= v.findViewById(R.id.distance_return_txt);
        textViewIfo= v.findViewById(R.id.text_info);
        textViewIfo.setVisibility(View.GONE);
        buttonTarjeta= v.findViewById(R.id.bnt_pay_car);
        buttonTarjetaCliente = v.findViewById(R.id.bnt_pay_car_by_client);
        txtTimeElapsed = v.findViewById(R.id.txt_time_elapsed);
        linearLayoutPagos= v.findViewById(R.id.conten_opcionespago);
        txtIsMinimum = v.findViewById(R.id.txt_is_minimum);
        constraintLayoutPeaje= v.findViewById(R.id.conten_peaje);
        txtContainOriginPac = v.findViewById(R.id.txt_contain_origin_pac);
        buttonTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerDialogFinalTravel.finalizarViajeTarjeta(montoTotal,peajeMonto,estacionamientoMonto,extraTime,extraTimePrice, false);
            }
        });

        buttonTarjetaCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerDialogFinalTravel.finalizarViajeTarjeta(montoTotal,peajeMonto,estacionamientoMonto,extraTime,extraTimePrice, true);
            }
        });


        buttonFirma= v.findViewById(R.id.btn_firm_voucher);
        buttonFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutPagos.setVisibility(View.GONE);
                textViewIfo.setVisibility(View.VISIBLE);
                textViewIfo.setText(R.string.procesando_operacion);
                listenerDialogFinalTravel.finalizarViajeFirma(montoTotal,peajeMonto,estacionamientoMonto,extraTime,extraTimePrice);
            }
        });


        buttonEfectivo= v.findViewById(R.id.btn_pay_cash);
        buttonEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutPagos.setVisibility(View.GONE);
                textViewIfo.setVisibility(View.VISIBLE);
                textViewIfo.setText(R.string.procesando_operacion);
                listenerDialogFinalTravel.finalizarViajeEfectivo(montoTotal,peajeMonto,estacionamientoMonto,extraTime,extraTimePrice);
            }
        });


        editTextEstacionamiento= v.findViewById(R.id.parkin_txt);
        editTextEstacionamiento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    calcularMontoTotal(true);
                } catch (Exception e) {
                    Log.e("ERRROR ESTACIOMANIENTO", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonFinalizar= v.findViewById(R.id.btn_end_one);
        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutPagos.setVisibility(View.GONE);
                textViewIfo.setVisibility(View.VISIBLE);
                textViewIfo.setText(R.string.procesando_operacion);
                listenerDialogFinalTravel.finalizarViajeWeb(montoTotal,peajeMonto,estacionamientoMonto,extraTime,extraTimePrice);
            }
        });

        editTextPejae= v.findViewById(R.id.peajes_txt);
        editTextPejae.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    calcularMontoTotal(true);
                } catch (Exception e) {
                    Log.e("ERRROR PEAJE", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (gloval.getGv_param().get(90).getValue()!=null){
            if(gloval.getGv_param().get(90).getValue().equals("0")){
                constraintLayoutPeaje.setVisibility(View.GONE);
            }
        }

        addWaitingTimeControl();
    }

    private void addWaitingTimeControl() {
        //Si es editable, mostrar este boton, de lo contrario, ocultarlo
        btnAddWaitingTime = v.findViewById(R.id.btnAddWaitingTime);

        btnAddWaitingTime.setVisibility(isEditable ? View.VISIBLE : View.GONE);

        btnAddWaitingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = Utils.covertSecondsToHHMMSS(extraTime);
                String hour, minutes, seconds;
                hour = time.split(":")[0];
                minutes = time.split(":")[1];
                seconds = time.split(":")[2];


                MyTimePickerDialog mTimePicker = new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                        try {
                            int timeSelected = seconds + minute * 60 + hourOfDay * 60 * 60;
                            double PARAM_5 = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA).getValue());// PRECIO LISTA TIEMPO DE ESPERA
                            int PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES).getValue());

                            extraTimePrice = Utils.getExtraTime(PARAM_5, PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES, timeSelected, currentTravel);
                            extraTime = timeSelected;

                            int param25 = Integer.parseInt(gloval.getGv_param().get(25).getValue());
                            showExtraTime(param25);
                            calcularMontoTotal(isEditable);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, Integer.parseInt(hour), Integer.parseInt(minutes), Integer.parseInt(seconds), true);
                mTimePicker.show();
            }
        });
    }

    private void validarPermisoPagoTarjeta(){
        if(Utils.isCardAcepted(gloval.getGv_param())){
            buttonTarjeta.setVisibility(View.VISIBLE);
        }else {
            buttonTarjeta.setVisibility(View.GONE);
        }
    }

    @SuppressLint("LongLogTag")
    private void setInfo() {
        int param25 = Integer.parseInt(gloval.getGv_param().get(25).getValue());
        Currency currency = Utils.getCurrency(getContext());
        if(currentTravel != null) {
            if(currentTravel.getIsTravelComany() == 1)// EMPRESA
            {
                if (currentTravel.isPaymentCash != 1) {
                    buttonEfectivo.setVisibility(View.GONE);
                } else {
                    buttonEfectivo.setVisibility(View.VISIBLE);
                }
                buttonFirma.setVisibility(View.VISIBLE);

            }else {
                buttonEfectivo.setVisibility(View.VISIBLE);
                buttonFirma.setVisibility(View.GONE);

            }
        }else {
            buttonEfectivo.setVisibility(View.VISIBLE);
        }

        if(param25 == 1) {
            textViewKmIda.setText(df.format(kilometros_ida).concat(getString(R.string.km)));
            textViewKmVuelta.setText(df.format(kilometros_vuelta).concat(getString(R.string.km)));
        }else {
            textViewKmIda.setText(df.format(kilometros_ida).concat(getString(R.string.km)));
            textViewKmVuelta.setText(df.format(kilometros_vuelta).concat(getString(R.string.km)));
        }

        showExtraTime(param25);

        if(currentTravel.getIsFleetTravelAssistance() > 0){
            textViewFlete.setText(currency.getSymbol().concat(" ").concat(df.format(priceFlet)));
        }else {
            textViewFlete.setText(currency.getSymbol().concat(" 0.0"));
        }

        PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR = Integer.valueOf(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR).getValue());
        if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR ==1 && currentTravel.getIsPreFinish()!=2){

            buttonFinalizar.setText(R.string.prefinalizar_viaje);

            buttonFinalizar.setVisibility(View.VISIBLE);
            buttonTarjeta.setVisibility(View.GONE);
            buttonEfectivo.setVisibility(View.GONE);
            buttonFirma.setVisibility(View.GONE);

        }else {
            buttonFinalizar.setVisibility(View.GONE);
        }

        if(!isEditable)
        {
            editTextEstacionamiento.setEnabled(false);
            editTextPejae.setEnabled(false);
            editTextPejae.setText(currentTravel.getAmountToll());
            editTextEstacionamiento.setText(currentTravel.getAmountParking());
        }
        else{
            editTextEstacionamiento.setEnabled(true);
            editTextPejae.setEnabled(true);
        }

        calcularMontoTotal(isEditable);
        setTimeElapsed();
        setIsTravelMinimum();
        setIsOriginPac();
        mostrarOcultarFlete();
        mostrarOcultarPagoTarjetaCliente();
    }

    private void showExtraTime(int param25)
    {
        Currency currency = Utils.getCurrency(getContext());
        if(param25 == 1) {
            textViewTiempoEspera.setText(Utils.covertSecondsToHHMMSS(extraTime).concat(" - ").concat(currency.getSymbol()).concat(Double.toString(round(extraTimePrice, 2))));
        }else {
            textViewTiempoEspera.setText(Utils.covertSecondsToHHMMSS(extraTime));
        }
    }

    private void mostrarOcultarFlete()
    {
        try{
            int muestraFlete =  Utils.getValueIntFromParameters(gloval.getGv_param(),Globales.ParametrosDeApp.PARAM_105_MUESTRA_FLETE_EN_APP_PASAJERO);
            layout_fleet.setVisibility(muestraFlete==0?View.GONE: View.VISIBLE);
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
    }

    private void mostrarOcultarPagoTarjetaCliente()
    {
        try{
            int muestraTarjetaCliente=  Utils.getValueIntFromParameters(gloval.getGv_param(),Globales.ParametrosDeApp.PARAM_101_ACCEPT_PAYMENT_CARD_IN_CLIENT);
            buttonTarjetaCliente.setVisibility(muestraTarjetaCliente==0?View.GONE: View.VISIBLE);
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
    }

    private void setTimeElapsed()
    {
        try{
            if(currentTravel.getIsTravelByHour()==1)
            {
                txtTimeElapsed.setVisibility(View.VISIBLE);
                txtTimeElapsed.setText(getString(R.string.tiempo_total).replace("[TIME]",timeElpasedInTravelByHour));
            }
            else{
                txtTimeElapsed.setVisibility(View.GONE);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void calcularMontoTotal(boolean isEditable){
        Currency currency = Utils.getCurrency(getContext());
        int param26_mostrar_precio = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP).getValue());

        String myString = editTextPejae.getText().toString();
        if (myString != null && !myString.equals("")) {
            peajeMonto = Double.valueOf(myString);
        } else {
            peajeMonto = 0;
        }

        String parkin_txt = editTextEstacionamiento.getText().toString();
        if (parkin_txt != null && !parkin_txt.equals("")) {
            estacionamientoMonto = Double.valueOf(parkin_txt);
        } else {
            estacionamientoMonto = 0;
        }

        //if(isEditable)
        //{
            montoTotal =  amounCalculateGps + extraTimePrice + peajeMonto + estacionamientoMonto + bandera + priceFlet;
        //}
        //else{
        //    montoTotal =  amounCalculateGps;
       // }



        showPrice(currency, param26_mostrar_precio);
    }

    private void setIsTravelMinimum()
    {
        try{
            txtIsMinimum.setVisibility(isMinimum ? View.VISIBLE : View.GONE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setIsOriginPac()
    {
        try{
            txtContainOriginPac.setVisibility(amountOriginPac!=0d ? View.VISIBLE : View.GONE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    private void showPrice(Currency currency, int param26_mostrar_precio) {

        switch (param26_mostrar_precio)
        {
            case 0:
                textViewMonto.setText(getString(R.string.monto_reservado));
                break;
            case 1:
                if (currentTravel.getIsTravelComany() == 1){
                    textViewMonto.setText(currency.getSymbol().concat(" ").concat(Double.toString(round(montoTotal,2))));
                }else {
                    textViewMonto.setText(getString(R.string.monto_reservado));
                }
                break;
            case 2:
                if (currentTravel.getIsTravelComany() == 0){
                    textViewMonto.setText(currency.getSymbol().concat(" ").concat(Double.toString(round(montoTotal,2))));
                }else {
                    textViewMonto.setText(getString(R.string.monto_reservado));
                }
                break;
            case 3:
                textViewMonto.setText(currency.getSymbol().concat(" ").concat(Double.toString(round(montoTotal,2))));
                break;
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    //region  OBTENER PRECIO
    public double getPriceBylistBeneficion(List<BeneficioEntity> list, double distance){
        double value = 0;
        double PARAM_1= Double.parseDouble(gloval.getGv_param().get(0).getValue());
        int indexBrack = 0;
        for (int i=0;i < list.size();i++){
            indexBrack++;
            // EVALUAMOS LOS DISTINTOS BENEFICIOS //
            Log.d("b-distance","value=  "+list.get(i).BenefitsToKm);
            if (distance >= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitsPreceKm());
            }else if (distance >= Double.parseDouble(list.get(i).BenefitsFromKm) && distance <= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitsPreceKm());
            }
        }

        Log.d("VALOR BENE","valor: "+value);

        // VALIDAMOS SI EL TOTAL KM ES MAYOR A //
        if(distance > Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm())){
            double distanceExtraBeneficio = distance - Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm());
            double amountExtra = 0;
            // ES VIAJE A EMPRESA //
            if(currentTravel.getIsTravelComany() == 1){// EMPRESA
                amountExtra = distanceExtraBeneficio * currentTravel.getPriceDitanceCompany();
            }else{
                amountExtra = distanceExtraBeneficio * PARAM_1;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }

    public double getPriceReturnBylistBeneficion(List<BeneficioEntity> list,double distance){
        double value = 0;
        double PARAM_6  = Double.parseDouble(gloval.getGv_param().get(5).getValue());

        int indexBrack = 0;
        for (int i=0;i < list.size();i++){
            indexBrack++;
            // EVALUAMOS LOS DISTINTOS BENEFICIOS //
            Log.d("b-distance","value=  "+list.get(i).BenefitsToKm);
            if (distance >= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitPreceReturnKm());
            }else if (distance >= Double.parseDouble(list.get(i).BenefitsFromKm) && distance <= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitPreceReturnKm());
            }
        }

        Log.d("VALOR BENE","valor: "+value);

        // VALIDAMOS SI EL TOTAL KM ES MAYOR A //
        if(distance > Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm())){
            double distanceExtraBeneficio = distance - Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm());
            double amountExtra = 0;
            // ES VIAJE A EMPRESA //
            if(currentTravel.getIsTravelComany() == 1){// EMPRESA
                amountExtra = distanceExtraBeneficio * currentTravel.getPriceDitanceCompany();
            }else{
                amountExtra = distanceExtraBeneficio * PARAM_6;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }
    //endregion



}
