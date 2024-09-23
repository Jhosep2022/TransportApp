package com.example.apptransport.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.NewDriverOptionalValueData;
import com.apreciasoft.mobile.asremis.Entity.NewDriverVehicleData;
import com.apreciasoft.mobile.asremis.Entity.fleetType;
import com.apreciasoft.mobile.asremis.Entity.modelDetailEntity;
import com.apreciasoft.mobile.asremis.Entity.modelEntity;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.responseFilterVehicle;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import  com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.viewmodels.NewDriverActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link NewDriverVehiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDriverVehiculoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ListenerRegisterChofer listener;
    private Button btnNext;
    private Button btnPrevious;


    private EditText editTextDominio;
    private Spinner spinnerMarca,spinnerModelo,spinnerCategoria;
    ServicesDriver apiDriver = null;
    private Integer idMarca,idModel,idCategoria;
    private static String[] VEHYCLEMARCK = new String[0];
    private static String[] VEHYCLEMODEL = new String[0];
    private static String[] VEHYCLETYPE = new String[0];
    public List<modelEntity> listModel = null;
    public  List<fleetType>   listFlet  = null;
    public  List<modelDetailEntity>   listModelDetail  = null;

    private NewDriverActivityViewModel viewModel;
    private NewDriverOptionalValueData optionalDriverData=null;
    private TextInputLayout txtDominioInputLayout;
    private TextView txtMarcaTitle, txtModeloTitle,txtCategoriaTitle;

    public NewDriverVehiculoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewDriverVehiculoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDriverVehiculoFragment newInstance(String param1, String param2) {
        NewDriverVehiculoFragment fragment = new NewDriverVehiculoFragment();
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
        view =  inflater.inflate(R.layout.fragment_new_driver_vehiculo, container, false);
        configureControls(view);
        apiDriver = HttpConexion.getUri().create(ServicesDriver.class);
        getMarca();
        getCategoria();
        initializeViewModel();
        initializeObserverParams();
        return view;
    }

    private void configureControls(View view)
    {
        editTextDominio= view.findViewById(R.id.edittext_dominio);
        spinnerMarca= view.findViewById(R.id.spinner_marca);
        spinnerModelo= view.findViewById(R.id.spinner_modelo);
        spinnerCategoria= view.findViewById(R.id.spinner_category);
        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idMarca =  listModel.get(position).getIdVehicleBrand();
                getModelo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idModel =  listModelDetail.get(position).getIdVehicleModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCategoria =  listFlet.get(position).getIdVehicleType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNextButton();
            }
        });

        btnPrevious = view.findViewById(R.id.btn_back);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnAtras(1);
            }
        });


        txtMarcaTitle = view.findViewById(R.id.txtMarcaTitle);
        txtModeloTitle = view.findViewById(R.id.txtModeloTitle);
        txtCategoriaTitle = view.findViewById(R.id.txtCategoriaTitle);
        txtDominioInputLayout = view.findViewById(R.id.txtDominioInputLayout);
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

            //Marca
            if (!TextUtils.isEmpty(optionalDriverData.getVehiculo().getBrandlabel())) {
                txtMarcaTitle.setText(optionalDriverData.getVehiculo().getBrandlabel());
            }

            //Modelo
            if (!TextUtils.isEmpty(optionalDriverData.getVehiculo().getModellabel())) {
                txtModeloTitle.setText(optionalDriverData.getVehiculo().getModellabel());
            }

            //Categoria
            if (!TextUtils.isEmpty(optionalDriverData.getVehiculo().getTypelabel())) {
                txtCategoriaTitle.setText(optionalDriverData.getVehiculo().getTypelabel());
            }

            //Dominio
            if (!TextUtils.isEmpty(optionalDriverData.getVehiculo().getDomainlabel())) {
                txtDominioInputLayout.setHint(optionalDriverData.getVehiculo().getDomainlabel());
            }
        }
    }

    private void btnNextButton()
    {
        if(validateFields())
        {
            NewDriverVehicleData vehicleData = getVehicleData();
            listener.saveVehicle(vehicleData);
            listener.btnSiguiente(1);
        }
    }

    private NewDriverVehicleData getVehicleData(){
        NewDriverVehicleData  result=new NewDriverVehicleData();
        result.idMarca = idMarca;
        result.idModel =  idModel;
        result.idCategoria = idCategoria;
        result.dominio = editTextDominio.getText().toString().trim();
        return result;
    }

    private boolean validateFields(){
        boolean result=false;
        NewDriverOptionalValueData optionalData = Utils.getOptionalDataValidation(getContext());

        String dominio= editTextDominio.getText().toString().trim();
        if (!optionalData.getVehiculo().isDominio() && dominio.isEmpty()){
            editTextDominio.setError(getString(R.string.completar_campo));
            editTextDominio.requestFocus();
        }else if (idMarca == null || idCategoria == null ){
            showSnack(getString(R.string.especificar_marca_categoria),Globales.StatusToast.WARNING);
        }
        else{
            result=true;
        }
        return result;
    }

    public void setListenerRegisterChofer(ListenerRegisterChofer listener)
    {
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getMarca(){
        apiDriver.filterForm().enqueue(new Callback<List<modelEntity>>() {
            @Override
            public void onResponse(Call<List<modelEntity>> call, Response<List<modelEntity>> response) {
                try {
                    if (response.code() == 200) {
                        listModel = response.body();
                        if(listModel!= null) {
                            List<String> listMarca = new ArrayList<String>();
                            VEHYCLEMARCK = new String[listModel.size()];
                            for (int i = 0; i < listModel.size(); i++) {
                                listMarca.add(listModel.get(i).getNameVehicleBrand());
                            }
                            listMarca.toArray(VEHYCLEMARCK);

                            setListMarca(listMarca);
                        }

                    } else if (response.code() == 404) {
                        showSnack(getString(R.string.no_modelo_agregado), Globales.StatusToast.INFO);

                    } else {
                        showSnack(response.errorBody().source().toString(),Globales.StatusToast.FAIL);
                    }
                }catch (Exception e){
                    Log.e("MODELO EXCEPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<List<modelEntity>> call, Throwable t) {
                Log.e("MODELO FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });

    }

    private void getModelo(){
        final ProgressDialog progressDialog =  showProgressDialog();
        progressDialog.show();
        apiDriver.getModelDetail(idMarca).enqueue(new Callback<responseFilterVehicle>() {
            @Override
            public void onResponse(Call<responseFilterVehicle> call, Response<responseFilterVehicle> response) {
                try {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        listModelDetail = response.body().getListModel();

                        if(listModelDetail!= null) {
                            VEHYCLEMODEL = new String[listModelDetail.size()];
                            List<String> listModelo = new ArrayList<>();

                            for (int i = 0; i < listModelDetail.size(); i++) {
                                listModelo.add(listModelDetail.get(i).getNameVehicleModel());
                            }
                            listModelo.toArray(VEHYCLEMODEL);

                            setListModelo(listModelo);
                        }

                    } else if (response.code() == 404) {
                        showSnack(getString(R.string.no_modelo_agregado),Globales.StatusToast.INFO);
                    } else {
                        showSnack(response.errorBody().source().toString(),Globales.StatusToast.FAIL);
                    }
                }catch (Exception e){
                    Log.e("MODELO EXECPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<responseFilterVehicle> call, Throwable t) {
                Log.e("MODELO FAILURE",t.toString());
                progressDialog.dismiss();
                showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });

    }

    private void getCategoria(){

        apiDriver.filterFormfleetType().enqueue(new Callback<List<fleetType>>() {
            @Override
            public void onResponse(Call<List<fleetType>> call, Response<List<fleetType>> response) {
                try {
                    if (response.code() == 200) {
                        listFlet = response.body();

                        List<String> listCategoria = new ArrayList<String>();
                        VEHYCLETYPE = new String[listFlet.size()];

                        for (int i = 0; i < listFlet.size(); i++) {
                            listCategoria.add(listFlet.get(i).getVehiclenType());
                        }
                        listCategoria.toArray(VEHYCLETYPE);
                        setListCategoria(listCategoria);

                    } else if (response.code() == 404) {
                        showSnack(getString(R.string.no_categorias_agregado),Globales.StatusToast.SUCCESS);

                    } else {
                        showSnack(response.errorBody().source().toString(),Globales.StatusToast.FAIL);
                    }
                }catch (Exception e){
                    Log.e("CATEGORIA EXCEPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
                }
            }

            @Override
            public void onFailure(Call<List<fleetType>> call, Throwable t) {
                Log.e("CATEGORIA FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),Globales.StatusToast.WARNING);
            }
        });

    }

    private void setListMarca( List<String> listMarca){
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listMarca);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(dataAdapter1);
    }

    private void setListModelo( List<String> listModelo){

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listModelo);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModelo.setAdapter(dataAdapter1);
    }

    private void setListCategoria( List<String> listCategoria){
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoria);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(dataAdapter1);
    }
    private void showSnack(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

    private ProgressDialog showProgressDialog()
    {
        ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.cargando_modelos),
                getString(R.string.espere_unos_segundos), true,false);
        return dialog;
    }
}
