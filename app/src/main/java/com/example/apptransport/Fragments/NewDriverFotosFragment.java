package com.example.apptransport.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.NewDriverOptionalValueData;
import com.apreciasoft.mobile.asremis.Entity.NewDriverPhotos;
import com.apreciasoft.mobile.asremis.Entity.TermAndConditionResult;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.viewmodels.NewDriverActivityViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 * to handle interaction events.
 * Use the {@link NewDriverFotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDriverFotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_GALLERY_FOTO_CONDUCTOR=1000;
    private static final int REQUEST_GALLERY_LICENCIA_CONDUCTOR=1001;
    private static final int REQUEST_GALLERY_FOTO_VEHICULO=1002;
    private static final int REQUEST_GALLERY_CHAPA_VEHICULO=1003;
    private static final int REQUEST_GALLERY_ANTECEDENTES_POLICIALES=1004;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ListenerRegisterChofer listener;
    ServicesDriver apiDriver = null;
    private Button btnPrevious;
    private Button btnRegister;
    private AppCompatImageView imgFotoConductorDymmy, imgFotoConductor, imgDeleteFotoConductor;
    private AppCompatImageView imgLicenciaConductorDymmy, imgLicenciaConductor, imgDeleteLicenciaConductor;
    private AppCompatImageView imgFotoVehiculoDymmy, imgFotoVehiculo, imgDeleteFotoVehiculo;
    private AppCompatImageView imgFotoChapaVehiculoDummy, imgFotoChapaVehiculo, imgDeleteFotoChapaVehiculo;
    private AppCompatImageView imgFotoAntecedentesPolicialesDummy, imgFotoAntecedentesPoliciales, imgDeleteAntecedentesPoliciales;

    private TextView txtFotoConductorSubtitle, txtLicenciaConductorSubtitle, txtFotoVehiculoSubtitle, txtFotoChapaVehiculoSubtitle,txtFotoAntecedentesPolicialesSubtitle;

    private boolean isFotoDelConductorChanged = false;

    private boolean isLicenciaDelConductorChanged = false;
    private boolean isFotoVehiculoChanged = false;
    private boolean isFotoChapaVehiculoChanged = false;
    private boolean isFotoAntecedentesPoliciales = false;

    private Bitmap bmpFotoDelConductor = null;
    private Bitmap bmpLicenciaDelConductor = null;
    private Bitmap bmpFotoVehiculo = null;
    private Bitmap bmpFotoChapaVehiculo = null;
    private Bitmap bmpFotoAntecedentesPoliciales= null;

    private boolean isPhotosRequired;
    private boolean isTerminosYCondicionesRequired;

    private TextView txtTermsAndConditions;
    private CheckBox chkTermsAndConditions;
    private View layoutTermsAndConditions;
    private String termsAndConditions="";

    private NewDriverActivityViewModel viewModel;
    private NewDriverOptionalValueData optionalDriverData=null;

    public NewDriverFotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewDriverFotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDriverFotosFragment newInstance(String param1, String param2) {
        NewDriverFotosFragment fragment = new NewDriverFotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_new_driver_fotos, container, false);
        apiDriver = HttpConexion.getUri().create(ServicesDriver.class);
        configureControls(view);
        fillTermsAndConditions();
        initializeViewModel();
        initializeObserverParams();
        return view;
    }

    /* RESPUESTA GALERIA */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try{
            if (resultCode == getActivity().RESULT_OK) {
                if (requestCode == REQUEST_GALLERY_FOTO_CONDUCTOR) {
                    cargarFotoConductor(data.getData());
                }else if(requestCode == REQUEST_GALLERY_LICENCIA_CONDUCTOR){
                    cargarLicenciaConductor(data.getData());
                }else if(requestCode==REQUEST_GALLERY_FOTO_VEHICULO){
                    cargarFotoVehiculo(data.getData());
                }else if(requestCode==REQUEST_GALLERY_CHAPA_VEHICULO){
                    cargarFotoChapaVehiculo(data.getData());
                }else if(requestCode==REQUEST_GALLERY_ANTECEDENTES_POLICIALES){
                    cargarFotoAntecedentesPoliciales(data.getData());
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void configureControls(View view) {
        configureControlsFotoConductor(view);
        configureControlsLicenciaConductor(view);
        configureControlsFotoVehiculo(view);
        configureControlsChapaVehiculo(view);
        configureControlsAntecedentesPoliciales(view);
        configurePhotoRequired(view);
        setConfigurationsFromParams(view);
        btnPrevious = view.findViewById(R.id.btn_back);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnAtras(2);
            }
        });

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterOnClick();
            }
        });

        txtTermsAndConditions = view.findViewById(R.id.txtTermsAndConditions);
        chkTermsAndConditions = view.findViewById(R.id.chkTermsAndConditions);
        layoutTermsAndConditions = view.findViewById(R.id.layoutTermsAndConditions);
        setVisibilityTermsAndConditions();
        txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsAndConditions();
            }
        });
    }

    private void showTermsAndConditions()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(termsAndConditions);
        dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    private void configurePhotoRequired(View view){
        txtFotoConductorSubtitle = view.findViewById(R.id.txtFotoConductorSubtitle);
        txtLicenciaConductorSubtitle = view.findViewById(R.id.txtLicenciaConductorSubtitle);
        txtFotoVehiculoSubtitle = view.findViewById(R.id.txtFotoVehiculoSubtitle);
        txtFotoChapaVehiculoSubtitle = view.findViewById(R.id.txtFotoChapaVehiculoSubtitle);
        txtFotoAntecedentesPolicialesSubtitle = view.findViewById(R.id.txtFotoAntecedentesPolicialesSubtitle);
    }

    private void setConfigurationsFromParams(View view) {
        try
        {
            isPhotosRequired = false;
            enableRequiredPhotoText(false);
            List<paramEntity> params = Utils.getParamsFromLocalPreferences(view.getContext());
            if (params != null) {

                paramEntity param99_subeFotos = params.get(Globales.ParametrosDeApp.PARAM_99_REGISTRAR_CHOFER_OBLIGATORIO_SUBIR_FOTOS);
                if ("1".equals(param99_subeFotos.getValue())) {
                    enableRequiredPhotoText(true);
                    isPhotosRequired = true;
                }

                paramEntity param114_habilita_terminos = params.get(Globales.ParametrosDeApp.PARAM_114_HABILITA_TERMINOS_Y_CONDICIONES);
                isTerminosYCondicionesRequired = "2".equals(param114_habilita_terminos.getValue()) ||
                        "3".equals(param114_habilita_terminos.getValue());

            }
        }
        catch (Exception ex){
         ex.printStackTrace();
        }
    }

    private void enableRequiredPhotoText(boolean required){
        txtFotoConductorSubtitle.setVisibility(required?View.VISIBLE:View.GONE);
        txtLicenciaConductorSubtitle.setVisibility(required?View.VISIBLE:View.GONE);
        txtFotoVehiculoSubtitle.setVisibility(required?View.VISIBLE:View.GONE);
        txtFotoChapaVehiculoSubtitle.setVisibility(required?View.VISIBLE:View.GONE);
        txtFotoAntecedentesPolicialesSubtitle.setVisibility(required?View.VISIBLE:View.GONE);
    }

    private void configureControlsFotoConductor(View view)
    {
        imgFotoConductor = view.findViewById(R.id.imgFotoConductor);
        imgFotoConductorDymmy = view.findViewById(R.id.imgFotoConductorDummy);
        imgDeleteFotoConductor = view.findViewById(R.id.imgDeleteFotoConductor);

        imgFotoConductorDymmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_FOTO_CONDUCTOR);
            }
        });

        imgDeleteFotoConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoConductor(false);
            }
        });
    }

    private void configureControlsLicenciaConductor(View view)
    {
        imgLicenciaConductor = view.findViewById(R.id.imgLicenciaConductor);
        imgLicenciaConductorDymmy = view.findViewById(R.id.imgLicenciaConductorDummy);
        imgDeleteLicenciaConductor = view.findViewById(R.id.imgDeleteLicenciaConductor);

        imgLicenciaConductorDymmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_LICENCIA_CONDUCTOR);
            }
        });

        imgDeleteLicenciaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgLicenciaConductor(false);
            }
        });
    }

    private void configureControlsFotoVehiculo(View view)
    {
        imgFotoVehiculo = view.findViewById(R.id.imgFotoVehiculo);
        imgFotoVehiculoDymmy = view.findViewById(R.id.imgFotoVehiculoDummy);
        imgDeleteFotoVehiculo = view.findViewById(R.id.imgDeleteFotoVehiculo);

        imgFotoVehiculoDymmy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_FOTO_VEHICULO);
            }
        });

        imgDeleteFotoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoVehiculo(false);
            }
        });
    }
    private void configureControlsChapaVehiculo(View view)
    {
        imgFotoChapaVehiculo = view.findViewById(R.id.imgFotoChapaVehiculo);
        imgFotoChapaVehiculoDummy = view.findViewById(R.id.imgFotoChapaVehiculoDummy);
        imgDeleteFotoChapaVehiculo = view.findViewById(R.id.imgDeleteFotoChapaVehiculo);

        imgFotoChapaVehiculoDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_CHAPA_VEHICULO);
            }
        });

        imgDeleteFotoChapaVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoChapaVehiculo(false);
            }
        });
    }

    private void configureControlsAntecedentesPoliciales(View view)
    {
        imgFotoAntecedentesPoliciales = view.findViewById(R.id.imgFotoAntecedentesPoliciales);
        imgFotoAntecedentesPolicialesDummy = view.findViewById(R.id.imgFotoAntecedentesPolicialesDummy);
        imgDeleteAntecedentesPoliciales = view.findViewById(R.id.imgDeleteAntecedentesPoliciales);

        imgFotoAntecedentesPolicialesDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_ANTECEDENTES_POLICIALES);
            }
        });

        imgDeleteAntecedentesPoliciales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoAntecedentesPoliciales(false);
            }
        });
    }


    public void setListenerRegisterChofer(ListenerRegisterChofer listener)
    {
        this.listener = listener;
    }

    private void startGallery(int requestCode){
        try
        {
            Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, requestCode);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    private void cargarFotoConductor(Uri returnUri)
    {
        try {
            bmpFotoDelConductor = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_user)
                    .into(imgFotoConductor);
            mostrarImgFotoConductor(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoConductor(false);
        }
    }

    private void cargarLicenciaConductor(Uri returnUri)
    {
        try {
            bmpLicenciaDelConductor = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_licencia_de_conducir)
                    .into(imgLicenciaConductor);
            mostrarImgLicenciaConductor(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgLicenciaConductor(false);
        }
    }

    private void cargarFotoVehiculo(Uri returnUri)
    {
        try {
            bmpFotoVehiculo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_car)
                    .into(imgFotoVehiculo);
            mostrarImgFotoVehiculo(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoVehiculo(false);
        }
    }

    private void cargarFotoChapaVehiculo(Uri returnUri)
    {
        try {
            //Bitmap bmpFotoChapaVehiculoTemp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            //bmpFotoChapaVehiculo = Utils.getScaledBitmap(bmpFotoChapaVehiculoTemp, 600,600);
            //bmpFotoChapaVehiculoTemp=null;
            bmpFotoChapaVehiculo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);

            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_patente)
                    .into(imgFotoChapaVehiculo);
            mostrarImgFotoChapaVehiculo(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoChapaVehiculo(false);
        }
    }

    private void cargarFotoAntecedentesPoliciales(Uri returnUri)
    {
        try {
            bmpFotoAntecedentesPoliciales = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoAntecedentesPoliciales);
            mostrarImgFotoAntecedentesPoliciales(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoAntecedentesPoliciales(false);
        }
    }


    private void mostrarImgFotoConductor(boolean mostrar)
    {
        bmpFotoDelConductor = mostrar ? bmpFotoDelConductor : null;
        isFotoDelConductorChanged = mostrar;
        imgFotoConductor.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoConductorDymmy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoConductor.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgLicenciaConductor(boolean mostrar)
    {
        bmpLicenciaDelConductor = mostrar ? bmpLicenciaDelConductor : null;
        isLicenciaDelConductorChanged = mostrar;
        imgLicenciaConductor.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgLicenciaConductorDymmy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteLicenciaConductor.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoVehiculo(boolean mostrar)
    {
        bmpFotoVehiculo = mostrar ? bmpFotoVehiculo : null;
        isFotoVehiculoChanged = mostrar;
        imgFotoVehiculo.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoVehiculoDymmy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoVehiculo.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoChapaVehiculo(boolean mostrar)
    {
        bmpFotoChapaVehiculo = mostrar ? bmpFotoChapaVehiculo : null;
        isFotoChapaVehiculoChanged = mostrar;
        imgFotoChapaVehiculo.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoChapaVehiculoDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoChapaVehiculo.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoAntecedentesPoliciales(boolean mostrar)
    {
        bmpFotoAntecedentesPoliciales =  mostrar ? bmpFotoAntecedentesPoliciales : null;
        isFotoAntecedentesPoliciales = mostrar;
        imgFotoAntecedentesPoliciales.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoAntecedentesPolicialesDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteAntecedentesPoliciales.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void btnRegisterOnClick() {
        Boolean isCheckedTerms = chkTermsAndConditions.isChecked();
        if (isTerminosYCondicionesRequired && !TextUtils.isEmpty(termsAndConditions) && !isCheckedTerms) {
            showSnack("Debe aceptar los términos y condiciones", Globales.StatusToast.FAIL);
        } else if (validatePhotos()) {
            NewDriverPhotos photos = new NewDriverPhotos(bmpFotoDelConductor, bmpLicenciaDelConductor, bmpFotoVehiculo, bmpFotoChapaVehiculo, bmpFotoAntecedentesPoliciales, chkTermsAndConditions.isChecked()?"1":"0");
            listener.btnRegisterDriver(photos);
        } else {
            showSnack(getString(R.string.new_driver_photo_required), Globales.StatusToast.WARNING);
        }
    }

    private boolean validatePhotos() {
        boolean result = false;
        if(isPhotosRequired){
            if(bmpFotoDelConductor!=null
                && bmpLicenciaDelConductor!=null
                && bmpFotoVehiculo!=null
                && bmpFotoChapaVehiculo!=null
                && bmpFotoAntecedentesPoliciales!=null)
            {
                result=true;
            }
        }
        else{
            result=true;
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showSnack(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

    private void fillTermsAndConditions() {
        apiDriver.getTermsAndConditions(2).enqueue(new Callback<TermAndConditionResult>() { //2: Chofer
            @Override
            public void onResponse(Call<TermAndConditionResult> call, Response<TermAndConditionResult> response) {
                try {
                    //: {"idDocument":"1","documentTitle":"Terminos y condiciones de cliente","documentText":""}
                    if (response.code() == 200) {
                        termsAndConditions = response.body().documentText;
                        setVisibilityTermsAndConditions();
                    }  else if (response.code() == 404) {
                        showSnack(getString(R.string.cliente_ya_registrado), 2);
                        setVisibilityTermsAndConditions();

                    }
                }catch (Exception e){
                    Log.e("CREAR CHOFER EXCEPTION", e.toString());
                    setVisibilityTermsAndConditions();
                }
            }

            @Override
            public void onFailure(Call<TermAndConditionResult> call, Throwable t) {
                Log.e("CRAR CLIENTE FAILURE", t.toString());
                setVisibilityTermsAndConditions();
            }
        });
    }

    private void initializeViewModel(){
        if(getActivity()!=null) {
            viewModel = new ViewModelProvider(getActivity()).get(NewDriverActivityViewModel.class);
        }
    }

    private void initializeObserverParams()
    {
        if(getActivity()!=null)
        {
            LiveData<List<paramEntity>> liveDataParams = viewModel.listParams();
            liveDataParams.observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<paramEntity>>() {
                @Override
                public void onChanged(List<paramEntity> params) {
                    try{
                        if(params!=null)
                        {
                            paramEntity param94_camposAdicionales = params.get(Globales.ParametrosDeApp.PARAM_94_REGISTRAR_CHOFER_CAMPOS_OPCIONALES);
                            Gson GSON = new GsonBuilder().create();
                            optionalDriverData = GSON.fromJson(param94_camposAdicionales.value, NewDriverOptionalValueData.class);
                            updateLabelsIfNecessary();
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    private void updateLabelsIfNecessary() {
        if(optionalDriverData!=null) {

            //Foto del conductor
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPhoto())) {
                txtFotoConductorSubtitle.setHint(optionalDriverData.getChofer().getNameTagPhoto());
            }

            //Licencia del conductor
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagLicense())) {
                txtLicenciaConductorSubtitle.setHint(optionalDriverData.getChofer().getNameTagLicense());
            }

            //Foto del Vehiculo
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagVehicle())) {
                txtFotoVehiculoSubtitle.setHint(optionalDriverData.getChofer().getNameTagVehicle());
            }

            //Foto de la chapa del vehiculo
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPlate())) {
                txtFotoChapaVehiculoSubtitle.setHint(optionalDriverData.getChofer().getNameTagPlate());
            }

            //Foto de antecendentes policiales
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPoliceRecord())) {
                txtFotoAntecedentesPolicialesSubtitle.setHint(optionalDriverData.getChofer().getNameTagPoliceRecord());
            }
        }
    }

    private void setVisibilityTermsAndConditions()
    {
        layoutTermsAndConditions.setVisibility(isTerminosYCondicionesRequired ? View.VISIBLE: View.GONE);
    }

}
