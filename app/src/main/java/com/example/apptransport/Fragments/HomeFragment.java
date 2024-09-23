package com.example.apptransport.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelSqliteEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.IExecutionComplete;
import com.apreciasoft.mobile.asremis.Util.ServicesSleep;
import com.apreciasoft.mobile.asremis.Util.SqliteUtil;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int ZOOM_LEVEL_START = 17;
    public static SqliteUtil antonellaSqlite;

    private static View view;
    public static List<LatLng> listPosition = new ArrayList<>();
    static GoogleMap mGoogleMap;
    private LocationManager locationManager;
    static GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static String nameLocation;
    public static PolylineOptions options;
    public static PolylineOptions optionReturnActive;
    public static GlovalVar gloval;
    public static TextView infoGneral = null;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences pref;
    public static TextView txt_distance_real = null;
    public static InfoTravelEntity currentTravelEntity;
    public ImageButton imageButtonMoreDetail;
    public SupportMapFragment mMap;
    private ListenerFragmentChofer listenerFragmentChofer;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    protected PowerManager.WakeLock wakelock;

    public static TextView txt_tiempo_transcurrido_viaje_x_hora = null;
    public static View panel_tiempo_transcurrido_viaje_x_hora = null;

    private Marker mMarkerCliente, mMarkerChofer,mMarkerDestino;

    private static ImageButton btnCenterDriver;
    private  boolean isMapZoomFirstTimeSet=false;
    private  int lastInfoSet=0;
    private static boolean isMapCenter=true;
    private static Polyline destinationRoute;

    private static SwitchCompat switchPause;
    private View viewMsgPausar;
    private TextView txtMsgPausar;
    private static boolean isDriverActiveErrorFromApi=false;


    public HomeFragment(ListenerFragmentChofer listenerFragmentChofer) {
        this.listenerFragmentChofer = listenerFragmentChofer;
    }


    @SuppressLint("InvalidWakeLockTag")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //evitar que la pantalla se apague
        final PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();

        gloval = ((GlovalVar) getActivity().getApplicationContext());

        view = inflater.inflate(R.layout.fragment_home, container, false);
        isMapZoomFirstTimeSet=false;
        lastInfoSet=0;
        isMapCenter=true;
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = getActivity().getSharedPreferences(HttpConexion.instance, 0); // 0 - for private mode
        editor = pref.edit();

        SupportMapFragment fr =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);

        if (fr == null) {
            mMap = ((SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.gmap));
            mMap.getMapAsync(this);

        } else {
            fr.getMapAsync(this);
        }

        txt_distance_real = getActivity().findViewById(R.id.txt_distance_real);
        imageButtonMoreDetail = getActivity().findViewById(R.id.button_info_travel);
        imageButtonMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTravelEntity != null) {
                    listenerFragmentChofer.showDialogTravelInfoChofer(currentTravelEntity);
                } else {
                    listenerFragmentChofer.showMessage(getString(R.string.no_viaje_asignado), 0);
                }
            }
        });

        txt_tiempo_transcurrido_viaje_x_hora = getActivity().findViewById(R.id.txt_tiempo_transcurrido);
        panel_tiempo_transcurrido_viaje_x_hora = getActivity().findViewById(R.id.section_travel_by_hour);
        setPanelViajeXHoraVisible(false);

        //bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.auto);
        infoGneral = getActivity().findViewById(R.id.infoGneral);
        infoGneral.setText(R.string.servicio_activo);
        isMapZoomFirstTimeSet=false;
        lastInfoSet=0;
        configureBtnCenterDriver();
        configureBtnPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        showTravelButtonInActivity();
    }

    private void showTravelButtonInActivity(){
        try{
            HomeChofer.MUST_SHOW_STREET_BUTTON=true;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void configureBtnPause() {

        setSwitchPausarVisible(true);
        switchPause = getActivity().findViewById(R.id.switchPause);
        viewMsgPausar = getActivity().findViewById(R.id.section_msg_paused);
        txtMsgPausar = getActivity().findViewById(R.id.txtMsgPausar);
        setInService();
        callServiceToPauseService(true);

        switchPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!isDriverActiveErrorFromApi)
                {
                    callServiceToPauseService(b);
                }
                else{
                    isDriverActiveErrorFromApi=false;
                }
            }
        });
    }

    private void callServiceToPauseService(final boolean isActive){
        try{
            ((HomeChofer)getActivity()).callServiceToPauseService(isActive, new IExecutionComplete() {
                @Override
                public void onResult(int result) {

                    if (result==1) {
                        setInService();
                    }
                    else if(result==0){
                        setPaused();
                    }
                    else{
                        revertWitchInPause();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


    private void setPaused() {
        try{
            //showOrHideMsgPausar(true);

            Utils.sendIsDriverToService(getActivity(), "false");
            txtMsgPausar.setText(R.string.servicio_inactivo);
            viewMsgPausar.setBackgroundColor(getResources().getColor(R.color.colorRojo));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setInService() {
        try{
            Utils.sendIsDriverToService(getActivity(), "true");
            txtMsgPausar.setText(R.string.servicio_activo);
            viewMsgPausar.setBackgroundColor(getResources().getColor(R.color.GRREN_FULL));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void revertWitchInPause()
    {
        isDriverActiveErrorFromApi = true;
        switchPause.setChecked(!switchPause.isChecked());
    }


    public void showOrHideMsgPausar(boolean value)
    {
        try{
            viewMsgPausar.setVisibility(value?View.VISIBLE: View.GONE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void setSwitchPausarVisible(boolean isVisible)
    {
        try {
            switchPause.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void setPanelViajeXHoraVisible(boolean isVisible)
    {
        if(panel_tiempo_transcurrido_viaje_x_hora!=null)
        {
            panel_tiempo_transcurrido_viaje_x_hora.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }



    private void configureBtnCenterDriver()
    {
        btnCenterDriver = view.findViewById(R.id.btn_center_driver);
        btnCenterDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerMapInActualPosition();
            }
        });
        setBtnCenterDriverVisible(true);
    }

    public static void centerMapInActualPosition()
    {
        try
        {
            if(mLastLocation!=null && mGoogleMap!=null)
            {
                LatLng actualLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraPosition position = new CameraPosition.Builder()
                        .target(actualLocation)
                        .zoom(ZOOM_LEVEL_START)
                        .build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),500,null);
                setBtnCenterDriverVisible(false);
                isMapCenter = true;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public static Location getmLastLocation() {
        return mLastLocation;
    }


    @SuppressLint({"RestrictedApi", "MissingPermission"})
    @Override
    public void onConnected(Bundle bundle) {
       startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public static float[] calculateMiles() {
        float totalDistance = 0;
        float totalDistanceVuelta = 0;

        if (options != null) {
            for (int i = 1; i < options.getPoints().size(); i++) {


                Location currLocation = new Location("this");
                currLocation.setLatitude(options.getPoints().get(i).latitude);
                currLocation.setLongitude(options.getPoints().get(i).longitude);

                Location lastLocation = new Location("this");
                lastLocation.setLatitude(options.getPoints().get(i - 1).latitude);
                lastLocation.setLongitude(options.getPoints().get(i - 1).longitude);

                totalDistance += lastLocation.distanceTo(currLocation);

                // VERIFICAMOS SI ACTIVO EL RETORNO PARA LA IDA Y VUELTA //
                if (currentTravelEntity != null) {

                    if (currentTravelEntity.isRoundTrip == 1) {
                        if (optionReturnActive.getPoints().get(0) != null) {
                            if (optionReturnActive.getPoints().get(0).latitude == options.getPoints().get(i).latitude) {
                                if (optionReturnActive.getPoints().get(0).longitude == options.getPoints().get(i).longitude) {
                                    Log.d("totalDistance 2", "retorno");

                                    totalDistanceVuelta += lastLocation.distanceTo(currLocation);
                                }

                            }
                        }
                    }
                }

            }

        }


        return new float[]{totalDistance, totalDistanceVuelta};
    }

    public void setInfoTravel(InfoTravelEntity currentTravel) {
        currentTravelEntity = currentTravel;

        infoGneral.setText(R.string.en_viaje);

        if (currentTravelEntity != null && currentTravelEntity != null && currentTravelEntity.getLonOrigin() != null) {

            if (currentTravelEntity.getIdSatatusTravel()>0){
                Log.e("STATUS TRAVEL",""+currentTravelEntity.getIdSatatusTravel());

                if (currentTravelEntity.getIdSatatusTravel()==Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER){
                    Location locationOrigen= new Location("origen");
                    locationOrigen.setLatitude(Double.valueOf(currentTravelEntity.getLatOrigin()));
                    locationOrigen.setLongitude(Double.valueOf(currentTravelEntity.getLonOrigin()));
                    mostarMarketCliente(locationOrigen);

                    if(!TextUtils.isEmpty(currentTravelEntity.getLatOrigin()) && getmLastLocation()!=null)
                    {
                        LatLng latLngOrigen = new LatLng(getmLastLocation().getLatitude(), getmLastLocation().getLongitude());
                        LatLng latLngDestino = new LatLng(locationOrigen.getLatitude(),locationOrigen.getLongitude());
                        alejarCamaraParaAbarcarPuntos(latLngOrigen, latLngDestino, 1);
                    }

                }else if (currentTravelEntity.getIdSatatusTravel()== Globales.StatusTravel.VIAJE_EN_CURSO){
                    borrarMarketCliente();

                    if(!TextUtils.isEmpty(currentTravelEntity.getLatDestination()))
                    {
                        Location locationDestino= new Location("destino");
                        locationDestino.setLatitude(Double.valueOf(currentTravelEntity.getLatDestination()));
                        locationDestino.setLongitude(Double.valueOf(currentTravelEntity.getLonDestination()));
                        mostrarMarketDestino(locationDestino);
                        if(getmLastLocation()!=null)
                        {
                            LatLng latLngOrigen = new LatLng(getmLastLocation().getLatitude(), getmLastLocation().getLongitude());
                            LatLng latLngDestino = new LatLng(locationDestino.getLatitude(),locationDestino.getLongitude());
                            alejarCamaraParaAbarcarPuntos(latLngOrigen, latLngDestino,2);

                        }
                    }
                }
                setInfoTravelByHour("00:00:00");
            }

        }
    }

    public void setInfoTravelByHour(String timeElapsed)
    {
        try{
            if(     currentTravelEntity!=null &&
                    currentTravelEntity.getIsTravelByHour()==1 &&
                    currentTravelEntity.getIdSatatusTravel()==Globales.StatusTravel.VIAJE_EN_CURSO
            )
            {
                panel_tiempo_transcurrido_viaje_x_hora.setVisibility(View.VISIBLE);
                txt_tiempo_transcurrido_viaje_x_hora.setText(timeElapsed);
            }
            else{
                panel_tiempo_transcurrido_viaje_x_hora.setVisibility(View.GONE);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void alejarCamaraParaAbarcarPuntos(LatLng origen, LatLng destino, int infoSet)
    {
        try{
            if(infoSet!=lastInfoSet)
            {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origen);
                builder.include(destino);

                LatLngBounds bounds = builder.build();
                //la ruta debe entrar en la mitad del mapa (alto) porque es lo que se muestra porque tiene el detalle del viaje arriba.
                CameraUpdate cuMapVisible = CameraUpdateFactory.newLatLngBounds(bounds,view.getWidth(),view.getHeight(),80);
                mGoogleMap.animateCamera(cuMapVisible,500,null);

                lastInfoSet=infoSet;
                isMapCenter=false;
                setBtnCenterDriverVisible(true);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void clearInfo() {
        currentTravelEntity = null;
        infoGneral.setText(R.string.servicio_activo);
        borrarMarketCliente();
        borrarMarketDestino();
        showDriverMarker();
        setInfoTravelByHour("");
    }


    public void safeDistance(int idTravel, double distance) {
        //Evento On Click para eliminar un producto de la tabla Ventas 	por el nombre

        //Se inicializa la clase.
        antonellaSqlite = new SqliteUtil(view.getContext());

        SQLiteDatabase sqlite = antonellaSqlite.getWritableDatabase();

        if (distance > 0) {
            //Clase que permite llamar a los métodos para crear, eliminar, leer y actualizar registros. Se establecen permisos de escritura.
            sqlite = antonellaSqlite.getWritableDatabase();
            ContentValues content = new ContentValues();

            //Se añaden los valores introducidos de cada campo mediante clave(columna)/valor(valor introducido en el campo de texto)
            content.put(TravelSqliteEntity.COLUMN_ID, idTravel);
            content.put(TravelSqliteEntity.COLUMN_DISTANCE, distance);
            content.put(TravelSqliteEntity.IS_DRETURN, currentTravelEntity.isRoundTrip);
            sqlite.insert(TravelSqliteEntity.TABLE_NAME, null, content);
        }

        //Se cierra la conexión abierta a la Base de Datos
        sqlite.close();

    }

    public static void removeTravel(int idTravel) {

        //Se inicializa la clase.
        antonellaSqlite = new SqliteUtil(view.getContext());

        //Se establecen permisos de escritura
        SQLiteDatabase sqlite = antonellaSqlite.getWritableDatabase();

        //Se especifica en la cláusula WHERE el campo Producto y el producto introducido en el campo de texto a eliminar
        String WHERE = "WHERE " + TravelSqliteEntity.COLUMN_ID + " = '" + idTravel + "'";

        //Se borra el producto indicado en el campo de texto
        sqlite.delete(TravelSqliteEntity.TABLE_NAME, WHERE, null);

        //Se cierra la conexión abierta a la Base de Datos
        sqlite.close();
    }

    public static Float getDistanceSafe(int idTravel, int isReturn) {

        antonellaSqlite = new SqliteUtil(view.getContext());

        float COLUMN_DISTANCE = 0, _DISTANCE = 0;
        List<Float> listPointSave = new ArrayList<Float>();

        String WHERE = "";

        if (isReturn == 1 || isReturn == 0) {
            WHERE = TravelSqliteEntity.COLUMN_ID + " = '" + idTravel + "' AND " + TravelSqliteEntity.IS_DRETURN + " = " + isReturn + " ";


        } else {
            WHERE = TravelSqliteEntity.COLUMN_ID + " = '" + idTravel + "'";

        }

        String[] columnas = {
                TravelSqliteEntity.COLUMN_DISTANCE
        };

        SQLiteDatabase sqlite = antonellaSqlite.getWritableDatabase();

        //Ejecuta la sentencia devolviendo los resultados de los parámetros pasados de tabla, columnas, producto y orden de los resultados obtenidos.
        Cursor cursor = sqlite.query(TravelSqliteEntity.TABLE_NAME, columnas, WHERE, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            float OLD_COLUMN_DISTANCE = 0;
            int c = 0;
            do {
                COLUMN_DISTANCE = cursor.getFloat(0);

                if (COLUMN_DISTANCE != OLD_COLUMN_DISTANCE) {
                    if (OLD_COLUMN_DISTANCE > COLUMN_DISTANCE) {
                        listPointSave.add(OLD_COLUMN_DISTANCE);
                        _DISTANCE = _DISTANCE + OLD_COLUMN_DISTANCE;
                    }

                    int j = cursor.getCount();
                    if (c + 1 == j) {
                        listPointSave.add(COLUMN_DISTANCE);
                        _DISTANCE = _DISTANCE + COLUMN_DISTANCE;
                    }
                }

                OLD_COLUMN_DISTANCE = COLUMN_DISTANCE;

                c++;
            } while (cursor.moveToNext());
        }

        if (listPointSave.size() == 0) {
            _DISTANCE = COLUMN_DISTANCE;
        }
        sqlite.close();
        return _DISTANCE;
    }


    //region GPS Y MAPA
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);

                    }

                } else {
                    listenerFragmentChofer.showMessage(getActivity().getString(R.string.no_permiso_gps), 3);
                }
                return;
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //endregion

    private static void setBtnCenterDriverVisible(boolean isVisible)
    {
        try
        {
            if(isVisible)
            {
                btnCenterDriver.setVisibility(View.VISIBLE);
            }
            else {
                btnCenterDriver.setVisibility(View.GONE);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    //region MAPA
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        mGoogleMap = googleMap;
        mMarkerChofer = null;
        Log.e("_CAR_", "Se inicializa google maps");
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (    reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE ||
                        reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
                    setBtnCenterDriverVisible(true);
                    isMapCenter = false;
                }
            }
        });

        buildGoogleApiClient();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mGoogleMap.setMyLocationEnabled(false);
               // mGoogleMap.getUiSettings().setCompassEnabled(true);
               // mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                checkLocationPermission();
            }

        }
    }

    private void showDriverMarker(){

        if (mLastLocation!=null){
            Log.e("_CAR_", "NO es nulo mLastLocation");
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            if (mMarkerChofer !=null){
                Log.e("_CAR_", "NO es nulo mMarkerChofer");
                double rotationDouble= Utils.bearingBetweenLocations(mMarkerChofer.getPosition(),latLng);
                float rotation = (float) rotationDouble;
                mMarkerChofer.setRotation(rotation);
                mMarkerChofer.setPosition(latLng);
                mMarkerChofer.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.auto));
            }
            else{
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(nameLocation);
                markerOptions.flat(false);
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.auto);
                Bitmap b = bitmapdraw.getBitmap();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));

                mMarkerChofer = mGoogleMap.addMarker(markerOptions);
                Log.e("_CAR_", "SI es nulo mMarkerChofer");
            }
        }
        else{
            Log.e("_CAR_", "SI es nulo mLastLocation");
        }
    }



    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private void  mostarMarketCliente(Location location){
        borrarMarketCliente();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.img_pasajero);
        Bitmap b = bitmapdraw.getBitmap();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
        mMarkerCliente = mGoogleMap.addMarker(markerOptions);
    }


    public void borrarMarketCliente(){
        if (mMarkerCliente!=null){
            mMarkerCliente.remove();
        }
    }

    private void mostrarMarketDestino(Location location){
        borrarMarketDestino();

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_pin_red);
        Bitmap b = bitmapdraw.getBitmap();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
        mMarkerDestino = mGoogleMap.addMarker(markerOptions);
    }

    public void borrarMarketDestino(){
        if (mMarkerDestino!=null){
            mMarkerDestino.remove();
        }
    }

    ///Genera la url para consumir el api de google para unir los nodos
    private void crearRuta(Double latitudDestino,Double longitudDestino){

        Double latOrigen= Double.valueOf(mLastLocation.getLatitude());
        Double longOrigin=Double.valueOf(mLastLocation.getLongitude());
        Double latDestino=latitudDestino;
        Double longDestino=longitudDestino;

        String url = makeURL(latOrigen, longOrigin, latDestino, longDestino);

        StringRequest stringRequest = new StringRequest(url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPUESTA", response);
                        drawPath(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        String gMapApiKey = gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_104_GOOGLE_MAPS_API_KEY).getValue();

        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=");
        //urlString.append(Globales.SERVER_KEY);
        urlString.append(gMapApiKey);
        return urlString.toString();
    }

    public void drawPath(String  result) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            decodePoly(encodedString);
        }
        catch (JSONException e) {
            Log.e("ERROR PARSION JSON RUTA",e.toString());
        }
    }

    private void decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        showRutas(poly);
    }


    private void showRutas(List _list) {

        List<LatLng> list = _list;
        destinationRoute = mGoogleMap.addPolyline(new PolylineOptions()
                .addAll(list)
                .width(20)
                .color(getResources().getColor(R.color.colorAzul))
                .geodesic(true));
    }

    private static void clearDestinationRoute()
    {
        if(destinationRoute!=null)
        {
            destinationRoute.remove();
        }
    }



    //endregion


    //region GPS
    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        if(getActivity()!=null)
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListenerSingle,null);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener);
            }
        }
    }

    private android.location.LocationListener locationListener =  new android.location.LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            try
            {
                if (getActivity() != null && !ServicesSleep.mRunning) {
                    cambioUbicacion();
                } else {
                    listenerFragmentChofer.showMessage(getActivity().getString(R.string.existe_movimiento_tiempo),0);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            listenerFragmentChofer.showMessage(getActivity().getString(R.string.obteniendo_ubiación), 0);
            listenerFragmentChofer.ocultarActivarGPS();
            listenerFragmentChofer.activarServicio();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onProviderDisabled(String s) {
            listenerFragmentChofer.activarGps();
        }
    };

    private android.location.LocationListener locationListenerSingle =  new android.location.LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            try
            {
                if (getActivity() != null && !ServicesSleep.mRunning) {
                    cambioUbicacion();
                } else {
                    listenerFragmentChofer.showMessage(getActivity().getString(R.string.existe_movimiento_tiempo),0);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            listenerFragmentChofer.showMessage(getActivity().getString(R.string.obteniendo_ubiación), 0);
            listenerFragmentChofer.ocultarActivarGPS();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void cambioUbicacion(){
        try {
            Log.v("HOMEFRAGMENT", "cambioUbicacion");
            showDriverMarker();
            clearDestinationRoute();
            if (obtenerDireccion()) {
                LatLng actualLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                if(isMapCenter)
                {
                    centerMapInActualPosition();
                }

                if (currentTravelEntity != null  && currentTravelEntity.getLonOrigin() != null) {
                   //El siguiente metodo funciona bien, dibuja la ruta y se va recalculando en cada iteración.
                    //El problema es que a veces, se queda dibujada la ruta y no se elimina, pero se crea una nueva ruta
                    //y se va eliminando esa, quedando la anterior "huérfana"
                    // drawRouteIfTravelExists();
                }
                calculatePastRouteIfTravelIsInCourse(actualLocation);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculatePastRouteIfTravelIsInCourse(LatLng actualLocation) {
        // SI POSEE UN VIAJE DIBUAMOS LA RUTA QUE ESTA RECORRINEDO EL CHOFER //
        if (currentTravelEntity != null) {
            if (currentTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_EN_CURSO) {
                listPosition.add(new LatLng(actualLocation.latitude, actualLocation.longitude));
                options = new
                        PolylineOptions()
                        .width(5)
                        .color(Color.TRANSPARENT)
                        .geodesic(true);
                //Log.d("totalDistance listPosition", String.valueOf(listPosition.size()));

                for (int z = 0; z < listPosition.size(); z++) {
                    LatLng point = listPosition.get(z);
                    if (currentTravelEntity.isRoundTrip == 1 && optionReturnActive == null)//VERIFICAMOS SI ESTA ACTIVA LA VUETA PARA SABER DESDE QUE UBUCACION SE REALIZO
                    {
                        optionReturnActive = new
                                PolylineOptions()
                                .width(5)
                                .color(Color.TRANSPARENT)
                                .geodesic(true);
                        optionReturnActive.add(point);


                    } else {
                        options.add(point);
                    }
                }
            } else {
                if (options != null) {
                    options.getPoints().clear();
                }
                options = null;
                listPosition.clear();
                listPosition = new ArrayList<>();
            }


            float[] _RECORD = calculateMiles();
            double DISTANCE = Utils.round((_RECORD[0] + _RECORD[1]) * 0.001, 2);

            /*
             * GUARDAMOS LA DITANCIA DEL RECORRIDO EN EL SQLITE LOCAL
             */
            safeDistance(currentTravelEntity.getIdTravel(), DISTANCE);
            txt_distance_real.setText(Utils.round(DISTANCE, 2) + "Km");

        } else {
            if (options != null) {
                options.getPoints().clear();
            }
            options = null;
            listPosition.clear();
            listPosition = new ArrayList<>();

            Log.d("CODUCE NO", String.valueOf(listPosition.size()));
            //content_ditanceReal.setVisibility(View.INVISIBLE);
            txt_distance_real.setText(0.0 + "Km");
        }
    }

    private void drawRouteIfTravelExists() {
        Log.e("STATUS TRAVEL",""+currentTravelEntity.getIdSatatusTravel());
        if (currentTravelEntity.getIdSatatusTravel()== Globales.StatusTravel.VIAJE_ACEPTADO_POR_CHOFER){
            if (mMarkerCliente!=null){
                crearRuta(mMarkerCliente.getPosition().latitude,mMarkerCliente.getPosition().longitude);
            }
        }else if (currentTravelEntity.getIdSatatusTravel()==Globales.StatusTravel.VIAJE_EN_CURSO){
            borrarMarketCliente();
            if (mMarkerDestino!=null){
                crearRuta(mMarkerDestino.getPosition().latitude,mMarkerDestino.getPosition().longitude);
            }
        }
    }

    private Boolean obtenerDireccion() throws IOException {
        Geocoder gCoder = new Geocoder(getActivity());
        List<android.location.Address> addresses = null;
        addresses = gCoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if (addresses.size() > 0) {
            android.location.Address returnedAddress = addresses.get(0);
            String strAdd = returnedAddress.getAddressLine(0);
            nameLocation = strAdd;

            return true;
        }else {
            return false;
        }
    }
    //endregion


    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}