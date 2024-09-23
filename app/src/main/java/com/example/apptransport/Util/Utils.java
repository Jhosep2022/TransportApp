package com.example.apptransport.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.media.AudioAttributes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceive;
import com.apreciasoft.mobile.asremis.Entity.CompanyData;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.NewDriverOptionalValueData;
import com.apreciasoft.mobile.asremis.Entity.PriceAndIsMinimum;
import com.apreciasoft.mobile.asremis.Entity.VehicleType;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
//import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayGlobales;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayGlobales;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.core.services.SocketServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.common.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by usario on 25/4/2017.
 */

public class Utils {

    public static void setBadgeCount(Context context, LayerDrawable icon, int count,int drawableImageId) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(drawableImageId);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(drawableImageId, badge);
        badge.invalidateSelf();
        icon.invalidateDrawable(badge);
    }

    public static void setupBadge(int mCartItemCount, TextView textCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public static void setListViewHeight(ExpandableListView listView,
                                         int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }




    public static String covertSecondsToHHMMSS(int tsegundos)
    {
        int horas = (tsegundos / 3600);
        int minutos = ((tsegundos-horas*3600)/60);
        int segundos = tsegundos-(horas*3600+minutos*60);


        String _horas = "00";
        String _minutos = "00";
        String _segundos = "00";

        if(horas<10){
            _horas = "0"+horas;
        }else {
            _horas = ""+horas;
        }

        if(horas<10){
            _horas = "0"+horas;
        }else {
            _horas = ""+horas;
        }

        if(minutos<10){
            _minutos = "0"+minutos;
        }else {
            _minutos = ""+minutos;
        }

        if(segundos<10){
            _segundos = "0"+segundos;
        }else {
            _segundos = ""+segundos;
        }

        String r = _horas + ":" + _minutos + ":" + _segundos;
        Log.d("r",r);
        return r;
    }

    public static Date convertStringToDate(String dateString)
    {
        Date convertedDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  convertedDate;
    }

    public static Date getNowMinus24hs()
    {
        Calendar cal = Calendar.getInstance();
        // remove next line if you're always using the current time.
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -24);
        Date result = cal.getTime();
        return result;
    }

    public static String getUrlImageUser(int userId)
    {
        String url = Globales.URL_PHOTO_IMAGE_USER;
        url = url.replace("{USER_ID}", String.valueOf(userId));
        return url;
    }

    public static Bitmap getScaledBitmap(Bitmap bm, int maxWidth, int maxHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static  String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 94, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Boolean validationEmail(String _email){

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


    public static void openDialer(Context context, String phoneNumber)
    {
        // Use format with "tel:" and phoneNumber
        if(!TextUtils.isEmpty(phoneNumber))
        {
            Uri u = Uri.parse("tel:" + phoneNumber);
            // Create the intent and set the data for the intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try {
                // Launch the Phone app's dialer with a phone number to dial a call.
                context.startActivity(i);
            } catch (SecurityException s) {
                // show() method display the toast with exception message.
                Toast.makeText(context, s.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void saveCurrency(Context context, Currency currency){
        if(context!=null && currency!=null)
        {
            saveValueToSharedPreference(context,"currency_currency", currency.getCurrency());
            saveValueToSharedPreference(context,"currency_curCode", currency.getCurCode());
            saveValueToSharedPreference(context,"currency_symbol", currency.getSymbol());
            saveValueToSharedPreference(context,"currency_fiscal", currency.getFiscal());
            saveValueToSharedPreference(context,"currency_format", currency.getFormat());
        }
    }

    public static Currency getCurrency(Context context)
    {
        Currency result = new Currency();
        if(context!=null)
        {
            try{
                result.setCurrency(getValueFromSharedPreferences(context,"currency_currency"));
                result.setCurCode(getValueFromSharedPreferences(context,"currency_curCode"));
                result.setSymbol(getValueFromSharedPreferences(context,"currency_symbol"));
                result.setFiscal(getValueFromSharedPreferences(context,"currency_fiscal"));
                result.setFormat(getValueFromSharedPreferences(context,"currency_format"));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                result.setSymbol("$");
            }
        }
        else{
            result.setSymbol("$");
        }
        return result;
    }


    public static void saveValueToSharedPreference(Context context, String keyString, String valueString)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HttpConexion.instance, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyString, valueString);
        editor.commit();
    }

    public static String getValueFromSharedPreferences(Context context, String key){
        String result;
        SharedPreferences sharedPref = context.getSharedPreferences(HttpConexion.instance, Context.MODE_PRIVATE);
        result = sharedPref.getString(key, "");
        return result;
    }

    public static void createNotificationChannelsClient(Context context ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_6_CARRERA_ACEPTADA,
                    context.getString(R.string.notif_channel_cliente_viaje_aceptado_por_agencia_titulo),
                    context.getString(R.string.notif_channel_cliente_viaje_aceptado_por_agencia_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_7_VIAJE_ACEPTADO,
                    context.getString(R.string.notif_channel_cliente_chofer_asignado_titulo),
                    context.getString(R.string.notif_channel_cliente_chofer_asignado_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_CHOFER_ASIGNADO
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_10_CHOFER_EN_CAMINO,
                    context.getString(R.string.notif_channel_cliente_viaje_aceptado_por_chofer_titulo),
                    context.getString(R.string.notif_channel_cliente_viaje_aceptado_por_chofer_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_CHOFER
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_9_CHOFER_LLEGANDO,
                    context.getString(R.string.notif_channel_cliente_chofer_acercandose_titulo),
                    context.getString(R.string.notif_channel_cliente_chofer_acercandose_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_CHOFER_CERCA
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_8_RESERVA_CONFIRMADA,
                    context.getString(R.string.notif_channel_cliente_reserva_confirmada_titulo),
                    context.getString(R.string.notif_channel_cliente_reserva_confirmada_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_RESERVA_CONFIRMADA
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_12_VIAJE_CANCELADO_POR_CHOFER,
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_chofer_titulo),
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_chofer_descripcion),
                    Globales.NotificationChannelCustom.CLIENTE_VIAJE_CANCELADO_POR_CHOFER
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_3_CARRERA_CANCELADA,
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_agencia_titulo),
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_agencia_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO
            );
            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_4_RESERVA_CANCELADA,
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_agencia_titulo),
                    context.getString(R.string.notif_channel_cliente_viaje_cancelado_por_agencia_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO
            );

        }
    }
    public static void createNotificationChannelsDriver(Context context ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannelByNameWithPriority(context,
                    Globales.SoundNamesForNotification.SONIDO_2_NUEVA_CARRERA,
                    context.getString(R.string.notif_channel_chofer_nuevo_viaje_titulo),
                    context.getString(R.string.notif_channel_chofer_nuevo_viaje_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_NUEVO_VIAJE,
                    NotificationManager.IMPORTANCE_HIGH
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_1_NUEVA_RESERVA,
                    context.getString(R.string.notif_channel_chofer_nueva_reserva_titulo),
                    context.getString(R.string.notif_channel_chofer_nueva_reserva_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_NUEVA_RESERVA
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_4_RESERVA_CANCELADA,
                    context.getString(R.string.notif_channel_chofer_reserva_cancelado_titulo),
                    context.getString(R.string.notif_channel_chofer_reserva_cancelado_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_3_CARRERA_CANCELADA,
                    context.getString(R.string.notif_channel_chofer_viaje_cancelado_titulo),
                    context.getString(R.string.notif_channel_chofer_viaje_cancelado_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO
            );

            createNotificationChannelByName(context,
                    Globales.SoundNamesForNotification.SONIDO_5_VIAJE_FINALIZADO_Y_APROBADO_POR_LA_AGENCIA,
                    context.getString(R.string.notif_channel_chofer_viaje_finalizado_titulo),
                    context.getString(R.string.notif_channel_chofer_viaje_cancelado_descripcion),
                    Globales.NotificationChannelCustom.CHOFFER_VIAJE_ACEPTADO_POR_AGENCIA
            );

            createNotificationForChatWithPriority(context,
                    "Chat Received",
                    "Message Chat Received",
                    Globales.NotificationChannelCustom.CHOFFER_MENSAJE_CHAT_RECBIDO,
                    NotificationManager.IMPORTANCE_HIGH
            );


        }
    }


    public static void createNotificationChannelByName(Context context, String audioPath, String channelName, String channelDescription, String notificationChannelId )
    {
        createNotificationChannelByNameWithPriority(context, audioPath, channelName, channelDescription, notificationChannelId, NotificationManager.IMPORTANCE_HIGH);
    }

    public static void createNotificationChannelByNameWithPriority(Context context, String audioPath, String channelName, String channelDescription, String notificationChannelId, int notifManagerImportance)
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(audioPath));
            int importance = notifManagerImportance;

            NotificationChannel channel = new NotificationChannel(notificationChannelId, channelName, importance);
            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.enableVibration(true);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            channel.setSound(soundUri, attributes);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public static void createNotificationForChatWithPriority(Context context, String channelName, String channelDescription, String notificationChannelId, int notifManagerImportance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = notifManagerImportance;

            NotificationChannel channel = new NotificationChannel(notificationChannelId, channelName, importance);
            channel.setDescription(channelDescription);
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }



        public static boolean hasAnyReservationWithountConfirm(List<InfoTravelEntity> listsReservations)
    {
        boolean result=false;
        for (InfoTravelEntity entity:listsReservations) {
            int status = entity.getIsAceptReservationByDriver();
            if(status==0)
            {
                result=true;
                break;
            }
        }
        return result;
    }

    public static void saveParamsToLocalPreferences(Context context, List<paramEntity> params)
    {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(HttpConexion.instance, 0); // 0 - for private mode
        editor = pref.edit();
        Gson gson = new Gson();
        editor.putString("param", gson.toJson(params));
        editor.apply(); // commit changes
    }

    public static List<paramEntity> getParamsFromLocalPreferences(Context context)
    {
        List<paramEntity> result=null;
        try
        {
            SharedPreferences sharedPref = context.getSharedPreferences(HttpConexion.instance, Context.MODE_PRIVATE);
            String preferencesString = sharedPref.getString("param", "");
            if(!TextUtils.isEmpty(preferencesString))
            {
                Type listType = new TypeToken<ArrayList<paramEntity>>(){}.getType();
                result = new Gson().fromJson(preferencesString, listType);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            result=null;
        }
        return result;
    }


    public static double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public static double getDistanceBetweenTwoPoints(LatLng latLng1, LatLng latLng2) {
        try
        {
            Location locationA=new Location("point1");
            locationA.setLatitude(latLng1.latitude);
            locationA.setLongitude(latLng1.longitude);

            Location locationB=new Location("point2");
            locationB.setLatitude(latLng2.latitude);
            locationB.setLongitude(latLng2.longitude);

            float distance = locationA.distanceTo(locationB);
            return distance;
        }
        catch (Exception ex){
            return -1;
        }
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static int pxFromDp(final Context context, final float dp) {
        int px =0;
        try
        {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if(windowManager!=null)
            {
                DisplayMetrics metrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(metrics);
                float logicalDensity = metrics.density;
                px = (int) Math.ceil(dp * logicalDensity);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return px;
    }

    public static void getDirectionBetweenTwoPointsApi(LatLng origin,  LatLng destination , String serverKey, final DirectionApiCallback callback) {

        try {
            //String serverKey = Globales.SERVER_KEY;
            //String serverKey = Globales.SERVER_KEY;
            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .unit(Unit.METRIC)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if(direction.isOK()){
                                if(direction.getRouteList().size()>0 && direction.getRouteList().get(0).getLegList().size()>0) {
                                    callback.onDirectionSuccess(direction);
                                }
                                else{
                                    callback.onDirectionError();
                                }
                            }
                            else{
                                callback.onDirectionError();
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                            Log.e("MAP_ERROR", t.getMessage());
                            callback.onDirectionError();
                        }
                    });
        }
        catch (Exception ex){}
    }

    public static PriceAndIsMinimum getMontoViajeParticular(int param78_BajadaDeBandera, double montoMinimoDeViaje,
                                                            InfoTravelEntity currentTravel, double kilometros_ida, double kilometros_vuelta,
                                                            double  kilometros_total, double priceByKm,
                                                            double PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA,
                                                            float PARAM_7_amountPercent, String PARAM_8_from, String PARAM_9_to)
    {
        PriceAndIsMinimum result = new PriceAndIsMinimum();
        double priceByKmFinal = getPriceByKmWithChargeIfNeeded(priceByKm,PARAM_7_amountPercent, PARAM_8_from, PARAM_9_to);
        switch (param78_BajadaDeBandera) {
            case Globales.Param78BajadaDeBandera.SOLO_LISTA_BENEFICIOS_X_KM_0:
                if(currentTravel!=null) {
                    //Le paso el precio x km normal, esto es así porque si le mando el precio por km con el aumento x horario, luego, lo va a calcular de nuevo.
                    //Solo usa el precio x km si no matchea con ningún ítem de la lista de beneficios
                    result.amount = getMontoViajeParticularesPorBeneficios(currentTravel, kilometros_ida, kilometros_vuelta, kilometros_total, priceByKm, PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA, montoMinimoDeViaje);
                    result.amount = result.amount + getMontoExtraParticular(PARAM_8_from, PARAM_9_to, result.amount, PARAM_7_amountPercent);
                }
                else{
                    result.amount = kilometros_ida * priceByKmFinal;
                    result.amount = result.amount + getMontoExtraParticular(PARAM_8_from, PARAM_9_to, result.amount, PARAM_7_amountPercent);
                    result.isMinimum = result.amount <= montoMinimoDeViaje;
                    result.amount = result.amount > montoMinimoDeViaje ? result.amount : montoMinimoDeViaje;
                }
                break;
            case Globales.Param78BajadaDeBandera.SUMA_VALOR_MINIMO_O_RECORRIDO_CHOFER_1:
                result.amount = kilometros_ida * priceByKmFinal + montoMinimoDeViaje;
                break;
            case Globales.Param78BajadaDeBandera.RECORRIDO_SUPERA_MINIMO_COBRA_VIAJE_REAL_2:
                result.amount = kilometros_ida * priceByKmFinal;
                result.isMinimum = result.amount <= montoMinimoDeViaje;
                result.amount = result.amount > montoMinimoDeViaje ? result.amount : montoMinimoDeViaje;
                break;
        }
        return result;


    }

    private static double getPriceByKmWithChargeIfNeeded(double priceByKm, float amountPercent, String from, String to)
    {
        double result = priceByKm;
        try{
            int horaFrom = parseInt(from.split(":")[0]);
            int minutoFrom = parseInt(from.split(":")[1]);

            int horaTo = parseInt(to.split(":")[0]);
            int minutoTo = parseInt(to.split(":")[1]);
            Date dateFrom = getTodayWithSpecificHourAndSeconds(horaFrom, minutoFrom);
            Date dateTo = getTodayWithSpecificHourAndSeconds(horaTo, minutoTo);
            Date dateNow = new Date();
            if(dateNow.after(dateFrom) && dateNow.before(dateTo))
            {
                result = result+result*amountPercent/100;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private static Date getTodayWithSpecificHourAndSeconds(int hours, int minutes)
    {
        Date result=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(result);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static PriceAndIsMinimum getMontoViajeEmpresa(VehicleType vehicleType,
                                              double _priceDistanceCompany, double _priceReturn, double _priceHour, double _kMex,
                                              int _isTravelByHour,int _hour_init_travel, double kilometros_total, int _isBenefitKmList,
                                              List<BeneficioEntity> _listBeneficios,
                                              double kilometros_ida, double kilometros_vuelta, int isTravelCompany,
                                              double PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA,
                                             double amountOriginPac, double distanceOriginPacInKm,
                                                         String priceMinEmpresa, String travelMinMode,
                                                         CompanyData companyData
                                              )
    {
        PriceAndIsMinimum result=new PriceAndIsMinimum();

        double pricePerKm =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceKm())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceKm()):_priceDistanceCompany;
        double pricePerKmReturn =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceKmDR())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceKmDR()):_priceReturn;
        double priceHour =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceHour())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceHour()):_priceHour;
        double pricePerKmExedente =vehicleType!=null && Utils.parseDouble(vehicleType.getPricePerKmEx())>0.1d ?Utils.parseDouble(vehicleType.getPricePerKmEx()):_priceDistanceCompany;
        double kmExcedente =vehicleType!=null && Utils.parseDouble(vehicleType.getKmEx())>0.1d ?Utils.parseDouble(vehicleType.getKmEx()):_kMex;
        double priceVehicleMin = vehicleType!=null && Utils.parseDouble(vehicleType.getVehicleValorMin())>0.1d ? Utils.parseDouble(vehicleType.getVehicleValorMin()):0d;
        double priceVehicleMinFinal = Utils.parseDouble(priceMinEmpresa)> 0 ? Utils.parseDouble(priceMinEmpresa): priceVehicleMin;



        kilometros_ida= kilometros_ida + distanceOriginPacInKm;
        kilometros_total= kilometros_total+distanceOriginPacInKm;


        /*VERIFICAMOS SI ES VIAJE POR HORA*/
        if(_isTravelByHour == 1) {
            int hours = new Time(System.currentTimeMillis()).getHours();
            int newHours = hours - _hour_init_travel;

            if(newHours <1 || _hour_init_travel == 0) {
                newHours = 1;
            }

            if(kmExcedente >0 && kilometros_total > kmExcedente){ // KILOMETROS EXEDENTES
                double kilometros_extra = kmExcedente - kilometros_total;
                result.amount = (newHours * priceHour) +  (kilometros_extra * pricePerKmExedente);
            }else {
                result.amount = newHours * priceHour;
            }
        }
        else{
            /*VERIFICAMOS SI ESTA ACTIVO EL CAMPO BENEFICIO POR KILOMETRO PARA ESA EMPRESA*/
            Log.d("-TRAVEL Beneficio-", "tiene beneficios? : "+String.valueOf("0".equals(travelMinMode)));

            List<BeneficioEntity> listaBeneficiosFinal =  vehicleType!=null && vehicleType.getListbenefitKm() != null && vehicleType.getListbenefitKm().size()>0? vehicleType.getListbenefitKm() : _listBeneficios;
            if (Globales.ID_LISTA_DE_BENEFICIOS_X_KM_EN_EMPRESA.equals(travelMinMode) //0: Lista de beneficios x KM
                    && listaBeneficiosFinal != null
                    && listaBeneficiosFinal.size() > 0) {

                Log.d("-TRAVEL_km_total-", String.valueOf(kilometros_total));

                /* VERIFICAMOS SI ESTA DENTRO DE EL RANGO DE EL BENEFICIO ESTABLECIDO */
                result.amount = getPriceBylistBeneficion(listaBeneficiosFinal, kilometros_ida, pricePerKm);

                // VERIFICAMOS SI HAY RETORNO //
                if (HomeChofer.isRoundTrip == 1) {
                    result.amount = result.amount + getPriceReturnBylistBeneficion(listaBeneficiosFinal , kilometros_vuelta, isTravelCompany, pricePerKm, PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA);
                }

                if(result.amount < 1){
                    result.amount = kilometros_total * pricePerKm;
                    Log.d("-TRAVEL result (2)-", String.valueOf(result));
                }

                //Le sumamos el precio extra x horario
                result.amount = result.amount + getMontoExtraEmpresa(companyData,result.amount);
            } else {
                result.amount = kilometros_total * pricePerKm;// PARA CLIENTES EMPREA BUSCAMOS EL PRECIO DE ESA EMPRESA

                Log.d("-TRAVEL result (3)-", String.valueOf(result));

                if (HomeChofer.isRoundTrip == 1) {
                    Log.d("-TRAVEL isRoundTrip -", String.valueOf(HomeChofer.isRoundTrip));
                    result.amount = kilometros_ida * pricePerKm;
                    Log.d("-TRAVEL result (4)-", String.valueOf(result));
                    result.amount = result.amount + kilometros_vuelta * pricePerKmReturn;
                    Log.d("-TRAVEL result (5)-", String.valueOf(result));
                }
                result.amount=result.amount + amountOriginPac;
                result.isMinimum = result.amount <= priceVehicleMinFinal;
                result.amount = getMinPriceViajeEmpresa(priceVehicleMinFinal, result.amount, travelMinMode);
                result.amount = result.amount + getMontoExtraEmpresa(companyData,result.amount);
            }
            result.amountOriginPac = amountOriginPac !=0d ? amountOriginPac : distanceOriginPacInKm * pricePerKm;
        }
        return result;
    }

    private static double getMontoExtraEmpresa(CompanyData companyData, double finalPrice)
    {
        double result = 0;
        try{
            if(companyData!=null)
            {
                Date dateToday = new Date();
                DateTime horaDesdeExtra = new DateTime(dateToday)
                        .withHourOfDay(Integer.parseInt(companyData.getCompanyParams().getExtraTmStart().split(":")[0]))
                        .withMinuteOfHour(Integer.parseInt(companyData.getCompanyParams().getExtraTmStart().split(":")[1]))
                        .withSecondOfMinute(Integer.parseInt(companyData.getCompanyParams().getExtraTmStart().split(":")[2]))
                        .withMillisOfSecond(0);


                DateTime horaHastaExtra = new DateTime(dateToday)
                        .withHourOfDay(Integer.parseInt(companyData.getCompanyParams().getExtraTmEnd().split(":")[0]))
                        .withMinuteOfHour(Integer.parseInt(companyData.getCompanyParams().getExtraTmEnd().split(":")[1]))
                        .withSecondOfMinute(Integer.parseInt(companyData.getCompanyParams().getExtraTmEnd().split(":")[2]))
                        .withMillisOfSecond(0);


                int dateDifference=DateTimeComparator.getInstance().compare(horaDesdeExtra, horaHastaExtra);
                int dateFromDifferenceWithNow,dateToDifferenceWithNow;
                dateFromDifferenceWithNow = DateTimeComparator.getInstance().compare(dateToday, horaDesdeExtra);
                dateToDifferenceWithNow = DateTimeComparator.getInstance().compare(dateToday, horaHastaExtra);
                if(dateDifference < 0)
                {
                    if(dateFromDifferenceWithNow>0 && dateToDifferenceWithNow<0)
                    {
                        result=getExtraPrice(companyData, finalPrice);
                    }
                }else if(dateDifference>0) {
                    if (dateFromDifferenceWithNow > 0 || dateToDifferenceWithNow < 0) {
                        result=getExtraPrice(companyData, finalPrice);
                    }
                }

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private static double getExtraPrice(CompanyData companyData, double ammount)
    {
        double result=0d;
        try{
            if("1".equals(companyData.getCompanyParams().getExtraTmFixed()))
            {
                result = parseDouble(companyData.getCompanyParams().getExtraTmAmount());
            } else if("0".equals(companyData.getCompanyParams().getExtraTmFixed()))
            {
                result = parseDouble(companyData.getCompanyParams().getExtraTmAmount())*ammount/100;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    /*******************************************************************************************/
    private static double getMontoExtraParticular(String horarioInicial, String horarioFinal , double finalPrice, float porcentajeDeAumento)
    {
        double result = 0;
        try{
            if(!TextUtils.isEmpty(horarioInicial))
            {
                Date dateToday = new Date();
                DateTime horaDesdeExtra = new DateTime(dateToday)
                        .withHourOfDay(Integer.parseInt(horarioInicial.split(":")[0]))
                        .withMinuteOfHour(Integer.parseInt(horarioInicial.split(":")[1]))
                        .withSecondOfMinute(0)
                        .withMillisOfSecond(0);


                DateTime horaHastaExtra = new DateTime(dateToday)
                        .withHourOfDay(Integer.parseInt(horarioFinal.split(":")[0]))
                        .withMinuteOfHour(Integer.parseInt(horarioFinal.split(":")[1]))
                        .withSecondOfMinute(0)
                        .withMillisOfSecond(0);


                int dateDifference=DateTimeComparator.getInstance().compare(horaDesdeExtra, horaHastaExtra);
                int dateFromDifferenceWithNow,dateToDifferenceWithNow;
                dateFromDifferenceWithNow = DateTimeComparator.getInstance().compare(dateToday, horaDesdeExtra);
                dateToDifferenceWithNow = DateTimeComparator.getInstance().compare(dateToday, horaHastaExtra);
                if(dateDifference < 0)
                {
                    if(dateFromDifferenceWithNow>0 && dateToDifferenceWithNow<0)
                    {
                        result=getExtraPriceParticular(porcentajeDeAumento, finalPrice);
                    }
                }else if(dateDifference>0) {
                    if (dateFromDifferenceWithNow > 0 || dateToDifferenceWithNow < 0) {
                        result=getExtraPriceParticular(porcentajeDeAumento, finalPrice);
                    }
                }

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private static double getExtraPriceParticular(float porcentaje, double ammount)
    {
        double result=0d;
        try {
            result = porcentaje * ammount / 100;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    /******************************************************************************************/

    private static double getMinPriceViajeEmpresa(double minPrice, double amount, String travelMinMode)
    {
        double result = amount;
        try{
            switch (travelMinMode)
            {
                case "0"://TODO: Debo agregar beneficios x km
                    result=minPrice+amount;
                    break;
                case "1":
                    result=minPrice+amount;
                    break;
                case "2":
                    result=amount > minPrice? amount : minPrice;
                    break;
                default:
                    result=amount;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    private static double getMontoViajeParticularesPorBeneficios( InfoTravelEntity currentTravel, double kilometros_ida, double kilometros_vuelta, double kilometros_total, double PARAM_1_PRECIO_LISTA_X_KM, double PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA, double param16_montoMinimoDeViaje )
    {
        double result=0d;
        try {
            if (currentTravel.getListBeneficio() != null && currentTravel.getListBeneficio().size() > 0) {
                result = getPriceBylistBeneficion(currentTravel.getListBeneficio(), kilometros_ida, PARAM_1_PRECIO_LISTA_X_KM);
                if (currentTravel.getIsRoundTrip() == 1) {
                    result = result + getPriceReturnBylistBeneficion(currentTravel.getListBeneficio(), kilometros_vuelta, currentTravel.getIsTravelComany(), currentTravel.priceDitanceCompany , PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA);
                }
                if (result < 1) {
                    result = kilometros_total * PARAM_1_PRECIO_LISTA_X_KM;
                }
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private static double getPriceReturnBylistBeneficion(List<BeneficioEntity> list, double distance,  int isTravelCompany, double priceDistanceCompany , double PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA){
        double value = 0;

        int indexBrack = 0;
        for (int i=0;i < list.size();i++){
            indexBrack++;
            // EVALUAMOS LOS DISTINTOS BENEFICIOS //
            Log.d("b-distance","value=  "+list.get(i).BenefitsToKm);
            if (distance >= Double.parseDouble(list.get(i).BenefitsFromKm) && distance <= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitPreceReturnKm());
            }
        }

        if(value==0 && list.size()>0) {
            value = Double.parseDouble(list.get(indexBrack-1).getBenefitPreceReturnKm());
        }

        // VALIDAMOS SI EL TOTAL KM ES MAYOR A //
        if(distance > Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm())){
            double distanceExtraBeneficio = distance - Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm());
            double amountExtra = 0;
            // ES VIAJE A EMPRESA //
            if(isTravelCompany == 1){// EMPRESA
                amountExtra = distanceExtraBeneficio * priceDistanceCompany;
            }else{
                amountExtra = distanceExtraBeneficio * PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }

    private static double getPriceBylistBeneficion(List<BeneficioEntity> list,double distance, double PARAM_1_PRECIO_LISTA_X_KM){
        double value = 0;

        int indexBrack = 0;
        for (int i=0;i < list.size();i++){
            indexBrack++;
            // EVALUAMOS LOS DISTINTOS BENEFICIOS //
            if (distance >= Double.parseDouble(list.get(i).BenefitsFromKm) && distance <= Double.parseDouble(list.get(i).BenefitsToKm)){
                value  = value + Double.parseDouble(list.get(i).getBenefitsPreceKm());
            }
        }

        if(value==0 && list.size()>0) {
            value = distance * PARAM_1_PRECIO_LISTA_X_KM;
            //value = Double.parseDouble(list.get(indexBrack-1).getBenefitsPreceKm());
        }

        // VALIDAMOS SI EL TOTAL KM ES MAYOR A //
        /*if(distance > Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm())){
            double distanceExtraBeneficio = distance - Double.parseDouble(list.get(indexBrack-1).getBenefitsToKm());
            double amountExtra = 0;
            // ES VIAJE A EMPRESA //
            if(currentTravel.getIsTravelComany() == 1){// EMPRESA
                amountExtra = distanceExtraBeneficio * currentTravel.getPriceDitanceCompany();
            }else{
                amountExtra = distanceExtraBeneficio * PARAM_1_PRECIO_LISTA_X_KM;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }*/

        return value;
    }



    private static double getPriceBylistBeneficion_original(List<BeneficioEntity> list,double distance,  InfoTravelEntity currentTravel, double PARAM_1_PRECIO_LISTA_X_KM){
        double value = 0;

        int indexBrack = 0;
        for (int i=0;i < list.size();i++){
            indexBrack++;
            // EVALUAMOS LOS DISTINTOS BENEFICIOS //

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
                amountExtra = distanceExtraBeneficio * PARAM_1_PRECIO_LISTA_X_KM;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }
    private static double getPriceReturnBylistBeneficion_original(List<BeneficioEntity> list, double distance,  InfoTravelEntity currentTravel, double PARAM_1_PRECIO_LISTA_X_KM, double PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA){
        double value = 0;

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
                amountExtra = distanceExtraBeneficio * PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }

    public static boolean isShowPrice(int param26_mostrarPrecio, boolean isTravelCompany)
    {
        boolean result=false;
        switch (param26_mostrarPrecio){
            case 1:
                result = isTravelCompany;
                break;
            case 2:
                result =!isTravelCompany;
                break;
            case 3:
                result=true;
        }
        return result;
    }

    public static boolean isCliente(int userProfile)
    {
        return userProfile == Globales.ProfileId.CLIENT_PARTICULAR
                || userProfile == Globales.ProfileId.CLIENT_COMPANY;
    }

    public static boolean isDriver(int userProfile)
    {
        return userProfile == Globales.ProfileId.DRIVER;
    }

    public static boolean isClienteCompany(int userProfile)
    {
        return userProfile==Globales.ProfileId.CLIENT_COMPANY;
    }

    public static boolean isClienteParticular(int userProfile)
    {
        return userProfile==Globales.ProfileId.CLIENT_PARTICULAR;
    }

    public static int parseInt(String value)
    {
        int result=0;
        try{
            result = Integer.parseInt(value);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static double parseDouble(String value)
    {
        double result=0;
        try{
            result = Double.parseDouble(value);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String getPriceWithMoneySymbol(Context context, String price){
        String result="";
        try{
            Currency currecy =  getCurrency(context);
            result = currecy.getSymbol().concat(" ").concat(price);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  result;
    }

    public static String convertDoubleToStringPrice(Double value)
    {
        String result="0.00";
        try
        {
            result = String.format(Locale.US, "%.2f", value);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static NewDriverOptionalValueData getOptionalDataValidation(Context context) {
        NewDriverOptionalValueData result = new NewDriverOptionalValueData();
        try
        {
            List<paramEntity> params = Utils.getParamsFromLocalPreferences(context);
            if (params != null) {
                paramEntity param94_datosOpcionales = params.get(Globales.ParametrosDeApp.PARAM_94_REGISTRAR_CHOFER_CAMPOS_OPCIONALES);
                Gson gson = new Gson();
                result = gson.fromJson(param94_datosOpcionales.getValue(), NewDriverOptionalValueData.class);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            result = new NewDriverOptionalValueData();
        }
        return result;
    }
    public static void hideKeyboard(Context context, final View view)
    {
        try{
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void showKeyboard(Context context, final View view)
    {
        try{
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static String filterStreetAndNumberInAddress(String address)
    {
        String result=address;
        try
        {
            if(!TextUtils.isEmpty(address))
            {
                if(address.contains(",")){
                    result = address.substring(0,address.indexOf(",")-1);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean isDeveloperInstance(){
        return "developer".equals(HttpConexion.instance) || "testing".equals(HttpConexion.instance) || BuildConfig.DEBUG;
    }

    public static boolean isInstanceDisabled(){
        return "MotoTaxiBolivia".equals(HttpConexion.instance);
    }

    public static boolean isPlaceToPayTesting()
    {
        return isDeveloperInstance();
    }

    public static boolean isMercadoPagoConfigured(List<paramEntity> listParam){
        boolean result=false;
        try{
            if(Globales.PaymentCardProviders.MERCADO_PAGO.equals(listParam.get(Globales.ParametrosDeApp.PARAM_103_PAYMENT_CARD_PROVIDER).getValue()) &&
                    !TextUtils.isEmpty(listParam.get(Globales.ParametrosDeApp.PARAM_69_KEY_MP).getValue()) &&
                    !TextUtils.isEmpty(listParam.get(Globales.ParametrosDeApp.PARAM_79_MP_SECRET_KEY).getValue())){
                result=true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            result=false;
        }
        return result;
    }


    public static boolean isPlaceToPayConfigured(List<paramEntity> listParam){
        boolean result=false;
        try{
            if(Globales.PaymentCardProviders.PLACE_TO_PAY.equals(listParam.get(Globales.ParametrosDeApp.PARAM_103_PAYMENT_CARD_PROVIDER).getValue()) &&
                    !TextUtils.isEmpty(listParam.get(Globales.ParametrosDeApp.PARAM_69_KEY_MP).getValue()) &&
                    !TextUtils.isEmpty(listParam.get(Globales.ParametrosDeApp.PARAM_79_MP_SECRET_KEY).getValue())){
                result=true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            result=false;
        }
        return result;
    }

    public static boolean isCardAcepted(List<paramEntity> listParams){
        boolean result=false;
        try{
            int PARAM_68_PAGA_CON_TARJETA = Integer.parseInt(listParams.get(Globales.ParametrosDeApp.PARAM_68_PAGA_CON_TARJETA).getValue());// SE PAGAR CON TARJETA
            result = PARAM_68_PAGA_CON_TARJETA==1 && (isMercadoPagoConfigured(listParams) || isPlaceToPayConfigured(listParams));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean isCardEnabled(List<paramEntity> listParams){
        boolean result=false;
        try{
            int PARAM_68_PAGA_CON_TARJETA = Integer.parseInt(listParams.get(Globales.ParametrosDeApp.PARAM_68_PAGA_CON_TARJETA).getValue());// SE PAGAR CON TARJETA
            result = PARAM_68_PAGA_CON_TARJETA==1;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean isCardAceptedByCilent(List<paramEntity> listParams){
        boolean result=false;
        try{
            int PARAM_101_PAGA_CON_TARJETA_CLIENTE = Integer.parseInt(listParams.get(Globales.ParametrosDeApp.PARAM_101_ACCEPT_PAYMENT_CARD_IN_CLIENT).getValue());// SE PAGAR CON TARJETA EN EL CLIENTE
            result = PARAM_101_PAGA_CON_TARJETA_CLIENTE==1 && (isMercadoPagoConfigured(listParams) || isPlaceToPayConfigured(listParams));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static String getStatusPaymentFromPlaceToPayStatus(String status, Context context)
    {
        String result="";
        try{
            if(!TextUtils.isEmpty(status))
            {
                switch (status)
                {
                    case PlaceToPayGlobales.StatusPlaceToPay.OK:
                        result=context.getString(R.string.ok);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.FAILED:
                        result=context.getString(R.string.place_to_pay_status_failed);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.APPROVED:
                        result=context.getString(R.string.pagado);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.APPROVED_PARTIAL:
                        result=context.getString(R.string.place_to_pay_status_aproved);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.PARTIAL_EXPIRED:
                        result=context.getString(R.string.place_to_pay_status_expired);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.REJECTED:
                        result=context.getString(R.string.place_to_pay_status_rejected);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.PENDING:
                        result=context.getString(R.string.place_to_pay_status_pending);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.PENDING_VALIDATION:
                        result=context.getString(R.string.place_to_pay_status_pending);
                        break;
                    case PlaceToPayGlobales.StatusPlaceToPay.REFUNDED:
                        result=context.getString(R.string.place_to_pay_status_refunded);
                        break;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean validateName(String name)
    {
        boolean result=false;
        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        result = matcher.find() && !name.contains(".");
        return result;
    }


    public static boolean isForegroundActivity(Context context, String nameActivity) {
        boolean result=false;
        try{
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);
            for (ActivityManager.RunningTaskInfo aTask : alltasks) {
                String name = aTask.topActivity.getClassName();
                Log.w("ACTIVITY", name);
                String[] parts = name.split("\\.");
                if(parts!=null && parts.length>0)
                {
                    name = parts[parts.length-1];

                    if(nameActivity.equals(name)){
                        result=true;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    public static boolean isForegroundApp(Context context, String nameActivity) {
        boolean result=false;
        try{
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> alltasks = am.getRunningAppProcesses();
            boolean isPrimeraVez=true;
            for (ActivityManager.RunningAppProcessInfo aTask : alltasks) {
                if(isPrimeraVez)
                {
                    isPrimeraVez=false;
                    if(aTask.importance== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    {
                       result=true;
                    }
                    else{
                        result=false;
                    }
                    /*isPrimeraVez=false;
                    String name = aTask.processName;
                    Log.w("ACTIVITY", name);
                    String[] parts = name.split("\\.");
                    if(parts!=null && parts.length>0)
                    {
                        name = parts[parts.length-1];

                        if(nameActivity.equals(name)){
                            result=true;
                        }
                    }*/
                }

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    public static String getTimeInTravelByTravelByHour(Context context)
    {
        String result="";
        try{
            Date timeTo = Calendar.getInstance().getTime();
            String timeFrom =  getValueFromSharedPreferences(context,Globales.KEY_FOR_INIT_TIME_TRAVEL_BY_HOUR);
            if(!TextUtils.isEmpty(timeFrom))
            {
                String endSavedTime = Utils.getValueFromSharedPreferences(context, Globales.KEY_FOR_END_TIME_TRAVEL_BY_HOUR);
                if(!TextUtils.isEmpty(endSavedTime))
                {
                    timeTo = new Date(Long.parseLong(endSavedTime));
                }

                long timeFromLong  = Long.parseLong(timeFrom);
                Date timeInitTravel = new Date(timeFromLong);
                long difference = timeTo.getTime() - timeInitTravel.getTime();
                int seconds = (int)difference/1000;
                result =  covertSecondsToHHMMSS(seconds);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static Long convertHHmmssToLongTime(String time)
    {
        Long result=0l;
        try
        {
            if(!TextUtils.isEmpty(time))
            {
                String[] timeParts = time.split(":");
                Long hours = Long.parseLong(timeParts[0])*1000*60*60;
                Long minutes = Long.parseLong(timeParts[1])*1000*60;
                Long seconds = Long.parseLong(timeParts[2])*1000;
                result = hours + minutes + seconds;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static void resetDurationForTravelByHour(Context context)
    {
        try
        {
            Utils.saveValueToSharedPreference(context,Globales.KEY_FOR_END_TIME_TRAVEL_BY_HOUR,"");
            Utils.saveValueToSharedPreference(context,Globales.KEY_FOR_INIT_TIME_TRAVEL_BY_HOUR,"");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static int convertHHmmssToSeconds(String time)
    {
        int result=0;
        try
        {
            if(!TextUtils.isEmpty(time))
            {
                String[] timeParts = time.split(":");
                int  hours = Integer.parseInt(timeParts[0])*60*60;
                int  minutes = Integer.parseInt(timeParts[1])*60;
                int  seconds = Integer.parseInt(timeParts[2]);
                result = hours + minutes + seconds;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static float getValueFloatFromParameters(List<paramEntity> params, int paramNumber)
    {
        float result=0f;
        try{
            result = Float.parseFloat(params.get(paramNumber).getValue());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getValueIntFromParameters(List<paramEntity> params, int paramNumber)
    {
        int result=0;
        try{
            result = Integer.parseInt(params.get(paramNumber).getValue());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }



    public static String getUserCountry(Context context) {
        try {
            if(Utils.isDeveloperInstance())
            {
                return "";
            }

            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
            else if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
        }
        catch (Exception e) { }
        return "";
    }

    public static double getExtraTime(double PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA, int PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES, int tiempoTxt, InfoTravelEntity currentTravel){
        double result=0d;
        double hor;
        double min = 0;
        hor=min/3600;
        min=(tiempoTxt-(3600*hor))/60;//  BUSCAMOS SI REALIZO ESPERA

        Log.d("-TRAVEL min espera -", String.valueOf(min));
        if(currentTravel.getIsTravelComany() == 1)// EMPRESA
        {
            if(min > currentTravel.getMinSlepNoAply()) {
                if(min-currentTravel.getMinSlepNoAply()>0.1)
                {
                    result = (min-currentTravel.getMinSlepNoAply()) * currentTravel.getPriceMinSleepCompany();
                }
                else{
                    result = 1 * currentTravel.getPriceMinSleepCompany();
                }
            }
        }
        else // PARTICULARES
        {
            if(min > PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES) {
                if(min-PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES>0.1)
                {
                    result = (min-PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES) * PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA;
                }
                else{
                    result = 1 * PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA;
                }
            }
        }

        return result;
    }

    public static void sendIsDriverToService(Activity activity, String isDriverActive)
    {
        try
        {
            Intent in = new Intent();
            in.putExtra("isDriverActive", isDriverActive);
            in.putExtra("isDriverPaused", "false".equals(isDriverActive) ? "true" : "false");
            in.setAction(SocketServices.IS_DRIVER_ACTIVE_RECEIVER_TAG);
            activity.sendBroadcast(in);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void sendChatMessageToService(Activity activity, String message, String userWeb)
    {
        try
        {
            Intent in = new Intent();
            in.putExtra("message", message);
            in.putExtra("userWeb", userWeb);

            in.setAction(SocketServices.CHAT_RECEIVER_TAG);
            activity.sendBroadcast(in);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String getDDMMYYYY_hhmmss(Date date){
        try{
            return android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:ss", date).toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static ChatMessageReceive getChatMessageFromJson(JSONObject obj){
        ChatMessageReceive result;
        try{
            result = new ChatMessageReceive(obj.getJSONObject("message"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            result = new ChatMessageReceive();
        }
        return result;
    }
}