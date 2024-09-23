package com.example.apptransport.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.apreciasoft.mobile.asremis.Dialog.DistanceChooserDialog;
import com.apreciasoft.mobile.asremis.Entity.Chat;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceive;
import com.apreciasoft.mobile.asremis.Entity.CompanyData;
import com.apreciasoft.mobile.asremis.Entity.DriverSemaforo;
import com.apreciasoft.mobile.asremis.Entity.DriverSemaforoItem;
import com.apreciasoft.mobile.asremis.Entity.FinalCalculatedPrices;
import com.apreciasoft.mobile.asremis.Entity.PriceAndIsMinimum;
import com.apreciasoft.mobile.asremis.Entity.VehicleType;
import com.apreciasoft.mobile.asremis.Fragments.BaseFragment;
import com.apreciasoft.mobile.asremis.Fragments.FragmentChatOperators;
import com.apreciasoft.mobile.asremis.Fragments.FragmentChatPassenger;
import com.apreciasoft.mobile.asremis.Fragments.FragmentFrequentQuestions;
import com.apreciasoft.mobile.asremis.Util.IExecutionComplete;
import com.apreciasoft.mobile.asremis.core.services.LayoutOverlayForNotification;
import com.apreciasoft.mobile.asremis.viewmodels.ChatViewModel;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Adapter.ListenerReservationUpdate;
import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Dialog.ExitDialog;
import com.apreciasoft.mobile.asremis.Dialog.FinalTravelDialog;
import com.apreciasoft.mobile.asremis.Dialog.ListenerDialogExit;
import com.apreciasoft.mobile.asremis.Dialog.ListenerDialogFinalTravel;
import com.apreciasoft.mobile.asremis.Dialog.NoGpsDialog;
import com.apreciasoft.mobile.asremis.Dialog.TravelDialog;
import com.apreciasoft.mobile.asremis.Dialog.TravelInfoChoferDialog;
import com.apreciasoft.mobile.asremis.Dialog.UpdateDialog;
import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.DestinationEntity;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.OriginEntity;
import com.apreciasoft.mobile.asremis.Entity.TraveInfoSendEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelBodyEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelLocationEntity;
import com.apreciasoft.mobile.asremis.Entity.notification;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Entity.token;
import com.apreciasoft.mobile.asremis.Entity.tokenFull;
import com.apreciasoft.mobile.asremis.Fragments.AccountStatusFragment;
import com.apreciasoft.mobile.asremis.Fragments.FragmentChat;
import com.apreciasoft.mobile.asremis.Fragments.FragmentHistoryTravel;
import com.apreciasoft.mobile.asremis.Fragments.FragmentReporte;
import com.apreciasoft.mobile.asremis.Fragments.HomeFragment;
import com.apreciasoft.mobile.asremis.Fragments.ListenerFragmentChofer;
import com.apreciasoft.mobile.asremis.Fragments.NotificationsFrangment;
import com.apreciasoft.mobile.asremis.Fragments.ProfileClientFr;
import com.apreciasoft.mobile.asremis.Fragments.ProfileDriverFr;
import com.apreciasoft.mobile.asremis.Fragments.ReservationsFrangment;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.RequestHandler;
import com.apreciasoft.mobile.asremis.Util.ServicesSleep;
import com.apreciasoft.mobile.asremis.Util.Signature;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.Util.WsTravel;
import com.apreciasoft.mobile.asremis.core.services.SocketServices;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */

@SuppressWarnings("FieldCanBeLocal")
@SuppressLint("LongLogTag")
public class HomeChofer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ListenerFragmentChofer,ListenerDialogFinalTravel,ListenerDialogExit {

    public static final String UPLOAD_URL = HttpConexion.getUrlBaseForVehiclesImages().concat("safeimg.php");
    public static final String UPLOAD_KEY = "image";
    public static final String CHAT_RECEIVER_TAG="chatReceiverTag";
    public static boolean MUST_SHOW_STREET_BUTTON=true;

    public static File f;
    public GlovalVar gloval;
    public boolean flatParam87 = false;
    public boolean stop = false;
    private boolean isWaitingForCloseTravelByweb = false;
    private FinalTravelDialog finalTravelDialog;
    public ServicesTravel daoTravel = null;
    public ServicesLoguin daoLoguin = null;

    public ProgressDialog loading;
    private android.app.AlertDialog alertDialog;
    public static double totalFinal = 0;
    public static double priceFlet = 0;
    private double peajeMonto;
    private double estacionamientoMonto;

    boolean _NOCONEXION = false;

    public FloatingActionButton floatingActionButton1;

    View menuItemReservationView;
    TextView textCartItemCount;

    public Timer timer, timerConexion, timeTravelByHour;
    public static final int SIGNATURE_ACTIVITY = 1;
    public static final int PROFILE_DRIVER_ACTIVITY = 2;
    public static final int CREDIT_CAR_ACTIVITY = 3;
    public final static int OVERLAY_REQUEST_CODE = 4;

    ServicesLoguin apiService = null;

    public static InfoTravelEntity currentTravel;

    public double km_total = 0.001;
    public double m_total = 0;
    public double kilometros_total = m_total * km_total;

    public double km_vuelta = 0.001;
    public double m_vuelta = 0;
    public double kilometros_vuelta = m_vuelta * km_vuelta;

    public double m_ida = 0;
    public double kilometros_ida = m_vuelta * km_vuelta;
    public double  bandera = 0;

    /*
    PAGOS DE TARJETA
     */
    public static String mp_jsonPaymentCard = "";
    public static boolean _PAYCREDITCAR_OK = false;

    private static final String TAG = "AnimationStarter";
    private ObjectAnimator textColorAnim;


    public DecimalFormat df = new DecimalFormat("####0.00");
    public double amounCalculateGps;
    private String timeElpasedInTravelByHour="";

    public boolean viewAlert = false;

    public Bitmap bitmap;
    public String path_image;
    public WsTravel ws = null;
    public int tiempoTxt = 0;
    public int idPaymentFormKf = 0;

    public Button btnPreFinish;
    public Button btnReturn;
    public static int isRoundTrip;
    double extraTime = 0;

    public TextView textTiempo;
    public View parentLayout = null;

    int PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB = 0; // CIERRE DE VIAJE EMPRESA EN WEB
    int PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB = 0; // CIERRE DE VIAJE PARTICULARES Y EXPRESSS EN WEB
    int PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR = 0; // VERIFICAR SI HAY QUE ESPERAR VALIDACIÓN DEL PANEL PARA FINALIZAR EL SERVICIO
    int PARAM_18 = 0; // ASIGNACION AUTOMATICA
    public static int PARAM_39, PARAM_66_VER_PRECIO_VIAJE_EN_APP = 0; // ACTIVAR BOTON DE VUELTA
    public static int PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP = 0;
    public static double PARAM_1_PRECIO_LISTA_X_KM, PARAM_6 = 0;
    public static int PARAM_68_PAGA_CON_TARJETA = 0; // ACTIVAR PAGO CON TARJETA
    public static String PARAM_69_KEY_MP = ""; // ACTIVAR PAGO CON TARJETA
    public static String PARAM_79 = ""; // ACTIVAR PAGO CON TARJETA
    public static int PARAM_82 = 0; // USO DE MERCADO PAGO
    public static int PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES = 0; // MINUTOS DE ESPERA QUE NO SE CARGAN VIAJES EN PARTICULARES
    public static String PARAM_103_PAYMENT_CARD_PROVIDER = "";


    public FloatingActionMenu materialDesignFAM;


    /*DIALOG*/
    public TravelDialog dialogTravel = null;
    public SharedPreferences.Editor editor;
    public static SharedPreferences pref;

    public DrawerLayout drawer;

    public CircleImageView your_imageView;

    public Button btnLogin;
    public Button btnInitSplep;
    public Button btn_return;
    public Button btn_init;
    public Button btn_cancel;
    public ImageButton btnOpenGMapFindPassenger;
    public ImageButton btnOpenGMapGoDestination;

    public ImageButton btnOpenWazeFindPassenger;
    public ImageButton btnOpenWazeGoDestination;

    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    private String location;
    private String lat;
    private String lon;
    private String dateTravel;

    private int ventanaNavegacion=0;
    private int isKmWithError = 0;
    public CompanyData companyData;

    private ChatNotifiacionSocketReceiver chatNotifiacionSocketReceiver;
    ChatViewModel chatViewModel;


