package com.example.apptransport.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apreciasoft.mobile.asremis.Entity.NewDriverOptionalValueData;
import com.apreciasoft.mobile.asremis.Entity.NewDriverPersonalData;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.apreciasoft.mobile.asremis.viewmodels.NewDriverActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link NewDriverDatosPersonalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDriverDatosPersonalesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View view;
    private ListenerRegisterChofer listener;
    private Button btnNext;

    private EditText editTextNombre,editTextApellido,editTextTelefono,editTextEmail, editTextContrasena,
            editTextContrasena2,editTextNumChofer;
    private TextInputLayout txtNombreInputLayout,txtApellidoInputLayout,txtEmailInputLayout,txtNumChoferInputLayout;
    CountryCodePicker ccp;

    private String firstName, lastName, email, socialid;
    private View cardViewPasswords;

    private NewDriverActivityViewModel viewModel;
    private NewDriverOptionalValueData optionalDriverData=null;

    public NewDriverDatosPersonalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewDriverDatosPersonalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDriverDatosPersonalesFragment newInstance(String param1, String param2) {
        NewDriverDatosPersonalesFragment fragment = new NewDriverDatosPersonalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadParams(getArguments());
    }

    private void loadParams(Bundle b)
    {
        try{
            if(b != null)
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_new_driver_datos_personales, container, false);
        configureControls(view);
        setUserDataIfExists();
        initializeViewModel();
        initializeObserverParams();
        return view;
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
            //Nombre
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNamelabel())) {
                txtNombreInputLayout.setHint(optionalDriverData.getChofer().getNamelabel());
            }
            //Apellido
            if(!TextUtils.isEmpty(optionalDriverData.getChofer().getLastNamelabel()))
            {
                txtApellidoInputLayout.setHint(optionalDriverData.getChofer().getLastNamelabel());
            }

            //Email
            /*if(!TextUtils.isEmpty(optionalDriverData.getChofer().getNamelabel()))
            {
                txtEmailInputLayout.setHint(optionalDriverData.getChofer().getNamelabel());
            }*/

            //Nro Driver
            if (!TextUtils.isEmpty(optionalDriverData.getChofer().getNumberlabel())) {
                txtNumChoferInputLayout.setHint(optionalDriverData.getChofer().getNumberlabel());
            }
        }
    }

    private void configureControls(View view)
    {
        editTextNombre= view.findViewById(R.id.edittext_nombre);

        editTextApellido= view.findViewById(R.id.edittext_apellido);
        editTextTelefono= view.findViewById(R.id.edittext_telefono);
        editTextEmail= view.findViewById(R.id.edittext_email);
        editTextContrasena = view.findViewById(R.id.edittext_contraseña);
        editTextContrasena2 = view.findViewById(R.id.edittext_contraseña_2);
        editTextNumChofer= view.findViewById(R.id.edittext_num_chofer);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        ccp.registerPhoneNumberTextView(editTextTelefono);
        cardViewPasswords = view.findViewById(R.id.cardView2);

        txtNombreInputLayout = view.findViewById(R.id.txtNombreInputLayout);
        txtApellidoInputLayout = view.findViewById(R.id.txtApellidoInputLayout);
        txtEmailInputLayout = view.findViewById(R.id.txtEmailInputLayout);
        txtNumChoferInputLayout = view.findViewById(R.id.txtNumChoferInputLayout);

        setDefaultCountry();

        btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNextButton();
            }
        });
    }

    private void setUserDataIfExists()
    {
        if(!TextUtils.isEmpty(socialid))
        {
            editTextNombre.setText(firstName);
            editTextApellido.setText(lastName);
            editTextEmail.setText(email);
            editTextContrasena.setEnabled(false);
            editTextContrasena2.setEnabled(false);
            cardViewPasswords.setVisibility(View.GONE);
        }
    }

    private void setDefaultCountry() {
        String userCountry =  Utils.getUserCountry(view.getContext());
        if(!TextUtils.isEmpty(userCountry))
        {
            ccp.setCountryForNameCode(userCountry);
        }
    }

    private void btnNextButton()
    {
        if(validateFields())
        {
            NewDriverPersonalData personalData = getPersonalData();
            listener.savePersonalData(personalData);
            listener.btnSiguiente(0);
        }
    }

    private NewDriverPersonalData getPersonalData(){
        NewDriverPersonalData result=new NewDriverPersonalData();
        result.nombre = editTextNombre.getText().toString().trim();
        result.apellido =  editTextApellido.getText().toString().trim();
        result.telefono = ccp.getFullNumberWithPlus();
        result.email = editTextEmail.getText().toString().trim();
        result.password =  editTextContrasena.getText().toString().trim();
        result.nroChofer = editTextNumChofer.getText().toString().trim();
        result.socialId = socialid;
        return result;
    }

    private boolean validateFields(){
        boolean result=false;
        NewDriverOptionalValueData optionalData = Utils.getOptionalDataValidation(getContext());

        String nombre = editTextNombre.getText().toString().trim();
        String apellido= editTextApellido.getText().toString().trim();
        String telefono= editTextTelefono.getText().toString().trim();
        String email= editTextEmail.getText().toString().trim();
        String numChofer= editTextNumChofer.getText().toString().trim();
        String contrasena= editTextContrasena.getText().toString().trim();
        String contrasena2= editTextContrasena2.getText().toString().trim();

        if (!optionalData.getChofer().isNombre() && nombre.isEmpty()){
            editTextNombre.setError(getString(R.string.completar_campo));
            editTextNombre.requestFocus();
        }else if (!optionalData.getChofer().isApellido() && apellido.isEmpty()){
            editTextApellido.setError(getString(R.string.completar_campo));
            editTextApellido.requestFocus();
        }else if (!optionalData.getChofer().isTelefono() &&  telefono.isEmpty()){
            editTextTelefono.setError(getString(R.string.completar_campo));
            editTextTelefono.requestFocus();
        }else if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (!Utils.validationEmail(email)){
            editTextEmail.setError(getString(R.string.email_invalido));
            editTextEmail.requestFocus();
        }else if (!optionalData.getChofer().isNumero() &&  numChofer.isEmpty()){
            editTextNumChofer.setError(getString(R.string.completar_campo));
            editTextNumChofer.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && contrasena.isEmpty()){
            editTextContrasena.setError(getString(R.string.completar_campo));
            editTextContrasena.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && contrasena2.isEmpty()){
            editTextContrasena2.setError(getString(R.string.completar_campo));
            editTextContrasena2.requestFocus();
        }else if (TextUtils.isEmpty(socialid) && !contrasena.equals(contrasena2)){
            editTextContrasena2.setError(getString(R.string.contraeseña_diferente));
            editTextContrasena2.requestFocus();
        }else {
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


}
