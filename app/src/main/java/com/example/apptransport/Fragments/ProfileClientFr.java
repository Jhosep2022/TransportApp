package com.example.apptransport.Fragments;

import android.annotation.SuppressLint;

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


import com.apreciasoft.mobile.asremis.Activity.HomeClienteNewStyle;
import com.apreciasoft.mobile.asremis.Entity.RequetClient;
import com.apreciasoft.mobile.asremis.Entity.Client;
import com.apreciasoft.mobile.asremis.Entity.ClienteFull;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
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

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usario on 29/4/2017.
 */
@SuppressLint("LongLogTag")
public class ProfileClientFr extends Fragment {

    public Bitmap bitmapImage = null;
    public  Bitmap bitmap = null;
    public GlovalVar gloval;
    private View myView;
    public static final String UPLOAD_URL =  HttpConexion.BASE_URL+HttpConexion.base+"/Frond/safeimgDriver.php";
    public static final String UPLOAD_KEY = "image";
    ServicesLoguin daoAuth = null;
    public ProgressDialog loading;
    public EditText editTextNombre;
    public EditText editTextDni;
    public EditText editTextEmail;
    public EditText editTextTelefono;
    public EditText editextApellido;
    public boolean changePick = false;
    private Button buttonInformacion;
    private ImageButton buttonFoto;
    private ImageView  imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile_company, container, false);

        buttonFoto =  myView.findViewById(R.id.btnSafePick);
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
        gloval = ((GlovalVar)getActivity().getApplicationContext());

        editTextNombre =  myView.findViewById(R.id.txt_name);
        editTextDni =  myView.findViewById(R.id.txt_dni);
        editTextEmail =  myView.findViewById(R.id.txt_mail);
        editTextTelefono =  myView.findViewById(R.id.txt_phone);
        editextApellido =  myView.findViewById(R.id.txt_last_name);

        imageView=  myView.findViewById(R.id.imgView);

        ImageView img = myView.findViewById(R.id.imgView);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGallery();
            }
        });

        getInfoClient();
        return myView;
    }



    //region DATOS
    private   void    getInfoClient() {

        if (this.daoAuth == null) { this.daoAuth = HttpConexion.getUri().create(ServicesLoguin.class); }

        daoAuth.findWithDiscriminate(
                Utils.isClienteParticular(gloval.getGv_id_profile()) ? gloval.getGv_id_cliet(): gloval.getGv_user_id(),
                gloval.getGv_id_profile()).enqueue(new Callback<RequetClient>() {
      //  daoAuth.find(gloval.getGv_user_id()).enqueue(new Callback<RequetClient>() {
            @Override
            public void onResponse(Call<RequetClient> call, Response<RequetClient> response) {
                try {
                    RequetClient rs =  response.body();
                    if(rs!=null)
                    {
                        if(Utils.isClienteParticular(gloval.getGv_id_profile()))
                        {
                            editTextNombre.setText(rs.getClient().getFirtNameClient());
                            editTextDni.setText(rs.getClient().getDniClient());
                            editTextEmail.setText(rs.getClient().getMailClient());
                            editTextTelefono.setText(rs.getClient().getPhoneClient());
                            editextApellido.setText(rs.getClient().getLastNameClient());

                        }
                        else{
                            editTextNombre.setText(rs.getClient().getFirtNameClient());
                            editTextDni.setText(rs.getClient().getDniClient());
                            editTextEmail.setText(rs.getClient().getMailClient());
                            editTextTelefono.setText(rs.getClient().getPhoneClient());
                            editextApellido.setText(rs.getClient().getLastNameClient());
                        }

                    }
                    else{
                        showMessage(getString(R.string.no_hay_resultado),3);
                    }

                }catch (Exception e){
                    Log.e("ERROR OBTNER INFO",e.toString());
                    showMessage(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<RequetClient> call, Throwable t) {
                Log.e("ONFAILUER OBTNER INFO",t.toString());
                showMessage(getString(R.string.fallo_general),3);

            }
        });
        descargarImagen();
    }


    private void validateAndSaveClient(){
        if (editTextNombre.getText().toString().isEmpty()){
            editTextNombre.setError(getString(R.string.completar_campo));
            editTextNombre.requestFocus();
        }
        else if(!Utils.validateName(editTextNombre.getText().toString()))
        {
            editTextNombre.setError(getString(R.string.agregar_nombre_valido));
            editTextNombre.requestFocus();
        }
        else if (editextApellido.getText().toString().isEmpty()){
            editextApellido.setError(getString(R.string.completar_campo));
            editextApellido.requestFocus();
        }
        else if(!Utils.validateName(editextApellido.getText().toString()))
        {
            editextApellido.setError(getString(R.string.agregar_apellido_valido));
            editextApellido.requestFocus();
        }
        else if (editTextDni.getText().toString().isEmpty()){
            editTextDni.setError(getString(R.string.completar_campo));
            editTextDni.requestFocus();
        }
        else if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!Utils.validationEmail(editTextEmail.getText().toString())){
            showMessage(getString(R.string.email_invalido),0);
        }else if (editTextTelefono.getText().toString().isEmpty()){
            editTextTelefono.setError(getString(R.string.completar_campo));
            editTextTelefono.requestFocus();
        }else {
            saveInfoCliente();
        }
    }



    public void saveInfoCliente() {
        if (this.daoAuth == null) { this.daoAuth = HttpConexion.getUri().create(ServicesLoguin.class); }

        final Client cliente =  new Client(gloval.getGv_clientinfo().getIdClient(),
                editTextNombre.getText().toString(),
                editextApellido.getText().toString(),
                editTextDni.getText().toString(),
                editTextTelefono.getText().toString(),
                editTextEmail.getText().toString(),
                gloval.getGv_user_id(),
                gloval.getGv_id_profile());


        loading = ProgressDialog.show(getActivity(), getString(R.string.actualizando), getString(R.string.espere), true, false);

        ClienteFull clienteFull =  new ClienteFull();
        clienteFull.setClient(cliente);

        if(Utils.isClienteParticular(gloval.getGv_id_profile()))
        {
            daoAuth.updateClientLiteMobilParticular(clienteFull).enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    try {
                        Client rs = response.body();
                        gloval.setGv_clientinfo(cliente);

                        showMessage(getString(R.string.informacion_actualizada),1);
                    }catch (Exception e){
                        Log.e("ERROR ACTUALIZAR INFO",e.toString());
                        showMessage(getString(R.string.fallo_general),3);
                    }
                    loading.dismiss();
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Log.e("ONFAILUER ACTUALIZAR INFO",t.toString());
                    showMessage(getString(R.string.fallo_general),3);
                    loading.dismiss();
                }
            });
        }
        else{
            daoAuth.updateClientLiteMobil(clienteFull).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    try {
                        Boolean rs = response.body();
                        gloval.setGv_clientinfo(cliente);

                        showMessage(getString(R.string.informacion_actualizada),1);
                    }catch (Exception e){
                        Log.e("ERROR ACTUALIZAR INFO",e.toString());
                        showMessage(getString(R.string.fallo_general),3);
                    }
                    loading.dismiss();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("ONFAILUER ACTUALIZAR INFO",t.toString());
                    showMessage(getString(R.string.fallo_general),3);
                    loading.dismiss();
                }
            });
        }


    }

    //endregion

    //region FOTO

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
                try{
                    String msg = TextUtils.isEmpty(s)? getString(R.string.foto_error_actualizar): getString(R.string.foto_actualizada);
                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    ((HomeClienteNewStyle)getActivity()).updateUserImage();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap imageScaled = Utils.getScaledBitmap(bitmapImage, 500,500);
                String uploadImage = Utils.getStringImage(imageScaled);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("filename", String.valueOf(gloval.getGv_user_id()));
                data.put("doc", Globales.SaveImageKey.UPLOAD_FOTO_DE_PERFIL_KEY);

                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }


        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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
    //endregion

    private void showMessage(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

}
