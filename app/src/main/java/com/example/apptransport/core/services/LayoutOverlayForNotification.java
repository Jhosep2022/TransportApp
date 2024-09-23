package com.example.apptransport.core.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntityLite;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;

public class LayoutOverlayForNotification extends Service  {

    public static String ACTION = "NotificationOnTopRereiver";
    MyCurrentTravelNotificationReceiver myBroadCastReceiver;
    private Context mContext;
    private WindowManager mWindowManager;
    private View mView;
    private boolean isOpenActivity;



    public static TextView txt_client_info = null;

    public static TextView txt_date_info = null;
    public static TextView txt_destination_info = null;
    public static TextView txt_origin_info = null;
    public static TextView txt_calling_info = null;
    public static TextView txt_amount_info = null;
    public static ImageView your_imageView;
    public static ImageView img_close;
    public static ImageView img_icon_person;
    private InfoTravelEntityLite infoTravelEntity;
    public static Button btn_acept;

    public static int PARAM_26_VER_PRECIO_EN_VIAJE = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        isOpenActivity=false;
        registerMyReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        initializeLayout(intent);
        moveView();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try{
            Log.w("NOTIF_ON_TOP","Destroy");
            unregisterReceiver(myBroadCastReceiver);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        if (mView != null) {
            mWindowManager.removeView(mView);
        }
        super.onDestroy();
    }

    WindowManager.LayoutParams mWindowsParams;
    private void moveView() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels);
        int height = (int) (metrics.heightPixels);

        mWindowsParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,

                (Build.VERSION.SDK_INT <= 25) ? WindowManager.LayoutParams.TYPE_PHONE : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                ,

                //WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, // Not displaying keyboard on bg activity's EditText
                //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, //Not work with EditText on keyboard
                PixelFormat.TRANSLUCENT);


        mWindowsParams.gravity = Gravity.TOP | Gravity.LEFT;
        //params.x = 0;
        mWindowsParams.y = 0;
        mWindowManager.addView(mView, mWindowsParams);

        mView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            long startTime = System.currentTimeMillis();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (System.currentTimeMillis() - startTime <= 300) {
                    return false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mWindowsParams.x;
                        initialY = mWindowsParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mWindowsParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mWindowsParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mView, mWindowsParams);
                        break;
                }
                return false;
            }
        });
    }

    private boolean isViewInBounds(View view, int x, int y) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    private void editTextReceiveFocus() {
        if (!wasInFocus) {
            mWindowsParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            mWindowManager.updateViewLayout(mView, mWindowsParams);
            wasInFocus = true;
        }
    }

    private void editTextDontReceiveFocus() {
        if (wasInFocus) {
            mWindowsParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            mWindowManager.updateViewLayout(mView, mWindowsParams);
            wasInFocus = false;
            hideKeyboard(mContext, edt1);
        }
    }

    private boolean wasInFocus = true;
    private EditText edt1;
    private void initializeLayout(Intent intent) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.layout_overlay_for_notification_view
                , null);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        mView.setVisibility(View.GONE);
        addView(mView);
    }

    private void openActivity()
    {
        try
        {
            if(!isOpenActivity)
            {
                Toast.makeText(getApplicationContext(),"Abriendo aplicación", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LayoutOverlayForNotification.this, HomeChofer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LayoutOverlayForNotification.this.startActivity(intent);
                isOpenActivity=true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void addView(View v){

        txt_date_info =  v.findViewById(R.id.txt_date_info);
        txt_client_info =  v.findViewById(R.id.text_name_info);
        txt_destination_info =  v.findViewById(R.id.txt_detination);
        txt_origin_info =  v.findViewById(R.id.txt_origin);
        txt_amount_info =  v.findViewById(R.id.txt_monto);
        txt_calling_info =  v.findViewById(R.id.txt_calling_info);
        your_imageView =  v.findViewById(R.id.img_face_client);
        img_close = v.findViewById(R.id.img_close);
        img_icon_person = v.findViewById(R.id.img_icon_person);
        btn_acept = v.findViewById(R.id.btn_acept);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.setVisibility(View.GONE);
            }
        });

        btn_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
    }

    private void setPhoto()
    {
        try
        {
            String url = Utils.getUrlImageUser(infoTravelEntity.getIdUserClient());
            Glide.with(your_imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_update)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.ic_user)
                    //.centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(your_imageView);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private void registerMyReceiver() {
        try
        {
            myBroadCastReceiver = new MyCurrentTravelNotificationReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION );
            registerReceiver(myBroadCastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setValores(){
        List<paramEntity> params =  Utils.getParamsFromLocalPreferences(getApplicationContext());
        Currency currecy =  Utils.getCurrency(getApplicationContext());
        txt_date_info.setText(infoTravelEntity.getMdate());
        txt_client_info.setText(infoTravelEntity.getClient());

        int param49_ver_Telefono_de_pasajero = Integer.parseInt(params.get(Globales.ParametrosDeApp.PARAM_49_VER_TELEFONO_DE_PASAJERO).getValue());// SE PUEDE VER TELEFONO DE PASAJEROS

        if(infoTravelEntity.getPhoneNumber() != null && param49_ver_Telefono_de_pasajero == 1) {
            txt_calling_info.setText(infoTravelEntity.getPhoneNumber());
        }
        txt_origin_info.setText(infoTravelEntity.nameOrigin);

        if (infoTravelEntity.isMultiDestination == null || "NO".equals(infoTravelEntity.isMultiDestination )){
            txt_destination_info.setText(infoTravelEntity.getNameDestination());
        }else{
            StringBuilder multiOrigen = new StringBuilder();
            String[] destination = infoTravelEntity.MultiDestination.split("-");

            for (int i = 0;i<destination.length; i++){
                multiOrigen.append(i + 1).append(") ").append(destination[i]).append("\n");
            }
            txt_destination_info.setText(multiOrigen.toString());
        }

        PARAM_26_VER_PRECIO_EN_VIAJE =  Integer.parseInt(params.get(Globales.ParametrosDeApp.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP).getValue());
        mostrarPrecio(PARAM_26_VER_PRECIO_EN_VIAJE, infoTravelEntity.getAmountCalculate(), currecy, infoTravelEntity.isTravelComany==1);
    }

    private void mostrarPrecio(int param26_mostrarPrecio, String amount, Currency currency, boolean isTravelCompany)
    {
        String amountToShow="0";
        if (Utils.isShowPrice(param26_mostrarPrecio, isTravelCompany)) {
            amountToShow=amount;
        }
        txt_amount_info.setText(currency.getSymbol().concat(" ").concat(amountToShow));
    }


    public class MyCurrentTravelNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                Log.w("NOTIF_ON_TOP", "MyCurrentTravelNotificationReceiver");
                Bundle extras = intent.getExtras();
                if(extras!= null){
                    if(extras.containsKey("currentTravel")){
                        showPopup();
                        Gson gson= new Gson();
                        InfoTravelEntityLite currentTravel = gson.fromJson(extras.getString("currentTravel"), InfoTravelEntityLite.class);
                        Log.w("NOTIF_ON_TOP", "CodTravel: ".concat(currentTravel.getCodTravel()));
                        infoTravelEntity = currentTravel;
                        setValores();
                        setPhoto();
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void showPopup()
    {
        mView.setVisibility(View.VISIBLE);
        img_icon_person.setVisibility(View.VISIBLE);
    }
}
