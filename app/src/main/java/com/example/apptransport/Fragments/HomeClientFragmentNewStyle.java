package com.example.apptransport.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle;
import com.apreciasoft.mobile.asremis.Adapter.AutocompleteAdapter;
import com.apreciasoft.mobile.asremis.Adapter.VehicleSelectionAdapter;
import com.apreciasoft.mobile.asremis.Entity.AutoCompletePredictionWithType;
import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.CarSelectionItem;
import com.apreciasoft.mobile.asremis.Entity.CompanyData;
import com.apreciasoft.mobile.asremis.Entity.CompanyDataOrigenPactado;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.DestinationEntity;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntityLite;
import com.apreciasoft.mobile.asremis.Entity.OriginEntity;
import com.apreciasoft.mobile.asremis.Entity.PointToPointItem;
import com.apreciasoft.mobile.asremis.Entity.PointToPointMaster;
import com.apreciasoft.mobile.asremis.Entity.PriceAndIsMinimum;
import com.apreciasoft.mobile.asremis.Entity.TravelBodyEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelEntity;
import com.apreciasoft.mobile.asremis.Entity.VehicleType;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.reason;
import com.apreciasoft.mobile.asremis.Entity.reasonEntity;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.SelectAddressMapActivity;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.AnchorSheetBehavior;
import com.apreciasoft.mobile.asremis.Util.CallbackActivity;
import com.apreciasoft.mobile.asremis.Util.DirectionApiCallback;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.ui.IconGenerator;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.mercadopago.android.px.internal.callbacks.RecyclerItemClickListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle.companyData;
import static com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle.currentTravel;
import static com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle.gloval;


