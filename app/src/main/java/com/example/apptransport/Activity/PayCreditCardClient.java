package com.example.apptransport.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.apreciasoft.mobile.asremis.Entity.TravelBodyForClientPayment;
import com.apreciasoft.mobile.asremis.Entity.TravelEntityForClientPayment;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
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

public class PayCreditCardClient extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_FOR_PLACE_TO_PAY=2;
    private EditText editTextEmail;
    private Button buttonPagar;
    private ProgressBar progressBar;
    private TextView txtMercadoPago;
    private ImageView imgPlaceToPay;

    public static InfoTravelEntity currentTravel;
    private Double totalFinal;
    private Double amounCalculateGps;
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


    private String name, surname, document, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_credit_card_client);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverNotifications, new IntentFilter("update-message"));

        Bundle extras = getIntent().getExtras();
        String stringcurrent= extras.getString("current");
        Gson gson= new Gson();
        currentTravel= gson.fromJson(stringcurrent,InfoTravelEntity.class);

        totalFinal= extras.getDouble("TotalAmount");
        keyMercado= extras.getString("keyMercado");
        keyMercadoPagoPrivado= extras.getString("keyMercadoPrivado");
        paymentCardProvider = extras.getString("PAYMENT_CARD_PROVIDER");
        listParams = Utils.getParamsFromLocalPreferences(getApplicationContext());
        String idPreferencia= extras.getString("idPreferencia");

        TextView textViewMonto= findViewById(R.id.txt_mount2);
        textViewMonto.setText("$ "+String.valueOf(totalFinal));


        ImageButton imageButtonCerrar= findViewById(R.id.imageButton_cerrar);
        imageButtonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFinishPaymentProcessByClient(Globales.ClientPaymentStatus.PAGA_CLIENTE_CANCELADO_POR_CLIENTE);
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

        if (idPreferencia!=null){
            startMercadoPagoCheckout(idPreferencia);
        }
        configureMotorDePagoLogo();
        getInfoClient();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                if (payment.getPaymentStatus().equals("approved")){
                    Log.d("ID PAGO",String.valueOf(payment.getId()));
                  //  Log.d("TARJETA PAGO",payment.getCard().toString());
                    Log.d("RESULTADO PAGO",payment.getPaymentStatusDetail());

                    InformacionPagoCredit informacionPagoCredit= new InformacionPagoCredit();
                    informacionPagoCredit.setAmount(String.valueOf(totalFinal));
                    informacionPagoCredit.setIdPayment(String.valueOf(payment.getId()));
                    informacionPagoCredit.setIdTravel(String.valueOf(currentTravel.getIdTravel()));
                    informacionPagoCredit.setStatusPayment(PlaceToPayGlobales.StatusPlaceToPay.APPROVED);
                    guardarPago(informacionPagoCredit, 0);

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

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(broadcastReceiverNotifications);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
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
                    startActivity(new Intent(PayCreditCardClient.this,HomeChofer.class));
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
            callInitPaymentProcess();
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
        itenMercadoPago.setUnit_price(totalFinal);
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



    private void guardarPago(final InformacionPagoCredit informacionPagoCredit, final int retryNumber){

        if(retryNumber < Globales.MAX_RETRY_SAVE_CREDIT_CARD_DATA)
        {
            Log.d("Informacion PAGO Credit",informacionPagoCredit.makeJson());
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            daoTravel.saveInfoPayCredit(informacionPagoCredit).enqueue(new Callback<GuardarPagoTcd>() {
                @Override
                public void onResponse(Call<GuardarPagoTcd> call, Response<GuardarPagoTcd> response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        if (response.body().getResponse()){
                            callFinishPaymentProcessByClient(Globales.ClientPaymentStatus.PAGA_CLIENTE_REALIZADO);
                        }else {
                            showMessage(getString(R.string.no_guardo_pago),2);
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    retryGuardarPago(informacionPagoCredit, retryNumber);
                                }
                            },2500);
                        }
                    }catch (Exception e){
                        Log.e("EXCEPGUARDARPAGO","error:"+e.toString());
                        showMessage(getString(R.string.no_guardo_pago),Globales.StatusToast.WARNING);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                retryGuardarPago(informacionPagoCredit, retryNumber);
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
                            retryGuardarPago(informacionPagoCredit, retryNumber);
                        }
                    },2500);
                }
            });
        }
        else{
            callFinishPaymentProcessByClient(Globales.ClientPaymentStatus.PAGA_CLIENTE_REALIZADO_PERO_ERROR_AL_GUARDAR_DATOS);
        }


    }

    private void retryGuardarPago(final InformacionPagoCredit informacionPagoCredit, int retryNumber)
    {
        showLoading(getString(R.string.finalizando_viaje).concat(getString(R.string.intento_n)).concat(String.valueOf(retryNumber+1)).concat( getString(R.string.de)).concat(String.valueOf(Globales.MAX_RETRY_SAVE_CREDIT_CARD_DATA)), getString(R.string.espere_unos_segundos), false);
        guardarPago(informacionPagoCredit, retryNumber+1);
    }

    @Override
    public void onBackPressed() {
        callFinishPaymentProcessByClient(Globales.ClientPaymentStatus.PAGA_CLIENTE_CANCELADO_POR_CLIENTE);
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
                Intent intent= new Intent(PayCreditCardClient.this,HomeChofer.class);
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
                        Toast.makeText(getApplicationContext(), getString(R.string.error_pagar_place_to_pay), Toast.LENGTH_LONG).show();
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
        result.setAmount(new PlaceToPayAmount(PlaceToPayGlobales.CURRENCY_ECUADOR, totalFinal));

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

                showLoadingWithCancel(getString(R.string.pago_rechazado_title),getString(R.string.intente_con_otra_tarjeta).concat(referenceMsg),null);
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
                title=getString(R.string.transaccion_aprobada_title);
            }
            else
            {
                title=getString(R.string.pago_pendiente);
                msg=getString(R.string.pago_pendiente).concat("\n");
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
                    informacionPagoCredit.setAmount(String.valueOf(totalFinal));
                    informacionPagoCredit.setIdPayment(placeToPayRequestId);
                    informacionPagoCredit.setIdTravel(String.valueOf(currentTravel.getIdTravel()));
                    informacionPagoCredit.setStatusPayment(result.getStatus().getStatus());
                    guardarPago(informacionPagoCredit, 0);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PayCreditCardClient.this);
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

    private void callFinishPaymentProcessByClient(int status)
    {
        showLoading(getString(R.string.finalizando_pago),getString(R.string.espere_unos_segundos),false);

        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        TravelEntityForClientPayment entity = new TravelEntityForClientPayment();
        TravelBodyForClientPayment request=new TravelBodyForClientPayment();
        entity.setIdTravel(currentTravel.getIdTravel());
        entity.setClientPaymentStatus(status);
        request.setTravel(entity);

        daoTravel.finishPaymentProcessByClient(request).enqueue(new Callback<InfoTravelEntity>() {
            @Override
            public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                try {
                    hideLoading();
                    if (response!=null && response.body()!=null){
                        closeActivity(true);
                    }else {
                        showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.WARNING);
                    }
                }catch (Exception e){
                    Log.e("EXCEPTION","error:"+e.toString());
                    showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.WARNING);
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                Log.e("FALLO CALL_CLIENT",t.toString());
                showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.FAIL);
                hideLoading();
            }
        });


    }

    private void callInitPaymentProcess()
    {
        showLoading(getString(R.string.iniciando_proceso_de_pago),getString(R.string.espere_unos_segundos),false);
        ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        TravelEntityForClientPayment entity = new TravelEntityForClientPayment();
        TravelBodyForClientPayment request=new TravelBodyForClientPayment();
        entity.setIdTravel(currentTravel.getIdTravel());
        entity.setClientPaymentStatus(Globales.ClientPaymentStatus.PAGA_CLIENTE_INICIO_EL_PROCESO_DE_PAGO);
        request.setTravel(entity);

        daoTravel.finishPaymentProcessByClient(request).enqueue(new Callback<InfoTravelEntity>() {
            @Override
            public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                try {
                    hideLoading();
                    if (response!=null && response.body()!=null){
                        payWithConfiguredPaymentProcessor();
                    }else {
                        showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.WARNING);
                    }
                }catch (Exception e){
                    Log.e("EXCEPTION","error:"+e.toString());
                    showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.WARNING);
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                Log.e("FALLO CALL_CLIENT",t.toString());
                showMessage(getString(R.string.error_call_finish_client_payment),Globales.StatusToast.FAIL);
                hideLoading();
            }
        });


    }


    private void closeActivity(boolean isOk)
    {
        Intent data = new Intent();
        String textToReturn = isOk ? "OK" : "CANCEL";
        data.setData(Uri.parse(textToReturn));
        setResult(RESULT_CANCELED, data);
        finish();
    }


    /*
BroadCastReceiver for Push Notifications
 */
    private BroadcastReceiver broadcastReceiverNotifications = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Utils.isForegroundActivity(getApplicationContext(), "PayCreditCardClient"))
            {
                getCurrentTravelByIdClient();
            }

        }
    };


    public  void getCurrentTravelByIdClient()
    {
        if(!Utils.verificaConexion(this)) {
            showAlertNoConexion();
        }else {
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);

            int typeClientProfile = currentTravel.getIsTravelComany() == 1? Globales.ProfileId.CLIENT_COMPANY: Globales.ProfileId.CLIENT_PARTICULAR;
            int userId= currentTravel.getIsTravelComany() == 1 ?
                    currentTravel.getIdUserCompanyKf() : currentTravel.getIdClient();

            Call<InfoTravelEntity> call = null;
            if(typeClientProfile == Globales.ProfileId.CLIENT_PARTICULAR){
                call = daoTravel.getCurrentTravelByIdClient(userId);

            }else {
                call = daoTravel.getCurrentTravelByIdUserCompany(userId);
            }

            call.enqueue(new Callback<InfoTravelEntity>() {
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                    try {
                        if(response!=null && response.body()!=null)
                        {
                            InfoTravelEntity currentTravel = response.body();
                            //manageClientHasFinish();
                            manageClientNotification(currentTravel);

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

    private void manageClientNotification(InfoTravelEntity infoTravel)
    {
        if(infoTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO &&
                infoTravel.getClientPaymentStatus()== Globales.ClientPaymentStatus.PAGA_CLIENTE_TIEMPO_EXPIRADO)
        {
            //Debo cancelar el pago
            closeActivity(false);

        }
    }


    /* ALERTA SIN CONEXION*/
    public void showAlertNoConexion(){
        try {
            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.problema_conexion_internet),
                    30000);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(null, Typeface.BOLD);

            snackbar.setAction(getString(R.string.verificar), new View.OnClickListener() {
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
}
