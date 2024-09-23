package com.example.apptransport.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.TraveInfoSendEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelBodyForClientPayment;
import com.apreciasoft.mobile.asremis.Entity.TravelEntityForClientPayment;
import com.apreciasoft.mobile.asremis.Entity.TravelLocationEntity;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Fragments.HomeFragment;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class CallClientPayWithCard extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_FOR_PLACE_TO_PAY=2;
    private Button buttonPagar;
    private ProgressBar progressBar;
    private TextView txtMercadoPago;
    private ImageView imgPlaceToPay;
    private TextView txtRechargeFee;
    private TextView textViewMonto;

    public static InfoTravelEntity currentTravel;
    private Double totalFinal;
    private Double amounCalculateGps;
    private Double bandera;
    private String keyMercado,keyMercadoPagoPrivado;
    private ProgressDialog loading;
    private AlertDialog alertDialog;
    public DecimalFormat df = new DecimalFormat("####0.00");

    private double kilometros_total;
    private int PARAM_3;
    private int PARAM_67;
    private String lat = "";
    private String lon = "";
    private String add = "";
    public double kilometros_vuelta;
    private double peajeMonto;
    private double estacionamientoMonto;
    public static double priceFlet;
    public double extraTime;
    private int tiempoTxt;
    private String paymentCardProvider="";
    private List<paramEntity> listParams;
    private ProgressDialog progressDialogSave;

    private String name, surname, document, phone, email;
    private boolean clientHasFinish;
    private double ePayDiffAmount;

    private int isKmWithError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_client_pay_with_card);
        Bundle extras = getIntent().getExtras();
        String stringcurrent= extras.getString("current");
        Gson gson= new Gson();
        currentTravel= gson.fromJson(stringcurrent,InfoTravelEntity.class);

        totalFinal= extras.getDouble("TotalAmount");
        amounCalculateGps = extras.getDouble("amounCalculateGps");
        bandera = extras.getDouble("bandera");
        keyMercado= extras.getString("keyMercado");
        keyMercadoPagoPrivado= extras.getString("keyMercadoPrivado");

        kilometros_total= extras.getDouble("kilometros_total");
        PARAM_3= extras.getInt("param_3",0);
        PARAM_67= extras.getInt("param_67",0);
        lat= extras.getString("lat");
        lon= extras.getString("lon");
        add= extras.getString("add");
        kilometros_vuelta= extras.getDouble("kilometros_vuelta",0);
        peajeMonto= extras.getDouble("peajeMonto",0);
        estacionamientoMonto= extras.getDouble("estacionamientoMonto",0);
        priceFlet= extras.getDouble("priceFlet",0);
        extraTime= extras.getDouble("extraTime",0);
        tiempoTxt= extras.getInt("tiempoTxt",0);
        paymentCardProvider = extras.getString("PAYMENT_CARD_PROVIDER");
        clientHasFinish = extras.getBoolean("CLIENT_HAS_FINISH");


        listParams = Utils.getParamsFromLocalPreferences(getApplicationContext());

        String idPreferencia= extras.getString("idPreferencia");
        ePayDiffAmount = extras.getDouble("ePayDiffAmount",0);
        isKmWithError = extras.getInt("isKmWithError", 0);



        textViewMonto = findViewById(R.id.txt_mount2);

        ImageButton imageButtonCerrar= findViewById(R.id.imageButton_cerrar);
        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetClientToPayWithCard();
            }
        });

        progressBar= findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        buttonPagar= findViewById(R.id.button_pagar_tarjeta);
        buttonPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCallClientToPayWithCard();
            }
        });

        txtRechargeFee = findViewById(R.id.txt_recharge_fee);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverNotifications, new IntentFilter("update-message"));

        if(clientHasFinish)
        {
            manageClientHasFinish();
        }

        showOrHideExtraFeeByCard();
        showTotalAmount();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiverNotifications);
            broadcastReceiverNotifications=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showOrHideExtraFeeByCard()
    {
        String porcentajeAdicional = listParams.get(Globales.ParametrosDeApp.PARAM_96_PORCENTAJE_ADICIONAL_POR_PAGO_CON_TARJETA).getValue();
        if(!TextUtils.isEmpty(porcentajeAdicional) && !"0".equals(porcentajeAdicional)) {
            txtRechargeFee.setText(getString(R.string.metodo_de_pago_recargo).replace("[VALUE]",porcentajeAdicional));
            txtRechargeFee.setVisibility(View.VISIBLE);
        }
        else{
            txtRechargeFee.setVisibility(View.GONE);
        }
    }

    private void showTotalAmount() {
        try {
            Currency currency = Utils.getCurrency(getApplicationContext());
            textViewMonto.setText(currency.getSymbol().concat(" ").concat(String.valueOf(Utils.round(getTotalAmount(),2))));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private double getTotalAmount()
    {
        return totalFinal + ePayDiffAmount;
    }

    private void manageClientHasFinish()
    {
        hideProgressDialog();
        hideLoading();
        hideAlertClientIsPaying();
        if(currentTravel.getClientPaymentStatus()==Globales.ClientPaymentStatus.PAGA_CLIENTE_PENDIENTE)
        {
            showAlertClientIsPaying();
        }
        else if(currentTravel.getClientPaymentStatus()==Globales.ClientPaymentStatus.PAGA_CLIENTE_CANCELADO_POR_CLIENTE ||
                currentTravel.getClientPaymentStatus()==Globales.ClientPaymentStatus.PAGA_CLIENTE_ERROR){

            showLoadingWithCancel(getString(R.string.error_pago_cliente), getString(R.string.error_pago_cliente_description), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        else if(currentTravel.getClientPaymentStatus()==Globales.ClientPaymentStatus.PAGA_CLIENTE_INICIO_EL_PROCESO_DE_PAGO) {
            loading =  getProgressDialog(getString(R.string.cliente_iniciado_pago));
            loading.show();
        }

        else{
            progressDialogSave =  getProgressDialog(getString(R.string.proceso_de_pago_con_tarjeta_cliente_finalizo));
            progressDialogSave.show();
            callFinishTravelAfterClientHasPayed();
        }
    }

    private void callFinishTravelAfterClientHasPayed()
    {
        verifickTravelFinish();
    }

    private void showLoadingWithCancel(String title, String msg, DialogInterface.OnClickListener onClickListener)
    {
        try{
            hideLoading();
            AlertDialog.Builder builder = new AlertDialog.Builder(CallClientPayWithCard.this);
            builder.setTitle(title);
            builder.setMessage(msg);
            builder.setNegativeButton(getString(R.string.accept), onClickListener);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void showAlertClientIsPaying()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(CallClientPayWithCard.this);
        builder.setTitle(R.string.cliente_esta_pagando);
        builder.setMessage(R.string.no_salga_pantalla);
        builder.setNegativeButton(R.string.cancelar_pago_cliente_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideAlertClientIsPaying();
                cancelPaymentInClient();
            }
        });
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }



    private void hideAlertClientIsPaying()
    {
        try{
            if(alertDialog!=null)
            {
                alertDialog.cancel();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initCallClientToPayWithCard()
    {
        showAlertClientIsPaying();
        callClientToPayWithCard();
    }

    private ProgressDialog getProgressDialog(String msg)
    {
        ProgressDialog dialog = ProgressDialog.show( this, getString(R.string.proceso_de_pago_title),
                msg , true,false);
        return dialog;
    }

    private void hideProgressDialog()
    {
        try{
            if(progressDialogSave!=null)
            {
                progressDialogSave.dismiss();
                progressDialogSave=null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void hideLoading()
    {
        try{
            if(loading!=null)
            {
                loading.dismiss();
                loading=null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void callClientToPayWithCard()
    {
        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        TravelEntityForClientPayment entity = new TravelEntityForClientPayment();
        TravelBodyForClientPayment request=new TravelBodyForClientPayment();
        entity.setIdTravel(currentTravel.getIdTravel());
        entity.setClientPaymentStatus(Globales.ClientPaymentStatus.PAGA_CLIENTE_PENDIENTE);
        entity.setClientPaymentAmount(getTotalAmount());
        request.setTravel(entity);

        daoTravel.payWithCardByClient(request).enqueue(new Callback<InfoTravelEntity>() {
            @Override
            public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                try {
                    if (response!=null && response.body()!=null){

                    }else {
                        showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.WARNING);
                        hideAlertClientIsPaying();
                    }
                }catch (Exception e){
                    Log.e("EXCEPTION","error:"+e.toString());
                    showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.WARNING);
                    hideAlertClientIsPaying();
                }
            }

            @Override
            public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                Log.e("FALLO CALL_CLIENT",t.toString());
                showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.FAIL);
                hideAlertClientIsPaying();
            }
        });
    }

    private void resetClientToPayWithCard()
    {
        if(currentTravel.getClientPaymentStatus()!=Globales.ClientPaymentStatus.NO_PAGA_CON_TARJETA)
        {
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            TravelEntityForClientPayment entity = new TravelEntityForClientPayment();
            TravelBodyForClientPayment request=new TravelBodyForClientPayment();
            entity.setIdTravel(currentTravel.getIdTravel());
            entity.setClientPaymentStatus(Globales.ClientPaymentStatus.NO_PAGA_CON_TARJETA);
            entity.setClientPaymentAmount(getTotalAmount());
            request.setTravel(entity);

            daoTravel.payWithCardByClient(request).enqueue(new Callback<InfoTravelEntity>() {
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                    try {
                        if (response!=null && response.body()!=null){
                            startActivity(new Intent(CallClientPayWithCard.this, HomeChofer.class));
                            finish();
                        }else {
                            showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.WARNING);
                            hideProgressDialog();
                        }
                    }catch (Exception e){
                        Log.e("EXCEPTION","error:"+e.toString());
                        showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.WARNING);
                        hideProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                    Log.e("FALLO CALL_CLIENT",t.toString());
                    showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.FAIL);
                    hideProgressDialog();
                }
            });
        }
        else{
            startActivity(new Intent(CallClientPayWithCard.this, HomeChofer.class));
            finish();
        }

    }

    public void showMessage(String mensaje,int status){
        try
        {
            SnackCustomService.show(this,mensaje,status,2500);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public  void  getCurrentTravelByIdDriver()
    {
        if(!Utils.verificaConexion(this)) {
            showAlertNoConexion();
        }else {
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            int idDriver = currentTravel.getIdDriverKf();

            daoTravel.getCurrentTravelByIdDriver(idDriver).enqueue(new Callback<InfoTravelEntity>() {
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                    try {
                        if(response!=null && response.body()!=null)
                        {
                            currentTravel = response.body();
                            manageClientHasFinish();
                        }
                    }catch (Exception e){
                        Log.e("ERROR_GetCurrentTravel",e.toString());
                        showMessage(getString(R.string.fallo_general),Globales.StatusToast.FAIL);
                    }
                }
                @Override
                public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                    Log.e("ERROR_GetCurrentTravel",t.toString());
                    showMessage(getString(R.string.fallo_general),Globales.StatusToast.FAIL);
                }
            });
        }
    }

    public  void  verifickTravelFinish()
    {
        hideProgressDialog();
        hideAlertClientIsPaying();
        loading = ProgressDialog.show(this, getString(R.string.finalizando_viaje), getString(R.string.espere_unos_segundos), true, false);
        loading.show();

        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        daoTravel.verifickTravelFinish(currentTravel.idTravel).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                try {
                    boolean result = response.body();
                    if (result) {
                        hideLoading();
                        showMessage(getString(R.string.viaje_finalizado_previamente),Globales.StatusToast.SUCCESS);
                        goToHome();
                    } else {
                        finishTravel();
                    }

                }catch (Exception e){
                    hideLoading();
                    hideProgressDialog();
                    Log.e("VerificarViajeEx",e.toString());
                    progressBar.setVisibility(View.GONE);
                    buttonPagar.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.error_mercado_pago), Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("VerificarViajeEx",t.toString());
                hideLoading();
                hideProgressDialog();
                progressBar.setVisibility(View.GONE);
                buttonPagar.setVisibility(View.VISIBLE);
                showMessage(getString(R.string.fallo_general),Globales.StatusToast.FAIL);
            }
        });

    }

    public  void  finishTravel() {

        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        String lat = "";
        String lon = "";
        String add = "";

        double _RECORIDO_TOTAL = 0;
        if (kilometros_total > 0) {
            _RECORIDO_TOTAL = Utils.round(kilometros_total, 2);
        }

        if (HomeFragment.getmLastLocation() != null) {
            lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
            lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
            add = HomeFragment.nameLocation;
        }

        String durationInTravelByHour = currentTravel.getIsTravelByHour()==1 ? Utils.getTimeInTravelByTravelByHour(getApplicationContext()) : "";
        Double finalIVA = 0d;//TODO: debo agregar el cálculo del IVA
        String ePayDiffPercent = listParams.get(Globales.ParametrosDeApp.PARAM_96_PORCENTAJE_ADICIONAL_POR_PAGO_CON_TARJETA).getValue();
        TraveInfoSendEntity travel =
                new TraveInfoSendEntity(new TravelLocationEntity
                        (
                                currentTravel.getIdTravel(),
                                amounCalculateGps + bandera,
                                _RECORIDO_TOTAL,
                                df.format(kilometros_total),
                                add,
                                lon,
                                lat,
                                peajeMonto,
                                estacionamientoMonto,
                                extraTime,
                                Utils.covertSecondsToHHMMSS(tiempoTxt),
                                3,
                                null,
                                "",
                                "",
                                "",
                                priceFlet,
                                Utils.round(kilometros_vuelta,2),
                                durationInTravelByHour,
                                finalIVA,
                                ePayDiffAmount,
                                ePayDiffPercent,
                                isKmWithError
                        )
                );

        final int isTravelComany = currentTravel.isTravelComany;
        daoTravel.finishPostTarjeta(travel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                hideLoading();
                try {
                    Utils.resetDurationForTravelByHour(getApplicationContext());
                    Log.d("CALL_CLIENT_PAYMENT",response.body().string());
                    int evaluationOperator;
                    if (isTravelComany == 1) {
                        evaluationOperator = PARAM_3;
                    } else {
                        evaluationOperator = PARAM_67;
                    }

                    if (evaluationOperator == 0) {
                        showMessage(getString(R.string.viaje_finalizado),Globales.StatusToast.SUCCESS);
                        goToHome();
                    } else {
                        showMessage(getString(R.string.viaje_enviando_aprobacion),Globales.StatusToast.SUCCESS);
                        goToHome();
                    }
                }catch (Exception e){
                    hideLoading();
                    showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("FinalizeTravelOnFailure",t.toString());
                hideLoading();
                showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });


    }

    private void goToHome(){
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(CallClientPayWithCard.this,HomeChofer.class);
                startActivity(intent);
                finish();
            }
        },2700);
    }

    /* ALERTA SIN CONEXION*/
    public void showAlertNoConexion(){
        try {
            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.problema_conexion_internet,
                    30000);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(null, Typeface.BOLD);

            snackbar.setAction(R.string.verificar, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                }
            });

            snackbar.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void cancelPaymentInClient()
    {
        try{
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            TravelEntityForClientPayment entity = new TravelEntityForClientPayment();
            TravelBodyForClientPayment request=new TravelBodyForClientPayment();
            entity.setIdTravel(currentTravel.getIdTravel());
            entity.setClientPaymentStatus(Globales.ClientPaymentStatus.PAGA_CLIENTE_TIEMPO_EXPIRADO);
            entity.setClientPaymentAmount(getTotalAmount());
            request.setTravel(entity);

            daoTravel.payWithCardByClient(request).enqueue(new Callback<InfoTravelEntity>() {
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                    try {
                        if (response!=null && response.body()!=null){
                            showMessage(getString(R.string.call_finish_client_payment_cancelled),Globales.StatusToast.SUCCESS);
                        }else {
                            showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.WARNING);
                            hideAlertClientIsPaying();
                        }
                    }catch (Exception e){
                        Log.e("EXCEPTION","error:"+e.toString());
                        showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.WARNING);
                        hideAlertClientIsPaying();
                    }
                }

                @Override
                public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                    Log.e("FALLO CALL_CLIENT",t.toString());
                    showMessage(getString(R.string.error_call_client_payment),Globales.StatusToast.FAIL);
                    hideAlertClientIsPaying();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /*
    BroadCastReceiver for Push Notifications
     */
    private BroadcastReceiver broadcastReceiverNotifications = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Utils.isForegroundActivity(getApplicationContext(), "CallClientPayWithCard")) {
                getCurrentTravelByIdDriver();
            }
        }
    };

}
