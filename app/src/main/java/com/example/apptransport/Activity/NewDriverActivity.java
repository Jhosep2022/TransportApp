package com.example.apptransport.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.apreciasoft.mobile.asremis.Entity.Driver;
import com.apreciasoft.mobile.asremis.Entity.SocialLoginData;
import com.apreciasoft.mobile.asremis.viewmodels.NewDriverActivityViewModel;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.apreciasoft.mobile.asremis.Entity.NewDriverPersonalData;
import com.apreciasoft.mobile.asremis.Entity.NewDriverPhotos;
import com.apreciasoft.mobile.asremis.Entity.NewDriverVehicleData;
import com.apreciasoft.mobile.asremis.Entity.dataAddPlusDriverEntity;
import com.apreciasoft.mobile.asremis.Entity.driverAdd;
import com.apreciasoft.mobile.asremis.Entity.fleet;
import com.apreciasoft.mobile.asremis.Entity.login;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.user;
import com.apreciasoft.mobile.asremis.Entity.userFull;
import com.apreciasoft.mobile.asremis.Fragments.ListenerRegisterChofer;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;

import com.apreciasoft.mobile.asremis.Activity.ui.main.SectionsPagerAdapter;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Util.CustomViewPagerRegister;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.RequestHandler;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewDriverActivity extends AppCompatActivity {

    public static final String UPLOAD_DRIVER_PHOTO_URL =  HttpConexion.BASE_URL+HttpConexion.base+"/Frond/safeimgDriver.php";
    public static final String UPLOAD_DRIVER_PHOTO_KEY = "image";

    private static final int MAX_HEIGHT=1024;
    private static final int MAX_WIDTH=1024;

    List<paramEntity> listParam=null;
    private TabLayout tabs;
    private CustomViewPagerRegister viewPager;
    private ListenerRegisterChofer listenerRegisterChofer;
    private NewDriverPersonalData personalData;
    private NewDriverVehicleData vehicleData;
    private NewDriverPhotos driverPhotos;
    int actual=0;
    int actualStepForTitle=0;
    private ProgressDialog progressDialogSave;
    ServicesDriver apiDriver = null;
    private Integer userIdNew;
    private Integer driverIdNew;
    private List<String> listErrors;
    private String firstName, lastName, email, socialid;
    private SocialLoginData socialLoginData;

    private NewDriverActivityViewModel newDriverActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_driver);
        initializeListenerRegisterChofer();
        loadParams();
        initializeViewModel();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), listenerRegisterChofer, socialLoginData);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        apiDriver = HttpConexion.getUri().create(ServicesDriver.class);
        loadParamsFromApi();

        setupTablLayout();
    }

    private void loadParams()
    {
        try{
            Bundle b = getIntent().getExtras();
            if(b != null)
            {
                firstName = b.getString("firstName");
                lastName = b.getString("lastName");
                email = b.getString("email");
                socialid = b.getString("socialid");
                socialLoginData=new SocialLoginData(firstName,lastName,email,socialid);

            }
            else{
                cleanParams();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            cleanParams();
        }
    }

    private void cleanParams()
    {
        firstName = "";
        lastName = "";
        email = "";
        socialid = "";
        socialLoginData = new SocialLoginData();
    }

    private void initializeViewModel()
    {
        newDriverActivityViewModel = new ViewModelProvider(this).get(NewDriverActivityViewModel .class);
    }

    private void showIconPersonalData(boolean isComplete)
    {
        if(isComplete)
        {
            tabs.getTabAt(0).setIcon(R.drawable.ic_person_ok);
        }
        else{
            tabs.getTabAt(0).setIcon(R.drawable.ic_person_white_24dp);
        }
    }

    private void showIconCarData(boolean isComplete)
    {
        if(isComplete)
        {
            tabs.getTabAt(1).setIcon(R.drawable.ic_car_ok);
        }
        else{
            tabs.getTabAt(1).setIcon(R.drawable.ic_directions_car_white_24dp);
        }
    }

    private void initializeListenerRegisterChofer()
    {
        listenerRegisterChofer= new ListenerRegisterChofer() {
            @Override
            public void btnSiguiente(int pos) {
                if(pos==0)
                {
                    tabs.getTabAt(1).select();
                    showIconPersonalData(true);
                }else if(pos==1)
                {
                    tabs.getTabAt(2).select();
                    showIconPersonalData(true);
                    showIconCarData(true);
                }
            }

            @Override
            public void btnAtras(int pos) {
                if(pos==1)
                {
                    tabs.getTabAt(0).select();
                    showIconPersonalData(false);
                    showIconCarData(false);
                }else if(pos==2)
                {
                    tabs.getTabAt(1).select();
                    showIconCarData(false);
                }
            }

            @Override
            public void savePersonalData(NewDriverPersonalData _personalData) {
             personalData =   _personalData;
            }

            @Override
            public void saveVehicle(NewDriverVehicleData _vehicleData) {
                vehicleData = _vehicleData;
            }

            @Override
            public void btnRegisterDriver(NewDriverPhotos photos) {
                driverPhotos = photos;
                listErrors=new ArrayList<>();
                registerDriver();
            }
        };
    }

    private void setupTablLayout()
    {
        tabs.getTabAt(0).setIcon(R.drawable.ic_person_white_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_directions_car_white_24dp);
        tabs.getTabAt(2).setIcon(R.drawable.ic_add_a_photo_white_24dp);
        viewPager.setPagingEnabled(false);
        //tabs.setTabIconTintResource(R.color.colorRojo);

        LinearLayout tabStrip = ((LinearLayout)tabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
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
                           listParam = response.body();
                            Utils.saveParamsToLocalPreferences(getApplicationContext(), listParam);
                            newDriverActivityViewModel.postParams(listParam);
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

    private void saveDriver(){
        dataAddPlusDriverEntity data =  new dataAddPlusDriverEntity(
                new driverAdd(
                        personalData.nombre.concat(" ").concat(personalData.apellido),
                        personalData.nroChofer,
                        personalData.email,
                        personalData.password,
                        personalData.telefono,
                        1,
                        1,
                        socialid,
                        driverPhotos.isChecked),
                new fleet(vehicleData.idMarca,vehicleData.idModel,vehicleData.idCategoria,vehicleData.dominio)
        );

        apiDriver.addPluDriver(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == 200) {
                        if(!TextUtils.isEmpty(response.body()))
                        {
                            getUserCreated(response.body());
                        }
                    }  else if (response.code() == 201) {
                        progressDialogSave.dismiss();
                        showPopupError(getString(R.string.error), getString(R.string.new_driver_error_email_registered), TabCreateDriver.USER_DATA );
                    } else if(response.code()==203) {
                        progressDialogSave.dismiss();
                        showPopupError(getString(R.string.error), getString(R.string.new_driver_error_driver_registered), TabCreateDriver.USER_DATA);
                    }else {
                        progressDialogSave.dismiss();
                        showPopupError(getString(R.string.error), response.message(), TabCreateDriver.PHOTO_DATA);
                    }
                }catch (Exception e){
                    progressDialogSave.dismiss();
                    Log.e("CREAR CHOFER EXCEPTION", e.toString());
                    showSnack(getString(R.string.fallo_general),Globales.StatusToast.FAIL);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CREAR CHOFER FAILURE", t.toString());
                progressDialogSave.dismiss();
                showSnack(getString(R.string.fallo_general), Globales.StatusToast.FAIL);
            }
        });
    }




    private void registerDriver()
    {
        actualStepForTitle=0;
        progressDialogSave =  getProgressDialog();
        progressDialogSave.show();
        saveDriver();
    }

    private ProgressDialog getProgressDialog()
    {
        ProgressDialog dialog = ProgressDialog.show( this, getString(R.string.new_driver_registering),
                getString(R.string.new_driver_saving_driver).concat("\n\n").concat(getString(R.string.new_driver_please_not_go_out)).concat(getActualStepAndTotal()), true,false);
        return dialog;
    }

    private String getActualStepAndTotal()
    {
        actualStepForTitle++;
        String result=" (";
        result = result.concat(String.valueOf(actualStepForTitle));
        result = result.concat("/");
        int totalStep = 1;
        totalStep+=driverPhotos.bmpFotoDelConductor!=null?1:0;
        totalStep+=driverPhotos.bmpFotoChapaVehiculo!=null?1:0;
        totalStep+=driverPhotos.bmpFotoAntecedentesPoliciales!=null?1:0;
        totalStep+=driverPhotos.bmpFotoVehiculo!=null?1:0;
        totalStep+=driverPhotos.bmpLicenciaDelConductor!=null?1:0;

        result = result.concat(String.valueOf(totalStep));
        result = result.concat(")");
        return result;
    }



    private void showSnack(String mensaje,int status){
        SnackCustomService.show(this,mensaje,status);
    }

    private void getUserCreated(String driver)
    {
        try{
            if(!"-1".equals(driver))
            {
                Gson g = new Gson();
                Driver driverObj = g.fromJson(driver, Driver.class);
                userIdNew = driverObj.getIdUser();
                driverIdNew = driverObj.getIdDriver();
                saveFotoDelCoductor();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            progressDialogSave.dismiss();
            showPopupError(getString(R.string.error), getString(R.string.new_driver_error_user_get), TabCreateDriver.USER_DATA );
        }
    }



    private void setMsgToProgressLoading(String msg){
        progressDialogSave.setMessage(msg.concat("\n\n").concat(getString(R.string.new_driver_please_not_go_out)).concat(getActualStepAndTotal()));
    }

    private void saveFotoDelCoductor()
    {
        if(driverPhotos.bmpFotoDelConductor!=null){
            try{
                Log.d("upload_img","SUBIR FOTO DEL CONDUCTOR");

                class UploadImage extends AsyncTask<Bitmap, Void, String> {
                    RequestHandler rh = new RequestHandler();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        setMsgToProgressLoading(getString(R.string.new_driver_uploading_driver_photo));
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(TextUtils.isEmpty(s)){
                            listErrors.add(getString(R.string.new_driver_error_upload_user_photo));
                        }
                        saveFotoDeLicenciaDelConductor();
                    }

                    @Override
                    protected String doInBackground(Bitmap... params) {
                        String result ="";
                        try{
                            Bitmap imageScaled = Utils.getScaledBitmap(params[0], MAX_WIDTH,MAX_HEIGHT);
                            String uploadImage = Utils.getStringImage(imageScaled);

                            HashMap<String, String> data = new HashMap<>();
                            data.put(UPLOAD_DRIVER_PHOTO_KEY, uploadImage);
                            data.put("filename", String.valueOf(userIdNew));
                            data.put("doc", Globales.SaveImageKey.UPLOAD_FOTO_DE_PERFIL_KEY);


                            result = rh.sendPostRequest(UPLOAD_DRIVER_PHOTO_URL, data);
                        }
                        catch (Exception ex){
                            listErrors.add(getString(R.string.new_driver_error_upload_user_photo));
                            saveFotoDeLicenciaDelConductor();
                        }
                        return result;
                    }
                }

                UploadImage ui = new UploadImage();
                ui.execute(driverPhotos.bmpFotoDelConductor);
            }
            catch (Exception ex){
                ex.printStackTrace();
                listErrors.add(getString(R.string.new_driver_error_upload_user_photo));
                saveFotoDeLicenciaDelConductor();
            }
        }
        else{
            saveFotoDeLicenciaDelConductor();
        }
    }

    private void saveFotoDeLicenciaDelConductor(){
        if(driverPhotos.bmpLicenciaDelConductor!=null)
        {
            try{
                Log.d("upload_img","SUBIR FOTO DE LA LICENCIA DEL CONDUCTOR");

                class UploadImage extends AsyncTask<Bitmap, Void, String> {
                    RequestHandler rh = new RequestHandler();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        setMsgToProgressLoading(getString(R.string.new_driver_uploading_driver_licence));
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(TextUtils.isEmpty(s)){
                            listErrors.add(getString(R.string.new_driver_error_upload_user_licence_photo));
                        }
                        saveFotoDelVehiculo();
                    }

                    @Override
                    protected String doInBackground(Bitmap... params) {
                        String result ="";
                        try{
                            Bitmap imageScaled = Utils.getScaledBitmap(params[0], MAX_WIDTH,MAX_HEIGHT);
                            String uploadImage = Utils.getStringImage(imageScaled);

                            HashMap<String, String> data = new HashMap<>();
                            data.put(UPLOAD_DRIVER_PHOTO_KEY, uploadImage);
                            data.put("filename", String.valueOf(driverIdNew));
                            data.put("doc", Globales.SaveImageKey.UPLOAD_LICENCIA_DEL_CONDUCTOR_KEY);
                            result = rh.sendPostRequest(UPLOAD_DRIVER_PHOTO_URL, data);
                        }
                        catch (Exception ex){
                            listErrors.add(getString(R.string.new_driver_error_upload_user_licence_photo));
                            saveFotoDelVehiculo();
                        }
                        return result;
                    }
                }

                UploadImage ui = new UploadImage();
                ui.execute(driverPhotos.bmpLicenciaDelConductor);
            }
            catch (Exception ex){
                ex.printStackTrace();
                listErrors.add(getString(R.string.new_driver_error_upload_user_licence_photo));
                saveFotoDelVehiculo();
            }
        }
        else{
            saveFotoDelVehiculo();
        }
    }

    private void saveFotoDelVehiculo(){
        if(driverPhotos.bmpFotoVehiculo!=null)
        {
            try{
                Log.d("upload_img","SUBIR FOTO DEL VEHICULO");

                class UploadImage extends AsyncTask<Bitmap, Void, String> {
                    RequestHandler rh = new RequestHandler();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        setMsgToProgressLoading(getString(R.string.new_driver_uploading_vehicle_photo));
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(TextUtils.isEmpty(s)){
                            listErrors.add(getString(R.string.new_driver_error_upload_vehicle_photo));
                        }
                        saveFotoDeLaChapa();
                    }

                    @Override
                    protected String doInBackground(Bitmap... params) {
                        String result ="";
                        try{
                            Bitmap imageScaled = Utils.getScaledBitmap(params[0], MAX_WIDTH,MAX_HEIGHT);
                            String uploadImage = Utils.getStringImage(imageScaled);

                            HashMap<String, String> data = new HashMap<>();
                            data.put(UPLOAD_DRIVER_PHOTO_KEY, uploadImage);
                            data.put("filename", String.valueOf(driverIdNew));
                            data.put("doc", Globales.SaveImageKey.UPLOAD_FOTO_DEL_VEHICULO_KEY);
                            result = rh.sendPostRequest(UPLOAD_DRIVER_PHOTO_URL, data);
                        }
                        catch (Exception ex){
                            listErrors.add(getString(R.string.new_driver_error_upload_vehicle_photo));
                            saveFotoDeLaChapa();
                        }
                        return result;
                    }
                }

                UploadImage ui = new UploadImage();
                ui.execute(driverPhotos.bmpFotoVehiculo);
            }
            catch (Exception ex){
                ex.printStackTrace();
                listErrors.add(getString(R.string.new_driver_error_upload_vehicle_photo));
                saveFotoDeLaChapa();
            }
        }
        else{
            saveFotoDeLaChapa();
        }
    }

    private void saveFotoDeLaChapa(){
        if(driverPhotos.bmpFotoChapaVehiculo!=null)
        {
            try{
                Log.d("upload_img","SUBIR FOTO DE LA CHAPA");

                class UploadImage extends AsyncTask<Bitmap, Void, String> {
                    RequestHandler rh = new RequestHandler();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        setMsgToProgressLoading(getString(R.string.new_driver_uploading_chapa_photo));
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(TextUtils.isEmpty(s)){
                            listErrors.add(getString(R.string.new_driver_error_upload_chapa_photo));
                        }
                        saveFotoAntecedentesPoliciales();
                    }

                    @Override
                    protected String doInBackground(Bitmap... params) {
                        String result ="";
                        try{
                            Bitmap imageScaled = Utils.getScaledBitmap(params[0], MAX_WIDTH,MAX_HEIGHT);
                            String uploadImage = Utils.getStringImage(imageScaled);

                            HashMap<String, String> data = new HashMap<>();
                            data.put(UPLOAD_DRIVER_PHOTO_KEY, uploadImage);
                            data.put("filename", String.valueOf(driverIdNew));
                            data.put("doc", Globales.SaveImageKey.UPLOAD_CHAPA_DEL_VEHICULO_KEY);
                            result = rh.sendPostRequest(UPLOAD_DRIVER_PHOTO_URL, data);
                        }
                        catch (Exception ex){
                            listErrors.add(getString(R.string.new_driver_error_upload_chapa_photo));
                            saveFotoAntecedentesPoliciales();
                        }
                        return result;
                    }
                }

                UploadImage ui = new UploadImage();
                ui.execute(driverPhotos.bmpFotoChapaVehiculo);
            }
            catch (Exception ex){
                ex.printStackTrace();
                listErrors.add(getString(R.string.new_driver_error_upload_chapa_photo));
                saveFotoAntecedentesPoliciales();
            }
        }
        else{
            saveFotoAntecedentesPoliciales();
        }
    }

    private void saveFotoAntecedentesPoliciales(){
        if(driverPhotos.bmpFotoAntecedentesPoliciales!=null)
        {
            try{
                Log.d("upload_img","SUBIR FOTO DE ANTECEDENTES POLICIALES");

                class UploadImage extends AsyncTask<Bitmap, Void, String> {
                    RequestHandler rh = new RequestHandler();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        setMsgToProgressLoading(getString(R.string.new_driver_uploading_antecendetes_policiales_photo));
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(TextUtils.isEmpty(s)){
                            listErrors.add(getString(R.string.new_driver_error_upload_antecedentes_policiales_photo));
                        }
                        showUserCreatedOkWithMessageAndExit(getString(R.string.chofer_registrado));
                    }

                    @Override
                    protected String doInBackground(Bitmap... params) {
                        String result ="";
                        try{
                            Bitmap imageScaled = Utils.getScaledBitmap(params[0], MAX_WIDTH,MAX_HEIGHT);
                            String uploadImage = Utils.getStringImage(imageScaled);

                            HashMap<String, String> data = new HashMap<>();
                            data.put(UPLOAD_DRIVER_PHOTO_KEY, uploadImage);
                            data.put("filename", String.valueOf(driverIdNew));
                            data.put("doc", Globales.SaveImageKey.UPLOAD_ANTECEDENTES_POLICIALES_KEY);
                            result = rh.sendPostRequest(UPLOAD_DRIVER_PHOTO_URL, data);
                        }
                        catch (Exception ex){
                            listErrors.add(getString(R.string.new_driver_error_upload_antecedentes_policiales_photo));
                            showUserCreatedOkWithMessageAndExit(getString(R.string.chofer_registrado));
                        }
                        return result;
                    }
                }

                UploadImage ui = new UploadImage();
                ui.execute(driverPhotos.bmpFotoAntecedentesPoliciales);
            }
            catch (Exception ex){
                ex.printStackTrace();
                listErrors.add(getString(R.string.new_driver_error_upload_antecedentes_policiales_photo));
                showUserCreatedOkWithMessageAndExit(getString(R.string.chofer_registrado));
            }
        }
        else{
            showUserCreatedOkWithMessageAndExit(getString(R.string.chofer_registrado));
        }
    }
    private void showPopupError(String title, String msg, final TabCreateDriver tab){
        switch (tab)
        {
            case USER_DATA:
                tabs.getTabAt(0).select();
                showIconPersonalData(false);
                showIconCarData(false);
                break;
            case VEHICLE_DATA:
                tabs.getTabAt(1).select();
                showIconPersonalData(true);
                showIconCarData(false);
                break;
            case PHOTO_DATA:
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .setIcon(R.drawable.ic_warning_orange)
                .show();
    }

    private void showUserCreatedOkWithMessageAndExit(String title)
    {
        progressDialogSave.dismiss();
        String msg="";
        int drawableIcon = R.drawable.ic_warning_orange;
        if(listErrors!=null && listErrors.size()>0)
        {
            msg = msg.concat(getString(R.string.new_driver_error_start_msg)).concat("\n");
        }
        for (String str : listErrors) {
          msg = msg.concat(str.concat("\n"));
        }

        if(TextUtils.isEmpty(msg)){

            msg = TextUtils.isEmpty(socialid)
                    ? getString(R.string.new_driver_created_validate_mail)
                    : getString(R.string.new_driver_created_with_social_id);
            drawableIcon = R.drawable.ic_check_green;
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                })
                .setIcon(drawableIcon)
                .setCancelable(false)
                .show();
    }

    private void finishActivity(){
        Intent data = new Intent();
        String text = "OK";
        data.setData(Uri.parse(text));
        setResult(RESULT_OK, data);
        finish();
    }

       enum TabCreateDriver{
        USER_DATA, VEHICLE_DATA, PHOTO_DATA
    }
}