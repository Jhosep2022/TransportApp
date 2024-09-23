package com.example.apptransport.Fragments;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.ClientEntityAdd;
import com.apreciasoft.mobile.asremis.Entity.Company;
import com.apreciasoft.mobile.asremis.Entity.RequetClient;
import com.apreciasoft.mobile.asremis.Entity.TermAndConditionResult;
import com.apreciasoft.mobile.asremis.Entity.acountCompany;
import com.apreciasoft.mobile.asremis.Entity.costCenterCompany;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesDriver;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.SnackCustomService;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNewClient extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_GALLERY_FOTO_CONDUCTOR=1000;

    private View view;
    private EditText editTextNombre,editTextApellido,editTextTelefono,editTextEmail,editTextContraseña,edittextCurp,
            editTextContraseña2, editTextDni;
    private View cardViewFotoPasajero;

    private Spinner spinnerEmpresa,spinnerCuenta,spinnerCentroCosto;
    private Button buttonRegistro;
    private CardView cardViewEmpresa;
    private CheckBox checkBoxEmpresa;
    ServicesDriver apiDriver = null;
    CountryCodePicker ccp;
    public  Integer idCompanyKf,idCostCenter,idCompanyAcount;

    private boolean checkEmpresa= false;

    public List<acountCompany> listAcountCompany = null;
    public  List<costCenterCompany>   listCostCenter  = null;
    public  List<Company>   listCompany  = null;


    private static String[] COSTCENTER = new String[0];
    private static String[] COMPANY = new String[0];
    private static String[] ACOUNT = new String[0];

    private String firstName, lastName, email, socialid;
    private View cardViewPasswords;

    private TextView txtTermsAndConditions;
    private CheckBox chkTermsAndConditions;
    private View layoutTermsAndConditions;
    private String termsAndConditions="";
    private boolean isTerminosYCondicionesRequired=false;


    private TextInputLayout inputLayoutNombre, inputLayoutApellido, inputLayoutCurp, inputLayoutEmail, inputLayoutDNI;
    private AppCompatImageView imgFotoConductorDymmy, imgFotoConductor, imgDeleteFotoConductor;
    private boolean isFotoDelConductorChanged = false;
    private Bitmap bmpFotoDelConductor = null;
    private TextView txtFotoPasajeroTitle;
    private boolean isPhotosRequired;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_client, container, false);
        getParametrs();
        addView();
        loadParamsFromApi();
        apiDriver = HttpConexion.getUri().create(ServicesDriver.class);
        fillViewIfSocialLogin();
        fillTermsAndConditions();
        setVisibleFields(false);
        return view;
    }

    /* RESPUESTA GALERIA */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try{
            if (resultCode == getActivity().RESULT_OK) {
                if (requestCode == REQUEST_GALLERY_FOTO_CONDUCTOR) {
                    cargarFotoConductor(data.getData());
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }





    private void fillViewIfSocialLogin() {
        if(!TextUtils.isEmpty(socialid))
        {
            editTextNombre.setText(firstName);
            editTextApellido.setText(lastName);
            editTextEmail.setText(email);
        }
    }

    private void addView() {
        editTextNombre = view.findViewById(R.id.edittext_nombre);
        editTextApellido = view.findViewById(R.id.edittext_apellido);
        editTextTelefono = view.findViewById(R.id.edittext_telefono);
        editTextDni = view.findViewById(R.id.edittext_dni);
        editTextEmail = view.findViewById(R.id.edittext_email);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        ccp.registerPhoneNumberTextView(editTextTelefono);
        setDefaultCountry();
        txtTermsAndConditions = view.findViewById(R.id.txtTermsAndConditions);
        edittextCurp = view.findViewById(R.id.edittext_curp);
        txtFotoPasajeroTitle = view.findViewById(R.id.txtFotoPasajeroTitle);

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validationEmail(s.toString())) {
                    getempresa(s.toString());
                }
            }
        });

        editTextContraseña = view.findViewById(R.id.edittext_contraseña);
        editTextContraseña2 = view.findViewById(R.id.edittext_contraseña_2);
        cardViewPasswords = view.findViewById(R.id.cardView2);
        cardViewPasswords.setVisibility(TextUtils.isEmpty(socialid) ? View.VISIBLE : View.GONE);


        cardViewEmpresa = view.findViewById(R.id.cardView_empresa);
        cardViewEmpresa.setVisibility(View.GONE);

        checkBoxEmpresa = view.findViewById(R.id.checkbox_empresa);
        checkBoxEmpresa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked) {
                                                               checkEmpresa = isChecked;
                                                               cardViewEmpresa.setVisibility(View.VISIBLE);
                                                           } else {
                                                               checkEmpresa = isChecked;
                                                               cardViewEmpresa.setVisibility(View.GONE);
                                                               idCompanyKf = null;
                                                               idCompanyAcount = null;
                                                               idCostCenter = null;
                                                           }
                                                       }
                                                   }
        );

        spinnerEmpresa = view.findViewById(R.id.spinner_empresa);
        spinnerCuenta = view.findViewById(R.id.spinner_cuenta);
        spinnerCentroCosto = view.findViewById(R.id.spinner_centro_costo);

        buttonRegistro = view.findViewById(R.id.button_register);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

        chkTermsAndConditions = view.findViewById(R.id.chkTermsAndConditions);
        layoutTermsAndConditions = view.findViewById(R.id.layoutTermsAndConditions);
        setVisibilityTermsAndConditions();
        txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsAndConditions();
            }
        });

        inputLayoutNombre = view.findViewById(R.id.textInputLayoutNombre);
        inputLayoutApellido = view.findViewById(R.id.textInputLayoutApellido);
        inputLayoutCurp = view.findViewById(R.id.textInputLayoutCurp);
        inputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        inputLayoutDNI = view.findViewById(R.id.textInputLayoutDNI);
        configureControlsFotoConductor(view);

    }

    private void configureControlsFotoConductor(View view)
    {
        cardViewFotoPasajero = view.findViewById(R.id.cardViewFotoPasajero);
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

    private void setVisibleFields(boolean isVisible)
    {
        cardViewFotoPasajero.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        edittextCurp.setVisibility(isVisible  ? View.VISIBLE : View.GONE );
    }

    private void mostrarImgFotoConductor(boolean mostrar)
    {
        bmpFotoDelConductor = mostrar ? bmpFotoDelConductor : null;
        isFotoDelConductorChanged = mostrar;
        imgFotoConductor.setVisibility(mostrar ?View.VISIBLE :  View.GONE);
        imgFotoConductorDymmy.setVisibility(mostrar?View.GONE : View.VISIBLE);
        imgDeleteFotoConductor.setVisibility(mostrar ? View.VISIBLE: View.GONE);
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


    private void getParametrs()
    {
        Bundle b = getArguments();
        try{
            if(b!=null)
            {
                firstName = b.getString("firstName");
                lastName = b.getString("lastName");
                email = b.getString("email");
                socialid = b.getString("socialid");
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
    }

    private void setDefaultCountry() {
        String userCountry =  Utils.getUserCountry(view.getContext());
        if(!TextUtils.isEmpty(userCountry))
        {
            ccp.setCountryForNameCode(userCountry);
        }
    }

    private void validarCampos(){
        String nombre = editTextNombre.getText().toString().trim();
        String apellido= editTextApellido.getText().toString().trim();
        String telefono= editTextTelefono.getText().toString().trim();
        String telefonoConPrefijo = ccp.getFullNumberWithPlus();
        String email= editTextEmail.getText().toString().trim();
        String contraseña= editTextContraseña.getText().toString().trim();
        String contraseña2= editTextContraseña2.getText().toString().trim();
        String dni = editTextDni.getText().toString().trim();
        Boolean isCheckedTerms = chkTermsAndConditions.isChecked();

        if (nombre.isEmpty()){
            editTextNombre.setError(getString(R.string.completar_campo));
            editTextNombre.requestFocus();
        }
        else if(!Utils.validateName(editTextNombre.getText().toString()))
        {
            editTextNombre.setError(getString(R.string.agregar_nombre_valido));
            editTextNombre.requestFocus();
        }
        else if (apellido.isEmpty()){
            editTextApellido.setError(getString(R.string.completar_campo));
            editTextApellido.requestFocus();
        }
        else if(!Utils.validateName(editTextApellido.getText().toString()))
        {
            editTextApellido.setError(getString(R.string.agregar_apellido_valido));
            editTextApellido.requestFocus();
        }
        else if (telefono.isEmpty()){
            editTextTelefono.setError(getString(R.string.completar_campo));
            editTextTelefono.requestFocus();
        }else if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!validationEmail(email)){
            editTextEmail.setError(getString(R.string.email_invalido));
            editTextEmail.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && contraseña.isEmpty()){
            editTextContraseña.setError(getString(R.string.completar_campo));
            editTextContraseña.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && contraseña2.isEmpty()){
            editTextContraseña2.setError(getString(R.string.completar_campo));
            editTextContraseña2.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && !contraseña.equals(contraseña2)){
            editTextContraseña2.setError(getString(R.string.contraeseña_diferente));
            editTextContraseña2.requestFocus();
        }
        else if(dni.isEmpty())
        {
            editTextDni.setError(getString(R.string.completar_campo));
            editTextDni.requestFocus();
        }
        else if(isTerminosYCondicionesRequired && !TextUtils.isEmpty(termsAndConditions) && !isCheckedTerms)
        {
            showSnack("Debe aceptar los términos y condiciones", Globales.StatusToast.FAIL);
        }
        else {

            if (checkEmpresa){
                if (idCompanyKf==null){
                    showSnack(getString(R.string.seleccion_empresa),1);
                }else {
                    crearCLiente(nombre,apellido,email,contraseña,telefonoConPrefijo, dni);
                }
            }else {
                crearCLiente(nombre,apellido,email,contraseña,telefonoConPrefijo, dni);
            }
        }
    }

    private void showSnack(String mensaje,int status){
        SnackCustomService.show(getActivity(),mensaje,status);
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

    private void crearCLiente(String firstName,String lasName,String mail,String pass,String phone, String dni){
        buttonRegistro.setText(R.string.validando);
        buttonRegistro.setEnabled(false);

        RequetClient client = new RequetClient(
                    new ClientEntityAdd(
                            firstName,
                            lasName,
                            mail,
                            !TextUtils.isEmpty(socialid) ? "" : pass,
                            1,
                            idCompanyAcount,
                            phone,
                            idCostCenter,
                            idCompanyKf,
                            dni,
                            socialid,
                            chkTermsAndConditions.isChecked()?"1":"0"
                    )
            );

        apiDriver.addClient(client).enqueue(new Callback<resp>() {
            @Override
            public void onResponse(Call<resp> call, Response<resp> response) {
                buttonRegistro.setText(R.string.registrar);
                buttonRegistro.setEnabled(true);

                try {
                    if (response.code() == 200) {
                        showSnack(getString(R.string.cliente_registrado),1);
                        new Handler().postDelayed(new Runnable(){
                            public void run(){
                                if(getActivity()!=null)
                                {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("socialid",socialid);
                                    getActivity().setResult(Activity.RESULT_OK,returnIntent);
                                    getActivity().finish();
                                }
                            }
                        }, 4000);

                    }  else if (response.code() == 404) {
                        showSnack(getString(R.string.cliente_ya_registrado),2);

                    }
                }catch (Exception e){
                        Log.e("CREAR CHOFER EXCEPTION", e.toString());
                        showSnack(getString(R.string.fallo_general),3);
                }

            }

            @Override
            public void onFailure(Call<resp> call, Throwable t) {
                    Log.e("CRAR CLIENTE FAILURE", t.toString());
                    buttonRegistro.setText(R.string.registrar);
                    buttonRegistro.setEnabled(true);
                    showSnack(getString(R.string.fallo_general),3);
            }
        });
    }

    private void setListEmpresa(List<String> listEmpresa){
        spinnerEmpresa.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listEmpresa);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpresa.setAdapter(dataAdapter1);
    }

    private void setListCuenta(List<String> listCuenta){
        spinnerCuenta.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCuenta);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuenta.setAdapter(dataAdapter1);
    }

    private void setListCentroCosto(List<String> listCentroCosto){
        spinnerCentroCosto.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCentroCosto);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentroCosto.setAdapter(dataAdapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spinner_cuenta:
                idCompanyAcount =  listAcountCompany.get(position).getIdCompanyAcount();
                getCentroCosto();
                break;
            case R.id.spinner_centro_costo:
                idCostCenter =  listCostCenter.get(position).getIdCostCenter();
                break;
            case R.id.spinner_empresa:
                idCompanyKf =  listCompany.get(position).getIdClientKf();
                getCuenta();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getempresa(String mailTxt) {

        apiDriver.validatorDomaint(mailTxt).enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                try {
                    if (response.code() == 200) {

                        List<Company> r = response.body();
                        listCompany = r;

                        List<String> listEmpresa = new ArrayList<>();

                        if(listCompany!= null) {

                            COMPANY = new String[listCompany.size()];
                            for (int i = 0; i < listCompany.size(); i++) {
                                listEmpresa.add(listCompany.get(i).getNameClientCompany());
                            }
                            listEmpresa.toArray(COMPANY);

                            setListEmpresa(listEmpresa);
                        }

                    } else if (response.code() == 404) {


                    } else {
                        showSnack(response.errorBody().source().toString(),3);
                    }

                }catch (Exception e){
                    Log.e("EMPRESA EXCEPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                Log.e("EMPRESA FAILURE",t.toString());
                try{
                    showSnack(getString(R.string.fallo_general),3);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

    }

    private void getCuenta(){

        apiDriver.getAcountByidCompany(idCompanyKf).enqueue(new Callback<List<acountCompany>>() {
            @Override
            public void onResponse(Call<List<acountCompany>> call, Response<List<acountCompany>> response) {
                try {
                    if (response.code() == 200) {
                        List<acountCompany> r  = response.body();
                        listAcountCompany = r;

                        List<String> listCuenta = new ArrayList<>();

                        if(listAcountCompany!= null) {

                            ACOUNT = new String[listAcountCompany.size()];

                            for (int i = 0; i < listAcountCompany.size(); i++) {
                                listCuenta.add(listAcountCompany.get(i).getNrAcount());
                            }
                            listCuenta.toArray(ACOUNT);

                            setListCuenta(listCuenta);
                        }
                    } else if (response.code() == 404) {
                        showSnack(getString(R.string.no_cuenta),0);

                    } else {
                        showSnack(response.errorBody().source().toString(),2);
                    }

                }catch (Exception e){
                    Log.e("CUENTA EXCEPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<acountCompany>> call, Throwable t) {
                Log.e("CUENTA FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),3);
            }
        });
    }

    public void getCentroCosto() {

        apiDriver.costCenterByidAcount(idCompanyAcount).enqueue(new Callback<List<costCenterCompany>>() {
            @Override
            public void onResponse(Call<List<costCenterCompany>> call, Response<List<costCenterCompany>> response) {
                try {
                    if (response.code() == 200) {

                        List<costCenterCompany> r  =  response.body();
                        listCostCenter = r;

                        List<String> listCentroCosto = new ArrayList<>();

                        if(listCostCenter!= null) {

                            COSTCENTER = new String[listCostCenter.size()];

                            for (int i = 0; i < listCostCenter.size(); i++) {
                                listCentroCosto.add(listCostCenter.get(i).getCostCenter());
                            }
                            listCentroCosto.toArray(COSTCENTER);

                            setListCentroCosto(listCentroCosto);
                        }
                    } else if (response.code() == 404) {
                        showSnack(getString(R.string.no_centro_costo),0);

                    } else {
                        showSnack(response.errorBody().source().toString(),2);
                    }

                }catch (Exception e){
                    Log.e("CENTRO COSTO EXECEPTION",e.toString());
                    showSnack(getString(R.string.fallo_general),3);
                }
            }

            @Override
            public void onFailure(Call<List<costCenterCompany>> call, Throwable t) {
                Log.e("CENTRO COSTO FAILURE",t.toString());
                showSnack(getString(R.string.fallo_general),3);
            }
        });
    }

    private void fillTermsAndConditions() {
        apiDriver.getTermsAndConditions(1).enqueue(new Callback<TermAndConditionResult>() {//1: Cliente
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

    private void setVisibilityTermsAndConditions() {
        layoutTermsAndConditions.setVisibility(isTerminosYCondicionesRequired ? View.VISIBLE : View.GONE);
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
                            List<paramEntity>listParam = response.body();
                            paramEntity param114_habilita_terminos = listParam.get(Globales.ParametrosDeApp.PARAM_114_HABILITA_TERMINOS_Y_CONDICIONES);
                            isTerminosYCondicionesRequired = "1".equals(param114_habilita_terminos.getValue()) ||
                                    "3".equals(param114_habilita_terminos.getValue());
                            setVisibilityTermsAndConditions();
                            changeLabelsFromParams(listParam.get(Globales.ParametrosDeApp.PARAM_94_REGISTRAR_CHOFER_CAMPOS_OPCIONALES), false);

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

    private void changeLabelsFromParams(paramEntity param94, boolean mustChange)
    {
        if(mustChange)
        {
            inputLayoutNombre.setHint("Nombre");
            inputLayoutApellido.setHint("Apellido");
            inputLayoutDNI.setHint("INE o IFE");
            inputLayoutEmail.setHint("Email");
            inputLayoutCurp.setHint("CURP");
            txtFotoPasajeroTitle.setText("Fotografía del usuario");
        }
    }
}
