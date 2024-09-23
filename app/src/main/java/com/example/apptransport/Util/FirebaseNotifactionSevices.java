package com.example.apptransport.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import com.apreciasoft.mobile.asremis.Activity.HomeChofer;

import com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle;
import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntityLite;
import com.apreciasoft.mobile.asremis.Entity.TravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.core.services.LayoutOverlayForNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Random;

/**
 * Created by jorge gutierrez on 13/02/2017.
 */

public class FirebaseNotifactionSevices extends FirebaseMessagingService {

    public static final String TAG = "NotifactionSevices:";
    public static final String DIRVER_IS_CLOSE ="1";
    public GlovalVar gloval;
    public Uri soundUri;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        try {

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Notificación: " + remoteMessage.getNotification().getBody());

            }

            if (remoteMessage.getData().size() > 0) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                gloval = ((GlovalVar) getApplicationContext());

                Log.d(TAG,gson.toJson(remoteMessage.getData()));
                gloval.setGv_travel_current_lite(gson.fromJson(gson.toJson(remoteMessage.getData()), InfoTravelEntityLite.class));

                String isDriverClose = gloval.getGv_travel_current_lite()!=null && gloval.getGv_travel_current_lite().getIsDriverClose()!=null && !"".equals(gloval.getGv_travel_current_lite().getIsDriverClose()) ? gloval.getGv_travel_current_lite().getIsDriverClose(): "0";

                String notifBody = gloval.getGv_travel_current_lite()!=null ? gloval.getGv_travel_current_lite().getNameDestination() : (remoteMessage.getNotification()!=null ? remoteMessage.getNotification().getBody(): "");
                notifBody=notifBody != null ? notifBody : getString(R.string.tiene_una_nueva_notificacion);

                Intent intent = new Intent("update-message");
                if(DIRVER_IS_CLOSE.equals(isDriverClose))
                {
                    intent.putExtra("is_close_driver", "true");
                }

                intent.putExtra("message", "CANGUE INFO FIREBASE (ASREMIS)");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                sendNotificationToOnTopServiceForDriver(gloval.getGv_travel_current_lite());

