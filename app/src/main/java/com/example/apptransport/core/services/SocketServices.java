package com.example.apptransport.core.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Activity.LoginActivity;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceive;
import com.apreciasoft.mobile.asremis.Entity.token;
import com.apreciasoft.mobile.asremis.Entity.tokenFull;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class SocketServices extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String CURRENT_TRAVEL_CHOFER_RECEIVER_TAG="MyCurrentTravelChofer";
    public static final String IS_DRIVER_ACTIVE_RECEIVER_TAG="IsDriverActiveReceiver";
    public static final String CHAT_RECEIVER_TAG="ChatReceiver";
    public static String CHAT_EVENT_RECEIVE = "chatmessage";
    public static String CHAT_EVENT_SEND = "sendmessage";

    private static final String TAG = "SocketServices";
    private MyCurrentTravelChoferReceiver myBroadCastReceiver;
    private IsDriverActiveReceiver isDriverBroadCastReceiver;
    private SendMessageChatReceiver sendMessageChatReceiver;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Location location;
    private Timer timer;

    public Socket socket;
    public String urlSocket =  HttpConexion.PROTOCOL+"://"+HttpConexion.ip+":"+HttpConexion.portWsWeb+"";
    public ServicesLoguin daoLoguin = null;
    private String fullNameDriver="",nrDriver="";
    private int idDriver=0;

    private String currentTravel;
    private String currentTravel_codTravel;
    private String currentTravel_nameStatusTravel;
    private String currentTravel_nameOrigin;
    private String currentTravel_nameDestination;
    private String currentTravel_client;
    private String currentTravel_clientId;

    private String currentTravel_phoneNumberDriver;
    private String currentTravel_latOrigin;
    private String currentTravel_lngOrigin;
    private String isDriverActive="true";
    private String isDriverPaused = "false";

    private int user_id;

    private NotificationCompat.Builder mBuilder;
    private String CHANNEL_ID = "antonella_ch_1";
    private String latitud="";
    private String longitud="";
    private boolean primeraLectura= false;
    static private final int NOTIFICATION_ID = 119;
    public Handler handlerConsumirUbicacion;



    @Override
    public void onCreate() {
        super.onCreate();
        HttpConexion.setBase(HttpConexion.instance);
        buildGoogleApiClient();
        registerMyReceiver();
        registerIsDriverActiveReceiver();
        registerChatSendMessageReceiver();
        handlerConsumirUbicacion = new Handler();

    }

    @SuppressWarnings("ConstantConditions")
    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            fullNameDriver= intent.getStringExtra("fullNameDriver");
            nrDriver= intent.getStringExtra("nrDriver");
            idDriver= intent.getIntExtra("idDriver", 0);
            currentTravel = intent.getStringExtra("currentTravel");
            currentTravel_codTravel = intent.getStringExtra("currentTravel_codTravel");
            currentTravel_nameStatusTravel = intent.getStringExtra("currentTravel_nameStatusTravel");
            currentTravel_nameOrigin = intent.getStringExtra("currentTravel_nameOrigin");
            currentTravel_nameDestination = intent.getStringExtra("currentTravel_nameDestination");
            currentTravel_client = intent.getStringExtra("currentTravel_client");
            currentTravel_clientId = intent.getStringExtra("currentTravel_clientId");
            currentTravel_phoneNumberDriver = intent.getStringExtra("currentTravel_phoneNumberDriver");
            currentTravel_latOrigin = intent.getStringExtra("currentTravel_latOrigin");
            currentTravel_lngOrigin = intent.getStringExtra("currentTravel_lngOrigin");
            user_id= intent.getIntExtra("user_id",0);

            createNotification();

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return Service.START_STICKY;
    }



    private  void  createNotification(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_gps_auto)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.capturando_ubicacion))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);

            String description = getString(R.string.capturando_ubicacion);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    //region STATUS GPS
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //endregion


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        Log.e(TAG,"APP: ".concat("startLocationUpdate"));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener,null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, locationListener);
        }
    }


    private LocationListener locationListenerSingle =  new LocationListener (){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "ERR_APP".concat("onLocationChanged locationListenerSingle"));
            Log.d(TAG,"ENTRO SINGLE: ".concat("onLocationChange: " + " LATITU: "+location.getLatitude()+" LONGITUD: "+location.getLongitude()));
            latitud= String.valueOf(location.getLatitude());
            longitud=String.valueOf(location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private LocationListener locationListener =  new LocationListener (){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "ENTRO: ".concat("onLocationChange: "+" LATITU: "+location.getLatitude()+" LONGITUD: "+location.getLongitude()));
            Log.e(TAG, "APP: ".concat("onLocationChanged"));
            latitud= String.valueOf(location.getLatitude());
            longitud=String.valueOf(location.getLongitude());

            if (primeraLectura==false){
                consumirUbicacion();
                primeraLectura=true;
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onProviderDisabled(String s) {
            Log.e(TAG, "onProviderDisabled");
            //finalizarServicio();
        }
    };

    private void consumirUbicacion(){
        handlerConsumirUbicacion.postDelayed(runnableConsumirUbicacion,3000);
    }

    Runnable runnableConsumirUbicacion = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            validarSocketActivo();
            consumirUbicacion();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        stopLocationUpdate();

        try{
            unregisterReceiver(myBroadCastReceiver);
            unregisterReceiver(isDriverBroadCastReceiver);
            unregisterReceiver((sendMessageChatReceiver));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        removeCallbacksConsumirUbicacion();
        super.onDestroy();
    }

    private void removeCallbacksConsumirUbicacion(){
        try{
            handlerConsumirUbicacion.removeCallbacks(runnableConsumirUbicacion);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void stopLocationUpdate() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /***********************************************
     * REGION SOCKET
     ***********************************************/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void validarSocketActivo(){
        if(socket == null || socket.id()==null){
            conexionSocketMap();
        } else {
            try {
                JSONObject location_ = new JSONObject();
                location_.put("log", latitud);
                location_.put("lat", longitud);

                JSONObject obj = new JSONObject();
                obj.put("location", location_);
                obj.put("fullNameDriver", fullNameDriver);
                obj.put("nrDriver",nrDriver);
                obj.put("idDriver",idDriver);

                if(currentTravel != null && !"".equals(currentTravel)) {
                    obj.put("currentTravel", getCurrentTravelJson());
                } else{
                    obj.put("currentTravel",null );
                }

                boolean isInService = "true".equals(isDriverActive);

                Log.v(TAG, "NEW_LOCATION: ".concat(obj.toString()));
                Log.v(TAG, "DRIVER_IN_SERVICE: ".concat(String.valueOf(isInService)));
                if(socket != null) {
                    Log.d(TAG, "SocketId: ".concat(socket.id()!=null ? socket.id(): "NULL"));

                    if(isInService)
                    {
                        socket.emit("newlocation", obj, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d(TAG,"SOCK MAP: ".concat("newlocation OK"));
                            }
                        });
                    }
                    if(!isInService && "true".equals(isDriverPaused)) {
                        isDriverPaused="false";
                        socket.emit("outofservice", obj, new Ack() {
                            @Override
                            public void call(Object... args) {
                                Log.d(TAG,"SOCK MAP: ".concat("outofservice OK"));
                            }
                        });
                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject getCurrentTravelJson(){
        JSONObject jsonCurrentTravel = new JSONObject();
        try{
            jsonCurrentTravel.put("idTravel",currentTravel );
            jsonCurrentTravel.put("codTravel",currentTravel_codTravel );
            jsonCurrentTravel.put("nameStatusTravel",currentTravel_nameStatusTravel );
            jsonCurrentTravel.put("nameOrigin",currentTravel_nameOrigin );
            jsonCurrentTravel.put("nameDestination",currentTravel_nameDestination );
            jsonCurrentTravel.put("client",currentTravel_client );
            jsonCurrentTravel.put("idClient",currentTravel_clientId );
            jsonCurrentTravel.put("phoneNumberDriver",currentTravel_phoneNumberDriver );

            jsonCurrentTravel.put("latOrigin",currentTravel_latOrigin);
            jsonCurrentTravel.put("lngOrigin",currentTravel_lngOrigin);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return jsonCurrentTravel;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void conexionSocketMap() {
        try{
            if(socket == null ) {
                if (HttpConexion.PROTOCOL.equals("https")) {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new SecureRandom());
                    IO.setDefaultSSLContext(sc);

                    IO.Options options = new IO.Options();
                    options.sslContext = sc;
                    options.secure = true;
                    options.port = HttpConexion.portWsWeb;
                    socket = IO.socket(urlSocket, options);

                } else {
                    socket = IO.socket(urlSocket);
                }
                Log.d(TAG, "SOCK MAP: ".concat("va a conectar: "+urlSocket));
            }

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    Log.d(TAG,"SOCK MAP: ".concat("EVENT_CONNECT"));
                    sendSocketId();
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    Log.d(TAG,"SOCK MAP: ".concat("EVENT_DISCONNECT"));

                }
            }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener(){
                @Override
                public void call(Object... args) {

                    Log.d(TAG,"SOCK MAP: ".concat("EVENT_RECONNECT_ERROR"));
                    if(args!=null)
                    {
                        for (Object obj :
                                args) {
                            Log.d(TAG, "SOCK MAP: ".concat(obj.toString()));
                        }
                    }
                }
            }).on(CHAT_EVENT_RECEIVE, new Emitter.Listener(){
                @Override
                public void call(Object... args) {
                    /* Our code */
                    Log.e(TAG,"CHAT_EVENT_RECEIVE CALLED");
                    try{
                        String  messageJson =  args[0].toString();
                        final JSONObject obj = new JSONObject(messageJson);
                        Log.e(TAG, obj.toString());
                        ChatMessageReceive messageObj =  Utils.getChatMessageFromJson(obj);
                        if("true".equals(messageObj.getChatFromWeb()) || "1".equals(messageObj.getChatFromWeb())) {
                            enviarChatMessage(messageObj);
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }



                }
            });


            if(socket != null){
                if(!socket.connected()) {
                    socket.connect();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***********************************************
     * REGION SOCKET CHAT
     ***********************************************/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendChatMessage(String msg, String userWeb){
        if(socket == null || socket.id()==null){
            conexionSocketMap();
        } else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("msg", msg);
                obj.put("user", userWeb);
                obj.put("driver",idDriver);
                Log.e(TAG, obj.toString());
                if(socket != null) {
                    Log.e(TAG, "SocketId: ".concat(socket.id()!=null ? socket.id(): "NULL"));
                    socket.emit(CHAT_EVENT_SEND, obj, new Ack() {
                        @Override
                        public void call(Object... args) {
                            Log.d(TAG,"SOCK MAP: ".concat("CHAT OK"));
                        }
                    });
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    } };

    /**
     * Se envía el socket id a un Endpoint
     */
    public void sendSocketId() {
        if(socket != null) {
            if (socket.id() != null) {
                daoLoguin = HttpConexion.getUri().create(ServicesLoguin.class);
                try {
                    Log.d(TAG,"SOCK MAP: ".concat("ENVIADO "+socket.id()));

                    if (socket.id() != null) {
                        token T = new token();
                        T.setToken(new tokenFull(user_id, socket.id()));

                        daoLoguin.updateSocketWeb(T).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                Log.d(TAG.concat("ResponseRequest"), call.request().toString());
                                Log.d(TAG.concat("ResponseRequestHeader"), call.request().headers().toString());
                                Log.d(TAG.concat("ResponseRawHeader"), response.headers().toString());
                                Log.d(TAG.concat("ResponseRaw"), String.valueOf(response.raw().body()));
                                Log.d(TAG.concat("ResponseCode"), String.valueOf(response.code()));
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                               t.printStackTrace();
                            }
                        });
                    }
                } finally {
                    daoLoguin = null;
                }
            }
        }
    }
    //endregion


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void finalizarServicio(){
        try{
            Toast.makeText(this.getApplicationContext(),getApplication().getText(R.string.servicio_tracking_detenido),Toast.LENGTH_SHORT).show();
            unregisterReceiver(myBroadCastReceiver);
            unregisterReceiver(isDriverBroadCastReceiver);
            //unregisterReceiver(sendMessageChatReceiver);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                stopForeground(intent.getFlags());
            }
            else{
                stopForeground(false);
            }
            stopSelf();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * This method is responsible to register an action to BroadCastReceiver
     * */
    private void registerMyReceiver() {
        try
        {
            myBroadCastReceiver = new MyCurrentTravelChoferReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CURRENT_TRAVEL_CHOFER_RECEIVER_TAG);
            registerReceiver(myBroadCastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void registerIsDriverActiveReceiver() {
        try
        {
            isDriverBroadCastReceiver = new IsDriverActiveReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IS_DRIVER_ACTIVE_RECEIVER_TAG);
            registerReceiver(isDriverBroadCastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void registerChatSendMessageReceiver() {
        try
        {
            sendMessageChatReceiver = new SendMessageChatReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CHAT_RECEIVER_TAG);
            registerReceiver(sendMessageChatReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void enviarChatMessage(ChatMessageReceive msg){
        try{
            Intent intent = new Intent();
            intent.putExtra("message",msg.serialize());
            intent.setAction(HomeChofer.CHAT_RECEIVER_TAG);
            sendBroadcast(intent);
        }
        catch (Exception ex){

            ex.printStackTrace();
        }
    }



    public class MyCurrentTravelChoferReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if(extras!= null){
                if(extras.containsKey("currentTravel")){
                    currentTravel =extras.getString("currentTravel");
                    currentTravel_client = extras.getString("currentTravel_client");
                    currentTravel_clientId = extras.getString("currentTravel_clientId");
                    currentTravel_codTravel = extras.getString("currentTravel_codTravel");
                    currentTravel_nameDestination = extras.getString("currentTravel_nameDestination");
                    currentTravel_nameOrigin = extras.getString("currentTravel_nameOrigin");
                    currentTravel_nameStatusTravel = extras.getString("currentTravel_nameStatusTravel");
                    currentTravel_phoneNumberDriver = extras.getString("currentTravel_phoneNumberDriver");

                    currentTravel_latOrigin= extras.getString("currentTravel_latOrigin");
                    currentTravel_lngOrigin= extras.getString("currentTravel_lngOrigin");
                    validarSocketActivo();
                    Log.e(CURRENT_TRAVEL_CHOFER_RECEIVER_TAG, currentTravel );

                }
            }
        }
    }

    public class IsDriverActiveReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if(extras!= null){
                try{
                    if(extras.containsKey("isDriverActive")){
                        isDriverActive =extras.getString("isDriverActive");
                        isDriverPaused =extras.getString("isDriverPaused");
                        Log.e("IsDriverActiveReceiver", isDriverActive );
                        validarSocketActivo();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    public class SendMessageChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if(extras!= null){
                try {

                    String message = extras.getString("message");
                    String userWeb = extras.getString("userWeb");
                    Log.e("CHAT DEL PASAJERO", message);
                    sendChatMessage(message, userWeb);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}
