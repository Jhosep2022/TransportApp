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

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 *
 * to handle interaction events.
 * Use the {@link NewDriverFotosExtendedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDriverFotosExtendedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_GALLERY_FOTO_CONDUCTOR=1000;
    private static final int REQUEST_GALLERY_LICENCIA_CONDUCTOR=1001;
    private static final int REQUEST_GALLERY_FOTO_VEHICULO=1002;
    private static final int REQUEST_GALLERY_CHAPA_VEHICULO=1003;
    private static final int REQUEST_GALLERY_ANTECEDENTES_POLICIALES=1004;

    private static final int REQUEST_GALLERY_FOTO_VEHICULO_FRENTE=1005;
    private static final int REQUEST_GALLERY_INE_O_IFE=1006;
    private static final int REQUEST_GALLERY_FISCAL_FRONTAL=1007;
    private static final int REQUEST_GALLERY_FISCAL_TRASERA=1008;
    private static final int REQUEST_GALLERY_VIN_DEL_VEHICULO=1009;
    private static final int REQUEST_GALLERY_ESTADO_DE_CUENTA=1010;


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

    private AppCompatImageView imgFotoVehiculoFrenteDummy, imgFotoVehiculoFrente, imgDeleteVehiculoFrente;
    private AppCompatImageView imgFotoINEyoIFEDummy, imgFotoINEyoIFE, imgDeleteFotoINEyoIFE;
    private AppCompatImageView imgFotoConstanciaFiscalFrontalDummy, imgFotoConstanciaFiscalFrontal, imgDeleteFotoConstanciaFiscalFrontal;
    private AppCompatImageView imgFotoConstanciaFiscalTraseraDummy, imgFotoConstanciaFiscalTrasera, imgDeleteFotoConstanciaFiscalTrasera;
    private AppCompatImageView imgFotoVinVehiculoDummy, imgFotoVinVehiculo, imgDeleteFotoVinVehiculo;
    private AppCompatImageView imgFotoEstadoDeCuentaDummy, imgFotoEstadoDeCuenta, imgDeleteFotoEstadoDeCuenta;

    private View cardFotoFotoDelConductor, cardFotoVehiculo, cardFotoVehiculoFrente,cardFotoChapa, cardFotoLicenciaDeConducir,cardFotoINEIFE, cardFotoConstanciaFiscalFrontal, cardFotoConstanciaFiscalTrasera, cardFotoVinDelVehiculo,cardFotoEstadoDeCuenta,cardFotoAntecedentesPoliciales;

    private TextView txtFotoConductorSubtitle, txtLicenciaConductorSubtitle, txtFotoVehiculoSubtitle, txtFotoChapaVehiculoSubtitle,txtFotoAntecedentesPolicialesSubtitle;

    private TextView txtFotoVehiculoFrenteSubtitle, txtFotoINEyoIFESubtitle, txtFotoConstanciaFiscalFrontalSubtitle, txtFotoConstanciaFiscalTraseraSubtitle,txtFotoVinVehiculoSubtitle, txtFotoEstadoDeCuentaSubtitle;
    private TextView txtFotoConductorTitle, txtFotoVehiculoTitle, txtFotoVehiculoFrenteTitle, txtFotoChapaVehiculoTitle,txtLicenciaConductorTitle, txtFotoINEyoIFETitle, txtFotoConstanciaFiscalFrontalTitle, txtFotoConstanciaFiscalTraseraTitle, txtFotoVinVehiculoTitle, txtFotoEstadoDeCuentaTitle, txtFotoAntecedentesPolicialesTitle;


    private boolean isFotoDelConductorChanged = false;
    private boolean isLicenciaDelConductorChanged = false;
    private boolean isFotoVehiculoChanged = false;
    private boolean isFotoChapaVehiculoChanged = false;
    private boolean isFotoAntecedentesPoliciales = false;

    private boolean isFotoVehiculoFrente = false;
    private boolean isFotoINEyoIFE = false;
    private boolean isFotoConstanciaFiscalFrontal = false;
    private boolean isFotoConstanciaFiscalTrasera = false;
    private boolean isFotoVinVehiculo = false;
    private boolean isFotoEstadoDeCuenta = false;



    private Bitmap bmpFotoDelConductor = null;
    private Bitmap bmpLicenciaDelConductor = null;
    private Bitmap bmpFotoVehiculo = null;
    private Bitmap bmpFotoChapaVehiculo = null;
    private Bitmap bmpFotoAntecedentesPoliciales= null;

    private Bitmap bmpFotoVehiculoFrente = null;
    private Bitmap bmpFotoINEyoIFE = null;
    private Bitmap bmpFotoConstanciaFiscalFrontal = null;
    private Bitmap bmpFotoConstanciaFiscalTrasera = null;
    private Bitmap bmpFotoVinVehiculo = null;
    private Bitmap bmpFotoEstadoDeCuenta = null;

    private boolean isPhotosRequired;
    private boolean isTerminosYCondicionesRequired;

    private TextView txtTermsAndConditions;
    private CheckBox chkTermsAndConditions;
    private View layoutTermsAndConditions;
    private String termsAndConditions="";


    //TODO: TENGO QUE AGREGAR EL CODIGO PARA CAMBIAR LOS LABELS DESDE PARAMS
    private NewDriverActivityViewModel viewModel;
    private NewDriverOptionalValueData optionalDriverData=null;

    public NewDriverFotosExtendedFragment() {
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
    public static NewDriverFotosExtendedFragment newInstance(String param1, String param2) {
        NewDriverFotosExtendedFragment fragment = new NewDriverFotosExtendedFragment();
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
        view =  inflater.inflate(R.layout.fragment_new_driver_fotos_extended, container, false);
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
                /************************************************************/
                else if(requestCode==REQUEST_GALLERY_FOTO_VEHICULO_FRENTE){
                    cargarFotoVehiculoFrente(data.getData());
                }else if(requestCode==REQUEST_GALLERY_INE_O_IFE){
                    cargarFotoINE_O_IFE(data.getData());
                }else if(requestCode==REQUEST_GALLERY_FISCAL_FRONTAL){
                    cargarFotoFiscalFrontal(data.getData());
                }else if(requestCode==REQUEST_GALLERY_FISCAL_TRASERA){
                    cargarFotoFiscalTrasera(data.getData());
                }else if(requestCode==REQUEST_GALLERY_VIN_DEL_VEHICULO){
                    cargarFotoVinDelVehiculo(data.getData());
                }else if(requestCode==REQUEST_GALLERY_ESTADO_DE_CUENTA){
                    cargarFotoEstadoDeCuenta(data.getData());
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void configureControls(View view)
    {
        configureTitles();
        configureControlsFotoConductor(view);
        configureControlsLicenciaConductor(view);
        configureControlsFotoVehiculo(view);
        configureControlsChapaVehiculo(view);
        configureControlsAntecedentesPoliciales(view);

        configureControlsFotoVehiculoFrente(view);
        configureControlsFotoINEyoIFE(view);
        configureControlsFotoConstanciaFiscalFrontal(view);
        configureControlsFotoConstanciaFiscalTrasera(view);
        configureControlsFotoVinVehiculo(view);
        configureControlsFotoEstadoDeCuenta(view);

        initializeCardViewsPhotos(view);

        configurePhotoRequired(view);
        setConfigurationsFromParams(view);
        configurePhotosVisible(false);
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

    private void configureTitles()
    {

        txtFotoConductorTitle = view.findViewById(R.id.txtFotoConductorTitle);
        txtFotoVehiculoTitle = view.findViewById(R.id.txtFotoVehiculoTitle);
        txtFotoVehiculoFrenteTitle = view.findViewById(R.id.txtFotoVehiculoFrenteTitle);
        txtFotoChapaVehiculoTitle = view.findViewById(R.id.txtFotoChapaVehiculoTitle);
        txtLicenciaConductorTitle = view.findViewById(R.id.txtLicenciaConductorTitle);
        txtFotoINEyoIFETitle = view.findViewById(R.id.txtFotoINEyoIFETitle);
        txtFotoConstanciaFiscalFrontalTitle = view.findViewById(R.id.txtFotoConstanciaFiscalFrontalTitle);
        txtFotoConstanciaFiscalTraseraTitle = view.findViewById(R.id.txtFotoConstanciaFiscalTraseraTitle);
        txtFotoVinVehiculoTitle = view.findViewById(R.id.txtFotoVinVehiculoTitle);
        txtFotoEstadoDeCuentaTitle = view.findViewById(R.id.txtFotoEstadoDeCuentaTitle);
        txtFotoAntecedentesPolicialesTitle = view.findViewById(R.id.txtFotoAntecedentesPolicialesTitle);
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
            txtFotoVehiculoFrenteTitle.setText("FOTOGRAFIA DEL VEHICULO DE FRENTE");
            txtFotoINEyoIFETitle.setText("INE Y/O IFE");
            txtFotoConstanciaFiscalFrontalTitle.setText("CONSTANCIA FISCAL FRONTAL");
            txtFotoConstanciaFiscalTraseraTitle.setText("CONSTANCIA FISCAL TRASERA");
            txtFotoVinVehiculoTitle.setText("FOTOGRAFIA VIN DEL VEHICULO");
            txtFotoEstadoDeCuentaTitle.setText("ESTADO DE CUENTA");


            //Foto del conductor
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPhoto())) {
                txtFotoConductorTitle.setText(optionalDriverData.getChofer().getNameTagPhoto());
            }

            //Licencia del conductor
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagLicense())) {
                txtLicenciaConductorTitle.setText(optionalDriverData.getChofer().getNameTagLicense());
            }

            //Foto del Vehiculo
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagVehicle())) {
                txtFotoVehiculoTitle.setText(optionalDriverData.getChofer().getNameTagVehicle());
            }

            //Foto de la chapa del vehiculo
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPlate())) {
                txtFotoChapaVehiculoTitle.setText(optionalDriverData.getChofer().getNameTagPlate());
            }

            //Foto de antecendentes policiales
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNameTagPoliceRecord())) {
                txtFotoAntecedentesPolicialesTitle.setText(optionalDriverData.getChofer().getNameTagPoliceRecord());
            }
        }
    }

    private void configurePhotoRequired(View view){
        txtFotoConductorSubtitle = view.findViewById(R.id.txtFotoConductorSubtitle);
        txtLicenciaConductorSubtitle = view.findViewById(R.id.txtLicenciaConductorSubtitle);
        txtFotoVehiculoSubtitle = view.findViewById(R.id.txtFotoVehiculoSubtitle);
        txtFotoChapaVehiculoSubtitle = view.findViewById(R.id.txtFotoChapaVehiculoSubtitle);
        txtFotoAntecedentesPolicialesSubtitle = view.findViewById(R.id.txtFotoAntecedentesPolicialesSubtitle);

        txtFotoVehiculoFrenteSubtitle = view.findViewById(R.id.txtFotoVehiculoFrenteSubtitle);
        txtFotoINEyoIFESubtitle = view.findViewById(R.id.txtFotoINEyoIFESubtitle);
        txtFotoConstanciaFiscalFrontalSubtitle = view.findViewById(R.id.txtFotoConstanciaFiscalFrontalSubtitle);
        txtFotoConstanciaFiscalTraseraSubtitle = view.findViewById(R.id.txtFotoConstanciaFiscalTraseraSubtitle);
        txtFotoVinVehiculoSubtitle = view.findViewById(R.id.txtFotoVinVehiculoSubtitle);
        txtFotoEstadoDeCuentaSubtitle = view.findViewById(R.id.txtFotoEstadoDeCuentaSubtitle);;
    }

    private void setConfigurationsFromParams(View view) {
        try
        {
            //configureTitlesFromParams(null);

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

                paramEntity param94_camposAdicionales = params.get(Globales.ParametrosDeApp.PARAM_94_REGISTRAR_CHOFER_CAMPOS_OPCIONALES);
               // configureTitlesFromParams(param94_camposAdicionales);

            }
        }
        catch (Exception ex){
         ex.printStackTrace();
        }
    }

    /*private void configureTitlesFromParams(paramEntity param94_camposAdicionales)
    {
        txtFotoConductorTitle.setText("FOTO DEL CONDUCTOR");
        txtFotoVehiculoTitle.setText("FOTOGRAFIA DEL VEHICULO DE COSTADO");
        txtFotoVehiculoFrenteTitle.setText("FOTOGRAFIA DEL VEHICULO DE FRENTE");
        txtFotoChapaVehiculoTitle.setText("FOTOGRAFIA DE LAS PLACAS");
        txtLicenciaConductorTitle.setText("LICENCIA DE CONDUCIR");
        txtFotoINEyoIFETitle.setText("INE Y/O IFE");
        txtFotoConstanciaFiscalFrontalTitle.setText("CONSTANCIA FISCAL FRONTAL");
        txtFotoConstanciaFiscalTraseraTitle.setText("CONSTANCIA FISCAL TRASERA");
        txtFotoVinVehiculoTitle.setText("FOTOGRAFIA VIN DEL VEHICULO");
        txtFotoEstadoDeCuentaTitle.setText("ESTADO DE CUENTA");
        txtFotoAntecedentesPolicialesTitle.setText("ANTECEDENTES POLICIALES");
    }*/

    private void enableRequiredPhotoText(boolean required){
        txtFotoConductorSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtLicenciaConductorSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoVehiculoSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoChapaVehiculoSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoAntecedentesPolicialesSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);

        txtFotoVehiculoFrenteSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoINEyoIFESubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoConstanciaFiscalFrontalSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoConstanciaFiscalTraseraSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoVinVehiculoSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
        txtFotoEstadoDeCuentaSubtitle.setVisibility(required?View.VISIBLE:View.INVISIBLE);
    }

    private void configurePhotosVisible(boolean isVisible) {
        cardFotoVehiculoFrente.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        cardFotoINEIFE.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        cardFotoConstanciaFiscalFrontal.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        cardFotoConstanciaFiscalTrasera.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        cardFotoVinDelVehiculo.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        cardFotoEstadoDeCuenta.setVisibility(isVisible ? View.VISIBLE : View.GONE);
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

    /************************************************************************/
    private void configureControlsFotoVehiculoFrente(View view)
    {
        imgFotoVehiculoFrente = view.findViewById(R.id.imgFotoVehiculoFrente);
        imgFotoVehiculoFrenteDummy = view.findViewById(R.id.imgFotoVehiculoFrenteDummy);
        imgDeleteVehiculoFrente = view.findViewById(R.id.imgDeleteFotoVehiculoFrente);

        imgFotoVehiculoFrenteDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_FOTO_VEHICULO_FRENTE);
            }
        });

        imgDeleteVehiculoFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoVehiculoFrente(false);
            }
        });
    }

    private void configureControlsFotoINEyoIFE(View view)
    {
        imgFotoINEyoIFE = view.findViewById(R.id.imgFotoINEyoIFE);
        imgFotoINEyoIFEDummy = view.findViewById(R.id.imgFotoINEyoIFEDummy);
        imgDeleteFotoINEyoIFE = view.findViewById(R.id.imgDeleteFotoINEyoIFE);

        imgFotoINEyoIFEDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_INE_O_IFE);
            }
        });

        imgDeleteFotoINEyoIFE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoINEyiIFE(false);
            }
        });
    }

    private void configureControlsFotoConstanciaFiscalFrontal(View view)
    {
        imgFotoConstanciaFiscalFrontal = view.findViewById(R.id.imgFotoConstanciaFiscalFrontal);
        imgFotoConstanciaFiscalFrontalDummy = view.findViewById(R.id.imgFotoConstanciaFiscalFrontalDummy);
        imgDeleteFotoConstanciaFiscalFrontal = view.findViewById(R.id.imgDeleteFotoConstanciaFiscalFrontal);

        imgFotoConstanciaFiscalFrontalDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_FISCAL_FRONTAL);
            }
        });

        imgDeleteFotoConstanciaFiscalFrontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoConstanciaFiscalFrontal(false);
            }
        });
    }

    private void configureControlsFotoConstanciaFiscalTrasera(View view)
    {
        imgFotoConstanciaFiscalTrasera = view.findViewById(R.id.imgFotoConstanciaFiscalTrasera);
        imgFotoConstanciaFiscalTraseraDummy = view.findViewById(R.id.imgFotoConstanciaFiscalTraseraDummy);
        imgDeleteFotoConstanciaFiscalTrasera = view.findViewById(R.id.imgDeleteFotoConstanciaFiscalTrasera);

        imgFotoConstanciaFiscalTraseraDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_FISCAL_TRASERA);
            }
        });

        imgDeleteFotoConstanciaFiscalTrasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoConstanciaFiscalTrasera(false);
            }
        });
    }

    private void configureControlsFotoVinVehiculo(View view)
    {
        imgFotoVinVehiculo = view.findViewById(R.id.imgFotoVinVehiculo);
        imgFotoVinVehiculoDummy = view.findViewById(R.id.imgFotoVinVehiculoDummy);
        imgDeleteFotoVinVehiculo = view.findViewById(R.id.imgDeleteFotoVinVehiculo);

        imgFotoVinVehiculoDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_VIN_DEL_VEHICULO);
            }
        });

        imgDeleteFotoVinVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoVinVehiculo(false);
            }
        });
    }

    private void configureControlsFotoEstadoDeCuenta(View view)
    {
        imgFotoEstadoDeCuenta = view.findViewById(R.id.imgFotoEstadoDeCuenta);
        imgFotoEstadoDeCuentaDummy = view.findViewById(R.id.imgFotoEstadoDeCuentaDummy);
        imgDeleteFotoEstadoDeCuenta = view.findViewById(R.id.imgDeleteFotoEstadoDeCuenta);

        imgFotoEstadoDeCuentaDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery(REQUEST_GALLERY_ESTADO_DE_CUENTA);
            }
        });

        imgDeleteFotoEstadoDeCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarImgFotoEstadoDeCuenta(false);
            }
        });
    }

    private void initializeCardViewsPhotos(View view)
    {
        cardFotoFotoDelConductor = view.findViewById(R.id.cardFotoFotoDelConductor);
        cardFotoVehiculo = view.findViewById(R.id.cardFotoVehiculo);
        cardFotoVehiculoFrente = view.findViewById(R.id.cardFotoVehiculoFrente);
        cardFotoChapa = view.findViewById(R.id.cardFotoChapa);
        cardFotoLicenciaDeConducir = view.findViewById(R.id.cardFotoLicenciaDeConducir);
        cardFotoINEIFE = view.findViewById(R.id.cardFotoINEIFE);
        cardFotoConstanciaFiscalFrontal = view.findViewById(R.id.cardFotoConstanciaFiscalFrontal);
        cardFotoConstanciaFiscalTrasera = view.findViewById(R.id.cardFotoConstanciaFiscalTrasera);
        cardFotoVinDelVehiculo = view.findViewById(R.id.cardFotoVinDelVehiculo);
        cardFotoEstadoDeCuenta = view.findViewById(R.id.cardFotoEstadoDeCuenta);
        cardFotoAntecedentesPoliciales = view.findViewById(R.id.cardFotoAntecedentesPoliciales);
    }

    /*************************************************************************************/


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

    /******************************************************************/

    private void cargarFotoVehiculoFrente(Uri returnUri)
    {
        try {
            bmpFotoVehiculoFrente = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoVehiculoFrente);
            mostrarImgFotoVehiculoFrente(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoVehiculoFrente(false);
        }
    }

    private void cargarFotoINE_O_IFE(Uri returnUri)
    {
        try {
            bmpFotoINEyoIFE = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoINEyoIFE);
            mostrarImgFotoINEyiIFE(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoINEyiIFE(false);
        }
    }

    private void cargarFotoFiscalFrontal(Uri returnUri)
    {
        try {
            bmpFotoConstanciaFiscalFrontal = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoConstanciaFiscalFrontal);
            mostrarImgFotoConstanciaFiscalFrontal(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoConstanciaFiscalFrontal(false);
        }
    }

    private void cargarFotoFiscalTrasera(Uri returnUri)
    {
        try {
            bmpFotoConstanciaFiscalTrasera = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoConstanciaFiscalTrasera);
            mostrarImgFotoConstanciaFiscalTrasera(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoConstanciaFiscalTrasera(false);
        }
    }

    private void cargarFotoVinDelVehiculo(Uri returnUri)
    {
        try {
            bmpFotoVinVehiculo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoVinVehiculo);
            mostrarImgFotoVinVehiculo(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoVinVehiculo(false);
        }
    }

    private void cargarFotoEstadoDeCuenta(Uri returnUri)
    {
        try {
            bmpFotoEstadoDeCuenta = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
            Glide.with(getActivity())
                    .load(returnUri)
                    .fitCenter()
                    .placeholder(R.drawable.ic_nota)
                    .into(imgFotoEstadoDeCuenta);
            mostrarImgFotoEstadoDeCuenta(true);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarImgFotoEstadoDeCuenta(false);
        }
    }
