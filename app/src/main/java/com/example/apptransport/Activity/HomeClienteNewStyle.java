package com.example.apptransport.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.apreciasoft.mobile.asremis.Entity.CompanyData;
import com.apreciasoft.mobile.asremis.Fragments.FragmentChatPassenger;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Dialog.ExitDialog;
import com.apreciasoft.mobile.asremis.Dialog.GenericDialog;
import com.apreciasoft.mobile.asremis.Dialog.ListenerDialogExit;
import com.apreciasoft.mobile.asremis.Dialog.ListenerDialogGeneric;
import com.apreciasoft.mobile.asremis.Dialog.NoGpsDialog;
import com.apreciasoft.mobile.asremis.Dialog.TravelInfoDialog;
import com.apreciasoft.mobile.asremis.Entity.CalificationStar;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntityLite;
import com.apreciasoft.mobile.asremis.Entity.PointToPointMaster;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.reason;
import com.apreciasoft.mobile.asremis.Entity.reasonEntity;
import com.apreciasoft.mobile.asremis.Entity.token;
import com.apreciasoft.mobile.asremis.Entity.tokenFull;
import com.apreciasoft.mobile.asremis.Fragments.FragmentChat;
import com.apreciasoft.mobile.asremis.Fragments.FragmentHistoryTravel;
import com.apreciasoft.mobile.asremis.Fragments.FragmentReporte;
import com.apreciasoft.mobile.asremis.Fragments.HomeClientFragmentNewStyle;
import com.apreciasoft.mobile.asremis.Fragments.ListTypeCarLayout;
import com.apreciasoft.mobile.asremis.Fragments.ListenerFragmentClient;
import com.apreciasoft.mobile.asremis.Fragments.NotificationsFrangment;
import com.apreciasoft.mobile.asremis.Fragments.PaymentFormClient;
import com.apreciasoft.mobile.asremis.Fragments.ProfileClientFr;
import com.apreciasoft.mobile.asremis.Fragments.ReservationsFrangment;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.CallbackActivity;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.Util.WsTravel;
import com.apreciasoft.mobile.asremis.core.services.SocketServices;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeClienteNewStyle  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        CallbackActivity,
        ListenerDialogGeneric,
        ListenerFragmentClient,
        ListenerDialogExit{

    private static final int OPEN_PAYMENT_ACTIVITY=10;
    private static String ReservationName;
    public static PointToPointMaster pointToPointListItems;
    ServicesTravel apiService = null;
    List<reason> list = null;

    Integer motivo = 0;

   // SupportPlaceAutocompleteFragment autocompleteFragment,autocompleteFragmentOrigenReserva,
   //         autocompleteFragmentDestinoReserva;
    public String TAG = "HOME_CLIENT_ACTIVITY";
    protected PowerManager.WakeLock wakelock;
    public static GlovalVar gloval;
    int isFleetTravelAssistance = 0;
    public ServicesTravel daoTravel = null;
    public static InfoTravelEntity currentTravel;
    public ServicesLoguin daoLoguin = null;
    public WsTravel ws = null;
    public ListTypeCarLayout dialogFragment = null;
    public static final int PROFILE_DRIVER_ACTIVITY = 2;
    public static  ArrayList resultList = null;
    public static  ArrayList resultListPlaceID = null;
    public List<Integer> listCatgoryId = new ArrayList<Integer>();
    public static String location = "";
    public String lat = "";
    public String lon = "";
    private View menuItemReservationView;
    private TextView textCartItemCount;

    public Button btnrequertReser;
    public Button btnrequetTravelNow;
    private InfoTravelEntityLite infoTravelEntityLite= null;



    public static String destination = "";
    public static String latDestination = "";
    public static String lonDestination = "";


    public  RatingBar rating;
    public Spinner spinner;
    public Spinner spinnerTipoVehiculo;
    public String dateTravel= "";

    public boolean isReervation = false;

    /* GOOGLE PALCE   */
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAIL = "/details";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyApZ1embZ2bhcI4Ir8NepmyjTfNvGvjUas";

    /*DATE*/
    private EditText fromDateEtxt;
    private EditText fromTimeEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private SimpleDateFormat dateFormatter;

    private static String[] VEHYCLETYPE = new String[0];

    //public FloatingActionMenu btnSolicitarViajesMaster;
    //public FloatingActionButton btnSolicitarReserva, btnSolicitarViaje;
    public SharedPreferences.Editor editor;


    private EditText hoursAribo;
    private EditText terminal;
    private EditText airlineCompany;
    private EditText flyNumber;
    private CheckBox isFly,isFleetTravel;
    private ConstraintLayout constraintLayoutFlete,constraintLayoutFly,constraintLayoutNuevoViaje, constraintLayoutSelectTypeAddres;
    private ConstraintLayout clClassicAddressSection, clCustomAddressSection;
    private TextView textViewDistancia,textViewMonto;
    private TextView textCustomAddressOrigen, textViewCustomAddressDestino;

    public ProgressDialog loading,loadingGloval;
    private Integer idTypeVehicle;

    public   NumberPicker np;

    private ConstraintLayout contentInfoReervation;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private boolean isCustomSelectAddress=false;

    public static CompanyData companyData;


    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLoadTodays, new IntentFilter("update-message"));

        SharedPreferences pref = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0); // 0 - for private mode
        editor = pref.edit();

        //evitar que la pantalla se apague
        final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();

        setContentView(R.layout.activity_client_home_new_style);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        createNotificationChannels(this); //Agregado el 19/06/2019: crea los canales de notificación para Android O en adelante

        // variable global //
        initializeGlovalIfNull();
        checkNeedLogin();

        String token = FirebaseInstanceId.getInstance().getToken();
        // Log.d(TAG, token);
        enviarTokenAlServidor(token,gloval.getGv_user_id());

        addViewChooseAddress();
        addViewViaje();
        addViewReserva();
        showReserva(false);
        showOrHideNewTravelContent(false);
        initializeTravelContent();
        loadParamsFromApi();
        getDataFromCompany();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fr =  getSupportFragmentManager();
        fr.beginTransaction().replace(R.id.content_frame_client, new HomeClientFragmentNewStyle(this)).commit();



        // HEADER MENU //
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.username);
        TextView email = (TextView)header.findViewById(R.id.email);
        name.setText(gloval.getGv_user_name());
        email.setText(getString(R.string.bienvenido));

        downloadUserImage(header);

        currentTravel = gloval.getGv_travel_current();
        controlViewTravel();

        // WEB SOCKET //
        ws = new WsTravel(this);
        ws.connectWebSocket(gloval.getGv_user_id());



        /*EDIT TEX DATE */
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        _setCategory();
        /*------------------*/
        configureParams();
        validarGPS();
        this.getCurrentTravelByIdClient();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager key = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            key.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getPointToPointItems();
    }

    @Override
    public void onBackPressed() {
        showReserva(false);
        showOrHideNewTravelContent(false);

        if(!performOnBackPressToTravelFragment())
        {
            new ExitDialog(this).show(getSupportFragmentManager(),"DialogExit");
        }
    }



    private boolean performOnBackPressToTravelFragment()
    {
        boolean result = false;
        try{
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame_client);
            if(f instanceof HomeClientFragmentNewStyle)
            {
                if(((HomeClientFragmentNewStyle) f).isTravelLayoutOpen())
                {
                    openMapFragment();
                    result=true;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        showReserva(false);
        showOrHideNewTravelContent(false);
        isReervation =  false;

        if (id == R.id.nav_ubicacion) {
            showMapFragment(fm);

        } else if (id == R.id.nav_historial) {

            FragmentHistoryTravel verifi = new FragmentHistoryTravel();
            verifi.ver = 1;
            fm.beginTransaction().replace(R.id.content_frame_client,new FragmentHistoryTravel()).commit();
            btnFlotingVisible(false);
        }else if (id == R.id.nav_pay_form_client) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_client,new PaymentFormClient()).commit();
            btnFlotingVisible(false);

        }else if (id == R.id.nav_manage) {

            fm.beginTransaction().replace(R.id.content_frame_client,new NotificationsFrangment()).commit();
            btnFlotingVisible(false);

        } else if (id == R.id.nav_reservations) {

            fn_gotoreservation();
            btnFlotingVisible(false);

        }else if (id == R.id.nav_profile){
            fn_gotoprofile();
            btnFlotingVisible(false);

        } else if (id == R.id.nav_exit){
            new ExitDialog(this).show(getSupportFragmentManager(),"DialogExit");
        }else if (id == R.id.nav_chat){

            fn_chat();
            btnFlotingVisible(false);

        } else if (id == R.id.nav_send) {

            fn_reporte();
            btnFlotingVisible(false);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // FIRMAAAA //
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode) {
            case PROFILE_DRIVER_ACTIVITY:

                break;
            case OPEN_PAYMENT_ACTIVITY:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_home, menu);

        // ESTO NOS PERMITE CARGAR EL ICONO DE MENU CON NOTIFICACIONBES DE RESERVAS //
        MenuItem menuItemReservation =  menu.findItem(R.id.action_reervations);
        menuItemReservationView = menuItemReservation.getActionView();
        textCartItemCount = (TextView) menuItemReservationView.findViewById(R.id.cart_badge);
        Utils.setupBadge(0, textCartItemCount);
        //menuItemReservationView.setImageResource(R.drawable.ic_notification_reservations);
        menuItemReservationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFlotingVisible(false);
                fn_gotoreservation();
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        showReserva(false);
        showOrHideNewTravelContent(false);
        isReervation =  false;

        if (id == R.id.action_notifications) {

            fn_gotonotification();
            btnFlotingVisible(false);

            return true;
        }
        else if (id == R.id.action_reervations) {

            fn_gotoreservation();
            btnFlotingVisible(false);

            return true;
        }
        else if (id == R.id.action_refhesh) {

            fn_refresh();
            btnFlotingVisible(false);

            return true;
        }
        else if(id==R.id.action_map){
            openMapFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openMapFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        showMapFragment(fm);
        fn_refresh();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void showMapFragment(FragmentManager fm){
        fm.beginTransaction().replace(R.id.content_frame_client,new HomeClientFragmentNewStyle(this)).commit();
        controlViewTravel();
        btnFlotingVisible(true);
    }

    public void validarGPS()
    {
        if (!isGPSProvider(this)&&!isNetowrkProvider(this)){
            new NoGpsDialog().show(getSupportFragmentManager(), "DialogGPS");
        }
    }













    @Override
    public void onClick(View view) {

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setInfoTravel() {
        try{

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame_client);
            if(f instanceof HomeClientFragmentNewStyle)
            {
                if (currentTravel != null) {
                    ((HomeClientFragmentNewStyle) f).setInfoTravel(gloval.getGv_travel_current_lite());
                } else {
                    gloval.setGv_travel_current_lite(null);
                    ((HomeClientFragmentNewStyle) f).clearInfo();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void clearInfoTravel() {
        try{

            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame_client);
            if(f instanceof HomeClientFragmentNewStyle)
            {
                ((HomeClientFragmentNewStyle) f).clearInfo();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();

        if(currentTravel !=  null) {
            currentTravel = gloval.getGv_travel_current();
        }
    }


    public void _setCategory()
    {
        try {

            List<String> list = new ArrayList<String>();

            VEHYCLETYPE = new String[gloval.getGv_listvehicleType().size()];

            for (int i = 0; i < gloval.getGv_listvehicleType().size(); i++) {
                list.add("Tipo De Vehiculo: " + gloval.getGv_listvehicleType().get(i).getVehiclenType());
                listCatgoryId.add(gloval.getGv_listvehicleType().get(i).getIdVehicleType());
            }
            list.toArray(VEHYCLETYPE);

            // Spinner click listener
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        this.idTypeVehicle =  listCatgoryId.get(position);

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
    }

    @SuppressLint("LongLogTag")
    public static ArrayList autocomplete(String input) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            // sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            sb.append("&components=".concat(URLEncoder.encode("country:AR", "utf8")));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.d("Error processing Places API URL", String.valueOf(e));
            return resultList;
        } catch (IOException e) {
            Log.d("Error connecting to Places API", String.valueOf(e));
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            resultListPlaceID = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                resultListPlaceID.add(predsJsonArray.getJSONObject(i).getString("place_id"));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public void showOrHidePopupViajeSolicitado(boolean active, String sms)
    {
        try{
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame_client);
            if(f instanceof HomeClientFragmentNewStyle)
            {
                ((HomeClientFragmentNewStyle) f).showOrHidePopupViajeSolicitado(active, sms);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showMotivosDeCancelacion(boolean active, String sms)
    {
        try{
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame_client);
            if(f instanceof HomeClientFragmentNewStyle)
            {
                ((HomeClientFragmentNewStyle) f).showMotivosDeCancelacion(active, sms);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void cancelTravelByClient()
    {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            loadingGloval = ProgressDialog.show(HomeClienteNewStyle.this, "Cancelar", "Espere unos Segundos...", true, false);


            motivo = motivo - 1;
            Call<Boolean> call = this.daoTravel.cancelByClient(gloval.getGv_user_id(), motivo, infoTravelEntityLite.getIdTravel() );

            Log.d(TAG, call.request().toString());
            Log.d(TAG, call.request().headers().toString());

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    loadingGloval.dismiss();

                    Toast.makeText(getApplicationContext(), getString(R.string.viaje_cancelado), Toast.LENGTH_LONG).show();

                    // cerramo el dialog //
                    showMotivosDeCancelacion(false,"");


                  //  btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                  //  btnSolicitarViajesMaster.close(true);

                    showReserva(false);

                    isReervation =  false;
                    showOrHideNewTravelContent(false);
                    setInfoTravel();

                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    loadingGloval.dismiss();

                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }


            });

        } finally {
            this.daoTravel = null;
        }

    }

    public  void  confirmCancelByClient(int idTravel)
    {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }


        try {
            Call<Boolean> call = this.daoTravel.confirmCancelByClient(idTravel);

            Log.d("fatal", call.request().toString());
            Log.d("fatal", call.request().headers().toString());

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    currentTravel = null;
                    setInfoTravel();
                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }
            });

        } finally {
            this.daoTravel = null;
        }
    }


    public  void confirmAceptByClient(int idTravel)
    {
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        try {
            Call<Boolean> call = this.daoTravel.confirmAceptaByClient(idTravel);

            Log.d("fatal", call.request().toString());
            Log.d("fatal", call.request().headers().toString());

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                  //  btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                    currentTravel = null;
                    infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                    gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019
                 //   btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                    gloval.setGv_travel_current(null);
                    setInfoTravel();
                    showOrHidePopupViajeSolicitado(false, "");
                    setInfoTravel();
                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }

            });

        } finally {
            this.daoTravel = null;
        }
    }


    // Enviar token al servidor //
    private void enviarTokenAlServidor(String str_token,int idUser) {


        if (this.daoLoguin == null) { this.daoLoguin = HttpConexion.getUri().create(ServicesLoguin.class); }

        try {
            token T = new token();
            T.setToken(new tokenFull(str_token, idUser,gloval.getGv_id_driver(),BuildConfig.VERSION_NAME));

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            Log.d(TAG,gson.toJson(T));

            Call<Boolean> call = this.daoLoguin.token(T);

            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    boolean  result = response!=null ? response.body() : false;
                    Log.w("SEND TOKEN", result?"true":"false");
                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("SEND TOKEN", "Error", t);
                }
            });

        } finally {
            this.daoTravel = null;
        }


    }



    // CONTROLO BOTON FLOTANTE //
    public  void  btnFlotingVisible(boolean isVisible)
    {

    }

    // Recibios notificacion //
    private BroadcastReceiver broadcastReceiverLoadTodays = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            currentTravel = gloval.getGv_travel_current();
            if(Utils.isForegroundActivity(getApplicationContext(), "HomeClienteNewStyle"))
            {
                if(intent!=null && "true".equals(intent.getStringExtra("is_close_driver"))    ) {
                    mostrarInfoMsgDriverClose();
                }
                else{
                    if(Utils.isCliente(gloval.getGv_id_profile())) {
                        getCurrentTravelByIdClient();
                    }
                }
            }
        }
    };


    public  void  getCurrentTravelByIdClient()
    {
        getCurrentTravelByIdClient(true);
    }

    public  void  getCurrentTravelByIdClient(final boolean manageViews)
    {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            Call<InfoTravelEntity> call = null;
            if(gloval.getGv_id_profile() == Globales.ProfileId.CLIENT_PARTICULAR){
                call = this.daoTravel.getCurrentTravelByIdClient(gloval.getGv_id_cliet());

            }else  if (gloval.getGv_id_profile() == Globales.ProfileId.CLIENT_COMPANY){
                call = this.daoTravel.getCurrentTravelByIdUserCompany(gloval.getGv_user_id());
            }

            call.enqueue(new Callback<InfoTravelEntity>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<InfoTravelEntity> call, Response<InfoTravelEntity> response) {

                    Log.d("VIAJE", response.toString());

                    gloval.setGv_travel_current(response.body());
                    if(isCurrentTravelDateValid(gloval.getGv_travel_current())){
                        currentTravel = gloval.getGv_travel_current();
                        gloval.setGv_travel_current_lite(new InfoTravelEntityLite(currentTravel));
                        if(manageViews){
                            controlViewTravel();
                        }

                    }
                    else {
                        gloval.setGv_travel_current(null);
                        gloval.setGv_travel_current_lite(null);
                        if(manageViews)
                        {
                            controlViewTravel();
                        }

                    }

                }

                public void onFailure(Call<InfoTravelEntity> call, Throwable t) {
                    Snackbar.make(findViewById(android.R.id.content), "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }


            });

        } finally {
            this.daoTravel = null;
        }
    }

    /*
    Fecha Creación  : 16/05/2019
    Creado por      : Sergio Franco
    Detalle         : Valida que la fecha del current travel sea válida.
    Si no está en curso, tiene que traer si o solo sí si el viaje que está dentro de las últimas 24 hs.
     */
    private boolean isCurrentTravelDateValid(InfoTravelEntity infoTravelEntity)
    {
        boolean result=true;
        try{
            if(infoTravelEntity.getIdSatatusTravel()!= Globales.StatusTravel.VIAJE_EN_CURSO &&
                    Utils.convertStringToDate(infoTravelEntity.getMdate()).before(Utils.getNowMinus24hs()))
            {
                result=false;
            }
        }
        catch (Exception ex){
            result=false;
        }
        return result;
    }


    private void mostrarInfoMsgDriverClose()
    {
        try{
            Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogInfo");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }

            new GenericDialog(getString(R.string.informacion_viaje)
                    ,getString(R.string.chofer_cerca_msg),
                    0,
                    0,
                    this)
                    .show(getSupportFragmentManager(), "DialogInfo");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    //estatus del viaje
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void controlViewTravel()
    {
        try {
            if(gloval.getGv_travel_current()!=null){
                Log.e("INFORAMCION DEL VIAJE", String.valueOf(gloval.getGv_travel_current().makeJson()));
            }
            infoTravelEntityLite = gloval.getGv_travel_current_lite();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("DialogInfo");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }

            if (infoTravelEntityLite != null) {
                showOrHidePopupViajeSolicitado(false, "");

                if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA ||
                        infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER ||
                        infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO ||
                        infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO ||
                        infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_CHOFER    ) {

                    if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER) {

                        new GenericDialog(getString(R.string.informacion_viaje)
                                ,getString(R.string.viaje_aceptado_chofer),
                                0,
                                0,
                                this)
                                .show(getSupportFragmentManager(), "DialogInfo");
                       // btnSolicitarViajesMaster.setVisibility(View.GONE);

                    } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO) {
                        if(needClientPayment(infoTravelEntityLite))
                        {
                            openActivityForPayment(infoTravelEntityLite);
                        }
                        else{
                            new GenericDialog(getString(R.string.informacion_viaje),
                                    getString(R.string.viaje_en_curso),
                                    0,
                                    0,
                                    this)
                                    .show(getSupportFragmentManager(), "DialogInfo");
                        }

                    } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO) {
                        Log.d("start", String.valueOf(infoTravelEntityLite.start));
                        if(infoTravelEntityLite.start == 0) {
                            clearInfoTravel();
                            this.ShowDialogStarts();
                        }else
                        {
                            currentTravel = null;
                            infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                            gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019

                       //     btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                            gloval.setGv_travel_current(null);
                            clearInfoTravel();
                        }
                        gloval.setLocationDriverFromClient(null);


                    } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_CHOFER) {

                        new GenericDialog(getString(R.string.informacion_viaje),
                                getString(R.string.viaje_rechazado_chofer),
                                infoTravelEntityLite.getIdTravel(),
                                2,
                                this)
                                .show(getSupportFragmentManager(), "DialogInfo");

                        currentTravel = null;
                        infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                        gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019
                      //  btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                        gloval.setGv_travel_current(null);
                        clearInfoTravel();
                        gloval.setLocationDriverFromClient(null);

                    } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA) {
                        if(infoTravelEntityLite.getIdReasonCancelKf()==Globales.ID_REASON_CANCEL_TRAVEL_NO_HAY_AUTOS)
                        {
                            showMessageCancelledTravel(getString(R.string.viaje_cancelado), getString(R.string.viaje_cancelado_sin_autos));
                        }
                        else{
                            String reasonCancel= TextUtils.isEmpty(infoTravelEntityLite.getReason()) ? "" :" ".concat(getString(R.string.mensaje)).concat(": ").concat(infoTravelEntityLite.getReason());
                            if(infoTravelEntityLite.getIdTypeTravelKf() == 1){
                                showMessageCancelledTravel(getString(R.string.informacion_viaje), getString(R.string.viaje_rechazado_agencia).concat(reasonCancel));
                            }
                            else if(infoTravelEntityLite.getIdTypeTravelKf() == 2){
                                showMessageCancelledTravel(getString(R.string.informacion_viaje),getString(R.string.reserva_rechazada_agencia).concat(reasonCancel));
                            }
                        }

                        currentTravel = null;
                        infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                        gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019
                      //  btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                        gloval.setGv_travel_current(null);
                        clearInfoTravel();
                        showOrHidePopupViajeSolicitado(false, "");
                        gloval.setLocationDriverFromClient(null);
                    }
                    setInfoTravel();

                } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA ) {

                    showOrHidePopupViajeSolicitado(false, "");

                    if (infoTravelEntityLite.getIdTypeTravelKf() == 1) {

                        new GenericDialog(getString(R.string.informacion_viaje),
                                getString(R.string.viaje_aceptado_agencia)+"\n"+
                                        getString(R.string.codigo_viaje)+infoTravelEntityLite.getCodTravel(),
                                0,
                                0,
                                this)
                                .show(getSupportFragmentManager(), "DialogInfo");
                       // btnSolicitarViajesMaster.setVisibility(View.INVISIBLE);
                        setInfoTravel();

                    } else {

                        if(infoTravelEntityLite.getIsConfirTravelAppFromWeb() == 1) {

                            new GenericDialog(getString(R.string.informacion_viaje),
                                    getString(R.string.reserva_acaptada)+"\n"+
                                            getString(R.string.codigo_viaje)+infoTravelEntityLite.getCodTravel(),
                                    currentTravel.getIdTravel(),
                                    1,
                                    this)
                                    .show(getSupportFragmentManager(), "DialogInfo");

                            currentTravel = null;
                            infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                            gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019
                            gloval.setGv_travel_current(null);
                            clearInfoTravel();

                        } else {
                            currentTravel = null;
                            infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                            gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019

                       //     btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                            gloval.setGv_travel_current(null);
                            clearInfoTravel();
                            showOrHidePopupViajeSolicitado(false, "");
                        }

                    }

                } else if (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.CHOFER_ASIGNADO) {

                    new GenericDialog(getString(R.string.informacion_viaje),
                            getString(R.string.chofer_asignado)+"\n"+
                                    getString(R.string.nombre_chofer)+" "+infoTravelEntityLite.getDriver(),
                            0,
                            0,
                            this)
                            .show(getSupportFragmentManager(), "DialogInfo");

                 //   btnSolicitarViajesMaster.close(true);
                    showReserva(false);
                    isReervation = false;
                    showOrHideNewTravelContent(false);
                    btnrequertReser.setEnabled(true);
                    btnrequetTravelNow.setEnabled(true);
                    setInfoTravel();
                }

            } else {
                currentTravel = null;
                infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019

             //   btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                gloval.setGv_travel_current(null);
                clearInfoTravel();
                showOrHidePopupViajeSolicitado(false, "");
                gloval.setLocationDriverFromClient(null);
            }

        }catch (Exception e) {
            Log.e("ERROR STATUS DEL VIAJE",e.getMessage());
        }

    }

    private void showMessageCancelledTravel(String title, String msg)
    {
        new GenericDialog(title,
                msg,
                infoTravelEntityLite.getIdTravel(),
                2,
                this)
                .show(getSupportFragmentManager(), "DialogInfo");

    }

    private boolean needClientPayment(InfoTravelEntityLite infoTravelEntityLite)
    {
        return (infoTravelEntityLite.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO &&
            infoTravelEntityLite.getClientPaymentStatus()==Globales.ClientPaymentStatus.PAGA_CLIENTE_PENDIENTE &&
            infoTravelEntityLite.getClientPaymentAmount()!=null);
    }

    private void openActivityForPayment(InfoTravelEntityLite infoTravelEntityLite)
    {
        double totalFinal= infoTravelEntityLite.getClientPaymentAmount();
        String param_103_payment_card_provider = getParamValue(Globales.ParametrosDeApp.PARAM_103_PAYMENT_CARD_PROVIDER,"");
        String param_69_key_mp = getParamValue(Globales.ParametrosDeApp.PARAM_69_KEY_MP,"");
        String param_79_mp_private = getParamValue(Globales.ParametrosDeApp.PARAM_79_MP_SECRET_KEY,"");
        Intent intent = new Intent(getApplicationContext(), PayCreditCardClient.class);
        intent.putExtra("TotalAmount",totalFinal);
        intent.putExtra("current",currentTravel.makeJson());
        intent.putExtra("PAYMENT_CARD_PROVIDER",  param_103_payment_card_provider);
        intent.putExtra("keyMercado", param_69_key_mp);
        intent.putExtra("keyMercadoPrivado",param_79_mp_private);
        startActivityForResult(intent, OPEN_PAYMENT_ACTIVITY);
    }


    private void initializeTravelContent()
    {
        contentInfoReervation.setVisibility(View.GONE);
        constraintLayoutSelectTypeAddres.setVisibility(View.GONE);
        constraintLayoutNuevoViaje.setVisibility(View.GONE);

    }


    public  void showOrHideNewTravelContent(boolean visible)
    {
        textViewDistancia.setText(getString(R.string.km_0));
        //setEstimatePrice(0d, false);
        isReervation = false;
        if(visible)
        {
            openNewTravelViewClassic();
        }
        else
        {
            closeNewTravelViewClassic();
        }

    }



    public void fn_refresh() {
        getCurrentTravelByIdClient();
        loadParamsFromApi();
        getPointToPointItems();
        getDataFromCompany();
    }

    private void getDataFromCompany()
    {
        companyData=null;
        if(Utils.isClienteCompany(gloval.getGv_id_profile()))
        {
            if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
            Call<CompanyData> call;
            call = daoTravel.getCompanyData(gloval.getGv_id_cliet());

            call.enqueue(new Callback<CompanyData>() {
                @Override
                public void onResponse(Call<CompanyData> call, Response<CompanyData> response) {
                    if(response.isSuccessful())
                    {
                        companyData = response.body();
                    }
                }
                @Override
                public void onFailure(Call<CompanyData> call, Throwable t) {
                    t.printStackTrace();
                    companyData=null;
                }
            });
        }
    }

    private void getPointToPointItems(){

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
        Call<PointToPointMaster> call;

        if(Utils.isClienteCompany(HomeClienteNewStyle.gloval.getGv_id_profile()))
        {
            call = daoTravel.getListPointToPointCompany(HomeClienteNewStyle.gloval.getGv_id_cliet());
        }else{
            call = daoTravel.getListPointToPointClient();
        }

        call.enqueue(new Callback<PointToPointMaster>() {
            @Override
            public void onResponse(Call<PointToPointMaster> call, Response<PointToPointMaster> response) {
                if(response.isSuccessful())
                {
                    pointToPointListItems = response.body();
                }
            }
            @Override
            public void onFailure(Call<PointToPointMaster> call, Throwable t) {
                t.printStackTrace();
                pointToPointListItems =null;
            }
        });
    }

    public  void  fn_gotoprofile()
    {

        try
        {
            FragmentManager fm = getSupportFragmentManager();

            //FloatingActionMenu btnTravelNew = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            //btnTravelNew.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.content_frame_client,new ProfileClientFr()).commit();


        }catch (Exception e)
        {
            Log.d("e",e.getMessage());
        }
    }

    // SALIR DE ACTIVITY //
    public void fn_exit(){


        currentTravel = null;
        gloval.setGv_logeed(false);
        new GlovalVar();

        enviarTokenAlServidor("",gloval.getGv_user_id());
        if(ws != null) {
            ws.closeWebSocket();
        }

        if(HomeClientFragmentNewStyle.timerblink!=null) {
            HomeClientFragmentNewStyle.timerblink.cancel();
        }


        SharedPreferences preferences = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit(); // commit changes

        stopService(new Intent(this,SocketServices.class));
        logoutSocialIfPresent();
        // LAMAMOS A EL MAIN ACTIVITY//
        Intent main = new Intent(HomeClienteNewStyle.this, LoginActivity.class);
        startActivity(main);
        finish();
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

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_update)

                .error(R.drawable.ic_user)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    // interfaz de ptro metodo de la actividad
    @Override
    public void doSomething() {
        serviceAllTravel();
    }


    public void ShowDialogStarts()
    {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        LinearLayout linearLayout = new LinearLayout(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT

        );



        lp.gravity = Gravity.CENTER;

        rating = new RatingBar(this);
        rating.setLayoutParams(lp);

        rating.setNumStars(5);
        rating.setStepSize(1);
        //add ratingBar to linearLayout
        linearLayout.addView(rating);


        popDialog.setIcon(android.R.drawable.star_big_on);
        popDialog.setTitle(R.string.set_puntuation_title);
        popDialog.setCancelable(false);
        popDialog.setMessage(getString(R.string.chofer_del_viaje).concat(currentTravel.getCodTravel()).concat(" ")
                .concat(getString(R.string.fecha)).concat(": ").concat(currentTravel.getMdate()).concat(" ")
                .concat(getString(R.string.origen)).concat(": ").concat(currentTravel.getNameOrigin()).concat(" ")
                .concat(getString(R.string.destino)).concat(": ").concat(currentTravel.getNameDestination()));

        //add linearLayout to dailog
        linearLayout.setGravity(Gravity.CENTER);
        popDialog.setView(linearLayout);


        // Button OK
        popDialog.setPositiveButton(getString(R.string.enviar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rating.getProgress();
                        dialog.dismiss();
                        servicesCalificateDriver(rating.getProgress());
                    }

                })

                // Button Cancel
                .setNegativeButton(getString(R.string.omitir),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                servicesCalificateDriver(-1);
                            }
                        });

        if(popDialog != null) {
            popDialog.create();
            popDialog.show();
        }

    }

    public void servicesCalificateDriver(int stars){

        if(currentTravel != null) {

            if (this.daoTravel == null) {
                this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            }


            try {
                CalificationStar starCalification = new CalificationStar();
                starCalification.setStar(stars);
                starCalification.setIdTravel(currentTravel.getIdTravel());

                Call<String> call = this.daoTravel.calificateDriver(currentTravel.getIdDriverKf(),starCalification);
                //Call<Boolean> call = this.daoTravel.calificateDriver(currentTravel.getIdTravel(),stars);

                Log.d("Call request", call.request().toString());

                call.enqueue(new Callback<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        Toast.makeText(getApplicationContext(), getString(R.string.puntuacion_enviada), Toast.LENGTH_LONG).show();
                        currentTravel = null;
                        infoTravelEntityLite=null; //Agregado by sf el 15/05/2019
                        gloval.setGv_travel_current_lite(null); //Agregado by sf el 15/05/2019
                    //    btnSolicitarViajesMaster.setVisibility(View.VISIBLE);
                        gloval.setGv_travel_current(null);
                        setInfoTravel();
                    }

                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                    }


                });

            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            finally {
                this.daoTravel = null;
            }
        }

    }

    public void serviceAllTravel() {

        this.apiService = HttpConexion.getUri().create(ServicesTravel.class);
        Call<reasonEntity> call = this.apiService.obtIdMotivo(2);

        Log.d("Call request", call.request().toString());

        call.enqueue(new Callback<reasonEntity>() {
            @Override
            public void onResponse(Call<reasonEntity> call, Response<reasonEntity> response) {

                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));

                if (response.code() == 200) {


                    list = (List<reason>) response.body().getReason();
                    showMotivosDeCancelacion(true,"");



                } else if (response.code() == 404) {

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(HomeClienteNewStyle.this).create();
                    alertDialog.setTitle("ERROR" + "(" + response.code() + ")");
                    alertDialog.setMessage(response.errorBody().source().toString());


                    Log.w("***", response.errorBody().source().toString());


                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }

            @Override
            public void onFailure(Call<reasonEntity> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content),
                        "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    //region Viaje
    @SuppressLint("WrongViewCast")
    private void addViewViaje(){


        constraintLayoutNuevoViaje =  findViewById(R.id.include3);
        constraintLayoutFlete = findViewById(R.id.content_flete);
        constraintLayoutFly = findViewById(R.id.content_fly);
        View btnCerrarNewAddress= findViewById(R.id.btn_close_new_viaje);
        clClassicAddressSection = findViewById(R.id.section_classic_address);
        clCustomAddressSection = findViewById(R.id.section_custom_address);
        textCustomAddressOrigen = findViewById(R.id.txt_custom_address_from);
        textViewCustomAddressDestino = findViewById(R.id.txt_custom_address_to);

        constraintLayoutFly.setVisibility(View.GONE);
        constraintLayoutFlete.setVisibility(View.GONE);

        showCustomAddressSectionInAddViaje(false);

        textViewDistancia=  findViewById(R.id.distanceTravel);
        textViewMonto=  findViewById(R.id.amountEstimate);
        spinner =  findViewById(R.id.spinner);

        isFleetTravel =  findViewById(R.id.isFleetTravel);

        np =  findViewById(R.id.isFleetTravelAssistance);
        np.setMinValue(0);
        np.setMaxValue(10);
        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                isFleetTravelAssistance = newVal;
            }
        });


        isFly =  findViewById(R.id.isFly);
        hoursAribo = findViewById(R.id.txt_hoursAribo);
        terminal = findViewById(R.id.txt_terminalnew);
        airlineCompany =  findViewById(R.id.txt_airlineCompany);
        flyNumber =  findViewById(R.id.txt_flyNumber);


        btnrequetTravelNow =  findViewById(R.id.btn_requetTravelNow);
        btnrequetTravelNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isCustomSelectAddress /*&& amountStimate==0*/){
                    showMessage(getString(R.string.espere_monto_viaje),3);
                }else if(isCustomSelectAddress && "".equals(textViewCustomAddressDestino.getText().toString()) ){
                    showMessage(getString(R.string.seleccionar_destino),Globales.StatusToast.FAIL);
                }
                else{
                    requestTravel();
                }

            }
        });

        btnCerrarNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeNewTravelViewClassic();
            }
        });
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or ""
     */
    public static String getUserCountry(Context context) {
        try {
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

    private void showCustomAddressSectionInAddViaje(boolean isVisible) {

        if(isVisible)
        {
            clCustomAddressSection.setVisibility(View.VISIBLE);
            clClassicAddressSection.setVisibility(View.GONE);
        }
        else{
            clCustomAddressSection.setVisibility(View.GONE);
            clClassicAddressSection.setVisibility(View.VISIBLE);
        }

    }

    //region Viaje
    @SuppressLint("WrongViewCast")
    private void addViewChooseAddress(){
        constraintLayoutSelectTypeAddres = findViewById(R.id.view_select_type_address);
        View cardViewClassicAddress= findViewById(R.id.card_classic_address);
        View cardViewCustomAddress= findViewById(R.id.card_custom_address);
        View btnCerrarChooseAddress= findViewById(R.id.btn_close_choose_address);

        btnCerrarChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeChooseAddressView();
            }
        });

        cardViewClassicAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustomSelectAddress=false;
                showCustomAddressSectionInAddViaje(false);
                showOrHideNewTravelContent(true);
            }
        });

        cardViewCustomAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCustomSelectAddress=true;
                showCustomAddressSectionInAddViaje(true);
                showOrHideNewTravelContent(true);
            }
        });

    }

    private void closeChooseAddressView()
    {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                -constraintLayoutSelectTypeAddres.getHeight());                // toYDelta
        animate.setDuration(300);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                constraintLayoutSelectTypeAddres.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        constraintLayoutSelectTypeAddres.startAnimation(animate);
    }

    private void openChooseAddressView()
    {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -constraintLayoutSelectTypeAddres.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                constraintLayoutNuevoViaje.setVisibility(View.GONE);
                constraintLayoutSelectTypeAddres.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        constraintLayoutSelectTypeAddres.startAnimation(animate);
    }

    private void closeNewTravelViewClassic()
    {
        if(constraintLayoutNuevoViaje.getVisibility()!=View.GONE)
        {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,  // fromYDelta
                    -constraintLayoutNuevoViaje.getHeight());                // toYDelta
            animate.setDuration(300);


            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    constraintLayoutNuevoViaje.setVisibility(View.GONE);
                  //  HomeClientFragmentNewStyle.clearRouteMap();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            constraintLayoutNuevoViaje.startAnimation(animate);
        }


    }

    private void openNewTravelViewClassic()
    {

        constraintLayoutNuevoViaje.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -constraintLayoutNuevoViaje.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);

        constraintLayoutNuevoViaje.startAnimation(animate);
    }


    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.isFly:
                if (checked){
                    constraintLayoutFly.setVisibility(View.VISIBLE);
                }
                else{
                    constraintLayoutFly.setVisibility(View.GONE);
                }
                break;
            case R.id.isFleetTravel:
                if (checked){
                    constraintLayoutFlete.setVisibility(View.VISIBLE);
                }
                else{
                    constraintLayoutFlete.setVisibility(View.GONE);
                }

                break;

        }
    }

    private void getDistanceCalculated()
    {
        Double result=0d;
        try{

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void requestTravel() {

    }


    //endregion

    //region RESERVA
    private void addViewReserva(){
        contentInfoReervation = findViewById(R.id.include2);

        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        setCategoriaTipoVehiculo();


        fromDateEtxt =  findViewById(R.id.txtdateReervation);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);

        fromTimeEtxt = findViewById(R.id.txtTimeReervation);
        fromTimeEtxt.setInputType(InputType.TYPE_NULL);


        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        fromTimeEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimePickerDialog.show();

            }
        });

        fromTimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        fromTimeEtxt.setText(hourOfDay + ":" + minute);

                    }
                },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);


        btnrequertReser = findViewById(R.id.btnrequertReser);
        btnrequertReser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatosReserva();
            }
        });

    }

    private void validarDatosReserva(){
        String fecha= fromDateEtxt.getText().toString().trim();
        String tiempo= fromTimeEtxt.getText().toString().trim();

        if (location.isEmpty()){
            showMessage(getString(R.string.seleccionar_origen),0);
        }else  if (destination.isEmpty()){
            showMessage(getString(R.string.seleccionar_destino),0);
        }else if (fecha.isEmpty()){
            showMessage(getString(R.string.seleccionar_fecha),0);
        }else if (tiempo.isEmpty()){
            showMessage(getString(R.string.seleccionar_hora),0);
        }else {
            /*if (amountStimate==0){
                showMessage(getString(R.string.espere_monto_viaje),3);
            }else {
                requestReserva(fecha,tiempo);
            }*/
        }
    }

    public void setCategoriaTipoVehiculo(){
        List<String> list = new ArrayList<String>();

        if(gloval.getGv_listvehicleType()!=null)
        {
            VEHYCLETYPE = new String[gloval.getGv_listvehicleType().size()];
            for (int i =0 ;i< gloval.getGv_listvehicleType().size();i++)
            {
                list.add("Tipo De Vehiculo: "+gloval.getGv_listvehicleType().get(i).getVehiclenType());
                listCatgoryId.add(gloval.getGv_listvehicleType().get(i).getIdVehicleType());
            }
        }
        else{
            VEHYCLETYPE = new String[1];
        }
        list.toArray(VEHYCLETYPE);

        spinnerTipoVehiculo.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoVehiculo.setAdapter(dataAdapter);

    }

    private void showReserva(boolean status){
        if (status){
            contentInfoReervation.setVisibility(View.VISIBLE);
        }else {
            contentInfoReervation.setVisibility(View.GONE);
        }

    }


    //endregion

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


    public void showMessage(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status);
    }

    @Override
    public void activarGps() {

    }

    @Override
    public void ocultarActivarGps() {

    }

    @Override
    public void showDialogTravelInfo() {
        if (infoTravelEntityLite!=null){
            showReserva(false);
            showOrHideNewTravelContent(false);

            TravelInfoDialog dialogTravelInfo = new TravelInfoDialog(infoTravelEntityLite,HomeClienteNewStyle.this);
            dialogTravelInfo.show(getSupportFragmentManager(), "DialogTravelInfo");
            dialogTravelInfo.setCancelable(false);
        }else {
            showMessage(getString(R.string.no_viaje_curso),0);
        }
    }

    @Override
    public void salirApp() {
        fn_exit();
    }

    //Notificacioens
    public void fn_gotonotification()
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame_client,new NotificationsFrangment()).commit();
    }

    public void fn_gotoreservation()
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame_client,new ReservationsFrangment()).commit();
    }

    private void  fn_chat(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame_client, new FragmentChatPassenger()).commit();
    }

    private void fn_reporte(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame_client, new FragmentReporte()).commit();
    }

    private void createNotificationChannels(Context context ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Utils.createNotificationChannelsClient(context);
        }
    }

    private void loadParamsFromApi()
    {
        try{

            ServicesLoguin apiServiceLoguin = HttpConexion.getUri().create(ServicesLoguin.class);
            Call<List<paramEntity>> call = apiServiceLoguin.getParams();
            call.enqueue(new Callback<List<paramEntity>>() {
                @Override
                public void onResponse(Call<List<paramEntity>> call, Response<List<paramEntity>> response) {
                    try
                    {
                        if(response.isSuccessful())
                        {
                            List<paramEntity> listParam = response.body();
                            gloval.setGv_param(listParam);
                            Utils.saveParamsToLocalPreferences(getApplicationContext(), listParam);
                            configureParams();
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

    private void configureParams()
    {
        initializeGlovalIfNull();
        int PARAM_35_USA_RESERVAS =  Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_35_USA_RESERVAS).getValue());// PUEDE SOLICITAR  RESERVA
        int PARAM_36_USA_VIAJES =  Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_36_USA_VIAJES).getValue());// PUEDE SOLICITAR  VIAJES

        if(PARAM_35_USA_RESERVAS != 1){
       //     btnSolicitarReserva.setVisibility(View.GONE);
        }

        if(PARAM_36_USA_VIAJES != 1){
         //   btnSolicitarViaje.setVisibility(View.GONE);
        }
    }

    private void initializeGlovalIfNull()
    {
        if(gloval==null){
            gloval = ((GlovalVar)getApplicationContext());
        }
    }

    private void checkNeedLogin()
    {
        String needLogin =  Utils.getValueFromSharedPreferences(getApplicationContext(), Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_1_CLIENT);
        String needLoginV4_4_1 =  Utils.getValueFromSharedPreferences(getApplicationContext(), Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_4_1_CLIENT);
        boolean isDisabled = Utils.isInstanceDisabled();

        if("".equals(needLogin) || "".equals(needLoginV4_4_1)){
            fn_exit();
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



}
