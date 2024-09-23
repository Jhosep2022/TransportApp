package com.example.apptransport.Fragments;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Activity.HomeChofer;
import com.apreciasoft.mobile.asremis.Entity.DriverFull;
import com.apreciasoft.mobile.asremis.Entity.Driver;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.RequestHandler;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usario on 27/4/2017.
 */

@SuppressLint("LongLogTag")
public class ProfileDriverFr extends Fragment {

    public  Bitmap bitmapImage = null;
    public  Bitmap bitmap = null;
    public GlovalVar gloval;
    private View myView;
    public static final String UPLOAD_URL =  HttpConexion.BASE_URL+HttpConexion.base+"/Frond/safeimgDriver.php";
    public static final String UPLOAD_KEY = "image";
    ServicesDriver daoDriver = null;
    public ProgressDialog loading;
    public boolean changePick = false;
    public EditText editTextNombre;
    public EditText editTextDni;
    public EditText editTextEmail;
    public EditText editTextTelefono;
    private Button buttonInformacion;
    private ImageButton buttonFoto;
    private ImageView imageView;

    //todo. eliminar
    private Bitmap imagenResizeada;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile_driver, container, false);

        imageView = myView.findViewById(R.id.imgView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGallery();
            }
        });

        buttonFoto = myView.findViewById(R.id.btnSafePick);
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePick();

            }
        });

        buttonInformacion = myView.findViewById(R.id.safe_info);
        buttonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveClient();
            }
        });

        // variable global //
        gloval = ((GlovalVar)getActivity().getApplicationContext());

        editTextNombre =  myView.findViewById(R.id.txt_name);
        editTextDni =  myView.findViewById(R.id.txt_dni);
        editTextEmail =  myView.findViewById(R.id.txt_mail);
        editTextTelefono =  myView.findViewById(R.id.txt_phone);

        getInfoDriver();
        return myView;
    }



    //region DATOS
    public  void    getInfoDriver() {

        if (this.daoDriver == null) { this.daoDriver = HttpConexion.getUri().create(ServicesDriver.class); }

        daoDriver.find(gloval.getGv_id_driver()).enqueue(new Callback<DriverFull>() {
            @Override
            public void onResponse(Call<DriverFull> call, Response<DriverFull> response) {
                try {
                    DriverFull rs = response.body();
                    editTextNombre.setText(rs.getDriver().getFisrtNameDriver());
                    editTextDni.setText(rs.getDriver().getDniDriver());
                    editTextEmail.setText(rs.getDriver().getEmailDriver());
                    editTextTelefono.setText(rs.getDriver().getPhoneNumberDriver());
                }catch (Exception e){
                    showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<DriverFull> call, Throwable t) {
                showMessage(getString(R.string.fallo_general), Globales.StatusToast.WARNING);
            }
        });
        descargarImagen();
    }

    private void validateAndSaveClient(){
        if (editTextNombre.getText().toString().isEmpty()){
            editTextNombre.setError(getString(R.string.completar_campo));
            editTextNombre.requestFocus();
        }else if (editTextDni.getText().toString().isEmpty()){
            editTextDni.setError(getString(R.string.completar_campo));
            editTextDni.requestFocus();
        }else if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!Utils.validationEmail(editTextEmail.getText().toString())){
            showMessage(getString(R.string.email_invalido),Globales.StatusToast.INFO);
        }else if (editTextTelefono.getText().toString().isEmpty()){
            editTextTelefono.setError(getString(R.string.completar_campo));
            editTextTelefono.requestFocus();
        }else {
            saveInfoChofer();
        }
    }

    public void saveInfoChofer() {
        if (this.daoDriver == null) { this.daoDriver = HttpConexion.getUri().create(ServicesDriver.class); }

        Driver dr =  new Driver(gloval.getGv_id_driver(),
                editTextNombre.getText().toString(),
                editTextDni.getText().toString(),
                editTextTelefono.getText().toString(),
                editTextEmail.getText().toString(),
                gloval.getGv_user_id());

        buttonInformacion.setEnabled(false);
        buttonInformacion.setText(getString(R.string.cargando_informacion));


        daoDriver.updateLiteMobil(dr).enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                buttonInformacion.setEnabled(true);
                buttonInformacion.setText(getString(R.string.actualizar_datos));
                try {
                    Driver rs = response.body();
                    editTextNombre.setText(rs.getFisrtNameDriver());
                    editTextDni.setText(rs.getDniDriver());
                    editTextEmail.setText(rs.getEmailDriver());
                    editTextTelefono.setText(rs.getPhoneNumberDriver());
                    showMessage(getString(R.string.informacion_actualizada),Globales.StatusToast.SUCCESS);
                }catch (Exception e){
                    Log.e("ERROR DATOS ONFAILURE",e.toString());
                    showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                buttonInformacion.setEnabled(true);
                buttonInformacion.setText(getString(R.string.actualizar_datos));
                Log.e("ACTULIZAR DATOS ONFAILURE",t.toString());
                showMessage(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });
    }

    //endregion


    // METODO PARA ACTUALIZAR LA FOTO //
    public void savePick()
    {
        if(changePick == true)
        {
            uploadImage();
        }


    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
            changePick = true;
        }
    }

    /* RESPUESTA GALERIA */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    Uri selectedImage = data.getData();
                    Glide.with(getActivity())
                            .load(selectedImage)
                            .centerCrop()
                            .placeholder(R.drawable.ic_user)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* SERVICIO QUE GUARDA LA FOTO */
    private void uploadImage()
    {
        Log.d("upload img","up img");



        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), getString(R.string.actualizando_foto), getString(R.string.espere_unos_segundos), true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Log.d("error",s);
                String msg = TextUtils.isEmpty(s)? getString(R.string.foto_error_actualizar): getString(R.string.foto_actualizada);
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                //ponerImagenResizeada(imagenResizeada);
                ((HomeChofer)getActivity()).updateUserImage();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap imageScaled = Utils.getScaledBitmap(bitmapImage, 500,500);
                String uploadImage = Utils.getStringImage(imageScaled);
                //imagenResizeada = imageScaled;

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("filename", String.valueOf(gloval.getGv_user_id()));
                data.put("doc", Globales.SaveImageKey.UPLOAD_FOTO_DE_PERFIL_KEY);

                String result = rh.sendPostRequest(UPLOAD_URL, data);
                //String result="casa";
                return result;
            }
        }


        if(bitmapImage!=null) {
            UploadImage ui = new UploadImage();
            ui.execute(bitmapImage);
        }

    }

    private void ponerImagenResizeada(Bitmap imageScaled){
        ImageView imageView =  myView.findViewById(R.id.imgView);

        Glide.with(imageView.getContext())
                .load(imageScaled)
                .placeholder(R.drawable.ic_update)
                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.ic_user)
                //.centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }


    private void descargarImagen(){
        String url = Utils.getUrlImageUser(gloval.getGv_user_id());

        ImageView imageView =  myView.findViewById(R.id.imgView);

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

    /*FIN SECCION FOTO*/

    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }


}