/**********************************************************/

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

    /***************************************************************************/
    private void mostrarImgFotoVehiculoFrente(boolean mostrar)
    {
        bmpFotoVehiculoFrente =  mostrar ? bmpFotoVehiculoFrente : null;
        isFotoVehiculoFrente = mostrar;
        imgFotoVehiculoFrente.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoVehiculoFrenteDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteVehiculoFrente.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoINEyiIFE(boolean mostrar)
    {
        bmpFotoINEyoIFE =  mostrar ? bmpFotoINEyoIFE : null;
        isFotoINEyoIFE = mostrar;
        imgFotoINEyoIFE.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoINEyoIFEDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoINEyoIFE.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoConstanciaFiscalFrontal(boolean mostrar)
    {
        bmpFotoConstanciaFiscalFrontal =  mostrar ? bmpFotoConstanciaFiscalFrontal : null;
        isFotoConstanciaFiscalFrontal = mostrar;
        imgFotoConstanciaFiscalFrontal.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoConstanciaFiscalFrontalDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoConstanciaFiscalFrontal.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoConstanciaFiscalTrasera(boolean mostrar)
    {
        bmpFotoConstanciaFiscalTrasera =  mostrar ? bmpFotoConstanciaFiscalTrasera : null;
        isFotoConstanciaFiscalTrasera = mostrar;
        imgFotoConstanciaFiscalTrasera.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoConstanciaFiscalTraseraDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoConstanciaFiscalTrasera.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoVinVehiculo(boolean mostrar)
    {
        bmpFotoVinVehiculo =  mostrar ? bmpFotoVinVehiculo : null;
        isFotoVinVehiculo = mostrar;
        imgFotoVinVehiculo.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoVinVehiculoDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoVinVehiculo.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }

    private void mostrarImgFotoEstadoDeCuenta(boolean mostrar)
    {
        bmpFotoEstadoDeCuenta =  mostrar ? bmpFotoEstadoDeCuenta : null;
        isFotoEstadoDeCuenta = mostrar;
        imgFotoEstadoDeCuenta.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoEstadoDeCuentaDummy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoEstadoDeCuenta.setVisibility(mostrar ? View.VISIBLE: View.GONE);
    }
    /****************************************************************************/

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


    private void setVisibilityTermsAndConditions()
    {
        layoutTermsAndConditions.setVisibility(isTerminosYCondicionesRequired ? View.VISIBLE: View.GONE);
    }

}
