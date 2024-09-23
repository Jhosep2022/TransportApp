package com.example.apptransport;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener,  GoogleApiClient.ConnectionCallbacks{

    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static GoogleApiClient mGoogleApiClient;
    private boolean isFirstTime;
    double lat, lon;
    int type;
    Marker markerCenter;
    TextView txtAddress;
    Address addressSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_map);

        Bundle extras = getIntent().getExtras();
        lat = extras.getDouble("lat");
        lon = extras.getDouble("lon");
        type = extras.getInt("type");
        initializeControls();
        isFirstTime=true;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initializeControls()
    {
        txtAddress=findViewById(R.id.txtAddress);
        TextView txt_name = findViewById(R.id.txt_name);
        ImageButton imageButton_cerrar = findViewById(R.id.imageButton_cerrar);
        Button btn_select_address = findViewById(R.id.btn_select_address);

        txt_name.setText(type== Globales.TypeAddressAutocomplete.ADDRESS_FROM ? getString(R.string.seleccionar_origen_title) : getString(R.string.seleccionar_destino_title));
        imageButton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btn_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelectAddress();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            SelectAddressMapActivity.this, R.raw.map_style_uber));

            if (!success) {
                Log.e("MAP_ERROR", "Style parsing failed.");
            }
        } catch (Exception e) {
            Log.e("MAP_ERROR", "Style parsing failed.");
        }


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney, Australia, and move the camera.


        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);


            } else {
                //Request Location Permission
                checkLocationPermission();
            }

        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }

        LatLng myPosition = new LatLng(lat, lon);
        getAddressFromLatLng(myPosition);
        markerCenter = mMap.addMarker(new MarkerOptions().position(myPosition).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,17));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                markerCenter.setPosition(mMap.getCameraPosition().target);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                getAddressFromLatLng(mMap.getCameraPosition().target);
            }
        });

    }

    private void getAddressFromLatLng(LatLng latLng)
    {
        try {
            Geocoder gCoder = new Geocoder(this);
            List<Address> addresses = gCoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressSelected =  addresses.get(0);
            txtAddress.setText(addressSelected.getAddressLine(0));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        try
        {
            //fusedLocationClient.

            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
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

    @Override
    public void onLocationChanged(Location location) {
        if(isFirstTime)
        {

            isFirstTime = false;
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void btnSelectAddress()
    {
        if(addressSelected!=null && addressSelected.getAddressLine(0)!=null)
        {
            Intent data = new Intent();
            data.putExtra("lat",addressSelected.getLatitude());
            data.putExtra("lng",addressSelected.getLongitude());
            data.putExtra("address",addressSelected.getAddressLine(0));
            data.putExtra("addressShort",getShortAddress());
            data.putExtra("type",type);
            setResult(RESULT_OK, data);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),getString(R.string.debe_ingresar_una_direccion_valida), Toast.LENGTH_LONG).show();
        }
    }

    private String getShortAddress() {
        String result = "";
        try {
            if (addressSelected != null) {
                if(!TextUtils.isEmpty(addressSelected.getThoroughfare()) && !"UNNAMED ROAD".equalsIgnoreCase(addressSelected.getThoroughfare())){
                    result = addressSelected.getThoroughfare();
                }

                if(!TextUtils.isEmpty(result)){
                    if(!TextUtils.isEmpty(addressSelected.getSubThoroughfare()) && !"".equalsIgnoreCase(addressSelected.getSubThoroughfare()))
                    {
                        result = result.concat(" ").concat(addressSelected.getSubThoroughfare());
                    }
                    else{
                        result = result.concat(" ").concat("s/n");
                    }
                }
                else
                {
                    result = addressSelected.getAddressLine(0);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}