public class HomeClientFragmentNewStyle  extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private static final int REQUEST_FOR_SELECT_ADDRESS_MAP=10;
    private static String G_MAP_API_KEY;
    static GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    static GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient fusedLocationClient;
    ServicesTravel apiService = null;

    Address mLastLocationAddress;
    public static Location mLastLocation;
    List<reason> list = null;
    Integer motivo = 0;
    public  static  String nameLocation;
    public static PolylineOptions options;
    private static Marker mCurrLocationMarker, mCarLocationMarker, mDestinationMarker;

    private Polyline routePolilines;
    private Marker routeDestinationMarker, routeDestinationMarker2;
    private Marker routeOriginMarker, routeOriginMarker2;
    public static   boolean isFistLocation,visible_progress = true;
    public static ArrayList<LatLng> MarkerPoints;
    public static   Timer timerblink;
    public static Location getmLastLocation() {
        return mLastLocation;
    }
    public static View view;
    public static TextView txtStatus;
    public static StateProgressBar stateProgressBar;
    private InfoTravelEntityLite infoTravelEntityLite= null;

    private boolean isTxtFromFocused=false;
    private String predictionSelectedAddressFullFrom;
    private String predictionSelectedAddressShortFrom;
    private LatLng latLngSelectedFrom;



    public ServicesTravel daoTravel = null;

    public static String[] satusTravel;
    CallbackActivity iCallback;




    private ListenerFragmentClient listenerFragmentClient;
    /*Para controles de info del viaje*/


    private static TextView textViewNombre,textViewTelefono,textViewOrigen,textViewDestino,textViewTiempo,
            textViewDominio,textViewKilometro,textViewMonto;
    private static Button buttonCancelarViaje;
    private static TextView txtEstimatedTime;
    private static BitmapDrawable bitmapDrawableIconDestination;
    private static String mTiempoLlegadaText;
    private static Date lastTimeGetDestinationCalculation;
    private static LinearLayout expectedTimeLayout;

    private static TextView txtStarsDriver;  //txt_stars_driver
    private static ImageView imgStarsDriver; //img_rating

    //Para Bottom Sheet de viaje en curso
    private BottomSheetBehavior sheetBehaviorTravelInfo;
    public  LinearLayout bottomLayoutTravelInfo;

    //Controles para Bottom Sheet para elegir que desea hacer
    private AnchorSheetBehavior sheetBehaviorWhereToGo;
    private LinearLayout tapactionlayoutWhereToGo;
    private View bottomSheetWhereToGo;

    //Para Bottom Sheet de elegir dirección
    private BottomSheetBehavior sheetBehaviorWriteAddress;
    public  LinearLayout bottomLayoutWriteAddress;
    private EditText txtAddressFrom;
    private EditText txtAddressTo;
    private EditText txtAddressFromAdditional;
    private ImageView imgCloseChooseAddress;
    private PlacesClient placesClient;
    private String userCountry;
    private RadioButton radioButtonTravelNow, radioButtonReserveFuture;
    private View reservationTimePickerLayout;
    /*DATE*/
    private TextView fromDateEtxt;
    private TextView fromTimeEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    private SimpleDateFormat dateFormatter;


    AutocompleteAdapter autocompleteAdapterAddressTo = null;
    RecyclerView rvAutocompleteTo = null;
    List<AutoCompletePredictionWithType> autocompletePredictionListTo;

    AutocompleteAdapter autocompleteAdapterAddressFrom = null;
    RecyclerView rvAutocompleteFrom = null;
    List<AutoCompletePredictionWithType> autocompletePredictionListFrom;

    AutocompleteSessionToken autocompleteSessionToken;

    //Para Bottom sheet Finalizar Elegir un viaje
    private AnchorSheetBehavior sheetBehaviorFinishChooseTravel;
    private ConstraintLayout bottomLayoutFinishChooseTravel;
    private LatLng initLatLngOnSlide;
    private Button btnAskTravel;
    private ImageView btnCloseBottomLayout;
    VehicleSelectionAdapter vehicleSelectionAdapter = null;
    RecyclerView rvVehicleSelection = null;
    List<CarSelectionItem> vehicleSelectionDataList;
    ImageView imgLeftArrowVehicles;
    ImageView imgRightArrowVehicles;

    private String addressToTextFull;
    private LatLng addressToLatLng;
    private String addressFromFull;
    private LatLng addressFromLatLng;
    private boolean isCustomSelectAddress=false;
    private String typeVehicleSelected;
    public ProgressDialog loading,loadingGloval;


    //Distancia y Precio
    private TextView textViewDistancia,textViewMontoCalculated, txtInfoForPrice;
    public double distanceTravel = 0;
    public double amountStimate = 0;
    public double amountOriginPac= 0;
    private boolean isTravelToRequestPointToPoint=false;


    //Flete
    private BottomSheetBehavior bottomSheetBehaviorChooseFlete;
    private TextView txtCantAyudantesMain;
    private Button btnAceptCantidadAyudantes;
    private CheckBox chkNeedFleet;
    private ImageView imgFleteCheck;
    public  LinearLayout bottomLayoutChooseFlete;
    private NumberPicker numberPickerAyudantes;
    private int fleetTravelAssistanceNumber = 0;
    private CardView cardViewNeedFlete;


    //Vuelo
    private BottomSheetBehavior bottomSheetBehaviorFlight;
    public  LinearLayout bottomLayoutChooseFlight;
    private CardView cardViewNeedFlight;
    private TextView txtDataFlightMain;

    private EditText oursAribo;
    private EditText terminal;
    private EditText airlineCompany;
    private EditText flyNumber;
    private Button btnAceptFlight;


    private boolean shouldCallFetchPlaceFrom;
    public HomeClientFragmentNewStyle() {

    }

    public HomeClientFragmentNewStyle(ListenerFragmentClient listenerFragmentClient) {
        this.listenerFragmentClient= listenerFragmentClient;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_home_client_new_style,container,false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        executeTimerBlink(); // ACTIVAR EFECTO BLINK

        return view;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            SupportMapFragment fr =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
            if(fr == null) {
                SupportMapFragment mMap =  ((SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.gmap));
                mMap.getMapAsync(this);
            }
            else {
                fr.getMapAsync(this);
            }
        setgMapApiKey();
        mLastLocationAddress=null;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        HomeClientFragmentNewStyle.txtStatus = getActivity().findViewById(R.id.txtStatus);
        stateProgressBar =  getActivity().findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.enableAnimationToCurrentState(true);
        mTiempoLlegadaText =  getActivity().getString(R.string.tiempo_llegada);
        satusTravel = new String[]{getString(R.string.aceptado), getString(R.string.chofer_en_camino), getString(R.string.en_curso)};
        stateProgressBar.setStateDescriptionData(satusTravel);
        txtEstimatedTime = getActivity().findViewById(R.id.txt_estimated_time);
        lastTimeGetDestinationCalculation = null;
        bitmapDrawableIconDestination = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_pin_red);

        //Nuevos de activity
        textViewDistancia = view.findViewById(R.id.distanceTravel);
        textViewMontoCalculated = view.findViewById(R.id.amountEstimate);
        txtInfoForPrice = view.findViewById(R.id.txtInfoForPrice);

        addViewInfoTravel();
        setPhoto();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(recibeNotifiacionSocket, new IntentFilter("update-loaction-driver"));

        initializeGooglePlaces();
        userCountry = Utils.getUserCountry(getContext());



        configureBottomSheetForWhereToGo();
        configureBottomSheetWriteAddress();
        configureBottomSheetFinishChooseTravel();
        configureBottomSheetFlete();
        configureBottomSheetFlight();
        configureBottomSheetTravelInfo();

        configureRecyclerViewAddressFrom();
        configureRecyclerViewAddressTo();
        configureRecyclerViewVehicleSelection();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iCallback = (CallbackActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            //stop location updates when Activity is no longer active
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case REQUEST_FOR_SELECT_ADDRESS_MAP:
                    if (resultCode == RESULT_OK) {
                        addressSelectedFromMap(data);
                    }
                    break;
            }
        } catch (Exception ex) {

        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.map_style_uber));

            if (!success) {
                Log.e("MAP_ERROR", "Style parsing failed.");
            }
        } catch (Exception e) {
            Log.e("MAP_ERROR", "Style parsing failed.");
        }

        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMapToolbarEnabled(false);


                if (MarkerPoints != null) {
                    MarkerPoints.clear();

                    if(mGoogleMap != null)
                    {
                        mGoogleMap.clear();
                    }

                }

            } else {
                //Request Location Permission
                checkLocationPermission();
                if (MarkerPoints != null) {
                    MarkerPoints.clear();

                    if(mGoogleMap != null)
                    {
                        mGoogleMap.clear();
                    }
                }
            }

        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

            if (MarkerPoints != null) {
                MarkerPoints.clear();

                if(mGoogleMap != null)
                {
                    mGoogleMap.clear();
                }
            }
        }

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Setting onclick event listener for the map
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE ) {
                    initLatLngOnSlide=null;
                }
            }
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        try {


            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);



                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {

                    LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

                }

            }
        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {

        Log.d("onLocationChanged","onLocationChanged");

        if(getActivity() != null) {

            Geocoder gCoder = new Geocoder(getActivity());
            List<Address> addresses = null;
            try {
                addresses = gCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                mLastLocationAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < mLastLocationAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(mLastLocationAddress.getAddressLine(i)).append("\n");
                }
                String strAdd = mLastLocationAddress.getAddressLine(0);
                HomeClientFragmentNewStyle.nameLocation = strAdd.toString();

                mLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (isFistLocation) {
                    //move map camera
                    isFistLocation = false;
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(30));
                }
                setTextToAddressFrom();
                addCurrentLocationMarker();
            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getApplicationContext(), R.string.permiso_denegado, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void initializeGooglePlaces() {
        try
        {
            Places.initialize(getContext(), G_MAP_API_KEY);
            placesClient = Places.createClient(getContext());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    getString(R.string.agencia_debe_conf_gmap),
                    Snackbar.LENGTH_LONG)
                    .setDuration(9000).show();
        }
    }


    private void configureBottomSheetFlete()
    {
        cardViewNeedFlete = view.findViewById(R.id.card_view_need_flete);
        bottomLayoutChooseFlete = view.findViewById(R.id.panel_for_finish_choose_flete_layout);
        numberPickerAyudantes =  view.findViewById(R.id.isFleetTravelAssistance);
        txtCantAyudantesMain = view.findViewById(R.id.txt_cant_ayudantes_main);
        btnAceptCantidadAyudantes = view.findViewById(R.id.btn_acept_helpers);
        chkNeedFleet = view.findViewById(R.id.chkNeedFleet);
        imgFleteCheck = view.findViewById(R.id.imgFleteCheck);

        bottomSheetBehaviorChooseFlete = BottomSheetBehavior.from(bottomLayoutChooseFlete);
        bottomSheetBehaviorChooseFlete.setState(BottomSheetBehavior.STATE_HIDDEN);


        numberPickerAyudantes.setMinValue(0);
        numberPickerAyudantes.setMaxValue(10);
        numberPickerAyudantes.setWrapSelectorWheel(true);
        setCantAyudantes();

        cardViewNeedFlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviorChooseFlete.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btnAceptCantidadAyudantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fleetTravelAssistanceNumber = numberPickerAyudantes.getValue();
                setCantAyudantes();
                bottomSheetBehaviorChooseFlete.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        mostrarOcultarFlete();
    }

    private void mostrarOcultarFlete()
    {
        try{
            int muestraFlete = getParamValue(Globales.ParametrosDeApp.PARAM_105_MUESTRA_FLETE_EN_APP_PASAJERO,0,gloval);
            cardViewNeedFlete.setVisibility(muestraFlete==0?View.GONE: View.VISIBLE);
        }
        catch (Exception ex ){
            ex.printStackTrace();
        }
    }

    private void configureBottomSheetFlight(){
        cardViewNeedFlight = view.findViewById(R.id.card_view_need_flight);
        bottomLayoutChooseFlight = view.findViewById(R.id.panel_for_finish_choose_flight_layout);
        txtDataFlightMain = view.findViewById(R.id.txt_ver_datos_flight);
        btnAceptFlight = view.findViewById(R.id.btn_acept_flight);
        oursAribo = view.findViewById(R.id.txt_hoursAribo);
        terminal = view.findViewById(R.id.txt_terminalnew);
        airlineCompany = view.findViewById(R.id.txt_airlineCompany);
        flyNumber = view.findViewById(R.id.txt_flyNumber);

        bottomSheetBehaviorFlight = BottomSheetBehavior.from(bottomLayoutChooseFlight);
        bottomSheetBehaviorFlight.setState(BottomSheetBehavior.STATE_HIDDEN);
        showOrHIdeFlightDataText();

        chkNeedFleet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCantAyudantes();
            }
        });

        bottomSheetBehaviorFlight.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Utils.hideKeyboard(view.getContext(),view);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        cardViewNeedFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviorFlight.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btnAceptFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehaviorFlight.setState(BottomSheetBehavior.STATE_HIDDEN);
                showOrHIdeFlightDataText();
            }
        });
    }

    private void setCantAyudantes()
    {
        imgFleteCheck.setVisibility(chkNeedFleet.isChecked() ? View.VISIBLE: View.GONE);


        if(fleetTravelAssistanceNumber==0 || !chkNeedFleet.isChecked())
        {
            txtCantAyudantesMain.setVisibility(View.INVISIBLE);
        }
        else{
            txtCantAyudantesMain.setVisibility(View.VISIBLE);
            txtCantAyudantesMain.setText(getString(R.string.cantidad_ayudantes).concat(String.valueOf(fleetTravelAssistanceNumber)));
        }
    }

    private void showOrHIdeFlightDataText()
    {
        if(hasFlightData())
        {
            txtDataFlightMain.setVisibility(View.VISIBLE);

        }
        else{
            txtDataFlightMain.setVisibility(View.GONE);
        }
    }

    private boolean hasFlightData()
    {
        boolean oursAriboB=!TextUtils.isEmpty(oursAribo.getText().toString().trim());
        boolean terminalB= !TextUtils.isEmpty(terminal.getText().toString().trim());
        boolean airlineCompanyB= !TextUtils.isEmpty(airlineCompany.getText().toString().trim());
        boolean flyNumberB=  !TextUtils.isEmpty(flyNumber.getText().toString().trim());
        return  ( oursAriboB ||
                 terminalB ||
                airlineCompanyB ||
                flyNumberB);
    }

    private void configureRecyclerViewAddressTo()
    {
        rvAutocompleteTo =  view.findViewById(R.id.rv_address_to);
        autocompletePredictionListTo=new ArrayList<>();
        addItemMyLocationToListAddress(autocompletePredictionListTo);
        autocompleteAdapterAddressTo = new AutocompleteAdapter(autocompletePredictionListTo);
        rvAutocompleteTo.setAdapter(autocompleteAdapterAddressTo);
        rvAutocompleteTo.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvAutocompleteTo.setLayoutManager(llm);
        rvAutocompleteTo.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {

                if(autocompletePredictionListTo.get(position).autocompleteType== Globales.AutocompleteType.ADDRESS)
                {
                    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);
                    FetchPlaceRequest request = FetchPlaceRequest.builder(autocompletePredictionListTo.get(position).autocompletePrediction.getPlaceId(),placeFields)
                            .setSessionToken(autocompleteSessionToken)
                            .build();

                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @Override
                        public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                            if(fetchPlaceResponse.getPlace().getLatLng()!=null)
                            {
                                rvAutoCompleteSelectedItemResultTo(fetchPlaceResponse, position, true, 0, 0, null, null);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("autocomplete",e.getMessage(),e);
                        }
                    });
                } else if(autocompletePredictionListTo.get(position).autocompleteType== Globales.AutocompleteType.SELECT_ADDRESS_FROM_MAP)
                {
                    openMapForSelectAddress(Globales.TypeAddressAutocomplete.ADDRESS_TO);
                }

            }
        }));
    }

    private void rvAutoCompleteSelectedItemResultTo(FetchPlaceResponse fetchPlaceResponse, int position, boolean isSelectedFromRecyclerView, double lat, double lng, String addressFull, String addressShort) {

        if (isReservaSelectedAndValidOrIsTravel()) {
            initLatLngOnSlide = null;
            sheetBehaviorWriteAddress.setState(BottomSheetBehavior.STATE_COLLAPSED);
            sheetBehaviorWhereToGo.setState(AnchorSheetBehavior.STATE_COLLAPSED);
            bottomLayoutFinishChooseTravel.setVisibility(View.VISIBLE);
            sheetBehaviorFinishChooseTravel.setState(AnchorSheetBehavior.STATE_EXPANDED);

            if (isSelectedFromRecyclerView) {
                String finalAddressResumed = autocompletePredictionListTo.get(position).autocompletePrediction.getPrimaryText(null).toString();
                String finalAddressFull = autocompletePredictionListTo.get(position).autocompletePrediction.getFullText(null).toString();
                callAddressSelectedTo(fetchPlaceResponse.getPlace().getLatLng(), finalAddressFull, finalAddressResumed);
                setBtnAskTravelText();
            } else {
                callAddressSelectedTo(new LatLng(lat,lng), addressFull, addressShort);
            }
        } else {
            showMessageOnParent(getString(R.string.must_select_date_reserve));
        }

    }

    private void openMapForSelectAddress(int type)
    {
        Intent intent = new Intent(getContext(), SelectAddressMapActivity.class);
        intent.putExtra("lat",getmLastLocation().getLatitude());
        intent.putExtra("lon",getmLastLocation().getLongitude());
        intent.putExtra("type", type);
        startActivityForResult(intent, REQUEST_FOR_SELECT_ADDRESS_MAP);
    }


    private boolean isReservaSelectedAndValidOrIsTravel(){
        boolean result=true;
        if(isReservation()){
            if(     TextUtils.isEmpty(fromDateEtxt.getText().toString()) ||
                    TextUtils.isEmpty(fromTimeEtxt.getText().toString()))
            {
                result=false;
            }
        }
        return result;
    }

    private void setBtnAskTravelText() {
        btnAskTravel.setText(radioButtonReserveFuture.isChecked() ? getString(R.string.solicitar_reserva) : getString(R.string.solicitar_viaje));
    }

    private void configureRecyclerViewAddressFrom()
    {
        rvAutocompleteFrom =  view.findViewById(R.id.rv_address_from);
        rvAutocompleteFrom.setVisibility(View.GONE);

        autocompletePredictionListFrom=new ArrayList<>();
        addItemMyLocationToListAddress(autocompletePredictionListFrom);
        autocompleteAdapterAddressFrom = new AutocompleteAdapter(autocompletePredictionListFrom);
        rvAutocompleteFrom.setAdapter(autocompleteAdapterAddressFrom);
        rvAutocompleteFrom.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvAutocompleteFrom.setLayoutManager(llm);


        predictionSelectedAddressFullFrom = null;
        predictionSelectedAddressShortFrom = null;
        latLngSelectedFrom = null;

        rvAutocompleteFrom.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {
                fetchPlaceFrom(position, true, true);
                shouldCallFetchPlaceFrom=false;

            }
        }));
    }

    private void fetchPlaceFrom(final int autocompletePredictionsIndex, final boolean setFocusToAddressTo, final boolean isSelectedFromRecyclerView)
    {
        if(autocompletePredictionListFrom!=null && autocompletePredictionListFrom.size()>0 && autocompletePredictionListFrom.get(autocompletePredictionsIndex)!=null){
            if(autocompletePredictionListFrom.get(autocompletePredictionsIndex).autocompleteType== Globales.AutocompleteType.ADDRESS)
            {
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);
                FetchPlaceRequest request = FetchPlaceRequest.builder(autocompletePredictionListFrom.get(autocompletePredictionsIndex).autocompletePrediction.getPlaceId(),placeFields)
                        .setSessionToken(autocompleteSessionToken)
                        .build();

                placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        try{
                            if(fetchPlaceResponse!=null && fetchPlaceResponse.getPlace().getLatLng()!=null)
                            {
                                autocompleteSelectionFrom(fetchPlaceResponse, autocompletePredictionsIndex, setFocusToAddressTo, isSelectedFromRecyclerView, null,"","");
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
            else if( autocompletePredictionListFrom.get(autocompletePredictionsIndex).autocompleteType== Globales.AutocompleteType.SELECT_ADDRESS_FROM_MAP)
            {
                if(isSelectedFromRecyclerView)
                {
                    openMapForSelectAddress(Globales.TypeAddressAutocomplete.ADDRESS_FROM);
                }
            }
        }
    }

    private void autocompleteSelectionFrom(FetchPlaceResponse fetchPlaceResponse, int autocompletePredictionsIndex, boolean setFocusToAddressTo, boolean isSelectedFromRecyclerView, LatLng latLngFromMap, String addressShortFromMap, String addressFullFromMap) {
        if(isSelectedFromRecyclerView)
        {
            AutocompletePrediction autocompletePrediction = autocompletePredictionListFrom.get(autocompletePredictionsIndex).autocompletePrediction;
            predictionSelectedAddressFullFrom = autocompletePrediction !=null  ? autocompletePrediction.getFullText(null).toString() : null;
            predictionSelectedAddressShortFrom = autocompletePrediction !=null  ? autocompletePrediction.getPrimaryText(null).toString():null;
            latLngSelectedFrom = fetchPlaceResponse.getPlace().getLatLng();
        }
        else {
            predictionSelectedAddressFullFrom = addressFullFromMap;
            predictionSelectedAddressShortFrom = addressShortFromMap;
            latLngSelectedFrom = latLngFromMap;
        }
        txtAddressFrom.setText(predictionSelectedAddressShortFrom !=null? predictionSelectedAddressShortFrom : predictionSelectedAddressFullFrom !=null? predictionSelectedAddressFullFrom : "");
        if(setFocusToAddressTo)
        {
            txtAddressTo.requestFocus();
        }

    }


    private void configureRecyclerViewVehicleSelection()
    {
        rvVehicleSelection =  view.findViewById(R.id.rv_vehicle_selection);
        imgLeftArrowVehicles = view.findViewById(R.id.imgMoreCarLeft);
        imgRightArrowVehicles = view.findViewById(R.id.imgMoreCarRight);
        vehicleSelectionDataList=new ArrayList<>();
        setListVehiclesTypes();
        //configureAnimationArrows();

        vehicleSelectionAdapter = new VehicleSelectionAdapter(vehicleSelectionDataList);
        rvVehicleSelection.setAdapter(vehicleSelectionAdapter);
        rvVehicleSelection.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false);

        rvVehicleSelection.setLayoutManager(llm);
        rvVehicleSelection.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,final int position) {
                setVehicleSelection(position);
                typeVehicleSelected = String.valueOf(vehicleSelectionDataList.get(position).getTypeVehicle());
                amountStimate = Utils.parseDouble(vehicleSelectionDataList.get(position).getPrice());
                amountOriginPac = vehicleSelectionDataList.get(position).getAmountOriginPac();
                setEstimatePriceToTxt(amountStimate, true, amountOriginPac);
                vehicleSelectionAdapter.notifyDataSetChanged();
            }
        }));
    }

    private void setVehicleSelection(int position){
        if(vehicleSelectionDataList!=null && vehicleSelectionDataList.size()>0)
        {
            for (CarSelectionItem item:vehicleSelectionDataList) {
                item.setSelected(false);
            }
            vehicleSelectionDataList.get(position).setSelected(true);
        }

    }

    private void configureAnimationArrows(){
        if(vehicleSelectionDataList!=null && vehicleSelectionDataList.size()>2)
        {
            imgLeftArrowVehicles.setVisibility(View.VISIBLE);
            imgRightArrowVehicles.setVisibility(View.VISIBLE);
            Animation animBlink;
            animBlink = AnimationUtils.loadAnimation(getContext(),
                    R.anim.blink);
            // start the animation
            imgLeftArrowVehicles.startAnimation(animBlink);
            imgRightArrowVehicles.startAnimation(animBlink);

        }
        else{
            imgLeftArrowVehicles.setVisibility(View.GONE);
            imgRightArrowVehicles.setVisibility(View.GONE);
        }
    }



    private void setListVehiclesTypes() {
        try {
            if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }
            Call<List<VehicleType>> call;
            if(Utils.isClienteCompany(gloval.getGv_id_profile())){
                call = this.daoTravel.getVehiclesTypeByCompanyId(gloval.getGv_id_cliet());
            }
            else{
                call = this.daoTravel.getVehiclesTypeGeneral();
            }

            call.enqueue(new Callback<List<VehicleType>>() {
                @Override
                public void onResponse(Call<List<VehicleType>> call, Response<List<VehicleType>> response) {
                    if(response.isSuccessful()){
                        try{
                            List<VehicleType> listVehicles = response.body();
                            if(listVehicles!=null){
                                setListVehiclesTypes(listVehicles);
                            }
                            else{
                                setListVehiclesTypes(HomeClienteNewStyle.gloval.getGv_listvehicleType());
                            }
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                            setListVehiclesTypes(HomeClienteNewStyle.gloval.getGv_listvehicleType());
                        }
                    }
                    else{
                        setListVehiclesTypes(HomeClienteNewStyle.gloval.getGv_listvehicleType());
                    }
                }

                @Override
                public void onFailure(Call<List<VehicleType>> call, Throwable t) {
                    t.printStackTrace();
                    setListVehiclesTypes(HomeClienteNewStyle.gloval.getGv_listvehicleType());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            setListVehiclesTypes(HomeClienteNewStyle.gloval.getGv_listvehicleType());
        }
    }

    private void setListVehiclesTypes(List<VehicleType> listVehicles)
    {
        try {
            List<String> list = new ArrayList<String>();
            vehicleSelectionDataList.clear();
            if (listVehicles != null && listVehicles.size()>0) {
                for (int i = 0; i < listVehicles.size(); i++) {
                    vehicleSelectionDataList.add(new CarSelectionItem(i==0,
                            listVehicles.get(i).getVehiclenType(),
                            listVehicles.get(i).getIdVehicleType(),
                            "0",
                            listVehicles.get(i).getVehicleMaxPeople(),
                            listVehicles.get(i),
                            0d,
                            i,
                            listVehicles.get(i).getListbenefitKm()
                    ));
                }
                setDefaultTypeVehicleSelected();
            }
            configureAnimationArrows();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setDefaultTypeVehicleSelected()
    {
        if(vehicleSelectionDataList!=null && vehicleSelectionDataList.size()>0)
        {
            typeVehicleSelected = String.valueOf(vehicleSelectionDataList.get(0).getTypeVehicle());
            setVehicleSelection(0);
        }

    }

    private void callAddressSelectedTo(LatLng latLngTo, String finalAddressFull, String finalAddressResumed) {
        try {
            addressFromLatLng = latLngSelectedFrom != null ? latLngSelectedFrom :new LatLng(getmLastLocation().getLatitude(), getmLastLocation().getLongitude());
            addressFromFull = predictionSelectedAddressFullFrom != null ? predictionSelectedAddressFullFrom : getCurrentNameLocationFull();
            String originAddressResumed = predictionSelectedAddressShortFrom != null ? predictionSelectedAddressShortFrom : getCurrentNameLocationResumed();
            addressToTextFull = finalAddressFull;
            addressToLatLng = latLngTo;
            setDistanceTravel(true, addressFromLatLng, addressToLatLng, originAddressResumed, finalAddressResumed);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // CALCULAMOS EL PRECIO ESTIMADO DEL VIAJE //
    public  void setDistanceTravel(final boolean showRouteOnMap, final LatLng originLatLng, final LatLng latLngDestination,final String addressOriginText,  final String addressDestinationText){
        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            textViewDistancia.setText(R.string.calculando);
            String gMapApiKey = G_MAP_API_KEY;

            LatLng destination = new LatLng(Double.parseDouble(String.valueOf(latLngDestination.latitude)), Double.parseDouble(String.valueOf(latLngDestination.longitude)));
            Utils.getDirectionBetweenTwoPointsApi(originLatLng, destination, gMapApiKey, new DirectionApiCallback() {
                @Override
                public void onDirectionSuccess(Direction direction) {
                    String distance = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
                    String time = direction.getRouteList().get(0).getLegList().get(0).getDuration().getText();
                    try{
                        if(showRouteOnMap)
                        {
                            setRouteMap(direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint(), addressOriginText, addressDestinationText);
                        }
                        distanceTravel = Utils.round(Double.parseDouble(direction.getRouteList().get(0).getLegList().get(0).getDistance().getValue())/1000,2);
                    }
                    catch (Exception ex) {
                        Toast.makeText(getContext(), R.string.error_calcular_distancia,Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                        distanceTravel = 0d;
                    }
                    textViewDistancia.setText(String.valueOf(distanceTravel).concat(getString(R.string.km)));
                    calculatePrice(originLatLng, latLngDestination);

                }

                @Override
                public void onDirectionError() {
                    textViewDistancia.setText(getString(R.string.km_0));
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            textViewDistancia.setText(getString(R.string.km_0));
        }
    }

    private void calculatePrice(LatLng latLngOriginPosition , LatLng latLngDestination){
        PointToPointItem pointToPointItem = getPointToPointIfExists(latLngOriginPosition, latLngDestination, HomeClienteNewStyle.pointToPointListItems);

        if(pointToPointItem!=null){

            setEstimatePriceToTxt(Utils.parseDouble(pointToPointItem.getPricePoint()),true, 0d);
            setPriceToAllVehiclesTypes(pointToPointItem.getPricePoint());
            isTravelToRequestPointToPoint=true;
        }
        else{
            getListBenefitsAndCalculateAllPrices(latLngOriginPosition, latLngDestination);
            isTravelToRequestPointToPoint=false;
        }
    }

    private PointToPointItem getPointToPointIfExists(LatLng latLngOriginPosition, LatLng latLngDestination, PointToPointMaster items )
    {
        PointToPointItem result=null;
        try {
            if(items!=null && items.getPoints()!=null && items.getPoints().size()>0) {
                for (int i = 0; i < items.getPoints().size(); i++) {
                    if(     isOriginInsidePointToPointLocation(latLngOriginPosition,items.getPoints().get(i)) &&
                            isDestinationInsidePointToPointLocation(latLngDestination,items.getPoints().get(i))
                    ){
                        result = items.getPoints().get(i);
                        break;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result=null;
        }
        return result;
    }

    private PointToPointItem getPointToPointIfExistsCompany(LatLng latLngOriginPosition, LatLng latLngDestination, List<PointToPointItem> items )
    {
        PointToPointItem result=null;
        try {
            if(items!=null && items.size()>0) {
                for (int i = 0; i < items.size(); i++) {
                    if(     isOriginInsidePointToPointLocation(latLngOriginPosition,items.get(i)) &&
                            isDestinationInsidePointToPointLocation(latLngDestination,items.get(i))
                    ){
                        result = items.get(i);
                        break;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result=null;
        }
        return result;
    }

    private boolean isOriginInsidePointToPointLocation(LatLng originLatLng, PointToPointItem itemPointToPoint){
        boolean result=false;
        Double distance;
        LatLng p2;
        try
        {
            p2 = new LatLng(Double.parseDouble(itemPointToPoint.getLatOrigin()),Double.parseDouble(itemPointToPoint.getLonOrigin()));
            distance = Utils.getDistanceBetweenTwoPoints(originLatLng,p2);
            if(distance>=0&& distance<=Globales.DISTANCE_FOR_POINT_TO_POINT){
                result=true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    private boolean isDestinationInsidePointToPointLocation(LatLng destinationLatLng, PointToPointItem itemPointToPoint){
        boolean result=false;
        Double distance;
        LatLng p2;

        try{
            p2 = new LatLng(Double.parseDouble(itemPointToPoint.getLatDestination()),Double.parseDouble(itemPointToPoint.getLonDestination()));
            distance = Utils.getDistanceBetweenTwoPoints(destinationLatLng,p2);
            if(distance>=0&& distance<=Globales.DISTANCE_FOR_POINT_TO_POINT){
                result=true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    private int getIsPointToPoint(){
        return isTravelToRequestPointToPoint?1:0;
    }

    private void getListBenefitsAndCalculateAllPrices(final LatLng latLngOriginPosition,LatLng latLngDestination ) {
        //TODO: Verificar el codigo siguiente
        int PARAM_57_BENEFICIO_X_KM_PARTICULARES =  Utils.parseInt(HomeClienteNewStyle.gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_57_BENEFICIO_X_KM_PARTICULARES).getValue());
        Call<List<BeneficioEntity>> call  = daoTravel.getListBenefitPerKm(PARAM_57_BENEFICIO_X_KM_PARTICULARES);

        call.enqueue(new Callback<List<BeneficioEntity>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<BeneficioEntity>> call, Response<List<BeneficioEntity>> response) {
                List<BeneficioEntity> listBenefits=null;
                if(response.isSuccessful()){
                    listBenefits = response.body();
                }

                InfoTravelEntity currentTravelTmp = new InfoTravelEntity();
                currentTravelTmp.setListBeneficio(listBenefits);
                currentTravelTmp.setIsRoundTrip(0);
                currentTravelTmp.setIsTravelComany(Utils.isClienteCompany(gloval.getGv_id_profile()) ? 1: 0);
                calculatePriceInAllVehicles(currentTravelTmp, latLngOriginPosition, latLngDestination, listBenefits);
            }

            public void onFailure(Call<List<BeneficioEntity>> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "ERROR (" + t.getMessage() + ")", Snackbar.LENGTH_LONG).show();
                textViewDistancia.setText(getString(R.string.km_0));
            }
        });
    }

    private void setPriceToAllVehiclesTypes(String price)
    {
        try{
            for (CarSelectionItem item : vehicleSelectionDataList) {
                item.setPrice(price);
            }
            vehicleSelectionAdapter.notifyDataSetChanged();
        }
        catch (Exception ex){

        }
    }

    private void calculatePriceInAllVehicles(InfoTravelEntity currentTravelTmp, final LatLng latLngOriginPosition, LatLng latLngDestination, List<BeneficioEntity> listBeneficiosDefault) {
        int PARAM_78_BAJADA_DE_BANDERA = Integer.parseInt(HomeClienteNewStyle.gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_78_BAJADA_DE_BANDERA).getValue());
        double PARAM_6 = Double.parseDouble(HomeClienteNewStyle.gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA).getValue());// PRECIO LISTA TIEMPO DE VUELTA
        Double price = 0d;
        List<paramEntity> params =  Utils.getParamsFromLocalPreferences(GlovalVar.getContextStatic());
        float PARAM_7_PORCENTAJE_AUMENTO = Utils.getValueFloatFromParameters(params, Globales.ParametrosDeApp.PARAM_7_PORCENTAJE_AUMENTO_HORA_NOCTURNA);
        String PARAM_8_HORA_NOCTURNA_FROM = params.get(Globales.ParametrosDeApp.PARAM_8_HORA_NOCTURNA_DESDE).getValue();
        String PARAM_9_HORA_NOCTURNA_TO = params.get(Globales.ParametrosDeApp.PARAM_9_HORA_NOCTURNA_HASTA).getValue();
        double distanceOrigenPactado = getDistanceOriginPac(latLngOriginPosition, latLngDestination);
        for (CarSelectionItem item : vehicleSelectionDataList) {
            if(currentTravelTmp.getIsTravelComany()==1)
            {
                PriceAndIsMinimum priceAndIsMinimum;
                priceAndIsMinimum = Utils.getMontoViajeEmpresa(item.getVehicleType(),
                        Utils.parseDouble(item.getVehicleType().getVehiclePriceKm()),
                        Utils.parseDouble(item.getVehicleType().getVehiclePriceKm()),
                        Utils.parseDouble(item.getVehicleType().getVehiclePriceHour()),
                        0d,
                        0,
                        0,
                        distanceTravel,
                        currentTravelTmp.getIsBenefitKmList(),
                        item.getListbenefitKm(), //currentTravelTmp.getListBeneficio(), //La lista de beneficio ahora viene en cada categoría
                        distanceTravel,
                        distanceTravel,
                        currentTravelTmp.getIsTravelComany(),
                        PARAM_6,
                        0d,
                        distanceOrigenPactado,
                        companyData.getCompanyParams().getTravelMinAmount(),
                        companyData.getCompanyParams().getTravelMinMode(),
                        companyData);
                price = priceAndIsMinimum.amount;

                item.setAmountOriginPac(priceAndIsMinimum.amountOriginPac);
            }
            else {
                //Si es un viaje particular, tengo que setear la lista de beneficio que tiene esa categoría del
                //vehículo en caso de tenerla. De lo contrario, dejar el que viene por default del parametro 57
                currentTravelTmp.setListBeneficio(item.getListbenefitKm() != null && item.getListbenefitKm().size() > 0 ? item.getListbenefitKm() : listBeneficiosDefault);
                PriceAndIsMinimum priceAndIsMinimum;
                priceAndIsMinimum = Utils.getMontoViajeParticular(PARAM_78_BAJADA_DE_BANDERA,
                        Utils.parseDouble(item.getVehicleType().getVehicleValorMin()), //PARAM_16_MONTO_MINIMO_VIAJE,
                        currentTravelTmp,
                        distanceTravel,
                        0,
                        distanceTravel,
                        Utils.parseDouble(item.getVehicleType().getVehiclePriceKm()),//PARAM_1_PRECIO_LISTA_X_KM,
                        PARAM_6,
                        PARAM_7_PORCENTAJE_AUMENTO,
                        PARAM_8_HORA_NOCTURNA_FROM,
                        PARAM_9_HORA_NOCTURNA_TO);
                price = priceAndIsMinimum.amount;
                item.setAmountOriginPac(0d);
            }
            item.setPrice(Utils.convertDoubleToStringPrice(price));
        }
        vehicleSelectionAdapter.notifyDataSetChanged();
        amountStimate = Utils.parseDouble(vehicleSelectionDataList.get(0).getPrice());
        amountOriginPac = vehicleSelectionDataList.get(0).getAmountOriginPac();
        setEstimatePriceToTxt(amountStimate, true, amountOriginPac);
    }

    /*
    1 - Punto / base pactada más cercano al Origen
    2 - Punto / base pactada más cercano al Destino
    3 - Punto / base pactada Inteligente (tomara la distancia mas corta bien sea por Origen o Destino)
     */
    private double getDistanceOriginPac(final LatLng latLngOriginPosition,final LatLng latLngDestinationPosition) {
        double result = 0d;
        double  distanceCalculatedOrigin;
        double  distanceCalculatedDestination;
        boolean isFirstTime=true;
        LatLng locationPactado;
        int TOPE_DISTANCIA_A_CALCULAR=100*1000; //100 KM

        try {
            if(Utils.isClienteCompany(gloval.getGv_id_profile()) &&
                    HomeClienteNewStyle.companyData!=null) {

                CompanyData companyData = HomeClienteNewStyle.companyData;
                if (    companyData.getOriginClientPactado() != null && companyData.getOriginClientPactado().size() > 0 &&
                        companyData.getAccountCompany() != null && companyData.getAccountCompany().size() > 0 &&
                        !"0".equals(companyData.getAccountCompany().get(0).getIsAddress())) {
                    String tipoBasePactada = companyData.getAccountCompany().get(0).getIsAddress();

                    for (CompanyDataOrigenPactado item : companyData.getOriginClientPactado()) {
                        locationPactado = new LatLng(Double.parseDouble(item.getLatPreOriginCom()), Double.parseDouble(item.getLongPreOriginCom()));
                        distanceCalculatedOrigin = Utils.getDistanceBetweenTwoPoints(latLngOriginPosition, locationPactado);
                        distanceCalculatedDestination = Utils.getDistanceBetweenTwoPoints(latLngDestinationPosition, locationPactado);

                        switch (tipoBasePactada) {
                            case "1":
                                if ((isFirstTime || distanceCalculatedOrigin < result) && distanceCalculatedOrigin <= TOPE_DISTANCIA_A_CALCULAR) { //distancia debe ser menor a 100 km
                                    result = distanceCalculatedOrigin;
                                    isFirstTime = false;
                                }
                                break;
                            case "2":
                                if ((isFirstTime || distanceCalculatedDestination < result) && distanceCalculatedDestination <= TOPE_DISTANCIA_A_CALCULAR) { //distancia debe ser menor a 100 km
                                    result = distanceCalculatedDestination;
                                    isFirstTime = false;
                                }
                                break;
                            case "3": {
                                if ((isFirstTime || distanceCalculatedOrigin < result) && distanceCalculatedOrigin <= TOPE_DISTANCIA_A_CALCULAR) { //distancia debe ser menor a 100 km
                                    result = distanceCalculatedOrigin;
                                    isFirstTime = false;
                                }

                                if ((isFirstTime || distanceCalculatedDestination < result) && distanceCalculatedDestination <= TOPE_DISTANCIA_A_CALCULAR) { //distancia debe ser menor a 100 km
                                    result = distanceCalculatedDestination;
                                    isFirstTime = false;
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result=0d;
        }

        return result/1000; //Debo devolver la distancia en KM
    }

    private Double getParamValue(int pos, Double defaultValue)
    {
        try {
            return Double.parseDouble(HomeClienteNewStyle.gloval.getGv_param().get(pos).getValue());
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private int getParamValue(int pos, int defaultValue, GlovalVar glovalVar)
    {
        try {
            return Integer.parseInt(glovalVar.getGv_param().get(pos).getValue());
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private void setEstimatePriceToTxt(double ammount, boolean setAmountEstimate, double ammountOriginPact)
    {
        if(setAmountEstimate)
        {
            amountStimate = ammount;
        }
        try{
            textViewMontoCalculated.setText(Utils.getPriceWithMoneySymbol(getContext(),Utils.convertDoubleToStringPrice(ammount)));
            if(ammountOriginPact!=0d) {
                txtInfoForPrice.setVisibility(View.VISIBLE);
            }
            else{
                txtInfoForPrice.setVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            textViewMontoCalculated.setText(Utils.getPriceWithMoneySymbol(getContext(), "0"));
        }


    }

    private void setTextToAddressFrom(){
        try{
            if(!isTxtFromFocused && latLngSelectedFrom ==null)
            {
                if((isWriteAddressLayoutOpen()) || !isWriteAddressLayoutOpen() )
                if(mLastLocationAddress!=null)
                {
                    txtAddressFrom.setText(getCurrentNameLocationResumed());
                    predictionSelectedAddressFullFrom = getCurrentNameLocationFull();
                    predictionSelectedAddressShortFrom = getCurrentNameLocationResumed();
                    latLngSelectedFrom = new LatLng( mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }else{
                    txtAddressFrom.setText("");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private String getCurrentNameLocationResumed(){
        String result="";
        if(mLastLocationAddress!=null) {
            String streetName = TextUtils.isEmpty(mLastLocationAddress.getThoroughfare()) ? TextUtils.isEmpty(mLastLocationAddress.getFeatureName()) ? getString(R.string.sin_nombre_calle) : mLastLocationAddress.getFeatureName() : mLastLocationAddress.getThoroughfare();
            String streetNumber = TextUtils.isEmpty(mLastLocationAddress.getSubThoroughfare()) ? getString(R.string.s_n) : mLastLocationAddress.getSubThoroughfare();
            result = streetName.concat( " ").concat(streetNumber);
        }
        return result;
    }

    private String getCurrentNameLocationFull(){
        String result="";
        if(mLastLocationAddress!=null) {
            result = mLastLocationAddress.getAddressLine(0);
        }
        return result;
    }

    private void setMapPaddingBotttom(Float offset) {
        //From 0.0 (min) - 1.0 (max) // bsExpanded - bsCollapsed;
        Float maxMapPaddingBottom = 1.0f;
        mGoogleMap.setPadding(0, 0, 0, Math.round(offset * maxMapPaddingBottom));

    }

    private void configureBottomSheetTravelInfo()
    {
        bottomLayoutTravelInfo = view.findViewById(R.id.bottom_sheet);
        sheetBehaviorTravelInfo = BottomSheetBehavior.from(bottomLayoutTravelInfo);
        if(currentTravel!=null){
            bottomLayoutTravelInfo.setVisibility(View.VISIBLE);
            bottomSheetWhereToGo.setVisibility(View.GONE);
        }
        else{
            bottomLayoutTravelInfo.setVisibility(View.GONE);
        }

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehaviorTravelInfo.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        ScrollView scroll = view.findViewById(R.id.scroll_inner_info_travel);
                        scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        ScrollView scroll = view.findViewById(R.id.scroll_inner_info_travel);
                        //btnBottomSheet.setText("Expand Sheet");
                        scroll.fullScroll(ScrollView.FOCUS_UP);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void configureBottomSheetWriteAddress()
    {
        bottomLayoutWriteAddress = view.findViewById(R.id.panel_for_client_choose_address);
        sheetBehaviorWriteAddress = BottomSheetBehavior.from(bottomLayoutWriteAddress);

        txtAddressFrom = view.findViewById(R.id.txt_address_from);
        txtAddressTo = view.findViewById(R.id.txt_address_to);
        txtAddressFromAdditional = view.findViewById(R.id.txt_address_from_additional);
        imgCloseChooseAddress = view.findViewById(R.id.img_close_choose_address);
        radioButtonTravelNow = view.findViewById(R.id.radio_travel_now);
        radioButtonReserveFuture = view.findViewById(R.id.radio_reservation_future);
        reservationTimePickerLayout = view.findViewById(R.id.reservationTimePickerLayout);
        reservationTimePickerLayout.setVisibility(View.GONE);

        fromDateEtxt =  view.findViewById(R.id.txtdateReervation);
        fromTimeEtxt = view.findViewById(R.id.txtTimeReervation);
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getContext(),R.style.DialogStyle, new DatePickerDialog.OnDateSetListener() {

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

        fromTimePickerDialog = new TimePickerDialog(getContext(),R.style.DialogStyle,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        fromTimeEtxt.setText(hourOfDay + ":" + minute);

                    }
                },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);



        isTxtFromFocused = false;

        Log.e("CONFIGURE","radioButtonTravelNow = CHECKED");

        radioButtonTravelNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonReserveFuture.setChecked(false);
                reservationTimePickerLayout.setVisibility(View.GONE);
            }
        });

        radioButtonReserveFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonTravelNow.setChecked(false);
                reservationTimePickerLayout.setVisibility(View.VISIBLE);
            }
        });

        enableOrDisableTravelNow();

        sheetBehaviorWriteAddress.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Utils.hideKeyboard(getContext(), HomeClientFragmentNewStyle.view);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Utils.showKeyboard(getContext(),txtAddressTo);
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Utils.hideKeyboard(getContext(), HomeClientFragmentNewStyle.view);

                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        txtAddressFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isTxtFromFocused=hasFocus;
                if(hasFocus){
                    rvAutocompleteFrom.setVisibility(View.VISIBLE);
                    rvAutocompleteTo.setVisibility(View.GONE);
                }
            }
        });

        txtAddressTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    rvAutocompleteTo.setVisibility(View.VISIBLE);
                    rvAutocompleteFrom.setVisibility(View.GONE);
                }
            }
        });

        txtAddressFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(autocompleteSessionToken!=null)
                {
                    Log.e("TOKEN", autocompleteSessionToken.toString());

                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setCountry(userCountry)
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(s.toString())
                            .build();

                    placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                        @Override
                        public void onSuccess(FindAutocompletePredictionsResponse response) {
                            autocompletePredictionListFrom.clear();
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                autocompletePredictionListFrom.add(new AutoCompletePredictionWithType(prediction, Globales.AutocompleteType.ADDRESS));
                            }
                            addItemMyLocationToListAddress(autocompletePredictionListFrom);
                            autocompleteAdapterAddressFrom.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("RESPONSE ERROR:", "ERROR", e);
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtAddressTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(autocompleteSessionToken!=null)
                {
                    Log.e("TOKEN", autocompleteSessionToken.toString());
                    FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                            .setCountry(userCountry)
                            .setSessionToken(autocompleteSessionToken)
                            .setQuery(s.toString())
                            .build();

                    placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                        @Override
                        public void onSuccess(FindAutocompletePredictionsResponse response) {
                            autocompletePredictionListTo.clear();
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                autocompletePredictionListTo.add(new AutoCompletePredictionWithType(prediction,Globales.AutocompleteType.ADDRESS));
                            }
                            addItemMyLocationToListAddress(autocompletePredictionListTo);
                            autocompleteAdapterAddressTo.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("RESPONSE ERROR:", "ERROR");
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgCloseChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehaviorWriteAddress.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void enableOrDisableTravelNow()
    {
        int param_36_is_travel_now_active = Utils.getValueIntFromParameters(HomeClienteNewStyle.gloval.getGv_param(),Globales.ParametrosDeApp.PARAM_36_USA_VIAJES);
        if(param_36_is_travel_now_active==0)
        {
            radioButtonTravelNow.setVisibility(View.GONE);
            radioButtonTravelNow.setChecked(false);
            radioButtonReserveFuture.setChecked(true);
            reservationTimePickerLayout.setVisibility(View.VISIBLE);
        }
        else{
            radioButtonTravelNow.setVisibility(View.VISIBLE);
            radioButtonTravelNow.setChecked(true);
            radioButtonReserveFuture.setChecked(false);
            reservationTimePickerLayout.setVisibility(View.GONE);
        }
    }


    private void callFetchPlaceFromInHalfSeconds()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(shouldCallFetchPlaceFrom)
                        {
                            Log.e("TIMER", "CALL FETCH");
                            fetchPlaceFrom(0, false, false);
                        }
                    }
                });

            }
        }, 1000);
    }
    private void addItemMyLocationToListAddress(List<AutoCompletePredictionWithType> listItems)
    {
        listItems.add(new AutoCompletePredictionWithType(null, Globales.AutocompleteType.SELECT_ADDRESS_FROM_MAP));
    }

    private void configureBottomSheetForWhereToGo(){
        tapactionlayoutWhereToGo = view.findViewById(R.id.tap_action_layout);
        bottomSheetWhereToGo = view.findViewById(R.id.panel_for_client_address_layout);
        Button btnWriteAddress = view.findViewById(R.id.btn_write_address);

        sheetBehaviorWhereToGo = AnchorSheetBehavior.from(bottomSheetWhereToGo);
        sheetBehaviorWhereToGo.setState(AnchorSheetBehavior.STATE_COLLAPSED);

        //anchor offset. any value between 0 and 1 depending upon the position u want
        sheetBehaviorWhereToGo.setAnchorOffset(0.5f);
        sheetBehaviorWhereToGo.setAnchorSheetCallback(new AnchorSheetBehavior.AnchorSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == AnchorSheetBehavior.STATE_COLLAPSED) {
                    //action if needed
                }

                if (newState == AnchorSheetBehavior.STATE_EXPANDED) {

                }

                if (newState == AnchorSheetBehavior.STATE_DRAGGING) {

                }

                if (newState == AnchorSheetBehavior.STATE_ANCHOR) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                float h = bottomSheet.getHeight();
                float off = h*slideOffset;

                switch (sheetBehaviorWhereToGo.getState()) {
                    case AnchorSheetBehavior.STATE_DRAGGING:
                        setMapPaddingBotttom(off);
                        //reposition marker at the center

                        if (mLastLocation != null){
                            //LatLng loc = mGoogleMap.getCameraPosition().target;
                            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        }
                        break;
                    case AnchorSheetBehavior.STATE_SETTLING:
                        setMapPaddingBotttom(off);
                        //reposition marker at the center
                        if (mLastLocation != null){
                            //LatLng loc = mGoogleMap.getCameraPosition().target;
                            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        }
                        break;
                    case AnchorSheetBehavior.STATE_HIDDEN:
                        break;
                    case AnchorSheetBehavior.STATE_EXPANDED:
                        break;
                    case AnchorSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }
        });

        tapactionlayoutWhereToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehaviorWhereToGo.getState()==AnchorSheetBehavior.STATE_COLLAPSED) {
                    sheetBehaviorWhereToGo.setState(AnchorSheetBehavior.STATE_ANCHOR);

                }
            }
        });

        btnWriteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehaviorWriteAddress.setState(BottomSheetBehavior.STATE_EXPANDED);
                txtAddressTo.requestFocus();
                isTxtFromFocused=false;
                predictionSelectedAddressFullFrom = null;
                predictionSelectedAddressShortFrom = null;
                latLngSelectedFrom = null;
                autocompleteSessionToken = AutocompleteSessionToken.newInstance();
                Utils.showKeyboard(view.getContext(), txtAddressTo);
                setTextToAddressFrom();
                enableOrDisableTravelNow();
            }
        });

        /*btnWriteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void configureBottomSheetFinishChooseTravel()
    {
        initLatLngOnSlide=null;
        bottomLayoutFinishChooseTravel = view.findViewById(R.id.panel_for_finish_choose_travel_layout);
        sheetBehaviorFinishChooseTravel = AnchorSheetBehavior.from(bottomLayoutFinishChooseTravel);
        sheetBehaviorFinishChooseTravel.setState(AnchorSheetBehavior.STATE_COLLAPSED);
        btnAskTravel = view.findViewById(R.id.btn_requetTravelNow);
        btnCloseBottomLayout = view.findViewById(R.id.btn_close_finish_travel);

        sheetBehaviorFinishChooseTravel.setAnchorOffset(0.40f);
        bottomLayoutFinishChooseTravel.setVisibility(View.GONE);

        sheetBehaviorFinishChooseTravel.setAnchorSheetCallback(new AnchorSheetBehavior.AnchorSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == AnchorSheetBehavior.STATE_COLLAPSED) {
                    //action if needed
                }

                if (newState == AnchorSheetBehavior.STATE_EXPANDED) {

                }

                if (newState == AnchorSheetBehavior.STATE_DRAGGING) {

                }

                if (newState == AnchorSheetBehavior.STATE_ANCHOR) {

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                float h = bottomSheet.getHeight();
                float off = h*slideOffset;

                switch (sheetBehaviorFinishChooseTravel.getState()) {
                    case AnchorSheetBehavior.STATE_DRAGGING:
                        setMapPaddingBotttom(off);
                        if(initLatLngOnSlide==null)
                        {
                            initLatLngOnSlide = mGoogleMap.getCameraPosition().target;
                        }

                        //reposition marker at the center
                        if (initLatLngOnSlide != null){
                            //LatLng loc = mGoogleMap.getCameraPosition().target;
                            LatLng loc = new LatLng(initLatLngOnSlide.latitude, initLatLngOnSlide.longitude);
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        }
                        break;
                    case AnchorSheetBehavior.STATE_SETTLING:
                        setMapPaddingBotttom(off);
                        //reposition marker at the center
                       /* if (mLastLocation != null){
                            //LatLng loc = mGoogleMap.getCameraPosition().target;
                            LatLng loc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        }*/
                        break;
                    case AnchorSheetBehavior.STATE_HIDDEN:
                        break;
                    case AnchorSheetBehavior.STATE_EXPANDED:
                        initLatLngOnSlide=null;
                        break;
                    case AnchorSheetBehavior.STATE_COLLAPSED:
                        initLatLngOnSlide=null;
                        break;
                   case AnchorSheetBehavior.STATE_ANCHOR:
                       initLatLngOnSlide=null;
                       break;
                }
            }
        });

        btnAskTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTravel();
            }
        });

        btnCloseBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reinitializeData();
            }
        });

    }

    public void requestTravel() {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            TravelEntity travel = new TravelEntity();

            //Datos del orígen
            String originObservations = txtAddressFromAdditional.getText().toString().trim();
            String addressOriginFinal = !TextUtils.isEmpty(addressFromFull) ? addressFromFull.concat(", ").concat(originObservations) : originObservations;
            String latOriginFinal =  String.valueOf(addressFromLatLng.latitude);
            String lonOriginFinal= String.valueOf(addressFromLatLng.longitude);

            //Datos del destino
            String destinationAddressFinal = addressToTextFull;// this.isCustomSelectAddress ? textViewCustomAddressDestino.getText().toString() : this.destination;
            String latDestinationFinal= String.valueOf(addressToLatLng.latitude);// this.isCustomSelectAddress ? "" : this.latDestination;
            String lonDestinationFinal= String.valueOf(addressToLatLng.longitude); //this.isCustomSelectAddress ? "" : this.lonDestination;
            double distanceFinal =  isCustomSelectAddress ? 0d : distanceTravel;

            //Fecha y hora del viaje
            String dateTravel = getDateByTravelOrReservation();

            //Travel Company
            int idUserCompany = HomeClienteNewStyle.gloval.getGv_id_profile() == Globales.ProfileId.CLIENT_COMPANY? HomeClienteNewStyle.gloval.getGv_user_id(): 0;
            boolean isTravelCompany = HomeClienteNewStyle.gloval.getGv_id_profile() == Globales.ProfileId.CLIENT_COMPANY;

            //Seteo si es punto a punto
            int isPointToPoint = getIsPointToPoint();

            /* INFORMACION DEL FLETE */
            int _isFleetTravelAssistance = 0;
            boolean _isFleetTravel = false;
            if(chkNeedFleet.isChecked()){
                _isFleetTravelAssistance = fleetTravelAssistanceNumber;
                _isFleetTravel = true;
            }

            /*Precio del Origen Pactado*/
            double amountOriginPacSelected = getAmountOriginPacFromVehicleSelected();


            /*INFORMACION DEL VUELO */
            boolean hasDataFlight = hasFlightData();
            String _hoursAribo =hasDataFlight? oursAribo.getText().toString():"";
            String _terminal = hasDataFlight? terminal.getText().toString():"";
            String _airlineCompany = hasDataFlight? airlineCompany.getText().toString():"";
            String _flyNumber = hasDataFlight? flyNumber.getText().toString():"";

            travel.setTravelBody(
                    new TravelBodyEntity(
                            HomeClienteNewStyle.gloval.getGv_id_cliet(),
                            isTravelCompany,
                            new OriginEntity(
                                    latOriginFinal,
                                    lonOriginFinal,
                                    addressOriginFinal),
                            new DestinationEntity(
                                    latDestinationFinal,
                                    lonDestinationFinal,
                                    destinationAddressFinal
                            ),
                            dateTravel,
                            Integer.parseInt(typeVehicleSelected),
                            true,
                            idUserCompany,
                            _hoursAribo,
                            _terminal,
                            _airlineCompany,
                            _flyNumber,
                            _isFleetTravelAssistance,
                            _isFleetTravel,
                            distanceFinal,
                            distanceFinal,
                            String.valueOf(amountStimate),
                            originObservations,
                            isPointToPoint,
                            amountOriginPacSelected
                    )
            );

            // VALIDAMOS RESERVA O VIAJE //
            boolean validateRequired = true;
            if (isReservation()) {

                if(travel.getTravelBody().getOrigin().getNameOrigin().length() > 0 &&
                        travel.getTravelBody().getmDestination().getNameDestination().length() >0 &&
                        !fromTimeEtxt.getText().toString().matches("") &&
                        !fromDateEtxt.getText().toString().matches("")){
                    validateRequired = true;
                }else {
                    validateRequired = false;
                }

            }


            if (validateRequired) {
                //  btnrequertReser.setEnabled(false);
                //  btnrequetTravelNow.setEnabled(false);

                Call<resp> call = this.daoTravel.addTravel(travel);

                Log.e("DATA", travel.makeJson());

                loading = ProgressDialog.show(getContext(), getString(R.string.enviando_solicitud), getString(R.string.espere_unos_segundos), true, false);

                call.enqueue(new Callback<resp>() {
                    @Override
                    public void onResponse(Call<resp> call, Response<resp> response) {
                        loading.dismiss();
                        amountStimate=0;
                        amountOriginPac = 0d;
                        if (response.code() == 200) {
                            getCurrentTravelRecentlyCreated();
                            resp responseBody = (resp) response.body();
                            if (isReservation()) {
                                Toast.makeText(getContext(), R.string.reserva_solicitada, Toast.LENGTH_SHORT).show();
                                showOrHidePopupViajeSolicitado(false, "");
                            } else {
                                Toast.makeText(getContext(), getString(R.string.viaje_solicitado_title), Toast.LENGTH_SHORT).show();
                                showOrHidePopupViajeSolicitado(true, getString(R.string.buscando_chofer));
                            }
                            reinitializeData();
                        } else {

                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("ERROR" + "(" + response.code() + ")");
                            alertDialog.setMessage(response.errorBody().source().toString());
                            Log.w("***", response.errorBody().source().toString());


                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getText(R.string.ok).toString().toUpperCase(),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<resp> call, Throwable t) {
                        loading.dismiss();

                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                    }
                });

            }

        } finally{
            this.daoTravel = null;
            fromDateEtxt.setText("");
            fromTimeEtxt.setText("");

        }

    }

    private double getAmountOriginPacFromVehicleSelected() {
        double result = 0d;
        if (Utils.isClienteCompany(gloval.getGv_id_profile())) {
            result = this.amountOriginPac;
        }
        return result;
    }

    private void getCurrentTravelRecentlyCreated(){
        try{
            if(getActivity()!=null)
            {
                ((HomeClienteNewStyle) getActivity()).getCurrentTravelByIdClient(false);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void reinitializeData()
    {
        try{
            sheetBehaviorWriteAddress.setState(BottomSheetBehavior.STATE_HIDDEN);
            sheetBehaviorFinishChooseTravel.setState(AnchorSheetBehavior.STATE_COLLAPSED);
            bottomLayoutFinishChooseTravel.setVisibility(View.GONE);
            bottomSheetWhereToGo.setVisibility(View.VISIBLE);
            sheetBehaviorWhereToGo.setState(AnchorSheetBehavior.STATE_COLLAPSED);
            enableOrDisableTravelNow();
            txtAddressTo.setText("");
            clearRouteMap();
            setDefaultTypeVehicleSelected();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void refreshMapFragment()
    {
        try{
            if(getActivity()!=null)
            {
                ((HomeClienteNewStyle) getActivity()).openMapFragment();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String getDateByTravelOrReservation(){
        String result="";
        String fecha= fromDateEtxt.getText().toString().trim();
        String tiempo= fromTimeEtxt.getText().toString().trim();
        String fechaHora = fecha.concat(" ").concat(tiempo);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        if (radioButtonReserveFuture.isChecked()) {
            result= fechaHora;
        } else {
            result= currentDate;
        }
        return result;
    }

    public void showOrHidePopupViajeSolicitado(boolean active, String sms)
    {

        if(active)
        {
            loading = new ProgressDialog(getContext());
            loading.setMessage(sms);
            loading.show();

            loading.setContentView(R.layout.custom_progressdialog);
            loading.setCancelable(false);

            ConstraintLayout car =  loading.findViewById(R.id.car_notifications_from_client);

            Button btnCnacel = loading.findViewById(R.id.car_notifications_cancel_client);
            btnCnacel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        showOrHidePopupViajeSolicitado(false,"");
                        getMotivosDeCancelacion();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });
        }
        else
        {
            if(loading != null)
            {
                loading.dismiss();
            }

        }
    }

    public void getMotivosDeCancelacion() {

        this.apiService = HttpConexion.getUri().create(ServicesTravel.class);
        Call<reasonEntity> call = this.apiService.obtIdMotivo(2);

        Log.d("Call request", call.request().toString());

        call.enqueue(new Callback<reasonEntity>() {
            @Override
            public void onResponse(Call<reasonEntity> call, Response<reasonEntity> response) {
                if (response.code() == 200) {
                    list = (List<reason>) response.body().getReason();
                    showMotivosDeCancelacion(true,"");
                } else if (response.code() == 404) {

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("ERROR" + "(" + response.code() + ")");
                    alertDialog.setMessage(response.errorBody().source().toString());
                    Log.w("***", response.errorBody().source().toString());


                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok).toUpperCase(),
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
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    public void showMotivosDeCancelacion(boolean active, String sms)
    {
        if(active)
        {
            loading = new ProgressDialog(getContext());
            loading.setMessage(sms);
            loading.show();

            loading.setContentView(R.layout.custom_modal);

            loading.setCancelable(false);

            CardView car = (CardView) loading.findViewById(R.id.car_notifications_from_client_cancelar);
            car.getBackground().setAlpha(200);

            RadioGroup rg = (RadioGroup) loading.findViewById(R.id.radio_group);

            for(int i=0;i<list.size();i++){
                RadioButton rb=new RadioButton(getContext()); // dynamically creating RadioButton and adding to RadioGroup.
                rb.setText(list.get(i).getReason());
                rg.addView(rb);
            }

            if(list.size() == 0){
                Toast.makeText(getContext(), R.string.agencia_sin_motivos_cancelacion , Toast.LENGTH_LONG).show();
            }

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int childCount = group.getChildCount();
                    for (int x = 0; x < childCount; x++) {
                        RadioButton btn = (RadioButton) group.getChildAt(x);
                        if (btn.getId() == checkedId) {
                            //Log.e("selected RadioButton->",btn.getText().toString());
                            motivo = (checkedId);
                        }
                    }
                }
            });

            Button btnCancel = (Button) loading.findViewById(R.id.btn_motivo);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (motivo == 0){
                            Toast.makeText(getContext(), R.string.seleccione_un_motivo , Toast.LENGTH_LONG).show();
                        }
                        else{
                            cancelTravelByClient();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            });

        }
        else
        {

            if(loading != null)
            {
                loading.dismiss();
            }

        }
    }

    public void cancelTravelByClient()
    {

        if (this.daoTravel == null) { this.daoTravel = HttpConexion.getUri().create(ServicesTravel.class); }

        try {
            loadingGloval = ProgressDialog.show(getContext(), getString(R.string.cancelar), getString(R.string.espere_unos_segundos), true, false);
            motivo = motivo - 1;
            Call<Boolean> call = this.daoTravel.cancelByClient(HomeClienteNewStyle.gloval.getGv_user_id(), motivo, HomeClienteNewStyle.currentTravel.getIdTravel() );
            call.enqueue(new Callback<Boolean>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    loadingGloval.dismiss();
                    Toast.makeText(getContext(), getString(R.string.viaje_cancelado), Toast.LENGTH_LONG).show();
                    // cerramo el dialog //
                    showMotivosDeCancelacion(false,"");
                    reinitializeData();
                    clearInfo();
                }

                public void onFailure(Call<Boolean> call, Throwable t) {
                    loadingGloval.dismiss();

                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "ERROR ("+t.getMessage()+")", Snackbar.LENGTH_LONG).show();
                }
            });

        } finally {
            this.daoTravel = null;
        }

    }



    private boolean isReservation()
    {
        return radioButtonReserveFuture.isChecked();
    }

    private void addViewInfoTravel(){

        textViewNombre= view.findViewById(R.id.txt_client_info);
        textViewTelefono= view.findViewById(R.id.txt_calling_info);
        textViewOrigen= view.findViewById(R.id.txt_origin_info);
        textViewDestino= view.findViewById(R.id.txt_destination_info);
        textViewTiempo= view.findViewById(R.id.txt_date_info);
        textViewDominio= view.findViewById(R.id.txt_domain);
        textViewKilometro= view.findViewById(R.id.txt_km_info);
        textViewMonto= view.findViewById(R.id.txt_amount_info);
        //imageView =  view.findViewById(R.id.img_face_client);
        expectedTimeLayout = view.findViewById(R.id.expected_time_layout);
        txtStarsDriver = view.findViewById(R.id.txt_stars_driver);
        imgStarsDriver = view.findViewById(R.id.img_rating);

        buttonCancelarViaje= view.findViewById(R.id.button_cancelar_viaje);
        buttonCancelarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(getActivity(), getString(R.string.solicitando_motivos_de_cancelacion), Toast.LENGTH_LONG).show();
                    getMotivosDeCancelacion();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });













        textViewTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(textViewTelefono.getText().toString());
            }
        });

        ImageView imgPhone =  view.findViewById(R.id.imageView8);
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer(textViewTelefono.getText().toString());
            }
        });
    }





    private BroadcastReceiver recibeNotifiacionSocket = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey("travelId")) {
                    String travelId = extras.getString("travelId");
                    String latDriver = extras.getString("latDriver");
                    String lngDriver = extras.getString("lngDriver");
                    String nameLocation = extras.getString("nameLocation");
                    addDriverPositionInMap(travelId, latDriver, lngDriver, nameLocation);
                }
            }
        }
    };


    public static void setVisibleprogressTravel(Boolean visible){
        HomeClientFragmentNewStyle.visible_progress = visible;
    }


    public void clearInfo() {
        try{
            bottomLayoutTravelInfo.setVisibility(View.GONE);
            bottomSheetWhereToGo.setVisibility(View.VISIBLE);
            HomeClientFragmentNewStyle.txtStatus.setVisibility(View.INVISIBLE);
            clearDriverIconMap();
            clearRouteMap();
            setVisibleprogressTravel(false);

        }catch (Exception E){
            Log.d("ERRO",E.getMessage());
        }

    }

    public void setInfoTravel(InfoTravelEntityLite currentTravel)
    {
        if(currentTravel != null) {

            if (view != null) {

                setVisibleprogressTravel(true);
                setValoresCurrentTravel();
                bottomSheetWhereToGo.setVisibility(View.GONE);
                bottomLayoutTravelInfo.setVisibility(View.VISIBLE);
                sheetBehaviorTravelInfo.setState(BottomSheetBehavior.STATE_COLLAPSED);
                HomeClientFragmentNewStyle.txtStatus.setText(currentTravel.getNameStatusTravel());
                HomeClientFragmentNewStyle.txtStatus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black87));
                sheetBehaviorTravelInfo.setPeekHeight(Utils.pxFromDp(view.getContext(),186f));
                setTxtEstimatedArrivalDriverTime("","");
                clearPhotoDriver();

                switch (currentTravel.getIdSatatusTravel()) {
                    case Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        HomeClientFragmentNewStyle.txtStatus.setText(view.getContext().getString(R.string.client_sin_chofer_text));
                        HomeClientFragmentNewStyle.txtStatus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.danger));
                        sheetBehaviorTravelInfo.setPeekHeight(Utils.pxFromDp(view.getContext(),118f));
                        break;
                    case Globales.StatusTravel.CHOFER_ASIGNADO:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        sheetBehaviorTravelInfo.setPeekHeight(Utils.pxFromDp(view.getContext(),118f));

                        break;
                    case Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        clearRouteMap();
                        setPhoto();
                        break;
                    case Globales.StatusTravel.VIAJE_EN_CURSO:
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                            }
                        }, 2000);
                        addDestinationMarkerToMap(getContext());
                        calculateTimeArriveToPoint(currentTravel.getLatDestination(), currentTravel.getLonDestination(), mTiempoLlegadaText);
                        HomeClientFragmentNewStyle.txtStatus.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorVerde));
                        expectedTimeLayout.setVisibility(View.VISIBLE);
                        setPhoto();
                        break;
                    case Globales.StatusTravel.VIAJE_FINALIZADO:
                        setVisibleprogressTravel(false);
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        clearDestinationIconMap();
                        clearInfo();
                        break;
                    default:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        setVisibleprogressTravel(false);
                }
            }
            else {
                setVisibleprogressTravel(false);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        try
        {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }catch (Exception e)
        {
            Log.d("Error",e.getMessage());
        }

    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



    public void addDriverMarkerToMap(String driverLat, String driverLng, String nameLocation) {
        try {
            if (mGoogleMap != null) {
                if(!"".equals(driverLat) && !"".equals(driverLng)) {
                    LatLng latLngDriver = new LatLng(Double.parseDouble(driverLat), Double.parseDouble(driverLng));
                    LatLng latLngCurrent = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLngDriver);
                    markerOptions.title(nameLocation);

                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.auto);
                    Bitmap b = bitmapdraw.getBitmap();
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));

                    if (mCarLocationMarker != null) {
                        double rotationDouble = Utils.bearingBetweenLocations(mCarLocationMarker.getPosition(), latLngDriver);
                        float rotation = (float) rotationDouble;
                        mCarLocationMarker.setRotation(rotation);
                        mCarLocationMarker.setPosition(latLngDriver);

                    } else {
                        mCarLocationMarker = mGoogleMap.addMarker(markerOptions);

                    }
                    alejarCamaraParaAbarcarPuntos(latLngCurrent, latLngDriver);
                }
                else{
                    clearDriverIconMap();
                }
            }
        }
        catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private static void addDestinationMarkerToMap(Context context) {
        try {
            if (mGoogleMap != null && mLastLocation!=null && currentTravel!=null && bitmapDrawableIconDestination!=null) {

                LatLng latLngDriver = new LatLng(Double.parseDouble(currentTravel.getLatDestination()), Double.parseDouble(currentTravel.getLonDestination()));
                LatLng latLngCurrent = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLngDriver);
                markerOptions.title(currentTravel.getNameDestination());

                IconGenerator iconGen = new IconGenerator(view.getContext());
                Bitmap bitmapIconText = iconGen.makeIcon(  context.getString(R.string.destino));

                //Bitmap b = bitmapDrawableIconDestination.getBitmap();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapIconText));

                clearDestinationIconMap();
                mDestinationMarker = mGoogleMap.addMarker(markerOptions);
                alejarCamaraParaAbarcarPuntos(latLngCurrent, latLngDriver);
            }
            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addDestinationMarkerToMap(context);
                    }
                }, 500);
            }
        }
        catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }


    private void addCurrentLocationMarker()
    {
        if (mLastLocation != null) {

        }
    }



    private void executeTimerBlink(){
        try {
            timerblink = new Timer();
            timerblink.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (getActivity() != null) {
                                    if (getActivity().findViewById(R.id.txtStatus) != null &&
                                            getActivity().findViewById(R.id.txtStatus) != null) {
                                        TextView txt =  getActivity().findViewById(R.id.txtStatus);
                                        if (HomeClientFragmentNewStyle.visible_progress) {
                                            if (txt.getVisibility() == View.VISIBLE) {
                                                txt.setVisibility(View.INVISIBLE);
                                            } else {
                                                txt.setVisibility(View.VISIBLE);
                                            }

                                            HomeClientFragmentNewStyle.stateProgressBar.setVisibility(View.VISIBLE);
                                        }

                                        if (!HomeClientFragmentNewStyle.visible_progress) {
                                            if (txt != null) {
                                                txt.setVisibility(View.INVISIBLE);
                                            }
                                            stateProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    calculateDestinationDuration();
                                    clearDriverIconIfPossible();
                                    clearDestinationIconIfPossible();
                                }
                            }
                        });
                    }
                }
            }, 0, 1000);
        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }

    private void clearDriverIconIfPossible()
    {
        try {
            if(currentTravel != null && currentTravel.getIdSatatusTravel()!=Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER) {
                clearDriverIconMap();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void clearDestinationIconIfPossible()
    {
        try {
            if(currentTravel != null && currentTravel.getIdSatatusTravel()!=Globales.StatusTravel.VIAJE_EN_CURSO) {
                clearDestinationIconMap();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void calculateDestinationDuration()
    {
        try {
            if(currentTravel != null) {
                if(currentTravel.getIdSatatusTravel()==Globales.StatusTravel.VIAJE_EN_CURSO)
                {
                    Date currentTime = Calendar.getInstance().getTime();
                    if(lastTimeGetDestinationCalculation==null || isValidTimeToRecalculateRoute(currentTime)){
                        lastTimeGetDestinationCalculation = currentTime;
                        calculateTimeArriveToPoint(currentTravel.getLatDestination(), currentTravel.getLonDestination(), mTiempoLlegadaText);
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean isValidTimeToRecalculateRoute(Date currentTime)
    {
        int numberOfseconds = -10;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.SECOND, numberOfseconds);
        return lastTimeGetDestinationCalculation.before(calendar.getTime());
    }

    public void addDriverPositionInMap(String travelId, String latDriver, String lngDriver, String nameLocation)
    {
        try {
            if(currentTravel != null) {
                if(currentTravel.getIdTravel()==Integer.parseInt(travelId) && currentTravel.getIdSatatusTravel()==Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER)
                {
                    addDriverMarkerToMap(latDriver, lngDriver, nameLocation);
                    calculateTimeArriveToPoint(latDriver, lngDriver, getString(R.string.tiempo_de_llegada_del_chofer));
                }
            }
        } finally {
            this.daoTravel = null;
        }
    }

    private static void calculateTimeArriveToPoint(String latDriver, String lngDriver, final String titleText)
    {
        try
        {
            LatLng origin = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            LatLng destination = new LatLng(Double.parseDouble(latDriver), Double.parseDouble(lngDriver));
            String gMapApiKey = G_MAP_API_KEY;
            Utils.getDirectionBetweenTwoPointsApi(origin, destination, gMapApiKey, new DirectionApiCallback() {
                @Override
                public void onDirectionSuccess(Direction direction) {
                    try{
                        String durationText = direction.getRouteList().get(0).getLegList().get(0).getDuration().getText();
                        setTxtEstimatedArrivalDriverTime(titleText, durationText);
                    }
                    catch (Exception ex){
                        setTxtEstimatedArrivalDriverTime(titleText,"");
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onDirectionError() {
                    setTxtEstimatedArrivalDriverTime(titleText,"");
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            setTxtEstimatedArrivalDriverTime(titleText,"");
        }
    }

    private static void setTxtEstimatedArrivalDriverTime(String txtTitle, String estimatedTime)
    {
        try
        {
            if(!"".equals(estimatedTime)){
                expectedTimeLayout.setVisibility(View.VISIBLE);
                txtEstimatedTime.setText(txtTitle.concat(" ").concat(estimatedTime));
            }
            else{
                expectedTimeLayout.setVisibility(View.GONE);
                txtEstimatedTime.setText("");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public  void setRouteMap(ArrayList<LatLng> directionPositionList,String startAddress, String endAddress){
        if (view != null) {
            try
            {
                clearRouteMap();
                //dibujo la ruta
                PolylineOptions routePolylineOptions = DirectionConverter.createPolyline(view.getContext(), directionPositionList, 5, Color.BLUE);
                routePolilines =  mGoogleMap.addPolyline(routePolylineOptions);

                drawDestinyMarkers(directionPositionList, endAddress);
                drawOriginMarkers(directionPositionList, startAddress);

                //Muevo la camara del mapa para que abarque a toda la ruta
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng item : directionPositionList) {
                    builder.include(item);
                }
                LatLngBounds bounds = builder.build();

                float screen_height = (float) view.getHeight();
                mGoogleMap.setPadding(0, 0, 0, Math.round(screen_height/2));
                //la ruta debe entrar en la mitad del mapa (alto) porque es lo que se muestra porque tiene el detalle del viaje arriba.
                CameraUpdate cuMapVisible = CameraUpdateFactory.newLatLngBounds(bounds,view.getWidth(),view.getHeight(),80);
                mGoogleMap.animateCamera(cuMapVisible);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void drawDestinyMarkers(ArrayList<LatLng> directionPositionList, String endAddress) {
        //Dibujo el marcador en el destino
        IconGenerator iconGen = new IconGenerator(view.getContext());
        iconGen.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.marker_for_address_icon));
        iconGen.setTextAppearance(R.style.styleMapTextMarkerAddress);
        Bitmap bitmapIconText = iconGen.makeIcon(endAddress);
        BitmapDescriptor iconWithText = BitmapDescriptorFactory.fromBitmap(bitmapIconText);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(directionPositionList.get(directionPositionList.size()-1));
        markerOptions.icon(iconWithText);
        markerOptions.anchor(0.5f,1.4f);

        //Marcador 2 de destino
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(directionPositionList.get(directionPositionList.size()-1));
        markerOptions2.anchor(0.5f,0.5f);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_circle_blue);
        markerOptions2.icon(icon2);

        routeDestinationMarker2 =mGoogleMap.addMarker(markerOptions2);
        routeDestinationMarker = mGoogleMap.addMarker(markerOptions);
    }

    private  void drawOriginMarkers(ArrayList<LatLng> directionPositionList, String startAddress) {
        //Dibujo el marcador en el destino
        IconGenerator iconGen = new IconGenerator(view.getContext());
        iconGen.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.marker_for_address_icon));
        iconGen.setTextAppearance(R.style.styleMapTextMarkerAddress);
        Bitmap bitmapIconText = iconGen.makeIcon(startAddress);
        BitmapDescriptor iconWithText = BitmapDescriptorFactory.fromBitmap(bitmapIconText);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(directionPositionList.get(0));
        markerOptions.icon(iconWithText);
        markerOptions.anchor(0.5f,1.4f);

        //Marcador 2 de destino
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(directionPositionList.get(0));
        markerOptions2.anchor(0.5f,0.5f);
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_circle_blue);
        markerOptions2.icon(icon2);

        routeOriginMarker2 =mGoogleMap.addMarker(markerOptions2);
        routeOriginMarker = mGoogleMap.addMarker(markerOptions);
    }

    public  void clearRouteMap(){
        if (view != null) {
            try
            {
                if(routePolilines!=null)
                {
                    routePolilines.remove();
                }

                if(routeDestinationMarker!=null){
                    routeDestinationMarker.remove();
                }
                if(routeDestinationMarker2!=null){
                    routeDestinationMarker2.remove();
                }
                if(routeOriginMarker!=null){
                    routeOriginMarker.remove();
                }
                if(routeOriginMarker2!=null){
                    routeOriginMarker2.remove();
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void clearDriverIconMap(){
        if (mCarLocationMarker != null) {
            mCarLocationMarker.remove();
            mCarLocationMarker=null;
        }
    }

    public static void clearDestinationIconMap(){
        if (mDestinationMarker!= null) {
            mDestinationMarker.remove();
            mDestinationMarker =null;
        }
    }

    private static void alejarCamaraParaAbarcarPuntos(LatLng origen, LatLng destino)
    {
        try {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(origen);
            builder.include(destino);

            LatLngBounds bounds = builder.build();
            //la ruta debe entrar en la mitad del mapa (alto) porque es lo que se muestra porque tiene el detalle del viaje arriba.
            CameraUpdate cuMapVisible = CameraUpdateFactory.newLatLngBounds(bounds, view.getWidth(), view.getHeight(), 80);
            mGoogleMap.animateCamera(cuMapVisible, 500, null);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void openDialer(String phoneNumber)
    {
        // Use format with "tel:" and phoneNumber
        if(!TextUtils.isEmpty(phoneNumber))
        {
            Uri u = Uri.parse("tel:" + phoneNumber);
            // Create the intent and set the data for the intent as the phone number.
            Intent i = new Intent(Intent.ACTION_DIAL, u);

            try {
                // Launch the Phone app's dialer with a phone number to dial a call.
                startActivity(i);
            } catch (SecurityException s) {
                // show() method display the toast with exception message.
                Toast.makeText(getActivity().getApplicationContext(), s.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static void setPhoto()
    {
        try {
            if (currentTravel != null) {
                ImageView imageView =  view.findViewById(R.id.img_face_client);
                String url = Utils.getUrlImageUser(currentTravel.getIdUserDriver());
                Glide.with(imageView.getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_update)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.ic_user)
                        //.centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void clearPhotoDriver()
    {
        if(view!=null)
        {
            ImageView imageView =  view.findViewById(R.id.img_face_client);
            if(imageView!=null)
            {
                imageView.setImageResource(R.drawable.ic_user);
            }
        }
    }

    private static void setValoresCurrentTravel(){
        if (currentTravel!=null) {
            Currency currency = Utils.getCurrency(view.getContext());
            textViewNombre.setText(currentTravel.getDriver());
            textViewTelefono.setText(currentTravel.getPhoneNumberDriver());
            textViewDominio.setText(currentTravel.getInfocar());
            textViewTiempo.setText(currentTravel.getMdate());
            textViewDestino.setText(currentTravel.getNameDestination());
            textViewOrigen.setText(currentTravel.getNameOrigin());
            textViewKilometro.setText(currentTravel.getDistanceLabel()+" Km");

            if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA ||
                    currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_AGENCIA ||
                    currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_RECHAZADO_POR_CHOFER ||
                    currentTravel.getIdSatatusTravel() == Globales.StatusTravel.CHOFER_ASIGNADO
            ) {// VIAJE EN BUSQUEDA DE CLIENTE
                textViewNombre.setText("");
                textViewTelefono.setText("");
                textViewDominio.setText("");
                txtStarsDriver.setText("");
                imgStarsDriver.setVisibility(View.GONE);
            }
            else{
                setStarsToDriver(currentTravel.getIdDriverKf());
            }

            if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER || currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_ACEPTADO_POR_AGENCIA) {// VIAJE EN BUSQUEDA DE CLIENTE
                buttonCancelarViaje.setVisibility(View.VISIBLE);
            }else {
                buttonCancelarViaje.setVisibility(View.INVISIBLE);
            }

            if (currentTravel.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO) {
                if (currentTravel.getAmountCalculate() != null) {
                    textViewMonto.setText(currentTravel.getTotalAmount().concat(" ").concat(currency.getSymbol()));
                }
            } else {
                if (currentTravel.getAmountCalculate() != null) {
                    textViewMonto.setText(currentTravel.getAmountCalculate().concat(" ").concat(currency.getSymbol()));
                }
            }
        }
    }

    private static void setStarsToDriver(int driverId)
    {
        try{
            ServicesTravel daoTravel = HttpConexion.getUri().create(ServicesTravel.class);
            daoTravel.getDriverRating(driverId).enqueue(new Callback<Double>() {

                @Override
                public void onResponse(Call<Double> call, Response<Double> response) {
                    txtStarsDriver.setText("");
                    imgStarsDriver.setVisibility(View.GONE);
                    if(response.isSuccessful())
                    {
                        txtStarsDriver.setText(String.valueOf(response.body()));
                        imgStarsDriver.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Double> call, Throwable t) {
                    try{
                        txtStarsDriver.setText("");
                        imgStarsDriver.setVisibility(View.GONE);
                    }
                    catch (Exception ex){}

                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    public boolean isTravelLayoutOpen(){
        return bottomLayoutFinishChooseTravel.getVisibility()==View.VISIBLE;
    }

    public boolean isWriteAddressLayoutOpen(){
        return bottomLayoutWriteAddress.getVisibility()==View.VISIBLE && sheetBehaviorWriteAddress.getState()!=BottomSheetBehavior.STATE_HIDDEN;
    }

    private void showMessageOnParent(String msg) {
        try {
            if(getActivity()!=null)
            {
                ((HomeClienteNewStyle) getActivity()).showMessage(msg,Globales.StatusToast.WARNING);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addressSelectedFromMap(Intent data)
    {
        try {
            String addressName = data.getExtras().getString("address");
            String addressNameShort = data.getExtras().getString("addressShort");
            double lat = data.getExtras().getDouble("lat");
            double lng = data.getExtras().getDouble("lng");
            int type = data.getExtras().getInt("type");
            if(type==Globales.TypeAddressAutocomplete.ADDRESS_TO)
            {
                rvAutoCompleteSelectedItemResultTo(null,0,false,lat,lng,addressName, addressNameShort);
            }
            else{
                autocompleteSelectionFrom(null, 0, true, false, new LatLng(lat,lng),addressNameShort,addressName);
            }


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setgMapApiKey()
    {
        G_MAP_API_KEY = gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_104_GOOGLE_MAPS_API_KEY).getValue();
    }
}