                if (gloval.getGv_id_profile() == 2 || gloval.getGv_id_profile() == 5) {
                    Log.d(TAG, String.valueOf("YES CLIENT (2,5)"));
                    //Si la app está en primer plano, si es cliente, y es notificación de que el chofer está cerca, muestro una notificación
                    //De lo contrario, no muestro nada, ya que en la pantalla de la app, ya se muestra el cartel correspondiente.
                    //De todas maneras, antes no se mostraba nunca, ya que había un error e iba al Catch.
                    mostrarNotificacion(notifBody, HomeClienteNewStyle.class, true, DIRVER_IS_CLOSE.equals(isDriverClose));

                } else {
                    Log.d(TAG, String.valueOf("YES DRIVER"));
                    if (gloval.getGv_travel_current_lite().getIdSatatusTravel() != Globales.StatusTravel.VIAJE_FINALIZADO) {
                        mostrarNotificacion(notifBody, HomeChofer.class, false, false);
                    }
                }

            }else{
                Intent intent = new Intent("update-message");
                // intent.putExtra("message", gson.fromJson(gson.toJson(remoteMessage.getData()), InfoTravelEntity.class));
                intent.putExtra("message", "CARGUE INFO FIREBASE (ASREMIS)");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                if (gloval.getGv_id_profile() == 2 || gloval.getGv_id_profile() == 5) {
                    Log.d(TAG, String.valueOf("YESS 2,5"));
                    mostrarNotificacion(remoteMessage.getNotification().getBody(), HomeClienteNewStyle.class, true,false);
                } else {
                    Log.d(TAG, String.valueOf("YESS DRIVER"));
                    mostrarNotificacion(remoteMessage.getNotification().getBody(), HomeChofer.class, false, false);
                }
            }
        }catch (Exception e){
            Log.d("ERROR" , e.getMessage());
        }

    }

    private void mostrarNotificacion(String body,Class<?> cls,boolean isClient, boolean isDriverClose) {
        Context context = this;

        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder;

        String notificationChannelId = getNofiticacionChannelId(isClient && isDriverClose, isClient);
        soundUri = getSoundNameByChannelId(context, notificationChannelId);
        String notifTitle = getNotificationTitleByChannelId(context, notificationChannelId);
        String notifbody = body;//getMessage();

        mBuilder = new NotificationCompat.Builder(this, notificationChannelId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (!"".equals(notificationChannelId)) {
                mBuilder.setSound(soundUri);
            }
            else{
                mBuilder.setSound(null);
            }
        }

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(notifTitle);
        mBuilder.setContentText(notifbody);

        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //startForeground(new Random().nextInt(), mBuilder.build());

        mNotificationManager.notify(new Random().nextInt(), mBuilder.build());

    }



    private String getNofiticacionChannelId(boolean isDriverClose, boolean isClient)
    {
        String result="";
        if(isDriverClose)
        {
            result = Globales.NotificationChannelCustom.CLIENTE_CHOFER_CERCA;
        }
        else {

            if (gloval.getGv_travel_current_lite() != null) {
                switch (gloval.getGv_travel_current_lite().getIdSatatusTravel()) {
                    case Globales.StatusTravel.CHOFER_ASIGNADO:
                        if(isClient)
                        {
                            result = Globales.NotificationChannelCustom.CLIENTE_CHOFER_ASIGNADO;
                        }
                        else{
                            result = gloval.getGv_travel_current_lite().getIdTypeTravelKf() == Globales.StatusTypeTravel.VIAJE ?
                                    Globales.NotificationChannelCustom.CHOFFER_NUEVO_VIAJE :
                                    Globales.NotificationChannelCustom.CHOFFER_NUEVA_RESERVA;
                        }
                        break;
                    case Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA:
                        result = gloval.getGv_travel_current_lite().getIdTypeTravelKf() == Globales.StatusTypeTravel.VIAJE ?
                                Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO :
                                Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO;
                        break;
                    case Globales.StatusTravel.VIAJE_CANCELADO_POR_CLIENTE:
                        result = gloval.getGv_travel_current_lite().getIdTypeTravelKf() == Globales.StatusTypeTravel.VIAJE ?
                                Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO :
                                Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO;
                        break;

                    case Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA:
                        result = gloval.getGv_travel_current_lite().getIdTypeTravelKf() == Globales.StatusTypeTravel.VIAJE ?
                                Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA :
                                Globales.NotificationChannelCustom.CLIENTE_RESERVA_CONFIRMADA;
                        break;

                    case Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER:
                        result = Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_CHOFER;
                        break;
                    case Globales.StatusTravel.VIAJE_RECHAZADO_POR_CHOFER:
                        result = Globales.NotificationChannelCustom.CLIENTE_VIAJE_CANCELADO_POR_CHOFER;
                        break;

                }
            }
        }
        return result;
    }

    private Uri getSoundNameByChannelId(Context context, String channelId) {
        Uri result;

        switch (channelId) {
            case Globales.NotificationChannelCustom.CHOFFER_NUEVA_RESERVA:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_1_NUEVA_RESERVA));
                break;
            case Globales.NotificationChannelCustom.CHOFFER_NUEVO_VIAJE:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_2_NUEVA_CARRERA));
                break;
            case Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_4_RESERVA_CANCELADA));
                break;
            case Globales.NotificationChannelCustom.CHOFFER_VIAJE_ACEPTADO_POR_AGENCIA:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_5_VIAJE_FINALIZADO_Y_APROBADO_POR_LA_AGENCIA));
                break;
            case Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_3_CARRERA_CANCELADA));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_CHOFER_ASIGNADO:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_7_VIAJE_ACEPTADO));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_CHOFER_CERCA:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_9_CHOFER_LLEGANDO));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_RESERVA_CONFIRMADA:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_8_RESERVA_CONFIRMADA));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_CHOFER:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_10_CHOFER_EN_CAMINO));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_6_CARRERA_ACEPTADA));
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_CANCELADO_POR_CHOFER:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(BuildConfig.SOUND_GROUP).concat("_").concat(Globales.SoundNamesForNotification.SONIDO_12_VIAJE_CANCELADO_POR_CHOFER));
                break;
            default:
                result = Uri.parse("android.resource://".concat(context.getPackageName()).concat("/raw/").concat(Globales.SoundNamesForNotification.SONIDO_DEFAULT));
        }
        return result;
    }



    private String getNotificationTitleByChannelId(Context context, String channelId) {
        String result;

        switch (channelId) {
            case Globales.NotificationChannelCustom.CHOFFER_NUEVA_RESERVA:
                result = getString(R.string.notif_nueva_reserva);
                break;
            case Globales.NotificationChannelCustom.CHOFFER_NUEVO_VIAJE:
                result = getString(R.string.notif_nuevo_viaje);
                break;
            case Globales.NotificationChannelCustom.CHOFFER_RESERVA_CANCELADO:
                result = getString(R.string.notif_reserva_cancelada);
                break;
            case Globales.NotificationChannelCustom.CHOFFER_VIAJE_ACEPTADO_POR_AGENCIA:
                result = getString(R.string.notif_viaje_aceptado_por_agencia);
                break;
            case Globales.NotificationChannelCustom.CHOFFER_VIAJE_CANCELADO:
                result = getString(R.string.notif_viaje_cancelado);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_CHOFER_ASIGNADO:
                result = getString(R.string.notif_chofer_asignado);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_CHOFER_CERCA:
                result = getString(R.string.notif_chofer_cerca);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_RESERVA_CONFIRMADA:
                result = getString(R.string.notif_reserva_aceptada_por_ag);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_CHOFER:
                result = getString(R.string.notif_chofer_acepta_viaje);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA:
                result = getString(R.string.notif_viaje_aceptado_por_agencia);
                break;
            case Globales.NotificationChannelCustom.CLIENTE_VIAJE_CANCELADO_POR_CHOFER:
                result = getString(R.string.notif_channel_cliente_viaje_cancelado_por_chofer_titulo);
                break;
            default:
                result = getString(R.string.informacion_del_viaje);
        }
        return result;
    }

    private void sendNotificationToOnTopServiceForDriver(InfoTravelEntityLite travel)
    {
        if(Utils.isDriver(gloval.getGv_id_profile()) && travel.getIdSatatusTravel()==Globales.StatusTravel.CHOFER_ASIGNADO)
        {
            Log.w(TAG, "SEND NOTIF_ON_TOP");
            if(!Utils.isForegroundApp(getApplicationContext(), "HomeChofer"))
            {
                Intent intent = new Intent();
                intent.putExtra("message", "CANGUE INFO FIREBASE (ASREMIS)");
                intent.putExtra("currentTravel",travel.makeJson());
                intent.setAction(LayoutOverlayForNotification.ACTION);
                sendBroadcast(intent);
            }
            else{
                Log.w(TAG, "SEND_NOTIF_ON_TOP: HomeChofer is OnTop");
            }

        }

    }
}