    @SuppressWarnings("deprecation")
    @SuppressLint({"WrongViewCast", "InvalidWakeLockTag", "CommitPrefEdits"})
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validarVersionApp();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLoadTodays, new IntentFilter("update-message"));
        escucharChatReceiver();
        setContentView(R.layout.activity_home);
        parentLayout = findViewById(R.id.content_home);
        Toolbar toolbar = getToolbarConfigured();



        // variable global //
        gloval = ((GlovalVar) getApplicationContext());
        configureGlovalData();
        recargarParametrosLocales();
        initializeControls();

        FirebaseMessaging.getInstance();
        //ACTIVAMOS EL TOkEN FIRE BASE//
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.w("JSON_TOKEN", token);
        enviarTokenAlServidor(token, gloval.getGv_user_id());


        textTiempo = findViewById(R.id.textTiempo);
        textTiempo.setVisibility(View.INVISIBLE);

        configureButtons();
        configureBtnGMaps();
        configureNavigationDrawer(toolbar);
        configureBtnRequestStreetTravel();
        configureFragment();
        checkNeedLogin();

        loadParamsFromApi();

        createNotificationChannels(this);   //Agregado el 19/06/2019: crea los canales de notificación para Android O en adelante

        //LLAMAMOS A EL METODO PARA HABILITAR PERMISOS//
        validarGPS();
        connectWebSocket();

        btPreFinishVisible(false);
        btInitVisible(false);
        btCancelVisible(false);
        btnOpenGMapFindPassengerVisible(false);
        btnOpenGmapGoDestinationVisible(false);
        _activeTimer();
        configureTxtTimeSleep();
        virifiqueParam();
        getCurrentTravelByIdDriver();
        checkDrawOverlayPermission();
        initializeViewModel();
        openChatFragmentIfComeFromNotification();
    }



    private void initializeViewModel(){
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
    }

    private void escucharChatReceiver(){
        chatNotifiacionSocketReceiver = new ChatNotifiacionSocketReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CHAT_RECEIVER_TAG);
        registerReceiver(chatNotifiacionSocketReceiver, intentFilter);
    }

    @SuppressLint("WrongViewCast")
    private void initializeControls() {
        // UI INICIALIZACION //
        this.your_imageView = findViewById(R.id.imageViewUser);
        this.drawer = findViewById(R.id.drawer_layout);
        this.btnLogin = findViewById(R.id.btn_pre_finish);
        this.btnInitSplep = findViewById(R.id.btn_iniTimeSlep);
        this.btn_return = findViewById(R.id.btn_return);
        this.btn_init = findViewById(R.id.btn_init);
        this.btn_cancel = findViewById(R.id.btn_cancel);

        this.btnPreFinish = findViewById(R.id.btn_pre_finish);
        this.btnReturn = findViewById(R.id.btn_return);
        this.materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu_chofer);
        this.floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item_chofer);
    }

    private void configureGlovalData() {
        pref = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0); // 0 - for private mode
        editor = pref.edit();
        gloval.setGv_user_id(pref.getInt("user_id", 0));
        gloval.setGv_idResourceSocket(pref.getString("is_resourceSocket", ""));
        gloval.setGv_id_cliet(pref.getInt("client_id", 0));
        gloval.setGv_id_profile(pref.getInt("profile_id", 0));
        gloval.setGv_id_driver(pref.getInt("driver_id", 0));
        gloval.setGv_user_mail(pref.getString("user_mail", ""));
        gloval.setGv_user_name(pref.getString("user_name", ""));
        gloval.setGv_base_intance(pref.getString("instance", ""));
        gloval.setGv_nr_driver(pref.getString("nrDriver", ""));
    }

    private Toolbar getToolbarConfigured() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        return toolbar;
    }

    private void configureBtnRequestStreetTravel() {
        materialDesignFAM.setIconAnimated(false);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

                materialDesignFAM.close(true);
                try {
                    requestTravel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void configureTxtTimeSleep() {
        tiempoTxt = pref.getInt("time_slepp", 0);
        if(tiempoTxt > 0){
            textTiempo.setVisibility(View.VISIBLE);
            textTiempo.setText(Utils.covertSecondsToHHMMSS(tiempoTxt));
        }else {
            textTiempo.setVisibility(View.INVISIBLE);
        }


        if(ServicesSleep.mRunning){
            btnInitSplep.setText("PAUSAR");
            textColorAnim = ObjectAnimator.ofInt(this.btnInitSplep, "textColor", Color.parseColor("#ffffff"),  Color.parseColor("#26c281"));
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
            textColorAnim.start();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (textColorAnim != null) {
                    textColorAnim.pause();
                }
            }
        }
    }

    private void connectWebSocket() {
        ws = new WsTravel(this);
        ws.connectWebSocket(pref.getInt("user_id", 0));
    }

    private void configureFragment() {
        FragmentManager fr = getSupportFragmentManager();
        fr.beginTransaction().replace(R.id.content_frame, new HomeFragment(this)).commitAllowingStateLoss();
    }

    private void configureNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // HEADER MENU //
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.username);
        TextView email = header.findViewById(R.id.email);
        name.setText(gloval.getGv_user_name());
        email.setText("Nº "+gloval.getGv_nr_driver());
        downloadUserImage(header);
        hideOrShowFrequentQuestions(navigationView);

    }

    private void hideOrShowFrequentQuestions(NavigationView navigationView)
    {
        try{
            int INDEX_MENU_DRIVER_FREQUENT_QUESTIONS=6;
            MenuItem itemFrequentQuestions =navigationView.getMenu().getItem(INDEX_MENU_DRIVER_FREQUENT_QUESTIONS);
            if(!Utils.isPlaceToPayConfigured(gloval.getGv_param())){
                itemFrequentQuestions.setVisible(false);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void configureBtnGMaps() {
        btnOpenGMapFindPassenger = findViewById(R.id.btn_open_gmap_find_passenger);
        btnOpenGMapFindPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String lat = gloval.getGv_travel_current().getLatOrigin();
                    String lng = gloval.getGv_travel_current().getLonOrigin();
                    openGoogleMapsNavigation(lat, lng);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        btnOpenGMapGoDestination = findViewById(R.id.btn_open_gmap_go_destination);
        btnOpenGMapGoDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String lat = gloval.getGv_travel_current().getLatDestination();
                    String lng = gloval.getGv_travel_current().getLonDestination();
                    openGoogleMapsNavigation(lat, lng);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        /*Waze*/
        btnOpenWazeFindPassenger = findViewById(R.id.btn_open_waze_find_passenger);
        btnOpenWazeFindPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String lat = gloval.getGv_travel_current().getLatOrigin();
                    String lng = gloval.getGv_travel_current().getLonOrigin();
                    openWazeNavigation(lat, lng);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        btnOpenWazeGoDestination = findViewById(R.id.btn_open_waze_go_destination);
        btnOpenWazeGoDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String lat = gloval.getGv_travel_current().getLatDestination();
                    String lng = gloval.getGv_travel_current().getLonDestination();
                    openWazeNavigation(lat, lng);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void configureButtons() {
        final Button btnInit = findViewById(R.id.btn_init);
        final Button btnCancel = findViewById(R.id.btn_cancel);
        final Button btnIniTimeSlep = findViewById(R.id.btn_iniTimeSlep);

        // BOTON PARA INICIAR TIEMPO DE ESPERA DE UN VIAJE //
        btnIniTimeSlep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iniTimeSlep();
            }
        });

        // BOTON PARA PRE FINALIZAR UN VIAJE //
        btnPreFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeChofer.this);
                builder.setMessage(R.string.finalizar_viaje_esta_seguro)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.finalizar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    cancalTimerTravelByHour();
                                    showFinshTravel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        // BOTON PARA PONER RETORNO DE  UN VIAJE //
        if (PARAM_39 == 1) {
            btnReturn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeChofer.this);
                    builder.setMessage(R.string.set_retornar_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.retornar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isRoundTrip();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        } else {
            btnReturn.setEnabled(false);
        }

        // BOTON PARA INICIAR UN VIAJE //
        btnInit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verifickTravelCancelInit();
            }
        });

        // BOTON PARA CANCELAR UN VIAJE //

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelTravel(-1);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {

        this.getParam();

        dismissDialogTravel();


        if (currentTravel != null) {
            Log.d("currentTravel", "onResume" + String.valueOf(currentTravel.getIdSatatusTravel()));
            controlViewTravel();
        } else {
            getCurrentTravelByIdDriver();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregister(broadcastReceiverLoadTodays);
            unregister(chatNotifiacionSocketReceiver);
            cancelTimer();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("onStop", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //createFloatingWidget(parentLayout);
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Log.d("onRestart", "onRestart");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("uri", true);

    }

    // FIRMA O TARJETA //
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        try {
            switch (requestCode) {
                case SIGNATURE_ACTIVITY:
                    if (resultCode == RESULT_OK) {

                        Bundle bundle = data.getExtras();
                        String status = bundle.getString("status");
                        path_image = bundle.getString("image");
                        Uri filePath = Uri.parse(path_image);

                        if (status.equalsIgnoreCase("done")) {
                            Toast.makeText(this, R.string.firma_guardada, Toast.LENGTH_SHORT).show();
                            try {

                                f=new File(path_image, Globales.NAME_FIRMA_IMG);
                                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));

                                postImageData();
                                f.delete();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                case PROFILE_DRIVER_ACTIVITY:
                    //super.onActivityResult(requestCode, resultCode, data);
                    break;
                case CREDIT_CAR_ACTIVITY:
                    //super.onActivityResult(requestCode, resultCode, data);
                    if (HomeChofer._PAYCREDITCAR_OK) {
                        verifickTravelFinish();
                    }
                    break;
                case OVERLAY_REQUEST_CODE: {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.canDrawOverlays(this)) {
                            openFloatingWindow();
                        }
                    } else {
                        openFloatingWindow();
                    }
                    break;
                }
            }
        }catch (Exception e){
            Log.d("e",e.getMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        if( Utils.verificaConexion(this) == false) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            if (id == R.id.nav_ubicacion) {
                showMapFragment(fm);

            } else if (id == R.id.nav_historial) {

                if (ventanaNavegacion!=1){
                    fm.beginTransaction().replace(R.id.content_frame, new FragmentHistoryTravel()).commitAllowingStateLoss();
                    ventanaNavegacion=1;
                }

            } else if (id == R.id.nav_slideshow) {

                if (ventanaNavegacion!=2){
                    fm.beginTransaction().replace(R.id.content_frame, new AccountStatusFragment()).commitAllowingStateLoss();
                    ventanaNavegacion=2;
                }

            } else if (id == R.id.nav_manage) {

                if (ventanaNavegacion!=3){
                    fm.beginTransaction().replace(R.id.content_frame, new NotificationsFrangment()).commitAllowingStateLoss();
                    ventanaNavegacion=3;
                }

            } else if (id == R.id.nav_reservations) {

                if (ventanaNavegacion!=3){
                    fn_gotoreservation();
                    ventanaNavegacion=3;
                }

            }else if (id == R.id.nav_chat){

                if (ventanaNavegacion!=4){
                    fn_chat();
                    ventanaNavegacion=4;
                }
            }
            else if (id == R.id.nav_send) {

                if (ventanaNavegacion!=5){
                    fn_reporte();
                    ventanaNavegacion=5;
                }

            } else if (id == R.id.nav_profile){

                if (ventanaNavegacion!=6){
                    fn_gotoprofile();
                    ventanaNavegacion=6;
                }

            } else if (id == R.id.nav_exit){
                new ExitDialog(this).show(getSupportFragmentManager(),"DialogExit");
            }
            else if (id == R.id.nav_frequent_questions){

                if (ventanaNavegacion!=7){
                    fm.beginTransaction().replace(R.id.content_frame, new FragmentFrequentQuestions()).commitAllowingStateLoss();
                    ventanaNavegacion=7;
                }

            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!isBackHandledByFragment()){
            new ExitDialog(this).show(getSupportFragmentManager(),"DialogExit");
        }
    }

    private boolean isBackHandledByFragment(){
        boolean result=false;
        try{
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            boolean handled;
            for(Fragment f : fragmentList) {
                if(f instanceof BaseFragment) {
                    handled = ((BaseFragment)f).onBackPressed();
                    if(handled) {
                        result=true;
                        break;
                    }
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    private void unregister(BroadcastReceiver receiver){
        try{
            unregisterReceiver(receiver);
        }
        catch (Exception ex){

        }
    }

    private int mNotificationsCount = 0;
    private int mNotificationsReservationsCount = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_home, menu);

        // ESTO NOS PERMITE CARGAR EL ICONO DE MENU CON NOTIFICACIONBES DE RESERVAS //
        MenuItem menuItemReservation =  menu.findItem(R.id.action_reervations);
        menuItemReservationView = menuItemReservation.getActionView();
        textCartItemCount = (TextView) menuItemReservationView.findViewById(R.id.cart_badge);
        //menuItemReservationView.setImageResource(R.drawable.ic_notification_reservations);
        menuItemReservationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaNavegacion=3;
                fn_gotoreservation();
            }
        });
        // ACTUALIZAMOS EL NUMERO DE NOTIFICCIONES
        getReservations();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            ventanaNavegacion=3;
            fn_gotonotification();
            return true;
        }
        else if (id == R.id.action_reervations) {
            ventanaNavegacion=3;
            fn_gotoreservation();
            return true;
        }
        else if (id == R.id.action_refhesh) {

            fn_refhesh();
            return true;
        }
        else if(id==R.id.action_map){

            FragmentManager fm = getSupportFragmentManager();
            showMapFragment(fm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkNeedLogin()
    {
        String needLogin =  Utils.getValueFromSharedPreferences(getApplicationContext(), Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_1_CLIENT);
        String needLoginV4_4_1 =  Utils.getValueFromSharedPreferences(getApplicationContext(), Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_4_1_CLIENT);
        if("".equals(needLogin) || "".equals(needLoginV4_4_1)){
            fn_exit();
        }
    }

    public  void validarVersionApp(){
        HttpConexion.setBase(HttpConexion.instance);
        if (this.apiService == null) {
            this.apiService = HttpConexion.getUri().create(ServicesLoguin.class);
        }

        apiService.checkVersion(BuildConfig.VERSION_NAME).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    if (response.code() == 200) {
                        boolean rs =  response.body();
                        if ( rs && !Utils.isDeveloperInstance()) {
                            new UpdateDialog().show(getSupportFragmentManager(), "DialogUpdate");
                        }

                    } else {
                        showMessage(getString(R.string.fallo_version_app),3);

                    }
                }catch (Exception e){
                    Log.e("ERROR VERSION APP",e.toString());
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("FALLO VERSION APP",t.toString());
            }
        });

    }

    private void virifiqueParam() {
        try{
            if (Integer.parseInt(gloval.getGv_param().get(83).getValue()) == 0) {
                floatingActionButton1.setVisibility(View.GONE);
            }else {
                floatingActionButton1.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }

    private void showMapFragment(FragmentManager fm){
        if (ventanaNavegacion!=0){
            fm.beginTransaction().replace(R.id.content_frame, new HomeFragment(this)).commitAllowingStateLoss();
            getCurrentTravelByIdDriver();
            ventanaNavegacion=0;
        }
    }

    /*
    Created by  : Sergio Franco
    Date Created: 18/05/2019
    Description : Actualiza la imagen del usuario en el Navigation Drawer. Se llama cuando se actualiza la foto en la edición de los datos.
     */
    public void updateUserImage()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        downloadUserImage(header);
    }

    private void downloadUserImage(View header){
        String url = Utils.getUrlImageUser(gloval.getGv_user_id());
        ImageView imageView = header.findViewById(R.id.imageViewUser);
        Log.e("URL_IMAGE", url);
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_update)

                .error(R.drawable.ic_user)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    /// LLAMAMOS A EL SERVICIO DE SOLICITAR VIAJE //
    public void requestTravel() throws JSONException {

        getCurrentTravelByIdDriver();
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        if(HomeFragment.getmLastLocation() == null){

            showMessage(getString(R.string.debe_activar_gps),0);

        }else {
            try {

                if(currentTravel == null) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date date = new Date();

                    String currentDate = dateFormat.format(date);

                    TravelEntity travel = new TravelEntity();

                    this.location = HomeFragment.nameLocation;
                    this.lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
                    this.lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
                    this.dateTravel = currentDate;

                    int idUserCompany = 0;
                    boolean isTravelComany = false;
                    idUserCompany = 0;

                    travel.setTravelBody(
                            new TravelBodyEntity(
                                    gloval.getGv_id_cliet(),
                                    isTravelComany,
                                    new OriginEntity(
                                            this.lat,
                                            this.lon,
                                            this.location),
                                    new DestinationEntity(
                                            "",
                                            "",
                                            ""
                                    )
                                    , this.dateTravel, -1, false, idUserCompany,
                                    "", "", "", "", 0, false,
                                    0.0,
                                    0.0,
                                    gloval.getGv_id_driver()
                            )
                    );


                    // VALIDAMOS RESERVA O VIAJE //
                    boolean validateRequired = true;
                    validateRequired = true;


                    if (validateRequired) {
                        floatingActionButton1.setEnabled(false);

                        Call<resp> call = this.daoTravel.addTravel(travel);

                        loading = ProgressDialog.show(this, getString(R.string.enviando_viaje), getString(R.string.espere_unos_segundos), true, false);


                        call.enqueue(new Callback<resp>() {
                            @Override
                            public void onResponse(Call<resp> call, Response<resp> response) {
                                loading.dismiss();

                                Log.d("Response raw header", response.headers().toString());
                                Log.d("Response raw", String.valueOf(response.raw().body()));
                                Log.d("Response code", String.valueOf(response.code()));


                                if (response.code() == 200) {

                                    Toast.makeText(getApplicationContext(), R.string.viaje_solicitado_title, Toast.LENGTH_SHORT).show();


                                    materialDesignFAM.close(true);

                                    showOrHideStreetButton(false);
                                    floatingActionButton1.setEnabled(false);

                                    getCurrentTravelByIdDriver();
                                } else {

                                    loading.dismiss();

                                    AlertDialog alertDialog = new AlertDialog.Builder(HomeChofer.this).create();
                                    alertDialog.setTitle("ERROR" + "(" + response.code() + ")");
                                    assert response.errorBody() != null;
                                    alertDialog.setMessage(response.errorBody().source().toString());


                                    Log.w("***", response.errorBody().source().toString());


                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    showOrHideStreetButton(true);
                                                    floatingActionButton1.setEnabled(true);
                                                }
                                            });
                                    alertDialog.show();

                                }


                            }

                            @Override
                            public void onFailure(Call<resp> call, Throwable t) {
                                loading.dismiss();

                                Snackbar.make(findViewById(android.R.id.content),
                                        "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                                showOrHideStreetButton(false);
                                floatingActionButton1.setEnabled(false);


                            }
                        });

                    }
                }else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.ya_posee_viaje, Snackbar.LENGTH_LONG).show();
                }

            } finally{
                this.daoTravel = null;
            }
        }

    }


    private void setParamLocal() {
        try {
            PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP).getValue());// SE PUEDE VER PRECIO EN VIAJE EN APP
            PARAM_66_VER_PRECIO_VIAJE_EN_APP = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_66_VER_PRECIO_EN_VIAJE_EN_APP).getValue());// SE PUEDE VER PRECIO EN VIAJE EN APP
            PARAM_68_PAGA_CON_TARJETA = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_68_PAGA_CON_TARJETA).getValue());// SE PAGAR CON TARJETA
            PARAM_69_KEY_MP = gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_69_KEY_MP).getValue();//
            PARAM_79 = gloval.getGv_param().get(78).getValue();//
            PARAM_82 = Integer.parseInt(gloval.getGv_param().get(81).getValue());//


        } catch (IndexOutOfBoundsException e) {
            PARAM_69_KEY_MP = "";
            PARAM_79 = "";
            PARAM_82 = 0;
        } catch (Exception e){
            Log.d("error",e.getMessage());
        }
        PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB = getParamValue(Globales.ParametrosDeApp.PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB,0);
        PARAM_39 = getParamValue(38,0);
        PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB = getParamValue(Globales.ParametrosDeApp.PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB,0);
        PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR = getParamValue(Globales.ParametrosDeApp.PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR,0);
        PARAM_18 = getParamValue(17, 0);
        PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES = getParamValue(Globales.ParametrosDeApp.PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES, 0);
        PARAM_103_PAYMENT_CARD_PROVIDER = getParamValue(Globales.ParametrosDeApp.PARAM_103_PAYMENT_CARD_PROVIDER, "");

    }

    private Double getParamValue(int pos, Double defaultValue)
    {
        try {
            return Double.parseDouble(gloval.getGv_param().get(pos).getValue());
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private int getParamValue(int pos, int defaultValue)
    {
        try {
            return Integer.parseInt(gloval.getGv_param().get(pos).getValue());
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private String getParamValue(int pos, String defaultValue)
    {
        try {
            return gloval.getGv_param().get(pos).getValue();
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    public void asigndTravelDriver() {
            loading = ProgressDialog.show(HomeChofer.this, getString(R.string.verificando_viaje), getString(R.string.espere_unos_segundos), true, false);


            if (!Utils.verificaConexion(this)) {
                showAlertNoConexion();
            } else { // VERIFICADOR DE CONEXION

                if (this.daoTravel == null) {
                    this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
                }


                try {


                    TravelEntity travel = new TravelEntity(new TravelBodyEntity(
                            gloval.getGv_id_driver(), currentTravel.getIdTravel(), gloval.getGv_user_id()
                    ));


                    Call<InfoTravelEntity> call = this.daoTravel.asigned(travel);

                    Log.d("fatal", call.request().toString());
                    Log.d("fatal", call.request().headers().toString());

                    call.enqueue(new Callback<InfoTravelEntity>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {


                            if (response.code() == 200) {
                                InfoTravelEntity result = response.body();
                                loading.cancel();
                                getCurrentTravelByIdDriver();
                                verifickTravelCancelInit();

                            } else {
                                loading.cancel();
                                AlertDialog alertDialog = new AlertDialog.Builder(HomeChofer.this).create();
                                alertDialog.setTitle("ERROR" + "(" + response.code() + ")");
                                alertDialog.setMessage(response.errorBody().source().toString());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                            }
                        }

                        public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                            loading.cancel();
                            Snackbar.make(findViewById(android.R.id.content),
                                    "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                        }

                    });

                } finally {
                    this.daoTravel = null;
                }

            }
    }

    public boolean checkDistanceSucces() {
        try {

            int param30 = Integer.parseInt(gloval.getGv_param().get(29).getValue());
            if (param30 > 0) {

                Location locationA = new Location(HomeFragment.nameLocation);
                locationA.setLatitude(HomeFragment.getmLastLocation().getLatitude());
                locationA.setLongitude(HomeFragment.getmLastLocation().getLongitude());

                Location locationB = new Location(currentTravel.getNameOrigin());
                locationB.setLatitude(Double.parseDouble(currentTravel.getLatOrigin()));
                locationB.setLongitude(Double.parseDouble(currentTravel.getLonOrigin()));

                float distance = locationA.distanceTo(locationB);
                Log.d("distance", String.valueOf(distance));
                Log.d("distance", String.valueOf(param30));

                return distance <= param30;
            }
            return false;

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            return false;
        }
    }

    public boolean validarGPS() {
        if (!isGPSProvider(this)&&!isNetowrkProvider(this)){
            activarGps();
            return  false;
        }else {
            activarServicio();
            return true;
        }
    }

    public void showDialogNewTravel() {
        try {


            dismissDialogTravel();


            if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA) {

                Toast.makeText(getApplicationContext(),  R.string.viaje_cancelado, Toast.LENGTH_LONG).show();
                btPreFinishVisible(false);

                btInitVisible(false);
                btCancelVisible(false);
                btnOpenGMapFindPassengerVisible(false);
                btnOpenGmapGoDestinationVisible(false);
                viewAlert = false;
                currentTravel = null;
                sendCurrentTravelToService();
                gloval.setGv_travel_current(null);
                setInfoTravel();

                tiempoTxt = 0;
                extraTime = 0;
                editor.putInt("time_slepp", 0);
                editor.commit(); // commit changes

                textTiempo.setVisibility(View.INVISIBLE);

                // _activeTimer();
            } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_CANCELADO_POR_CLIENTE) {
                Toast.makeText(getApplicationContext(), R.string.viaje_cancelado_por_cliente, Toast.LENGTH_LONG).show();
                btPreFinishVisible(false);

                btInitVisible(false);
                btCancelVisible(false);
                btnOpenGMapFindPassengerVisible(false);
                btnOpenGmapGoDestinationVisible(false);

                viewAlert = false;
                currentTravel = null;
                sendCurrentTravelToService();
                gloval.setGv_travel_current(null);
                setInfoTravel();

                tiempoTxt = 0;
                extraTime = 0;
                editor.putInt("time_slepp", 0);
                editor.commit(); // commit changes

                textTiempo.setVisibility(View.INVISIBLE);

                //  _activeTimer();
            } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO) {
                Toast.makeText(getApplicationContext(), R.string.viaje_finalizado_desde_web, Toast.LENGTH_LONG).show();

                btPreFinishVisible(false);
                viewAlert = false;

                currentTravel = null;
                sendCurrentTravelToService();
                gloval.setGv_travel_current(null);
                setInfoTravel();

                tiempoTxt = 0;
                extraTime =0;
                editor.putInt("time_slepp", 0);
                editor.commit(); // commit changes

                textTiempo.setVisibility(View.INVISIBLE);

                //_activeTimer();
            } else {

                if (!viewAlert) {
                    if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.CHOFER_ASIGNADO) {
                        viewAlert = true;
                        dialogTravel = new TravelDialog();
                        dialogTravel.show(getSupportFragmentManager(), "DialogTravel");
                        dialogTravel.setCancelable(false);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fn_exit() {

        Utils.sendIsDriverToService( this,"false");
        currentTravel = null;
        sendCurrentTravelToService();
        gloval.setGv_logeed(false);
        new GlovalVar();

        enviarTokenAlServidor("", gloval.getGv_user_id());

        if (ws != null) {
            ws.closeWebSocket();
        }

        cancelTimer();

        if (timerConexion != null) {
            timerConexion.cancel();
        }

        cancalTimerTravelByHour();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit(); // commit changes

        if (isServiceRunning(SocketServices.class)){
            stopService(new Intent(this,SocketServices.class));
        }
        closeFloatingWindowsService();
        logoutSocialIfPresent();
        Intent main = new Intent(HomeChofer.this, LoginActivity.class);
        startActivity(main);

        finish();
    }

    private void cancelTimer()
    {
        try{
            if (timer != null) {
                timer.cancel();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void cancalTimerTravelByHour()
    {
        try{
            if(timeTravelByHour!=null)
            {
                timeTravelByHour.cancel();
                timeTravelByHour=null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void controlViewTravel() {
        try {
            setBtnPauseVisible(true);
            if (currentTravel == null) {
                currentTravel = gloval.getGv_travel_current();
                sendCurrentTravelToService();

            }
            if (currentTravel != null) {
                setBtnPauseVisible(false);
                Log.d("TravelStatus", String.valueOf(currentTravel.getIdSatatusTravel()));

                HomeChofer.isRoundTrip = currentTravel.isRoundTrip;

                // CHOFER BUSQUEDA DE CLIENTE //
                if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER) {
                    btInitVisible(true);
                    btCancelVisible(true);
                    btnOpenGMapFindPassengerVisible(true);
                    btnOpenGmapGoDestinationVisible(false);
                    btPreFinishVisible(false);
                    setInfoTravel();
                    Log.d("VIAJE ESTATUS ", "1");

                    // EN CURSO //
                } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO) {
                    if(needClientPayment(currentTravel))
                    {
                        manageClientPaymentAction();
                    }
                    else{
                        manageTravelInCourse();

                    }

                    // POR ACEPTAR//
                } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.CHOFER_ASIGNADO) {
                    viewAlert = false;
                    setNotificationAndShowDialogNewTravel(currentTravel);
                    btInitVisible(false);
                    btCancelVisible(false);
                    btnOpenGMapFindPassengerVisible(false);
                    btnOpenGmapGoDestinationVisible(false);
                    btPreFinishVisible(false);
                    textTiempo.setVisibility(View.INVISIBLE);
                    // POR RECHAZADO POR OTRO CHOFER//
                } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_CHOFER) {
                    setNotificationAndShowDialogNewTravel(currentTravel);
                    btInitVisible(false);
                    btCancelVisible(false);
                    btnOpenGMapFindPassengerVisible(false);
                    btnOpenGmapGoDestinationVisible(false);
                    btPreFinishVisible(false);
                    textTiempo.setVisibility(View.INVISIBLE);
                    setBtnPauseVisible(true);

                } else if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA) {

                    dismissDialogTravel();

                    final int idTravel = currentTravel.getIdTravel();

                    Snackbar snackbar = Snackbar.make(
                            findViewById(android.R.id.content),
                            getString(R.string.viaje_cancelado).concat(" ") + currentTravel.getCodTravel()+" - "+currentTravel.getReason(),
                            30000);
                    snackbar.setActionTextColor(Color.RED);
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTypeface(null, Typeface.BOLD);
                    snackbar.setAction(R.string.confirmar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmCancelByDriver(idTravel);
                        }
                    });

                    snackbar.show();

                    btInitVisible(false);
                    btCancelVisible(false);
                    btnOpenGMapFindPassengerVisible(false);
                    btnOpenGmapGoDestinationVisible(false);
                    btPreFinishVisible(false);

                    showOrHideStreetButton(true);
                    floatingActionButton1.setEnabled(true);
                    setBtnPauseVisible(true);

                }


            } else {


                dismissDialogTravel();
                setInfoTravel();
                btInitVisible(false);
                btCancelVisible(false);
                btnOpenGMapFindPassengerVisible(false);
                btnOpenGmapGoDestinationVisible(false);
                btPreFinishVisible(false);
                textTiempo.setVisibility(View.INVISIBLE);
                currentTravel = null;
                sendCurrentTravelToService();

                // _activeTimer();
            }


            if(currentTravel == null){
                showOrHideStreetButton(true);
                floatingActionButton1.setEnabled(true);
            }else {
                showOrHideStreetButton(false);
                floatingActionButton1.setEnabled(false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manageTravelInCourse()
    {
        if(currentTravel.getIsPreFinish()==Globales.PreFinishValues.DEFAULT)
        {
            //activeTimerInit();
            btPreFinishVisible(true);
            btInitVisible(false);
            btCancelVisible(false);
            btnOpenGMapFindPassengerVisible(false);
            btnOpenGmapGoDestinationVisible(true);
            setInfoTravel();
            manageTravelByHourIfExists();
        }
        else if(currentTravel.getIsPreFinish()==Globales.PreFinishValues.APP_PREFINALIZED)
        {
            showLoadingWaitingFinishTravelFromWeb();
        }
        else if(currentTravel.getIsPreFinish()==Globales.PreFinishValues.WEB_FINALIZED)
        {
            hideLoading();
            pay();
        }
    }

    private boolean needClientPayment(InfoTravelEntity infoTravelEntityLite)
    {
        return (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO &&
                infoTravelEntityLite.getClientPaymentStatus()!= Globales.ClientPaymentStatus.NO_PAGA_CON_TARJETA &&
                infoTravelEntityLite.getClientPaymentStatus()!= Globales.ClientPaymentStatus.PAGA_CLIENTE_TIEMPO_EXPIRADO &&
                infoTravelEntityLite.getClientPaymentAmount()!=null);
    }

    private void manageClientPaymentAction()
    {
        callFinishTravelByClient();
    }

    public void confirmCancelByDriver(int idTravel) {

        if (this.daoTravel == null) {
            this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
        }


        try {
            Call<Boolean> call = this.daoTravel.confirmCancelByDriver(idTravel);

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {


                    currentTravel = null;
                    sendCurrentTravelToService();
                    setInfoTravel();
                    showOrHideStreetButton(true);
                    floatingActionButton1.setEnabled(true);

                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                }


            });

        } finally {
            this.daoTravel = null;
        }
    }

    // Enviar token al servidor //
    private void enviarTokenAlServidor(String str_token, int idUser) {
        if (idUser > 0) {
            if (this.daoLoguin == null) {
                this.daoLoguin = HttpConexion.getUri().create(ServicesLoguin.class);
            }

            try {
                token T = new token();
                T.setToken(new tokenFull(str_token, idUser, gloval.getGv_id_driver(), BuildConfig.VERSION_NAME));

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Log.d("JSON TOKEN ", gson.toJson(T));

                Call<Boolean> call = this.daoLoguin.token(T);

                call.enqueue(new Callback<Boolean>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d("Response request", call.request().toString());
                        Log.d("Response request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", String.valueOf(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));

                    }

                    public void onFailure(Call<Boolean> call, Throwable t) {


                        Log.d("ERROR", t.getMessage());
                    }
                });

            } finally {
                this.daoTravel = null;
            }
        }


    }

    ///GESTIONA EL SERVICIO SERVICESLEEP
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void iniTimeSlep() {


        if(ServicesSleep.mRunning) {
            btnPreFinish.setEnabled(true);
            btnReturn.setEnabled(true);
           // btnInitSplep.setBackgroundColor(getResources().getColor(R.color.CLEAR));

            stopService(new Intent(this, ServicesSleep.class));
            isWait(1);
            tiempoTxt =  pref.getInt("time_slepp", 0);
            textTiempo.setVisibility(View.VISIBLE);
            textTiempo.setText(Utils.covertSecondsToHHMMSS(tiempoTxt));

            Toast.makeText(getApplicationContext(), R.string.espera_desactivada, Toast.LENGTH_LONG).show();
            btnInitSplep.setText(getString(R.string.esperar));
            btnInitSplep.setTextColor(Color.parseColor("#ffffff"));
            textColorAnim.pause();

        }else {
            btnPreFinish.setEnabled(false);
            btnReturn.setEnabled(false);
            startService(new Intent(this, ServicesSleep.class));
           // btnInitSplep.setBackgroundColor(getResources().getColor(R.color.succes));
            isWait(0);
            Toast.makeText(getApplicationContext(), R.string.espera_activada, Toast.LENGTH_LONG).show();
            btnInitSplep.setText(R.string.pausar);
            textColorAnim = ObjectAnimator.ofInt(this.btnInitSplep, "textColor", Color.parseColor("#ffffff"),  Color.parseColor("#26c281"));
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
            textColorAnim.start();

        }
    }




    private BroadcastReceiver broadcastReceiverLoadTodays = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(gloval.getGv_travel_current_lite()!=null && Utils.isForegroundActivity(getApplicationContext(), "HomeChofer"))
            {
                Log.e("BROADCAST","BROADCAST RECIVED");
                if (gloval.getGv_travel_current_lite().getIdTypeTravelKf()==Globales.StatusTypeTravel.RESERVA){
                    getReservations();
                    Log.e("RESERVA","RESERVA RECIBIDA");
                }else {
                    if (isWaitingForCloseTravelByweb) {
                        isWaitingForCloseTravelByweb=false;
                    }
                    getCurrentTravelByIdDriver();
                }
            }
        }
    };

    // tiempo de espera //
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        Log.d(TAG, counter);
        Log.d(TAG, time);

       // TextView txtDateTime = (TextView) findViewById(R.id.txtDateTime);
        //TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        //txtDateTime.setText(time);
        //txtCounter.setText(counter);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setInfoTravel()
    {
        try{
            if(currentTravel != null)
            {
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (homeFragment!=null){
                    homeFragment.setInfoTravel(currentTravel);
                    setBtnPauseVisible(false);
                }
            }
            else
            {
                viewAlert = false;
                dismissDialogTravel();
                setBtnPauseVisible(true);
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (homeFragment!=null){
                    homeFragment.clearInfo();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setInfoTravelByHour(String timeElapsed)
    {
        try{
            if(currentTravel != null)
            {
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (homeFragment!=null){
                    homeFragment.setInfoTravelByHour(timeElapsed);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void clearInfoTravel(){
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (homeFragment!=null){
            homeFragment.clearInfo();
            HomeFragment.setSwitchPausarVisible(true);
        }
    }





    public void showFinshTravel() throws IOException {
        if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR == 1) {
            idPaymentFormKf = 5;
            flatParam87 = true;
        }
        pay();
    }

    public void pay()
    {
        try {
            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }
            Call<List<VehicleType>> call;
            if (currentTravel.getIsTravelComany()==1) {
                call = this.daoTravel.getVehiclesTypeByCompanyId(currentTravel.getIdClient());
            } else {
                call = this.daoTravel.getVehiclesTypeGeneral();
            }


            call.enqueue(new Callback<List<VehicleType>>() {
                @Override
                public void onResponse(Call<List<VehicleType>> call, Response<List<VehicleType>> response) {
                    if (response.isSuccessful()) {
                        try {
                            List<VehicleType> listVehicles = response.body();
                            if (listVehicles != null) {
                                setVehicleTypeAndCallCalculatePriceAndPay(listVehicles);
                            } else {
                                setVehicleTypeAndCallCalculatePriceAndPay(gloval.getGv_listvehicleType());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            setVehicleTypeAndCallCalculatePriceAndPay(gloval.getGv_listvehicleType());
                        }
                    } else {
                        setVehicleTypeAndCallCalculatePriceAndPay(gloval.getGv_listvehicleType());
                    }
                }

                @Override
                public void onFailure(Call<List<VehicleType>> call, Throwable t) {
                    t.printStackTrace();
                    setVehicleTypeAndCallCalculatePriceAndPay(gloval.getGv_listvehicleType());
                }
            });
        }catch (Exception e){
            Log.d("ERRROR",e.getMessage());
        }

    }

    private void setVehicleTypeAndCallCalculatePriceAndPay(List<VehicleType> listVehicles)
    {
        //TODO: Acá debo calcular si es viaje por hora
        //Recupero precios a partir del vehiculo
        VehicleType type = getVehicleData(currentTravel, listVehicles);
        double  priceMin;
        double pricePerKm;
        double PARAM_16_MONTO_MINIMO_VIAJE  = getParamValue(Globales.ParametrosDeApp.PARAM_16_MONTO_MINIMO_VIAJE,0d);//VALOR MINIMO DE VIAJE
        PARAM_1_PRECIO_LISTA_X_KM = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_1_PRECIO_LISTA_X_KM).getValue());// PRECIO DE LISTA
        PARAM_6  = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA).getValue());// PRECIO LISTA TIEMPO DE VUELTA

        if(type!=null)
        {
            priceMin = Utils.parseDouble(type.getVehicleValorMin());
            pricePerKm = Utils.parseDouble(type.getVehiclePriceKm());
        }
        else{
            priceMin = PARAM_16_MONTO_MINIMO_VIAJE;
            pricePerKm = PARAM_1_PRECIO_LISTA_X_KM;
        }

        calculatePriceAndPay( priceMin, pricePerKm, type);



    }

    private Double getPriceTravelByHour(VehicleType vehicleType,  String time)
    {
        Double result=0d;
        try{
            Double priceHour = Double.parseDouble(!TextUtils.isEmpty(vehicleType.getVehiclePriceHour())? vehicleType.getVehiclePriceHour() : "0");
            Double priceMin = Double.parseDouble(!TextUtils.isEmpty(vehicleType.getVehiclePriceMin())? vehicleType.getVehiclePriceMin(): "0");
            String[] timeParts = time.split(":");
            Double hours = Double.parseDouble(timeParts[0]);
            Double minutes = Double.parseDouble(timeParts[1]);
            Double seconds = Double.parseDouble(timeParts[2]);
            result = priceHour*hours+priceMin*minutes+(seconds>10?priceMin:0);
            Double priceMinimum = !TextUtils.isEmpty(vehicleType.getVehicleMinHourService()) ?  Double.parseDouble(vehicleType.getVehicleMinHourService())*priceHour : 0d;
            result = result<priceMinimum?priceMinimum:result;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    private void calculatePriceAndPay( final double priceMin, final double pricePerKm, final VehicleType vehicleType) {

        double PARAM_5  = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA).getValue());// PRECIO LISTA TIEMPO DE ESPERA
        if(currentTravel.getIsPreFinish()!=Globales.PreFinishValues.WEB_FINALIZED)
        {
            m_ida  = HomeFragment.getDistanceSafe(currentTravel.getIdTravel(),0);//BUSCAMOS LA DISTANCIA VUELTA
            extraTime = Utils.getExtraTime(PARAM_5,PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES,tiempoTxt,currentTravel);
        }
        else{
            m_ida = currentTravel.getDistanceGps();
            extraTime = Double.parseDouble(currentTravel.getAmountTimeSleep());
            tiempoTxt = Utils.convertHHmmssToSeconds(currentTravel.getTimeSleppGps());
        }

        m_ida  = HomeFragment.getDistanceSafe(currentTravel.getIdTravel(),0);//BUSCAMOS LA DISTANCIA VUELTA
        final double distancePresupuested = extractKmInText(currentTravel.getDistanceLabel());

        if(m_ida < distancePresupuested * 0.70) {
            final DistanceChooserDialog dialogChooser= new DistanceChooserDialog(
                    m_ida ,
                    distancePresupuested);

            dialogChooser.setOnClickListener(new DistanceChooserDialog.ChooseDistanceListener() {
                @Override
                public void btnAcept(double km_selected, int _isKmWithError) {
                    m_ida = km_selected;
                    isKmWithError = _isKmWithError;
                    try{
                        dialogChooser.dismissAllowingStateLoss();
                        Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogDistanceChooser");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    callFinishDialogWithPrices(priceMin, pricePerKm, vehicleType);
                }
            });

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialogChooser.setCancelable(false);
            ft.add(dialogChooser, "DialogDistanceChooser");
            ft.commitAllowingStateLoss();
        }
        else{
            isKmWithError = 0;
            callFinishDialogWithPrices(priceMin, pricePerKm, vehicleType);
        }
    }

    private void callFinishDialogWithPrices(double priceMin, double pricePerKm, VehicleType vehicleType) {
        getDataFromCompany(new IDataCompany() {
            @Override
            public void getCompanyData(CompanyData companyData) {
                FinalCalculatedPrices prices;
                prices = calculatePrices(priceMin,pricePerKm, vehicleType,m_ida, extraTime, tiempoTxt, companyData);
                Log.e("VIAJE FINAL DIALOG",currentTravel.makeJson());
                finalTravelDialog= new FinalTravelDialog(
                        currentTravel,
                        prices.getKilometrosIda(),
                        prices.getKilometrosVuelta(),
                        prices.getAmountTimeSleep(),
                        prices.getPriceFleet(),
                        prices.getBajadaDeBandera(),
                        HomeChofer.this,
                        gloval,
                        prices.getAmountCalculateGps(),
                        prices.getTimeSleepGps(),
                        prices.getTimeElpasedInTravelByHour(),
                        prices.isMinimumPrice(),
                        prices.getAmountOriginPac());

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                finalTravelDialog.setCancelable(false);
                ft.add(finalTravelDialog, "DialogFinal");
                ft.commitAllowingStateLoss();
                //finalTravelDialog.show(getSupportFragmentManager(),"DialogFinal");
            }
        });

    }

    private double extractKmInText(String value) {
        double result = 0d;
        String temp;
        try {
            temp = value.replace("KM", "");
            temp = temp.replace("km", "");
            temp = temp.replace(" ", "");
            temp = temp.replace(",", ".");
            result = Utils.parseDouble(temp);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = 0d;
        }
        return result;
    }

    private void closeFinalDialog()
    {
        try{
            if(finalTravelDialog!=null)
            {
                finalTravelDialog.dismissAllowingStateLoss();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogFinal");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private FinalCalculatedPrices calculatePrices( double priceMin, double pricePerKm, VehicleType vehicleType, double m_ida, double extraTime, int tiempoTxt, CompanyData companyData)
    {
        FinalCalculatedPrices result = new FinalCalculatedPrices();
        int PARAM_78_BAJADA_DE_BANDERA = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_78_BAJADA_DE_BANDERA).getValue());// BAJADA DE BANDERA
        double PARAM_74_PRECIO_X_HORA_AYUDANTES  = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_74_PRECIO_X_HORA_AYUDANTES).getValue());// PRECIO POR HORA AYUDANTES
        double numAsistentes  = currentTravel.isFleetTravelAssistance;//   AYUDANTES
        btPreFinishVisible(false);

        /* DITANCIA TOTAL IDA */
        kilometros_ida = Utils.round(m_ida,2) ;//LO CONVERTIMOS A KILOMETRO
        //**************************//

        /* DISTANCIA VUELTA*/
        kilometros_vuelta = getKmVuelta(kilometros_ida);

        /* DITANCIA TOTAL RECORRIDA */
        //m_total  = HomeFragment.calculateMiles(false)[0];//BUSCAMOS LA DISTANCIA TOTLA
        m_total  = kilometros_ida + kilometros_vuelta; // HomeFragment.getDistanceSafe(currentTravel.getIdTravel(),-1);//BUSCAMOS LA DISTANCIA TOTAL

        /// if(m_total > 0){
        kilometros_total = m_total;//LO CONVERTIMOS A KILOMETRO y sumamos la distancia salvada


        timeElpasedInTravelByHour="";
        PriceAndIsMinimum priceAndIsMinimum = new PriceAndIsMinimum();
        List<paramEntity> params =  Utils.getParamsFromLocalPreferences(getApplicationContext());
        float PARAM_7_PORCENTAJE_AUMENTO = Utils.getValueFloatFromParameters(params, Globales.ParametrosDeApp.PARAM_7_PORCENTAJE_AUMENTO_HORA_NOCTURNA);
        String PARAM_8_HORA_NOCTURNA_FROM = params.get(Globales.ParametrosDeApp.PARAM_8_HORA_NOCTURNA_DESDE).getValue();
        String PARAM_9_HORA_NOCTURNA_TO = params.get(Globales.ParametrosDeApp.PARAM_9_HORA_NOCTURNA_HASTA).getValue();


        if(currentTravel.getIsTravelByHour()==1)
        {
            saveFinishHourInTravelByHour();
            timeElpasedInTravelByHour = Utils.getTimeInTravelByTravelByHour(getApplicationContext());
            amounCalculateGps = getPriceTravelByHour(vehicleType, timeElpasedInTravelByHour);
        }
        else if(currentTravel.getIsTravelComany() == 1)// EMPRESA
        {
            priceAndIsMinimum = Utils.getMontoViajeEmpresa(vehicleType, currentTravel.getPriceDitanceCompany(), currentTravel.getPriceReturn(), currentTravel.getPriceHour(), currentTravel.getKmex(),
                    currentTravel.getIsTravelByHour(), gloval.getGv_hour_init_travel(), kilometros_total, currentTravel.getIsBenefitKmList(), currentTravel.getListBeneficio(),
                    kilometros_ida, kilometros_vuelta, currentTravel.getIsTravelComany(), PARAM_6, currentTravel.getAmountOriginPac(), 0d,
                    companyData != null ? companyData.getCompanyParams().getTravelMinAmount() : "0",
                    companyData != null ? companyData.getCompanyParams().getTravelMinMode() : "0",
                    companyData);
            amounCalculateGps = priceAndIsMinimum.amount;
        }
        else // PARTICULARES
        {
            currentTravel.setListBeneficio(vehicleType.getListbenefitKm()!=null && vehicleType.getListbenefitKm().size() > 0 ? vehicleType.getListbenefitKm() : currentTravel.listBeneficio);
            //Si es un viaje particular, tengo que setear la lista de beneficio que tiene esa categoría del
            //vehículo en caso de tenerla. De lo contrario, dejar el que viene por default del parametro 57
            //currentTravel.setListBeneficio(item.getListbenefitKm() != null && item.getListbenefitKm().size() > 0 ? item.getListbenefitKm() : listBeneficiosDefault);
            //PriceAndIsMinimum priceAndIsMinimum;
            priceAndIsMinimum=Utils.getMontoViajeParticular(PARAM_78_BAJADA_DE_BANDERA,
                    priceMin,
                    currentTravel,
                    kilometros_ida,
                    kilometros_vuelta,
                    kilometros_total,
                    pricePerKm,
                    PARAM_6,
                    PARAM_7_PORCENTAJE_AUMENTO,
                    PARAM_8_HORA_NOCTURNA_FROM,
                    PARAM_9_HORA_NOCTURNA_TO);

            amounCalculateGps = priceAndIsMinimum.amount;
        }

        //VALIDAMOS SI EL VIAJE NO SUPERA EL MINUMO//
        bandera = getBajadaDeBandera(vehicleType);
        priceFlet = getPriceFlet(PARAM_74_PRECIO_X_HORA_AYUDANTES, numAsistentes);

        // VALIDAMOS VIAJES PUNTO A PUNTO //Debe estar al final del cálculo, porque si es Punto a Punto debe tomar el valor del punto a punto y no calcular nada
        if(currentTravel.getIsPointToPoint() == 1) {
            //amounCalculateGps = amounCalculateGps + Double.parseDouble(currentTravel.amountCalculate); // sumamos el precio del punto a punto
            amounCalculateGps = Double.parseDouble(currentTravel.amountCalculate); // Cambio 26/07/2019 by SF: Se pidió que solo muestre el precio punto a punto.
            bandera = 0;
            kilometros_total = 0;
        }

        result.setKilometrosIda(kilometros_ida);
        result.setKilometrosVuelta(kilometros_vuelta);
        result.setAmountTimeSleep(extraTime);
        result.setPriceFleet(priceFlet);
        result.setBajadaDeBandera(bandera);
        result.setAmountCalculateGps(amounCalculateGps);
        result.setTimeSleepGps(tiempoTxt);
        result.setTimeElpasedInTravelByHour(timeElpasedInTravelByHour);
        result.setMinimumPrice(priceAndIsMinimum.isMinimum);
        result.setAmountOriginPac(priceAndIsMinimum.amountOriginPac);

        return result;
    }

    private FinalCalculatedPrices getPrefinishCalculatedPrices(VehicleType vehicleType)
    {
        FinalCalculatedPrices result = new FinalCalculatedPrices();
        double PARAM_74_PRECIO_X_HORA_AYUDANTES  = Double.parseDouble(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_74_PRECIO_X_HORA_AYUDANTES).getValue());// PRECIO POR HORA AYUDANTES
        double numAsistentes  = currentTravel.isFleetTravelAssistance;//   AYUDANTES

        result.setKilometrosIda(currentTravel.getDistanceGps());
        result.setKilometrosVuelta(getKmVuelta(result.getKilometrosIda()));
        result.setAmountTimeSleep(Double.parseDouble(currentTravel.getAmountTimeSleep()));
        result.setPriceFleet(getPriceFlet(PARAM_74_PRECIO_X_HORA_AYUDANTES, numAsistentes));
        result.setBajadaDeBandera(getBajadaDeBandera(vehicleType));
        result.setAmountCalculateGps(Double.parseDouble(currentTravel.getTotalAmount()));
        result.setTimeSleepGps(Utils.convertHHmmssToSeconds(currentTravel.getTimeSleppGps()));
        result.setTimeElpasedInTravelByHour(Utils.getTimeInTravelByTravelByHour(getApplicationContext()));
        result.setMinimumPrice(false);
        return result;
    }

    private double getKmVuelta(double kilometros_ida) {
        double result;
        try {
            /* DITANCIA TOTAL VULETA */
            m_vuelta = HomeFragment.getDistanceSafe(currentTravel.getIdTravel(), 1);//BUSCAMOS LA DISTANCIA VUELTA
            double kilometros_vuelta = m_vuelta;//LO CONVERTIMOS A KILOMETRO
            //**************************//


            if (kilometros_vuelta > 0) {
                if (kilometros_ida < kilometros_vuelta) {
                    kilometros_vuelta = Utils.round(kilometros_vuelta - kilometros_ida, 2);
                } else {
                    kilometros_vuelta = Utils.round(kilometros_ida - kilometros_vuelta, 2);
                }
            } else {
                kilometros_vuelta = 0;
            }
            result = kilometros_vuelta;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result=0d;
        }
        return result;
    }

    private double getPriceMinTravelByCompanyTravel(VehicleType vehicleType) {
        return vehicleType != null && Utils.parseDouble(vehicleType.getVehicleValorMin()) > 0.1d ? Utils.parseDouble(vehicleType.getVehicleValorMin()) : currentTravel.getPriceMinTravel();
    }

    private double getBajadaDeBandera(VehicleType vehicleType)
    {
        int PARAM_78_BAJADA_DE_BANDERA = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_78_BAJADA_DE_BANDERA).getValue());// BAJADA DE BANDERA
        double result;
        //VALIDAMOS SI EL VIAJE NO SUPERA EL MINUMO//
        if(currentTravel.getIsTravelComany() == 1)// PARA EMPRESA
        {
            double priceMinTravel = getPriceMinTravelByCompanyTravel(vehicleType);
            if(amounCalculateGps <  priceMinTravel){
                amounCalculateGps = priceMinTravel ;
            }
            result = getBanderaParaEmpresa(PARAM_78_BAJADA_DE_BANDERA, priceMinTravel);
        }
        else{
            result=0;
        }
        return  result;
    }


    private VehicleType getVehicleData(InfoTravelEntity currentTravelData,List<VehicleType> listVehicles) {
        VehicleType result = null;
        try {
            int vehicleType = currentTravelData.getIdTypeVehicle();
            if (listVehicles != null && listVehicles.size() > 0) {
                for (int i = 0; i < listVehicles.size(); i++) {
                    if (listVehicles.get(i).getIdVehicleType() == vehicleType) {
                        result = listVehicles.get(i);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public double getMontoViajeEmpresa(VehicleType vehicleType)
    {

        double pricePerKm =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceKm())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceKm()):currentTravel.getPriceDitanceCompany();
        double pricePerKmReturn =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceKmDR())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceKmDR()):currentTravel.getPriceReturn();
        double priceHour =vehicleType!=null && Utils.parseDouble(vehicleType.getVehiclePriceHour())>0.1d ?Utils.parseDouble(vehicleType.getVehiclePriceHour()):currentTravel.getPriceHour();
        double pricePerKmExedente =vehicleType!=null && Utils.parseDouble(vehicleType.getPricePerKmEx())>0.1d ?Utils.parseDouble(vehicleType.getPricePerKmEx()):currentTravel.getPricePerKmex();
        double kmExcedente =vehicleType!=null && Utils.parseDouble(vehicleType.getKmEx())>0.1d ?Utils.parseDouble(vehicleType.getKmEx()):currentTravel.getKmex();

        double result=0d;
        /*VERIFICAMOS SI ES VIAJE POR HORA*/
        if(currentTravel.getIsTravelByHour() == 1) {
            int hours = new Time(System.currentTimeMillis()).getHours();
            int newHours = hours - gloval.getGv_hour_init_travel();

            if(newHours <1 || gloval.getGv_hour_init_travel() == 0) {
                newHours = 1;
            }

            if(kmExcedente >0 && kilometros_total > kmExcedente){ // KILOMETROS EXEDENTES
                double kilometros_extra = kmExcedente - kilometros_total;
                result = (newHours * priceHour) +  (kilometros_extra * pricePerKmExedente);
            }else {
                result = newHours * priceHour;
            }
        }
        else{
            /*VERIFICAMOS SI ESTA ACTIVO EL CAMPO BENEFICIO POR KILOMETRO PARA ESA EMPRESA*/
            Log.d("-TRAVEL Beneficio-", "tiene beneficios?:"+String.valueOf(currentTravel.getIsBenefitKmList()));
            if (currentTravel.getIsBenefitKmList() == 1
                    &&currentTravel.getListBeneficio() != null
                    && currentTravel.getListBeneficio().size() > 0) {

                Log.d("-TRAVEL kilometros_total-", String.valueOf(kilometros_total));
                Log.d("-TRAVEL BenefitsToKm-", String.valueOf(currentTravel.getBenefitsToKm()));
                Log.d("-TRAVEL BenefitsFromKm-", String.valueOf(currentTravel.getBenefitsFromKm()));

                /* VERIFICAMOS SI ESTA DENTRO DE EL RANGO DE EL BENEFICIO ESTABLECIDO */
                result = this.getPriceBylistBeneficion(currentTravel.getListBeneficio(),kilometros_ida);

                // VERIFICAMOS SI HAY RETORNO //
                if (HomeChofer.isRoundTrip == 1) {
                    result = result + this.getPriceReturnBylistBeneficion(currentTravel.getListBeneficio(),kilometros_vuelta);
                }

                if(result < 1){
                    result = kilometros_total * pricePerKm;
                    Log.d("-TRAVEL result (2)-", String.valueOf(result));
                }
            } else {
                result = kilometros_total * pricePerKm;// PARA CLIENTES EMPREA BUSCAMOS EL PRECIO DE ESA EMPRESA

                Log.d("-TRAVEL result (3)-", String.valueOf(result));

                if (HomeChofer.isRoundTrip == 1) {
                    Log.d("-TRAVEL isRoundTrip -", String.valueOf(HomeChofer.isRoundTrip));
                    result = kilometros_ida * pricePerKm;
                    Log.d("-TRAVEL result (4)-", String.valueOf(result));
                    result = result + kilometros_vuelta * pricePerKmReturn;
                    Log.d("-TRAVEL result (5)-", String.valueOf(result));
                }
            }
        }
        return result;
    }


    private Double getBanderaParaEmpresa(int param78_BajadaDeBandera, double priceMinTravel)
    {
        double result = 0d;
        if(param78_BajadaDeBandera > 0) {
            if(currentTravel.getIsTravelComany() == 1){// EMPRESA
                if(param78_BajadaDeBandera == 1){
                    result = priceMinTravel;
                }else if(param78_BajadaDeBandera == 2) {
                    if(amounCalculateGps <  priceMinTravel) {
                        result = priceMinTravel;
                    }
                }
            }else{
                //si es viaje particular, la bandera ya se incluye en el precio calculado (amounCalculateGps)
                result=0;
            }
        }
        return result;
    }

    private double getPriceFlet(double PARAM_74,double numAsiste)
    {
        double result=0d;
        if(currentTravel.getIsFleetTravelAssistance() > 0){

            int hours = new Time(System.currentTimeMillis()).getHours();
            int newHours = hours - gloval.getGv_hour_init_travel();

            if(newHours <1 || gloval.getGv_hour_init_travel() == 0){
                newHours = 1;
            }

            priceFlet = (PARAM_74 * newHours) * numAsiste;
        }

        return result;
    }

    private double getExtraTime2(double PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA){
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
            Log.d("-TRAVEL min extraTime COMPANY-", String.valueOf(extraTime));
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    /*  modificar notificacione  */
    private void updateNotificationsBadge(int couterNotifications,int couterReservations) {
        mNotificationsCount = couterNotifications;
        mNotificationsReservationsCount = couterReservations;
        invalidateOptionsMenu();

        if(couterReservations >0) {
            Toast.makeText(getApplicationContext(), getString(R.string.tienes_x_reservas).replace("[CANT]", String.valueOf(couterReservations)), Toast.LENGTH_LONG).show();
        }
    }







    private void fn_verificateConexion() {

        if(Utils.verificaConexion(this)) {
            _NOCONEXION = false;
        }else
        {
            if(!_NOCONEXION) {
                showAlertNoConexion();
                _NOCONEXION = true;
            }
        }
    }

    private void fn_refhesh() {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            loadParamsFromApi();
            this.getCurrentTravelByIdDriver();
        }
    }

    public  void  fn_gotoprofile()
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            FragmentManager fm = getSupportFragmentManager();

            // VERIFICAMO SI E UN CHOFER //
            if (gloval.getGv_id_profile() == 3) {
                fm.beginTransaction().replace(R.id.content_frame, new ProfileDriverFr()).commitAllowingStateLoss();
            }
            // VERIFICAMO SI E UN CLIENTE PÁRTICULAR//
            else if (gloval.getGv_id_profile() == 2) {
                fm.beginTransaction().replace(R.id.content_frame, new ProfileClientFr()).commitAllowingStateLoss();
            }
        }
    }


    private void  fn_chat(){
        if( Utils.verificaConexion(this) == false) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, FragmentChatOperators.newInstance(1)).commitAllowingStateLoss();
        }
    }

    private void fn_reporte(){
        if( Utils.verificaConexion(this) == false) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new FragmentReporte()).commitAllowingStateLoss();
        }
    }

    public void fn_gotonotification()
    {
        if( Utils.verificaConexion(this) == false) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NotificationsFrangment()).commitAllowingStateLoss();
        }
    }

    public void fn_gotoreservation()
    {
        if( Utils.verificaConexion(this) == false) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            FragmentManager fm = getSupportFragmentManager();
            ReservationsFrangment fragment = new ReservationsFrangment();
            fragment.attachReservationListener(new ListenerReservationUpdate() {
                @Override
                public void updateAppBarNotifications() {
                    getReservations();
                }
            });
            fm.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
        }
    }



    public void _activeTimer()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("TIMER","TIMER 1");
                        setLocationVehicheDriver();
                    }
                });

            }
        }, 0, 6000);


        timerConexion = new Timer();
        timerConexion.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                //Log.d("TIMER","TIMER 2");
                                fn_verificateConexion();
                            }
                        });

            }
        }, 0, 30000);

    }

    @SuppressLint("LongLogTag")
    public  void setLocationVehicheDriver()
    {
        if (currentTravel != null ) {
            if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER ||
                    currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO
            ) {
                if (this.daoTravel == null) {
                    this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
                }

                try {
                    String lat = "";
                    String lon = "";
                    String add = "";
                    if (HomeFragment.getmLastLocation() != null) {
                        lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
                        lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
                        add = HomeFragment.nameLocation;
                    }

                    if (!add.equals("")) {
                        int idTrave = 0;
                        int idClientKf = 0;
                        int idUserCompanyKf=0;
                        if (currentTravel != null) {
                            idTrave = currentTravel.getIdTravel();
                            idClientKf = currentTravel.getIdClientKf();
                            idUserCompanyKf = currentTravel.getIdUserCompanyKf();
                        }

                        TraveInfoSendEntity travel =
                                new TraveInfoSendEntity(new
                                        TravelLocationEntity(
                                        gloval.getGv_user_id(),
                                        idTrave,
                                        add,
                                        lon,
                                        lat,
                                        gloval.getGv_id_driver(),
                                        gloval.getGv_id_vehichle(),
                                        idClientKf,
                                        HomeFragment.calculateMiles()[0],
                                        kilometros_vuelta,
                                        idUserCompanyKf,
                                        currentTravel.getIdSatatusTravel()
                                )
                                );
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        Log.d("setLocationVehicheDriver", gson.toJson(travel));
                        Call<Boolean> call = this.daoTravel.sendPosition(travel);
                        call.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                 if (response.code() == 200) {
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.d("**ERROR**", t.getMessage());
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("setLocationVehicheDriver", e.getMessage());
                } finally {
                    this.daoTravel = null;
                }

            }
        }
    }


    public void notificate(List<notification> list,List<InfoTravelEntity> listReservations)
    {

        int couterNotifications = 0;
        int couterRervations = 0;

        if(list != null)
        {
            couterNotifications = list.size();
        }

        if(listReservations != null)
        {
            couterRervations = listReservations.size();
        }

        updateNotificationsBadge(couterNotifications,couterRervations);
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setNotificationAndShowDialogNewTravel(final InfoTravelEntity travel)
    {
        Log.d("currentTravel  full", String.valueOf(viewAlert));

        if(viewAlert == false)
        {
            currentTravel =  travel;
            sendCurrentTravelToService();
            gloval.setGv_travel_current(currentTravel);

            showDialogNewTravel();// MOSTRAMO EL FRAGMENT DIALOG

        }
    }

    // RECHAZAR VIAJE //
    public void cancelTravel(int idTravel)
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            if (idTravel == -1) {
                idTravel = gloval.getGv_travel_current().idTravel;
            }

            try {

                loading = ProgressDialog.show(HomeChofer.this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);


                Call<InfoTravelEntity> call = this.daoTravel.refuse(idTravel);


                call.enqueue(new Callback<InfoTravelEntity>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {

                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.viaje_rechazado, Toast.LENGTH_LONG).show();


                        dismissDialogTravel();

                        clearNotificationAndoid();
                        // viewAlert = false;
                        gloval.setGv_travel_current(null);
                        currentTravel = null;
                        sendCurrentTravelToService();
                        btCancelVisible(false);
                        btInitVisible(false);
                        btnOpenGMapFindPassengerVisible(false);
                        btnOpenGmapGoDestinationVisible(false);
                        clearInfoTravel();
                        viewAlert = false;
                    }

                    public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                        loading.dismiss();

                        Snackbar.make(findViewById(android.R.id.content),
                                "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                    }


                });

            } finally {
                this.daoTravel = null;
            }
        }
    }

    public  void btCancelVisible(boolean b)
    {


        if(b)
        {
            btn_cancel.setVisibility(View.VISIBLE);
            if(PARAM_66_VER_PRECIO_VIAJE_EN_APP == 1){
                btn_cancel.setEnabled(true);
            }else {
                btn_cancel.setEnabled(false);
            }

        }else {
            btn_cancel.setVisibility(View.INVISIBLE);
        }
    }

    public  void btInitVisible(boolean b)
    {

        if(b)
        {
            btn_init.setVisibility(View.VISIBLE);
        }else {
            btn_init.setVisibility(View.INVISIBLE);
        }
    }

    public  void btPreFinishVisible(boolean b)
    {

        if(b)
        {
            this.btnLogin.setVisibility(View.VISIBLE);
            this.btnInitSplep.setVisibility(View.VISIBLE);
            this.btn_return.setVisibility(View.VISIBLE);
        }else {
            this.btnLogin.setVisibility(View.INVISIBLE);
            this.btnInitSplep.setVisibility(View.INVISIBLE);
            this.btn_return.setVisibility(View.INVISIBLE);
        }
    }

    public  void btnOpenGMapFindPassengerVisible(boolean b)
    {
        if(b)
        {
            btnOpenGMapFindPassenger.setVisibility(View.VISIBLE);
            btnOpenWazeFindPassenger.setVisibility(View.VISIBLE);
        }else {
            btnOpenGMapFindPassenger.setVisibility(View.GONE);
            btnOpenWazeFindPassenger.setVisibility(View.GONE);
        }
    }

    public void btnOpenGmapGoDestinationVisible(boolean isVisible)
    {
        if(isVisible)
        {
            btnOpenGMapGoDestination.setVisibility(View.VISIBLE);
            btnOpenWazeGoDestination.setVisibility(View.VISIBLE);
        }else{
            btnOpenGMapGoDestination.setVisibility(View.GONE);
            btnOpenWazeGoDestination.setVisibility(View.GONE);
        }
    }


    /* SERVICIO PARA RETORNAR UN VIAJE EN EL MONITOR DDE VIAJES */
    public  void  isRoundTrip()
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            try {
                Call<Boolean> call = this.daoTravel.isRoundTrip(currentTravel.idTravel);

                Log.d("fatal", call.request().toString());
                Log.d("fatal", call.request().headers().toString());

                call.enqueue(new Callback<Boolean>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        HomeChofer.isRoundTrip = 1;
                        Toast.makeText(getApplicationContext(), R.string.vuelta_activada, Toast.LENGTH_LONG).show();
                        btn_return.setVisibility(View.INVISIBLE);
                        currentTravel.setIsRoundTrip(1);
                        setInfoTravel();
                    }

                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                    }


                });

            } finally {
                this.daoTravel = null;
            }
        }
    }

    /* SERVICIO PARA VERIFICA SI EL VIAJE NO SE FINALIZON ANTES  */
    public  void  verifickTravelFinish()
    {

        Log.e("VERIFICAR PAGO FINAL ","SI");

        loading = ProgressDialog.show(HomeChofer.this, getString(R.string.verificando_viaje), getString(R.string.espere_unos_segundos), true, false);


        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }

            try {

                if(currentTravel != null) {

                    Call<Boolean> call = this.daoTravel.verifickTravelFinish(currentTravel.idTravel);

                    call.enqueue(new Callback<Boolean>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            closeFinalDialog();
                            loading.cancel();
                            boolean result = response.body();

                            if (result) {
                                showMessage(getString(R.string.viaje_finalizado_previamente),1);
                                //borrarMarketCliente();
                                //borrarMarketDestino();
                                clearFinish();
                            } else {
                                finishTravel();
                            }

                        }

                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.e("Verificar Viaje OnFailura",t.toString());
                            loading.cancel();
                           closeFinalDialog();
                            showMessage(getString(R.string.fallo_general),3);

                        }


                    });
                }else {
                    closeFinalDialog();
                    loading.cancel();

                    showMessage(getString(R.string.viaje_finalizado),1);
                    clearFinish();
                }

            } finally {
                this.daoTravel = null;
            }
        }
    }

    /* SERVICIO PARA VERIFICA SI EL VIAJE NO SE CANCELO ANTES  */
    public  void  verifickTravelCancel(final int idTravel)
    {

        loading = ProgressDialog.show(HomeChofer.this, getString(R.string.verificando_viaje), getString(R.string.espere_unos_segundos), true, false);


        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            try {

                if(currentTravel != null) {

                    Call<Boolean> call = this.daoTravel.verifickTravelCancel(idTravel);

                    call.enqueue(new Callback<Boolean>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            boolean result = response.body();
                            if (result) {
                                loading.cancel();
                                showMessage(getString(R.string.viaje_finalizado_previamente),0);
                                fn_refhesh();
                            } else {
                                loading.cancel();
                                aceptTravel(idTravel);// ACEPTAMOS EL VIAJE
                            }

                        }

                        public void onFailure(Call<Boolean> call, Throwable t) {
                            loading.cancel();
                            showMessage(getString(R.string.fallo_general),3);
                        }


                    });
                }else {
                    loading.cancel();
                    showMessage(getString(R.string.viaje_finalizado_previamente),0);
                    clearFinish();
                }

            } finally {
                this.daoTravel = null;
            }
        }
    }


    /* SERVICIO PARA VERIFICA SI EL VIAJE NO SE CANCELO ANTES para poder iniciar  */
    public  void  verifickTravelCancelInit()
    {

            loading = ProgressDialog.show(HomeChofer.this, getString(R.string.verificando_viaje), getString(R.string.espere_unos_segundos), true, false);


            if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

                if (this.daoTravel == null) {
                    this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
                }

                try {

                    if(currentTravel != null) {

                        Call<Boolean> call = this.daoTravel.verifickTravelCancel(currentTravel.idTravel);

                        Log.d("fatal", call.request().toString());
                        Log.d("fatal", call.request().headers().toString());

                        call.enqueue(new Callback<Boolean>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                boolean result = response.body();
                                if (result) {
                                    loading.cancel();
                                    showMessage(getString(R.string.viaje_cancelado_previamento),0);
                                    fn_refhesh();
                                } else {
                                    loading.cancel();
                                    initTravel();// INICIAMOS EL VIAJE
                                }

                            }

                            public void onFailure(Call<Boolean> call, Throwable t) {
                                loading.cancel();
                                showMessage(getString(R.string.fallo_general),3);
                            }


                        });
                    }else {
                        loading.cancel();

                        showMessage(getString(R.string.viaje_finalizado_previamente),0);
                        clearFinish();
                    }

                } finally {
                    this.daoTravel = null;
                }
        }
    }

    public void clearFinish(){
        btInitVisible(false);
        btCancelVisible(false);
        btnOpenGMapFindPassengerVisible(false);
        btnOpenGmapGoDestinationVisible(false);
        btPreFinishVisible(false);

        currentTravel = null;
        sendCurrentTravelToService();
        if (HomeFragment.options != null) {
            HomeFragment.options.getPoints().clear();
        }
        gloval.setGv_travel_current(null);
        setInfoTravel();
        viewAlert = false;

        tiempoTxt = 0;
        textTiempo.setVisibility(View.INVISIBLE);
        extraTime = 0;
        editor.putInt("time_slepp", 0);
        editor.commit(); // commit changes

        gloval.setGv_hour_init_travel(0);// GUARDAMOS LA HORA QUE LO INICIO
    }

    /* SERVICIO PARA INDICAR ESPERA DE UN VIAJE EN EL MONITOR DDE VIAJES */
    public  void  isWait(int value)
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            try {
                Call<Boolean> call = this.daoTravel.isWait(currentTravel.idTravel, value);

                Log.d("fatal", call.request().toString());

                call.enqueue(new Callback<Boolean>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        setInfoTravel();

                    }

                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                    }


                });

            } finally {
                this.daoTravel = null;
            }
        }
    }

    public  void  getCurrentTravelByIdDriver()
    {
        if(!Utils.verificaConexion(this)) {
            showAlertNoConexion();
            setBtnPauseVisible(false);
        }else {
            setBtnPauseVisible(true);
            if (daoTravel == null) {
                daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }

            int idDriver = gloval.getGv_id_driver();

            if (idDriver == 0) {
                idDriver = pref.getInt("driver_id", 0);
                gloval.setGv_id_driver(idDriver);
            }

            daoTravel.getCurrentTravelByIdDriver(idDriver).enqueue(new Callback<InfoTravelEntity>() {
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                    try {
                        InfoTravelEntity TRAVEL = response.body();
                        gloval.setGv_travel_current(TRAVEL);
                        currentTravel = gloval.getGv_travel_current();
                        sendCurrentTravelToService();
                        controlViewTravel();

                    }catch (Exception e){
                        Log.e("ERROR getCurrentTravelByIdClient",e.toString());
                        showMessage(getString(R.string.fallo_general),3);
                    }
                }

                @Override
                public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                    Log.e("ONFAILURE getCurrentTravelByIdClient",t.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            });
            getReservations();
        }
    }

    /* METODO PARA ACEPATAR EL VIAJE*/
    public  void  aceptTravel(int idTravel)
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            try {
                loading = ProgressDialog.show(HomeChofer.this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);
                Call<InfoTravelEntity> call = this.daoTravel.accept(idTravel);
                call.enqueue(new Callback<InfoTravelEntity>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                        loading.dismiss();
                        Log.d("HOMECHOFER","VIAJE ACEPTADO");
                        btInitVisible(true);
                        btCancelVisible(true);
                        btnOpenGMapFindPassengerVisible(true);
                        btnOpenGmapGoDestinationVisible(false);
                        clearNotificationAndoid();
                        viewAlert = true;
                        dismissDialogTravel();
                        currentTravel = response.body();
                        sendCurrentTravelToService();
                        getCurrentTravelByIdDriver();
                        //setInfoTravel();

                        showOrHideStreetButton(false);
                        floatingActionButton1.setEnabled(false);
                    }

                    public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                        loading.dismiss();

                        Snackbar.make(findViewById(android.R.id.content),
                                "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                    }


                });

            } finally {

                this.daoTravel = null;
            }
        }
    }

    private void saveInitHourInTravelByHour() {
        try {
            if (currentTravel != null) {
                if(currentTravel.getIsTravelByHour()==1) {
                    Date currentTime = Calendar.getInstance().getTime();
                    String timeStart = String.valueOf(currentTime.getTime());
                    Utils.saveValueToSharedPreference(getApplicationContext(),Globales.KEY_FOR_INIT_TIME_TRAVEL_BY_HOUR, timeStart);
                    Utils.saveValueToSharedPreference(getApplicationContext(),Globales.KEY_FOR_END_TIME_TRAVEL_BY_HOUR, "");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveFinishHourInTravelByHour() {
        try {
            if (currentTravel != null && currentTravel.getIsTravelByHour()==1) {
                String endSavedTime = Utils.getValueFromSharedPreferences(getApplicationContext(), Globales.KEY_FOR_END_TIME_TRAVEL_BY_HOUR);
                if(TextUtils.isEmpty(endSavedTime)) {
                    Date endTime = Calendar.getInstance().getTime();
                    String timeEndString = String.valueOf(endTime.getTime());
                    Utils.saveValueToSharedPreference(getApplicationContext(),Globales.KEY_FOR_END_TIME_TRAVEL_BY_HOUR, timeEndString);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void  manageTravelByHourIfExists()
    {
        try {
            if (currentTravel.getIsTravelByHour() == 1) {
                if(timeTravelByHour==null)
                {
                    timeTravelByHour = new Timer();
                    timeTravelByHour.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String time = Utils.getTimeInTravelByTravelByHour(getApplicationContext());
                                    setInfoTravelByHour(time);
                                    Log.e("TIME", time);
                                }
                            });

                        }
                    }, 0, 1000);
                }
                else{
                    //cancalTimerTravelByHour();
                    //manageTravelByHourIfExists();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public  void  initTravel()
    {
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            if (this.validarGPS()) {
                if (this.checkDistanceSucces()) {

                    try {
                        loading = ProgressDialog.show(HomeChofer.this, getString(R.string.enviado), getString(R.string.espere_unos_segundos), true, false);

                        Call<InfoTravelEntity> call = this.daoTravel.init(currentTravel.getIdTravel());

                        call.enqueue(new Callback<InfoTravelEntity>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {

                                loading.dismiss();
                                btInitVisible(false);
                                btCancelVisible(false);
                                btnOpenGMapFindPassengerVisible(false);
                                btnOpenGmapGoDestinationVisible(false);
                                btPreFinishVisible(true);
                                saveInitHourInTravelByHour();
                                currentTravel = response.body();
                                sendCurrentTravelToService();
                                getCurrentTravelByIdDriver();
                                setInfoTravel();
                                //activeTimerInit();
                            }

                            public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                                loading.dismiss();
                                Snackbar.make(findViewById(android.R.id.content),
                                        "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                            }
                        });
                        int hours = new Time(System.currentTimeMillis()).getHours();
                        Log.d("hours", String.valueOf(hours));
                        gloval.setGv_hour_init_travel(hours);// GUARDAMOS LA HORA QUE LO INICIO
                    } finally {
                        this.daoTravel = null;
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content),
                            R.string.error_debe_aproximarse_mas, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }


    /*LLAMAMOS A LA PATALLA DE BOUCHE*/
    public void finishTravelVouche() {

        closeFinalDialog();

        if(HomeChofer.currentTravel != null){
            if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
                Intent intent = new Intent(getApplicationContext(), Signature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
            }
        }else {
            clearFinish();
        }

    }


    //region LISTENER
    @Override
    public void finalizarViajeEfectivo(double montoTotal,double peajeMonto_,double estacionamientoMonto_, int waitingTime, double waitingPrice) {
        totalFinal= montoTotal;
        peajeMonto= peajeMonto_;
        estacionamientoMonto= estacionamientoMonto_;
        extraTime=waitingPrice;
        tiempoTxt=waitingTime;

        idPaymentFormKf = 4;
        if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR == 1 && !flatParam87){
            finishTravel();
        }else{
            verifickTravelFinish();
        }
    }

    @Override
    public void finalizarViajeTarjeta(double montoTotal,double peajeMonto_,double estacionamientoMonto_, int waitingTime, double waitingPrice, boolean mustPayClient) {
        extraTime=waitingPrice;
        tiempoTxt=waitingTime;
        openActivityfinalizarViajeTarjeta(montoTotal,peajeMonto_,estacionamientoMonto_,false, mustPayClient);
    }

    private void callFinishTravelByClient(){
        double amountToll = TextUtils.isEmpty(currentTravel.getAmountToll()) ? Utils.parseDouble(currentTravel.getAmountToll()) : 0;
        double amountParking = TextUtils.isEmpty(currentTravel.getAmountParking()) ? Utils.parseDouble(currentTravel.getAmountParking()) : 0;
        openActivityfinalizarViajeTarjeta(currentTravel.getClientPaymentAmount(),amountToll,amountParking,true, true);
    }

    private Double getTotalAmountWithExtraFee(double montoTotal)
    {
        Double result = 0d;
        try{
            String porcentajeAdicional = gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_96_PORCENTAJE_ADICIONAL_POR_PAGO_CON_TARJETA).getValue();
            if(!TextUtils.isEmpty(porcentajeAdicional) && !"0".equals(porcentajeAdicional)) {
                result = (montoTotal * Double.parseDouble(porcentajeAdicional) / 100);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private void openActivityfinalizarViajeTarjeta(double montoTotal,double peajeMonto_,double estacionamientoMonto_, boolean clientHasFinish, boolean mustPayClient){

        totalFinal= montoTotal;
        double ePayDiffAmount = getTotalAmountWithExtraFee(montoTotal);
        peajeMonto= peajeMonto_;
        estacionamientoMonto= estacionamientoMonto_;

        idPaymentFormKf = 3;

        String lat = "";
        String lon = "";
        String add = "";

        if (HomeFragment.getmLastLocation() != null) {
            lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
            lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
            add = HomeFragment.nameLocation;
        }

        Intent intent;
        if(mustPayClient)
        {
            intent = new Intent(getApplicationContext(), CallClientPayWithCard.class);
        }
        else{
            intent = new Intent(getApplicationContext(), PayCreditCard.class);
        }

        //Intent intent = new Intent(getApplicationContext(), CallClientPayWithCard.class);
        intent.putExtra("TotalAmount",totalFinal);
        intent.putExtra("amounCalculateGps",amounCalculateGps);
        intent.putExtra("bandera",bandera);
        intent.putExtra("current",currentTravel.makeJson());
        intent.putExtra("keyMercado", PARAM_69_KEY_MP);
        intent.putExtra("keyMercadoPrivado",PARAM_79);
        intent.putExtra("kilometros_total",kilometros_total);
        intent.putExtra("param_3", PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB);
        intent.putExtra("param_67", PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        intent.putExtra("add",add);
        intent.putExtra("kilometros_vuelta",kilometros_vuelta);
        intent.putExtra("peajeMonto",peajeMonto);
        intent.putExtra("estacionamientoMonto",estacionamientoMonto);
        intent.putExtra("priceFlet",priceFlet);
        intent.putExtra("extraTime",extraTime);
        intent.putExtra("tiempoTxt",tiempoTxt);
        intent.putExtra("PAYMENT_CARD_PROVIDER", PARAM_103_PAYMENT_CARD_PROVIDER);
        intent.putExtra("CLIENT_HAS_FINISH", clientHasFinish);
        intent.putExtra("ePayDiffAmount",ePayDiffAmount);
        intent.putExtra("isKmWithError",isKmWithError);



        startActivity(intent);
        finish();
    }


    public void finalizarViajeTarjeta_back(double montoTotal,double peajeMonto_,double estacionamientoMonto_) {
        totalFinal= montoTotal;
        peajeMonto= peajeMonto_;
        estacionamientoMonto= estacionamientoMonto_;

        idPaymentFormKf = 3;

        String lat = "";
        String lon = "";
        String add = "";

        if (HomeFragment.getmLastLocation() != null) {
            lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
            lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
            add = HomeFragment.nameLocation;
        }

        Intent intent = new Intent(getApplicationContext(), PayCreditCard.class);
        intent.putExtra("TotalAmount",totalFinal);
        intent.putExtra("amounCalculateGps",amounCalculateGps);
        intent.putExtra("current",currentTravel.makeJson());
        intent.putExtra("keyMercado", PARAM_69_KEY_MP);
        intent.putExtra("keyMercadoPrivado",PARAM_79);
        intent.putExtra("kilometros_total",kilometros_total);
        intent.putExtra("param_3", PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB);
        intent.putExtra("param_67", PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        intent.putExtra("add",add);
        intent.putExtra("kilometros_vuelta",kilometros_vuelta);
        intent.putExtra("peajeMonto",peajeMonto);
        intent.putExtra("estacionamientoMonto",estacionamientoMonto);
        intent.putExtra("priceFlet",priceFlet);
        intent.putExtra("extraTime",extraTime);
        intent.putExtra("tiempoTxt",tiempoTxt);
        intent.putExtra("PAYMENT_CARD_PROVIDER", PARAM_103_PAYMENT_CARD_PROVIDER);


        startActivity(intent);
        finish();
    }

    @Override
    public void finalizarViajeFirma(double montoTotal,double peajeMonto_,double estacionamientoMonto_, int waitingTime, double waitingPrice) {
        totalFinal= montoTotal;
        peajeMonto= peajeMonto_;
        estacionamientoMonto= estacionamientoMonto_;
        extraTime=waitingPrice;
        tiempoTxt=waitingTime;

        idPaymentFormKf = 5;
        finishTravelVouche();
    }

    @Override
    public void finalizarViajeWeb(double montoTotal, double _peajeMonto, double _estacionamientoMonto, int waitingTime, double waitingPrice) {
        totalFinal= montoTotal;
        peajeMonto= _peajeMonto;
        estacionamientoMonto= _estacionamientoMonto;
        extraTime=waitingPrice;
        tiempoTxt=waitingTime;

        idPaymentFormKf = 5;
        if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR == 1 && !flatParam87){
            finishTravel();
        }else{
            verifickTravelFinish();
        }
    }


    /* METODO PARA FINALIZAR O PREFINALIZAR  UN VIAJE*/
    public  void  finishTravel() {
        Log.e("ENTRO FINISH TRAVEL","SI");
        if(!Utils.verificaConexion(this)) {showAlertNoConexion();}else { // VERIFICADOR DE CONEXION
            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }
            try {
                if (currentTravel != null) {
                    String lat = "";
                    String lon = "";
                    String add = "";
                    if (HomeFragment.getmLastLocation() != null) {
                        lat = String.valueOf(HomeFragment.getmLastLocation().getLatitude());
                        lon = String.valueOf(HomeFragment.getmLastLocation().getLongitude());
                        add = HomeFragment.nameLocation;
                    }


                    double _RECORIDO_TOTAL = 0;
                    if (kilometros_total > 0) {
                        _RECORIDO_TOTAL = Utils.round(kilometros_total, 2);
                    }

                    String durationInTravelByHour = currentTravel.getIsTravelByHour()==1 ? Utils.getTimeInTravelByTravelByHour(getApplicationContext()) : "";
                    Double finalIVA = 0d;//TODO: debo agregar el cálculo del IVA

                    TraveInfoSendEntity travel =
                            new TraveInfoSendEntity(new
                                    TravelLocationEntity
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
                                            idPaymentFormKf,
                                            mp_jsonPaymentCard,
                                            "",
                                            "",
                                            "",
                                            priceFlet,
                                            Utils.round(kilometros_vuelta,2),
                                            durationInTravelByHour,
                                            finalIVA,
                                            0d,
                                            "",
                                            isKmWithError
                                    )
                            );
                    Call<InfoTravelEntity> call = null;
                    /* VERIFICAMOS I ESTA HABILITADO EL CIERRE DE VIAJES DEDE LA APP O NO*/
                    int evaluationOperator;
                    final int isTravelComany = currentTravel.isTravelComany;
                    if (isTravelComany == 1) {
                        evaluationOperator = PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB;
                    } else {
                        evaluationOperator = PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR;// PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB;
                    }

                    if (evaluationOperator == 0 || PARAM_18 == 1 || currentTravel.getIsPreFinish()==Globales.PreFinishValues.WEB_FINALIZED) {
                        call = this.daoTravel.finishPost(travel);
                    } else {
                        call = this.daoTravel.preFinishMobil(travel);
                    }

                    hideLoading();
                    loading = ProgressDialog.show(HomeChofer.this, getString(R.string.finalizando_viaje), getString(R.string.espere_unos_segundos), true, false);

                    call.enqueue(new Callback<InfoTravelEntity>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {
                            closeFinalDialog();
                            int isPrefinishTravel = currentTravel.getIsPreFinish();
                            if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR == 1){
                                if (!flatParam87 || isPrefinishTravel==Globales.PreFinishValues.WEB_FINALIZED){
                                    currentTravel = null;
                                    sendCurrentTravelToService();
                                    gloval.setGv_travel_current(null);
                                    gloval.setGv_hour_init_travel(0);// GUARDAMOS LA HORA QUE LO INICIO
                                    showOrHideStreetButton(true);
                                    floatingActionButton1.setEnabled(true);
                                }else{
                                    showOrHideStreetButton(false);
                                    floatingActionButton1.setEnabled(false);
                                }
                            }else{
                                currentTravel = null;
                                sendCurrentTravelToService();
                                gloval.setGv_travel_current(null);
                                gloval.setGv_hour_init_travel(0);
                                showOrHideStreetButton(true);
                                floatingActionButton1.setEnabled(true);
                            }

                            int evaluationOperator;
                            if (isTravelComany == 1) {
                                evaluationOperator = PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB;
                            } else {
                                evaluationOperator = PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR; //PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB;
                            }

                            if(evaluationOperator == 1 && isPrefinishTravel==Globales.PreFinishValues.DEFAULT)
                            {
                                showMessage(getString(R.string.viaje_enviando_aprobacion),1);
                                showLoadingWaitingFinishTravelFromWeb();
                            }
                            else{
                                hideLoading();
                                showMessage(getString(R.string.viaje_finalizado),1);
                                Utils.resetDurationForTravelByHour(getApplicationContext());
                            }

                            btInitVisible(false);
                            btCancelVisible(false);
                            btnOpenGMapFindPassengerVisible(false);
                            btnOpenGmapGoDestinationVisible(false);
                            btPreFinishVisible(false);

                            if (HomeFragment.options != null) {
                                HomeFragment.options.getPoints().clear();
                            }
                            setInfoTravel();
                            viewAlert = false;
                            tiempoTxt = 0;
                            textTiempo.setVisibility(View.INVISIBLE);
                            extraTime = 0;
                            editor.putInt("time_slepp", 0);
                            editor.commit(); // commit changes

                        }

                        public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                            loading.dismiss();
                            closeFinalDialog();

                            showMessage(getString(R.string.fallo_general),3);

                        }
                    });
                } else {
                    loading.dismiss();
                    this.getCurrentTravelByIdDriver();
                }
            } finally {
                this.daoTravel = null;
            }
        }

    }

    private void showLoadingWaitingFinishTravelFromWeb()
    {
        hideLoading();
         loading = ProgressDialog.show(HomeChofer.this, getString(R.string.viaje_siendo_aprobado_por_web), getString(R.string.espere_unos_segundos), true, false);
         loading.show();
         isWaitingForCloseTravelByweb=true;
    }

    public void clearNotificationAndoid()
    {
        NotificationManager notifManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }



    public void preFinihMpago(){
        if(PARAM_82 == 1) {
            loading = ProgressDialog.show(HomeChofer.this, getString(R.string.cargando_pago), getString(R.string.espere_unos_segundos), true, false);
            new SendPostRequest().execute();
        }else {
            idPaymentFormKf = 3;
            verifickTravelFinish();
        }

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {

        try {

            URL url = new URL(HttpConexion.BASE_URL+"as_mpago/index.php"); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("clienteid", HomeChofer.PARAM_69_KEY_MP);
            postDataParams.put("clientesecret", HomeChofer.PARAM_79);
            postDataParams.put("currency_id", HttpConexion.COUNTRY);
            postDataParams.put("unit_price", totalFinal);
            postDataParams.put("agency", gloval.getGv_base_intance());
            postDataParams.put("idTravel", currentTravel.getIdTravel());


            Log.d("postDataParams", String.valueOf(postDataParams));



            String message = postDataParams.toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setFixedLengthStreamingMode(message.getBytes().length);



            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(message);
            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder response = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine())!=null){
                    response.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                os.close();
                return response.toString();

            }
            else {
                return "false : " + responseCode;
            }
        }
        catch(Exception e){
            return "Exception: " + e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
    }
}

    public void postImageData()
    {
        uploadImage();
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    /* SERVICIO QUE GUARDA LA FIRMA */
    private void uploadImage()
    {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            private ProgressDialog loading;
            private RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(HomeChofer.this, "Procesando Firma", "Espere unos Segundos...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // if("true".equals(s))
               // {
                    // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    idPaymentFormKf = 5;
                    if (PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR == 1 && !flatParam87){
                        finishTravel();
                    }else{
                        verifickTravelFinish();
                    }

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                // Get the Image's file name
                String fileNameSegments[] = path_image.split("/");
                // Put file name in Async Http Post Param which will used in Php web app
                data.put("filename", String.valueOf(currentTravel.getIdTravel()));
                return rh.sendPostRequest(UPLOAD_URL, data);
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    private void retrySaveFirma()
    {
        showLoadingWithCancel(getString(R.string.error_guardar_firma), getString(R.string.por_favor_intente_nuevamente), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    showFinshTravel();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    retrySaveFirma();
                }
            }
        });
    }

    private void showLoadingWithCancel(String title, String msg, DialogInterface.OnClickListener onClickListener)
    {
        try{
            hideLoading();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeChofer.this);
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


    public double getPriceReturnBylistBeneficion(List<BeneficioEntity> list,double distance){
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
                amountExtra = distanceExtraBeneficio * PARAM_6;
                Log.d("VALOR resto","valor: "+amountExtra);
            }

            value = amountExtra + value;

            Log.d("VALOR total","valor: "+value);
        }

        return value;
    }

    public double getPriceBylistBeneficion(List<BeneficioEntity> list,double distance){
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



    public void getParam(){
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        Log.d("PARAM_69_KEY_MP", "GET PARAM");
        try {
            Call<List<paramEntity>> call = this.daoTravel.getparam();
            Log.d("PARAM_69_KEY_MP", call.request().toString());
            Log.d("PARAM_69_KEY_MP", call.request().headers().toString());
            call.enqueue(new Callback<List<paramEntity>>() {
                @Override
                public void onResponse(Call<List<paramEntity>> call, Response<List<paramEntity>> response) {
                    Log.d("PARAM_69_KEY_MP", response.headers().toString());
                    Log.d("PARAM_69_KEY_MP", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        //the response-body is already parseable to your ResponseBody object
                        List<paramEntity> listParam = (List<paramEntity>) response.body();
                        gloval.setGv_param(listParam);
                        setParamLocal();
                        paymentCardConfiguration();
                    }
                }

                @Override
                public void onFailure(Call<List<paramEntity>> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }
            });

        } finally {
            this.daoTravel = null;
        }
    }

    public void paymentCardConfiguration(){
        //TODO: Descomentar y validar tambien el otro motor de pago


        if(Utils.isCardEnabled(gloval.getGv_param()) && !Utils.isCardAcepted(gloval.getGv_param())){
            Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.motor_de_pago_no_configurado),
                    Snackbar.LENGTH_LONG)
                    .setDuration(9000).show();
        }
    }



    //region VALIDAR GPS ACTIVADO
    public static boolean isGPSProvider(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetowrkProvider(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    //endregion

    //region DIALOGOS - MENSAJES - LISTENER
    @Override
    public void showDialogTravelInfoChofer(InfoTravelEntity currentTravelEntity) {
        if (currentTravelEntity!=null){
            new TravelInfoChoferDialog(currentTravelEntity,gloval).show(getSupportFragmentManager(),"DialogInfo");
        }else {
            showMessage(getString(R.string.no_viaje_asignado),0);
        }

    }

    @Override
    public void salirApp() {
        fn_exit();
    }

    public void showMessage(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status);
    }

    @Override
    public void activarServicio() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogGPS");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }

        if (!isServiceRunning(SocketServices.class)){
            Intent service= new Intent(this, SocketServices.class);
            service.putExtra("fullNameDriver", gloval.getGv_user_name());
            service.putExtra("nrDriver",  gloval.getGv_nr_driver());
            service.putExtra("idDriver",  gloval.getGv_id_driver());


            //Log.v("CURRENT_TRAVEL",currentTravel);

            if (currentTravel!=null){
                service.putExtra("currentTravel",String.valueOf(currentTravel.getIdTravel()));
                service.putExtra("currentTravel_codTravel",currentTravel.getCodTravel());
                service.putExtra("currentTravel_nameStatusTravel",currentTravel.getNameStatusTravel());
                service.putExtra("currentTravel_nameOrigin",currentTravel.getNameOrigin());
                service.putExtra("currentTravel_nameDestination",currentTravel.getNameDestination());
                service.putExtra("currentTravel_client",currentTravel.getClient());
                service.putExtra("currentTravel_clientId",currentTravel.getIdClient());
                service.putExtra("currentTravel_phoneNumberDriver",currentTravel.getPhoneNumber());

                service.putExtra("currentTravel_latOrigin",currentTravel.getLatOrigin());
                service.putExtra("currentTravel_lngOrigin",currentTravel.getLonOrigin());

            }
            service.putExtra("user_id",pref.getInt("user_id", 0));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(service);
            }else{
                startService(service);
            }
        }

    }

    @Override
    public void ocultarActivarGPS() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogGPS");
        if (prev != null) {
            try {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }catch (Exception e){

            }
        }

    }

    @Override
    public void activarGps() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogGPS");
        if (prev == null) {
            new NoGpsDialog().show(getSupportFragmentManager(), "DialogGPS");
        }
    }
    //endregion

    //region VALIDAR SI EL SERVICIO ESTA ACTIVO
    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    //endregion



    private void sendCurrentTravelToService()
    {
        try
        {
            Intent in = new Intent();
            in.putExtra("currentTravel", currentTravel!=null ? String.valueOf(currentTravel.getIdTravel()) : "");
            in.putExtra("currentTravel_client", currentTravel!=null ? String.valueOf(currentTravel.getClient()) : "");
            in.putExtra("currentTravel_clientId", currentTravel!=null ? String.valueOf(currentTravel.getIdClient()) : "");
            in.putExtra("currentTravel_codTravel", currentTravel!=null ? String.valueOf(currentTravel.getCodTravel()) : "");
            in.putExtra("currentTravel_nameDestination", currentTravel!=null ? String.valueOf(currentTravel.getNameDestination()) : "");
            in.putExtra("currentTravel_nameOrigin", currentTravel!=null ? String.valueOf(currentTravel.getNameOrigin()) : "");
            in.putExtra("currentTravel_nameStatusTravel", currentTravel!=null ? String.valueOf(currentTravel.getNameStatusTravel()) : "");
            in.putExtra("currentTravel_phoneNumberDriver", currentTravel!=null ? String.valueOf(currentTravel.getPhoneNumberDriver()) : "");

            in.putExtra("currentTravel_latOrigin", currentTravel!=null ? String.valueOf(currentTravel.getLatOrigin()) : "");
            in.putExtra("currentTravel_lngOrigin", currentTravel!=null ? String.valueOf(currentTravel.getLonOrigin()) : "");

            in.setAction(SocketServices.CURRENT_TRAVEL_CHOFER_RECEIVER_TAG);
            sendBroadcast(in);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void createNotificationChannels(Context context ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Utils.createNotificationChannelsDriver(context);
        }
    }

    private void openWazeNavigation(String lat, String lng) {

       try{
           Uri gmmIntentUri = Uri.parse("https://waze.com/ul?ll=".concat(lat).concat(",").concat(lng).concat("&navigate=yes"));
           Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
           startActivity(mapIntent);
       }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }

    private void openGoogleMapsNavigation(String lat, String lng) {
         Uri gmmIntentUri = Uri.parse("google.navigation:q=".concat(lat).concat(",").concat(lng).concat("&mode=d"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    private void getReservations(){
        try
        {
            Call<List<InfoTravelEntity>> call = null;
            if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
            call =  this.daoTravel.getReservations(gloval.getGv_id_driver(),0,3);
            call.enqueue(new Callback<List<InfoTravelEntity>>() {
                @Override
                public void onResponse(Call<List<InfoTravelEntity>> call, Response<List<InfoTravelEntity>> response) {
                    try {
                        if (response.body()!=null){
                            List<InfoTravelEntity> listReservations = response.body();
                            refreshContentReservations(listReservations);
                        }
                        else{
                            refreshContentReservations(null);
                        }
                    }catch (Exception e){
                        refreshContentReservations(null);
                        Log.e("ERROR RESERVAS", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<List<InfoTravelEntity>> call, Throwable t) {
                    Log.e("FAILURE RESERVAS", t.toString());
                    refreshContentReservations(null);

                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void dismissDialogTravel()
    {
        try{
            if(dialogTravel!=null) {
                dialogTravel.dismissAllowingStateLoss();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void refreshContentReservations(List<InfoTravelEntity> listReservations)
    {
        try
        {
            // ACTUALIZAMOS EL NUMERO DE NOTIFICCIONES
            if(listReservations!=null && menuItemReservationView!=null) {
                Utils.setupBadge(listReservations.size(),textCartItemCount);

                if (Utils.hasAnyReservationWithountConfirm(listReservations)) {
                    Animation mAnimation = new AlphaAnimation(1, 0);
                    mAnimation.setDuration(400);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    mAnimation.setRepeatCount(Animation.INFINITE);
                    mAnimation.setRepeatMode(Animation.REVERSE);

                    menuItemReservationView.startAnimation(mAnimation);
                } else {
                    menuItemReservationView.clearAnimation();
                }
            }
            else{
                Utils.setupBadge(0, textCartItemCount);
                menuItemReservationView.clearAnimation();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void recargarParametrosLocales() {
        TypeToken<List<paramEntity>> token3 = new TypeToken<List<paramEntity>>() {};
        Gson gson = new Gson();
        List<paramEntity> listParam = gson.fromJson(pref.getString("param", ""), token3.getType());
        gloval.setGv_param(listParam);
        setParamLocal();
    }

    private void loadParamsFromApi()
    {
        try{
            if (this.apiService == null) {
                this.apiService = HttpConexion.getUri().create(ServicesLoguin.class);
            }

            Call<List<paramEntity>> call = this.apiService.getParams();
            call.enqueue(new Callback<List<paramEntity>>() {
                @Override
                public void onResponse(Call<List<paramEntity>> call, Response<List<paramEntity>> response) {
                    try
                    {
                        if(response.isSuccessful())
                        {
                            List<paramEntity> listParam = response.body();
                            gloval.setGv_param(listParam);
                            setParamLocal();
                            Utils.saveParamsToLocalPreferences(getApplicationContext(), listParam);
                            paymentCardConfiguration();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<paramEntity>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /* ALERTA SIN CONEXION*/
    public void showAlertNoConexion(){
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                R.string.problema_internet,
                30000);
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);

        snackbar.setAction(getString(R.string.verificar), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

        snackbar.show();
    }



    /*********************************************
    * Región para Overlay Window
    * ********************************************/
    public void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_REQUEST_CODE);
            } else {
                openFloatingWindow();
            }
        } else {
            openFloatingWindow();
        }
    }

    private void closeFloatingWindowsService() {
        try {
            stopService(new Intent(this, LayoutOverlayForNotification.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void openFloatingWindow() {
        Intent intent = new Intent(this, LayoutOverlayForNotification.class);
        this.stopService(intent);
        this.startService(intent);
    }

    private void logoutSocialIfPresent()
    {
        try{
            LoginManager.getInstance().logOut();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setBtnPauseVisible(boolean isVisible)
    {
        try{
            HomeFragment.setSwitchPausarVisible(isVisible);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void callServiceToPauseService(boolean setActive, IExecutionComplete callback) {
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            DriverSemaforo driverSemaforo = new DriverSemaforo();
            driverSemaforo.driver=new DriverSemaforoItem();
            driverSemaforo.driver.id = gloval.getGv_id_driver();
            driverSemaforo.driver.active = setActive?1:0;
            Call<resp> call = this.daoTravel.setDriverOnline(driverSemaforo);
            call.enqueue(new Callback<resp>() {
                @Override
                public void onResponse(Call<resp> call, Response<resp> response) {
                    if (response.code() == 200) {
                        try{
                            String result= response.body().response;
                            callback.onResult(result.contains("Activo") ? 1: 0);
                            //callback.onResult(-1);

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            callback.onResult(-1);
                        }
                    }
                    else{
                        callback.onResult(-1);
                    }
                }

                @Override
                public void onFailure(Call<resp> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                    callback.onResult(-1);
                }
            });

        } finally {
            this.daoTravel = null;
        }

    }

    private void getDataFromCompany(IDataCompany callback)
    {
        companyData=null;
        if(currentTravel.isTravelComany==1)
        {
            if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
            Call<CompanyData> call;
            call = daoTravel.getCompanyData(currentTravel.getIdClient());
            call.enqueue(new Callback<CompanyData>() {
                @Override
                public void onResponse(Call<CompanyData> call, Response<CompanyData> response) {
                    if(response.isSuccessful())
                    {
                        companyData = response.body();
                        callback.getCompanyData(companyData);
                    }
                }
                @Override
                public void onFailure(Call<CompanyData> call, Throwable t) {
                    t.printStackTrace();
                    companyData=null;
                    callback.getCompanyData(companyData);
                }
            });
        }
        else
        {
            callback.getCompanyData(null);
        }
    }

    private void showNotificationIfChatMessageNotShowing(ChatMessageReceive chatMessage){
        try{
            if(!FragmentChat.isChatFragmentVisible){
                createNotificationForChatReceived(chatMessage);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void createNotificationForChatReceived(ChatMessageReceive chatMessage){

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeChofer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("IsFromChatNotification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Globales.NotificationChannelCustom.CHOFFER_MENSAJE_CHAT_RECBIDO)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.chat_message_received_title))
                .setContentText(chatMessage.getChatMessage())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }

    private void openChatFragmentIfComeFromNotification(){
        try{
            boolean isFromChat = getIntent().getExtras().getBoolean("IsFromChatNotification", false);
            if(isFromChat){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fn_chat();
                        ventanaNavegacion=4;
                    }
                }, 500);


            }
        }
        catch (Exception ex){

        }

    }

    public interface IDataCompany{
        void getCompanyData(CompanyData companyData);
    }

    public void showOrHideStreetButton(boolean show){
        try{
            materialDesignFAM.setVisibility(show && MUST_SHOW_STREET_BUTTON ? View.VISIBLE: View.GONE);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public class ChatNotifiacionSocketReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("CHAT_", "RECIBE EL RECEIVER EN HOMECHOFER");
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey("message")) {
                    try{
                        String objMessageSerialized = extras.getString("message");
                        JSONObject obj = new JSONObject(objMessageSerialized);
                        ChatMessageReceive chatMessage=new ChatMessageReceive(obj);
                        if(chatViewModel.addChatMessage(new Chat("2","1",chatMessage.getChatMessage(),chatMessage.getChatDate()),chatMessage.getIdMessage())){
                            showNotificationIfChatMessageNotShowing(chatMessage);
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

}

