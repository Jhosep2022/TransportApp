package com.example.apptransport.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.apreciasoft.mobile.asremis.Entity.Client;
import com.apreciasoft.mobile.asremis.Entity.TermsAndConditionsUpdate;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Dialog.ListenerDialogRecoveryPass;
import com.apreciasoft.mobile.asremis.Dialog.RecoveryPassDialog;
import com.apreciasoft.mobile.asremis.Dialog.UpdateDialog;
import com.apreciasoft.mobile.asremis.Entity.VehicleType;
import com.apreciasoft.mobile.asremis.Entity.login;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.user;
import com.apreciasoft.mobile.asremis.Entity.userFull;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.android.px.internal.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apreciasoft.mobile.asremis.Util.Utils.verificaConexion;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class LoginActivity extends AppCompatActivity implements ListenerDialogRecoveryPass {

    public static final String TAG = "NOTICIAS";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int REGISTER_ACTIVITY = 10;
    public static final int REGISTER_ACTIVITY_SOCIAL = 11;
    public ProgressDialog loading;
    ServicesLoguin apiService = null;
    public  GlovalVar gloval = null;

    public  SharedPreferences.Editor editor;
    public  SharedPreferences pref;
    private EditText mUser,mPass;
    private Button btnLogin,btn_newacount,btn_recovery;
    RecoveryPassDialog dialogRecovery;

    //The original Facebook button
    private LoginButton btn_facebook_original;
    private Button btn_facebook_custom;
    CallbackManager callbackManagerFacebook;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    boolean isFacebookLoginCalled;

    private View loadingView;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.gloval = ((GlovalVar) getApplicationContext());
        pref = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0);

        if(checkAndRequestPermissions()) {

            Gson gson = new Gson();
            Client clientInfo = null;
            String clientInfoString = pref.getString("clientInfo", "");
            if (!TextUtils.isEmpty(clientInfoString)) {
                clientInfo = gson.fromJson(pref.getString("clientInfo", ""), Client.class);
                gloval.setGv_clientinfo(clientInfo);
                if (pref.getBoolean("isLoged", false)) {

                    gloval.setGv_logeed(true);
                    gloval.setGv_user_id(pref.getInt("user_id", 0));
                    gloval.setGv_idResourceSocket(pref.getString("is_resourceSocket", ""));
                    gloval.setGv_id_cliet(pref.getInt("client_id", 0));
                    gloval.setGv_id_profile(pref.getInt("profile_id", 0));
                    gloval.setGv_id_driver(pref.getInt("driver_id", 0));
                    gloval.setGv_user_mail(pref.getString("user_mail", ""));
                    gloval.setGv_user_name(pref.getString("user_name", ""));
                    gloval.setGv_base_intance(pref.getString("instance", ""));
                    gloval.setGv_nr_driver(pref.getString("nrDriver", ""));

                    TypeToken<List<paramEntity>> token3 = new TypeToken<List<paramEntity>>() {
                    };
                    List<paramEntity> listParam = gson.fromJson(pref.getString("param", ""), token3.getType());
                    gloval.setGv_param(listParam);

                    TypeToken<List<VehicleType>> token2 = new TypeToken<List<VehicleType>>() {
                    };
                    List<VehicleType> vehicleTypenew = gson.fromJson(pref.getString("list_vehichle", ""), token2.getType());
                    gloval.setGv_listvehicleType(vehicleTypenew);

                    HttpConexion.setBase(pref.getString("instance", ""));
                }
            }
            else{
                gloval.setGv_logeed(false);
            }
        }

        if(gloval.getGv_logeed())
        {
            if(Utils.isCliente(gloval.getGv_id_profile()))
            {
                // LAMAMOS A EL SEGUNDO ACTIVITY DE HOME CIENT//
                Intent homeClient = new Intent(LoginActivity.this, HomeClienteNewStyle.class);
                startActivity(homeClient);
                finish();
            }else if(gloval.getGv_id_profile() == Globales.ProfileId.DRIVER) {
                // LAMAMOS A EL SEGUNDO ACTIVITY//
                Intent home = new Intent(LoginActivity.this, HomeChofer.class);
                startActivity(home);
                finish();
            }
        }else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                // only for gingerbread and newer versions
                checkAndRequestPermissions();
            }

            // ESTO PERMITE QUE CUANDO LA APP ESTE EN SEGUNDO PLADONO Y LA NOTIFICACION LLEGUE SI LÑE DA CLICK ABRAE LA APP EN HOME ACTIVITY
            if (getIntent().getExtras() != null) {
                if(gloval.getGv_id_profile() == Globales.ProfileId.DRIVER){
                    Intent intent = new Intent(LoginActivity.this, HomeChofer.class);
                    startActivity(intent);
                }else {
                    inicializaVista();
                }

            }
            else {
               inicializaVista();
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        onActivityResultFacebook(requestCode,resultCode,data);
        if(requestCode==REGISTER_ACTIVITY_SOCIAL)
        {
            manageUserCreation(resultCode, data);
        }
    }

    private void onActivityResultFacebook(int requestCode, int resultCode, @Nullable Intent data)
    {
        try{
            if(callbackManagerFacebook!=null)
            {
                callbackManagerFacebook.onActivityResult(requestCode, resultCode, data);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void manageUserCreation(int resultCode, Intent data) {
        hideLoading();
        if(resultCode == Activity.RESULT_OK){
            String socialid = data.getStringExtra("socialid");
            if(!TextUtils.isEmpty(socialid))
            {
                showLoading();
                login("", "", socialid, new LoginCallbacks() {
                    @Override
                    public void login(boolean success) {
                        if(!success)
                        {
                            hideLoading();
                            showMessage(getString(R.string.user_no_not_exist), Globales.StatusToast.FAIL);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(profileTracker!=null)
            {
                accessTokenTracker.stopTracking();
                profileTracker.stopTracking();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    private void inicializaVista(){

        setContentView(R.layout.activity_login);

        if (!verificaConexion(this)) {
            Snackbar snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.problema_internet),
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

        HttpConexion.setBase(HttpConexion.instance);

        mUser =  findViewById(R.id.user_txt);
        mPass =  findViewById(R.id.pass_text);

        btnLogin =  findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validarCampos();
            }
        });

        btn_newacount =  findViewById(R.id.btn_newacount);
        btn_newacount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFromRegister();
            }
        });


        btn_recovery =  findViewById(R.id.btn_recovery_pass);
        btn_recovery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDialogRecoery();
            }
        });


        /*Login con facebook*/
        initializeFacebook();

        loadingView = findViewById(R.id.loadingView);
        hideLoading();
    }

    private void showLoading()
    {
        try{
            if(loadingView!=null)
            {
                loadingView.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void hideLoading()
    {
        try{
            if(loadingView!=null)
            {
                loadingView.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initializeFacebook() {

        try{
            boolean useFacebook = true;
            callbackManagerFacebook = CallbackManager.Factory.create();
            isFacebookLoginCalled=false;

            btn_facebook_original = findViewById(R.id.btn_facebook_original);
            btn_facebook_custom = findViewById(R.id.btn_facebook_custom);
            btn_facebook_custom.setVisibility(useFacebook ? View.VISIBLE : View.INVISIBLE);

            btn_facebook_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLoading();
                    Profile profile = Profile.getCurrentProfile();
                    isFacebookLoginCalled = false;
                    if (profile != null) {
                        getFacebookEmail(AccessToken.getCurrentAccessToken(), profile);
                    }
                    else{
                        btn_facebook_original.performClick();
                    }
                }
            });


            btn_facebook_original.setPermissions("email");
            btn_facebook_original.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
            //btn_facebook_original

            btn_facebook_original.registerCallback(callbackManagerFacebook, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    accessTokenTracker = new AccessTokenTracker() {
                        @Override
                        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

                        }
                    };
                    accessTokenTracker.startTracking();
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                            getFacebookEmail(loginResult.getAccessToken(), profile1);
                            //profile1 tiene el profile
                            //Llamar a la funcion para seguir logueandose o creando la cuenta
                        }
                    };
                    profileTracker.startTracking();
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        getFacebookEmail(loginResult.getAccessToken(), profile);
                    }
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.e("LoginFBCancel", "Login cancelado");
                    hideLoading();
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e("ErrorLoginFB", exception.toString(), exception);
                    hideLoading();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void logoutFacebook()
    {
        try{
            LoginManager.getInstance().logOut();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getFacebookEmail(AccessToken accessToken, Profile profile)
    {
        if(!isFacebookLoginCalled) {
            isFacebookLoginCalled = true;
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            // Application code
                            try {
                                String email = object.getString("email");
                                continueWithFacebookLogin(email, profile);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                continueWithFacebookLogin("", profile);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    private void continueWithFacebookLogin(String email, Profile profile)
    {
        String id = profile.getId();
        login("","", id, new LoginCallbacks(){
            @Override
            public void login(boolean success) {
                hideLoading();
                if(!success)
                {
                    Intent intent = new Intent(getApplicationContext(), FragmentManagerRegisterActivity.class);
                    intent.putExtra("email", !TextUtils.isEmpty(email) ? email: "");
                    intent.putExtra("socialid", id);
                    intent.putExtra("firstName", !TextUtils.isEmpty(profile.getFirstName())?profile.getFirstName():"" );
                    intent.putExtra("lastName", !TextUtils.isEmpty(profile.getFirstName()) ? profile.getLastName() : "" );
                    startActivityForResult(intent, REGISTER_ACTIVITY_SOCIAL);
                }
            }
        } );
    }



    private  boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        List<String> listPermissionsNeeded = new ArrayList<>();

        /*if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            Log.d("P-)","01");
        }*/
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.d("P-)","02");
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d("P-)","03");
        }
      /* if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
            Log.d("P-)","04");
        }*/
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //explain("Necesitamos algunos permisos Para esta Aplicacion");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancelar), okListener)
                .create()
                .show();
    }

    private void validarCampos(){
        if (mUser.getText().toString().trim().isEmpty()){
            mUser.setError(getString(R.string.completar_campo));
            mUser.requestFocus();
        }else if (mPass.getText().toString().trim().isEmpty()){
            mPass.setError(getString(R.string.completar_campo));
            mPass.requestFocus();
        }else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mPass.getWindowToken(), 0);

            checkVersion();
        }
    }

    public  void  checkVersion()
    {

        showLoading();
        if (this.apiService == null) {
            this.apiService = HttpConexion.getUri().create(ServicesLoguin.class);
        }

        try {
            btnLogin.setEnabled(false);
            btnLogin.setText(R.string.validando);

            apiService.checkVersion(BuildConfig.VERSION_NAME).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    if (response.code() == 200) {
                        boolean rs =  response.body();
                        if (!rs || Utils.isDeveloperInstance()) {
                            login(mUser.getText().toString(), mPass.getText().toString(),"", null);
                        } else {
                            btnLogin.setEnabled(true);
                            btnLogin.setText(R.string.ingresar);
                            openDialogUpdate();
                            hideLoading();
                        }

                    } else {
                        btnLogin.setEnabled(true);
                        btnLogin.setText(R.string.ingresar);
                        showMessage(getString(R.string.fallo_version_app),2);
                        hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText(R.string.ingresar);
                    showMessage(getString(R.string.fallo_general),3);


                }
            });

        } finally {
            this.apiService = null;
        }

    }

    public String getErrorBody(Response<userFull> response)
    {
        String result="";
        try{
            result = response.errorBody().string();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public void login(String email, String password, String socialId, LoginCallbacks callbacks) {

        if (this.apiService == null) {
            this.apiService = HttpConexion.getUri().create(ServicesLoguin.class);
        }

        //login auth = new login(new user(mUser.getText().toString(), mPass.getText().toString(),2, socialId));
        login auth = new login(new user(email, password,2, socialId));

        try {

            Utils.saveValueToSharedPreference(getApplicationContext(),Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_1_CLIENT,"1");
            Utils.saveValueToSharedPreference(getApplicationContext(),Globales.NEED_LOGIN_NEW_CLIENT_STYLE_V4_4_1_CLIENT,"1");
            btnLogin.setEnabled(false);
            btnLogin.setText(R.string.validando);


            Call<userFull> call = this.apiService.getUser(auth);

            call.enqueue(new Callback<userFull>() {
                @Override
                public void onResponse(Call<userFull> call, Response<userFull> response) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText(R.string.ingresar);
                    try {
                        String errorBodyString = getErrorBody(response);
                        if (response.code() ==   200) {
                            userFull userLogued = response.body();

                            if(userLogued.response.getUser().getIdProfileUser() == 2 ||
                                    userLogued.response.getUser().getIdProfileUser() == 5 ||
                                    userLogued.response.getUser().getIdProfileUser() == 3){


                                if (!userLogued.response.isDriverInactive()) {

                                    gloval.setGv_user_id(userLogued.response.getUser().getIdUser());
                                    gloval.setGv_user_mail(userLogued.response.getUser().getEmailUser());

                                    gloval.setGv_user_name(userLogued.response.getUser().getFirstNameUser() + " "
                                            + userLogued.response.getUser().getLastNameUser());

                                    gloval.setGv_id_cliet(userLogued.response.getUser().getIdClient());
                                    gloval.setGv_id_driver(userLogued.response.getUser().getIdDriver());
                                    gloval.setGv_id_profile(userLogued.response.getUser().getIdProfileUser());
                                    gloval.setGv_id_vehichle(userLogued.response.getUser().getIdVeichleAsigned());
                                    gloval.setGv_travel_current(userLogued.response.getCurrentTravel());
                                    gloval.setGv_param(userLogued.response.getParam());
                                    gloval.setGv_logeed(true);
                                    //gloval.setGv_driverinfo(userLogued.response.getDriver());
                                    gloval.setGv_clientinfo(userLogued.response.getClient());
                                    gloval.setGv_listvehicleType(userLogued.response.getListVehicleType());

                                    gloval.setGv_idResourceSocket(userLogued.response.getUser().getIdResourceSocket());
                                    Utils.saveCurrency(getApplicationContext(), userLogued.response.getCurrency());

                                    HttpConexion.setBase(userLogued.response.getInstance());
                                    // SETEAMOS LA INTANCIA PARA AUTENTICARNOS

                                    if (Utils.isCliente(userLogued.response.getUser().getIdProfileUser())) {
                                        // LAMAMOS A EL SEGUNDO ACTIVITY DE HOME CIENT//
                                        Intent homeClient = new Intent(LoginActivity.this, HomeClienteNewStyle.class);
                                        startActivity(homeClient);
                                        finish();
                                    } else {
                                        gloval.setGv_srviceActive(userLogued.response.getDriver().getIdStatusDriverTravelKf());
                                        gloval.setGv_nr_driver(userLogued.response.getDriver().getNrDriver());
                                        if (userLogued.response.getCurrentTravel() != null) {
                                            gloval.setGv_travel_current(userLogued.response.getCurrentTravel());
                                        }
                                        // LAMAMOS A EL SEGUNDO ACTIVITY//
                                        Intent home = new Intent(LoginActivity.this, HomeChofer.class);
                                        startActivity(home);
                                        finish();
                                    }

                                    /** PREFERENCIAS LOCALES **/
                                    pref = getApplicationContext().getSharedPreferences(HttpConexion.instance, 0); // 0 - for private mode
                                    editor = pref.edit();

                                    Gson gson = new Gson();

                                    editor.putBoolean("isLoged", true);
                                    editor.putInt("user_id", gloval.getGv_user_id());
                                    editor.putString("is_resourceSocket", gloval.getGv_idResourceSocket());
                                    editor.putInt("client_id", gloval.getGv_id_cliet());
                                    editor.putInt("profile_id", gloval.getGv_id_profile());
                                    editor.putInt("driver_id", gloval.getGv_id_driver());
                                    editor.putString("user_mail", gloval.getGv_user_mail());
                                    editor.putString("user_name", gloval.getGv_user_name());
                                    editor.putString("instance", gloval.getGv_base_intance());
                                    editor.putString("param", gson.toJson(gloval.getGv_param()));
                                    editor.putString("list_vehichle", gson.toJson(gloval.getGv_listvehicleType()));
                                    editor.putString("nrDriver", gloval.getGv_nr_driver());
                                    editor.putString("clientInfo", gson.toJson(gloval.getGv_clientinfo()));
                                    ;
                                    editor.putInt("time_slepp", 0);
                                    editor.commit(); // commit changes
                                    /************************/

                                } else {
                                    hideLoading();
                                    showMessagePopup(getString(R.string.usuario_inactivo),2);
                                    HttpConexion.setBase(HttpConexion.instance);

                                }

                            }else {
                                hideLoading();
                                showMessage(getString(R.string.usuario_contraseña_invalida),2);
                                HttpConexion.setBase(HttpConexion.instance);
                            }
                        }  else if (response.code() == 203)  {
                            showMessage(getString(R.string.usuario_contraseña_invalida),2);
                            HttpConexion.setBase(HttpConexion.instance);
                            hideLoading();


                        }  else if (response.code() == 212)  {
                            showMessagePopup(getString(R.string.usuario_inactivo),2);
                            HttpConexion.setBase(HttpConexion.instance);
                            hideLoading();

                        }
                        else if (response.code() == 400)  {
                            hideLoading();
                            showMessage(getMessageFromError(errorBodyString),0);
                        }else {
                            if(userDoNotExist(response) && !TextUtils.isEmpty(socialId))
                            {
                                callbacks.login(false);
                            }
                            else if(!TextUtils.isEmpty(getStringFromJsonError(errorBodyString,"terms")))
                            {
                                hideLoading();
                                String terms = getStringFromJsonError(errorBodyString,"terms");
                                String userId = getStringFromJsonError(errorBodyString, "id");
                                showMessageToAcceptTermsAndConditions(terms, userId, email, password, socialId, callbacks);
                            }
                            else
                            {
                                hideLoading();
                                showMessage(getStringFromJsonError(errorBodyString,"error"), 3);
                                HttpConexion.setBase(HttpConexion.instance);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage(getString(R.string.fallo_general), 3);
                        hideLoading();
                    }
                }

                @Override
                public void onFailure(Call<userFull> call, Throwable t) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText(R.string.ingresar);
                    hideLoading();
                    showMessage(getString(R.string.fallo_general),3);
                }
            });

        }finally {
            this.apiService = null;
        }
    }

    private void showMessageToAcceptTermsAndConditions(String terms, String userId, String mail, String password,String socialId, LoginCallbacks callbacks) {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setMessage(terms);
        dialog.setTitle("Aceptar Términos y Condiciones");
        dialog.setPositiveButton(" ACEPTAR ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                callAcceptTermsAndConditions(userId, mail, password, socialId, callbacks);
            }
        });
        dialog.setNegativeButton("RECHAZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void callAcceptTermsAndConditions(String id, String mail, String password, String socialId, LoginCallbacks callbacks)
    {
        showLoading();
        if (this.apiService == null) {
            this.apiService = HttpConexion.getUri().create(ServicesLoguin.class);
        }
        TermsAndConditionsUpdate terms=new TermsAndConditionsUpdate(id,1);

        Call<resp> call = this.apiService.updateTermsAndConditions(terms);
        call.enqueue(new Callback<resp>() {
            @Override
            public void onResponse(Call<resp> call, Response<resp> response) {
                login(mail, password, socialId, callbacks);
            }

            @Override
            public void onFailure(Call<resp> call, Throwable t) {
                t.printStackTrace();
                hideLoading();
            }
        });
    }

    private boolean userDoNotExist(Response<userFull> response) {
        try {
            String msgError = response.errorBody().string().toLowerCase();
            boolean result= response!=null &&
                    response.errorBody()!=null &&
                    (msgError.contains("el usuario no se encuentra registrado") ||
                            TextUtils.isEmpty(msgError));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getMessageFromError(String msgError)
    {
        String result="";
        try{
            JSONObject obj2 =new JSONObject(msgError);
            result  = obj2.getString("error");
        }
        catch (Exception ex)
        {
            result=TextUtils.isEmpty(msgError) ? getString(R.string.error_intente_nuevamente): msgError;
        }
        return result;
    }

    private String getStringFromJsonError(String msgError, String key)
    {
        String result;
        try{
            JSONObject obj2 =new JSONObject(msgError);
            result  = obj2.getString(key);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            result="";
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void  openFromRegister()
    {
        Intent intent = new Intent(getApplicationContext(), FragmentManagerRegisterActivity.class);
        startActivityForResult(intent, REGISTER_ACTIVITY);
    }

    private void openDialogRecoery(){
        dialogRecovery =  new RecoveryPassDialog(this);
        dialogRecovery.show(getSupportFragmentManager(), "DialogRecuperar");
    }

    private void openDialogUpdate(){
        new UpdateDialog().show(getSupportFragmentManager(), "DialogUpdate");
    }

    @Override
    public void respuestaDialogRecovery(String mensaje) {
        try{
            showMessage(mensaje,0);
            dialogRecovery.dismiss();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    private void showMessage(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status);
    }

    private void showMessagePopup(String mensaje,int status){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.informacion))
                .setMessage(mensaje)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

interface LoginCallbacks{
    void login(boolean success);
}
