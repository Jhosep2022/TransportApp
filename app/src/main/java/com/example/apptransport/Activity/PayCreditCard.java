package com.example.apptransport.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Entity.GuardarPagoTcd;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.InformacionPagoCredit;
import com.apreciasoft.mobile.asremis.Entity.ItenMercadoPago;
import com.apreciasoft.mobile.asremis.Entity.Passenger;
import com.apreciasoft.mobile.asremis.Entity.RequetClient;
import com.apreciasoft.mobile.asremis.Entity.TraveInfoSendEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelLocationEntity;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Fragments.HomeFragment;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayAmount;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayAuth;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayBuyer;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayGlobales;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayPayment;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayPaymentRequest;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayPaymentResult;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayRetrofitInterface;
import com.apreciasoft.mobile.asremis.PlaceToPay.WSAuthentication;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.google.gson.Gson;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.internal.datasource.MercadoPagoPaymentConfiguration;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Marlon Viana on 24/02/2019
 * @email 92marlonViana@gmail.com
 */

@SuppressLint("LongLogTag")
public class PayCreditCard extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_FOR_PLACE_TO_PAY=2;
    private EditText editTextEmail;
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
    private double ePayDiffAmount;
    private int isKmWithError;

    private String name, surname, document, phone, email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit_car);

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

        listParams = Utils.getParamsFromLocalPreferences(getApplicationContext());

        String idPreferencia= extras.getString("idPreferencia");

        ePayDiffAmount = extras.getDouble("ePayDiffAmount",0);
        isKmWithError = extras.getInt("isKmWithError", 0);

        textViewMonto = findViewById(R.id.txt_mount2);



        ImageButton imageButtonCerrar= findViewById(R.id.imageButton_cerrar);
        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayCreditCard.this,HomeChofer.class));
                finish();
            }
        });

        progressBar= findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        buttonPagar= findViewById(R.id.button_pagar_tarjeta);
        buttonPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarEmail();
            }
        });

        editTextEmail= findViewById(R.id.edit_email_mercado);
        txtRechargeFee = findViewById(R.id.txt_recharge_fee);

        if (idPreferencia!=null){
            startMercadoPagoCheckout(idPreferencia);
        }

        configureMotorDePagoLogo();
        getInfoClient();
        showOrHideExtraFeeByCard();
        showTotalAmount();
    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                if (payment.getPaymentStatus().equals("approved")){
                    Log.d("ID PAGO",String.valueOf(payment.getId()));
                    //Log.d("TARJETA PAGO",payment.getCard().toString());
                    Log.d("RESULTADO PAGO",payment.getPaymentStatusDetail());

                    InformacionPagoCredit informacionPagoCredit= new InformacionPagoCredit();
                    informacionPagoCredit.setAmount(String.valueOf(getTotalAmount()));
                    informacionPagoCredit.setIdPayment(String.valueOf(payment.getId()));
                    informacionPagoCredit.setIdTravel(String.valueOf(currentTravel.getIdTravel()));
                    informacionPagoCredit.setStatusPayment(PlaceToPayGlobales.StatusPlaceToPay.APPROVED);
                    guardarPago(informacionPagoCredit);

                }else {
                    showMessage(getString(R.string.pago_rechazado)+" "+payment.getPaymentStatus(),2);
                }
            } else if (resultCode == RESULT_CANCELED) {

                progressBar.setVisibility(View.GONE);
                buttonPagar.setVisibility(View.VISIBLE);

                if (data != null && data.getExtras() != null && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError = (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);

                    Log.e("ERRROR MERCADO PAGO",mercadoPagoError.getMessage());
                    showMessage(getString(R.string.fallo_general),3);
                } else {
                    Log.e("ERRROR MERCADO PAGO","");
                    showMessage(getString(R.string.fallo_general),3);
                }
            }
        }
        else if(requestCode == REQUEST_CODE_FOR_PLACE_TO_PAY)
        {
            if (resultCode == RESULT_OK) {
                String resultPayment = data.getData().toString();
                if("OK".equals(resultPayment)){
                    finalizePlaceToPayOk();
                }
                else{
                    finalizePlaceToPayCancelByClient();
                }
            }
            else{
                finalizePlaceToPayOk();
            }
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
        textViewMonto.setText("$ ".concat(String.valueOf(Utils.round(getTotalAmount(),2))));
    }

    private double getTotalAmount()
    {
        return totalFinal + ePayDiffAmount;
    }


    private boolean hasExtraFeeByCard()
    {
        String porcentajeAdicional = listParams.get(Globales.ParametrosDeApp.PARAM_96_PORCENTAJE_ADICIONAL_POR_PAGO_CON_TARJETA).getValue();
        return !TextUtils.isEmpty(porcentajeAdicional) && !"0".equals(porcentajeAdicional);
    }

    private void configureMotorDePagoLogo(){
        txtMercadoPago = findViewById(R.id.txtMercadopago);
        imgPlaceToPay = findViewById(R.id.imgPlaceToPayLogo);

        imgPlaceToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String url = PlaceToPayGlobales.URL_WEBSITE;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), R.string.redireccionando_a_place_to_pay,Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


        if(Utils.isMercadoPagoConfigured(listParams) &&
                !TextUtils.isEmpty(keyMercadoPagoPrivado) &&
                !TextUtils.isEmpty(keyMercado))
        {
            txtMercadoPago.setVisibility(View.VISIBLE);
            imgPlaceToPay.setVisibility(View.GONE);
        }else if(Utils.isPlaceToPayConfigured(listParams) &&
                !TextUtils.isEmpty(keyMercadoPagoPrivado) &&
                !TextUtils.isEmpty(keyMercado)){
            txtMercadoPago.setVisibility(View.GONE);
            imgPlaceToPay.setVisibility(View.VISIBLE);
        }
        else{
            showLoadingWithCancel(getString(R.string.error), getString(R.string.error_mercado_pago), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PayCreditCard.this,HomeChofer.class));
                    finish();
                }
            });
        }
    }

    private void validarEmail() {
        String email= editTextEmail.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!validationEmail(email)){
           editTextEmail.setError(getString(R.string.email_invalido));
        }else {
            payWithConfiguredPaymentProcessor();

        }
    }

    private void payWithConfiguredPaymentProcessor()
    {
        if(Utils.isMercadoPagoConfigured(listParams))
        {
            obtenerIdPago();
        }
        else if(Utils.isPlaceToPayConfigured(listParams)){
            payWithPlaceToPay();
        }
    }



    private void obtenerIdPago(){
        final ItenMercadoPago itenMercadoPago= new ItenMercadoPago();
        itenMercadoPago.setIdTravel(currentTravel.getIdTravel());
        itenMercadoPago.setItemTitle("Pago "+ getString(R.string.app_name));
        itenMercadoPago.setUnit_price(getTotalAmount());
        itenMercadoPago.setEmail(editTextEmail.getText().toString().trim());
        itenMercadoPago.setConstencia("Origen:"+currentTravel.getNameOrigin()+", Destino:"+currentTravel.getNameDestination());

        progressBar.setVisibility(View.VISIBLE);
        buttonPagar.setVisibility(View.GONE);

        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        daoTravel.preferenceMercado(itenMercadoPago).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    editTextEmail.setEnabled(false);
                    if (response.body()!=null){
                        Log.d("ID PAGO",response.body());
                        startMercadoPagoCheckout(response.body());
                    }
                    else{
                        showMessage(getString(R.string.fallo_general),3);
                        buttonPagar.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    Log.e("FALLO",e.toString());
                    buttonPagar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("FALLO","OBTENER ID DE PAGO PREFERENCIA");

                progressBar.setVisibility(View.GONE);
                buttonPagar.setVisibility(View.VISIBLE);
                showMessage(getString(R.string.fallo_general),3);
            }
        });
    }

    private void startMercadoPagoCheckout(final String checkoutPreferenceId) {


        new MercadoPagoCheckout.Builder(keyMercado, checkoutPreferenceId).build()
                .startPayment(this, REQUEST_CODE);

    }



    private void guardarPago(final InformacionPagoCredit informacionPagoCredit){

        Log.d("Informacion PAGO Credit",informacionPagoCredit.makeJson());
        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        daoTravel.saveInfoPayCredit(informacionPagoCredit).enqueue(new Callback<GuardarPagoTcd>() {
            @Override
            public void onResponse(Call<GuardarPagoTcd> call, Response<GuardarPagoTcd> response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    if (response.body().getResponse()){
                        showMessage(getString(R.string.pago_guardado),1);
                        verifickTravelFinish();
                    }else {
                        showMessage(getString(R.string.no_guardo_pago),2);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                verifickTravelFinish();
                            }
                        },2500);
                    }
                }catch (Exception e){
                    Log.e("EXCEPTION GUARDAR PAGO","error:"+e.toString());
                    showMessage(getString(R.string.no_guardo_pago),2);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verifickTravelFinish();
                        }
                    },2500);
                }
            }

            @Override
            public void onFailure(Call<GuardarPagoTcd> call, Throwable t) {
                Log.e("FALLO GUARDADO",t.toString());
                showMessage(getString(R.string.no_guardo_pago),2);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verifickTravelFinish();
                    }
                },2500);
            }
        });

    }


    public  void  verifickTravelFinish()
    {
        loading = ProgressDialog.show(this, getString(R.string.finalizando_viaje), getString(R.string.espere_unos_segundos), true, false);

        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        daoTravel.verifickTravelFinish(currentTravel.idTravel).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                hideLoading();
                try {
                    boolean result = response.body();

                    if (result) {
                        deleteLocalRequestIdSavedPlaceToPay();
                        showMessage(getString(R.string.viaje_finalizado_previamente),1);
                        goToHome();
                    } else {
                        finishTravel();
                    }

                }catch (Exception e){
                    Log.e("Verificar Viaje Exception",e.toString());
                    progressBar.setVisibility(View.GONE);
                    buttonPagar.setVisibility(View.VISIBLE);
                    showMessage(getString(R.string.error_mercado_pago), Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Verificar Viaje OnFailura",t.toString());
                loading.cancel();
                progressBar.setVisibility(View.GONE);
                buttonPagar.setVisibility(View.VISIBLE);
                showMessage(getString(R.string.fallo_general),3);
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
                Utils.resetDurationForTravelByHour(getApplicationContext());
                hideLoading();
                try {
                    Log.d("RESPUESTA",response.body().string());
                    deleteLocalRequestIdSavedPlaceToPay();
                    int evaluationOperator;
                    if (isTravelComany == 1) {
                        evaluationOperator = PARAM_3;
                    } else {
                        evaluationOperator = PARAM_67;
                    }

                    if (evaluationOperator == 0) {
                        showMessage(getString(R.string.viaje_finalizado),1);
                        goToHome();
                    } else {
                        showMessage(getString(R.string.viaje_enviando_aprobacion),1);
                        goToHome();
                    }
                }catch (Exception e){
                    hideLoading();
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Finalizar viaje Viaje OnFailura",t.toString());
                hideLoading();
                showMessage(getString(R.string.fallo_general),3);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PayCreditCard.this,HomeChofer.class));
        finish();
    }

    private Boolean validationEmail(String _email){

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(_email);

        if (mather.find() == true) {
            return  true;

        } else {
            return  false;
        }

    }

    public void showMessage(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status);
    }

    private void goToHome(){
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(PayCreditCard.this,HomeChofer.class);
                startActivity(intent);
                finish();
            }
        },3500);
    }

    /********************************************/
    /** Región de código para Place To Pay ******/
    /********************************************/
    private void payWithPlaceToPay(){
        try {
            showLoading(getString(R.string.iniciando_proceso_pago), getString(R.string.espere_unos_segundos), false);
            PlaceToPayPaymentRequest placeToPayPaymentRequest = getPlaceToPayPaymentRequest();
            Retrofit retrofit = getPlaceToPayRetofit();
            PlaceToPayRetrofitInterface postService = retrofit.create(PlaceToPayRetrofitInterface.class);
            Call<PlaceToPayPaymentResult> call = postService.getPaymentRequest(placeToPayPaymentRequest);

            call.enqueue(new Callback<PlaceToPayPaymentResult>() {
                @Override
                public void onResponse(Call<PlaceToPayPaymentResult> call, Response<PlaceToPayPaymentResult> response) {
                    hideLoading();
                    PlaceToPayPaymentResult result =  response.body();
                    if(result!=null){
                        Utils.saveValueToSharedPreference(getApplicationContext(), PlaceToPayGlobales.LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP, String.valueOf(result.getRequestId()) );
                        openWebViewPlaceToPlacePayment(result.getProcessUrl(), result.getRequestId());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_pagar_place_to_pay, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PlaceToPayPaymentResult> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    hideLoading();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            hideLoading();
            Toast.makeText(getApplicationContext(), getString(R.string.error_pagar_place_to_pay), Toast.LENGTH_LONG).show();
        }
    }



    private void openWebViewPlaceToPlacePayment(String url, int requestId) {
        Intent intent = new Intent(getApplicationContext(), PlaceToPayPaymentActivity.class);
        intent.putExtra(PlaceToPayGlobales.PARAM_URL_PAYMENT, url);
        startActivityForResult(intent,REQUEST_CODE_FOR_PLACE_TO_PAY);
    }

    private Retrofit getPlaceToPayRetofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.isPlaceToPayTesting() ? PlaceToPayGlobales.URL_TEST : PlaceToPayGlobales.URL_PRODUCTION)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    private PlaceToPayPaymentRequest getPlaceToPayPaymentRequest()
    {
        PlaceToPayPaymentRequest result=new PlaceToPayPaymentRequest();
        PlaceToPayAuth auth = getPlaceToPayAuth(keyMercado, keyMercadoPagoPrivado);
        PlaceToPayBuyer buyer = getPlaceToPayBuyer();
        PlaceToPayPayment payPayment = getPlaceToPayPayment();
        result.setAuth(auth);
        result.setBuyer(buyer);
        result.setPayment(payPayment);
        result.setExpiration(getPlaceToPayExpiration());
        result.setIpAddress(Utils.getIPAddress(true));
        result.setReturnUrl(PlaceToPayGlobales.REDIRECT_URL_OK);
        result.setCancelUrl(PlaceToPayGlobales.REDIRECT_URL_CANCEL);
        result.setSkipResult(true);
        result.setUserAgent(System.getProperty("http.agent"));

        return result;
    }

    private PlaceToPayPaymentRequest getPlaceToPayGetInformationRequest()
    {
        PlaceToPayPaymentRequest result=new PlaceToPayPaymentRequest();
        PlaceToPayAuth auth = getPlaceToPayAuth(keyMercado, keyMercadoPagoPrivado);
        result.setAuth(auth);
        return result;
    }

    private PlaceToPayPaymentRequest getPlaceToPayReversePaymentRequest(int refefence)
    {
        PlaceToPayPaymentRequest result=new PlaceToPayPaymentRequest();
        PlaceToPayAuth auth = getPlaceToPayAuth(keyMercado, keyMercadoPagoPrivado);
        result.setAuth(auth);
        result.setInternalReference(refefence);
        return result;
    }

    private String getPlaceToPayExpiration(){
        String result="";
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingTenMins=new Date(t + (PlaceToPayGlobales.MINUTES_FOR_EXPIRATION_REQUEST * PlaceToPayGlobales.ONE_MINUTE_IN_MILLIS));
        result = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault())).format(afterAddingTenMins);
        return result;
    }



    private PlaceToPayAuth getPlaceToPayAuth(String placeToPayLogin, String placeToPayTrankey)
    {
        WSAuthentication wsAuthentication = new WSAuthentication(placeToPayLogin, placeToPayTrankey);
        PlaceToPayAuth auth = new PlaceToPayAuth();
        auth.setLogin(wsAuthentication.getLogin());
        auth.setNonce(wsAuthentication.getNonce());
        auth.setSeed(wsAuthentication.getSeed());
        auth.setTranKey(wsAuthentication.getTranKey());
        return auth;
    }

    private PlaceToPayBuyer getPlaceToPayBuyer() {
        PlaceToPayBuyer result = new PlaceToPayBuyer();
        result.setName(name);
        result.setSurname(surname);
        result.setDocument(document);
        result.setDocumentType(getDocumentType(document));
        result.setEmail(editTextEmail.getText().toString());
        result.setMobile(phone);
        return result;
    }

    private String getDocumentType(String documentNumber){
        return documentNumber!=null && documentNumber.length()==10?PlaceToPayGlobales.ECUADOR_DOCUMENT_TYPE: PlaceToPayGlobales.ECUADOR_DOCUMENT_TYPE_PASSPORT;
    }

    private PlaceToPayPayment getPlaceToPayPayment()
    {
        PlaceToPayPayment result=new PlaceToPayPayment();
        result.setReference(currentTravel.getCodTravel());
        result.setDescription("Viaje. Origen: ".concat(currentTravel.getNameOrigin()).concat(", Destino: ").concat(currentTravel.getNameDestination()));
        result.setAmount(new PlaceToPayAmount(PlaceToPayGlobales.CURRENCY_ECUADOR, getTotalAmount()));

        return result;
    }

    private void finalizePlaceToPayCancelByClient()
    {
        String referenceMsg = getString(R.string.referencia).concat(currentTravel.getCodTravel());
        showLoadingWithCancel(getString(R.string.pago_rechazado_title), getString(R.string.debe_realizar_pago).concat(referenceMsg), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void finalizePlaceToPayOk()
    {
        try {
            String placeToPayRequestId = Utils.getValueFromSharedPreferences(getApplicationContext(), PlaceToPayGlobales.LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP);
            if(!TextUtils.isEmpty(placeToPayRequestId))
            {
                int requestId = Integer.parseInt(placeToPayRequestId);
                showLoading(getString(R.string.procesando_pago),getString(R.string.espere_unos_segundos), false);
                progressBar.setVisibility(View.VISIBLE);
                PlaceToPayPaymentRequest placeToPayPaymentRequest = getPlaceToPayGetInformationRequest();
                Retrofit retrofit = getPlaceToPayRetofit();
                PlaceToPayRetrofitInterface postService = retrofit.create(PlaceToPayRetrofitInterface.class);
                Call<PlaceToPayPaymentResult> call = postService.getPaymentInformation( requestId, placeToPayPaymentRequest);

                call.enqueue(new Callback<PlaceToPayPaymentResult>() {
                    @Override
                    public void onResponse(Call<PlaceToPayPaymentResult> call, Response<PlaceToPayPaymentResult> response) {
                        PlaceToPayPaymentResult result =  response.body();
                        if(result!=null){
                            manageStatusFinalizePaymentPlaceToPay(result);
                        }
                        else {
                            showRetryMessagePlaceToPay();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlaceToPayPaymentResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        showRetryMessagePlaceToPay();
                    }
                });
            }
            else{
             showLoadingWithCancel(getString(R.string.error_en_el_pago),getString(R.string.error_en_el_pago_msg),null);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            showRetryMessagePlaceToPay();
        }
    }

    private void showRetryMessagePlaceToPay()
    {
        showLoadingWithCancel(getString(R.string.error), getString(R.string.error_procesar_place_to_pay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLocalRequestIdSavedPlaceToPay();
            }
        });
    }

    private void manageStatusFinalizePaymentPlaceToPay(final PlaceToPayPaymentResult result)
    {
        String referenceMsg = getString(R.string.referencia).concat(currentTravel.getCodTravel());
        switch (result.getStatus().getStatus())
        {
            case PlaceToPayGlobales.StatusPlaceToPay.APPROVED:
                sendDataToBackendFinishPaymentPlaceToPay(result);
                break;
            case PlaceToPayGlobales.StatusPlaceToPay.FAILED:
                deleteLocalRequestIdSavedPlaceToPay();
                showLoadingWithCancel(getString(R.string.pago_rechazado_title),getString(R.string.pago_rechazado_retry),null);
                break;
            case PlaceToPayGlobales.StatusPlaceToPay.REJECTED:
                deleteLocalRequestIdSavedPlaceToPay();

                showLoadingWithCancel(getString(R.string.pago_rechazado_title),getString(R.string.pago_rechazado_retry).concat(referenceMsg),null);
                break;
            case PlaceToPayGlobales.StatusPlaceToPay.PENDING:
                sendDataToBackendFinishPaymentPlaceToPay(result);
                break;
            case PlaceToPayGlobales.StatusPlaceToPay.PENDING_VALIDATION:
                sendDataToBackendFinishPaymentPlaceToPay(result);
                break;
            default:
                deleteLocalRequestIdSavedPlaceToPay();
                showLoadingWithCancel(getString(R.string.tarjeta_no_valida), getString(R.string.intente_con_otra_tarjeta).concat(referenceMsg), null);
                break;
        }
    }

    private void sendDataToBackendFinishPaymentPlaceToPay(final PlaceToPayPaymentResult result)
    {
        String msg="";
        String title="";

        if(result.getPayment()!=null)
        {
            if(PlaceToPayGlobales.StatusPlaceToPay.APPROVED.equals(result.getStatus().getStatus()))
            {
                msg = getString(R.string.codigo_autorizacion).concat(result.getPayment().get(0).getAuthorization()).concat("\n");
                title = getString(R.string.transaccion_aprobada_title);
            }
            else
            {
                title=getString(R.string.pago_pendiente);
                msg=getString(R.string.pago_pendiente_msg).concat("\n");
            }

            try{

                Log.e("REFERENCE", String.valueOf(result.getRequestId()));
                Log.e("REFERENCE_PAYMENT", String.valueOf(result.getPayment().get(0).getReference()));

                msg += getString(R.string.metodo_de_pago).concat(result.getPayment().get(0).getPaymentMethodName()).concat("\n")
                        .concat(getString(R.string.id_de_referencia_es)).concat(String.valueOf(result.getPayment().get(0).getReference()));

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            showLoadingWithCancel(title, msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showLoading(getString(R.string.finalizando_viaje), getString(R.string.espere_unos_segundos), false);
                    String placeToPayRequestId = Utils.getValueFromSharedPreferences(getApplicationContext(), PlaceToPayGlobales.LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP);
                    InformacionPagoCredit informacionPagoCredit= new InformacionPagoCredit();
                    informacionPagoCredit.setAmount(String.valueOf(getTotalAmount()));
                    informacionPagoCredit.setIdPayment(placeToPayRequestId);
                    informacionPagoCredit.setIdTravel(String.valueOf(currentTravel.getIdTravel()));
                    informacionPagoCredit.setStatusPayment(result.getStatus().getStatus());
                    guardarPago(informacionPagoCredit);
                    //getStatusPayment(result.getRequestId());
                }
            });
        }
        else{
            finalizePlaceToPayCancelByClient();
        }
    }

    private void getStatusPayment(int requestId){
        PlaceToPayPaymentRequest request = getPlaceToPayPaymentRequest();
        Retrofit retrofit = getPlaceToPayRetofit();
        PlaceToPayRetrofitInterface postService = retrofit.create(PlaceToPayRetrofitInterface.class);
        Call<PlaceToPayPaymentResult> call = postService.getStatusPayment( requestId, request);

        call.enqueue(new Callback<PlaceToPayPaymentResult>() {
            @Override
            public void onResponse(Call<PlaceToPayPaymentResult> call, Response<PlaceToPayPaymentResult> response) {
                PlaceToPayPaymentResult result =  response.body();
                if(result!=null){
                    Log.e("RESULT_STATUS_PAY","FINALiZADO OK");
                }
                else {
                    Log.e("RESULT_STATUS_PAY","FINALiZADO ERROR");
                }
            }

            @Override
            public void onFailure(Call<PlaceToPayPaymentResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void deleteLocalRequestIdSavedPlaceToPay()
    {

        Utils.saveValueToSharedPreference(getApplicationContext(),PlaceToPayGlobales.LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP,"");
    }

    private void showLoading(String title, String msg, boolean cancelable)
    {
        try{
            if(loading!=null)
            {
                loading.dismiss();
            }
            loading = ProgressDialog.show(this,  title, msg, true, cancelable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showLoadingWithCancel(String title, String msg, DialogInterface.OnClickListener onClickListener)
    {
        try{

            hideLoading();

            AlertDialog.Builder builder = new AlertDialog.Builder(PayCreditCard.this);
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


    private void hideLoading()
    {
        if(loading!=null)
        {
            try{
                loading.dismiss();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(alertDialog!=null)
        {
            try{
                alertDialog.dismiss();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }



    private  void getInfoClient() {
        try{
            ServicesLoguin daoAuth = HttpConexion.getUri().create(ServicesLoguin.class);

            int typeClientProfile = currentTravel.getIsTravelComany() == 1? Globales.ProfileId.CLIENT_COMPANY: Globales.ProfileId.CLIENT_PARTICULAR;
            int userId= currentTravel.getIsTravelComany() == 1 ?
                    currentTravel.getIdUserCompanyKf() : currentTravel.getIdClient();
            if(userId!=0){
                getInfoClientByDefaultEndpoint(daoAuth, typeClientProfile, userId);
            }
            else{
                getInfoClientByPassengersEndpoint();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getInfoClientByDefaultEndpoint(ServicesLoguin daoAuth, int typeClientProfile, int userId) {
        daoAuth.findWithDiscriminate(userId, typeClientProfile).enqueue(new Callback<RequetClient>() {
            @Override
            public void onResponse(Call<RequetClient> call, Response<RequetClient> response) {
                try {
                    RequetClient rs = response.body();
                    if (rs != null) {
                        try {
                            editTextEmail.setText(rs.getClient().getMailClient());
                            name = rs.getClient().getFirtNameClient();
                            surname = rs.getClient().getLastNameClient();
                            document = rs.getClient().getDniClient();
                            phone = rs.getClient().getPhoneClient();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    Log.e("ERROR OBTNER INFO",e.toString());
                }
            }

            @Override
            public void onFailure(Call<RequetClient> call, Throwable t) {
                Log.e("ONFAILUER OBTNER INFO",t.toString());
                //showMessage(getString(R.string.fallo_general),3);

            }
        });
    }

    private void getInfoClientByPassengersEndpoint() {
        ServicesTravel servicesTravel = HttpConexion.getUri().create(ServicesTravel.class);
        Call<List<Passenger>> call = servicesTravel.getPassengerFromTravel(currentTravel.getIdTravel());

        call.enqueue(new Callback<List<Passenger>>() {
            @Override
            public void onResponse(Call<List<Passenger>> call, Response<List<Passenger>> response) {
                try {

                    if (response != null && response.body()!=null) {
                        List<Passenger> listPassenger = response.body();
                        try {
                            if(listPassenger.size()>=1){
                                editTextEmail.setText(listPassenger.get(1).getEmailPasaguer()!=null ? listPassenger.get(1).getEmailPasaguer(): "");
                                name =  listPassenger.get(1).getFullNamePanguer()!=null ? listPassenger.get(1).getFullNamePanguer(): "";
                                surname = "";
                                document="";
                                phone = listPassenger.get(1).getPhonePasaguer()!=null ? listPassenger.get(1).getPhonePasaguer(): "";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    Log.e("ERROR OBTNER INFO",e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Passenger>> call, Throwable t) {
                Log.e("ONFAILUER OBTNER INFO",t.toString());
                //showMessage(getString(R.string.fallo_general),3);

            }
        });
    }


}
