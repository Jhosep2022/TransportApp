package com.example.apptransport.Fragments;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.apreciasoft.mobile.asremis.Entity.Driver;
import com.apreciasoft.mobile.asremis.Entity.dataAddPlusDriverEntity;
import com.apreciasoft.mobile.asremis.Entity.driverAdd;
import com.apreciasoft.mobile.asremis.Entity.fleet;
import com.apreciasoft.mobile.asremis.Entity.fleetType;
import com.apreciasoft.mobile.asremis.Entity.modelDetailEntity;
import com.apreciasoft.mobile.asremis.Entity.modelEntity;
import com.apreciasoft.mobile.asremis.Entity.responseFilterVehicle;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNewDriver extends Fragment implements  AdapterView.OnItemSelectedListener{

    private View view;
    private EditText editTextNombre,editTextApellido,editTextTelefono,editTextEmail,editTextContraseña,
            editTextContraseña2,editTextNumChofer,editTextDominio;

    private Spinner spinnerMarca,spinnerModelo,spinnerCategoria;
    private Button buttonRegistro;

    ServicesDriver apiDriver = null;
    private Integer idMarca,idModel,idCategoria;
    private static String[] VEHYCLEMARCK = new String[0];
    private static String[] VEHYCLEMODEL = new String[0];
    private static String[] VEHYCLETYPE = new String[0];
    public  List<modelEntity> listModel = null;
    public  List<fleetType>   listFlet  = null;
    public  List<modelDetailEntity>   listModelDetail  = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_chofer, container, false);
        addView();
        apiDriver = HttpConexion.getUri().create(ServicesDriver.class);
        getMarca();
        getCategoria();
        return view;
    }

    private void addView() {
        editTextNombre= view.findViewById(R.id.edittext_nombre);
        editTextApellido= view.findViewById(R.id.edittext_apellido);
        editTextTelefono= view.findViewById(R.id.edittext_telefono);
        editTextEmail= view.findViewById(R.id.edittext_email);
        editTextContraseña= view.findViewById(R.id.edittext_contraseña);
        editTextContraseña2= view.findViewById(R.id.edittext_contraseña_2);
        editTextNumChofer= view.findViewById(R.id.edittext_num_chofer);
        editTextDominio= view.findViewById(R.id.edittext_dominio);

        spinnerMarca= view.findViewById(R.id.spinner_marca);
        spinnerModelo= view.findViewById(R.id.spinner_modelo);
        spinnerCategoria= view.findViewById(R.id.spinner_category);

        buttonRegistro= view.findViewById(R.id.button_register);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    private void validarCampos(){
        String nombre = editTextNombre.getText().toString().trim();
        String apellido= editTextApellido.getText().toString().trim();
        String telefono= editTextTelefono.getText().toString().trim();
        String email= editTextEmail.getText().toString().trim();
        String numChofer= editTextNumChofer.getText().toString().trim();
        String contraseña= editTextContraseña.getText().toString().trim();
        String contraseña2= editTextContraseña2.getText().toString().trim();
        String dominio= editTextDominio.getText().toString().trim();

        if (nombre.isEmpty()){
            editTextNombre.setError(getString(R.string.completar_campo));
            editTextNombre.requestFocus();
        }else if (apellido.isEmpty()){
            editTextApellido.setError(getString(R.string.completar_campo));
            editTextApellido.requestFocus();
        }else if (telefono.isEmpty()){
            editTextTelefono.setError(getString(R.string.completar_campo));
            editTextTelefono.requestFocus();
        }else if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!validationEmail(email)){
            editTextEmail.setError(getString(R.string.email_invalido));
            editTextEmail.requestFocus();
        }else if(numChofer.isEmpty()){
            editTextNumChofer.setError(getString(R.string.completar_campo));
            editTextNumChofer.requestFocus();
        }else if (contraseña.isEmpty()){
            editTextContraseña.setError(getString(R.string.completar_campo));
            editTextContraseña.requestFocus();
        }else if (contraseña2.isEmpty()){
            editTextContraseña2.setError(getString(R.string.completar_campo));
            editTextContraseña2.requestFocus();
        }else if (!contraseña.equals(contraseña2)){
            editTextContraseña2.setError(getString(R.string.contraeseña_diferente));
            editTextContraseña2.requestFocus();
        }else if (dominio.isEmpty()){
            editTextDominio.setError(getString(R.string.completar_campo));
            editTextDominio.requestFocus();
        }else if (idMarca == null || idCategoria == null ){
            showSnack(getString(R.string.especificar_marca_categoria),1);
        }else {
            String nombreCompleto= nombre+" "+apellido;
            crearChofer(nombreCompleto,numChofer,email,contraseña,telefono,dominio,idMarca,idModel,idCategoria,"");
        }

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
                        showSnack(getString(R.string.no_modelo_agregado),0);

                    } else {
                        showSnack(response.errorBody().source().toString(),2);
                    }
                }catch (Exception e){
                    Log.e("MODELO EXCETION",e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<modelEntity>> call, Throwable t) {
                Log.e("MODELO FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),3);
            }
        });

    }

    private void getModelo(){
        apiDriver.getModelDetail(idMarca).enqueue(new Callback<responseFilterVehicle>() {
            @Override
            public void onResponse(Call<responseFilterVehicle> call, Response<responseFilterVehicle> response) {
                try {
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
                        showSnack(getString(R.string.no_modelo_agregado),0);
                    } else {
                        showSnack(response.errorBody().source().toString(),2);
                    }
                }catch (Exception e){
                    Log.e("MODELO EXECPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<responseFilterVehicle> call, Throwable t) {
                Log.e("MODELO FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),3);
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
                      showSnack(getString(R.string.no_categorias_agregado),1);

                  } else {
                      showSnack(response.errorBody().source().toString(),2);
                  }
              }catch (Exception e){
                  Log.e("CATEGORIA EXCEPTION",e.toString());
                  showSnack(getString(R.string.fallo_general),3);
              }
          }

          @Override
          public void onFailure(Call<List<fleetType>> call, Throwable t) {
              Log.e("CATEGORIA FAILURE",t.toString());
              showSnack(getString(R.string.fallo_general),3);
          }
      });

    }

    private void setListMarca( List<String> listMarca){
        spinnerMarca.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listMarca);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(dataAdapter1);
    }

    private void setListModelo( List<String> listModelo){
        spinnerModelo.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listModelo);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModelo.setAdapter(dataAdapter1);
    }

    private void setListCategoria( List<String> listCategoria){
        spinnerCategoria.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCategoria);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(dataAdapter1);
    }

    private void crearChofer(final String nombre, String numChofer, String email, String pass, String phone,
                             String dominio, Integer idMarca, Integer idModelo, Integer idCategoria, String isAcceptedTermsAndConditions){

        buttonRegistro.setText(R.string.validando);
        buttonRegistro.setEnabled(false);

        dataAddPlusDriverEntity data =  new dataAddPlusDriverEntity(
                new driverAdd(
                        nombre,
                        numChofer,
                        email,
                        pass,
                        phone,
                        1,
                        1,
                        "",
                        isAcceptedTermsAndConditions),
                new fleet(idMarca,idModelo,idCategoria,dominio)
        );

        apiDriver.addPluDriver(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                buttonRegistro.setText(R.string.registrar);
                buttonRegistro.setEnabled(true);

                try {
                    if (response.code() == 200) {
                       showSnack(getString(R.string.chofer_registrado),1);
                        new Handler().postDelayed( new Runnable(){
                            public void run(){
                                getActivity().finish();
                            }
                        }, 5000);


                    }  else if (response.code() == 201) {
                        editTextEmail.setError(getString(R.string.email_error_registrado));
                        editTextEmail.requestFocus();
                    } else if(response.code()==203) {
                        editTextNumChofer.setError(getString(R.string.chofer_error_registrado));
                        editTextNumChofer.requestFocus();
                    }else {
                        showSnack(response.message(),2);
                    }
                }catch (Exception e){
                    Log.e("CREAR CHOFER EXCEPTION", e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CREAR CHOFER FAILURE", t.toString());
                buttonRegistro.setText(R.string.registrar);
                buttonRegistro.setEnabled(true);
                showSnack(getString(R.string.fallo_general),3);
            }
        });

    }

    private void showSnack(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.spinner_marca:
                this.idMarca =  listModel.get(position).getIdVehicleBrand();
                getModelo();
                break;
            case R.id.spinner_modelo:
                this.idModel =  listModelDetail.get(position).getIdVehicleModel();
                break;
            case R.id.spinner_category:
                this.idCategoria =  listFlet.get(position).getIdVehicleType();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Boolean validationEmail(String _email){

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(_email);

        if (mather.find() == true) {
            return  true;

        } else {
            return  false;
        }

    }
}
